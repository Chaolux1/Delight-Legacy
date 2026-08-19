package net.chaolux.delightlegacy.legacy.assets;

import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class ClassPathResourceProvider implements ResourceProvider {
    private final ClassLoader classLoader;
    public ClassPathResourceProvider(Class<?> classPath) {
        if(classPath == null) throw new IllegalArgumentException("Class cannot be null");
        ClassLoader loader=classPath.getClassLoader();
        this.classLoader=loader != null ? loader : ClassLoader.getSystemClassLoader();
    }

    public ClassPathResourceProvider(ClassLoader classLoader) {
        if(classLoader == null) throw new IllegalArgumentException("Class loader cannot be null");
        this.classLoader=classLoader;
    }

    @Override
    public boolean is(LegacyPackType legacyPackType, ResourceLocation resourceLocation) {
        return classLoader.getResource(ResourcePaths.path(legacyPackType,resourceLocation)) != null;
    }

    @Override
    public InputStream inputStream(LegacyPackType legacyPackType,ResourceLocation resourceLocation) throws IOException {
        String string=ResourcePaths.path(legacyPackType,resourceLocation);
        InputStream inputStream=classLoader.getResourceAsStream(string);
        if(inputStream == null) throw new FileNotFoundException("Resource not found: " + ResourceLocations.getString(resourceLocation) + " (" + string + ")");
        return inputStream;
    }

    @Override
    public Set<ResourceLocation> set(LegacyPackType legacyPackType,String string,String suffix) throws IOException {
        if(legacyPackType == null) throw new IllegalArgumentException("Pack type cannot be null");
        String normal=normalize(string);
        String end=suffix == null ? "" : suffix;
        LinkedHashSet<ResourceLocation> locations=new LinkedHashSet<ResourceLocation>();
        LinkedHashSet<String> linkedHashSet=new LinkedHashSet<String>();
        Enumeration<URL> enumeration=classLoader.getResources(legacyPackType.getString());
        while (enumeration.hasMoreElements()) {
            URL url=enumeration.nextElement();
            if("file".equals(url.getProtocol())) {
                File file=getFile(url);
                if(file != null && linkedHashSet.add("dir:" + file.getAbsolutePath())) scanPack(file,normal,end,locations);
            } else if("jar".equals(url.getProtocol())) {
                JarFile jarFile=((JarURLConnection) url.openConnection()).getJarFile();
                if(linkedHashSet.add("jar:" + jarFile.getName())) scanJar(jarFile,legacyPackType,normal,end,locations);
            }
        }
        ClassLoader loader=classLoader;
        while (loader != null) {
            if(loader instanceof URLClassLoader) {
                for(URL url : ((URLClassLoader) loader).getURLs()) {
                    scanURLClass(url,legacyPackType,normal,end,locations,linkedHashSet);
                }
            }
            loader=loader.getParent();
        }
        String classPath=System.getProperty("java.class.path");
        if(classPath != null && !classPath.isEmpty()) {
            String[] strings=classPath.split(Pattern.quote(File.pathSeparator));
            for (String entry : strings) {
                if(!entry.isEmpty()) {
                    try {
                        scanClassFile(new File(entry),legacyPackType,normal,end,locations,linkedHashSet);
                    } catch (IOException exception) {

                    }
                }
            }
        }
        return Collections.unmodifiableSet(locations);
    }

    private void scanURLClass(URL url,LegacyPackType legacyPackType,String string,String suffix,Set<ResourceLocation> set,Set<String> strings) throws IOException {
        if(url == null) return;
        if("file".equals(url.getProtocol())) {
            File file=getFile(url);
            if(file != null) scanClassFile(file,legacyPackType,string,suffix,set,strings);
        } else if("jar".equals(url.getProtocol())) {
            JarFile jarFile=((JarURLConnection) url.openConnection()).getJarFile();
            if(strings.add("jar:" + jarFile.getName())) scanJar(jarFile,legacyPackType,string,suffix,set);
        }
    }

    private void scanClassFile(File file,LegacyPackType legacyPackType,String string,String suffix,Set<ResourceLocation> set,Set<String> strings) throws IOException {
        if(file == null || !file.exists()) return;
        if(file.isDirectory()) {
            File pack=new File(file,legacyPackType.getString());
            if(pack.isDirectory() && strings.add("dir:" + pack.getAbsolutePath())) scanPack(pack,string,suffix,set);
            return;
        }
        String name=file.getName().toLowerCase();
        if(!name.endsWith(".jar") && !name.endsWith(".zip")) return;
        if(!strings.add("jar:" + file.getAbsolutePath())) return;
        try(JarFile jarFile=new JarFile(file)) {
            scanJar(jarFile,legacyPackType,string,suffix,set);
        }
    }

    private void scanPack(File file,String string,String suffix,Set<ResourceLocation> set) {
        File[] files=file.listFiles();
        if(files == null) return;
        for (File namespace : files) {
            if(!namespace.isDirectory()) continue;
            String namespaceString=namespace.getName();
            if(!ResourceLocations.isValidNamespace(namespaceString)) continue;
            File findFile=string.isEmpty() ? namespace : new File(namespace,string);
            if(findFile.isDirectory()) scanNamespace(namespaceString,namespace,findFile,suffix,set);
        }
    }

    private void scanNamespace(String string,File file, File files,String suffix,Set<ResourceLocation> set) {
        File[] children=files.listFiles();
        if(children == null) return;
        for (File child : children) {
            if(child.isDirectory()) {
                scanNamespace(string,file,child,suffix,set);
            } else if (child.isFile() && (suffix.isEmpty() || child.getName().endsWith(suffix))) {
                String path=file.toURI().relativize(child.toURI()).getPath();
                if(path != null && !path.isEmpty()) {
                    try {
                        set.add(ResourceLocations.locations(string,path));
                    } catch (IllegalArgumentException exception) {

                    }
                }
            }
        }
    }

    private void scanJar(JarFile jarFile,LegacyPackType legacyPackType,String string,String suffix,Set<ResourceLocation> set) {
        String path= legacyPackType.getString() + "/";
        String prefix=string.isEmpty() ? "" : string + "/";
        Enumeration<JarEntry> enumeration=jarFile.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry=enumeration.nextElement();
            if(jarEntry.isDirectory()) continue;
            String name=jarEntry.getName();
            if(!name.startsWith(path) || (!suffix.isEmpty() && !name.endsWith(suffix))) continue;
            String nameString=name.substring(path.length());
            int value=nameString.indexOf('/');
            if(value <= 0) continue;
            String namespace=nameString.substring(0,value);
            String namespaceString=nameString.substring(value + 1);
            if(!ResourceLocations.isValidNamespace(namespace)) continue;
            if(!prefix.isEmpty() && !namespaceString.startsWith(prefix)) continue;
            try {
                set.add(ResourceLocations.locations(namespace,namespaceString));
            } catch (IllegalArgumentException exception) {

            }
        }
    }

    private static String normalize(String string) {
        if(string == null || string.isEmpty()) return "";
        String path=string.replace('\\','/');
        while (path.startsWith("/")) {
            path=path.substring(1);
        }
        while (path.endsWith("/")) {
            path=path.substring(0,path.length() - 1);
        }
        if(path.isEmpty()) return "";
        if(!ResourceLocations.isValidPath(path) || path.contains("../") || path.equals("..")) throw new IllegalArgumentException("Invalid resource: " + string);
        return path;
    }

    private static File getFile(URL url) {
        try {
            return new File(url.toURI());
        } catch (URISyntaxException exception) {
            return null;
        }
    }
}

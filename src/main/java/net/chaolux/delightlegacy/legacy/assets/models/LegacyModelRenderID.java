package net.chaolux.delightlegacy.legacy.assets.models;

public class LegacyModelRenderID {
    private static int modelRenderID=0;
    public static int getModelRenderID() {
        return modelRenderID;
    }

    public static void setModelRenderID(int modelID) {
        modelRenderID=modelID;
    }
}

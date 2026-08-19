package net.chaolux.delightlegacy.legacy.assets;

public enum LegacyPackType {
    ASSETS("assets"), DATA("data");
    public final String string;
    LegacyPackType(String string) {
        this.string=string;
    }

    public String getString() {
        return string;
    }
}

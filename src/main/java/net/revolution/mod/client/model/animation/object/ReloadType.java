package net.revolution.mod.client.model.animation.object;

public enum ReloadType {
    Unload(0),
    Load(1),
    Full(2);

    public final int i;

    ReloadType(int i) {
        this.i = i;
    }

    public static ReloadType getTypeFromInt(int i) {
        for(ReloadType type : values()) {
            if(type.i == i) {
                return type;
            }
        }
        return null;
    }
}

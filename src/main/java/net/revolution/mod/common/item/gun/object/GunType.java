package net.revolution.mod.common.item.gun.object;

import java.util.Objects;

public enum GunType {
    RIFLE("rifle"),
    SHOTGUN("shotgun"),
    HANDGUN("handgun"),
    REVOLVER("revolver"),
    PISTOL("pistol"),
    MACHINE_GUN("machine_gun");

    public final String type;

    GunType(String type) {
        this.type = type;
    }

    public static GunType getTypeFromString(String type) {
        for(GunType stringType : values()) {
            if(Objects.equals(stringType.type, type)) {
                return stringType;
            }
        }
        return null;
    }
}

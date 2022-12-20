package net.revolution.mod.client.model.animation.registry;

import net.revolution.mod.client.model.animation.WeaponAnimation;
import net.revolution.mod.utils.ModReference;

import java.util.HashMap;

public class WeaponAnimations {

    private static final HashMap<String, WeaponAnimation> animationMap = new HashMap<>();

    /**
     * Weapon Animations
     */
    public static String PISTOL = "pistol";

    public static String registerAnimation(String internalName, WeaponAnimation animation) {
        animationMap.put(internalName, animation);
        ModReference.LOGGER.info("The animation " + internalName + " has been registered!");

        return internalName;
    }

    public static WeaponAnimation getAnimation(String internalName) {
        WeaponAnimation weaponAnimation = animationMap.get(internalName);

        if(weaponAnimation == null)
            ModReference.LOGGER.warn(String.format("Animation named '%s' does not exist in animation registry.", internalName));

        return animationMap.get(internalName);
    }

}

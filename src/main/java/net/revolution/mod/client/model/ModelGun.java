package net.revolution.mod.client.model;

import com.mojang.math.Vector3f;

public abstract class ModelGun {

    /**
     * Changes the gun model scale.
     */
    public abstract Vector3f gunScale();

    /**
     * How much of the gun will translate back while holding shift.
     */
    public abstract float shiftGunPullBack();

    /**
     * Allows you to change how the gun is held without effecting the sight alignment.
     */
    public abstract Vector3f translateHipPosition();
    public abstract Vector3f rotateHipPosition();

    /**
     * Allows you to change how the gun is held while aiming.
     */
    public abstract Vector3f translateAimPosition();
    public abstract Vector3f rotateAimPosition();

    /**
     * Allows you to change how the gun is held while running.
     */
    public abstract Vector3f sprintTranslate();
    public abstract Vector3f sprintRotate();

    /**
     * Allows you to change the static arm scale.
     */
    public abstract Vector3f staticArmScale();

    /**
     * Allows you to change the static arm hip location.
     */
    public abstract Vector3f staticArmHipTranslate();
    public abstract Vector3f staticArmHipRotate();

    /**
     * Allows you to change the static arm scale.
     */
    public abstract Vector3f movingArmScale();

    /**
     * Allows you to change the moving arm hip location.
     */
    public abstract Vector3f movingArmHipTranslate();
    public abstract Vector3f movingArmHipRotate();

    /**
     * Allows you to change the static arm aiming location.
     */
    public abstract Vector3f staticArmAimingTranslate();
    public abstract Vector3f staticArmAimingRotate();

    /**
     * Allows you to change the moving arm aiming location.
     */
    public abstract Vector3f movingArmAimingTranslate();
    public abstract Vector3f movingArmAimingRotate();

}

package net.revolution.mod.client.model.guns;

import com.mojang.math.Vector3f;
import net.revolution.mod.client.model.ModelGun;

public class ModelCal45 extends ModelGun {

    @Override
    public Vector3f gunScale() {
        return new Vector3f(0.4F, 0.4F, 0.4F);
    }

    @Override
    public float shiftGunPullBack() {
        return 0.1F;
    }

    @Override
    public Vector3f translateHipPosition() {
        return new Vector3f(0.8F, -0.F, 1.1F);
    }

    @Override
    public Vector3f rotateHipPosition() {
        return new Vector3f(-100F, -70F, -100F);
    }

    @Override
    public Vector3f translateAimPosition() {
        return new Vector3f(1.22F, 0.436F, 0.0F);
    }

    @Override
    public Vector3f rotateAimPosition() {
        return new Vector3f(-6.0F, 3.8F, 0.0F);
    }

    @Override
    public Vector3f sprintTranslate() {
        return new Vector3f(0.0F, 0.0F, 0.0F);
    }

    @Override
    public Vector3f sprintRotate() {
        return new Vector3f(-60.0F, 0.0F, 0.0F);
    }

    @Override
    public Vector3f staticArmScale() {
        return new Vector3f(2.0F, 2.0F, 2.0F);
    }

    @Override
    public Vector3f staticArmHipTranslate() {
        return new Vector3f(0.35F, -0.1F, -0.6F);
    }

    @Override
    public Vector3f staticArmHipRotate() {
        return new Vector3f(90.0F, 0.0F, -5.0F);
    }

    @Override
    public Vector3f movingArmScale() {
        return new Vector3f(2.0F, 2.0F, 2.0F);
    }

    @Override
    public Vector3f movingArmHipTranslate() {
        return new Vector3f(0.2F, -0.15F, -0.6F);
    }

    @Override
    public Vector3f movingArmHipRotate() {
        return new Vector3f(90.0F, 0.0F, 40.0F);
    }

    @Override
    public Vector3f staticArmAimingTranslate() {
        return new Vector3f(0.0F, 0.0F, 0.0F);
    }

    @Override
    public Vector3f staticArmAimingRotate() {
        return new Vector3f(0.0F, 0.0F, 0.0F);
    }

    @Override
    public Vector3f movingArmAimingTranslate() {
        return new Vector3f(0.0F, 0.0F, 0.0F);
    }

    @Override
    public Vector3f movingArmAimingRotate() {
        return new Vector3f(0.0F, 0.0F, 0.0F);
    }

}

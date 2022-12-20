package net.revolution.mod.client.model.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.revolution.mod.client.model.ModelGun;
import net.revolution.mod.client.model.animation.object.ReloadType;
import net.revolution.mod.client.model.animation.object.StateEntry;
import net.revolution.mod.client.model.animation.object.StateType;

import java.util.ArrayList;

public class WeaponAnimation {

    public Vector3f ammoLoadOffset;

    public void onGunAnimation(PoseStack poseStack, float reloadRotate, AnimationState animation)
    {

    }

    public void onAmmoAnimation(PoseStack poseStack, ModelGun gunModel, float ammoPosition, int reloadAmmoCount, AnimationState animation)
    {

    }

    public ArrayList<StateEntry> getReloadStates(ReloadType reloadType, int reloadCount)
    {
        ArrayList<StateEntry> states = new ArrayList<StateEntry>();
        states.add(new StateEntry(StateType.Tilt, 0.15f, 0f, StateEntry.MathType.Add));
        if(reloadType == ReloadType.Unload || reloadType == ReloadType.Full)
            states.add(new StateEntry(StateType.Unload, 0.35f, 0f, StateEntry.MathType.Add));
        if(reloadType == ReloadType.Load || reloadType == ReloadType.Full)
            states.add(new StateEntry(StateType.Load, 0.35f, 1f, StateEntry.MathType.Sub, reloadCount));
        states.add(new StateEntry(StateType.Untilt, 0.15f, 1f, StateEntry.MathType.Sub));
        return states;
    }

    public ArrayList<StateEntry> getShootStates(ModelGun gunModel) {
        ArrayList<StateEntry> states = new ArrayList<StateEntry>();

        // TODO
        //if(gunModel.pumpModel.length > 0) {
            //states.add(new StateEntry(StateType.PumpOut, 0.5f, 1f, StateEntry.MathType.Sub));
            //states.add(new StateEntry(StateType.PumpIn, 0.5f, 0f, StateEntry.MathType.Add));
        //}

        return states;
    }

}

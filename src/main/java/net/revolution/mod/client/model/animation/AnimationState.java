package net.revolution.mod.client.model.animation;

import net.revolution.mod.client.model.animation.object.ReloadType;
import net.revolution.mod.client.model.animation.object.StateEntry;
import net.revolution.mod.client.model.animation.object.StateType;
import net.revolution.mod.client.model.animation.registry.WeaponAnimations;

import java.util.ArrayList;

public class AnimationState {

    // Reloading Animation State
    private ArrayList<StateEntry> reloadStateEntries;
    private StateEntry currentReloadState;
    public float reloadProgress = 0f;
    private int reloadStateIndex = 0;
    public boolean reloading = false;
    public boolean tiltHold = false;
    private ReloadType reloadType;
    private float reloadTime;

    // Shot Animation State
    public boolean shooting = false;
    private float shootTime;
    private float shootProgress = 0f;
    private ArrayList<StateEntry> shootStateEntries;
    private StateEntry currentShootState;
    private int shootStateIndex = 0;

    // Recoil
    public float gunRecoil = 0F, lastGunRecoil = 0F;

    // Slide
    public float gunSlide = 0F, lastGunSlide = 0F;

    // Hammer
    public float hammerRotation = 0F;
    public int timeUntilPullback = 0;
    public float gunPullback = -1F, lastGunPullback = -1F;
    public boolean isFired = false;

    // Muzzle Flash
    public int muzzleFlashTime = 0;
    public int flashInt = 0;

    // Misc
    public boolean isGunEmpty = false;

    public void triggerReload(int reloadTime, int reloadCount, ReloadType reloadType) {
        ArrayList<StateEntry> animEntries = WeaponAnimations.getAnimation("pistol").getReloadStates(reloadType, reloadCount);
        reloadStateEntries = adjustTiming(animEntries);

        this.reloadTime = reloadType != ReloadType.Full ? reloadTime*0.65f : reloadTime;
        this.reloadType = reloadType;
        this.reloading = true;
    }

    // Internal Methods
    private ArrayList<StateEntry> adjustTiming(ArrayList<StateEntry> animEntries) {
        float currentTiming = 0f;
        float dividedAmount = 0f;
        float cutOffTime = 0f;

        for(StateEntry entry : animEntries)
            currentTiming += entry.stateTime;

        if(currentTiming < 1f)
            dividedAmount = (1f-currentTiming) / animEntries.size();

        if(dividedAmount > 0f) {
            for(StateEntry entry : animEntries) {
                entry.stateTime += dividedAmount;
            }
        }

        for(StateEntry entry : animEntries) {
            cutOffTime += entry.stateTime;
            entry.cutOffTime += cutOffTime;
        }

        return animEntries;
    }

    public void onRenderTickUpdate() {
        if(reloading && currentReloadState != null)
            currentReloadState.onTick(reloadTime);

        if(shooting && currentShootState != null)
            currentShootState.onTick(shootTime);
    }

    public void onTickUpdate() {
        if(reloading) {
            if(currentReloadState == null)
                currentReloadState = reloadStateEntries.get(0);

            if(currentReloadState.stateType == StateType.Tilt)
                tiltHold = true;
            if(currentReloadState.stateType == StateType.Untilt)
                tiltHold = false;

            if(reloadProgress >= currentReloadState.cutOffTime)
            {
                if(reloadStateIndex+1 < reloadStateEntries.size())
                {
                    reloadStateIndex++;
                    currentReloadState.finished = true;
                    currentReloadState = reloadStateEntries.get(reloadStateIndex);
                }
            }

            reloadProgress += 1F / reloadTime;
            if(reloadProgress >= 0.9F)
                isGunEmpty = false;
            if(reloadProgress >= 1F) {
                reloading = false;
                reloadProgress = 0f;
                reloadStateEntries = null;
                currentReloadState = null;
                reloadStateIndex = 0;
                reloadType = null;
            }
        }

        if(shooting) {
            if(currentShootState == null)
                currentShootState = shootStateEntries.get(0);

            if(shootProgress >= currentShootState.cutOffTime)
            {
                if(shootStateIndex+1 < shootStateEntries.size())
                {
                    shootStateIndex++;
                    currentShootState.finished = true;
                    currentShootState = shootStateEntries.get(shootStateIndex);
                }
            }

            shootProgress += 1F / shootTime;

            if(shootProgress >= 1F) {
                shooting = false;
                shootProgress = 0f;
                shootStateEntries = null;
                currentShootState = null;
                shootStateIndex = 0;
            }
        }

        // Slide model
        lastGunSlide = gunSlide;
        if (isGunEmpty)
            lastGunSlide = gunSlide = 0.5F;
        if (!isGunEmpty && gunSlide > 0.9) // Add one extra frame to slide
            gunSlide -= 0.1F;
        else if (gunSlide > 0 && !isGunEmpty)
            gunSlide *= 0.5F;


        // Recoil
        lastGunRecoil = gunRecoil;
        if (gunRecoil > 0)
            gunRecoil *= 0.5F;

        // Time until hammer pullback
        if (isFired) {
            gunPullback += 2F / 4;
            if (gunPullback >= 0.999F)
                isFired = false;
        }

        if (timeUntilPullback > 0) {
            timeUntilPullback--;
            if (timeUntilPullback == 0) {
                // Reset the hammer
                isFired = true;
                lastGunPullback = gunPullback = -1F;
            }
        } else {
            hammerRotation *= 0.6F;
        }

        if(muzzleFlashTime > 0)
            muzzleFlashTime--;
    }

    public boolean isReloadState(StateType stateType) {
        return currentReloadState != null && currentReloadState.stateType == stateType;
    }
}

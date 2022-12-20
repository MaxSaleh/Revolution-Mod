package net.revolution.mod.client.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.revolution.mod.client.model.animation.AnimationState;
import net.revolution.mod.client.model.animation.object.StateEntry;
import net.revolution.mod.client.renderer.RenderGun;
import net.revolution.mod.common.item.gun.GunItem;
import net.revolution.mod.utils.ModReference;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID, value = Dist.CLIENT)
public class ClientRenderHooks {

    public static HashMap<LivingEntity, AnimationState> weaponAnimations = new HashMap<>();
    private static final Minecraft minecraft = Minecraft.getInstance();

    @SubscribeEvent
    public static void test(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getItemStack().getItem() instanceof GunItem) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.PASS);
        }
    }

    /**
     * Vanilla Cross Hair Fix When Gun Is Aiming.
     */
    @SubscribeEvent
    public static void crossHairFix(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type() && RenderGun.adsSwitch == 1.0F) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            RenderGun.smoothing = event.renderTickTime;

            float renderTick = event.renderTickTime;
            renderTick *= 60d / (double) minecraft.getFrameTime();
            StateEntry.smoothing = renderTick;
            onRenderTickStart(minecraft, renderTick);
        }
    }

    /**
     * Render Tick Start Event
     */
    public static void onRenderTickStart(Minecraft minecraft, float renderTick) {
        Player player = minecraft.player;

        if (minecraft.player == null || minecraft.level == null)
            return;

        if(player.getMainHandItem().getItem() instanceof GunItem) {
            AnimationState anim = ClientRenderHooks.getAnimMachine(player);

            float adsSpeed = (0.15f + 0.02F) * renderTick;
            boolean aimChargeMisc = !anim.reloading;
            float value = Minecraft.getInstance().isWindowActive() && minecraft.mouseHandler.isRightPressed() && aimChargeMisc ? RenderGun.adsSwitch + adsSpeed : RenderGun.adsSwitch - adsSpeed;

            // Gun Aiming
            RenderGun.adsSwitch = Math.max(0, Math.min(1, value));

            float sprintSpeed = 0.15f * renderTick;
            float sprintValue = player.isSprinting() ? RenderGun.sprintSwitch + sprintSpeed : RenderGun.sprintSwitch - sprintSpeed;

            // Sprinting
            RenderGun.sprintSwitch = Math.max(0, Math.min(1, sprintValue));

            float reloadSpeed = 0.15f * renderTick;
            float reloadValue = anim.reloading ? RenderGun.reloadSwitch - reloadSpeed : RenderGun.reloadSwitch + reloadSpeed;

            // Reloading
            RenderGun.reloadSwitch = Math.max(0, Math.min(1, reloadValue));

            float crouchSpeed = 0.15f * renderTick;
            float crouchValue = player.isCrouching() ? RenderGun.crouchSwitch + crouchSpeed : RenderGun.crouchSwitch - crouchSpeed;

            // Crouching
            RenderGun.crouchSwitch = Math.max(0, Math.min(1, crouchValue));;


            for(AnimationState stateMachine : ClientRenderHooks.weaponAnimations.values()) {
                stateMachine.onRenderTickUpdate();
            }
        } else {
            RenderGun.resetRenderMods();
        }
    }

    public static AnimationState getAnimMachine(Player entityPlayer) {
        AnimationState animation;
        if (weaponAnimations.containsKey(entityPlayer)) {
            animation = weaponAnimations.get(entityPlayer);
        } else {
            animation = new AnimationState();
            weaponAnimations.put(entityPlayer, animation);
        }
        return animation;
    }

    /**
     * Client Tick For The Gun
     */
    @SubscribeEvent
    public static void clientTickGun(TickEvent.ClientTickEvent event) {
        switch (event.phase) {
            //case START -> onClientTickStart(mc);
            case END -> onClientTickEnd(minecraft);
        }
    }

    /**
     * Client Tick End Method
     */
    public static void onClientTickEnd(Minecraft minecraft) {
        if (minecraft.player == null || minecraft.level == null)
            return;

        for(AnimationState stateMachine : ClientRenderHooks.weaponAnimations.values()) {
            stateMachine.onTickUpdate();
        }
    }

    /**
     * Third Person Player Model Render Changes To The Player.
     */
    @SubscribeEvent
    public static void renderThirdPersonWeapon(RenderLivingEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {

            // Gets the Player's Model Parts To Be Modified
            PlayerRenderer playerrenderer = (PlayerRenderer) event.getRenderer();
            HumanoidModel<AbstractClientPlayer> humanoidModel = playerrenderer.getModel();

            // Modifies The Players Hands Positions When Sprinting Or Not
            if (player.isSprinting())
                humanoidModel.rightArmPose = HumanoidModel.ArmPose.CROSSBOW_CHARGE;
            else
                humanoidModel.rightArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;

            // Modifies The Players Head If They Are Aiming Or Not
            if (RenderGun.adsSwitch == 1.0F)
                humanoidModel.head.zRot = -0.4F;
            else
                humanoidModel.head.zRot = 0.0F;
        }
    }
}

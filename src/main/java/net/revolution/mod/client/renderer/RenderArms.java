package net.revolution.mod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.revolution.mod.common.item.gun.GunItem;
import net.revolution.mod.common.item.gun.object.GunType;

public class RenderArms {

    private static final Minecraft minecraft = Minecraft.getInstance();

    /**
     * Renders the static (left or right) arms within the first person depending on which hand is not reloading.
     */
    public static void renderStaticArm(PoseStack poseStack, GunItem gunItem, boolean leftHandReload) {
        if (minecraft.player == null)
            return;

        minecraft.getTextureManager().bindForSetup(minecraft.player.getSkinTextureLocation());
        MultiBufferSource multiBufferSource = minecraft.renderBuffers().bufferSource();
        int packedLight = minecraft.getEntityRenderDispatcher().getPackedLightCoords(minecraft.player, minecraft.getFrameTime());
        PlayerRenderer playerrenderer = (PlayerRenderer) minecraft.getEntityRenderDispatcher().getRenderer(minecraft.player);

        if (leftHandReload)
            playerrenderer.renderRightHand(poseStack, multiBufferSource, packedLight, minecraft.player); // Right Arm
        else
            playerrenderer.renderLeftHand(poseStack, multiBufferSource, packedLight, minecraft.player); // Left Arm
    }

    /**
     * Renders the moving (left or right) arms within the first person depending on which hand is reloading.
     */
    public static void renderMovingArm(PoseStack poseStack, GunItem gunItem, boolean leftHandReload) {
        if (minecraft.player == null)
            return;

        minecraft.getTextureManager().bindForSetup(minecraft.player.getSkinTextureLocation());
        MultiBufferSource multiBufferSource = minecraft.renderBuffers().bufferSource();
        int packedLight = minecraft.getEntityRenderDispatcher().getPackedLightCoords(minecraft.player, minecraft.getFrameTime());
        PlayerRenderer playerrenderer = (PlayerRenderer) minecraft.getEntityRenderDispatcher().getRenderer(minecraft.player);

        // Removes the left arm if the gun is a pistol while running
        if (gunItem.gunType == GunType.PISTOL) {
            if (RenderGun.sprintSwitch == 0.0F) {
                if (leftHandReload)
                    playerrenderer.renderLeftHand(poseStack, multiBufferSource, packedLight, minecraft.player); // Left Arm
                else
                    playerrenderer.renderRightHand(poseStack, multiBufferSource, packedLight, minecraft.player); // Right Arm
            } else {
                if (RenderGun.adsSwitch == 1.0F) {
                    if (leftHandReload)
                        playerrenderer.renderLeftHand(poseStack, multiBufferSource, packedLight, minecraft.player); // Left Arm
                    else
                        playerrenderer.renderRightHand(poseStack, multiBufferSource, packedLight, minecraft.player); // Right Arm
                }
            }
        } else {
            if (leftHandReload)
                playerrenderer.renderLeftHand(poseStack, multiBufferSource, packedLight, minecraft.player); // Left Arm
            else
                playerrenderer.renderRightHand(poseStack, multiBufferSource, packedLight, minecraft.player); // Right Arm
        }
    }

}

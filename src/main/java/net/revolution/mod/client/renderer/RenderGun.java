package net.revolution.mod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.revolution.mod.client.handlers.ClientRenderHooks;
import net.revolution.mod.client.model.animation.AnimationState;
import net.revolution.mod.common.item.gun.GunItem;
import net.revolution.mod.utils.ModReference;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderGun extends BlockEntityWithoutLevelRenderer {
    private final Minecraft minecraft = Minecraft.getInstance();
    public static final RenderGun render = new RenderGun();
    public static float smoothing;
    private String gunName;

    // Checker values
    public static float adsSwitch = 0F; // Aiming
    public static float sprintSwitch = 0f; // Sprinting
    public static float reloadSwitch = 1F; // Reload
    public static float swayVertical = 0f; // Swing up or down
    public static float swayHorizontal = 0f; // Swing right or left
    public static float crouchSwitch = 0f; // Crouching
    public static Float swayVerticalEP;
    public static Float swayHorizontalEP;

    public static float crouchZoomGun = -0.2F; // TODO move this to the model gun class

    // Extra
    public double prevLookAngle;

    public RenderGun() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(@NotNull ItemStack itemStack, ItemTransforms.@NotNull TransformType transformType, @NotNull PoseStack poseStack,
                             @NotNull MultiBufferSource multiBufferSource, int combinedLightIn, int combinedOverlayIn) {
        ItemRenderer renderer = this.minecraft.getItemRenderer();
        Player player = (Player) this.minecraft.getCameraEntity();
        AbstractClientPlayer clientPlayer = this.minecraft.player;
        AnimationState animationState = ClientRenderHooks.getAnimMachine(player);
        this.minecraft.options.bobView().set(false); // TODO need to set it to true when gun is not rendered

        if (player == null || clientPlayer == null)
            return;
        
        // Gets the gun item
        GunItem gunItem = null;
        if (itemStack.getItem() instanceof GunItem item)
            gunItem = item;
        if (gunItem == null)
            return;

        // Pop off the transformations applied by ItemRenderer before calling this
        poseStack.popPose();
        poseStack.pushPose();

        // Guns Base JSON Model's
        BakedModel gunBaseModel = Minecraft.getInstance().getModelManager().getModel(
                new ResourceLocation(ModReference.MOD_ID, "item/" + this.getGunName() + "_base"));
        gunBaseModel = gunBaseModel.applyTransform(transformType, poseStack, isLeftHand(transformType));
        BakedModel gunBulletModel = Minecraft.getInstance().getModelManager().getModel(
                new ResourceLocation(ModReference.MOD_ID, "item/" + this.getGunName() + "_bullet"));
        BakedModel gunHammerModel = Minecraft.getInstance().getModelManager().getModel(
                new ResourceLocation(ModReference.MOD_ID, "item/" + this.getGunName() + "_hammer"));
        BakedModel gunMagModel = Minecraft.getInstance().getModelManager().getModel(
                new ResourceLocation(ModReference.MOD_ID, "item/" + this.getGunName() + "_mag"));
        BakedModel gunSlideModel = Minecraft.getInstance().getModelManager().getModel(
                new ResourceLocation(ModReference.MOD_ID, "item/" + this.getGunName() + "_slide"));
        BakedModel gunTriggerModel = Minecraft.getInstance().getModelManager().getModel(
                new ResourceLocation(ModReference.MOD_ID, "item/" + this.getGunName() + "_trigger"));

        // First Person Gun Render
        if (transformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND ||
                transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
            poseStack.pushPose();
            {
                // Bob View
                float f = player.walkDist - player.walkDistO;
                float f1 = -(player.walkDist + f * 0.5F);
                float f2 = Mth.lerp(0.5F, player.oBob, player.bob);

                // Gun Swing // TODO Start working and fixing this feature
                {
                    if (RenderGun.adsSwitch == 0.0F && RenderGun.sprintSwitch == 0.0F) {
                        Camera camera = this.minecraft.gameRenderer.getMainCamera();
                        // Camera Right
                        if (camera.getYRot() > prevLookAngle) {
                            //poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), -3F, true));
                        }
                        // Camera Left
                        else if (camera.getYRot() < prevLookAngle) {
                            //poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), 3F, true));
                        }
                        prevLookAngle = camera.getYRot();
                    }
                }

                // Holding Shift While Holding Gun Feature
                if (player.isShiftKeyDown())
                    if (RenderGun.adsSwitch == 0.0F)
                        poseStack.translate(gunItem.modelGun.shiftGunPullBack(), 0.0F, 0.0F);


                // Gun Scale
                poseStack.scale(gunItem.modelGun.gunScale().x(), gunItem.modelGun.gunScale().y(), gunItem.modelGun.gunScale().z());

                // Gun Hip Translation
                poseStack.translate(gunItem.modelGun.translateHipPosition().x(), gunItem.modelGun.translateHipPosition().y(),
                        gunItem.modelGun.translateHipPosition().z());
                if (RenderGun.adsSwitch != 1.0F) {
                    poseStack.translate(Mth.sin(f1 * (float)Math.PI) * f2 * 0.5F,
                            -Math.abs(Mth.cos(f1 * (float)Math.PI) * f2), 0.0D);
                }

                // Gun Hip Rotations
                poseStack.mulPose(new Quaternion(new Vector3f(1F, 0F, 0F),
                        gunItem.modelGun.rotateHipPosition().x(), true));
                poseStack.mulPose(new Quaternion(new Vector3f(0F, 1F, 0F),
                        gunItem.modelGun.rotateHipPosition().y(), true));
                poseStack.mulPose(new Quaternion(new Vector3f(0F, 0F, 1F),
                        gunItem.modelGun.rotateHipPosition().z(), true));
                if (RenderGun.adsSwitch != 1.0F) {
                    poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(f1 * (float)Math.PI) * f2 * 3.0F));
                    poseStack.mulPose(Vector3f.XP.rotationDegrees(Math.abs(Mth.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F));
                }

                // Gun Sprinting
                if (RenderGun.sprintSwitch == 1.0F && RenderGun.adsSwitch == 0.0F) {

                    // Gun Translation
                    poseStack.translate(gunItem.modelGun.sprintTranslate().x(), gunItem.modelGun.sprintTranslate().y(),
                            gunItem.modelGun.sprintTranslate().z());

                    // Gun Rotation
                    poseStack.mulPose(new Quaternion(new Vector3f(1F, 0F, 0F),
                            gunItem.modelGun.sprintRotate().x(), true));
                    poseStack.mulPose(new Quaternion(new Vector3f(0F, 1F, 0F),
                            gunItem.modelGun.sprintRotate().y(), true));
                    poseStack.mulPose(new Quaternion(new Vector3f(0F, 0F, 1F),
                            gunItem.modelGun.sprintRotate().z(), true));
                }

                // Gun Aiming
                if (RenderGun.adsSwitch == 1.0F) {

                    // Gun Translation
                    poseStack.translate(gunItem.modelGun.translateAimPosition().x(), gunItem.modelGun.translateAimPosition().y(),
                            gunItem.modelGun.translateAimPosition().z());

                    // Gun Rotation
                    poseStack.mulPose(new Quaternion(new Vector3f(1F, 0F, 0F),
                            gunItem.modelGun.rotateAimPosition().x(), true));
                    poseStack.mulPose(new Quaternion(new Vector3f(0F, 1F, 0F),
                            gunItem.modelGun.rotateAimPosition().y(), true));
                    poseStack.mulPose(new Quaternion(new Vector3f(0F, 0F, 1F),
                            gunItem.modelGun.rotateAimPosition().z(), true));
                }

                // Renders Gun Base Model
                this.renderModel(poseStack, itemStack, gunBaseModel, multiBufferSource, renderer, combinedLightIn, combinedOverlayIn);

                // Renders Gun Slide Model
                this.renderModel(poseStack, itemStack, gunSlideModel, multiBufferSource, renderer, combinedLightIn, combinedOverlayIn);

                // Static Arm Rendering
                poseStack.pushPose();
                {
                    // Static Arm Scale
                    poseStack.scale(gunItem.modelGun.staticArmScale().x(), gunItem.modelGun.staticArmScale().y(),
                            gunItem.modelGun.staticArmScale().z());

                    // Static Arm Translations
                    poseStack.translate(gunItem.modelGun.staticArmHipTranslate().x(), gunItem.modelGun.staticArmHipTranslate().y(),
                            gunItem.modelGun.staticArmHipTranslate().z());

                    // Static Arm Rotations
                    poseStack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F),
                            gunItem.modelGun.staticArmHipRotate().x(), true));
                    poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F),
                            gunItem.modelGun.staticArmHipRotate().y(), true));
                    poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, 1.0F),
                            gunItem.modelGun.staticArmHipRotate().z(), true));

                    // Renders The Left Or Right Static Arm
                    RenderArms.renderStaticArm(poseStack, gunItem, true);
                }
                poseStack.popPose();

                // Moving Arm Rendering
                poseStack.pushPose();
                {
                    // Moving Arm Scale
                    poseStack.scale(gunItem.modelGun.movingArmScale().x(), gunItem.modelGun.movingArmScale().y(),
                            gunItem.modelGun.movingArmScale().z());

                    // Moving Arm Translations
                    poseStack.translate(gunItem.modelGun.movingArmHipTranslate().x(), gunItem.modelGun.movingArmHipTranslate().y(),
                            gunItem.modelGun.movingArmHipTranslate().z());

                    // Moving Arm Rotations
                    poseStack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F),
                            gunItem.modelGun.movingArmHipRotate().x(), true));
                    poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F),
                            gunItem.modelGun.movingArmHipRotate().y(), true));
                    poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, 1.0F),
                            gunItem.modelGun.movingArmHipRotate().z(), true));

                    // Renders The Left Or Right Moving Arm
                    RenderArms.renderMovingArm(poseStack, gunItem, true);
                }
                poseStack.popPose();
            }
            poseStack.popPose();
        }

        // Third Person Gun Render
        else if (transformType == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND ||
                transformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND) {
            poseStack.pushPose();
            {
                // Gun Scale
                poseStack.scale(0.4F, 0.4F, 0.4F);

                // Gun Translations
                poseStack.translate(0.0F, -0.05F, 0.25F);

                // Gun Rotations
                poseStack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), 0.0F, true));
                poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F),  180.0F, true));
                poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, 1.0F),  0.0F, true));

                // Renders Gun Base Model
                this.renderModel(poseStack, itemStack, gunBaseModel, multiBufferSource, renderer, combinedLightIn, combinedOverlayIn);
            }
            poseStack.popPose();
        }

        // Ground Entity Gun Renderer
        else if (transformType == ItemTransforms.TransformType.GROUND) {
            poseStack.pushPose();
            {
                // Gun Scale
                poseStack.scale(0.3F, 0.3F, 0.3F);

                // Renders Gun Base Model
                this.renderModel(poseStack, itemStack, gunBaseModel, multiBufferSource, renderer, combinedLightIn, combinedOverlayIn);
            }
            poseStack.popPose();
        }

        // GUI Slot Gun Renderer
        else if (transformType == ItemTransforms.TransformType.GUI) {
            poseStack.pushPose();
            {
                // Gun Scale
                poseStack.scale(0.7F, 0.7F, 0.7F);

                // Gun Translations
                poseStack.translate(-0.2F, 0.0F, 0.0F);

                // Gun Rotations
                poseStack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), 20.0F, true));
                poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F),  50.0F, true));
                poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, 1.0F),  -30.0F, true));

                // Renders Gun Base Model
                this.renderModel(poseStack, itemStack, gunBaseModel, multiBufferSource, renderer, combinedLightIn, combinedOverlayIn);
            }
            poseStack.popPose();
        }

        if (animationState.gunRecoil > 0.1F && player.isSprinting()) {
            swayHorizontal = 0f;
            swayVertical = 0f;
            swayHorizontalEP = 0f;
            swayVerticalEP = 0f;
            reloadSwitch = 0f;
            sprintSwitch = 0f;
        }
    }

    private void renderModel(PoseStack poseStack, ItemStack itemStack, BakedModel bakedModel, MultiBufferSource multiBufferSource,
                             ItemRenderer renderer, int combinedLightIn, int combinedOverlayIn) {
        boolean glint = itemStack.hasFoil();
        for (RenderType type : bakedModel.getRenderTypes(itemStack, true)) {
            type = RenderTypeHelper.getEntityRenderType(type, true);
            VertexConsumer consumer = ItemRenderer.getFoilBuffer(multiBufferSource, type, true, glint);
            renderer.renderModelLists(bakedModel, itemStack, combinedLightIn, combinedOverlayIn, poseStack, consumer);
        }
    }

    private boolean isLeftHand(ItemTransforms.TransformType type) {
        return type == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND || type == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
    }

    public void setGunName(String name) {
        this.gunName = name;
    }

    public String getGunName() {
        return this.gunName;
    }

    // Resets render modifiers
    public static void resetRenderMods() {
        RenderGun.swayHorizontal = 0f;
        RenderGun.swayVertical = 0f;
        RenderGun.swayHorizontalEP = 0f;
        RenderGun.swayVerticalEP = 0f;
        RenderGun.reloadSwitch = 0f;
        RenderGun.sprintSwitch = 0f;
        RenderGun.adsSwitch = 0f;
        RenderGun.crouchSwitch = 0f;
    }

    @SubscribeEvent
    public static void registerGunRenders(ModelEvent.RegisterAdditional event) {
        registerAllModels("cal_45", event);
    }

    private static void registerAllModels(String gunName, ModelEvent.RegisterAdditional event) {
        event.register(new ResourceLocation(ModReference.MOD_ID, "item/" + gunName + "_base"));
        event.register(new ResourceLocation(ModReference.MOD_ID, "item/" + gunName + "_bullet"));
        event.register(new ResourceLocation(ModReference.MOD_ID, "item/" + gunName + "_hammer"));
        event.register(new ResourceLocation(ModReference.MOD_ID, "item/" + gunName + "_mag"));
        event.register(new ResourceLocation(ModReference.MOD_ID, "item/" + gunName + "_slide"));
        event.register(new ResourceLocation(ModReference.MOD_ID, "item/" + gunName + "_trigger"));
    }
}

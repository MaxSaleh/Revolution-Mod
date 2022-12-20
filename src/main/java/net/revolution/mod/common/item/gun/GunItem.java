package net.revolution.mod.common.item.gun;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.revolution.mod.client.model.ModelGun;
import net.revolution.mod.client.renderer.RenderGun;
import net.revolution.mod.common.init.ModCreativeTabs;
import net.revolution.mod.common.item.gun.object.GunType;

import java.util.function.Consumer;

public class GunItem extends Item {

    public String gunName;
    public ModelGun modelGun;
    public GunType gunType;

    public GunItem(String gunName, ModelGun modelGun, GunType gunType) {
        super(new Item.Properties()
                .stacksTo(1)
                .setNoRepair()
                .tab(ModCreativeTabs.REVOLUTION_GUNS));
        this.gunName = gunName;
        this.modelGun = modelGun;
        this.gunType = gunType;

        RenderGun.render.setGunName(gunName);
    }

    /**
     * Sets the gun render to the item.
     */
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return RenderGun.render;
            }
        });
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem() || slotChanged;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }
}

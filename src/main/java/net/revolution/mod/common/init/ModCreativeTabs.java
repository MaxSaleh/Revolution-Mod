package net.revolution.mod.common.init;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModCreativeTabs {

    /**
     * Creative Tab For The Guns
     */
    public static final CreativeModeTab REVOLUTION_GUNS = (new CreativeModeTab("revolution.guns") {
        @Override
        public @NotNull Component getDisplayName() {
            return Component.literal(ChatFormatting.BLUE + super.getDisplayName().getString());
        }

        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.M9.get());
        }
    });

    /**
     * Creative Tab For The Guns
     */
    public static final CreativeModeTab REVOLUTION_AMMO = (new CreativeModeTab("revolution.ammo") {
        @Override
        public @NotNull Component getDisplayName() {
            return Component.literal(ChatFormatting.RED + super.getDisplayName().getString());
        }

        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.M9.get());
        }
    });

}

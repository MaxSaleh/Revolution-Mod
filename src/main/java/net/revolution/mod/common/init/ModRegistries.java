package net.revolution.mod.common.init;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ModRegistries {

    /**
     * Registers all the mods' information.
     */
    public static void register() {
        final IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Mods Stuff
        ModItems.ITEMS.register(iEventBus);

        // Event Handlers
        //MinecraftForge.EVENT_BUS.register(RenderGunOld.class);
    }

}

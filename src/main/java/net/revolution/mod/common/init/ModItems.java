package net.revolution.mod.common.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.revolution.mod.client.model.ModelGun;
import net.revolution.mod.client.model.guns.ModelCal45;
import net.revolution.mod.common.item.gun.GunItem;
import net.revolution.mod.common.item.gun.object.GunType;
import net.revolution.mod.utils.ModReference;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModReference.MOD_ID);

    /**
     * Gun Items
     */
    public static final RegistryObject<Item> M9 = ITEMS.register("cal_45", () -> new GunItem("cal_45", new ModelCal45(), GunType.PISTOL));

}

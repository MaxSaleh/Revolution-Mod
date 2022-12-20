package net.revolution.mod;

import net.minecraftforge.fml.common.Mod;
import net.revolution.mod.common.init.ModRegistries;
import net.revolution.mod.utils.ModReference;

@Mod(ModReference.MOD_ID)
public class RevolutionMod {

    public RevolutionMod() {
        ModRegistries.register();
    }

}

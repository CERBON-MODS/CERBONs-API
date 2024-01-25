package com.cerbon.cerbons_api.forge;

import com.cerbon.cerbons_api.CerbonsAPI;
import com.cerbon.cerbons_api.util.Constants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class CerbonsAPIForge {

    public CerbonsAPIForge() {
        CerbonsAPI.init();
        MinecraftForge.EVENT_BUS.register(this);
    }
}
package com.cerbon.cerbons_api;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CerbonsApi.MOD_ID)
public class CerbonsApi {
    public static final String MOD_ID = "cerbons_api";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CerbonsApi() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}

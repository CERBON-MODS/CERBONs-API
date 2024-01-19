package com.cerbon.cerbons_api.neoforge;

import com.cerbon.cerbons_api.CerbonsAPI;
import com.cerbon.cerbons_api.util.Constants;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class CerbonsAPINeoForge {

    public CerbonsAPINeoForge() {
        CerbonsAPI.init();
    }
}

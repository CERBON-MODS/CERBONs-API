package com.cerbon.cerbons_api.api.static_utilities;

import net.minecraftforge.fml.ModList;

public class MiscUtils {

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}

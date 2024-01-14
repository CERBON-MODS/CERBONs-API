package com.cerbon.cerbons_api.api.static_utilities;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import com.cerbon.cerbons_api.platform.Services;
import net.minecraft.world.level.Level;

public class CapabilityUtils {

    public static EventScheduler getLevelEventScheduler(Level level) {
        return Services.PLATFORM_CAPABILITY.getLevelEventScheduler(level);
    }
}

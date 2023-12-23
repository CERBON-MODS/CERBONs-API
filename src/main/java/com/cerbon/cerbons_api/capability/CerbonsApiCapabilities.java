package com.cerbon.cerbons_api.capability;

import com.cerbon.cerbons_api.capability.providers.LevelEventSchedulerProvider;
import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import net.minecraft.world.level.Level;

public class CerbonsApiCapabilities {

    public static EventScheduler getLevelEventScheduler(Level level) {
        return level.getCapability(LevelEventSchedulerProvider.EVENT_SCHEDULER).resolve().orElseThrow();
    }
}

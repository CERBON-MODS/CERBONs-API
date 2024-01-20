package com.cerbon.cerbons_api.neoforge.platform;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import com.cerbon.cerbons_api.neoforge.attachment.saved_data.LevelEventScheduler;
import com.cerbon.cerbons_api.platform.services.ICapabilityHelper;
import net.minecraft.world.level.Level;

public class NeoForgeCapabilityHelper implements ICapabilityHelper {

    @Override
    public EventScheduler getLevelEventScheduler(Level level) {
        return LevelEventScheduler.get(level);
    }
}

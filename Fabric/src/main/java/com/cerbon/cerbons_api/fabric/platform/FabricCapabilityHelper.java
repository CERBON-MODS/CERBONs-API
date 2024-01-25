package com.cerbon.cerbons_api.fabric.platform;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import com.cerbon.cerbons_api.fabric.cardinalComponents.Components;
import com.cerbon.cerbons_api.platform.services.ICapabilityHelper;
import net.minecraft.world.level.Level;

public class FabricCapabilityHelper implements ICapabilityHelper {

    @Override
    public EventScheduler getLevelEventScheduler(Level level) {
        return Components.getLevelEventScheduler(level);
    }
}

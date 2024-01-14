package com.cerbon.cerbons_api.fabric.cardinalComponents;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class LevelEventScheduler implements ILevelEventSchedulerComponent, ServerTickingComponent, ClientTickingComponent {
    private final EventScheduler eventScheduler = new EventScheduler();

    public LevelEventScheduler(Level level) {}

    @Override
    public EventScheduler get() {
        return eventScheduler;
    }

    @Override
    public void clientTick() {
        eventScheduler.updateEvents();
    }

    @Override
    public void serverTick() {
        eventScheduler.updateEvents();
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        // NO-OP
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        // NO-OP
    }
}

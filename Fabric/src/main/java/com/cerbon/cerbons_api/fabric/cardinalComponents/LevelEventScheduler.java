package com.cerbon.cerbons_api.fabric.cardinalComponents;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

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
    public void readData(ValueInput readView) {
        // NO-OP
    }

    @Override
    public void writeData(ValueOutput writeView) {
        // NO-OP
    }
}

package com.cerbon.cerbons_api.forge.capability;

import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import com.cerbon.cerbons_api.api.static_utilities.CapabilityUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class provides a capability, which is attached to {@link Level}. This allows you to add events to the
 * level's event scheduler and execute code at a future time.
 * <p>
 * See also: {@link CapabilityUtils}
 */
@AutoRegisterCapability
public class LevelEventSchedulerProvider implements ICapabilityProvider {
    public static final Capability<EventScheduler> EVENT_SCHEDULER = CapabilityManager.get(new CapabilityToken<>() {});

    private EventScheduler eventScheduler;
    private final LazyOptional<EventScheduler> optional = LazyOptional.of(this::createEventScheduler);

    private EventScheduler createEventScheduler() {
        if (this.eventScheduler == null)
            this.eventScheduler = new EventScheduler();

        return this.eventScheduler;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return EVENT_SCHEDULER.orEmpty(cap, optional);
    }
}
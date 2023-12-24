package com.cerbon.cerbons_api.capability.providers;

import com.cerbon.cerbons_api.api.general.data.HistoricalData;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class MoveHistoryProvider implements ICapabilityProvider {
    public static final Capability<MoveHistory> HISTORICAL_DATA = CapabilityManager.get(new CapabilityToken<>() {});

    private MoveHistory positionalHistory;
    private final LazyOptional<MoveHistory> optional = LazyOptional.of(this::createHistoricalData);

    private MoveHistory createHistoricalData() {
        if(this.positionalHistory == null)
            this.positionalHistory = new MoveHistory(Vec3.ZERO, 10);

        return this.positionalHistory;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return HISTORICAL_DATA.orEmpty(cap, optional);
    }

    public static class MoveHistory extends HistoricalData<Vec3> {

        public MoveHistory(Vec3 initialValue, int maxHistory) {
            super(initialValue, maxHistory);
        }
    }
}

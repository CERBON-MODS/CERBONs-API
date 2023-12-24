package com.cerbon.cerbons_api.capability.providers;

import com.cerbon.cerbons_api.api.general.data.HistoricalData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class PlayerBlockPosHistoryProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<PlayerBlockPosHistory> HISTORICAL_DATA = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerBlockPosHistory positionalHistory;
    private final LazyOptional<PlayerBlockPosHistory> optional = LazyOptional.of(this::createHistoricalData);

    private PlayerBlockPosHistory createHistoricalData() {
        if(this.positionalHistory == null)
            this.positionalHistory = new PlayerBlockPosHistory(new BlockPos(0, 0, 0), 10);

        return this.positionalHistory;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (positionalHistory == null) return tag;
        tag.putLongArray("LastBlocksPos", positionalHistory.stream().mapToLong(BlockPos::asLong).toArray());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (!tag.contains("LastBlocksPos")) return;
        List<BlockPos> blocksPos = Arrays.stream(tag.getLongArray("LastBlocksPos")).mapToObj(BlockPos::of).toList();

        this.positionalHistory = new PlayerBlockPosHistory(new BlockPos(0, 0, 0), 10);
        this.positionalHistory.remove(0);
        this.positionalHistory.addAll(blocksPos);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return HISTORICAL_DATA.orEmpty(cap, optional);
    }

    public static class PlayerBlockPosHistory extends HistoricalData<BlockPos> {

        public PlayerBlockPosHistory(BlockPos initialValue, int maxHistory) {
            super(initialValue, maxHistory);
        }
    }
}

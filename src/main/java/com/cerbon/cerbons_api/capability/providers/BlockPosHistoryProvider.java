package com.cerbon.cerbons_api.capability.providers;

import com.cerbon.cerbons_api.api.general.data.HistoricalData;
import com.cerbon.cerbons_api.api.static_utilities.CapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * This class provides a capability for storing block positions.
 * To use this capability you need to attach it to your object.
 * <p>
 * Never use it with vanilla objects.
 * <p>
 * See also: {@link AttachCapabilitiesEvent}, {@link CapabilityUtils}
 */
@AutoRegisterCapability
public class BlockPosHistoryProvider implements ICapabilitySerializable<CompoundTag> {
    private final BlockPos initialValue;
    private final int maxHistory;
    private final boolean persistData;

    private BlockPosHistory positionalHistory;
    private final LazyOptional<BlockPosHistory> optional = LazyOptional.of(this::createHistoricalData);

    public static final Capability<BlockPosHistory> HISTORICAL_DATA = CapabilityManager.get(new CapabilityToken<>() {});

    public BlockPosHistoryProvider(BlockPos initialValue, int maxHistory, boolean persistData) {
        this.initialValue = initialValue;
        this.maxHistory = maxHistory;
        this.persistData = persistData;
    }

    public BlockPosHistoryProvider(int maxHistory, boolean persistData) {
        this(new BlockPos(0, 0, 0), maxHistory, persistData);
    }

    private BlockPosHistory createHistoricalData() {
        if(this.positionalHistory == null)
            this.positionalHistory = new BlockPosHistory(initialValue, maxHistory);

        return this.positionalHistory;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (positionalHistory == null || !persistData) return tag;
        tag.putLongArray("LastBlocksPos", positionalHistory.stream().mapToLong(BlockPos::asLong).toArray());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (!tag.contains("LastBlocksPos") || !persistData) return;

        List<BlockPos> blocksPos = Arrays.stream(tag.getLongArray("LastBlocksPos")).mapToObj(BlockPos::of).toList();

        this.positionalHistory = new BlockPosHistory(new BlockPos(0, 0, 0), maxHistory);
        this.positionalHistory.remove(0);
        this.positionalHistory.addAll(blocksPos);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return HISTORICAL_DATA.orEmpty(cap, optional);
    }

    public static class BlockPosHistory extends HistoricalData<BlockPos> {

        public BlockPosHistory(BlockPos initialValue, int maxHistory) {
            super(initialValue, maxHistory);
        }
    }
}

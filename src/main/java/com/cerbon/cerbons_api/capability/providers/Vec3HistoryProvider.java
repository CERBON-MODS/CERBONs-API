package com.cerbon.cerbons_api.capability.providers;

import com.cerbon.cerbons_api.api.general.data.HistoricalData;
import com.cerbon.cerbons_api.api.static_utilities.CapabilityUtils;
import com.cerbon.cerbons_api.api.static_utilities.VecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * This class provides a capability for storing vec3 values.
 * To use this capability you need to attach it to your object.
 * <p>
 * Never use it with vanilla objects.
 * <p>
 * See also: {@link AttachCapabilitiesEvent}, {@link CapabilityUtils}
 */
@AutoRegisterCapability
public class Vec3HistoryProvider implements ICapabilitySerializable<CompoundTag> {
    private final Vec3 initialValue;
    private final int maxHistory;
    private final boolean persistData;

    private Vec3History positionalHistory;
    private final LazyOptional<Vec3History> optional = LazyOptional.of(this::createHistoricalData);

    public static final Capability<Vec3History> HISTORICAL_DATA = CapabilityManager.get(new CapabilityToken<>() {});

    public Vec3HistoryProvider(Vec3 initialValue, int maxHistory, boolean persistData) {
        this.initialValue = initialValue;
        this.maxHistory = maxHistory;
        this.persistData = persistData;
    }

    public Vec3HistoryProvider(int maxHistory, boolean persistData) {
        this(Vec3.ZERO, maxHistory, persistData);
    }

    private Vec3History createHistoricalData() {
        if(this.positionalHistory == null)
            this.positionalHistory = new Vec3History(initialValue, maxHistory);

        return this.positionalHistory;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (positionalHistory == null || !persistData) return tag;
        tag.putLongArray("LastVec3s", positionalHistory.stream().map(BlockPos::containing).mapToLong(BlockPos::asLong).toArray());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (!tag.contains("LastVec3s") || !persistData) return;

        List<Vec3> vec3s = Arrays.stream(tag.getLongArray("LastVec3s")).mapToObj(BlockPos::of).map(VecUtils::asVec3).toList();

        this.positionalHistory = new Vec3History(Vec3.ZERO, maxHistory);
        this.positionalHistory.remove(0);
        this.positionalHistory.addAll(vec3s);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return HISTORICAL_DATA.orEmpty(cap, optional);
    }

    public static class Vec3History extends HistoricalData<Vec3> {

        public Vec3History(Vec3 initialValue, int maxHistory) {
            super(initialValue, maxHistory);
        }
    }
}

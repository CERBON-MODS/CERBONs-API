package com.cerbon.cerbons_api.api.static_utilities;

import com.cerbon.cerbons_api.api.general.data.HistoricalData;
import com.cerbon.cerbons_api.capability.providers.BlockPosHistoryProvider;
import com.cerbon.cerbons_api.capability.providers.Vec3HistoryProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;

// If you want to use the methods from this class, you need to attach either a Vec3History or a BlockPosHistory capability to an entity.
public class CapabilityUtils {

    public static List<Vec3> getLastPositions(Entity entity) {
        return entity.getCapability(Vec3HistoryProvider.HISTORICAL_DATA)
                .map(HistoricalData::getAll)
                .orElse(Collections.emptyList());
    }

    public static List<BlockPos> getLastBlockPositions(Entity entity) {
        return entity.getCapability(BlockPosHistoryProvider.HISTORICAL_DATA)
                .map(HistoricalData::getAll)
                .orElse(Collections.emptyList());
    }

    public static BlockPos getLastBlockPos(Entity entity) {
        return entity.getCapability(BlockPosHistoryProvider.HISTORICAL_DATA)
                .map(data -> data.get(0))
                .orElse(new BlockPos(0, 0, 0));
    }
}

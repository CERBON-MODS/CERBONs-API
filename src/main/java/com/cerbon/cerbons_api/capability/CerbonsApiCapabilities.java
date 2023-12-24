package com.cerbon.cerbons_api.capability;

import com.cerbon.cerbons_api.api.general.data.HistoricalData;
import com.cerbon.cerbons_api.capability.providers.LevelEventSchedulerProvider;
import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import com.cerbon.cerbons_api.capability.providers.BlockPosHistoryProvider;
import com.cerbon.cerbons_api.capability.providers.MoveHistoryProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;

public class CerbonsApiCapabilities {

    public static EventScheduler getLevelEventScheduler(Level level) {
        return level.getCapability(LevelEventSchedulerProvider.EVENT_SCHEDULER).resolve().orElseThrow();
    }

    public static List<Vec3> getLastPositions(LivingEntity livingEntity) {
        return livingEntity.getCapability(MoveHistoryProvider.HISTORICAL_DATA)
                .map(HistoricalData::getAll)
                .orElse(Collections.emptyList());
    }

    public static List<BlockPos> getLastBlockPositions(LivingEntity livingEntity) {
        return livingEntity.getCapability(BlockPosHistoryProvider.HISTORICAL_DATA)
                .map(HistoricalData::getAll)
                .orElse(Collections.emptyList());
    }

    public static BlockPos getLastBlockPos(LivingEntity livingEntity) {
        return livingEntity.getCapability(BlockPosHistoryProvider.HISTORICAL_DATA)
                .map(data -> data.get(0))
                .orElse(new BlockPos(0, 0, 0));
    }
}

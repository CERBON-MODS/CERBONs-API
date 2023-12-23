package com.cerbon.cerbons_api.capability;

import com.cerbon.cerbons_api.api.general.data.HistoricalData;
import com.cerbon.cerbons_api.capability.providers.LevelEventSchedulerProvider;
import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import com.cerbon.cerbons_api.capability.providers.PlayerMoveHistoryProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;

public class CerbonsApiCapabilities {

    public static EventScheduler getLevelEventScheduler(Level level) {
        return level.getCapability(LevelEventSchedulerProvider.EVENT_SCHEDULER).resolve().orElseThrow();
    }

    public static List<Vec3> getPlayerPositions(ServerPlayer player){
        return player.getCapability(PlayerMoveHistoryProvider.HISTORICAL_DATA)
                .map(HistoricalData::getAll)
                .orElse(Collections.emptyList());
    }
}

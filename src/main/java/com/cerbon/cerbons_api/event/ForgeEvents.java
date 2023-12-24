package com.cerbon.cerbons_api.event;

import com.cerbon.cerbons_api.CerbonsApi;
import com.cerbon.cerbons_api.capability.providers.LevelEventSchedulerProvider;
import com.cerbon.cerbons_api.api.general.event.EventScheduler;
import com.cerbon.cerbons_api.capability.providers.PlayerBlockPosHistoryProvider;
import com.cerbon.cerbons_api.capability.providers.PlayerMoveHistoryProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CerbonsApi.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player)) return;

        if (!event.getObject().getCapability(PlayerMoveHistoryProvider.HISTORICAL_DATA).isPresent())
            event.addCapability(new ResourceLocation(CerbonsApi.MOD_ID, "player_move_history"), new PlayerMoveHistoryProvider());

        if (!event.getObject().getCapability(PlayerBlockPosHistoryProvider.HISTORICAL_DATA).isPresent())
            event.addCapability(new ResourceLocation(CerbonsApi.MOD_ID, "player_block_pos_history"), new PlayerBlockPosHistoryProvider());
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesLevel(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject() == null || event.getObject().getCapability(LevelEventSchedulerProvider.EVENT_SCHEDULER).isPresent()) return;
        event.addCapability(new ResourceLocation(CerbonsApi.MOD_ID, "event_scheduler"), new LevelEventSchedulerProvider());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) return;

        event.player.getCapability(PlayerMoveHistoryProvider.HISTORICAL_DATA).ifPresent(data -> {
            Vec3 previousPosition = data.get(0);
            Vec3 newPosition = event.player.position();

            // Extremely fast movement in one tick is a sign of teleportation or dimension hopping, and thus we should clear history to avoid undefined behavior
            if (previousPosition.distanceToSqr(newPosition) > 5)
                data.clear();

            data.add(newPosition);
        });

        event.player.getCapability(PlayerBlockPosHistoryProvider.HISTORICAL_DATA).ifPresent(data -> {
            BlockPos previousPosition = data.get(0);
            BlockPos newPosition = event.player.blockPosition();
            Level level = event.player.level();
            boolean isValidBlock = level.getBlockState(newPosition.below()).isRedstoneConductor(level, newPosition.below());

            if (newPosition != previousPosition && isValidBlock)
                data.add(newPosition);
        });
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level.getGameTime() % 2 == 0)
            event.level.getCapability(LevelEventSchedulerProvider.EVENT_SCHEDULER).ifPresent(EventScheduler::updateEvents);
    }
}

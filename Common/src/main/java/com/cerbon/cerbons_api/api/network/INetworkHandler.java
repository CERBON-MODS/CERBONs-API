package com.cerbon.cerbons_api.api.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface INetworkHandler {

    /**
     * Sends the packet to the server, if the server has the packet registered.
     *
     * @param packet the packet
     * @param <T>    The type
     */
    default <T> void sendToServer(T packet) {
        sendToServer(packet, false);
    }

    /**
     * Sends the packet to the server. Can ignore the check if the server has the packet registered.
     * Likely use case for this is talking to bukkit/spigot/paper servers.
     *
     * @param packet      the packet
     * @param ignoreCheck ignore the check if the server has the packet registered.
     * @param <T>         The type
     */
    <T> void sendToServer(T packet, boolean ignoreCheck);

    /**
     * Sends the packet to the client player, only if the player has the packet registered.
     *
     * @param packet the packet
     * @param player the player
     * @param ignoreCheck - ignore the check if the client has the packet registered.
     * @param <T>    The type
     */
    <T> void sendToClient(T packet, ServerPlayer player, boolean ignoreCheck);

    /**
     * Sends the packet to the client players, only if the players has the packet registered.
     *
     * @param packet  the packet
     * @param players the players
     * @param <T>     The type
     */
    default <T> void sendToClients(T packet, List<ServerPlayer> players) {
        for (ServerPlayer player : players)
            sendToClient(packet, player, false);
    }

    /**
     * Sends the packet to all the client players in the server, only if the players has the packet registered.
     *
     * @param packet the packet
     * @param server the server
     * @param <T>    The type
     */
    default <T> void sendToAllClients(T packet, MinecraftServer server) {
        sendToClients(packet, server.getPlayerList().getPlayers());
    }

    /**
     * Sends the packet to all the client players in the level, only if the players has the packet registered.
     *
     * @param packet the packet
     * @param level  the level
     * @param <T>    The type
     */
    default <T> void sendToClientsInLevel(T packet, ServerLevel level) {
        sendToClients(packet, level.players());
    }

    /**
     * Sends the packet to all the client players loading a chunk, only if the players has the packet registered.
     *
     * @param packet the packet
     * @param chunk  the chunk
     * @param <T>    The type
     */
    default <T> void sendToClientsLoadingChunk(T packet, LevelChunk chunk) {
        ServerChunkCache chunkCache = (ServerChunkCache) chunk.getLevel().getChunkSource();
        sendToClients(packet, chunkCache.chunkMap.getPlayers(chunk.getPos(), false));
    }


    /**
     * Sends the packet to all the client players loading a position, only if the players has the packet registered.
     *
     * @param packet the packet
     * @param level  the level
     * @param pos    the chunkpos
     * @param <T>    The type
     */
    default <T> void sendToClientsLoadingPos(T packet, ServerLevel level, ChunkPos pos) {
        sendToClientsLoadingChunk(packet, level.getChunk(pos.x, pos.z));
    }

    /**
     * Sends the packet to all the client players loading a position, only if the players has the packet registered.
     *
     * @param packet the packet
     * @param level  the level
     * @param pos    the blockpos
     * @param <T>    The type
     */
    default <T> void sendToClientsLoadingPos(T packet, ServerLevel level, BlockPos pos) {
        sendToClientsLoadingPos(packet, level, new ChunkPos(pos));
    }

    /**
     * Sends the packet to all the client players loading a position, only if the players has the packet registered.
     *
     * @param packet the packet
     * @param level  the level
     * @param pos    the vec3 pos
     * @param <T>    The type
     */
    default <T> void sendToClientsLoadingPos(T packet, ServerLevel level, Vec3 pos) {
        sendToClientsLoadingPos(packet, level, BlockPos.containing(pos));
    }

    /**
     * Sends the packet to all the client players in range of a position, only if the players has the packet registered.
     *
     * @param packet the packet
     * @param level  the level
     * @param pos    the blockpos
     * @param range  the range
     * @param <T>    The type
     */
    default <T> void sendToClientsInRange(T packet, ServerLevel level, BlockPos pos, double range) {
        for (ServerPlayer player : level.players())
            if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= range * range)
                sendToClient(packet, player, false);
    }
}

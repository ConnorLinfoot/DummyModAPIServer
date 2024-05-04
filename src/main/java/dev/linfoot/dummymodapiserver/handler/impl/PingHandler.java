package dev.linfoot.dummymodapiserver.handler.impl;

import dev.linfoot.dummymodapiserver.DummyModAPIServer;
import dev.linfoot.dummymodapiserver.handler.PacketHandler;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPingPacket;
import org.bukkit.entity.Player;

public class PingHandler implements PacketHandler<ServerboundPingPacket> {
    @Override
    public void handlePacket(DummyModAPIServer server, Player player, ServerboundPingPacket packet) {
        server.sendPacket(player, new ClientboundPingPacket("pong"));
    }
}

package dev.linfoot.dummymodapiserver.handler.impl;

import dev.linfoot.dummymodapiserver.DummyModAPIServer;
import dev.linfoot.dummymodapiserver.handler.PacketHandler;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundRegisterPacket;
import org.bukkit.entity.Player;

public class RegisterHandler implements PacketHandler<ServerboundRegisterPacket> {
    @Override
    public void handlePacket(DummyModAPIServer server, Player player, ServerboundRegisterPacket packet) {
        server.getLogger().info("Received register packet from " + player.getName() + " for packets " + packet.getSubscribedEvents());
        server.setSubscribedEvents(player, packet.getSubscribedEvents());
    }
}

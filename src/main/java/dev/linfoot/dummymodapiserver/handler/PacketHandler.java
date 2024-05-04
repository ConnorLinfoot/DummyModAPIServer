package dev.linfoot.dummymodapiserver.handler;

import dev.linfoot.dummymodapiserver.DummyModAPIServer;
import net.hypixel.modapi.packet.HypixelPacket;
import org.bukkit.entity.Player;

public interface PacketHandler<T extends HypixelPacket> {

    void handlePacket(DummyModAPIServer server, Player player, T packet);

}

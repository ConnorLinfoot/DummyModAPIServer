package dev.linfoot.dummymodapiserver.handler.impl;

import dev.linfoot.dummymodapiserver.DummyModAPIServer;
import dev.linfoot.dummymodapiserver.handler.PacketHandler;
import net.hypixel.data.region.Environment;
import net.hypixel.data.type.GameType;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundLocationPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundLocationPacket;
import org.bukkit.entity.Player;

public class LocationHandler implements PacketHandler<ServerboundLocationPacket> {
    @Override
    public void handlePacket(DummyModAPIServer server, Player player, ServerboundLocationPacket packet) {
        server.sendPacket(player, new ClientboundLocationPacket(
                Environment.TEST,
                "localhost",
                "local-server",
                GameType.HOUSING,
                null,
                null,
                null
        ));
    }
}

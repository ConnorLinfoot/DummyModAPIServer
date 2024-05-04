package dev.linfoot.dummymodapiserver.handler.impl;

import com.google.common.collect.ImmutableSet;
import dev.linfoot.dummymodapiserver.DummyModAPIServer;
import dev.linfoot.dummymodapiserver.handler.PacketHandler;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPartyInfoPacket;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PartyInfoHandler implements PacketHandler<ServerboundPartyInfoPacket> {
    @Override
    public void handlePacket(DummyModAPIServer server, Player player, ServerboundPartyInfoPacket packet) {
        server.sendPacket(player, new ClientboundPartyInfoPacket(
                true,
                player.getUniqueId(),
                ImmutableSet.of(
                        player.getUniqueId(),
                        UUID.randomUUID()
                )
        ));
    }
}

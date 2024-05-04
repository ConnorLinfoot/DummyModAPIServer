package dev.linfoot.dummymodapiserver.handler.impl;

import dev.linfoot.dummymodapiserver.DummyModAPIServer;
import dev.linfoot.dummymodapiserver.handler.PacketHandler;
import net.hypixel.data.rank.MonthlyPackageRank;
import net.hypixel.data.rank.PackageRank;
import net.hypixel.data.rank.PlayerRank;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPlayerInfoPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPlayerInfoPacket;
import org.bukkit.entity.Player;

public class PlayerInfoHandler implements PacketHandler<ServerboundPlayerInfoPacket> {
    @Override
    public void handlePacket(DummyModAPIServer server, Player player, ServerboundPlayerInfoPacket packet) {
        server.sendPacket(player, new ClientboundPlayerInfoPacket(
                PlayerRank.ADMIN,
                PackageRank.MVP_PLUS,
                MonthlyPackageRank.SUPERSTAR,
                null
        ));
    }
}

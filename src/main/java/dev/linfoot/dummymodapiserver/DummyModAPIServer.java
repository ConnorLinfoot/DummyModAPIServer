package dev.linfoot.dummymodapiserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class DummyModAPIServer extends JavaPlugin {

    @Override
    public void onEnable() {
        ServerboundPacketHandler serverboundPacketHandler = new ServerboundPacketHandler(this);

        for (String identifier : HypixelModAPI.getInstance().getRegistry().getIdentifiers()) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, identifier);
            Bukkit.getMessenger().registerIncomingPluginChannel(this, identifier, serverboundPacketHandler);
        }
    }

    @Override
    public void onDisable() {
    }

    public void sendPacket(Player player, ClientboundHypixelPacket packet) {
        ByteBuf buf = Unpooled.buffer();
        try {
            PacketSerializer serializer = new PacketSerializer(buf);

            serializer.writeBoolean(true); // Indicates a successful response packet
            packet.write(serializer);

            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);

            player.sendPluginMessage(this, packet.getIdentifier(), data);
        } finally {
            buf.release();
        }
    }

    public void sendError(Player player, String identifier, ErrorReason reason) {
        ByteBuf buf = Unpooled.buffer();
        try {
            PacketSerializer serializer = new PacketSerializer(buf);

            serializer.writeBoolean(false); // Indicates an error response packet
            serializer.writeVarInt(reason.getId());

            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);

            player.sendPluginMessage(this, identifier, data);
        } finally {
            buf.release();
        }
    }
}

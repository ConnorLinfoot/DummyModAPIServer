package dev.linfoot.dummymodapiserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypixel.data.type.GameType;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.error.ErrorReason;
import net.hypixel.modapi.packet.ClientboundHypixelPacket;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationEventPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.UUID;

public final class DummyModAPIServer extends JavaPlugin {
    private final Map<UUID, Map<String, Integer>> subscribedEvents = new HashMap<>();

    @Override
    public void onEnable() {
        ServerboundPacketHandler serverboundPacketHandler = new ServerboundPacketHandler(this);

        for (String identifier : HypixelModAPI.getInstance().getRegistry().getIdentifiers()) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, identifier);
            Bukkit.getMessenger().registerIncomingPluginChannel(this, identifier, serverboundPacketHandler);
        }

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        // For testing, we send the location event every 30 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    OptionalInt version = getSubscribedVersion(player, "hyevent:location");
                    if (!version.isPresent()) {
                        continue;
                    }

                    sendPacket(player, new ClientboundLocationEventPacket(
                            "local-server",
                            GameType.HOUSING,
                            null,
                            null,
                            null
                    ));
                }
            }
        }.runTaskTimer(this, 0, 20 * 30L);
    }

    @Override
    public void onDisable() {
    }

    public void setSubscribedEvents(Player player, Map<String, Integer> subscribedEvents) {
        if (subscribedEvents.isEmpty()) {
            this.subscribedEvents.remove(player.getUniqueId());
            return;
        }

        this.subscribedEvents.put(player.getUniqueId(), subscribedEvents);
    }

    public OptionalInt getSubscribedVersion(Player player, String event) {
        Map<String, Integer> subscribed = subscribedEvents.get(player.getUniqueId());
        if (subscribed == null) {
            return OptionalInt.empty();
        }

        Integer version = subscribed.get(event);
        if (version == null) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(version);
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

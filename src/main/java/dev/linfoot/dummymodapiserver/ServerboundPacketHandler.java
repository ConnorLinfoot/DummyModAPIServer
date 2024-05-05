package dev.linfoot.dummymodapiserver;

import dev.linfoot.dummymodapiserver.handler.PacketHandler;
import dev.linfoot.dummymodapiserver.handler.impl.*;
import io.netty.buffer.Unpooled;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.packet.impl.serverbound.*;
import net.hypixel.modapi.serializer.PacketSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

class ServerboundPacketHandler implements PluginMessageListener {
    private final Map<Class<? extends HypixelPacket>, PacketHandler> packetHandlers = new HashMap<>();

    private final DummyModAPIServer server;

    ServerboundPacketHandler(DummyModAPIServer server) {
        this.server = server;

        registerHandler(ServerboundPingPacket.class, new PingHandler());
        registerHandler(ServerboundLocationPacket.class, new LocationHandler());
        registerHandler(ServerboundPartyInfoPacket.class, new PartyInfoHandler());
        registerHandler(ServerboundPlayerInfoPacket.class, new PlayerInfoHandler());
        registerHandler(ServerboundRegisterPacket.class, new RegisterHandler());
    }

    private <T extends HypixelPacket> void registerHandler(Class<T> packetClass, PacketHandler<T> handler) {
        packetHandlers.put(packetClass, handler);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String identifier, @NotNull Player player, byte[] bytes) {
        PacketSerializer serializer = new PacketSerializer(Unpooled.wrappedBuffer(bytes));
        HypixelPacket serverboundPacket = HypixelModAPI.getInstance().getRegistry().createServerboundPacket(identifier, serializer);
        if (serverboundPacket == null) {
            server.getLogger().log(Level.WARNING, "Received unknown packet with identifier " + identifier);
            return;
        }

        PacketHandler handler = packetHandlers.get(serverboundPacket.getClass());
        if (handler == null) {
            server.getLogger().log(Level.WARNING, "No handler found for packet " + serverboundPacket.getClass().getSimpleName());
            return;
        }

        try {
            handler.handlePacket(server, player, serverboundPacket);
        } catch (Exception e) {
            server.getLogger().log(Level.WARNING, "An error occurred while handling packet " + serverboundPacket.getClass().getSimpleName(), e);
        }
    }
}

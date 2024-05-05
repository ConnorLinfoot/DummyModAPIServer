package dev.linfoot.dummymodapiserver;

import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundHelloEventPacket;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;

import java.util.Collections;

class PlayerListener implements Listener {

    private final DummyModAPIServer server;

    PlayerListener(DummyModAPIServer server) {
        this.server = server;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRegisterChannel(PlayerRegisterChannelEvent event) {
        // We have to send hello once the client registers it, sending it before will result in Bukkit blocking the packet
        if (!event.getChannel().equals("hyevent:hello")) {
            return;
        }

        event.getPlayer().sendMessage("Sending hello packet!");
        server.sendPacket(event.getPlayer(), new ClientboundHelloEventPacket());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        server.setSubscribedEvents(event.getPlayer(), Collections.emptyMap());
    }

}

package eu.ventura.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import eu.ventura.Pit;
import eu.ventura.event.PlayerVanishEvent;
import eu.ventura.model.PlayerModel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * author: ekkoree
 * created at: 1/27/2026
 */
public class VanishListener implements Listener {
    private final Map<UUID, BukkitRunnable> runnableMap = new HashMap<>();
    private final ProtocolManager protocolManager;

    public VanishListener() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    private boolean isVanished(Player player) {
        if (player == null) return false;
        if (player.hasMetadata("vanished")) return true;
        PlayerModel model = PlayerModel.getInstance(player);
        return model != null && model.vanished;
    }

    @EventHandler
    public void onVanish(PlayerVanishEvent event) {
        if (event.isState()) {
            hide(event.getPlayer());
        } else {
            show(event.getPlayer());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = PlayerModel.getInstance(player);

        Bukkit.getScheduler().runTask(Pit.instance, () -> {
            if (model.vanished) {
                hide(player);
            }
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (other.equals(player)) continue;
                if (isVanished(other)) {
                    hideFrom(other, player);
                }
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BukkitRunnable task = runnableMap.remove(player.getUniqueId());
        if (task != null) task.cancel();
    }

    private void hide(Player player) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (viewer.equals(player)) continue;
            hideFrom(player, viewer);
        }

        player.setAllowFlight(true);
        player.setFlying(true);
        player.setInvulnerable(true);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendActionBar(Component.text("You're hidden from everyone!").color(NamedTextColor.GREEN));
            }
        };

        task.runTaskTimerAsynchronously(Pit.instance, 0, 20);
        runnableMap.put(player.getUniqueId(), task);
        player.setMetadata("vanished", new FixedMetadataValue(Pit.instance, true));
    }

    private void hideFrom(Player target, Player viewer) {
        if (viewer.equals(target)) return;

        viewer.hidePlayer(Pit.instance, target);

        PacketContainer removePacket = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        removePacket.getUUIDLists().write(0, List.of(target.getUniqueId()));

        try {
            protocolManager.sendServerPacket(viewer, removePacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void show(Player player) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (viewer.equals(player)) continue;
            showTo(player, viewer);
        }

        player.setAllowFlight(false);
        player.setFlying(false);
        player.setInvulnerable(false);

        BukkitRunnable task = runnableMap.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }

        player.sendActionBar(Component.text("You're visible to players now!").color(NamedTextColor.RED));
        player.removeMetadata("vanished", Pit.instance);
    }

    private void showTo(Player target, Player viewer) {
        if (viewer.equals(target)) return;

        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(target);
        PlayerInfoData playerInfoData = new PlayerInfoData(
                target.getUniqueId(),
                0,
                true,
                EnumWrappers.NativeGameMode.fromBukkit(target.getGameMode()),
                profile,
                WrappedChatComponent.fromText(target.getName()),
                true,
                0,
                null
        );

        PacketContainer infoPacket = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        infoPacket.getPlayerInfoActions().write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER));
        infoPacket.getPlayerInfoDataLists().write(1, List.of(playerInfoData));

        try {
            protocolManager.sendServerPacket(viewer, infoPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewer.showPlayer(Pit.instance, target);
    }
}

package eu.ventura.listener;

import eu.ventura.Pit;
import eu.ventura.constants.Sounds;
import eu.ventura.menu.EnderChestGUI;
import eu.ventura.service.PitBlockService;
import eu.ventura.util.NBTHelper;
import eu.ventura.util.RegionHelper;
import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public class WorldListener implements Listener {
    private final Set<PitBlockModel> placedBlocks = new HashSet<>();

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null && block.getType() == Material.ENDER_CHEST && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            event.setCancelled(true);
            new EnderChestGUI(player, () -> player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.9f, 1.0f)).open();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("collision");

        if (team == null) {
            team = scoreboard.registerNewTeam("collision");
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.ALWAYS);
        }

        team.addEntry(player.getName());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onProjectile(ProjectileLaunchEvent event) {
        Location launchLoc = event.getEntity().getLocation();
        boolean launchedFromSpawn = RegionHelper.isInSpawn(launchLoc);

        if (launchedFromSpawn) {
            event.setCancelled(true);
            if (event.getEntity().getShooter() instanceof Player shooter) {
                Sounds.NO.play(shooter);
            }
            return;
        }

        if (event.getEntity() instanceof Arrow arrow) {
            Bukkit.getScheduler().runTaskLater(Pit.instance, () -> {
                if (arrow.isValid()) {
                    arrow.remove();
                }
            }, 100L);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("collision");

        if (team != null) {
            team.removeEntry(event.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItemInHand();

        if (itemInHand.getType() == Material.AIR) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (!NBTHelper.hasKey(itemInHand, "pit-block-item")) {
            event.setCancelled(true);
            return;
        }

        if (RegionHelper.isInSpawn(event.getBlockPlaced().getLocation()) || RegionHelper.isInMiddle(event.getBlockPlaced().getLocation())) {
            event.setCancelled(true);
            return;
        }

        Block placedBlock = event.getBlock();
        BlockState originalState = placedBlock.getState();

        PitBlockModel entry = new PitBlockModel(placedBlock, originalState);
        placedBlocks.add(entry);
        PitBlockService.saveBlock(placedBlock.getLocation(), 120);

        Bukkit.getScheduler().runTaskLater(Pit.instance, () -> {
            if (placedBlocks.contains(entry)) {
                placedBlock.setType(Material.AIR);
                PitBlockService.removeBlock(placedBlock.getLocation());
                placedBlocks.remove(entry);
            }
        }, 120 * 20L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(false);
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        PitBlockModel matched = null;
        for (PitBlockModel entry : placedBlocks) {
            if (entry.getBlock().getLocation().equals(block.getLocation())) {
                matched = entry;
                break;
            }
        }

        if (matched == null && !PitBlockService.isPitBlock(block.getLocation())) {
            event.setCancelled(true);
            return;
        }

        block.setType(Material.AIR);
        PitBlockService.removeBlock(block.getLocation());
        placedBlocks.remove(matched);
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    private static class PitBlockModel {
        @Getter
        private final Block block;
        private final BlockState originalState;

        public PitBlockModel(Block block, BlockState originalState) {
            this.block = block;
            this.originalState = originalState;
        }

        public Location getLocation() {
            return block.getLocation();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PitBlockModel that = (PitBlockModel) obj;
            return block.getLocation().equals(that.block.getLocation());
        }

        @Override
        public int hashCode() {
            return block.getLocation().hashCode();
        }
    }
}

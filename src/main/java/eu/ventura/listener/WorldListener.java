package eu.ventura.listener;

import eu.ventura.Pit;
import eu.ventura.util.NBTHelper;
import eu.ventura.util.RegionHelper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public class WorldListener implements Listener {
    private final Set<PitBlockModel> placedBlocks = new HashSet<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItemInHand();

        if (itemInHand.getType() == Material.AIR) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(false);
            return;
        }

        if (!NBTHelper.hasKey(itemInHand, "pit-block-item")) {
            event.setCancelled(true);
            return;
        }

        if (RegionHelper.isInSpawn(player)) {
            event.setCancelled(true);
            return;
        }

        Block placedBlock = event.getBlock();
        BlockState originalState = placedBlock.getState();

        PitBlockModel model = new PitBlockModel(placedBlock, originalState, 120);
        placedBlocks.add(model);

        Bukkit.getScheduler().runTaskLater(Pit.instance, () -> {
            if (placedBlocks.contains(model)) {
                placedBlock.setType(Material.AIR);
                placedBlocks.remove(model);
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
        for (PitBlockModel pitBlockModel : placedBlocks) {
            if (pitBlockModel.getBlock().getLocation().equals(block.getLocation())) {
                matched = pitBlockModel;
                break;
            }
        }

        if (matched == null) {
            event.setCancelled(true);
            return;
        }

        block.setType(Material.AIR);
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

    @Getter
    public static class PitBlockModel {
        private final Block block;
        private final BlockState originalState;
        private final long expireAfter;

        public PitBlockModel(Block block, BlockState originalState, long expireAfter) {
            this.block = block;
            this.originalState = originalState;
            this.expireAfter = expireAfter;
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

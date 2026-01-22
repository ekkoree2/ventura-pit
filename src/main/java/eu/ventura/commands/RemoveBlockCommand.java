package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import eu.ventura.Pit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
@CommandAlias("rmblock|purge")
@CommandPermission("rank.owner")
public class RemoveBlockCommand extends BaseCommand {
    @Default
    public void onRemoveBlock(Player player, @Optional Material blockType) {
        if (blockType == null) {
            return;
        }
        World world = player.getWorld();
        final Chunk[] chunks = world.getLoadedChunks();

        new BukkitRunnable(){
            int currentChunk = 0;

            public void run() {
                int i = 0;
                while (i < 10 && this.currentChunk < chunks.length) {
                    Chunk chunk = chunks[this.currentChunk];
                    for (int x = 0; x < 16; ++x) {
                        for (int y = 0; y < chunk.getWorld().getMaxHeight(); ++y) {
                            for (int z = 0; z < 16; ++z) {
                                if (chunk.getBlock(x, y, z).getType() != blockType) continue;
                                chunk.getBlock(x, y, z).setType(Material.AIR);
                            }
                        }
                    }
                    ++i;
                    ++this.currentChunk;
                }
                if (this.currentChunk >= chunks.length) {
                    this.cancel();
                }
            }
        }.runTaskTimer(Pit.instance, 0L, 1L);
    }
}

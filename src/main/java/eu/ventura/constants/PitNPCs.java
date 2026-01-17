package eu.ventura.constants;

import de.oliver.fancynpcs.api.NpcData;
import eu.ventura.menu.PermanentGUI;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@Getter
public enum PitNPCs {
    PERKS_NPC(PitHologram.PERKS_NPC, "EKKOREESIGMAMALE", p -> new PermanentGUI(p).open()),
    SHOP_NPC(PitHologram.SHOP_NPC, "groszus", null);

    private final PitHologram hologram;
    private final String skin;
    private final Consumer<Player> task;

    PitNPCs(PitHologram hologram, String skin, Consumer<Player> task) {
        this.hologram = hologram;
        this.skin = skin;
        this.task = task;
    }

    public NpcData getNpcData(Player player, Location location) {
        String uniqueId = name().toLowerCase() + "_" + player.getUniqueId().toString().substring(0, 8);
        NpcData data = new NpcData(uniqueId, player.getUniqueId(), location);
        data.setDisplayName(null);
        data.setSkin(skin);
        data.setType(EntityType.PLAYER);
        data.setShowInTab(false);
        data.setSpawnEntity(true);
        data.setCollidable(false);
        if (task != null) {
            data.setOnClick(task);
        }
        return data;
    }
}

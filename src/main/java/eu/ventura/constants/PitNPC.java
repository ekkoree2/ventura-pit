package eu.ventura.constants;

import de.oliver.fancynpcs.api.NpcData;
import eu.ventura.menu.PermanentGUI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public enum PitNPC {
    PERKS_NPC(PitHologram.PERKS_NPC, "EKKOREESIGMAMALE", p -> new PermanentGUI(p).open()),
    SHOP_NPC(PitHologram.SHOP_NPC, "groszus", null);

    private final PitHologram hologram;
    private final String skin;
    private final Consumer<Player> task;

    public NpcData getNpcData(Player player, Location location) {
        NpcData data = new NpcData(this.skin, player.getUniqueId(), location);
        data.setDisplayName(null);
        data.setSkin(skin);
        data.setType(EntityType.PLAYER);
        data.setShowInTab(false);
        data.setSpawnEntity(true);
        data.setCollidable(false);
        return data;
    }
}

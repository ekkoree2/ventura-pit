package eu.ventura.constants;

import eu.ventura.menu.PermanentGUI;
import eu.ventura.menu.PrestigeGUI;
import eu.ventura.menu.ShopGUI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
* author: ekkoree
* created at: 1/17/2026
  */
@RequiredArgsConstructor
@Getter
public enum PitNPC {
    PERKS_NPC(PitHologram.PERKS_NPC, "EKKOREESIGMAMALE", p -> new PermanentGUI(p).open(), 180),
    SHOP_NPC(PitHologram.SHOP_NPC, "groszus", p -> new ShopGUI(p).open(), 180),
    PRESTIGE(PitHologram.PRESTIGE_NPC, "L3KOZ", p -> new PrestigeGUI(p).open(), 0),

    ;

    private final PitHologram hologram;
    private final String skin;
    private final Consumer<Player> task;
    private final float yaw;
}

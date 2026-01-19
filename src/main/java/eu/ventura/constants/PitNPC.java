package eu.ventura.constants;

import eu.ventura.menu.PermanentGUI;
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
    SHOP_NPC(PitHologram.SHOP_NPC, "groszus", null, 180);

    private final PitHologram hologram;
    private final String skin;
    private final Consumer<Player> task;
    private final float yaw;
}

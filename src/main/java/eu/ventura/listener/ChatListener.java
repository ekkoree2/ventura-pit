package eu.ventura.listener;

import eu.ventura.model.PlayerModel;
import eu.ventura.service.PlayerService;
import eu.ventura.util.LevelUtil;
import eu.ventura.util.MathUtil;
import eu.ventura.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * author: ekkoree
 * created at: 1/20/2026
 */
public class ChatListener implements Listener {

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        String message = event.getMessage();
        message = message.replace("%", "%%");

        PlayerModel playerModel = PlayerService.getPlayer(player);

        int prestige = playerModel.getPrestige();
        int level = playerModel.getLevel();

        String bracketsColor = LevelUtil.getBracketsColorChat(prestige).toString();

        String prestigePrefix = prestige > 0 ? "§e" + MathUtil.roman(prestige) + bracketsColor + "-" : "";

        String rank = PlayerUtil.getRank(player).replace("%", "%%");

        String formatted = String.format(
                "%s[%s%s%d%s] %s %s: §f%s",
                bracketsColor,
                prestigePrefix,
                LevelUtil.getLevelColorChat(level),
                level,
                bracketsColor,
                rank,
                player.getName(),
                message
        );
        event.setFormat(formatted);
    }
}

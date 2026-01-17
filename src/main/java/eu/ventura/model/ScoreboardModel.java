package eu.ventura.model;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

/**
 * author: ekkoree
 * created at: 12/29/2025
 */
@Getter
public class ScoreboardModel {
    private final Player player;

    private final Scoreboard scoreboard;
    private final Objective objective;

    private final String[] lines = new String[15];

    @SuppressWarnings("deprecation")
    public ScoreboardModel(Player player) {
        this.player = player;

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("sidebar", "dummy");
        this.objective.setDisplayName("§e§lᴠᴇɴᴛᴜʀᴀ ᴘɪᴛ");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 1; i <= 15; i++) {
            Team team = scoreboard.registerNewTeam("line" + i);
            team.addEntry(genEntry(i));
        }

        player.setScoreboard(scoreboard);
    }

    public void updateLines(List<String> lines) {
        int i = 15;

        for (int j = 0; j < 15; j++) {
            String newLine = (i > 0 && j < lines.size()) ? lines.get(j) : null;
            updateLine(i, newLine);
            i--;
        }
    }

    private void updateLine(int line, String newText) {
        if (line < 1 || line > 15) return;

        Team team = scoreboard.getTeam("line" + line);
        String entry = genEntry(line);

        if (team == null) return;

        String currentLine = lines[line - 1];

        if (newText == null) {
            if (currentLine != null) {
                team.prefix(Component.empty());
                team.suffix(Component.empty());
                scoreboard.resetScores(entry);
                lines[line - 1] = null;
            }
            return;
        }

        if (newText.equals(currentLine)) return;

        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();

        if (newText.length() <= 16) {
            team.prefix(serializer.deserialize(newText));
            team.suffix(Component.empty());
        } else {
            String prefix = newText.substring(0, 16);
            String suffix;

            if (prefix.charAt(15) == '§') {
                prefix = newText.substring(0, 15);
                suffix = newText.substring(15);
            } else {
                suffix = getLastColors(prefix) + newText.substring(16);
            }

            if (suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }

            team.prefix(serializer.deserialize(prefix));
            team.suffix(serializer.deserialize(suffix));
        }

        objective.getScore(entry).setScore(line);
        lines[line - 1] = newText;
    }

    private String getLastColors(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = input.length() - 1; i > 0; i--) {
            if (input.charAt(i - 1) == '§') {
                char code = input.charAt(i);
                if ((code >= '0' && code <= '9') || (code >= 'a' && code <= 'f') || code == 'r') {
                    result.insert(0, "§" + code);
                    if (code == 'r') break;
                } else if (code == 'k' || code == 'l' || code == 'm' || code == 'n' || code == 'o') {
                    result.insert(0, "§" + code);
                }
            }
        }
        return result.toString();
    }

    private String genEntry(int line) {
        String[] colors = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f"};
        if (line >= 0 && line < colors.length) {
            return colors[line] + colors[line];
        }
        return "§" + line + "§" + line;
    }
}

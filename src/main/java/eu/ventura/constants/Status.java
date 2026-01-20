package eu.ventura.constants;

import eu.ventura.model.PlayerModel;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
@RequiredArgsConstructor
public enum Status {
    IDLING("§aCichy", "§aIdling"),
    FIGHTING("§cPodczas walki", "§cFighting"),
    BOUNTIED("§cMistrz", "§cBountied");

    private final String polish;
    private final String english;

    public String getTitle(Player player) {
        Strings.Language language = PlayerModel.getInstance(player).language;
        return switch (language) {
            case ENGLISH -> english;
            case POLISH -> polish;
        };
    }

    public String getTitle(PlayerModel playerModel) {
        Strings.Language language = playerModel.language;
        return switch (language) {
            case ENGLISH -> english;
            case POLISH -> polish;
        };
    }
}

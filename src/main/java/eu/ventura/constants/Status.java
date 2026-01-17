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
    IDLING("§aᴄɪᴄʜʏ", "§aɪᴅʟɪɴɢ"),
    FIGHTING("§cᴡ ᴡᴀʟᴄᴇ", "§cꜰɪɢʜᴛɪɴɢ"),
    BOUNTIED("§cᴍɪѕᴛʀᴢ", "§cʙᴏᴜɴᴛɪᴇᴅ");

    private final String polish;
    private final String english;

    public String getTitle(Player player) {
        Strings.Language language = PlayerModel.getInstance(player).getLanguage();
        return switch (language) {
            case ENGLISH -> english;
            case POLISH -> polish;
        };
    }

    public String getTitle(PlayerModel playerModel) {
        Strings.Language language = playerModel.getLanguage();
        return switch (language) {
            case ENGLISH -> english;
            case POLISH -> polish;
        };
    }
}

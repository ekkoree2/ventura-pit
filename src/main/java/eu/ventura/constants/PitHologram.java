package eu.ventura.constants;

import eu.ventura.model.PlayerModel;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Function;

/**
 * author: ekkoree
 * created at: 1/17/2026
 */
public enum PitHologram {
    ENDER_CHEST(player -> List.of(
            "&5&lᴇɴᴅᴇʀ ᴄʜᴇѕᴛ",
            of(player, "&7ᴘʀᴢᴇᴄʜᴏᴡᴜᴊ ɪᴛᴇᴍʏ", "&7ѕᴛᴏʀᴇ ɪᴛᴇᴍѕ ꜰᴏʀᴇᴠᴇʀ")
    )),
    PERKS_NPC(player -> List.of(
            of(player, "&a&lᴜʟᴇᴘѕᴢᴇɴɪᴀ", "&a&lᴜᴘɢʀᴀᴅᴇѕ"),
            of(player, "&7ᴅᴏᴢʏᴡᴏᴛɴɪᴇ", "&7ᴘᴇʀᴍᴀɴᴇɴᴛ")
    )),
    SHOP_NPC(player -> List.of(
            of(player, "&6&lɪᴛᴇᴍʏ", "&6&lɪᴛᴇᴍѕ"),
            of(player, "&7ᴛʏᴍᴄᴢᴀѕᴏᴡᴇ", "&7ɴᴏɴ ᴘᴇʀᴍᴀɴᴇɴᴛ")
    )),
    PRESTIGE_NPC(player -> List.of(
            of(player, "&e&lᴘʀᴇѕᴛɪɢᴇ", "&6&lᴘʀᴇѕᴛɪɢᴇ"),
            of(player, "&7ʀᴇѕᴇᴛʏ & ʀᴇɴᴏᴡɴ", "&7ʀᴇѕᴇᴛѕ & ʀᴇɴᴏᴡɴ")
    )),

    ;

    private final Function<Player, List<String>> lines;

    PitHologram(Function<Player, List<String>> lines) {
        this.lines = lines;
    }

    public List<String> getLines(Player player) {
        return lines.apply(player).stream()
                .map(line -> line.replaceAll("&([0-9a-fk-or])", "§$1"))
                .toList();
    }

    private static String of(Player player, String polish, String english) {
        return PlayerModel.getInstance(player).language == Strings.Language.POLISH ? polish : english;
    }
}

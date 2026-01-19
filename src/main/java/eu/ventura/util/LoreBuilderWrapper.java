package eu.ventura.util;

import eu.ventura.constants.Strings;
import eu.ventura.model.PlayerModel;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public class LoreBuilderWrapper {
    private final LoreBuilder polish;
    private final LoreBuilder english;

    public LoreBuilderWrapper(LoreBuilder polish, LoreBuilder english) {
        this.polish = polish;
        this.english = english;
    }

    public LoreBuilderWrapper(LoreBuilder both) {
        this.polish = both;
        this.english = both;
    }

    public LoreBuilder get(Strings.Language lang) {
        return lang == Strings.Language.ENGLISH ? english : polish;
    }

    public LoreBuilder get(Player player) {
        return get(PlayerModel.getInstance(player).getLanguage());
    }

    public List<String> compile(Strings.Language lang) {
        return get(lang).compile();
    }

    public List<String> compile(Player player) {
        return get(player).compile();
    }
}

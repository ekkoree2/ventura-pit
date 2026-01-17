package eu.ventura.enchantment;

import eu.ventura.constants.Strings;
import eu.ventura.model.AttackModel;
import eu.ventura.util.LoreBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public interface PitEnchant {

    default String getId() {
        String className = this.getClass().getSimpleName();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    String getDisplayName();

    LoreBuilder getDescription(int level, Strings.Language language);

    EnchantRarity getRarity();

    EnchantItem[] getItemTypes();

    EnchantType getType();

    ApplyOn getApplyOn();

    default int getPriority() {
        return 0;
    }

    default String getFormattedName(Strings.Language language) {
        String prefix = isRare()
                ? Strings.Simple.RARE_PREFIX.get(language)
                : "";
        return Strings.Formatted.ENCHANT_DISPLAY_NAME.format(language, prefix, getDisplayName());
    }

    default boolean isRare() {
        return false;
    }

    default void apply(int level, AttackModel model) {

    }

    default void applySomber(int level, AttackModel model) {

    }

    default void getFinalApply(int level, AttackModel model, Consumer<AttackModel> consumer) {
        consumer.accept(model);
    }
}

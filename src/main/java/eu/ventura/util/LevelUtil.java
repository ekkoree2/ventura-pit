package eu.ventura.util;

import eu.ventura.model.PlayerModel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class LevelUtil {
    public record LevelColorInfo(TextColor color, boolean bold) {
        public static LevelColorInfo of(TextColor color, boolean bold) {
            return new LevelColorInfo(color, bold);
        }

        public static LevelColorInfo of(TextColor color) {
            return new LevelColorInfo(color, false);
        }
    }
    public static double getMultiplier(int prestige) {
        return switch (prestige) {
            case 0 -> 1.0;
            case 1 -> 1.1;
            case 2 -> 1.2;
            case 3 -> 1.3;
            case 4 -> 1.4;
            case 5 -> 1.5;
            case 6 -> 1.75;
            case 7 -> 2.0;
            case 8 -> 2.5;
            case 9 -> 3.0;
            case 10 -> 4.0;
            case 11 -> 5.0;
            case 12 -> 6.0;
            case 13 -> 7.0;
            case 14 -> 8.0;
            case 15 -> 9.0;
            case 16 -> 10.0;
            case 17 -> 12.0;
            case 18 -> 14.0;
            case 19 -> 16.0;
            case 20 -> 18.0;
            case 21 -> 20.0;
            case 22 -> 24.0;
            case 23 -> 28.0;
            case 24 -> 32.0;
            case 25 -> 36.0;
            case 26 -> 40.0;
            case 27 -> 45.0;
            case 28 -> 50.0;
            case 29 -> 75.0;
            default -> 100.0;
        };
    }

    public static double getPrestigeXpAmount(int prestige) {
        return switch (prestige) {
            case 1, 2 -> 0.5;
            case 4, 5 -> 1.5;
            case 6 -> 2.0;
            case 7 -> 2.5;
            case 8 -> 2.75;
            case 9 -> 3.25;
            case 10 -> 5.0;
            case 11 -> 6.0;
            case 12 -> 8.0;
            case 13 -> 9.0;
            case 14 -> 11.0;
            case 15 -> 12.0;
            case 16 -> 14.0;
            case 17 -> 15.0;
            case 18 -> 17.0;
            case 19 -> 19.0;
            case 20 -> 25.0;
            case 21 -> 27.0;
            case 22 -> 29.0;
            case 23 -> 31.0;
            case 24 -> 32.0;
            case 25 -> 37.0;
            case 26 -> 40.0;
            case 27 -> 44.0;
            case 28 -> 55.0;
            case 29 -> 67.0;
            case 30 -> 89.0;
            case 31 -> 120.0;
            case 32 -> 140.0;
            case 33 -> 145.0;
            case 34 -> 150.0;
            case 35 -> 200.0;
            default -> 1.0;
        };
    }

    public static int getRenownFromPrestige(int prestige) {
        return switch (prestige) {
            case 0 -> 0;
            case 1, 2, 3, 4, 5 -> 10;
            case 6, 7, 8, 9, 10 -> 20;
            case 11, 12, 13, 14, 15 -> 30;
            case 16, 17, 18, 19, 20 -> 40;
            case 21, 22, 23, 24, 25 -> 50;
            case 26, 27, 28 -> 75;
            case 30 -> 250;
            default -> 100;
        };
    }

    public static double getGoldReq(int prestige) {
        return switch (prestige) {
            case 0 -> 10000.0;
            case 1, 2, 3 -> 20000.0;
            case 4 -> 30000.0;
            case 5 -> 35000.0;
            case 6 -> 40000.0;
            case 7 -> 45000.0;
            case 8 -> 50000.0;
            case 9 -> 60000.0;
            case 10 -> 70000.0;
            case 11 -> 80000.0;
            case 12 -> 90000.0;
            case 13 -> 100000.0;
            case 14 -> 125000.0;
            case 15 -> 150000.0;
            case 16 -> 175000.0;
            case 17 -> 200000.0;
            case 18 -> 250000.0;
            case 19 -> 300000.0;
            case 20 -> 350000.0;
            case 21 -> 400000.0;
            case 22 -> 500000.0;
            case 23 -> 600000.0;
            case 24 -> 700000.0;
            case 25 -> 800000.0;
            case 26 -> 900000.0;
            case 27, 28, 29, 30, 31, 32, 33, 34 -> 1000000.0;
            default -> 2000000.0;
        };
    }

    public static int xpToNextLevel(int prestige, int level) {
        double multiplier = getMultiplier(prestige);
        if (level < 9) return (int) Math.ceil(15.0 * multiplier);
        if (level < 19) return (int) Math.ceil(30.0 * multiplier);
        if (level < 29) return (int) Math.ceil(50.0 * multiplier);
        if (level < 39) return (int) Math.ceil(75.0 * multiplier);
        if (level < 49) return (int) Math.ceil(125.0 * multiplier);
        if (level < 59) return (int) Math.ceil(300.0 * multiplier);
        if (level < 69) return (int) Math.ceil(600.0 * multiplier);
        if (level < 79) return (int) Math.ceil(800.0 * multiplier);
        if (level < 89) return (int) Math.ceil(900.0 * multiplier);
        if (level < 99) return (int) Math.ceil(1000.0 * multiplier);
        if (level < 109) return (int) Math.ceil(1200.0 * multiplier);
        if (level < 119) return (int) Math.ceil(1500.0 * multiplier);
        return -1;
    }

    public static double getXpPerPrestige(int prestige) {
        int totalXp = 0;
        for (int level = 1; level <= 119; level++) {
            totalXp += xpToNextLevel(prestige, level);
        }
        return totalXp;
    }

    public static TextColor getBracketsColor(int prestige) {
        if (prestige == 0) return NamedTextColor.GRAY;
        if (prestige < 5) return NamedTextColor.BLUE;
        if (prestige < 10) return NamedTextColor.YELLOW;
        if (prestige < 15) return NamedTextColor.GOLD;
        if (prestige < 20) return NamedTextColor.RED;
        if (prestige < 25) return NamedTextColor.DARK_PURPLE;
        if (prestige < 30) return NamedTextColor.LIGHT_PURPLE;
        if (prestige < 35) return NamedTextColor.WHITE;
        if (prestige < 40) return NamedTextColor.AQUA;
        if (prestige < 45) return NamedTextColor.DARK_BLUE;
        if (prestige < 48) return NamedTextColor.BLACK;
        if (prestige < 50) return NamedTextColor.DARK_RED;
        return NamedTextColor.DARK_GRAY;
    }

    public static LevelColorInfo getLevelColor(int level) {
        if (level < 0) return LevelColorInfo.of(NamedTextColor.AQUA, true);
        if (level < 10) return LevelColorInfo.of(NamedTextColor.GRAY);
        if (level < 20) return LevelColorInfo.of(NamedTextColor.BLUE);
        if (level < 30) return LevelColorInfo.of(NamedTextColor.DARK_AQUA);
        if (level < 40) return LevelColorInfo.of(NamedTextColor.DARK_GREEN);
        if (level < 50) return LevelColorInfo.of(NamedTextColor.GREEN);
        if (level < 60) return LevelColorInfo.of(NamedTextColor.YELLOW);
        if (level < 70) return LevelColorInfo.of(NamedTextColor.GOLD, true);
        if (level < 80) return LevelColorInfo.of(NamedTextColor.RED, true);
        if (level < 90) return LevelColorInfo.of(NamedTextColor.DARK_RED, true);
        if (level < 100) return LevelColorInfo.of(NamedTextColor.DARK_PURPLE, true);
        if (level < 110) return LevelColorInfo.of(NamedTextColor.LIGHT_PURPLE, true);
        if (level < 120) return LevelColorInfo.of(NamedTextColor.WHITE, true);
        return LevelColorInfo.of(NamedTextColor.AQUA, true);
    }

    public static String getFormattedLevelFromValues(PlayerModel data) {
        return getFormattedLevelFromValues(data.getPrestige(), data.getLevel());
    }

    public static String getFormattedLevelFromValues(int prestige, int level) {
        TextColor bracketColor = getBracketsColor(prestige);
        LevelColorInfo levelInfo = getLevelColor(level);

        Component openBracket = Component.text("[", bracketColor);
        Component levelNumber = Component.text(level, levelInfo.color());
        Component closeBracket = Component.text("]", bracketColor);

        if (levelInfo.bold()) {
            levelNumber = levelNumber.decorate(TextDecoration.BOLD);
        }

        Component component = openBracket.append(levelNumber).append(closeBracket);
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static ChatColor getBracketsColorChat(int prestige) {
        if (prestige == 0) return ChatColor.GRAY;
        if (prestige < 5) return ChatColor.BLUE;
        if (prestige < 10) return ChatColor.YELLOW;
        if (prestige < 15) return ChatColor.GOLD;
        if (prestige < 20) return ChatColor.RED;
        if (prestige < 25) return ChatColor.DARK_PURPLE;
        if (prestige < 30) return ChatColor.LIGHT_PURPLE;
        if (prestige < 35) return ChatColor.WHITE;
        if (prestige < 40) return ChatColor.AQUA;
        if (prestige < 45) return ChatColor.DARK_BLUE;
        if (prestige < 48) return ChatColor.BLACK;
        if (prestige < 50) return ChatColor.DARK_RED;
        return ChatColor.DARK_GRAY;
    }

    public static String getLevelColorChat(int level) {
        if (level < 0) return ChatColor.AQUA + ChatColor.BOLD.toString();
        if (level < 10) return ChatColor.GRAY.toString();
        if (level < 20) return ChatColor.BLUE.toString();
        if (level < 30) return ChatColor.DARK_AQUA.toString();
        if (level < 40) return ChatColor.DARK_GREEN.toString();
        if (level < 50) return ChatColor.GREEN.toString();
        if (level < 60) return ChatColor.YELLOW.toString();
        if (level < 70) return ChatColor.GOLD + ChatColor.BOLD.toString();
        if (level < 80) return ChatColor.RED + ChatColor.BOLD.toString();
        if (level < 90) return ChatColor.DARK_RED + ChatColor.BOLD.toString();
        if (level < 100) return ChatColor.DARK_PURPLE + ChatColor.BOLD.toString();
        if (level < 110) return ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString();
        if (level < 120) return ChatColor.WHITE + ChatColor.BOLD.toString();
        return ChatColor.AQUA + ChatColor.BOLD.toString();
    }

    public static String getFormattedLevelFromValuesChat(PlayerModel data) {
        return getFormattedLevelFromValuesChat(data.getPrestige(), data.getLevel());
    }

    public static String getFormattedLevelFromValuesChat(int prestige, int level) {
        return getBracketsColorChat(prestige) + "[" + getLevelColorChat(level) + level + getBracketsColorChat(prestige) + "]";
    }
}

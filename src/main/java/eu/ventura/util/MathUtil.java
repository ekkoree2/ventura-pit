package eu.ventura.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class MathUtil {
    @RequiredArgsConstructor
    @Getter
    public enum Symbols {
        COMMON(new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"}),
        FANCY(new String[]{"ᴍ", "ᴄᴍ", "ᴅ", "ᴄᴅ", "ᴄ", "хᴄ", "ʟ", "хʟ", "х", "ɪх", "ᴠ", "ɪᴠ", "ɪ"});

        private final String[] symbols;
    }

    public static String roman(int number) {
        return roman(number, Symbols.COMMON);
    }

    public static String roman(int number, Symbols symbols) {
        if (number <= 0) {
            return null;
        }

        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                result.append(symbols.symbols[i]);
                number -= values[i];
            }
        }

        return result.toString();
    }

    public static String getFormattedTime(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}

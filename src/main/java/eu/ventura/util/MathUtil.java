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

    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    public static float lerpAngle(float a, float b, float t) {
        float diff = ((b - a + 540) % 360) - 180;
        return a + diff * t;
    }

    public static double easeInOutCubic(double t) {
        return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
    }

    public static org.bukkit.Location cubicBezierLocationFromPoints(double t, org.bukkit.Location p0, org.bukkit.Location p1, org.bukkit.Location p2, org.bukkit.Location p3) {
        double xl1 = lerp(p0.getX(), p1.getX(), t);
        double xl2 = lerp(p1.getX(), p2.getX(), t);
        double xl3 = lerp(p2.getX(), p3.getX(), t);
        double xa = lerp(xl1, xl2, t);
        double xb = lerp(xl2, xl3, t);
        double yl1 = lerp(p0.getY(), p1.getY(), t);
        double yl2 = lerp(p1.getY(), p2.getY(), t);
        double yl3 = lerp(p2.getY(), p3.getY(), t);
        double ya = lerp(yl1, yl2, t);
        double yb = lerp(yl2, yl3, t);
        double zl1 = lerp(p0.getZ(), p1.getZ(), t);
        double zl2 = lerp(p1.getZ(), p2.getZ(), t);
        double zl3 = lerp(p2.getZ(), p3.getZ(), t);
        double za = lerp(zl1, zl2, t);
        double zb = lerp(zl2, zl3, t);
        double finalX = lerp(xa, xb, t);
        double finalY = lerp(ya, yb, t);
        double finalZ = lerp(za, zb, t);
        return new org.bukkit.Location(p0.getWorld(), finalX, finalY, finalZ, p0.getYaw(), p0.getPitch());
    }
}

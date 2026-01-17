package eu.ventura.constants;

import java.text.DecimalFormat;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class NumberFormat {
    public static final Formatter GOLD_KILL = new Formatter("#,##0.00");
    public static final Formatter GOLD_DISPLAY_HIGH = new Formatter("###,###,###,###,###,##0$");
    public static final Formatter GOLD_DISPLAY_LOW = new Formatter("###,###,###,###,###,##0.00$");
    public static final Formatter DEF = new Formatter("#,###,###");

    public static String formatGold(double gold) {
        if (gold < 10000.0) {
            return GOLD_DISPLAY_LOW.of(gold);
        }
        return GOLD_DISPLAY_HIGH.of(gold);
    }

    public static class Formatter {
        private final DecimalFormat decimalFormat;

        public Formatter(String formatter) {
            this.decimalFormat = new DecimalFormat(formatter);
        }

        public String of(double number) {
            return decimalFormat.format(number);
        }
    }
}

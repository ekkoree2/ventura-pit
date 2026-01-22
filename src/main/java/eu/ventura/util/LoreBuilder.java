package eu.ventura.util;

import lombok.RequiredArgsConstructor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
@RequiredArgsConstructor
public class LoreBuilder {
    private final Map<Integer, Integer> lengths = new HashMap<>();
    private final List<String> lines = new ArrayList<>();

    public LoreBuilder setMaxLength(int maxLength) {
        int startLine = lines.isEmpty() ? 0 : lines.size();
        lengths.put(startLine, maxLength);
        return this;
    }

    private int getMaxLength(int line) {
        for (int i = line; i >= 0; i--) {
            if (lengths.containsKey(i)) {
                return lengths.get(i);
            }
        }
        return 25;
    }

    public LoreBuilder add(String text) {
        if (lines.isEmpty()) {
            lines.add("");
        }
        int last = lines.size() - 1;
        lines.set(last, lines.get(last) + text);
        return this;
    }

    public LoreBuilder add(int number) {
        return add(String.valueOf(number));
    }

    public LoreBuilder add(double number) {
        DecimalFormat df = new DecimalFormat("0.##");
        return add(df.format(number));
    }

    public LoreBuilder addNewline() {
        lines.add("");
        return this;
    }

    public LoreBuilder addNewline(String text) {
        lines.add(text);
        return this;
    }

    public LoreBuilder addAll(List<String> additionalLines) {
        lines.addAll(additionalLines);
        return this;
    }

    public LoreBuilder addSpace() {
        return add("\u00A0");
    }

    public List<String> compile() {
        List<String> compiled = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            line = line.replaceAll("&([0-9a-fk-or])", "§$1");
            int maxLength = getMaxLength(i);
            compiled.addAll(wrapLine(line, maxLength, i));
        }
        return compiled;
    }

    public List<String> compile(Object... args) {
        List<String> compiled = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < args.length; j++) {
                line = line.replace("{" + j + "}", String.valueOf(args[j]));
            }
            line = line.replaceAll("&([0-9a-fk-or])", "§$1");
            int maxLength = getMaxLength(i);
            compiled.addAll(wrapLine(line, maxLength, i));
        }
        return compiled;
    }

    private List<String> wrapLine(String input, int maxLength, int lineIndex) {
        String color = "§7";
        boolean bold = false, underline = false, italic = false, strikethrough = false, magic = false;

        List<String> result = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        String[] words = input.split(" ");
        for (String word : words) {
            String temp = color;

            boolean tempBold = bold, tempUnderline = underline, tempItalic = italic;
            boolean tempStrikethrough = strikethrough, tempMagic = magic;

            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == '§' && i + 1 < word.length()) {
                    char type = word.charAt(i + 1);
                    switch (type) {
                        case 'k' -> tempMagic = true;
                        case 'l' -> tempBold = true;
                        case 'm' -> tempStrikethrough = true;
                        case 'n' -> tempUnderline = true;
                        case 'o' -> tempItalic = true;
                        case 'r' -> {
                            tempMagic = false;
                            tempBold = false;
                            tempStrikethrough = false;
                            tempUnderline = false;
                            tempItalic = false;
                            temp = "§7";
                        }
                        default -> {
                            if ((type >= '0' && type <= '9') || (type >= 'a' && type <= 'f')) {
                                temp = "§" + type;
                            }
                        }
                    }
                    i++;
                }
            }

            int length = line.toString().replaceAll("§.", "").replaceAll(" ", "").length();
            int fixedLength = word.replaceAll("§.", "").length();

            int currentMaxLength = result.isEmpty() ? maxLength : getMaxLength(lineIndex + result.size());

            if (length > 0 && length + fixedLength > currentMaxLength) {
                result.add(line.toString());
                line = new StringBuilder(color);
                if (bold) line.append("§l");
                if (italic) line.append("§o");
                if (underline) line.append("§n");
                if (strikethrough) line.append("§m");
                if (magic) line.append("§k");
            } else if (length > 0) {
                line.append(" ");
            }

            line.append(word);

            color = temp;
            bold = tempBold;
            underline = tempUnderline;
            italic = tempItalic;
            strikethrough = tempStrikethrough;
            magic = tempMagic;
        }

        result.add(line.toString());
        return result;
    }
}
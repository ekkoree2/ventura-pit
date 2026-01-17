package eu.ventura.java;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class NewString {
    public static String of(String value) {
        return value.replaceAll("&([0-9a-fk-or])", "§$1");
    }

    public static String format(String format, Object... args) {
        Object[] parts = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            parts[i] = args[i] == null ? "" : args[i];
        }
        return of(String.format(format, parts));
    }
}

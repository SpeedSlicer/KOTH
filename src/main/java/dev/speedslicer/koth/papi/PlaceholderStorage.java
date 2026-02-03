package dev.speedslicer.koth.papi;

import java.util.HashMap;
import java.util.Locale;

public class PlaceholderStorage {
    static HashMap<String, String> placeholders = new HashMap<>();
    public static HashMap<String, String> getPlaceholders() {
        return placeholders;
    }
    public static void setPlaceholders(String name, String value) {
        placeholders.put(name.toLowerCase(Locale.ROOT), value);
    }
    public static String getPlaceholder(String name) {
        return placeholders.get(name);
    }
}

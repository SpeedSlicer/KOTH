package dev.speedslicer.koth.papi;

import com.avaje.ebean.validation.NotNull;
import dev.speedslicer.koth.Koth;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Locale;

public class KothExpansion extends PlaceholderExpansion {

    private final Koth plugin; //

    public KothExpansion(Koth plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "koth";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        return PlaceholderStorage.getPlaceholder(params.toLowerCase(Locale.ROOT));
    }
    @Override
    public String onPlaceholderRequest(Player player, String params) {
        return PlaceholderStorage.getPlaceholder(params.toLowerCase(Locale.ROOT));
    }
}
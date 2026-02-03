package dev.speedslicer.koth;

import dev.speedslicer.koth.config.Config;
import dev.speedslicer.koth.listeners.KothAreaHandler;
import dev.speedslicer.koth.papi.KothExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Koth extends JavaPlugin {
    FileConfiguration config;
    KothAreaHandler areaHandler;
    public Config config_constants;
    public static Logger LOGGER = Logger.getLogger("KOTH");
    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new KothExpansion(this).register();
        }
        else {
            LOGGER.severe("PlaceholderAPI is not installed! Highly recommended!");
        }
        this.saveDefaultConfig();
        config = this.getConfig();
        config.addDefault("claim-seconds", 5D);
        config.addDefault("reward-cooldown-seconds", 5D);
        config.addDefault("reward-command", "give %controlling_player% diamond 1");
        config.addDefault("world", "world");
        config.addDefault("radius", 5);
        config.addDefault("center-pos-z", 0);
        config.addDefault("center-pos-y", 0);
        config.addDefault("center-pos-x", 0);
        config.options().copyDefaults(true);
        saveConfig();
        areaHandler = new KothAreaHandler();
        reload();
    }

    public void reload() {
        config_constants.claim_seconds = config.getDouble("claim-seconds");
        config_constants.reward_cooldown_seconds = config.getDouble("reward-cooldown-seconds");
        config_constants.reward_command = config.getString("reward-command");
        config_constants.radius = config.getInt("radius");

        String worldName = config.getString("world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            LOGGER.severe("World '" + worldName + "' does not exist!");
            return;
        }
        config_constants.world = worldName;

        config_constants.center_pos_x = config.getInt("center-pos-x");
        config_constants.center_pos_y = config.getInt("center-pos-y");
        config_constants.center_pos_z = config.getInt("center-pos-z");

        areaHandler.reload(config_constants);
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    public void setCenterLocation(Location location) {
        config.set("center-pos-x", location.getBlockX());
        config.set("center-pos-y", location.getBlockY());
        config.set("center-pos-z", location.getBlockZ());
        config.set("world",  location.getWorld().getName());
        saveConfig();
        reload();
    }
    public void setRadius(int range) {
        config.set("radius", range);
        saveConfig();
        reload();
    }
}

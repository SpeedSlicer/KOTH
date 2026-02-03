package dev.speedslicer.koth.listeners;

import dev.speedslicer.koth.config.Config;
import dev.speedslicer.koth.papi.PlaceholderStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;
import java.util.stream.Collectors;

public class KothAreaHandler implements Listener {
    World world;
    Location location;
    int radius;
    Collection<Entity> entities;
    HashMap<Player, Float> players = new HashMap<>();
    double claimSeconds, rewardSeconds;
    String command;
    String currentlyControllingPlayer = "";

    float lastRewardTick = 0;
    public KothAreaHandler() {

    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

    }
    public void reload(Config config) {
        world = Bukkit.getServer().getWorld(config.world);
        location = new Location(world,config.center_pos_x,config.center_pos_y,config.center_pos_z);
        radius = config.radius;
        claimSeconds = config.claim_seconds;
        rewardSeconds = config.reward_cooldown_seconds;
        command = config.reward_command;
    }
    public void onUpdate() {
        Collection<Entity> nearby = world.getNearbyEntities(location, radius, radius, radius);

        List<Player> nearbyPlayers = nearby.stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());

        if (nearbyPlayers.size() > 1) {
            players.clear();
            currentlyControllingPlayer = "";
            PlaceholderStorage.setPlaceholders("player_controlling", "Contested!");
            return;
        }
        if (nearbyPlayers.isEmpty()) {
            players.clear();
            currentlyControllingPlayer = "";
            PlaceholderStorage.setPlaceholders("player_controlling", "None");
            return;
        }

        Player player = nearbyPlayers.get(0);
        currentlyControllingPlayer = player.getDisplayName();

        float time = players.getOrDefault(player, 0f) + 1f;
        players.clear();
        players.put(player, time);

        updatePAPI();
    }
    public void updatePAPI() {
        if (players.size() != 1) return;

        Player player = players.keySet().iterator().next();
        float ticks = players.get(player);

        if (ticks >= rewardSeconds * 20) {
            PlaceholderStorage.setPlaceholders("player_controlling", player.getDisplayName());
        }

        if (Math.abs(lastRewardTick - (ticks - rewardSeconds * 20)) > rewardSeconds ) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%controlling_player%", currentlyControllingPlayer));
        }
    }

}

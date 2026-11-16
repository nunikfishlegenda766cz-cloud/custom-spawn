package cz.tvojeserver.customspawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomSpawn extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("setspawn").setExecutor(this);
        getCommand("spawn").setExecutor(this);
        getLogger().info("CustomSpawn plugin byl uspesne zapnut!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Tento prikaz muze pouzit jen hrac.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("setspawn")) {
            if (!player.hasPermission("customspawn.admin")) {
                player.sendMessage(ChatColor.RED + "Nemas opravneni nastavit spawn.");
                return true;
            }

            Location loc = player.getLocation();
            getConfig().set("spawn.world", loc.getWorld().getName());
            getConfig().set("spawn.x", loc.getX());
            getConfig().set("spawn.y", loc.getY());
            getConfig().set("spawn.z", loc.getZ());
            getConfig().set("spawn.yaw", (double) loc.getYaw());
            getConfig().set("spawn.pitch", (double) loc.getPitch());
            saveConfig();

            player.sendMessage(ChatColor.GREEN + "Spawn byl uspesne nastaven na tvoji pozici!");
            return true;
        }

        if (command.getName().equalsIgnoreCase("spawn")) {
            if (!getConfig().contains("spawn.world")) {
                player.sendMessage(ChatColor.RED + "Spawn jeste nebyl nastaven! Pouzij /setspawn.");
                return true;
            }

            String worldName = getConfig().getString("spawn.world");
            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                player.sendMessage(ChatColor.RED + "Svet '" + worldName + "' nebyl nalezen!");
                return true;
            }

            double x = getConfig().getDouble("spawn.x");
            double y = getConfig().getDouble("spawn.y");
            double z = getConfig().getDouble("spawn.z");
            float yaw = (float) getConfig().getDouble("spawn.yaw");
            float pitch = (float) getConfig().getDouble("spawn.pitch");

            Location spawnLoc = new Location(world, x, y, z, yaw, pitch);
            player.teleport(spawnLoc);
            player.sendMessage(ChatColor.GREEN + "Byl jsi teleportovan na spawn!");
            return true;
        }

        return false;
    }
}

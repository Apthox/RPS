package hypercubemc.com.rps.commands;

import hypercubemc.com.rps.managers.OriginManager;
import hypercubemc.com.rps.origins.Orion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Arrays;
import java.util.List;

public class RPS implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //const String classes[] = {"ORION", "HYDRA", "GAIA"};

        if (args.length >= 1) {
            String sub = args[0];

            if (sub.equalsIgnoreCase("set")) {
                set(sender, args);
            }

            if (sub.equalsIgnoreCase("clear")) {
                clear(sender, args);
            }

            return true;
        }

        sender.sendMessage(ChatColor.RED + "SUB-COMMANDS: SET, CLEAR");

        return true;
    }

    // /rps set [player] [class]
    public void set(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "RPS SET USAGE: /rps set [player] [class]");
            return;
        }

        String username = args[1];
        String classname = args[2];

        // check if valid username
        Player player = Bukkit.getPlayer(username);
        if (player == null) {
            Bukkit.broadcastMessage(ChatColor.RED + "RPS SET: Couldn't find player " + username + "!");
            return;
        }

        if (!(classname.equalsIgnoreCase("orion") ||
                classname.equalsIgnoreCase("hydra") ||
                classname.equalsIgnoreCase("gaia"))
        ) {
            sender.sendMessage(ChatColor.RED + "RPS SET: not a valid class!");
            return;
        }

        OriginManager manager = hypercubemc.com.rps.RPS.getOriginManager();
        manager.addPlayer(player, new Orion(player));
        sender.sendMessage(ChatColor.GREEN + "RPS SET: " + username + " has been set to " + classname.toUpperCase() + " successfully!");

    }

    public void clear(CommandSender sender, String[] args) {
        if (args.length == 1) {
            // clears self
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "RPS CLEAR: you must be a player to clear yourself");
                return;
            }
            Player player = (Player) sender;

            OriginManager manager = hypercubemc.com.rps.RPS.getOriginManager();
            manager.removePlayer(player);
        } else if (args.length == 2) {
            String username = args[1];

            // check if valid username
            Player player = Bukkit.getPlayer(username);
            if (player == null) {
                Bukkit.broadcastMessage(ChatColor.RED + "RPS CLEAR: Couldn't find player " + username + "!");
                return;
            }

            OriginManager manager = hypercubemc.com.rps.RPS.getOriginManager();
            manager.removePlayer(player);
            sender.sendMessage(ChatColor.GREEN + "RPS CLEAR: " + username + " has been cleared of any class.");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Arrays.asList(new String[]{"A", "B", "C"});
    }



}

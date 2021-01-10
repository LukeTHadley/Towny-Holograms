package com.github.lukethadley.townyholograms.commands.subcommands;

import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramItem;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class list extends SubCommand {


    public list() {
        super("list", "Show a list of all holograms in your town");
        setPermission(Strings.REGULAR_PERM + "list");
    }

    @Override
    public String getPossibleArguments() {
        return "";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(TownyHolograms plugin, CommandSender sender, String label, String[] args, ConfigValues configValues, DatabaseConnection databaseConnection) throws CommandException {
        Player player = (Player) sender;
        ArrayList<HologramItem> holograms = databaseConnection.getHolograms(plugin.getTownFromPlayer(player).getUuid().toString());
        sender.sendMessage(ChatColor.GOLD + ".oOo.___________.[ " + ChatColor.YELLOW + plugin.getTownFromPlayer(player).getName() + " Holograms " + ChatColor.GOLD + "].___________.oOo.");
        if (holograms.size() == 0){
            sender.sendMessage(ChatColor.DARK_GREEN + "Your town has no holograms at the moment.");
        }
        for (HologramItem hologram : holograms){
            sender.sendMessage(" " + ChatColor.DARK_AQUA + hologram.getName() + ChatColor.GRAY + " - " + ChatColor.AQUA + hologram.getLocation().getWorld().getName() + " X:" + String.format("%.03f", hologram.getLocation().getX()) + " Y:" + String.format("%.03f", hologram.getLocation().getY()) + " Z:" + String.format("%.03f", hologram.getLocation().getZ()));
        }
    }
}

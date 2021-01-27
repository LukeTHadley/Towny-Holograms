package com.github.lukethadley.townyholograms.commands.subcommands.admin;

import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.Permission;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramItem;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class AdminListHolograms extends SubCommand {

    public AdminListHolograms(){
        super("list", "List all holograms or holograms in a specified town.");
        setPermission(Permission.ADMIN_LIST_HOLOGRAMS);
        setConsoleCommand(true);
    }

    @Override
    public String getPossibleArguments() {
        return "<Town Name>";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(TownyHolograms plugin, CommandSender sender, String label, String[] args, ConfigValues configValues, DatabaseConnection databaseConnection) throws CommandException {

        try {

            if (args.length == 0) {

                sender.sendMessage(ChatColor.GOLD + ".oOo.___________.[ " + ChatColor.YELLOW + "All Holograms " + ChatColor.GOLD + "].___________.oOo.");
                sender.sendMessage(ChatColor.DARK_GREEN + "There are " + ChatColor.GREEN + plugin.getTotalHolograms() + ChatColor.DARK_GREEN + " towny holograms active on the server.");

                for (UUID key : plugin.holograms.keySet()) {
                    String name = TownyAPI.getInstance().getDataSource().getTown(key).getName();
                    sender.sendMessage(ChatColor.DARK_AQUA + " " + name + ChatColor.GRAY + " - " + ChatColor.AQUA + plugin.holograms.get(key).size());

                }
                return;


            } else if (args.length > 0) {

                for (UUID key : plugin.holograms.keySet()) {

                    String name = TownyAPI.getInstance().getDataSource().getTown(key).getName();
                    if (args[0].equalsIgnoreCase(name)){
                        sender.sendMessage(ChatColor.GOLD + ".oOo.___________.[ " + ChatColor.YELLOW + name + " Holograms " + ChatColor.GOLD + "].___________.oOo.");
                        sender.sendMessage(ChatColor.DARK_GREEN + "There are " + ChatColor.GREEN + plugin.holograms.get(key).size() + ChatColor.DARK_GREEN + " in that town.");
                        for (HologramItem hologram : plugin.holograms.get(key)){
                            sender.sendMessage(" " + ChatColor.DARK_AQUA + hologram.getName() + ChatColor.GRAY + " - " + ChatColor.AQUA + hologram.getLocation().getWorld().getName() + " X:" + String.format("%.03f", hologram.getLocation().getX()) + " Y:" + String.format("%.03f", hologram.getLocation().getY()) + " Z:" + String.format("%.03f", hologram.getLocation().getZ()));
                        }
                    }

                }
                sender.sendMessage(Strings.DISPLAY_PREFIX + " A town with the name " + args[0] + " could not be found or does not have any Towny Holograms.");
                return;

            }

        } catch (NotRegisteredException exception) {
            exception.printStackTrace();
        }


    }
}

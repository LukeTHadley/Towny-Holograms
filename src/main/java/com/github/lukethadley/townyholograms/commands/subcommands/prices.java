package com.github.lukethadley.townyholograms.commands.subcommands;

import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramAllowance;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public class prices extends SubCommand {

    public prices(){
        super("prices", "Shows the prices and levels of holograms.");
        setPermission(Strings.REGULAR_PERM + "prices");
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


        sender.sendMessage(ChatColor.GOLD + ".oOo._________.[" + ChatColor.YELLOW + " TownyHolograms Information " + ChatColor.GOLD + "]._________.oOo.");
        sender.sendMessage(ChatColor.YELLOW + " [New] " + ChatColor.DARK_GREEN + "Hologram: " + ChatColor.GREEN + configValues.getNewHologramPrice());
        sender.sendMessage(ChatColor.YELLOW + " [Hologram Allowance] " + ChatColor.DARK_GREEN + "How many holograms a town can have");
        for (HologramAllowance allowance : configValues.getHologramAllowances()){
            sender.sendMessage(ChatColor.DARK_GREEN + "          Residents: " + ChatColor.GREEN + allowance.getNumberOfResidents() + ChatColor.DARK_GRAY + " | " + ChatColor.DARK_GREEN + "Holograms: " + ChatColor.GREEN + allowance.getNumberOfHolograms());
        }

        sender.sendMessage(ChatColor.YELLOW + " [Other Information] ");
        sender.sendMessage(ChatColor.DARK_GREEN + "          Holograms in a town will be deleted if a town falls.");
        sender.sendMessage(ChatColor.DARK_GREEN + "          Holograms in a chunk will be deleted if unclaimed.");
        if (configValues.getClearHologramsOnPlotClear()){
            sender.sendMessage(ChatColor.DARK_GREEN + "          Holograms in a chunk will be deleted on " + ChatColor.GREEN + "/plot clear" + ChatColor.DARK_GREEN + ".");
        } else{
            sender.sendMessage(ChatColor.DARK_GREEN + "          Holograms in a chunk will not be deleted on " + ChatColor.GREEN + "/plot clear" + ChatColor.DARK_GREEN + ".");
        }


        //sender.sendMessage(" " + ChatColor.YELLOW + "[New] " + ChatColor.DARK_GREEN + "Hologram: " + ChatColor.GREEN + configValues.getNewHologramPrice());






    }
}

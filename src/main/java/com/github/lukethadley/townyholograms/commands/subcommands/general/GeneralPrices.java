package com.github.lukethadley.townyholograms.commands.subcommands.general;

import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.Permission;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramAllowance;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GeneralPrices extends SubCommand {

    public GeneralPrices(){
        super("prices", "Shows the prices and levels of holograms.", new String[]{"guide", "wiki"});
        setPermission(Permission.PRICES);
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






        try {

            Player player = (Player) sender;
            int townResidentCount = TownyAPI.getInstance().getTownBlock(player.getLocation()).getTown().getNumResidents();



            HologramAllowance closest = new HologramAllowance(0, 0);
            for (HologramAllowance allowance : plugin.prices){

                if (allowance.getNumberOfResidents() <= townResidentCount && allowance.getNumberOfResidents() > closest.getNumberOfResidents()){
                    closest = allowance;
                }
            }

            sender.sendMessage(ChatColor.DARK_GREEN + "          Your town is allowed " + ChatColor.GREEN + closest.getNumberOfHolograms() + ChatColor.DARK_GREEN + " holograms.");

        } catch (NotRegisteredException e ){
            e.printStackTrace();
        }





    }
}

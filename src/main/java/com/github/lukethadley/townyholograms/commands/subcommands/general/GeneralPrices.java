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

        try {

            Player player = (Player) sender;
            HologramAllowance closestAllowance = configValues.getClosestAllowance(TownyAPI.getInstance().getTownBlock(player.getLocation()).getTown().getNumResidents());


            sender.sendMessage(ChatColor.GOLD + ".oOo._________.[" + ChatColor.YELLOW + " TownyHolograms Information " + ChatColor.GOLD + "]._________.oOo.");
            sender.sendMessage(ChatColor.YELLOW + " [Allowances] " + ChatColor.DARK_GREEN + "Different allowance levels/costs are below\n                  Red shows your town's allowance.");
            int counter = 1;
            for (int key : configValues.getHologramSettings().keySet()){
                HologramAllowance current = configValues.getHologramSettings().get(key);
                String colour = ChatColor.YELLOW + "";
                if (current == closestAllowance){
                    colour = ChatColor.RED + "";
                }
                sender.sendMessage(colour + "          [" + counter + "]" + ChatColor.DARK_GREEN + " Residents: " + ChatColor.GREEN + current.getResidents() + ChatColor.DARK_GRAY + " | " + ChatColor.DARK_GREEN + "Holograms: " + ChatColor.GREEN + current.getMaxNumber() + ChatColor.DARK_GRAY + " | " + ChatColor.DARK_GREEN + "New Holo Cost: " + ChatColor.GREEN + current.getHologramCost());
                sender.sendMessage(ChatColor.DARK_GREEN + "               Line Limit: " + ChatColor.GREEN + current.getLineLimit() + ChatColor.DARK_GRAY + " | " + ChatColor.DARK_GREEN + "Additional Line Cost: " + ChatColor.GREEN + current.getLineCost());
                counter++;
            }

            sender.sendMessage(ChatColor.YELLOW + " [Other Information] ");
            sender.sendMessage(ChatColor.DARK_GREEN + "          Holograms in a town will be deleted if a town falls.");
            sender.sendMessage(ChatColor.DARK_GREEN + "          Holograms in a chunk will be deleted if unclaimed.");
            if (configValues.getPlotClearSetting()){
                sender.sendMessage(ChatColor.DARK_GREEN + "          Holograms in a chunk will be deleted on " + ChatColor.GREEN + "/plot clear" + ChatColor.DARK_GREEN + ".");
            } else{
                sender.sendMessage(ChatColor.DARK_GREEN + "          Holograms in a chunk will not be deleted on " + ChatColor.GREEN + "/plot clear" + ChatColor.DARK_GREEN + ".");
            }








        } catch (NotRegisteredException e ){
            e.printStackTrace();
        }





    }
}

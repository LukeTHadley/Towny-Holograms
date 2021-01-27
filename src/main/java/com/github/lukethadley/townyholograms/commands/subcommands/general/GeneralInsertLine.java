package com.github.lukethadley.townyholograms.commands.subcommands.general;

import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.Permission;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramAllowance;
import com.github.lukethadley.townyholograms.storage.HologramItem;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GeneralInsertLine extends SubCommand {

    public GeneralInsertLine(){
        super("insertline", "Insert a line into an existing hologram");
        setPermission(Permission.INSERT_LINE);
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName> <index> [text]";
    }

    @Override
    public int getMinimumArguments() {
        return 3;
    }

    @Override
    public void execute(TownyHolograms plugin, CommandSender sender, String label, String[] args, ConfigValues configValues, DatabaseConnection databaseConnection) throws CommandException {
        Player player = (Player) sender;
        try{
            String hologramName = args[0];
            int lineIndex = Integer.parseInt(args[1])+1;

            Town town = TownyAPI.getInstance().getTownBlock(player.getLocation()).getTown();
            HologramAllowance allowance = configValues.getClosestAllowance(town.getNumResidents());

            HologramItem hologram = databaseConnection.getHologram(hologramName, plugin.getTownFromPlayer(player).getUuid().toString());

            if (hologram == null){
                sender.sendMessage(Strings.DISPLAY_PREFIX + " A hologram with the name '" + hologramName + "' does not exist!");
                return;
            }

            if (lineIndex <= 0 || lineIndex > hologram.getLines().length){
                sender.sendMessage(Strings.DISPLAY_PREFIX + " The index you gave was invalid for the hologram '" + hologramName + "', as it has only " + hologram.getLines().length + " lines");
                return;
            }

            if (allowance == null){
                sender.sendMessage(Strings.DISPLAY_PREFIX + " An allowance setting for your town size can not be found.");
                return;
            }


            if (allowance.getLineLimit() != 0){
                if (hologram.getLines().length >= allowance.getLineLimit()){
                    sender.sendMessage(Strings.DISPLAY_PREFIX + " You can't add another line with your current hologram allowance.");
                    return;
                }
            }

            String hologramText = ChatColor.AQUA + "New Hologram!";

            if (args.length >= 3){ //There is text in the argument to add to the hologram.
                hologramText = "";
                for(int i = 2; i < args.length; i++){
                    if(i == args.length-1){
                        hologramText = hologramText + args[i];
                    }
                    else {
                        hologramText = hologramText + args[i] + " ";
                    }
                }
            }
            else {
                sender.sendMessage(Strings.DISPLAY_PREFIX + " You did not give any content to set to the line.");
            }

            String formattedHologramText = ChatColor.translateAlternateColorCodes('&', hologramText);

            int counter = 0;
            ArrayList<HologramItem> townList = plugin.holograms.get(hologram.getTownUUID());
            for (HologramItem holo : townList){
                if (holo.getName().equals(hologram.getName()) && holo.getTown().equals(hologram.getTown())){
                    townList.get(counter).insertLine(lineIndex-1, formattedHologramText);
                    databaseConnection.updateContent(townList.get(counter).linesToString(), hologramName, plugin.getTownFromPlayer(player).getUuid().toString());
                    sender.sendMessage(Strings.DISPLAY_PREFIX + " The content of hologram '" + hologramName + "' was updated!");
                    plugin.holograms.replace(hologram.getTownUUID(), townList);
                    return;
                }
                counter++;
            }




        }
        catch (NumberFormatException e){
            sender.sendMessage(Strings.DISPLAY_PREFIX + " Please enter a integer index, not a string.");
        }
        catch (NotRegisteredException e){
            sender.sendMessage(Strings.DISPLAY_PREFIX + " An issue with towny occurred while inserting a line");
        }


    }
}

package com.github.lukethadley.townyholograms.commands.subcommands;

import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.Permission;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramItem;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class insertline extends SubCommand {

    public insertline(){
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

            HologramItem hologram = databaseConnection.getHologram(hologramName, plugin.getTownFromPlayer(player).getUuid().toString());

            if (hologram == null){
                sender.sendMessage(Strings.DISPLAY_PREFIX + " A hologram with the name '" + hologramName + "' does not exist!");
                return;
            }

            if (lineIndex <= 0 || lineIndex > hologram.getLines().length){
                sender.sendMessage(Strings.DISPLAY_PREFIX + " The index you gave was invalid for the hologram '" + hologramName + "', as it has only " + hologram.getLines().length + " lines");
                return;
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


    }
}

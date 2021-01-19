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

public class addline extends SubCommand {

    public addline(){
        super("addline", "Add a line to a given hologram");
        setPermission(Permission.ADD_LINE);
    }


    @Override
    public String getPossibleArguments() {
        return "<hologramName> [text]";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public void execute(TownyHolograms plugin, CommandSender sender, String label, String[] args, ConfigValues configValues, DatabaseConnection databaseConnection) throws CommandException {

        Player player = (Player) sender;
        String hologramName = args[0];

        HologramItem hologram = databaseConnection.getHologram(hologramName, plugin.getTownFromPlayer(player).getUuid().toString());

        if (hologram == null){
            sender.sendMessage(Strings.DISPLAY_PREFIX + " A hologram with the name '" + hologramName + "' does not exist!");
            return;
        }

        String hologramText = ChatColor.AQUA + "New Hologram!";

        if (args.length > 1){ //There is text in the argument to add to the hologram.
            hologramText = "";
            for(int i = 1; i < args.length; i++){
                if(i == args.length-1){
                    hologramText = hologramText + args[i];
                }
                else {
                    hologramText = hologramText + args[i] + " ";
                }
            }
        }
        String formattedHologramText = ChatColor.translateAlternateColorCodes('&', hologramText);

        int counter = 0;
        ArrayList<HologramItem> hologramItems = plugin.holograms.get(hologram.getTownUUID());
        for (HologramItem holo : hologramItems){
            if (holo.getName().equals(hologram.getName()) && holo.getTown().equals(hologram.getTown())){

                hologramItems.get(counter).addLine(formattedHologramText);
                databaseConnection.updateContent(hologramItems.get(counter).linesToString(), hologramName, plugin.getTownFromPlayer(player).getUuid().toString());


                sender.sendMessage(Strings.DISPLAY_PREFIX + " The content of hologram '" + hologramName + "' was updated!");
                plugin.holograms.replace(hologram.getTownUUID(), hologramItems);
                return;


            }
            counter++;
        }


    }
}

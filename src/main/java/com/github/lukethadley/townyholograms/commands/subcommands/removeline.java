package com.github.lukethadley.townyholograms.commands.subcommands;

import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramItem;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class removeline extends SubCommand {

    public removeline(){
        super("removeline", "Remove a line from a given hologram", new String[]{"delline", "deleteline"});
        setPermission(Strings.REGULAR_PERM + "removeline");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName> <lineNumber>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public void execute(TownyHolograms plugin, CommandSender sender, String label, String[] args, ConfigValues configValues, DatabaseConnection databaseConnection) throws CommandException {
        Player player = (Player) sender;
        try {
            String hologramName = args[0];
            int lineIndex = (Integer.parseInt(args[1]));

            HologramItem hologram = databaseConnection.getHologram(hologramName, plugin.getTownFromPlayer(player).getUuid().toString());

            if (hologram == null) {
                sender.sendMessage(Strings.DISPLAY_PREFIX + " A hologram with the name '" + hologramName + "' does not exist!");
                return;
            }

            if (lineIndex <= 0 || lineIndex > hologram.getLines().length) {
                sender.sendMessage(Strings.DISPLAY_PREFIX + " The index you gave was invalid for the hologram '" + hologramName + "', as it has only " + hologram.getLines().length + " lines");
                return;
            }

            int counter = 0;
            ArrayList<HologramItem> townList = plugin.holograms.get(hologram.getTownUUID());
            for (HologramItem holo : townList) {
                if (holo.getName().equals(hologram.getName()) && holo.getTown().equals(hologram.getTown())) {
                    if (holo.getLines().length == 1 && lineIndex == 1) {
                        plugin.deleteHologram(hologram);
                        sender.sendMessage(Strings.DISPLAY_PREFIX + " Hologram '" + hologramName + "' has been removed due to having no more lines!");

                    }
                    else {
                        townList.get(counter).removeLine(lineIndex-1);
                        databaseConnection.updateContent(townList.get(counter).linesToString(), hologramName, plugin.getTownFromPlayer(player).getUuid().toString());
                        sender.sendMessage(Strings.DISPLAY_PREFIX + " The content of hologram '" + hologramName + "' was updated!");

                    }
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

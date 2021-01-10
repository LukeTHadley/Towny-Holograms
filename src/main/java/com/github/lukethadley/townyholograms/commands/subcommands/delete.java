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

public class delete extends SubCommand {


    public delete() {
        super("delete", "Delete a hologram in your town", new String[]{"remove", "del"});
        setPermission(Strings.REGULAR_PERM + "delete");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName>";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public void execute(TownyHolograms plugin, CommandSender sender, String label, String[] args, ConfigValues configValues, DatabaseConnection databaseConnection) throws CommandException {

        Player player = (Player) sender;

        String hologramName = args[0];
        HologramItem getHologram = databaseConnection.getHologram(hologramName, plugin.getTownFromPlayer(player).getUuid().toString());

        if (getHologram == null){
            sender.sendMessage(Strings.DISPLAY_PREFIX + " A hologram with the name '" + hologramName + "' does not exist!");
            return;
        }

        if(databaseConnection.popHologram(hologramName, plugin.getTownFromPlayer(player).getUuid().toString()) == null){
            sender.sendMessage(Strings.DISPLAY_PREFIX + " An internal error occurred while removing the hologram");
            return;
        }

        plugin.deleteHologram(getHologram);

        sender.sendMessage(Strings.DISPLAY_PREFIX + " Hologram '" + hologramName + "' was removed!");

    }
}

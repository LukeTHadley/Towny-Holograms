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
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class AdminTeleport extends SubCommand {

    public AdminTeleport(){
        super("teleport", "Teleport to the location of the specified hologram.", new String[]{"visit", "tp"});
        setPermission(Permission.ADMIN_TELEPORT);
    }

    @Override
    public String getPossibleArguments() {
        return "<Town Name> <Hologram Name>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public void execute(TownyHolograms plugin, CommandSender sender, String label, String[] args, ConfigValues configValues, DatabaseConnection databaseConnection) throws CommandException {

        Player player = (Player) sender;
        String townName = args[0];
        String hologramName = args[1];
        boolean validTownName = false;
        UUID townUUID = null;

        try {
            for (UUID key : plugin.holograms.keySet()) { //Check that the town name given was valid and is a town with existing holograms
                String name = TownyAPI.getInstance().getDataSource().getTown(key).getName();
                if (townName.equalsIgnoreCase(name)){
                    validTownName = true;
                    townUUID = TownyAPI.getInstance().getDataSource().getTown(key).getUuid();
                    break;
                }
            }
            if (validTownName){
                ArrayList<HologramItem> holograms = plugin.holograms.get(townUUID);
                for (HologramItem hologram : holograms){
                    if (hologram.getName().equalsIgnoreCase(hologramName)){
                        player.teleport(hologram.getLocation());
                        sender.sendMessage( Strings.DISPLAY_PREFIX + " Teleporting you to the location of " + hologramName + " in " + townName + " (X:" + String.format("%.03f", hologram.getLocation().getX()) + " Y:" + String.format("%.03f", hologram.getLocation().getY()) + " Z:" + String.format("%.03f", hologram.getLocation().getZ()) + ")");
                        return;
                    }
                }
                sender.sendMessage(Strings.DISPLAY_PREFIX + " A hologram with that name couldn't be found in the given town");
            }
            else {
                sender.sendMessage(Strings.DISPLAY_PREFIX + " The town name given was either not a valid town name or not a name of a town with existing holograms.");
                return;
            }

        }
        catch (NotRegisteredException exception) {
            sender.sendMessage(Strings.DISPLAY_PREFIX + " An error with towny occurred while executing this command.");
        }

    }
}

package com.github.lukethadley.townyholograms.commands.subcommands;

import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramItem;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Locale;

public class move extends SubCommand {

    public move(){
        super("move", "Moves a hologram to a given position in the town");
        setPermission(Strings.REGULAR_PERM + "move");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName> here|(<XPosition> <YPosition> <ZPosition>)";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public void execute(TownyHolograms plugin, CommandSender sender, String label, String[] args, ConfigValues configValues, DatabaseConnection databaseConnection) throws CommandException {

        Player player = (Player) sender;

        double x, y, z;

        String hologramName = args[0];
        HologramItem hologram = databaseConnection.getHologram(hologramName, plugin.getTownFromPlayer(player).getUuid().toString());

        if (hologram == null){
            sender.sendMessage(Strings.DISPLAY_PREFIX + " A hologram with the name '" + hologramName + "' does not exist!");
            return;
        }


        if (args.length == 2){
            if (args[1].equalsIgnoreCase("here")){

                x = player.getLocation().getX();
                y = player.getLocation().getY();
                z = player.getLocation().getZ();
            }
            else {
                sender.sendMessage(Strings.DISPLAY_PREFIX + " Usage: /" + label + " " + getName() + " " + getPossibleArguments());;
                return;
            }
        }
        else if (args.length == 4){
            x = Double.parseDouble(args[1]);
            y = Double.parseDouble(args[2]);
            z = Double.parseDouble(args[3]);
        }
        else {
            sender.sendMessage(Strings.DISPLAY_PREFIX + " Usage: /" + label + " " + getName() + " " + getPossibleArguments());;
            return;
        }

        Location newLocation = new Location(player.getWorld(), x, y, z);

        try {
            if (TownyAPI.getInstance().isWilderness(newLocation)) {
                sender.sendMessage(Strings.DISPLAY_PREFIX + " You can only move a hologram to a location in your town, not the wild.");
                return;
            }
            Town town = TownyAPI.getInstance().getTownBlock(newLocation).getTown();
            Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());
            if (resident.hasTown()){
                if (resident.getTown().equals(town)) {

                    DecimalFormat df = new DecimalFormat("0.000");


                    plugin.deleteHologram(hologram);

                    Hologram newHologram = HologramsAPI.createHologram(plugin, newLocation); //Make a new hologram
                    HologramItem item = new HologramItem(hologramName, plugin.getTownFromPlayer(player).getUuid().toString(), hologram.linesToString(), newLocation, newHologram);
                    plugin.addHologram(item);
                    sender.sendMessage(Strings.DISPLAY_PREFIX + " The location of hologram '" + hologramName + "' was moved to X:" + df.format(x) + " Y:" + df.format(y) + " Z:" + df.format(z));
                    return;

                } else {
                    sender.sendMessage(Strings.DISPLAY_PREFIX + " You can only move a hologram to a location in your town, not another town.");
                    return;
                }
            } else {
                sender.sendMessage(Strings.DISPLAY_PREFIX + " You don't belong to a town.");
                return;
            }


        } catch (NotRegisteredException e) {
            //Code put here can also be code that ought to be run if the player is in the wilderness and not a town.
            e.printStackTrace();
        } catch (NumberFormatException e){
            sender.sendMessage(Strings.DISPLAY_PREFIX + " Please enter valid integer or decimal values.");
            return;
        }
        catch (ArrayIndexOutOfBoundsException e){
            sender.sendMessage(Strings.DISPLAY_PREFIX + " Usage: /" + label + " " + getName() + " " + getPossibleArguments());
            return;
        }
    }
}

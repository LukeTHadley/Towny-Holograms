package com.github.lukethadley.townyholograms.commands.subcommands;

import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class test extends SubCommand {

    public test() {
        super("test", "A test command");
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

        Player player = (Player) sender;

        try {
            if (TownyAPI.getInstance().isWilderness(player.getLocation())) {
                System.out.println("WHILD");
                return;
            }
            Town town = TownyAPI.getInstance().getTownBlock(player.getLocation()).getTown();
            Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());




            if (resident.hasTown()){
                if (resident.getTown().equals(town)) {
                    System.out.println("THIS IS YOUR TOWN");
                    return;
                } else {
                    System.out.println("YTHIS IS NOT YOUR TOWN");
                    return;
                }
            } else {
                System.out.println("YOU DONT BELONG TO A TOWN");
                return;
            }




        } catch (NotRegisteredException e) {
            //Code put here can also be code that ought to be run if the player is in the wilderness and not a town.
            e.printStackTrace();
        }

        /*
        if (TownyAPI.getInstance().isWilderness(player.getLocation())) {
            sender.sendMessage(Strings.DISPLAY_PREFIX + " You can not move a hologram wilderness.");
            return;
        }



        try {

            Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());
            Town town = TownyAPI.getInstance().getTownBlock(player.getLocation()).getTown();



            if (TownyAPI.getInstance().getTown()){ //Player doesn't have a town
                sender.sendMessage("You do have a town");
            }


        } catch (NotRegisteredException e) {
            //Code put here can also be code that ought to be run if the player is in the wilderness and not a town.
            e.printStackTrace();
            System.out.println("!");
        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("!!");
        }

    }

    public boolean isLocationInTown(Location location){
        try {
            if (TownyAPI.getInstance().isWilderness(location)) {
                return true;
            }
            if (TownyAPI.getInstance().getTown(location)){
                return true;
            }

        } catch (NotRegisteredException e){
            return false;
        }




        return false;
    }
*/}

}

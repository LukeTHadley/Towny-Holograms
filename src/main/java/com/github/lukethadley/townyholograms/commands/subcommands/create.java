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
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class create extends SubCommand {

    public create(){
        super("create", "Make a new hologram in your town", new String[]{"new"});

        setPermission(Strings.REGULAR_PERM + "create");
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

        if (TownyAPI.getInstance().isWilderness(player.getLocation())) {
            sender.sendMessage(Strings.DISPLAY_PREFIX + " You can not create a new hologram in the wilderness.");
            return;
        }

        try {
            Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());
            Town town = TownyAPI.getInstance().getTownBlock(player.getLocation()).getTown();

            if (resident.getTown().equals(town)) { //Player is in their town

                String hologramName = args[0];

                if ( town.getAccount().getHoldingBalance() < configValues.getNewHologramPrice()){
                    sender.sendMessage(Strings.DISPLAY_PREFIX + " You don't have enough funds in the town bank for a hologram.");
                } else {
                    town.getAccount().withdraw(configValues.getNewHologramPrice(), "TownyHologram - Creation of hologram " + hologramName);


                    HologramItem getHologram = databaseConnection.getHologram(hologramName, town.getUuid().toString());

                    if (getHologram != null) {
                        sender.sendMessage(Strings.DISPLAY_PREFIX + " A hologram with the name '" + hologramName + "' already exists!");
                        return;
                    }


                    ArrayList<HologramItem> items = plugin.holograms.get(town.getUuid());
                    int hologramCount;
                    if (items == null){
                        hologramCount = 0;
                    } else {
                        hologramCount = items.size();
                    }

                    if (hologramCount >= plugin.getHologramAllowance(town).getNumberOfHolograms()){
                        sender.sendMessage(Strings.DISPLAY_PREFIX + " You can't have any more holograms at your current town size.");
                        return;
                    }



                    String hologramText = ChatColor.AQUA + "New Hologram!";

                    if (args.length > 1) { //There is text in the argument to add to the hologram.
                        hologramText = "";
                        for (int i = 1; i < args.length; i++) {
                            if (i == args.length - 1) {
                                hologramText = hologramText + args[i];
                            } else {
                                hologramText = hologramText + args[i] + " ";
                            }
                        }
                    }

                    String formattedHologramText = ChatColor.translateAlternateColorCodes('&', hologramText);


                    Hologram hologram = HologramsAPI.createHologram(plugin, player.getLocation());

                    HologramItem newHologram = new HologramItem(hologramName, plugin.getTownFromPlayer(player).getUuid().toString(), formattedHologramText, player.getLocation(), hologram);


                    plugin.addHologram(newHologram);


                    sender.sendMessage(Strings.DISPLAY_PREFIX + " Creating new hologram '" + hologramName + "' in " + plugin.getTownFromPlayer(player).getName() + " with text '" + formattedHologramText + ChatColor.AQUA + "' for " + configValues.getNewHologramPrice() + ".");
                }
            }
            else {
                sender.sendMessage(Strings.DISPLAY_PREFIX + " You can't make a hologram in another players town.");
                return;
            }



        } catch (NotRegisteredException e) {// I DON'T LIKE THIS
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (EconomyException e) {
            e.printStackTrace();
        }


    }
}

package com.github.lukethadley.townyholograms.commands.subcommands.general;

import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.Permission;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramItem;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GeneralList extends SubCommand {

    public GeneralList() {
        super("list", "Show a list of all holograms in your town");
        setPermission(Permission.LIST);
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
            Town town = TownyAPI.getInstance().getTownBlock(player.getLocation()).getTown();
            int townResidentCount = town.getNumResidents();
            ArrayList<HologramItem> holograms = plugin.holograms.get(town.getUuid());

            sender.sendMessage(ChatColor.GOLD + ".oOo.___________.[ " + ChatColor.YELLOW + town.getName() + " Holograms " + ChatColor.GOLD + "].___________.oOo.");

            if (holograms.size() == 0) {
                sender.sendMessage(ChatColor.DARK_GREEN + "Your town has no holograms at the moment.");
                return;
            }

            sender.sendMessage(ChatColor.DARK_GREEN + "Your town is using " + ChatColor.GREEN + holograms.size() + ChatColor.DARK_GREEN + "/" + ChatColor.GREEN + configValues.getClosestAllowance(townResidentCount).getMaxNumber() + ChatColor.DARK_GREEN + " allowed holograms.");
            for (HologramItem hologram : holograms) {
                sender.sendMessage(" " + ChatColor.DARK_AQUA + hologram.getName() + ChatColor.GRAY + " - " + ChatColor.AQUA + hologram.getLocation().getWorld().getName() + " X:" + String.format("%.03f", hologram.getLocation().getX()) + " Y:" + String.format("%.03f", hologram.getLocation().getY()) + " Z:" + String.format("%.03f", hologram.getLocation().getZ()));
            }

        } catch (NotRegisteredException e){
            plugin.getLogger().warning("An issue occurred with towny listing town holograms.");
        }
        catch (NullPointerException e){
            plugin.getLogger().warning("An issue occurred while listing town holograms.");
        }
    }
}

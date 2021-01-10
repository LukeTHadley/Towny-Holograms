package com.github.lukethadley.townyholograms.commands;

import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.subcommands.*;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler implements TabExecutor {

    private List<SubCommand> subCommands;
    private ConfigValues configValues;
    private DatabaseConnection databaseConnection;
    private TownyHolograms plugin;

    public CommandHandler(TownyHolograms plugin, ConfigValues configValues, DatabaseConnection databaseConnection){
        this.plugin = plugin;
        this.configValues = configValues;
        this.databaseConnection = databaseConnection;
        subCommands = new ArrayList<>();

        help helpCommand = new help();

        subCommands.add(new create());
        subCommands.add(new delete());
        subCommands.add(new list());
        subCommands.add(new info());
        subCommands.add(new addline());
        subCommands.add(new removeline());
        subCommands.add(new setline());
        subCommands.add(new insertline());
        subCommands.add(new move());

        subCommands.add(helpCommand);
        subCommands.add(new prices());
        subCommands.add(new test());

        helpCommand.setSubCommands(subCommands);
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (sender instanceof Player){ //Check that the sender was a player


            if (args.length == 0) {
                sender.sendMessage(ChatColor.GOLD + ".oOo._________.[" + ChatColor.YELLOW + " Towny Hologram " + ChatColor.GOLD + "]._________.oOo.");
                sender.sendMessage(ChatColor.DARK_GREEN + "Version: '" + ChatColor.GREEN + plugin.getDescription().getVersion() + ChatColor.DARK_GREEN + "'" + ChatColor.GRAY + " | " + ChatColor.DARK_GREEN + "Authors: " + ChatColor.GREEN + plugin.getDescription().getAuthors().toString());
                sender.sendMessage(ChatColor.RED + "Town Workers:" + ChatColor.DARK_AQUA + " /" + label +  " help" + ChatColor.AQUA + " Show a list of usable commands.");
                sender.sendMessage(ChatColor.RED + "Admin:" + ChatColor.DARK_AQUA + " /" + label + " admin" + ChatColor.AQUA + " Show a list of admin commands.");

                return true;
            }



            for (SubCommand subCommand : subCommands) {
                if (subCommand.isValidTrigger(args[0])) {


                    try {
                        Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());
                        if (!resident.hasTown()) { //Player doesn't have a town
                            sender.sendMessage(Strings.DISPLAY_PREFIX + " You must belong to a town to use that command.");
                            return true;
                        }

                        if (!subCommand.hasPermission(sender)) {
                            sender.sendMessage(Strings.DISPLAY_PREFIX + " You don't have permission.");
                            return true;
                        }

                    } catch (NotRegisteredException e) {
                        e.printStackTrace();
                    }


                    if (args.length - 1 >= subCommand.getMinimumArguments()) {
                        try {
                            subCommand.execute(plugin, sender, label, Arrays.copyOfRange(args, 1, args.length), configValues, databaseConnection);
                        } catch (CommandException e) {
                            sender.sendMessage(e.getMessage());
                        }
                    } else {
                        sender.sendMessage(Strings.DISPLAY_PREFIX + " Usage: /" + label + " " + subCommand.getName() + " " + subCommand.getPossibleArguments());
                    }

                    return true;
                }
            }


            sender.sendMessage(Strings.DISPLAY_PREFIX + ChatColor.RED + " Unknown Command, please do /" + label + " help for a list of all commands and usage.");


        }
        else { //Console was the sender
            sender.sendMessage(Strings.DISPLAY_PREFIX + " Hello Console!");
        }

        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        try {
            if (args.length == 1) {
                for (SubCommand commandName : subCommands) {
                    for (String commandAlias : commandName.getAliases()){
                        if (commandAlias.startsWith(args[0])){
                            suggestions.add(commandAlias);
                        }
                    }
                    if (commandName.getName().startsWith(args[0])) {
                        suggestions.add(commandName.getName());
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException e){ }

        return suggestions;
    }





}

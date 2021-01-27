package com.github.lukethadley.townyholograms.commands.subcommands.admin;

import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.Permission;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AdminHelp extends SubCommand {

    private List<SubCommand> subCommands;

    public AdminHelp(){
        super("help", "Admin help command.");
        setPermission(Permission.ADMIN_HELP);
        setConsoleCommand(true);
    }

    public void setSubCommands(List<SubCommand> cmds){
        subCommands = cmds;
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
        sender.sendMessage(ChatColor.GOLD + ".oOo.________.[" + ChatColor.YELLOW + " Towny Hologram Admin Help " + ChatColor.GOLD + "].________.oOo.");
        for (SubCommand cmd : subCommands){
            sender.sendMessage("  " + ChatColor.DARK_AQUA + "/" + label + " " + cmd.getName() + " " + ChatColor.AQUA + cmd.getPossibleArguments() + " " + ChatColor.GRAY + cmd.getDescription());
        }
    }
}

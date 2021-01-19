package com.github.lukethadley.townyholograms.commands.subcommands;

import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.Permission;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

public class help extends SubCommand {

    private List<SubCommand> subCommands;

    public help(){
        super("help", "Show a list of all plugin commands.", new String[]{"?"});
        setPermission(Permission.HELP);
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
        sender.sendMessage(ChatColor.GOLD + ".oOo.___________.[" + ChatColor.YELLOW + " Towny Hologram Help " + ChatColor.GOLD + "].___________.oOo.");
        for (SubCommand cmd : subCommands){
            sender.sendMessage("  " + ChatColor.DARK_AQUA + "/" + label + " " + cmd.getName() + " " + ChatColor.AQUA + cmd.getPossibleArguments() + " " + ChatColor.GRAY + cmd.getDescription());
        }
    }


}

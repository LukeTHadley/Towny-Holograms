package com.github.lukethadley.townyholograms.commands;

import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    private String name;
    private String permission;
    private String[] aliases;
    private String description;
    private int argumentCount;

    public SubCommand(String name, String description){
        this.name = name;
        this.description = description;
    }

    public SubCommand(String name, String description, String[] aliases){
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    public String getDescription(){
        return description;
    }

    public String getName(){
        return name;
    }

    public String[] getAliases(){
        if (aliases == null){
            return new String[]{};
        }
        else {
            return aliases;
        }
    }

    public void setPermission(String permission){
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }

    public final boolean hasPermission(CommandSender sender) {
        if (permission == null) return true;
        return sender.hasPermission(permission);
    }

    public abstract String getPossibleArguments();

    public abstract int getMinimumArguments();

    public final boolean isValidTrigger(String name) {
        if (this.name.equalsIgnoreCase(name)) {
            return true;
        }

        if (aliases != null) {
            for (String alias : aliases) {
                if (alias.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }

        return false;
    }

    public abstract void execute(TownyHolograms plugin, CommandSender sender, String label, String[] args, ConfigValues configValues, DatabaseConnection databaseConnection) throws CommandException;
}

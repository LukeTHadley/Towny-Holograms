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
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.UUID;

public class AdminInfo extends SubCommand {

    public AdminInfo() {
        super("info", "Get information on a specific hologram in your town", new String[]{"i"});
        setPermission(Permission.ADMIN_INFO);
        setConsoleCommand(true);
    }

    @Override
    public String getPossibleArguments() {
        return "<townName> <hologramName>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public void execute(TownyHolograms plugin, CommandSender sender, String label, String[] args, ConfigValues configValues, DatabaseConnection databaseConnection) throws CommandException {

        String townName = args[0];
        String hologramName = args[1];
        boolean validTownname = false;
        UUID townUUID = null;

        try {

            for (UUID key : plugin.holograms.keySet()) { //Check that the town name given was valid and is a town with existing holograms
                String name = TownyAPI.getInstance().getDataSource().getTown(key).getName();
                if (townName.equalsIgnoreCase(name)){
                    validTownname = true;
                    townUUID = TownyAPI.getInstance().getDataSource().getTown(key).getUuid();
                    break;
                }
            }
            if (validTownname){

                ArrayList<HologramItem> holograms = plugin.holograms.get(townUUID);
                for (HologramItem hologram : holograms){
                    if (hologram.getName().equalsIgnoreCase(hologramName)){
                        sender.sendMessage(ChatColor.GOLD + ".oOo.___________.[" + ChatColor.YELLOW + " Hologram Information " + ChatColor.GOLD + "].___________.oOo.");
                        sender.sendMessage(" " + ChatColor.DARK_GREEN + "Name: " + ChatColor.GREEN + hologram.getName());
                        sender.sendMessage(" " + ChatColor.DARK_GREEN + "Town: " + ChatColor.GREEN + townName);
                        sender.sendMessage(" " + ChatColor.DARK_GREEN + "World Name: " + ChatColor.GREEN + hologram.getLocation().getWorld().getName());
                        sender.sendMessage(" " + ChatColor.DARK_GREEN + "X: " + ChatColor.GREEN + hologram.getLocation().getX());
                        sender.sendMessage(" " + ChatColor.DARK_GREEN + "Y: " + ChatColor.GREEN + hologram.getLocation().getY());
                        sender.sendMessage(" " + ChatColor.DARK_GREEN + "Z: " + ChatColor.GREEN + hologram.getLocation().getZ());
                        for (int i = 0; i < hologram.getLines().length; i++){
                            if (i == 0){
                                sender.sendMessage(" " + ChatColor.DARK_GREEN + "Lines " + ChatColor.GREEN + "[" + hologram.getLines().length + "]" + ChatColor.DARK_GREEN + ": " + ChatColor.RESET + hologram.getLines()[i]);
                            }
                            else {
                                sender.sendMessage("              " + ChatColor.RESET + hologram.getLines()[i]);
                            }
                        }

                        if (sender instanceof ConsoleCommandSender){
                            return;
                        }

                        BaseComponent footer = new TextComponent();

                        TextComponent deleteComponent = new TextComponent(ChatColor.DARK_RED + "[Delete]");
                        deleteComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram admin delete " + townName + " " + hologramName)));
                        deleteComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Delete Hologram" ).create() ) );

                        TextComponent removeLineComponent = new TextComponent(ChatColor.RED + " [Delete Line]");
                        removeLineComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram admin removeline " + townName + " " + hologramName)));
                        removeLineComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Remove a line on the hologram" ).create() ) );

                        TextComponent addLineComponent = new TextComponent(ChatColor.AQUA + " [Add Line]");
                        addLineComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram admin addline " + townName + " " + hologramName)));
                        addLineComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Add a line to the hologram" ).create() ) );

                        TextComponent setLineComponent = new TextComponent(ChatColor.AQUA + " [Set Line]");
                        setLineComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram admin setline " + townName + " " + hologramName)));
                        setLineComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Set a specific line on the hologram to something else" ).create() ) );

                        TextComponent moveHereComponent = new TextComponent(ChatColor.DARK_AQUA + " [Move]");
                        moveHereComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram admin move " + townName + " " + hologramName)));
                        moveHereComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Move a hologram to your current position" ).create() ) );

                        TextComponent teleportToComponent = new TextComponent(ChatColor.RED + " [Go To]");
                        teleportToComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram admin teleport " + townName + " " + hologramName)));
                        teleportToComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Teleport to a specific hologram" ).create() ) );

                        footer.addExtra(deleteComponent);
                        footer.addExtra(removeLineComponent);
                        footer.addExtra(addLineComponent);
                        footer.addExtra(setLineComponent);
                        footer.addExtra(moveHereComponent);
                        footer.addExtra(teleportToComponent);


                        sender.spigot().sendMessage(footer);

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
package com.github.lukethadley.townyholograms.commands.subcommands;

import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramItem;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.w3c.dom.Text;

public class info extends SubCommand {

    public info() {
        super("info", "Get information on a specific hologram in your town");
        setPermission(Strings.REGULAR_PERM + "info");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName>";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public void execute(TownyHolograms plugin, CommandSender sender, String label, String[] args, ConfigValues configValues, DatabaseConnection databaseConnection) throws CommandException {

        try {

        String hologramName = args[0];
        Player player = (Player) sender;
        HologramItem hologram = databaseConnection.getHologram(hologramName, plugin.getTownFromPlayer(player).getUuid().toString());

        Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());
        Town town = TownyAPI.getInstance().getTownBlock(player.getLocation()).getTown();

        if (hologram == null){
            sender.sendMessage(Strings.DISPLAY_PREFIX + " A hologram with the name '" + hologramName + "' does not exists!");
            return;
        }
            sender.sendMessage(ChatColor.GOLD + ".oOo.___________.[" + ChatColor.YELLOW + " Hologram Information " + ChatColor.GOLD + "].___________.oOo.");
            sender.sendMessage(" " + ChatColor.DARK_GREEN + "Name: " + ChatColor.GREEN + hologram.getName());
            sender.sendMessage(" " + ChatColor.DARK_GREEN + "Town: " + ChatColor.GREEN + town.getName());
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


        BaseComponent footer = new TextComponent();

        TextComponent deleteComponent = new TextComponent(ChatColor.DARK_RED + "[Delete]");
        deleteComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram delete " + hologramName)));
        deleteComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Delete Hologram" ).create() ) );

        TextComponent removeLineComponent = new TextComponent(ChatColor.RED + " [Delete Line]");
        removeLineComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram removeline " + hologramName)));
        removeLineComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Remove a line on the hologram" ).create() ) );

        TextComponent addLineComponent = new TextComponent(ChatColor.AQUA + " [Add Line]");
        addLineComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram addline " + hologramName)));
        addLineComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Add a line to the hologram" ).create() ) );

        TextComponent setLineComponent = new TextComponent(ChatColor.AQUA + " [Set Line]");
        setLineComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram setline " + hologramName)));
        setLineComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Set a specific line on the hologram to something else" ).create() ) );

        TextComponent moveHereComponent = new TextComponent(ChatColor.DARK_AQUA + " [Move Here]");
        moveHereComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram move " + hologramName)));
        moveHereComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Move a hologram to your current position" ).create() ) );

        TextComponent teleportToComponent = new TextComponent(ChatColor.RED + " [Go To]");
        teleportToComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ("/townyhologram teleport " + hologramName)));
        teleportToComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Teleport to a specific hologram" ).create() ) );

        footer.addExtra(deleteComponent);
        footer.addExtra(removeLineComponent);
        footer.addExtra(addLineComponent);
        footer.addExtra(setLineComponent);
        footer.addExtra(moveHereComponent);
        footer.addExtra(teleportToComponent);


        player.spigot().sendMessage(footer);


    }


         catch (NotRegisteredException exception) {
            exception.printStackTrace();
        }

        /*

        TextComponent mainComponent = new TextComponent( "Here's a question: " );
        mainComponent.setColor( ChatColor.GOLD.asBungee() );

        TextComponent subComponent = new TextComponent( "Maybe u r noob?" );
        subComponent.setColor( ChatColor.AQUA.asBungee() );

        subComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Click me!" ).create() ) );
        subComponent.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/wiki/the-chat-component-api/" ) );



        mainComponent.addExtra( subComponent );
        mainComponent.addExtra( " Does that answer your question?" );


        player.spigot().sendMessage( mainComponent );*/

    }

}

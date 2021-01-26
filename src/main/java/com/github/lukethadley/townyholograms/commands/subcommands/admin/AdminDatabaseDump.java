package com.github.lukethadley.townyholograms.commands.subcommands.admin;

import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.TownyHolograms;
import com.github.lukethadley.townyholograms.commands.Permission;
import com.github.lukethadley.townyholograms.commands.SubCommand;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import com.github.lukethadley.townyholograms.storage.database.DatabaseStatements;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDatabaseDump extends SubCommand {

    public AdminDatabaseDump(){
        super("databasedump", "Outputs all records in the database table to a '.csv' file in the plugin directory.", new String[]{"dbdump"});
        setPermission(Permission.ADMIN_DATABASE_DUMP);
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

        try{
            ResultSet results = databaseConnection.queary(DatabaseStatements.DATABASE_QUEARY_ALL);
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy@HH-mm");
            String filePath = plugin.getDataFolder() + "/TownyHolograms_DBDUMP_" + format.format(now) + ".csv";
            FileWriter file = new FileWriter(filePath);
            String world, name, town, content, x, y, z;

            file.write("WORLD UUID,HOLOGRAM NAME,TOWN UUID,HOLOGRAM CONTENT,X,Y,Z\n");

            while (results.next()) {
               world = results.getString("world");
               name = results.getString("name");
               town = results.getString("town");
               content = results.getString("content").replace("\n", "\t");
               x = Double.toString(results.getDouble("x"));
               y = Double.toString(results.getDouble("y"));
               z = Double.toString(results.getDouble("z"));
               file.write(world + "," + name + "," + town + "," + content + "," + x + "," + y + "," + z + "\n");
            }

            file.close();
            sender.sendMessage(Strings.DISPLAY_PREFIX + " Created Database Dump '" + filePath + "'.");

        }
        catch (IOException | SQLException e){
            sender.sendMessage("An issue occured connecting to the database.");
        }
    }

}

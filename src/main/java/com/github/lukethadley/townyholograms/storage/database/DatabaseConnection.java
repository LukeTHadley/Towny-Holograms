package com.github.lukethadley.townyholograms.storage.database;


import com.github.lukethadley.townyholograms.Strings;
import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramItem;
import com.github.lukethadley.townyholograms.storage.SQLCredentials;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class DatabaseConnection {

    private SQLCredentials sqlCredentials;
    static Connection connection; //This is the variable we will use to connect to database

    public DatabaseConnection(SQLCredentials sqlCredentials) {
        this.sqlCredentials = sqlCredentials;
    }


    public boolean executeStatement(String statement) {
        try {
            PreparedStatement stmt = connection.prepareStatement(statement);
            // I use executeUpdate() to update the databases table.
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean executeStatement(PreparedStatement statement) {
        try {

            // I use executeUpdate() to update the databases table.
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean connect() {
        try { //We use a try catch to avoid errors, hopefully we don't get any.
            Class.forName("com.mysql.jdbc.Driver"); //this accesses Driver in jdbc.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("jdbc driver unavailable!");
            return false;
        }
        try { //Another try catch to get any SQL errors (for example connections errors)
            connection = DriverManager.getConnection(sqlCredentials.getUrl(), sqlCredentials.getUsername(), sqlCredentials.getPassword());
            //with the method getConnection() from DriverManager, we're trying to set
            //the connection's url, username, password to the variables we made earlier and
            //trying to get a connection at the same time. JDBC allows us to do this.
        } catch (SQLException e) { //catching errors)
            //e.printStackTrace(); //prints out SQLException errors to the console (if any)
            return false;
        }
        return true;
    }


    public boolean disconnect() {
        try { //using a try catch to catch connection errors (like wrong sql password...)
            if (connection != null && !connection.isClosed()) { //checking if connection isn't null to
                //avoid receiving a nullpointer
                connection.close(); //closing the connection field variable.
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public ResultSet queary(String statement) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(statement);
        ResultSet results = stmt.executeQuery();
        return results;
    }

    public ResultSet queary(PreparedStatement statement) throws SQLException {
        ResultSet results = statement.executeQuery();
        return results;
    }

    public int getTotalHolograms() {
        int size = 0;
        try {
            ResultSet results = queary(DatabaseStatements.DATABASE_QUEARY_TOTAL_RECORDS);
            if (results != null) {
                results.next();
                size = results.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return size;
    }

    public boolean addHologram(HologramItem hologram) {
        try {
            PreparedStatement statement = connection.prepareStatement(DatabaseStatements.DATABASE_INSERT_HOLOGRAM);
            statement.setString(1, hologram.getName());
            statement.setString(2, hologram.getTown());
            statement.setString(3, hologram.getLocation().getWorld().getUID().toString());
            statement.setString(4, hologram.linesToString());
            statement.setDouble(5, hologram.getLocation().getX());
            statement.setDouble(6, hologram.getLocation().getY());
            statement.setDouble(7, hologram.getLocation().getZ());
            executeStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public HologramItem getHologram(String name, String town) {
        HologramItem hologram;
        try {
            PreparedStatement statement = connection.prepareStatement(DatabaseStatements.DATABASE_QUEARY_EXISTING_HOLOGRAM);
            statement.setString(1, town);
            statement.setString(2, name);
            ResultSet result = queary(statement);
            if (!result.next()) {

                return null;
            }
            World locationWorld = Bukkit.getServer().getWorld(UUID.fromString(result.getString("world")));
            hologram = new HologramItem(result.getString("name"),
                    result.getString("town"),
                    result.getString("content"),
                    (new Location(locationWorld,
                            result.getDouble("x"),
                            result.getDouble("y"),
                            result.getDouble("z")))
            );


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            System.out.println(Strings.CONSOLE_DISPLAY_PREFIX + "An issue occurred while getting world information from SQL");
            e.printStackTrace();
            return null;
        }
        return hologram;
    }

    public ArrayList<HologramItem> getHolograms(String town) {
        ArrayList<HologramItem> hologramItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(DatabaseStatements.DATABASE_QUEARY_ALL_IN_TOWN);
            statement.setString(1, town);
            ResultSet result = queary(statement);
            while (result.next()) {
                World locationWorld = Bukkit.getServer().getWorld(UUID.fromString(result.getString("world")));

                hologramItems.add(new HologramItem(
                        result.getString("name"),
                        result.getString("town"),
                        result.getString("content"),
                        (new Location(locationWorld,
                                result.getDouble("x"),
                                result.getDouble("y"),
                                result.getDouble("z")))
                ));
            }


        } catch (SQLException e) {
            return hologramItems;
        } catch (NullPointerException e) {
            System.out.println(Strings.CONSOLE_DISPLAY_PREFIX + "An issue occurred while getting world information from SQL");
            e.printStackTrace();
        }
        return hologramItems;
    }

    public HologramItem popHologram(String name, String town) {
        HologramItem hologram = getHologram(name, town);
        try {
            PreparedStatement statement = connection.prepareStatement(DatabaseStatements.DATABASE_REMOVE_RECORD);
            statement.setString(1, town);
            statement.setString(2, name);
            executeStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return hologram;
    }

    public boolean updateContent(String content, String name, String town){
        try {
            PreparedStatement statement = connection.prepareStatement(DatabaseStatements.DATABASE_UPDATE_CONTENT);
            statement.setString(1, content);
            statement.setString(2, name);
            statement.setString(3, town);
            executeStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<HologramItem> getAllHolograms(){
        ArrayList<HologramItem> hologramItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(DatabaseStatements.DATABASE_QUEARY_ALL);
            ResultSet result = queary(statement);
            while (result.next()) {
                World locationWorld = Bukkit.getServer().getWorld(UUID.fromString(result.getString("world")));

                hologramItems.add(new HologramItem(
                        result.getString("name"),
                        result.getString("town"),
                        result.getString("content"),
                        (new Location(locationWorld,
                                result.getDouble("x"),
                                result.getDouble("y"),
                                result.getDouble("z")))
                ));
            }


        } catch (SQLException e) {
            return hologramItems;
        }
        return hologramItems;
    }

    public boolean removeHologram(String name, String town){
        try {
            PreparedStatement statement = connection.prepareStatement(DatabaseStatements.DATABASE_REMOVE_RECORD);
            statement.setString(1, town);
            statement.setString(2, name);
            executeStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<HologramItem> getHologramsFromChunk(int x, int z){
        ArrayList<HologramItem> hologramItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(DatabaseStatements.DATABASE_QUEARY_ALL_FROM_CHUNK);
            statement.setInt(1, x);
            statement.setInt(2, z);
            ResultSet result = queary(statement);
            while (result.next()) {
                World locationWorld = Bukkit.getServer().getWorld(UUID.fromString(result.getString("world")));

                hologramItems.add(new HologramItem(
                        result.getString("name"),
                        result.getString("town"),
                        result.getString("content"),
                        (new Location(locationWorld,
                                result.getDouble("x"),
                                result.getDouble("y"),
                                result.getDouble("z")))
                ));
            }


        } catch (SQLException e) {
            return hologramItems;
        }
        return hologramItems;
    }


    public void removeAllFromTown(String town){
        try {
            PreparedStatement statement = connection.prepareStatement(DatabaseStatements.DATABASE_REMOVE_ALL_TOWN_RECORDS);
            statement.setString(1, town);
            executeStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        return;
    }

}

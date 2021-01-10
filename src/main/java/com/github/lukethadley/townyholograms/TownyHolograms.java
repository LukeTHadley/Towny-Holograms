package com.github.lukethadley.townyholograms;

import com.github.lukethadley.townyholograms.commands.CommandHandler;

import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramAllowance;
import com.github.lukethadley.townyholograms.storage.HologramItem;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import com.github.lukethadley.townyholograms.storage.database.DatabaseStatements;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;


public final class TownyHolograms extends JavaPlugin {

    private ConfigValues configurationValues;
    private DatabaseConnection databaseConnection;

    public Map<UUID, ArrayList<HologramItem>> holograms;
    public List<HologramAllowance> prices;

    @Override
    public void onEnable() {
        configurationValues = new ConfigValues(this);
        databaseConnection = new DatabaseConnection(configurationValues);

        saveDefaultConfig();
        prices = configurationValues.getHologramAllowances();


        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays") || !Bukkit.getPluginManager().isPluginEnabled("Towny")) {
            getLogger().log(Level.WARNING, "This plugin requires 'HolographicDisplays' or 'TownyAdvanced' which can not be found.");
            getLogger().log(Level.WARNING, "This plugin will be disabled.");
            this.setEnabled(false);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }



        if (!databaseConnection.connect()){ //Try connecting to the database using credentials
            getLogger().log(Level.WARNING, "Unable to connect to the SQL database, please check credentials in 'config.yml'.");
            getLogger().log(Level.WARNING, "This plugin will be disabled.");
            this.setEnabled(false);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        if (!databaseConnection.executeStatement(DatabaseStatements.DATABASE_CREATE_STATEMENT)){ //Create database if needed
            getLogger().log(Level.WARNING, "Unable to create database! Are the credentials correct?");
            getLogger().log(Level.WARNING, "This plugin will be disabled.");
            this.setEnabled(false);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (this.isEnabled()){
            holograms = loadHolograms();
            getCommand("townyHolograms").setExecutor(new CommandHandler(this, configurationValues, databaseConnection)); //Setup Command Handler
            getServer().getPluginManager().registerEvents(new TownyEventListener(this, configurationValues, databaseConnection), this); //Setup Event Handling
            getLogger().log(Level.INFO, "Finished loading and setting up " + getTotalHolograms() + " holograms.");
        }


    }

    @Override
    public void onDisable() {
        try {
            unloadHolograms();
            if (!databaseConnection.disconnect()){
                getLogger().log(Level.INFO, "An error occurred while disconnecting from the SQL database.");
            }
        } catch (NullPointerException e){
            return;
        }



    }

    private Map<UUID, ArrayList<HologramItem>> loadHolograms(){
        getLogger().log(Level.INFO, "Starting loading and setting up holograms...");

        ArrayList<HologramItem> holograms = databaseConnection.getAllHolograms();
        Map<UUID, ArrayList<HologramItem>> newList = new HashMap<>();

        for (HologramItem holo: holograms){
            ArrayList<HologramItem> items;
            if (newList.containsKey(holo.getTownUUID())){
                items = newList.get(holo.getTownUUID());
                Hologram hologram = HologramsAPI.createHologram(this, holo.getLocation());
                for (String line : holo.getLines()){
                    hologram.appendTextLine(line);
                }
                holo.setHologram(hologram);
                items.add(holo);
                newList.replace(holo.getTownUUID(), items);

            }
            else {
                items = new ArrayList<>();
                Hologram hologram = HologramsAPI.createHologram(this, holo.getLocation());
                for (String line : holo.getLines()){
                    hologram.appendTextLine(line);
                }
                holo.setHologram(hologram);
                items.add(holo);
                newList.put(holo.getTownUUID(), items);
            }
        }

        return newList;
    }

    private void unloadHolograms(){

        try {
            for (Map.Entry<UUID, ArrayList<HologramItem>> townSet : holograms.entrySet()){
                for (HologramItem holo : townSet.getValue()){
                    holo.getHologram().delete();
                }
            }
        } catch (NullPointerException e){
            return;
        }


    }

    public void addHologram(HologramItem hologram){
        ArrayList<HologramItem> items;
        if (holograms.containsKey(hologram.getTownUUID())){
            items = holograms.get(hologram.getTownUUID());
            items.add(hologram);
            holograms.replace(hologram.getTownUUID(), items);
        }
        else {
            items = new ArrayList<>();
            items.add(hologram);
            holograms.put(hologram.getTownUUID(), items);
        }
        databaseConnection.addHologram(hologram);

    }

    public void deleteHologram(HologramItem hologram){

        ArrayList<HologramItem> townList = holograms.get(hologram.getTownUUID());

        int counter = 0;
        for (HologramItem holo : townList){
            if (holo.getName().equals(hologram.getName()) && holo.getTown().equals(hologram.getTown())){
                townList.get(counter).getHologram().delete();
                townList.remove(holo);
                databaseConnection.removeHologram(hologram.getName(), hologram.getTown());
                holograms.replace(hologram.getTownUUID(), townList);
                return;
            }
            counter++;
        }

    }

    public Town getTownFromPlayer(Player player){
        try {
            Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());
            return TownyAPI.getInstance().getDataSource().getTown(resident.getTown().getUuid());
        }
        catch (NotRegisteredException e){
            return null;
        }
    }

    public int getTotalHolograms(){
        int counter = 0;
        for (Map.Entry<UUID, ArrayList<HologramItem>> holoSet : holograms.entrySet()){
            counter = counter + holoSet.getValue().size();
        }
        return counter;
    }

    public HologramAllowance getHologramAllowance(Town town){
            HologramAllowance closest = new HologramAllowance(0, 0);
            for (HologramAllowance allowance : prices){
                if (allowance.getNumberOfResidents() <= town.getNumResidents() && allowance.getNumberOfResidents() > closest.getNumberOfResidents()){
                    closest = allowance;
                }
            }
            return closest;
    }





}

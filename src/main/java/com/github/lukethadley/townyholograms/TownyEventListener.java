package com.github.lukethadley.townyholograms;


import com.github.lukethadley.townyholograms.storage.ConfigValues;
import com.github.lukethadley.townyholograms.storage.HologramItem;
import com.github.lukethadley.townyholograms.storage.database.DatabaseConnection;
import com.palmergames.bukkit.towny.event.PlotClearEvent;
import com.palmergames.bukkit.towny.event.PreDeleteTownEvent;
import com.palmergames.bukkit.towny.event.TownUnclaimEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.palmergames.bukkit.towny.event.DeleteTownEvent;

import java.util.ArrayList;
import java.util.logging.Level;


public class TownyEventListener implements Listener {

    private TownyHolograms plugin;
    private ConfigValues configValues;
    private DatabaseConnection database;

    private boolean townDeleteInProgress = false; //Is a town delete event coming

    public TownyEventListener(TownyHolograms instance, ConfigValues configValues, DatabaseConnection database){
        this.plugin = instance;
        this.configValues = configValues;
        this.database = database;
    }


    private int deleteHologramsInPlot(int plotX, int plotZ, String town){
        ArrayList<HologramItem> chunkHolograms = database.getHologramsFromChunk(plotX, plotZ);
        for (HologramItem hologram : chunkHolograms){
            plugin.deleteHologram(hologram);
        }
        return chunkHolograms.size();
    }


    @EventHandler(priority = EventPriority.NORMAL)
    private void preTownDeleteEvent(PreDeleteTownEvent e){
        townDeleteInProgress = true;
    }


    @EventHandler(priority = EventPriority.NORMAL)
    private void onTownDeleteEvent(DeleteTownEvent e){
        ArrayList<HologramItem> townList = plugin.holograms.get(e.getTownUUID());
        if (townList == null){
            return;
        }
        for (HologramItem holo : townList){
            holo.getHologram().delete();
        }
        database.removeAllFromTown(e.getTownUUID().toString());
        plugin.getLogger().log(Level.INFO, " A town was deleted or fell. " + townList.size() + " holograms were removed.");
        townDeleteInProgress = false;
    }


    @EventHandler(priority = EventPriority.NORMAL)
    private void onTownUnclaimEvent(TownUnclaimEvent e){

            if (!townDeleteInProgress) { //Clear hologram
                int plotX = e.getWorldCoord().getX();
                int plotZ = e.getWorldCoord().getZ();

                int hologramCount = deleteHologramsInPlot(plotX, plotZ, e.getTown().getUuid().toString());

                plugin.getLogger().log(Level.INFO, "Removed " + hologramCount + " holograms from plot X:" + plotX + " Z:" + plotZ + " in town" + e.getTown().getName());
            }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onPlotClearEvent(PlotClearEvent e) {

        if (configValues.getPlotClearSetting()){
            try {
                int plotX = e.getTownBlock().getX();
                int plotZ = e.getTownBlock().getZ();

                int hologramCount = deleteHologramsInPlot(plotX, plotZ, e.getTownBlock().getTown().getUuid().toString());

                plugin.getLogger().log(Level.INFO, "Removed " + hologramCount + " holograms from plot X:" + plotX + " Z:" + plotZ + " in town" + e.getTownBlock().getTown().getName());

            }
            catch (NotRegisteredException exception) {
                plugin.getLogger().severe("An error occurred during a plot clear event.");
                exception.printStackTrace();
            }
        }

    }

}






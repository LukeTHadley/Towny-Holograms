package com.github.lukethadley.townyholograms.storage;

import com.google.gson.Gson;
import org.bukkit.plugin.Plugin;
import java.util.*;

public class ConfigValues {

    private Gson gson;
    private Plugin plugin;

    private Map<Integer, HologramAllowance> hologramSettings;
    private SQLCredentials sqlCredentials;
    private boolean clearHologramsOnPlotClear;

    public ConfigValues(Plugin plugin){
        this.plugin = plugin;
        this.hologramSettings = new HashMap<>();
        gson = new Gson();
        readSQLCredentials();
        readHologramSettings();
        readPlotClearSetting();
    }

    private boolean readSQLCredentials(){
        try {
            String host = plugin.getConfig().getString("sql-database.host");
            int port = plugin.getConfig().getInt("sql-database.port");
            String databaseName = plugin.getConfig().getString("sql-database.database-name");
            String username = plugin.getConfig().getString("sql-database.username");
            String password = plugin.getConfig().getString("sql-database.password");

            if (host == null || databaseName == null || username == null || password == null){
                return false;
            }

            sqlCredentials = new SQLCredentials(host, port, databaseName, username, password);

        }
        catch (NullPointerException e){
            return false;
        }
        return true;
    }

    private boolean readPlotClearSetting() {
        clearHologramsOnPlotClear = plugin.getConfig().getBoolean("ClearHologramsOnPlotClear");
        return true;
    }

    private void readHologramSettings(){
        try {
            int numberOfAllowances = plugin.getConfig().getList("hologram-settings").size();
            for (int counter = 0; counter < numberOfAllowances; counter++){
                Map<String, Object> currentAllowance = (Map<String, Object>) plugin.getConfig().getList("hologram-settings").get(counter);
                String jsonToFormat = currentAllowance.get("allowance").toString();
                HologramAllowance hologramAllowance = gson.fromJson(jsonToFormat, HologramAllowance.class);
                hologramSettings.put(hologramAllowance.getResidents(), hologramAllowance);
            }
            for (int key : hologramSettings.keySet()){
                System.out.println(hologramSettings.get(key).toString());
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public boolean getClearHologramsOnPlotClear() {
        return true;
    }


    /**
     * Returns the SQLCredentials object containing credentials to login to the database
     *
     * @return SQLCredentials object containing connection credentials.
     */
    public SQLCredentials getSqlCredentials(){ return sqlCredentials; }

    /**
     * Returns if holograms should be cleared on /plot clear Towny event.
     *
     * @return boolean : true if holograms should be cleared on /plot clear, false if otherwise.
     */
    public boolean getPlotClearSetting() { return clearHologramsOnPlotClear; }

    /**
     * Returns the Map of all hologram allowance settings read from the config.yml on startup
     *
     * @return the Map<Integer, HologramAllowance> object
     */
    public Map<Integer, HologramAllowance> getHologramSettings() {
        return hologramSettings;
    }

    /**
     * Returns the HologramAllowance object for a town given the number of residents.
     *
     * @param numberOfResidents the number of residents in the town to be checked.
     * @return HologramAllowance object, null if no allowance for that town.
     */
    public HologramAllowance getClosestAllowance(int numberOfResidents){
        HologramAllowance allowance = new HologramAllowance(0, 0, 0, 0, 0);
        for (int key : hologramSettings.keySet()){
            if (key <= numberOfResidents && key > allowance.getResidents()){
                allowance = hologramSettings.get(key);
            }
        }
        return allowance;
    }

}
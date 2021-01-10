package com.github.lukethadley.townyholograms.storage;

import org.apache.commons.lang.StringUtils;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ConfigValues {

    private Plugin plugin;

    public ConfigValues(Plugin plugin){
        this.plugin = plugin;
    }


    public String getTestText(){
        return plugin.getConfig().getString("test-text");
    }

    public String getSQLHost(){
        return plugin.getConfig().getString("sql-database.host");
    }

    public int getSQLPort(){
        return plugin.getConfig().getInt("sql-database.port");
    }

    public String getSQLDatabaseName(){
        return plugin.getConfig().getString("sql-database.database-name");
    }

    public String getSQLUsername(){
        return plugin.getConfig().getString("sql-database.username");
    }

    public String getSQLPassword(){
        return plugin.getConfig().getString("sql-database.password");
    }

    public boolean getDebug() { return plugin.getConfig().getBoolean("debug"); }

    public int getNewHologramPrice() { return plugin.getConfig().getInt("NewHologramPrice"); }

    public boolean getClearHologramsOnPlotClear() {return plugin.getConfig().getBoolean("ClearHologramsOnPlotClear");}

    public List<HologramAllowance> getHologramAllowances(){
        List<HologramAllowance> list = new ArrayList<>();

        try{
            for(Object messages : plugin.getConfig().getList("hologram-allowances")) {
                String line = messages.toString();
                String arr = StringUtils.strip(line, "[]");
                String[] test = arr.split(", ");

                list.add(new HologramAllowance(Integer.parseInt(test[0]), Integer.parseInt(test[1])));

            }
        } catch (IndexOutOfBoundsException e){

        }

        //for(Object messages : plugin.getConfig().getList("hologram-allowances")) {
            //System.out.println(plugin.getConfig().getInt("hologram-allowances.allowance.number-of-residents"));
        //}

        //plugin.getConfig().getSerializable("hologram-allowances", new HologramAllowance());

        //List<HologramAllowance> test;
        //for(Object object : plugin.getConfig().getList("hologram-allowances")) {

        //test = (List<HologramAllowance>) plugin.getConfig().getList("hologram-allowances");

        //System.out.println(test.toString());


        return list;
    }

}

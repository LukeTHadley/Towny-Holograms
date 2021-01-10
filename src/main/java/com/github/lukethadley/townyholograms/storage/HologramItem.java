package com.github.lukethadley.townyholograms.storage;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;

public class HologramItem {

    private String name;
    private String town;
    private ArrayList<String> lines;
    private Location location;
    private Hologram hologram;

    public HologramItem(String name, String town, String content, Location location, Hologram hologram){
        this.name = name;
        this.town = town;
        lines = new ArrayList<>();
        this.location = location;
        this.hologram = hologram;
        linesFromString(content);
    }

    public HologramItem(String name, String town, String content, Location location){
        this.name = name;
        this.town = town;
        lines = new ArrayList<>();
        this.location = location;
        linesFromString(content);
    }

    public void addLine(String text){
        if (hologram != null){
            hologram.appendTextLine(text);
        }
        lines.add(text);

    }

    public void removeLine(int index){

        if (hologram != null){
            hologram.removeLine(index);
        }
        lines.remove(index);



    }

    public void setLine(int index, String text){
        if (hologram != null){
            hologram.removeLine(index);
            hologram.insertTextLine(index, text);
        }
        lines.remove(index);
        lines.add(index, text);

    }

    public void insertLine(int index, String text){
        if (hologram != null){
            hologram.insertTextLine(index, text);
        }
        lines.add(index, text);
    }

    public Hologram getHologram(){
        return hologram;
    }

    public void setHologram(Hologram hologram){
        this.hologram = hologram;
    }

    public String getName() {
        return name;
    }

    public String getTown() {
        return town;
    }

    public UUID getTownUUID(){
        return UUID.fromString(town);
    }

    public String[] getLines() {
        return lines.toArray(new String[lines.size()]);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public String linesToString(){
        String string = "";
        for (int i = 0; i < lines.size(); i++){
            if (i <= lines.size()){
                string = string + lines.get(i) + "\n";
            }
            else {
                string = string + lines.get(i);
            }
        }
        return string;
    }

    private void linesFromString(String string){
        String[] splitString = string.split("\n");
        for (String line : splitString){
            addLine(line);
        }
    }


}

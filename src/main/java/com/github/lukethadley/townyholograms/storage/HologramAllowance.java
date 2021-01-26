package com.github.lukethadley.townyholograms.storage;

public class HologramAllowance {

    private int residents;
    private int max_number;
    private int hologram_cost;
    private int line_limit;
    private int line_cost;

    public HologramAllowance(int residents, int max_number, int hologram_cost, int line_limit, int line_cost){
        this.residents = residents;
        this.max_number = max_number;
        this.hologram_cost = hologram_cost;
        this.line_limit = line_limit;
        this.line_cost = line_cost;
    }

    public int getResidents() {
        return residents;
    }

    public int getMaxNumber() {
        return max_number;
    }

    public int getHologramCost() {
        return hologram_cost;
    }

    public int getLineLimit() {
        return line_limit;
    }

    public int getLineCost() {
        return line_cost;
    }

    @Override
    public String toString() {
        return "HologramAllowance{" +
                "residents=" + residents +
                ", max_number=" + max_number +
                ", hologram_cost=" + hologram_cost +
                ", line_limit=" + line_limit +
                ", line_cost=" + line_cost +
                '}';
    }

}

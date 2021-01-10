package com.github.lukethadley.townyholograms.storage;

public class HologramAllowance {

    private int numberOfResidents, numberOfHolograms;

    public HologramAllowance(int numberOfResidents, int numberOfHolograms){
        this.numberOfResidents = numberOfResidents;
        this.numberOfHolograms = numberOfHolograms;
    }

    public int getNumberOfHolograms() {
        return numberOfHolograms;
    }

    public void setNumberOfHolograms(int numberOfHolograms) {
        this.numberOfHolograms = numberOfHolograms;
    }

    public int getNumberOfResidents() {
        return numberOfResidents;
    }

    public void setNumberOfResidents(int numberOfResidents) {
        this.numberOfResidents = numberOfResidents;
    }


}

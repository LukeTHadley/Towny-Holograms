package com.github.lukethadley.townyholograms.commands;

public enum Permission {

    //General user permissions
    ADD_LINE ("townyholograms.commands.addline"),
    CREATE ("townyholograms.commands.create"),
    DELETE ("townyholograms.commands.delete"),
    HELP ("townyholograms.commands.help"),
    INFO ("townyholograms.commands.info"),
    INSERT_LINE ("townyholograms.commands.insert"),
    LIST ("townyholograms.commands.list"),
    MOVE ("townyholograms.commands.move"),
    PRICES ("townyholograms.commands.prices"),
    REMOVE_LINE ("townyholograms.commands.removeline"),
    SET_LINE ("townyholograms.commands.setline");

    private String node;

    Permission(String node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return node;
    }

}

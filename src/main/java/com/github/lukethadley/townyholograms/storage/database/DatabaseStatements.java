package com.github.lukethadley.townyholograms.storage.database;

public class DatabaseStatements {

    public static final String DATABASE_CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS townyHologramsStorage(id integer NOT NULL AUTO_INCREMENT, name varchar(64) not null, town varchar(36) not null, world varchar(36) not null, content mediumtext not null, x real not null, y real not null, z real not null, PRIMARY KEY (id));";

    public static final String DATABASE_QUEARY_TOTAL_RECORDS = "SELECT COUNT(*) FROM townyHologramsStorage;";

    public static final String DATABASE_INSERT_HOLOGRAM = "INSERT INTO townyHologramsStorage (name, town, world, content, x, y, z) VALUES (?, ?, ?, ?, ?, ?, ?);";

    public static final String DATABASE_QUEARY_EXISTING_HOLOGRAM = "SELECT * FROM `townyHologramsStorage` WHERE town = ? AND name = ? ";

    public static final String DATABASE_QUEARY_ALL_IN_TOWN = "SELECT * FROM `townyHologramsStorage` WHERE town = ?";

    public static final String DATABASE_REMOVE_RECORD = "DELETE FROM townyHologramsStorage WHERE town = ? AND name = ?;";

    public static final String DATABASE_UPDATE_CONTENT = "UPDATE townyHologramsStorage SET content = ? WHERE name = ? AND town = ?;";

    public static final String DATABASE_QUEARY_ALL = "SELECT * FROM `townyHologramsStorage`";

    public static final String DATABASE_QUEARY_ALL_FROM_CHUNK = "SELECT * FROM `townyHologramsStorage` WHERE FLOOR(x/16) = ? AND FLOOR(z/16) = ? ;";

    public static final String DATABASE_REMOVE_ALL_TOWN_RECORDS = "DELETE FROM townyHologramsStorage WHERE town = ?;";





}

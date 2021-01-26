package com.github.lukethadley.townyholograms.storage;

public class SQLCredentials {

    private String host;
    private int port;
    private String databaseName;
    private String username;
    private String password;

    public SQLCredentials(String host, String databaseName, String username, String password){
        this.host = host;
        this.port = 3306;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    public SQLCredentials(String host, int port, String databaseName, String username, String password){
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() { return password; }

    public String getUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
    }

}

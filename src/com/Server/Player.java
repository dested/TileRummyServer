package com.Server;

import com.MessageParseJunk.TileData;

public class Player {
    public String Name;
    public String FullName;
    public boolean IsReady = false;

    TileData[] playerTiles;

    public Player(String fullname, TileData[] playerTiles) {
        this.playerTiles = playerTiles;
        FullName = fullname;
        Name = FullName.split("/")[FullName.split("/").length - 1];
    }
}

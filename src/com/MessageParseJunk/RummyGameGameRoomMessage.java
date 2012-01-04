package com.MessageParseJunk;

import com.Server.Player;

import java.util.ArrayList;

import java.util.ArrayList;

public class RummyGameGameRoomMessage {
    public String PlayerName;
    public int SetIndex;
    public String MoveToPlayerName;
    public int MoveToSetIndex;
    public ArrayList<String> PlayerNames;


    public enum GameRoomMessageType {
        PlayerTiles, AddSetToPlayer, AddTileToSet, SplitSet, MoveTile, AddTileToPlayer, GameStarted, GameFinish, Ping, GiveMeTile, Leave
    }

    public TileData[] TileData;
    public GameRoomMessageType Type;

    public static RummyGameGameRoomMessage Parse(String st) {

        RummyGameGameRoomMessage t = new RummyGameGameRoomMessage();
        String[] d = st.split("\\|");
        int de = Integer.valueOf(d[0]);
        switch (de) {

            case 0:
                t.Type = GameRoomMessageType.PlayerTiles;
                t.TileData = parseRummyTiles(d[1]);

                break;
            case 1:
                t.Type = GameRoomMessageType.AddSetToPlayer;
                t.PlayerName = d[1];
                break;

            case 2:
                t.Type = GameRoomMessageType.AddTileToSet;
                t.TileData = parseRummyTiles(d[1]);
                t.PlayerName = d[2];
                t.SetIndex = Integer.parseInt(d[3]);
                break;
            case 3:
                t.Type = GameRoomMessageType.SplitSet;
                t.TileData = parseRummyTiles(d[1]);
                t.PlayerName = d[2];
                t.SetIndex = Integer.parseInt(d[3]);
                t.MoveToSetIndex = Integer.parseInt(d[4]);
                break;
            case 4:
                t.Type = GameRoomMessageType.MoveTile;
                t.TileData = parseRummyTiles(d[1]);
                t.PlayerName = d[2];
                t.SetIndex = Integer.parseInt(d[3]);
                t.MoveToPlayerName = d[4];
                t.MoveToSetIndex = Integer.parseInt(d[5]);
                break;
            case 5:
                t.Type = GameRoomMessageType.AddTileToPlayer;
                t.TileData = parseRummyTiles(d[1]);
                t.PlayerName = d[2];
                break;
            case 6:
                t.Type = GameRoomMessageType.GiveMeTile;
                t.PlayerName = d[1];
                break;

            case 7:
                t.Type = GameRoomMessageType.GameStarted;
                t.PlayerNames = new ArrayList<String>();
                for (int i = 2; i < d.length; i++) {
                    t.PlayerNames.add(d[i]);
                }
                break;
            case 8:
                t.Type = GameRoomMessageType.GameFinish;
                break;
            case 9:
                t.Type = GameRoomMessageType.Leave;
                break;
            case 10:
                t.Type = GameRoomMessageType.Ping;
                break;
        }

        return t;

    }

    public String GenerateMessage() {
        String d = "";
        switch (Type) {

            case PlayerTiles:
                d = "0|" + makeRummyTiles(TileData);

                break;
            case AddSetToPlayer:
                d = String.format("1|%s", PlayerName);
                break;
            case AddTileToSet:
                d = String.format("2|%s|%s|%d", makeRummyTiles(TileData), PlayerName, SetIndex);
                break;
            case SplitSet:

                d = String.format("3|%s|%s|%d|%d", makeRummyTiles(TileData), PlayerName, SetIndex, MoveToSetIndex);
                break;
            case MoveTile:
                d = String.format("4|%s|%s|%d|%s|%d", makeRummyTiles(TileData), PlayerName, SetIndex, MoveToPlayerName, MoveToSetIndex);
                break;
            case AddTileToPlayer:
                d = String.format("5|%s|%s", makeRummyTiles(TileData), PlayerName);
                break;
            case GiveMeTile:
                d = String.format("6|%s", PlayerName);
                break;
            case GameStarted:
                d = "7|";
                for (String pname : PlayerNames) {
                    d += "|" + pname;
                }
                break;
            case GameFinish:
                d = "8|";
                break;
            case Leave:
                d = "9|";
                break;
            case Ping:
                d = "10|";
                break;

        }

        return d;// + "|" + Argument;
    }

    private static TileData[] parseRummyTiles(String string) {
        ArrayList<TileData> td = new ArrayList<TileData>();
        for (String d : string.split("\\.")) {
            String[] m = d.split("-");
            TileData tc = new TileData(Integer.parseInt(m[0]), Integer.parseInt(m[1]));
            td.add(tc);
        }
        TileData[] t = new TileData[td.size()];

        td.toArray(t);
        return t;
    }

    private static String makeRummyTiles(TileData[] tiles) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < tiles.length; i++) {
            sb.append(tiles[i].Number + "-" + tiles[i].Color + ".");
        }

        return sb.toString();
    }

    public RummyGameGameRoomMessage(GameRoomMessageType t, ArrayList<Player> playersInGame) {
        Type = t;
        ArrayList<String> names = new ArrayList<String>();
        for (Player pl : playersInGame) {
            names.add(pl.Name);
        }
        PlayerNames = names;
    }

    public RummyGameGameRoomMessage(GameRoomMessageType t) {
        Type = t;
    }

    public RummyGameGameRoomMessage(GameRoomMessageType t, TileData[] p) {
        Type = t;
        TileData = p;
    }

    public RummyGameGameRoomMessage(GameRoomMessageType t, TileData[] p, String playerName) {
        Type = t;
        TileData = p;
        PlayerName = playerName;
    }

    public RummyGameGameRoomMessage(GameRoomMessageType t, String playerName) {
        Type = t;
        PlayerName = playerName;
    }

    public RummyGameGameRoomMessage(GameRoomMessageType t, TileData[] p, String playerName, int setIndex) {
        Type = t;
        TileData = p;
        PlayerName = playerName;
        SetIndex = setIndex;
    }

    public RummyGameGameRoomMessage(GameRoomMessageType t, TileData[] p, String playerName, int setIndex, int setIndex2) {
        Type = t;
        TileData = p;

        PlayerName = playerName;
        SetIndex = setIndex;
        MoveToSetIndex = setIndex2;

    }

    public RummyGameGameRoomMessage(GameRoomMessageType t, TileData[] p, String playerName, int setIndex, String playerName2, int setIndex2) {
        Type = t;
        TileData = p;

        PlayerName = playerName;
        SetIndex = setIndex;
        MoveToPlayerName = playerName2;
        MoveToSetIndex = setIndex2;
    }


    private RummyGameGameRoomMessage() {
    }

}


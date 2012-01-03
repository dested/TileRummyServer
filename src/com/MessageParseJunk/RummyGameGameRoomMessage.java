package com.MessageParseJunk;


import java.util.ArrayList;

public class RummyGameGameRoomMessage {
    public enum GameRoomMessageType {
        RummyPlayerTiles, RummyGameMove, GameStarted, GameFinish, Ping, Leave
    }

    public TileData[] TileData;
    public GameRoomMessageType Type;

    public static RummyGameGameRoomMessage Parse(String st) {

        RummyGameGameRoomMessage t = new RummyGameGameRoomMessage();
        String[] d = st.split("\\|");
        int de = Integer.valueOf(d[0]);
        switch (de) {
            case 0:
                t.Type = GameRoomMessageType.RummyPlayerTiles;
                t.TileData = parseRummyTiles(d[1]);
                break;
            case 1:
                t.Type = GameRoomMessageType.RummyGameMove;
                //	t.point = new SudokuPoint(Integer.valueOf(d[1]), new Point(Integer.parseInt(d[2]), Integer.parseInt(d[3])));
                break;
            case 2:
                t.Type = GameRoomMessageType.GameStarted;
                break;
            case 3:
                t.Type = GameRoomMessageType.GameFinish;
                break;
            case 4:
                t.Type = GameRoomMessageType.Leave;
                break;
            case 5:
                t.Type = GameRoomMessageType.Ping;
                break;
        }

        return t;

    }

    public String GenerateMessage() {
        String d = "";
        switch (Type) {
            case RummyPlayerTiles:
                d = "0|" + makeRummyTiles(TileData);
                break;
            case GameStarted:
                d = "2|";
                break;
            case GameFinish:
                d = "3|";
                break;
            case Leave:
                d = "4|";
                break;
            case Ping:
                d = "5|";
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
            sb.append(tiles[i].Number +"-" + tiles[i].Color + ".");
        }

        return sb.toString();
    }

    public RummyGameGameRoomMessage(GameRoomMessageType t) {
        Type = t;
    }

    public RummyGameGameRoomMessage(GameRoomMessageType t, TileData[] p) {
        Type = t;
        TileData = p;
    }

    private RummyGameGameRoomMessage() {
    }

}


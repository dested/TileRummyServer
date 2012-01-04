package com.Server;

import java.io.IOException;

import java.util.*;


import com.MessageParseJunk.RummyGameGameRoomMessage;
import com.MessageParseJunk.TileData;
import com.MessageParseJunk.WaitingRoomMessage;
import com.MessageParseJunk.WaitingRoomMessage.MessageType;


import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class ClientConnector {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        new ClientConnector();
        while (true) {
            String ds = in.nextLine();
            if (ds.toLowerCase().equals("quit"))
                break;

        }

    }

    XMPPConnection xmpp;
    final String uName = "sudoker";

    public ClientConnector() {
        ConnectionConfiguration config = new ConnectionConfiguration("lamplightonline.com", 5222, "gameService.lamplightonline.com");
        config.setSecurityMode(SecurityMode.disabled);
        config.setCompressionEnabled(true);
        xmpp = new XMPPConnection(config);
        try {
            xmpp.connect();
            xmpp.login(uName, "d");
            Timer t = new Timer();
            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    xmpp.sendPacket(new Presence(Presence.Type.available, null, 1, Presence.Mode.available));
                }
            }, 100, 50 * 1000);

        } catch (XMPPException e) {
            e.printStackTrace();
        }

        xmpp.addPacketListener(new PacketListener() {

                    @Override
                    public void processPacket(Packet arg0) {
                        System.out.println(arg0.toXML());
                    }
                }, new PacketFilter() {
            @Override
            public boolean accept(Packet arg0) {
                return true;
            }
        }
        );


        for (int i = 1; i <= 3; i++) {
            try {
                DoWaitingRoom(i);
                DoRummyGameGameRoom(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public class RummyGameGameInformation {
        public boolean gameStarted;
        public ArrayList<Player> PlayersInGame = new ArrayList<Player>();
        public TileData[] resiv;
        int resivIndex = 0;
        public int NumOfBots;

        public RummyGameGameInformation(int i) {
            NumOfBots = i;
        }


        public TileData[] takeFromResiv(int i) {
            int d = 0;
            TileData[] fc = new TileData[i];
            for (int c = 0; c < i; c++) {

                fc[d++] = resiv[resivIndex++];
            }
            return fc;

        }
    }

    public void DoRummyGameGameRoom(final int gameRoomIndex) throws Exception {

        final RummyGameGameInformation game = new RummyGameGameInformation(3);
        System.out.println("RummyGame Game Room" + gameRoomIndex + " Started ");
        game.resiv = makeTilesAndRandom();

        final MultiUserChat muc = new MultiUserChat(xmpp, "rummyGamegameroom" + gameRoomIndex + "@gameservice.lamplightonline.com");

        try {
            // muc.create(uName);
            muc.join(uName);

            for (Iterator<String> it = muc.getOccupants(); it.hasNext(); ) {
                String vf = it.next();
                if (vf.endsWith(uName)) {
                    continue;
                }

                throw new Exception("No Users allowed before me");

            }
        } catch (XMPPException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw e1;
        }
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    muc.sendMessage(new RummyGameGameRoomMessage(RummyGameGameRoomMessage.GameRoomMessageType.Ping).GenerateMessage());
                } catch (XMPPException e) {
                    e.printStackTrace();
                }

            }
        }, 100, 50 * 1000);


        muc.addParticipantListener(new PacketListener() {
            @Override
            public void processPacket(Packet arg0) {
                if (arg0.getFrom().endsWith(uName)) {
                    return;
                }

                Presence pre = (Presence) arg0;
                Player p;

                switch (pre.getType()) {
                    case available:
                        game.PlayersInGame.add(p = new Player(arg0.getFrom(), game.takeFromResiv(14)));
                        System.out.println(p.Name + " Has Joined");
                        try {

                            muc.sendMessage(new RummyGameGameRoomMessage(RummyGameGameRoomMessage.GameRoomMessageType.PlayerTiles, game.PlayersInGame.get(game.PlayersInGame.size() - 1).playerTiles).GenerateMessage());
                            if (!game.gameStarted && game.PlayersInGame.size() + game.NumOfBots > 3
                                    ) {

                                for (int c = 0; c < game.NumOfBots; c++) {
                                    game.PlayersInGame.add(p = new Player(randomName(), game.takeFromResiv(14)));
                                }
                                muc.sendMessage(new RummyGameGameRoomMessage(RummyGameGameRoomMessage.GameRoomMessageType.GameStarted, game.PlayersInGame).GenerateMessage());
                            }

                        } catch (XMPPException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    case unavailable:
                        for (Player pd : game.PlayersInGame) {

                            if (pd.FullName.equals(arg0.getFrom())) {
                                game.PlayersInGame.remove(pd);
                                System.out.println(pd.Name + " Has Left");
                                break;
                            }
                        }
                        if (game.PlayersInGame.size() -game.NumOfBots== 0) {
                            game.PlayersInGame.clear();
                            game.resiv = makeTilesAndRandom();
                            game.resivIndex = 0;
                            System.out.println("  -  Cleared");

                        }


                        break;
                }
            }


        });

        muc.addMessageListener(new PacketListener() {
            @Override
            public void processPacket(Packet message) {
                if (message.getFrom().endsWith(uName)) {
                    return;
                }

                System.out.println(((Message) message).getBody());
                RummyGameGameRoomMessage d = RummyGameGameRoomMessage.Parse(((Message) message).getBody());

                switch (d.Type) {

                    case GameStarted:
                        break;

                    case GameFinish:
                        game.gameStarted = false;


                        // wait and start one up again
                        break;
                    case Leave:
                        break;

                    case PlayerTiles:
                        break;
                    case AddSetToPlayer:
                        break;
                    case AddTileToSet:
                        break;
                    case SplitSet:
                        break;
                    case MoveTile:
                        break;
                    case AddTileToPlayer:
                        break;
                    case Ping:
                        break;
                    case GiveMeTile:

                        try {
                            muc.sendMessage(new RummyGameGameRoomMessage(RummyGameGameRoomMessage.GameRoomMessageType.AddTileToPlayer,
                                    game.takeFromResiv(1), d.PlayerName).GenerateMessage());
                        } catch (XMPPException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                        break;
                }

            }
        });
        System.out.println("RummyGame Game Room " + gameRoomIndex + " Done ");
    }

    Random r = new Random();
    String[] fakeNames = new String[]{"Joe", "Steve", "Chris", "Dave", "Nick", "Mike"};

    private String randomName() {
        return fakeNames[r.nextInt(fakeNames.length)];
    }

    private TileData[] makeTilesAndRandom() {
        ArrayList<TileData> tiles = new ArrayList<TileData>();

        for (int c = 0; c < 2; c++) {
            for (int i = 0; i < 13; i++) {
                for (int j = 0; j < 4; j++) {
                    tiles.add(new TileData(i+1, j));
                }
            }
        }
        TileData[] td = new TileData[tiles.size()];
        int fc = 0;
        while (tiles.size() > 0) {
            int f;
            td[fc++] = tiles.get(f = r.nextInt(tiles.size()));
            tiles.remove(f);
        }
        return td;

    }

    public void DoWaitingRoom(final int waitingRoomIndex) throws Exception {
        final ArrayList<Player> players = new ArrayList<Player>();
        final MultiUserChat muc = new MultiUserChat(xmpp, "waitingroom" + waitingRoomIndex + "@gameservice.lamplightonline.com");
        System.out.println("Waiting Room " + waitingRoomIndex + " Started ");
        try {
            // muc.create(uName);
            muc.join(uName);

            for (Iterator<String> it = muc.getOccupants(); it.hasNext(); ) {
                String vf = it.next();
                if (vf.endsWith(uName)) {
                    continue;
                }
                throw new Exception("No Users allowed before me");

            }
        } catch (XMPPException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw e1;
        }
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    muc.sendMessage(new WaitingRoomMessage(MessageType.Ping).GenerateMessage());
                } catch (XMPPException e) {
                    e.printStackTrace();
                }

            }
        }, 100, 50 * 1000);

        muc.addParticipantListener(new PacketListener() {
            @Override
            public void processPacket(Packet arg0) {
                if (arg0.getFrom().endsWith(uName)) {
                    return;
                }

                Presence pre = (Presence) arg0;
                Player p;
                WaitingRoomMessage vm;
                switch (pre.getType()) {
                    case available:
                        players.add(p = new Player(arg0.getFrom(), null));

                        vm = new WaitingRoomMessage(MessageType.Chat, "*", p.Name + " Has Joined");
                        try {
                            muc.sendMessage(vm.GenerateMessage());
                        } catch (XMPPException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        System.out.println(p.Name + " Has Joined");

                        break;
                    case unavailable:
                        for (Player pd : players) {

                            if (pd.FullName.equals(arg0.getFrom())) {
                                players.remove(pd);
                                vm = new WaitingRoomMessage(MessageType.Chat, "*", pd.Name + " Has Left");
                                try {
                                    muc.sendMessage(vm.GenerateMessage());
                                } catch (XMPPException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                System.out.println(pd.Name + " Has Left");

                                break;
                            }
                        }
                        break;
                }
            }
        });

        muc.addMessageListener(new PacketListener() {
            @Override
            public void processPacket(Packet message) {
                if (message.getFrom().endsWith(uName)) {
                    return;
                }

                System.out.println(((Message) message).getBody());
                for (Player p : players) {
                    if (p.FullName.equals(message.getFrom())) {
                        WaitingRoomMessage d = WaitingRoomMessage.Parse(((Message) message).getBody());
                        switch (d.Type) {
                            case Chat:
                                System.out.println("Chat: " + d.Argument);
                                break;
                            case TurnStatusOff:
                                p.IsReady = false;

                                System.out.println(p.Name + "is not ready");
                                break;
                            case TurnRummyGameStatusOn:
                                p.IsReady = true;
                                try {
                                    muc.sendMessage(new WaitingRoomMessage(MessageType.JoinRummyGameRoom, p.Name, "rummyGamegameroom" + waitingRoomIndex)
                                            .GenerateMessage());
                                } catch (XMPPException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                System.out.println(p.Name + "is ready to play RummyGame");
                                break;
                            case TurnDrawStatusOn:
                                p.IsReady = true;
                                try {
                                    muc.sendMessage(new WaitingRoomMessage(MessageType.JoinDrawRoom, p.Name, "drawgameroom" + waitingRoomIndex).GenerateMessage());
                                } catch (XMPPException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                System.out.println(p.Name + "is ready to play Draw");
                                break;
                            case TurnMazeStatusOn:
                                p.IsReady = true;
                                try {
                                    muc.sendMessage(new WaitingRoomMessage(MessageType.JoinMazeRoom, p.Name, "mazegameroom" + waitingRoomIndex).GenerateMessage());
                                } catch (XMPPException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                System.out.println(p.Name + "is ready to play Maze");
                                break;

                        }
                    }
                }

            }
        });
        System.out.println("Waiting Room " + waitingRoomIndex + " Done");
    }
}

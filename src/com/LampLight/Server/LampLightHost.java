package com.LampLight.Server;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;

import Helper.WSHelper;

public abstract class LampLightHost {

	public String UserName;
	public XMPPConnection xmpp;

	public static LampLightHost SearchForGame(LampPlayer player) {
		return null;
	}

	int gameRoomIndex;

	public LampLightHost(int gameIndex) {
		gameRoomIndex = gameIndex;
	}

	public void SendNetworkMessage(LampPlayer whoTo,   Serializable content) {

	}

	public abstract void RecieveNetworkMessage(LampPlayer whoFrom,  Serializable content);

	MultiUserChat lampMuc;

	public void onConnectionEnded() {
		
	}
	
	public void onConnectionEstablished() {
		lampMuc = new MultiUserChat(xmpp, "squaregame" + gameRoomIndex + "@gameservice.lamplightonline.com");
		try {
			lampMuc.join(UserName);
			for (Iterator<String> it = lampMuc.getOccupants(); it.hasNext();) {
				String vf = it.next();
				if (vf.endsWith(UserName)) {
					continue;
				}

			}
		} catch (XMPPException e1) {
			e1.printStackTrace();
		}
		lampMuc.addParticipantListener(new PacketListener() {
			@Override
			public void processPacket(Packet arg0) {
				Presence pre = (Presence) arg0;
				switch (pre.getType()) {
				case available:
					onUserLogin(new LampPlayer(arg0.getFrom()));
					break;
				case unavailable:
					onUserLogout(new LampPlayer(arg0.getFrom()));
					break;
				}
			}
		});
		
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					lampMuc.sendMessage("---");
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		}, 100, 50 * 1000);

		
		
		lampMuc.addMessageListener(new PacketListener() {
			@Override
			public void processPacket(Packet message) {
				
				Message ms=(Message)message;
				if(ms.getBody().equals("---"))
					return;
				RecieveNetworkMessage(new LampPlayer(message.getFrom()), (Serializable) WSHelper.SToO(ms.getBody()));
			}

		});
	}

	public abstract void onUserLogin(LampPlayer lampPlayer);

	public abstract void onUserLogout(LampPlayer lampPlayer);

}

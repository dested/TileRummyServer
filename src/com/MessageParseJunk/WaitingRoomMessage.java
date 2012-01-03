package com.MessageParseJunk;

public class WaitingRoomMessage {
	public enum MessageType {
		Chat, TurnRummyGameStatusOn, TurnMazeStatusOn, TurnDrawStatusOn, TurnStatusOff, JoinMazeRoom, JoinRummyGameRoom, JoinDrawRoom, Ping
	}

	public String Argument2;
	public String Argument;
	public MessageType Type;

	public static WaitingRoomMessage Parse(String st) {

		WaitingRoomMessage t = new WaitingRoomMessage();

		String[] d = st.split("\\|");
		int de = Integer.valueOf(d[0]);
		switch (de) {
		case 0:
			t.Type = MessageType.Chat;
			t.Argument2 = d[2];

			break;
		case 1:
			t.Type = MessageType.TurnRummyGameStatusOn;
			break;
		case 2:
			t.Type = MessageType.TurnMazeStatusOn;
			break;
		case 3:
			t.Type = MessageType.TurnDrawStatusOn;
			break;
		case 4:
			t.Type = MessageType.TurnStatusOff;
			break;
		case 5:
			t.Type = MessageType.JoinRummyGameRoom;
			t.Argument2 = d[2];
			break;
		case 6:
			t.Type = MessageType.JoinMazeRoom;
			t.Argument2 = d[2];
			break;
		case 7:
			t.Type = MessageType.JoinDrawRoom;
			t.Argument2 = d[2];
			break;		
			case 8:
				t.Type = MessageType.Ping;
				break;
		}

		t.Argument = d[1];
		return t;

	}

	public WaitingRoomMessage(MessageType t, String arg) {
		Type = t;
		Argument = arg;
	}

	public WaitingRoomMessage(MessageType t, String arg, String arg2) {
		Type = t;
		Argument = arg;
		Argument2 = arg2;
	}

	public WaitingRoomMessage(MessageType t) {
		Type = t;
	}

	private WaitingRoomMessage() {
	}

	public String GenerateMessage() {
		String d = "";
		switch (Type) {
		case Chat:
			d = "0";
			break;
		case TurnRummyGameStatusOn:
			d = "1";
			break;
		case TurnMazeStatusOn:
			d = "2";
			break;
		case TurnDrawStatusOn:
			d = "3";
			break;
		case TurnStatusOff:
			d = "4";
			break;
		case JoinRummyGameRoom:
			d = "5";
			break;
		case JoinMazeRoom:
			d = "6";
			break;
		case JoinDrawRoom:
			d = "7";
			break;
		case Ping:
			d = "8";
			break;
		}
		if (Argument2 != null) {

			return d + "|" + Argument + "|" + Argument2;
		}

		return d + "|" + Argument;
	}
}

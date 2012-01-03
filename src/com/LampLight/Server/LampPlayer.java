package com.LampLight.Server;

public class LampPlayer {

	public String Name;
	public String FullName; 

	public LampPlayer(String fn) {
		FullName = fn;
	}

	public LampPlayer() {

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LampPlayer)
			return false;

		return FullName.equals(((LampPlayer) obj).FullName);
	}

	@Override
	public int hashCode() {
		return FullName.hashCode();
	}

	@Override
	public String toString() {
		return FullName;
	}
}

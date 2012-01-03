package drawBig;

import Helper.Helping;

public class Player {
	public String Name;
	public String FullName;
	public DColor Color;
	public int Score;

	public Player(String fn, DColor c) {
		FullName = fn;
		Name = Helping.GetNameFromLongName(fn);
		Color = c;
	}
}

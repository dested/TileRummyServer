package Helper;
 
public class Helping {
	public static String GetNameFromLongName(String d) {
		String[] vf = d.split("/");
		return vf[vf.length - 1];
	}
 

}

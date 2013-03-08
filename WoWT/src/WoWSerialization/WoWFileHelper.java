package WoWSerialization;

public class WoWFileHelper 
{
	public static String WoWDefaultTree = "WoW/WoWTree.txt";
	public static String WoWItemsFolder = "WoW/WoWItems/";
	public static String WoWDefaultSessionInfo = "WoW/default_info.xml";
	public static String WoWTempSessionFile = "WoW/temp_session.xml";
	
	public static String getExtension(String fileName) 
	{
	    String ext = null;
	    int i = fileName.lastIndexOf('.');

	    if (i > 0 &&  i < fileName.length() - 1) {
	        ext = fileName.substring(i+1).toLowerCase();
	    }
	    return ext;
	}
}

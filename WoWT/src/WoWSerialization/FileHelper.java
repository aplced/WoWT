package WoWSerialization;

public class FileHelper 
{
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

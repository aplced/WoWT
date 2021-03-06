package WoWSerialization;
import java.io.File;

import javax.swing.filechooser.FileFilter;


public class CustomFileFilter extends FileFilter 
{
	String extFilter = "";
	String filtName = "";
	
	public CustomFileFilter(String extension, String filterName)
	{
		extFilter = extension;
		filtName = filterName;
	}
	
	private static String getExtension(File f) 
	{
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) 
        {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

	@Override
	public boolean accept(File f) 
	{
		if(f.isDirectory())
		{
			return true;
		}
		
		String extension = getExtension(f);
		if(extension != null && extension.equals(extFilter))
		{
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() 
	{
		return filtName;
	}

}

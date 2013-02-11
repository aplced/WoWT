
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WoWSerialableNode 
{
	private String uniqueID;
	private String displayName;
	private String description = "";
	private Boolean doneState = false;
	private ArrayList<String> childNodes = new ArrayList<String>();
	private ArrayList<String> parentNodes = new ArrayList<String>();
	
	public String getUniqueID()
	{
		return this.uniqueID;
	}
	public void setUniqueID(String uniqueID)
	{
		this.uniqueID = uniqueID;
	}
	
	public String getDisplayName()
	{
		return this.displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public Boolean getDoneState()
	{
		return this.doneState;
	}
	public void setDoneState(Boolean doneState)
	{
		this.doneState = doneState;
	}

    public ArrayList<String> ListOfChildNodes() 
    {
        return childNodes;
    }

    public ArrayList<String> ListOfParentNodes() 
    {
        return parentNodes;
    }
    
    public void SaveWoWItemToFile(String directory)
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(directory + getUniqueID() + ".xml");
            XMLEncoder out = new XMLEncoder(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        }
        catch(IOException i)
        {
            i.printStackTrace();
        }
    }
    
    static public WoWSerialableNode LoadWoWItemFromFile(String directory, String uniqueId)
    {
        WoWSerialableNode wowItem;

        try
        {
           FileInputStream fileIn = new FileInputStream(directory + uniqueId + ".xml");
           XMLDecoder in = new XMLDecoder(fileIn);
           wowItem = (WoWSerialableNode) in.readObject();

           in.close();
           fileIn.close();
       }
       catch(IOException i)
       {
           i.printStackTrace();
           wowItem = null;
       }
        
        return wowItem;
    } 
}

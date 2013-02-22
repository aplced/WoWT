package WoWSerialization;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WoWSerializableNode 
{
	private String uniqueID;
	private String displayName;
	private String description = "";
	private String userNotes = "";
	private Boolean doneState = false;
	private float taskDaysEstimate = 0;
	private ArrayList<String> childNodes = new ArrayList<String>();
	private ArrayList<String> parentNodes = new ArrayList<String>();
	private ArrayList<String> invokeables = new ArrayList<String>();
	
	public static String StartableWorkId = "startable_work";
	public static String WoWItemsFolder = "WoW/WoWItems/";
	
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

	public float getTaskDaysEstimate()
	{
		return taskDaysEstimate;
	}
	public void setTaskDaysEstimate(float iTaskDaysEstimate)
	{
		taskDaysEstimate = iTaskDaysEstimate;
	}
	
    public String getUserNotes()
    {
    	return userNotes;
    }
    public void setUserNotes(String notes)
    {
    	userNotes = notes;
    }
    
    public ArrayList<String> getInvokeables()
    {
    	return invokeables;
    }
    public void setInvokeables(ArrayList<String> lstInvk)
    {
    	invokeables = lstInvk;
    }
	
    public ArrayList<String> ListOfChildNodes() 
    {
        return childNodes;
    }

    public ArrayList<String> ListOfParentNodes() 
    {
        return parentNodes;
    }
    
    public void SaveToFile(String directory)
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
    
    static public WoWSerializableNode LoadFromFile(String directory, String uniqueId)
    {
        WoWSerializableNode wowItem;

        try
        {
           FileInputStream fileIn = new FileInputStream(directory + uniqueId + ".xml");
           XMLDecoder in = new XMLDecoder(fileIn);
           wowItem = (WoWSerializableNode) in.readObject();

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

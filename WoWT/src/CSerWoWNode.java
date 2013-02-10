
import java.util.ArrayList;

public class CSerWoWNode 
{
	private String uniqueID;
	private String displayName;
	private String description;
	private Boolean doneState;
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
}

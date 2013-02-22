package WoWSerialization;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WoWSessionInfoSerializable 
{
	private String devName;
	private String devUserName;
	private String streamName;
	private String components;
	private String buildingBlocks;
	private String funcClusters;
	
	public String getDeveloperName()
	{
		return devName;
	}
	public void setDeveloperName(String name)
	{
		devName = name;
	}
	
	public String getUserName()
	{
		return devUserName;
	}
	public void setUserName(String usrName)
	{
		devUserName = usrName;
	}    	
	
	public String getStreamName()
	{
		return streamName;
	}
	public void setStreamName(String strName)
	{
		streamName = strName;
	}
	
	public String getComponents()
	{
		return components;
	}
	public void setComponents(String comp)
	{
		components = comp;
	} 
	
	public String getBuildingBlocks()
	{
		return buildingBlocks;
	}
	public void setBuildingBlocks(String bblocks)
	{
		buildingBlocks = bblocks;
	}
	
	public String getFuncClusters()
	{
		return funcClusters;
	}
	public void setFuncClusters(String fclusters)
	{
		funcClusters = fclusters;
	} 
	
    public void SaveToFile(String fileName)
    {
    	String completeName = fileName;
    	if(FileHelper.getExtension(completeName) == null)
    		completeName += ".xml";
    	
        try
        {
            FileOutputStream fileOut = new FileOutputStream(completeName);
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
    
    static public WoWSessionInfoSerializable LoadFromFile(String filePath)
    {
    	WoWSessionInfoSerializable wowSessionInfo;

        try
        {
           FileInputStream fileIn = new FileInputStream(filePath);
           XMLDecoder in = new XMLDecoder(fileIn);
           wowSessionInfo = (WoWSessionInfoSerializable) in.readObject();

           in.close();
           fileIn.close();
       }
       catch(IOException i)
       {
           i.printStackTrace();
           wowSessionInfo = null;
       }
        
        return wowSessionInfo;
    } 
}

package WoWSerialization.WoWSerializationObjects.Implementation;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import WoWSerialization.WoWFileHelper;
import WoWSerialization.WoWSerializationObjects.ObjectChangedEventDispatcher;

public class WoWSessionInfoSerializable  extends ObjectChangedEventDispatcher
{
	private String devName = "Ivan Ivanov";
	private String devUserName = "user.name";
	private String streamName = "my_stream";
	private Integer taskId = 0;
	private String taskName = "";
	private String scrumMasters = "";
	private String components = "";
	private String buildingBlocks = "";
	private String funcClusters = "";
	
	public String getDeveloperName()
	{
		return devName;
	}
	public void setDeveloperName(String name)
	{
		devName = name;
		FireObjectChangedEvent();
	}
	
	public String getUserName()
	{
		return devUserName;
	}
	public void setUserName(String usrName)
	{
		devUserName = usrName;
		FireObjectChangedEvent();
	}    	
	
	public String getStreamName()
	{
		return streamName;
	}
	public void setStreamName(String strName)
	{
		streamName = strName;
		FireObjectChangedEvent();
	}
	
	public Integer getTaskId()
	{
		return taskId;
	}
	public void setTaskId(Integer inTaskId)
	{
		taskId = inTaskId;
		FireObjectChangedEvent();
	}
	
	public String getTaskName()
	{
		return taskName;
	}
	public void setTaskName(String iTaskName)
	{
		taskName = iTaskName;
		FireObjectChangedEvent();
	}

	public String getScrumMasters()
	{
		return scrumMasters;
	}
	public void setScrumMasters(String iScrumMasters)
	{
		scrumMasters = iScrumMasters;
		FireObjectChangedEvent();
	}
	
	public String getComponents()
	{
		return components;
	}
	public void setComponents(String comp)
	{
		components = comp;
		FireObjectChangedEvent();
	} 
	
	public String getBuildingBlocks()
	{
		return buildingBlocks;
	}
	public void setBuildingBlocks(String bblocks)
	{
		buildingBlocks = bblocks;
		FireObjectChangedEvent();
	}
	
	public String getFuncClusters()
	{
		return funcClusters;
	}
	public void setFuncClusters(String fclusters)
	{
		funcClusters = fclusters;
		FireObjectChangedEvent();
	} 
	
    public void SaveToFile(String fileName)
    {
    	String completeName = fileName;
    	if(WoWFileHelper.getExtension(completeName) == null)
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
    
    public void CopyFrom(WoWSessionInfoSerializable cloneObj)
    {
    	devName = cloneObj.devName;
    	devUserName = cloneObj.devUserName;
    	streamName = cloneObj.streamName;
    	taskId = cloneObj.taskId;
    	taskName = cloneObj.taskName;
    	scrumMasters = cloneObj.scrumMasters;
    	components = cloneObj.components;
    	buildingBlocks = cloneObj.buildingBlocks;
    	funcClusters = cloneObj.funcClusters;
    	
    	FireObjectChangedEvent();
    }
}

package WoWSerialization;
import java.util.ArrayList;


    public class WoWSessionSerializable
    {
    	private String WoWTree = "";
    	private ArrayList<WoWSerializableNode> Nodes = new ArrayList<WoWSerializableNode>();
    	
    	private String devName;
    	private String devUserName;
    	private String components;
    	private String buildingBlocks;
    	private String funcClusters;
    	
    	public String getWoWTree()
    	{
    		return WoWTree;
    	}
    	public void setWoWTree(String iWoWTree)
    	{
    		WoWTree = iWoWTree;
    	}
    	
    	public ArrayList<WoWSerializableNode> getNodes()
    	{
    		return Nodes;
    	}
    	public void setNodes(ArrayList<WoWSerializableNode> iNodes)
    	{
    		Nodes = iNodes;
    	}
    	
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
    }

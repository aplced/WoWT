package WoWSerialization;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class WoWSessionSerializable
{
	private String WoWTree = "";
	private ArrayList<WoWSerializableNode> Nodes = new ArrayList<WoWSerializableNode>();
	private WoWSessionInfoSerializable sessionInfo = new WoWSessionInfoSerializable();
	
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
	
	public WoWSessionInfoSerializable getSessionInfo()
	{
		return sessionInfo;
	}
	public void setSessionInfo(WoWSessionInfoSerializable iSessionInfo)
	{
		sessionInfo = iSessionInfo;
	}
	
    public void CreateFromWoWTree(String fileName)
    {
    	StringBuilder wowTreeMap = new StringBuilder();
        try
        {
           FileInputStream fileIn = new FileInputStream(fileName);
           BufferedReader br = new BufferedReader(new InputStreamReader(fileIn, Charset.forName("UTF-8")));

           String line;
           while((line = br.readLine()) != null)
           {
        	   wowTreeMap.append(line + "\n");
           }
           
           br.close();
           fileIn.close();
       }
       catch(IOException i)
       {
           i.printStackTrace();
       }
        
        WoWTree = wowTreeMap.toString();
        
        ParseAndCreateTree(WoWTree, Nodes);
    }
    
    private static void ParseWoWTree(String wowTreeMap, ArrayList<WoWSerializableNode> allSerializedWoWItems)
    {
    	String[] allNodeIds = wowTreeMap.split("\n");
    	
    	for(String nodeIds : allNodeIds)
    	{
    		String[] nodeAndParents = nodeIds.split(":");
    		
    		if(nodeAndParents.length > 0 && !nodeAndParents[0].isEmpty())
    		{
	    		String nodeId = nodeAndParents[0].trim();
	    		if(!nodeId.isEmpty())
	    		{
		    		WoWSerializableNode node = LoadWoWItem(nodeId, allSerializedWoWItems);
		    		
		    		if(nodeAndParents.length > 1 && !nodeAndParents[1].isEmpty())
		    		{
			    		ArrayList<WoWSerializableNode> parentNodes = new ArrayList<WoWSerializableNode>();
			    		String[] parentNodeIds = nodeIds.split(":")[1].split(",");
			    		for(String parentId : parentNodeIds)
			    		{
			    			parentNodes.add(LoadWoWItem(parentId.trim(), allSerializedWoWItems));
			    		}
			    		
			    		for(WoWSerializableNode parent : parentNodes)
			    		{
			    			node.ListOfParentNodes().add(parent.getUniqueID());
			    			parent.ListOfChildNodes().add(node.getUniqueID());
			    		}
		    		}
	    		}
    		}
    	}
    }
    
    private static ArrayList<WoWSerializableNode> GetRoodNodes(ArrayList<WoWSerializableNode> allItems)
    {
    	ArrayList<WoWSerializableNode> rootNodes = new ArrayList<WoWSerializableNode>();
    	for(WoWSerializableNode wowItem : allItems)
    	{
    		if(wowItem.ListOfParentNodes().size() == 0)
    			rootNodes.add(wowItem);
    	}
    	return rootNodes;
    }    
    
    private static WoWSerializableNode CreateStartableWorkSer()
    {
	    WoWSerializableNode startableWorkSer = new WoWSerializableNode();
	    startableWorkSer.setUniqueID(WoWSerializableNode.StartableWorkId);
	    startableWorkSer.setDisplayName("Start stream delivery");
	    startableWorkSer.setDoneState(false);
	    startableWorkSer.setDescription("Stream delivery proccess");
	    
	    return startableWorkSer;
    }
    
    private static WoWSerializableNode AddWoWStreamStartSer(ArrayList<WoWSerializableNode> allItems)
    {
    	WoWSerializableNode startableRoot = CreateStartableWorkSer();
    	ArrayList<WoWSerializableNode> rootNodes = GetRoodNodes(allItems);

	    for(WoWSerializableNode roots : rootNodes)
	    {
	    	if(roots.getUniqueID().equals(startableRoot.getUniqueID()))
	    		return null;
	    }
	    
	    for(WoWSerializableNode roots : rootNodes)
	    {
	    	startableRoot.ListOfChildNodes().add(roots.getUniqueID());
	    	roots.ListOfParentNodes().add(startableRoot.getUniqueID());
	    }
	    
	    return startableRoot;
    }    
    
    private static WoWSerializableNode LoadWoWItem(String uniqueId, ArrayList<WoWSerializableNode> allSerializedWoWItems)
    {
    	for(WoWSerializableNode item : allSerializedWoWItems)
    	{
    		if(item.getUniqueID().compareTo(uniqueId) == 0)
    			return item;
    	}

    	WoWSerializableNode item = WoWSerializableNode.LoadFromFile(WoWSerializableNode.WoWItemsFolder, uniqueId);
    	allSerializedWoWItems.add(item);
    	return item;
    }    
    
	private static void ParseAndCreateTree(String wowTreeMap, ArrayList<WoWSerializableNode> allSerializedWoWItems)
	{
        ParseWoWTree(wowTreeMap, allSerializedWoWItems);
        
        WoWSerializableNode rootNeeded = AddWoWStreamStartSer(allSerializedWoWItems);
        if(rootNeeded != null)
            allSerializedWoWItems.add(rootNeeded);
	}    
	
	public static WoWSessionSerializable LoadSessionFromFile(String fileName)
	{   
		WoWSessionSerializable sesSr = null;
		
        try
        {
           FileInputStream fileIn = new FileInputStream(fileName);
           XMLDecoder in = new XMLDecoder(fileIn);
           sesSr = (WoWSessionSerializable) in.readObject();
           in.close();
           fileIn.close();
           
           WoWSessionSerializable.ParseAndCreateTree(sesSr.WoWTree, sesSr.Nodes);
       }
       catch(IOException i)
       {
           i.printStackTrace();
       }
        
        return sesSr;
	}	
	
	public void SaveSessionToFile(String fileName)
    {     	
        try
        {
        	String completeName = fileName;
        	if(FileHelper.getExtension(completeName) == null)
        		completeName += ".xml";
        	
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
}

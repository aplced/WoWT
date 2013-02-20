package WoWPanelUI;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.swing.JPanel;

import WoWSerialization.WoWSerializableNode;
import WoWSerialization.WoWSessionSerializable;


@SuppressWarnings("serial")
public class WoWTreeJPanel extends JPanel 
{
	GridLayout gridL;
	ArrayList<WoWItemJPanel> allItems;
	
	public WoWTreeJPanel()
	{
	    gridL = new GridLayout();
	    setLayout(gridL);
	}
	
	private ArrayList<WoWItemJPanel> ParseAndLinkTree(String wowTreeMap, ArrayList<WoWSerializableNode> allSerializedWoWItems)
	{
        ParseWoWTree(wowTreeMap, allSerializedWoWItems);
        
        ArrayList<WoWItemJPanel> deserializedPanels = new ArrayList<WoWItemJPanel>();
        
        WoWSerializableNode rootNeeded = AddWoWStreamStartSer(allSerializedWoWItems);
        if(rootNeeded != null)
            allSerializedWoWItems.add(rootNeeded);
        
        for(WoWSerializableNode wowSerialized : allSerializedWoWItems)
        {
        	if(wowSerialized.getUniqueID().equals(WoWSerializableNode.StartableWorkId))
        	{
        		deserializedPanels.add(new WoWProcessStartJPanel(wowSerialized));
        	}
        	else
        	{
        		deserializedPanels.add(new WoWItemJPanel(wowSerialized));
        	}
        }
         
        for(WoWItemJPanel wowItem : deserializedPanels)
        {
        	wowItem.LinkNodes(deserializedPanels);
        }
        
        WoWItemJPanel wowItem = GetRootPanel(deserializedPanels);
        wowItem.UpdateEnableWoWItemState();
        //for(WoWItemJPanel wowItem : deserializedPanels)
        //{
        //	wowItem.UpdateEnableWoWItemState(0.0);
        //}
        
        return deserializedPanels;
	}
	
    private ArrayList<WoWItemJPanel> LoadFromFile(String fileName)
    {
        return ParseAndLinkTree(LoadWoWTree(fileName), new ArrayList<WoWSerializableNode>());
    }
  
    private WoWSerializableNode LoadWoWItem(String uniqueId, ArrayList<WoWSerializableNode> allSerializedWoWItems)
    {
    	for(WoWSerializableNode item : allSerializedWoWItems)
    	{
    		if(item.getUniqueID().compareTo(uniqueId) == 0)
    			return item;
    	}
    	
    	WoWSerializableNode item = WoWSerializableNode.LoadWoWItemFromFile(WoWSerializableNode.WoWItemsFolder, uniqueId);
    	allSerializedWoWItems.add(item);
    	return item;
    }
    
    private void ParseWoWTree(String wowTreeMap, ArrayList<WoWSerializableNode> allSerializedWoWItems)
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
    
    private String LoadWoWTree(String fileName)
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
        
        return wowTreeMap.toString();
    }
    
    private void CollectWoWDependecyTree(WoWItemJPanel wowItem, StringBuilder dependency)
    {
    	for(WoWItemJPanel child : wowItem.GetChildren())
    	{
    		CollectWoWDependecyTree(child, dependency);
    	}
    	
    	dependency.append(wowItem.GetUniqueID() + ":");
    	
    	for(WoWItemJPanel parent : wowItem.GetParents())
    	{
    		dependency.append(parent.GetUniqueID() + ",");	
    	}
    	dependency.append("\n");
    }
    
    private ArrayList<WoWSerializableNode> GetRoodNodes(ArrayList<WoWSerializableNode> allItems)
    {
    	ArrayList<WoWSerializableNode> rootNodes = new ArrayList<WoWSerializableNode>();
    	for(WoWSerializableNode wowItem : allItems)
    	{
    		if(wowItem.ListOfParentNodes().size() == 0)
    			rootNodes.add(wowItem);
    	}
    	return rootNodes;
    }
    
    private WoWItemJPanel GetRootPanel(ArrayList<WoWItemJPanel> allItems)
    {
    	for(WoWItemJPanel wowItem : allItems)
    	{
    		if(wowItem.GetParents().size() == 0)
    			return wowItem;
    	}
    	return null;
    }
    
    private void CalcualteWoWItemsTreeDepth(WoWItemJPanel wowItem, int treeDepth)
    {       
    	for(WoWItemJPanel child : wowItem.GetChildren())
    	{
    		if(child.TreeDepth < treeDepth)
    		{
    			child.TreeDepth = treeDepth;
    		}
    		
    		CalcualteWoWItemsTreeDepth(child, treeDepth + 1);
    	}
    }
    
    private JPanel GetOrCreateTierPanel(int treeDepth, ArrayList<JPanel> tierPanels)
    {
    	if(tierPanels.size() <= treeDepth)
    	{
    		JPanel tierP = new JPanel();
        	tierP.setLayout(new FlowLayout());
        	tierPanels.add(tierP);
    	}
    	
    	return tierPanels.get(treeDepth);
    }
    
    private void CreateWoWTiers(WoWItemJPanel wowItem, int treeDepth, ArrayList<JPanel> tierPanels)
    {
        JPanel tierP = GetOrCreateTierPanel(treeDepth, tierPanels);
        
    	for(WoWItemJPanel child : wowItem.GetChildren())
    	{
    		if(child.TreeDepth == treeDepth)
    		{
    			tierP.add(child);
    			CreateWoWTiers(child, treeDepth + 1, tierPanels);
    		}
    	}
    }

    private WoWSerializableNode CreateStartableWorkSer()
    {
	    WoWSerializableNode startableWorkSer = new WoWSerializableNode();
	    startableWorkSer.setUniqueID(WoWSerializableNode.StartableWorkId);
	    startableWorkSer.setDisplayName("Start stream delivery");
	    startableWorkSer.setDoneState(false);
	    startableWorkSer.setDescription("Stream delivery proccess");
	    
	    return startableWorkSer;
    }
    
    private WoWSerializableNode AddWoWStreamStartSer(ArrayList<WoWSerializableNode> allItems)
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
    
    private void SetUpTreeViewPanel(ArrayList<WoWItemJPanel> deserializedPanels)
    {
    	removeAll();
    	allItems = deserializedPanels;
    	
	    ArrayList<JPanel> tierPanels = new ArrayList<JPanel>();
	    
	    WoWItemJPanel startableWork = GetRootPanel(deserializedPanels);
	    GetOrCreateTierPanel(0, tierPanels).add(startableWork);
	    
	    CalcualteWoWItemsTreeDepth(startableWork, 1);
	    CreateWoWTiers(startableWork, 1, tierPanels);
	    
	    gridL.setRows(tierPanels.size());
	    for(JPanel tierP : tierPanels)
	    {
	    	add(tierP);
	    }
    }
    
	public void LoadPanelFromFile(String fileName)
	{    
	    SetUpTreeViewPanel(LoadFromFile(fileName));
	}
	
	public void LoadSessionFromFile(String fileName)
	{   
		WoWSessionSerializable sesSr;
		removeAll();
		
        try
        {
           FileInputStream fileIn = new FileInputStream(fileName);
           XMLDecoder in = new XMLDecoder(fileIn);
           sesSr = (WoWSessionSerializable) in.readObject();
           in.close();
           fileIn.close();
           
           SetUpTreeViewPanel(ParseAndLinkTree(sesSr.getWoWTree(), sesSr.getNodes()));
       }
       catch(IOException i)
       {
           i.printStackTrace();
       }
	}	

	private static String getExtension(String fileName) 
	{
	    String ext = null;
	    int i = fileName.lastIndexOf('.');

	    if (i > 0 &&  i < fileName.length() - 1) {
	        ext = fileName.substring(i+1).toLowerCase();
	    }
	    return ext;
	}
	
	public void SaveSessionToFile(String fileName)
    {
        StringBuilder dependecies = new StringBuilder();
    	CollectWoWDependecyTree(GetRootPanel(allItems), dependecies);
    	
    	ArrayList<WoWSerializableNode> nodes = new ArrayList<WoWSerializableNode>();
    	for(WoWItemJPanel item : allItems)
    	{
    		nodes.add(item.CreateSerializable());
    	}
    	
    	WoWSessionSerializable sesSr = new WoWSessionSerializable();
    	sesSr.setWoWTree(dependecies.toString());
    	sesSr.setNodes(nodes);
    	
        try
        {
        	String completeName = fileName;
        	if(getExtension(completeName) == null)
        		completeName += ".xml";
        	
            FileOutputStream fileOut = new FileOutputStream(completeName);
            XMLEncoder out = new XMLEncoder(fileOut);
            out.writeObject(sesSr);
            out.close();
            fileOut.close();
         }
         catch(IOException i)
         {
             i.printStackTrace();
         }
    }
}

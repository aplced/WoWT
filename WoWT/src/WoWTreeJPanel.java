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
	
	private ArrayList<WoWItemJPanel> ParseAndLinkTree(String wowTreeMap, ArrayList<WoWSerialableNode> allSerializedWoWItems)
	{
        ParseWoWTree(wowTreeMap, allSerializedWoWItems);
        
        ArrayList<WoWItemJPanel> deserializedPanels = new ArrayList<WoWItemJPanel>();
        
        for(WoWSerialableNode wowSerialized : allSerializedWoWItems)
        {
        	deserializedPanels.add(new WoWItemJPanel(wowSerialized));
        }
        
        for(WoWItemJPanel wowItem : deserializedPanels)
        {
        	wowItem.LinkNodes(deserializedPanels);
        }
        
        for(WoWItemJPanel wowItem : deserializedPanels)
        {
        	wowItem.UpdateEnableWoWItemState();
        }
        
        return deserializedPanels;
	}
	
    private ArrayList<WoWItemJPanel> LoadFromFile(String fileName)
    {
        return ParseAndLinkTree(LoadWoWTree(fileName), new ArrayList<WoWSerialableNode>());
    }
  
    private WoWSerialableNode LoadWoWItem(String uniqueId, ArrayList<WoWSerialableNode> allSerializedWoWItems)
    {
    	for(WoWSerialableNode item : allSerializedWoWItems)
    	{
    		if(item.getUniqueID().compareTo(uniqueId) == 0)
    			return item;
    	}
    	
    	WoWSerialableNode item = WoWSerialableNode.LoadWoWItemFromFile("WoW/WoWItems/", uniqueId);
    	allSerializedWoWItems.add(item);
    	return item;
    }
    
    private void ParseWoWTree(String wowTreeMap, ArrayList<WoWSerialableNode> allSerializedWoWItems)
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
		    		WoWSerialableNode node = LoadWoWItem(nodeId, allSerializedWoWItems);
		    		
		    		if(nodeAndParents.length > 1 && !nodeAndParents[1].isEmpty())
		    		{
			    		ArrayList<WoWSerialableNode> parentNodes = new ArrayList<WoWSerialableNode>();
			    		String[] parentNodeIds = nodeIds.split(":")[1].split(",");
			    		for(String parentId : parentNodeIds)
			    		{
			    			parentNodes.add(LoadWoWItem(parentId.trim(), allSerializedWoWItems));
			    		}
			    		
			    		for(WoWSerialableNode parent : parentNodes)
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
    
    private ArrayList<WoWItemJPanel> GetRoodNodes(ArrayList<WoWItemJPanel> allItems)
    {
    	ArrayList<WoWItemJPanel> rootNodes = new ArrayList<WoWItemJPanel>();
    	for(WoWItemJPanel wowItem : allItems)
    	{
    		if(wowItem.GetParents().size() == 0)
    			rootNodes.add(wowItem);
    	}
    	return rootNodes;
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
    
    private void CreateWoWTiers(WoWItemJPanel wowItem, int treeDepth, ArrayList<JPanel> tierPanels)
    {
    	
    	if(tierPanels.size() <= treeDepth)
    	{
    		JPanel tierP = new JPanel();
        	tierP.setLayout(new FlowLayout());
        	tierPanels.add(tierP);
    	}
        
    	for(WoWItemJPanel child : wowItem.GetChildren())
    	{
    		if(child.TreeDepth == treeDepth)
    		{
    			tierPanels.get(treeDepth).add(child);
    			CreateWoWTiers(child, treeDepth + 1, tierPanels);
    		}
    	}
    }

    private WoWItemJPanel AddWoWStartDummyNode(ArrayList<WoWItemJPanel> allItems)
    {
    	ArrayList<WoWItemJPanel> rootNodes = GetRoodNodes(allItems);
    	
	    WoWSerialableNode startableWorkSer = new WoWSerialableNode();
	    startableWorkSer.setUniqueID("startable_work");
	    startableWorkSer.setDisplayName("Stream delivery");
	    startableWorkSer.setDoneState(true);
	    startableWorkSer.setDescription("");
	    for(WoWItemJPanel roots : rootNodes)
	    {
	    	startableWorkSer.ListOfChildNodes().add(roots.GetUniqueID());
	    }
	    WoWItemJPanel startableWork = new WoWItemJPanel(startableWorkSer);
	    startableWork.LinkNodes(allItems);
	    startableWork.UpdateEnableWoWItemState();
	    
	    return startableWork;
    }
    
    private void SetUpTreeViewPanel(ArrayList<WoWItemJPanel> deserializedPanels)
    {
    	removeAll();
    	allItems = deserializedPanels;
    	
        WoWItemJPanel startableWork = AddWoWStartDummyNode(allItems);
	    
	    ArrayList<JPanel> tierPanels = new ArrayList<JPanel>();
	    
	    CalcualteWoWItemsTreeDepth(startableWork, 0);
	    CreateWoWTiers(startableWork, 0, tierPanels);
	    
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

	public void SaveSessionToFile(String fileName)
    {
        StringBuilder dependecies = new StringBuilder();
    	CollectWoWDependecyTree(allItems.get(0), dependecies);
    	
    	ArrayList<WoWSerialableNode> nodes = new ArrayList<WoWSerialableNode>();
    	for(WoWItemJPanel item : allItems)
    	{
    		nodes.add(item.CreateSerializable());
    	}
    	
    	WoWSessionSerializable sesSr = new WoWSessionSerializable();
    	sesSr.setWoWTree(dependecies.toString());
    	sesSr.setNodes(nodes);
    	
        try
        {
            FileOutputStream fileOut = new FileOutputStream(fileName + ".xml");
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

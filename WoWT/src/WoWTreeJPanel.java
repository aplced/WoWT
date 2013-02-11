import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class WoWTreeJPanel extends JPanel 
{
	GridLayout gridL;
	
	public WoWTreeJPanel()
	{
	    gridL = new GridLayout();
	    setLayout(gridL);
	}
	
    private ArrayList<WoWItemJPanel> LoadFromFile(String fileName)
    {
        ArrayList<WoWSerialableNode> allSerializedWoWItems = new ArrayList<WoWSerialableNode>();

        String wowTreeMap = LoadWoWTree(fileName);
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
    	String wowTreeMap;
        try
        {
           FileInputStream fileIn = new FileInputStream(fileName);
           XMLDecoder in = new XMLDecoder(fileIn);
           wowTreeMap = (String) in.readObject();

           in.close();
           fileIn.close();
       }
       catch(IOException i)
       {
           i.printStackTrace();
           wowTreeMap = "";
       }
        
        return wowTreeMap;
    }
    
    @SuppressWarnings("unused")
	private void SaveToFile(ArrayList<WoWItemJPanel> wowItems)
    {
    	for(WoWItemJPanel item : wowItems)
    	{
    		item.CreateSerializable().SaveWoWItemToFile("WoW/WoWItems/");
    	}
    	
    	SaveWoWTreeToFile(wowItems);
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
    
    private void SaveWoWTreeToFile(ArrayList<WoWItemJPanel> wowItems)
    {
    	StringBuilder dependecies = new StringBuilder();
    	
    	CollectWoWDependecyTree(wowItems.get(0), dependecies);
    	
        try
        {
            FileOutputStream fileOut = new FileOutputStream("WoW/WoWTree.xml");
            XMLEncoder out = new XMLEncoder(fileOut);
            out.writeObject(dependecies.toString());
            out.close();
            fileOut.close();
        }
        catch(IOException i)
        {
            i.printStackTrace();
        }
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
    
    private void CreateBFSWoWTiers(WoWItemJPanel wowItem, int treeDepth, ArrayList<JPanel> tierPanels)
    {
    	
    	if(tierPanels.size() <= treeDepth)
    	{
    		JPanel tierP = new JPanel();
        	tierP.setLayout(new FlowLayout());
        	tierPanels.add(tierP);
    	}
        
    	for(WoWItemJPanel child : wowItem.GetChildren())
    	{
    		tierPanels.get(treeDepth).add(child);
    		CreateBFSWoWTiers(child, treeDepth + 1, tierPanels);
    	}
    }

	public void LoadPanelFromFile(String fileName)
	{    
		removeAll();
	    ArrayList<WoWItemJPanel> allItems = LoadFromFile(fileName);
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
	    
	    ArrayList<JPanel> tierPanels = new ArrayList<JPanel>();
	    
	    CreateBFSWoWTiers(startableWork, 0, tierPanels);
	    
	    gridL.setRows(tierPanels.size());
	    for(JPanel tierP : tierPanels)
	    {
	    	add(tierP);
	    }
	}
}

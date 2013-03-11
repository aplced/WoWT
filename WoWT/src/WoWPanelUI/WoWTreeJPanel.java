package WoWPanelUI;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import WoWPanelUI.WoWItemJPanels.WoWItemJPanel;
import WoWPanelUI.WoWItemJPanels.WoWProcessStartJPanel;
import WoWSerialization.WoWFileHelper;
import WoWSerialization.WoWSerializationObjects.IWoWDataChangedAction;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSerializableNode;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionSerializable;


@SuppressWarnings("serial")
public class WoWTreeJPanel extends JPanel implements IWoWDataChangedAction
{
	GridLayout gridL;
	ArrayList<WoWItemJPanel> allItems;
	WoWSessionSerializable curSesSr;
	
	public WoWTreeJPanel()
	{
	    gridL = new GridLayout();
	    setLayout(gridL);
	}
	
	private ArrayList<WoWItemJPanel> CreateJPanelTree(String wowTreeMap, ArrayList<WoWSerializableNode> allSerializedWoWItems)
	{    
        ArrayList<WoWItemJPanel> deserializedPanels = new ArrayList<WoWItemJPanel>();
        
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
        
        return deserializedPanels;
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
	
    public void LoadSession(WoWSessionSerializable sesSr)
    {
		if(sesSr != null)
		{
			if(curSesSr != null)
			{
				curSesSr.CopyFrom(sesSr);
			}
			else
			{
				curSesSr = sesSr;
				curSesSr.addObjectChangedEventListener(this);
			}
			removeAll();
			SetUpTreeViewPanel(CreateJPanelTree(sesSr.getWoWTree(), sesSr.getNodes()));
		}
    }
    
	public void LoadSessionFromFile(String fileName)
	{   
		WoWSessionSerializable sesSr = WoWSessionSerializable.LoadSessionFromFile(fileName);
		LoadSession(sesSr);
	}	
	
	public void SaveSessionToFile(String fileName)
    {
		if(allItems != null)
		{
	        StringBuilder dependecies = new StringBuilder();
	    	CollectWoWDependecyTree(GetRootPanel(allItems), dependecies);
	    	
	    	ArrayList<WoWSerializableNode> nodes = new ArrayList<WoWSerializableNode>();
	    	for(WoWItemJPanel item : allItems)
	    	{
	    		nodes.add(item.CreateSerializable());
	    	}
	    	
	    	curSesSr.setWoWTree(dependecies.toString());
	    	curSesSr.setNodes(nodes);
	    	curSesSr.SaveSessionToFile(fileName);
		}
    }

	@Override
	public void DataChanged()
	{
		SaveSessionToFile(WoWFileHelper.WoWTempSessionFile);
	}
}

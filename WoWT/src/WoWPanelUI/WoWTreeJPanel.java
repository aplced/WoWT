package WoWPanelUI;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import WoWPanelUI.WoWItemJPanels.WoWItemJPanel;
import WoWPanelUI.WoWItemJPanels.WoWProcessStartJPanel;
import WoWSerialization.WoWFileHelper;
import WoWSerialization.WoWSerializationObjects.IWoWDataChangedAction;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSerializableNode;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionSerializable;


@SuppressWarnings("serial")
public class WoWTreeJPanel extends JPanel implements IWoWDataChangedAction, IRefreshInfo
{
	GridLayout gridL;
	ArrayList<WoWItemJPanel> allItems;
	WoWSessionSerializable curSesSr;
	WoWItemJPanel startableWork;
	WoWTreeTimerUpdate timerRefresh;
	
	public WoWTreeJPanel()
	{
	    gridL = new GridLayout();
	    setLayout(gridL);
	    
	    timerRefresh = new WoWTreeTimerUpdate();
	    timerRefresh.Start(5*60*1000); //Update once every five minutes
	    timerRefresh.AddRefreshInfoObject(this);
	}
	
	protected void finalize()
	{
		timerRefresh.RemoveRfreshInfoObject(this);
		timerRefresh.Stop();
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
	    
	    startableWork = GetRootPanel(deserializedPanels);
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
	
	String CreateNodeInfo(WoWSerializableNode node)
	{
		if(!node.getUserNotes().isEmpty())
		{
			StringBuilder nodeInfo = new StringBuilder();
			StringBuilder dashes = new StringBuilder();
			for(int i = 0; i < node.getDisplayName().length(); i++)
			{
				dashes.append("-");
			}
			dashes.append("\n");
		
			nodeInfo.append(dashes.toString());
			nodeInfo.append(node.getDisplayName() + ":\n");
			nodeInfo.append(dashes.toString());
		
			//if(!node.getDescription().isEmpty())
			//	nodeInfo.append(node.getDescription() + "\n");
		
		
			nodeInfo.append(node.getUserNotes() + "\n");
		
			nodeInfo.append("\n");
		
			return nodeInfo.toString();
		}
		else
		{
			return "";
		}
	}
	
	void GetTieredNodeInfo(StringBuilder allInfos, WoWItemJPanel pnl, int desiredTier, ArrayList<WoWItemJPanel> visitedPanels)
	{
		if(pnl.TreeDepth == desiredTier) // If this is *our* tier, then collect the info
		{
			if(!visitedPanels.contains(pnl))
			{
				visitedPanels.add(pnl);
				allInfos.append(CreateNodeInfo(pnl.CreateSerializable()));
			}
		}
		else if(pnl.TreeDepth < desiredTier) // If this tier is lower than what is wanted, check it's children
		{
			for(WoWItemJPanel child : pnl.GetChildren())
			{
				GetTieredNodeInfo(allInfos, child, desiredTier, visitedPanels);
			}  
		}
	}
	
    private int TraverseTreeDepth(WoWItemJPanel wowItem, int treeDepth)
    {
    	if(wowItem.TreeDepth > treeDepth)
    		treeDepth = wowItem.TreeDepth;
    	
    	for(WoWItemJPanel child : wowItem.GetChildren())
    	{
    		int res = TraverseTreeDepth(child, treeDepth);
    		if(res > treeDepth)
    			treeDepth = res;
    	}
    	
    	return treeDepth;
    }
    
    private int GetTreeDepth(WoWItemJPanel root)
    {
    	int depth = -1;
    	
    	depth = TraverseTreeDepth(root, depth);
    	
    	return depth;
    }
	
	String GetAllNodesInfo(WoWItemJPanel root)
	{
		ArrayList<WoWItemJPanel> visitedPanels = new ArrayList<WoWItemJPanel>();
		StringBuilder allNodesInfo = new StringBuilder();
		
		int depth = GetTreeDepth(root);
		
		for(int i = 0; i < depth; i++)
		{
			GetTieredNodeInfo(allNodesInfo, root, i, visitedPanels);
		}
		
		return allNodesInfo.toString();
	}

	public void SaveNotesToFile(String fileName)
	{
		if(allItems != null)
		{
	    	 String notes = GetAllNodesInfo(GetRootPanel(allItems));
	    	
	    	 try
	         {
	         	String completeName = fileName;
	         	if(WoWFileHelper.getExtension(completeName) == null)
	         		completeName += ".txt";
	         	
	             BufferedWriter fileOut = new BufferedWriter(new FileWriter(completeName));
	             fileOut.write(notes);
	             fileOut.close();
	          }
	          catch(IOException i)
	          {
	              i.printStackTrace();
	          }	    	
		}
	}
	
	@Override
	public void DataChanged()
	{
		SaveSessionToFile(WoWFileHelper.WoWTempSessionFile);
	}
	
	@Override
	public void Refresh() 
	{
		if(allItems != null)
		{
			WoWItemJPanel wowItem = GetRootPanel(allItems);
        	wowItem.UpdateEnableWoWItemState();
		}
	}
	
	public WoWItemJPanel GetRootPanel()
	{
		return startableWork;
	}
	
	public WoWSessionSerializable GetLoadedSession()
	{
		return curSesSr;
	}
}

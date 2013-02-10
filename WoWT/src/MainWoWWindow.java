import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;


@SuppressWarnings("serial")
public class MainWoWWindow extends JFrame 
{
    public static void main(String[] args)
    {
    	MainWoWWindow win = new MainWoWWindow();
        win.setVisible(true);
    }
    
    private ArrayList<CWoWJPanel> LoadFromFile()
    {
        ArrayList<CSerWoWNode> allSerializedWoWItems = new ArrayList<CSerWoWNode>();

        String wowTreeMap = LoadWoWTree();
        ParseWoWTree(wowTreeMap, allSerializedWoWItems);
        
        ArrayList<CWoWJPanel> deserializedPanels = new ArrayList<CWoWJPanel>();
        
        for(CSerWoWNode wowSerialized : allSerializedWoWItems)
        {
        	deserializedPanels.add(new CWoWJPanel(wowSerialized));
        }
        
        for(CWoWJPanel wowItem : deserializedPanels)
        {
        	wowItem.LinkNodes(deserializedPanels);
        }
        
        for(CWoWJPanel wowItem : deserializedPanels)
        {
        	wowItem.UpdateEnableWoWItemState();
        }
        
        return deserializedPanels;
    }
    
    private CSerWoWNode LoadWoWItemFromFile(String uniqueId)
    {
        CSerWoWNode wowItem;

        try
        {
           FileInputStream fileIn = new FileInputStream("WoW/WoWItems/" + uniqueId + ".xml");
           XMLDecoder in = new XMLDecoder(fileIn);
           wowItem = (CSerWoWNode) in.readObject();

           in.close();
           fileIn.close();
       }
       catch(IOException i)
       {
           i.printStackTrace();
           wowItem = null;
       }
        
        return wowItem;
    }    

    private CSerWoWNode LoadWoWItem(String uniqueId, ArrayList<CSerWoWNode> allSerializedWoWItems)
    {
    	for(CSerWoWNode item : allSerializedWoWItems)
    	{
    		if(item.getUniqueID().compareTo(uniqueId) == 0)
    			return item;
    	}
    	
    	CSerWoWNode item = LoadWoWItemFromFile(uniqueId);
    	allSerializedWoWItems.add(item);
    	return item;
    }
    
    private void ParseWoWTree(String wowTreeMap, ArrayList<CSerWoWNode> allSerializedWoWItems)
    {
    	String[] allNodeIds = wowTreeMap.split("\n");
    	
    	for(String nodeIds : allNodeIds)
    	{
    		String[] nodeAndParents = nodeIds.split(":");
    		
    		if(nodeAndParents.length > 0 && !nodeAndParents[0].isEmpty())
    		{
	    		String nodeId = nodeAndParents[0].trim();
	    		CSerWoWNode node = LoadWoWItem(nodeId, allSerializedWoWItems);
	    		
	    		if(nodeAndParents.length > 1 && !nodeAndParents[1].isEmpty())
	    		{
		    		ArrayList<CSerWoWNode> parentNodes = new ArrayList<CSerWoWNode>();
		    		String[] parentNodeIds = nodeIds.split(":")[1].split(",");
		    		for(String parentId : parentNodeIds)
		    		{
		    			parentNodes.add(LoadWoWItem(parentId.trim(), allSerializedWoWItems));
		    		}
		    		
		    		for(CSerWoWNode parent : parentNodes)
		    		{
		    			node.ListOfParentNodes().add(parent.getUniqueID());
		    			parent.ListOfChildNodes().add(node.getUniqueID());
		    		}
	    		}
    		}
    	}
    }    
    
    private String LoadWoWTree()
    {
    	String wowTreeMap;
        try
        {
           FileInputStream fileIn = new FileInputStream("WoW/WoWTree.xml");
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
    
    private void SaveToFile(ArrayList<CWoWJPanel> wowItems)
    {
    	for(CWoWJPanel item : wowItems)
    	{
    		SaveWoWItemToFile(item.CreateSerializable());
    	}
    	
    	SaveWoWTreeToFile(wowItems);
    }
    
    private void CollectWoWDependecyTree(CWoWJPanel wowItem, StringBuilder dependency)
    {
    	for(CWoWJPanel child : wowItem.GetChildren())
    	{
    		CollectWoWDependecyTree(child, dependency);
    	}
    	
    	dependency.append(wowItem.GetUniqueID() + ":");
    	
    	for(CWoWJPanel parent : wowItem.GetParents())
    	{
    		dependency.append(parent.GetUniqueID() + ",");	
    	}
    	dependency.append("\n");
    }
    
    private void SaveWoWTreeToFile(ArrayList<CWoWJPanel> wowItems)
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
    
    private void SaveWoWItemToFile(CSerWoWNode serializableWoWItem)
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream("WoW/WoWItems/" + serializableWoWItem.getUniqueID() + ".xml");
            XMLEncoder out = new XMLEncoder(fileOut);
            out.writeObject(serializableWoWItem);
            out.close();
            fileOut.close();
        }
        catch(IOException i)
        {
            i.printStackTrace();
        }
    }
    
    private CWoWJPanel GetRoodNode(ArrayList<CWoWJPanel> allItems)
    {
    	for(CWoWJPanel wowItem : allItems)
    	{
    		if(wowItem.GetParents().size() == 0)
    			return wowItem;
    	}
    	return null;
    }
    
    private void CreateBFSWoWTiers(CWoWJPanel wowItem, int treeDepth, ArrayList<JPanel> tierPanels)
    {
    	
    	if(tierPanels.size() <= treeDepth)
    	{
    		JPanel tierP = new JPanel();
        	tierP.setLayout(new FlowLayout());
        	tierPanels.add(tierP);
    	}
        
    	for(CWoWJPanel child : wowItem.GetChildren())
    	{
    		tierPanels.get(treeDepth).add(child);
    		CreateBFSWoWTiers(child, treeDepth + 1, tierPanels);
    	}
    }
    
    public MainWoWWindow()
    {
    	super();
    	
        try
        { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        }
        catch (Exception e)
        {
        } 
        
        JPanel mainPanel = new JPanel();
        GridLayout gridL = new GridLayout();
        mainPanel.setLayout(gridL);
        
        ArrayList<CWoWJPanel> allItems = LoadFromFile();
        CWoWJPanel rootNode = GetRoodNode(allItems);
        
        ArrayList<JPanel> tierPanels = new ArrayList<JPanel>();
        JPanel rootTierP = new JPanel();
        rootTierP.setLayout(new FlowLayout());
    	tierPanels.add(rootTierP);
    	rootTierP.add(rootNode);
        
        CreateBFSWoWTiers(rootNode, 1, tierPanels);
        
        gridL.setRows(tierPanels.size());
        for(JPanel tierP : tierPanels)
        {
        	mainPanel.add(tierP);
        }
        setContentPane(mainPanel);
        pack();
        
        //SaveToFile(allItems);        
    }
    
}

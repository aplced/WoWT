import java.util.ArrayList;


    public class WoWSessionSerializable
    {
    	private String WoWTree = "";
    	private ArrayList<WoWSerialableNode> Nodes = new ArrayList<WoWSerialableNode>();
    	
    	public String getWoWTree()
    	{
    		return WoWTree;
    	}
    	public void setWoWTree(String iWoWTree)
    	{
    		WoWTree = iWoWTree;
    	}
    	
    	public ArrayList<WoWSerialableNode> getNodes()
    	{
    		return Nodes;
    	}
    	public void setNodes(ArrayList<WoWSerialableNode> iNodes)
    	{
    		Nodes = iNodes;
    	}
    }

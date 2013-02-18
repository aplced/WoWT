import java.util.ArrayList;


    public class WoWSessionSerializable
    {
    	private String WoWTree = "";
    	private ArrayList<WoWSerializableNode> Nodes = new ArrayList<WoWSerializableNode>();
    	
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
    }

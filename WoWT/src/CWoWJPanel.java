import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CWoWJPanel extends JPanel implements ActionListener
{
	private String uniqueID;
	private JLabel displayName = new JLabel("WoW item");
	private JLabel description = new JLabel("WoW description");
	private JCheckBox doneState = new JCheckBox("Done");
	
	private ArrayList<CWoWJPanel> parentWoWNodes = new ArrayList<CWoWJPanel>();
	private ArrayList<CWoWJPanel> childWoWNodes = new ArrayList<CWoWJPanel>();
	
	private ArrayList<String> parentWoWNodesIDs = new ArrayList<String>();
	private ArrayList<String> childWoWNodesIDs = new ArrayList<String>();
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		for(CWoWJPanel child : childWoWNodes)
		{
			child.UpdateEnableWoWItemState();
		}
	}
	
	public CWoWJPanel(CSerWoWNode serNode)
	{
		super();
		
		doneState.addActionListener(this);
		
		InitFromSerializable(serNode);
		
		setLayout(new BorderLayout());
		
		add(displayName, BorderLayout.NORTH);
		add(description, BorderLayout.CENTER);
		add(doneState, BorderLayout.EAST);
	}
	
	private void InitFromSerializable(CSerWoWNode serNode)
	{
		uniqueID = serNode.getUniqueID();
		displayName.setText(serNode.getDisplayName());
		description.setText(serNode.getDescription());
		doneState.setSelected(serNode.getDoneState());
		
		parentWoWNodesIDs = serNode.ListOfParentNodes();
		childWoWNodesIDs = serNode.ListOfChildNodes();
	}
	
	public CSerWoWNode CreateSerializable()
	{
		CSerWoWNode serializable = new CSerWoWNode();
		serializable.setUniqueID(uniqueID);
		serializable.setDisplayName(displayName.getText());
		serializable.setDescription(description.getText());
		serializable.setDoneState(doneState.isSelected());
		
		for(CWoWJPanel parent : parentWoWNodes)
		{
			serializable.ListOfParentNodes().add(parent.GetUniqueID());
		}

		for(CWoWJPanel child : childWoWNodes)
		{
			serializable.ListOfChildNodes().add(child.GetUniqueID());
		}
		
		return serializable;
	}
	
	private void SetControlsEnabled(boolean enabled)
	{
		displayName.setEnabled(enabled);
		description.setEnabled(enabled);
		if(!enabled)
			doneState.setSelected(false);
		doneState .setEnabled(enabled);
	}
	
	public void UpdateEnableWoWItemState()
	{
		boolean allParentsDone = true;
		StringBuilder dependencies = new StringBuilder();
		dependencies.append("Depends on following items:\n");
		for(CWoWJPanel parent : parentWoWNodes)
		{
			if(!parent.IsWoWItemDone())
			{
				allParentsDone = false;
				dependencies.append(parent.WoWItemName() + "\n");
			}
		}
		
		SetControlsEnabled(allParentsDone);
		
		for(CWoWJPanel child : childWoWNodes)
		{
			child.UpdateEnableWoWItemState();
		}
	}
	
	public void LinkNodes(ArrayList<CWoWJPanel> allNodesList)
	{
		for(CWoWJPanel node : allNodesList)
		{
			for(String parentID : parentWoWNodesIDs)
			{
				if(parentID.compareTo(node.GetUniqueID()) == 0)
				{
					parentWoWNodes.add(node);
					break;
				}
			}
			
			for(String childID : childWoWNodesIDs)
			{
				if(childID.compareTo(node.GetUniqueID()) == 0)
				{
					childWoWNodes.add(node);
					break;
				}
			}
		}
	}
	
	public String GetUniqueID()
	{
		return uniqueID;
	}
	
	public Boolean IsWoWItemDone()
	{
		return doneState.isSelected();
	}
	
	public String WoWItemName()
	{
		return displayName.getText();
	}
	
	public ArrayList<CWoWJPanel> GetChildren()
	{
		return childWoWNodes;
	}
	
	public ArrayList<CWoWJPanel> GetParents()
	{
		return parentWoWNodes;
	}
}

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class WoWItemJPanel extends JPanel implements ActionListener
{
	
	protected String uniqueID;
	protected JLabel displayName = new JLabel("WoW item");
	//protected JLabel description = new JLabel("WoW description");
	protected String description;
	protected JButton popDescription = new JButton("?");
	protected JCheckBox doneState = new JCheckBox("Done");
	protected float taskDaysEstimate;
	
	protected ArrayList<WoWItemJPanel> parentWoWNodes = new ArrayList<WoWItemJPanel>();
	protected ArrayList<WoWItemJPanel> childWoWNodes = new ArrayList<WoWItemJPanel>();
	
	protected ArrayList<String> parentWoWNodesIDs = new ArrayList<String>();
	protected ArrayList<String> childWoWNodesIDs = new ArrayList<String>();
	
	public int TreeDepth = 0;
	
	protected void notifyChildrenOfStateChange(int daysSinceStart)
	{
		for(WoWItemJPanel child : childWoWNodes)
		{
			child.UpdateEnableWoWItemState(daysSinceStart);
		}
	}
	
	protected void performChangeStateAction()
	{
		notifyChildrenOfStateChange(0);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == popDescription)
		{
			JOptionPane.showMessageDialog(this, description);
		}
		else if(e.getSource() == doneState)
		{
			performChangeStateAction();
		}
	}
	
	public WoWItemJPanel(WoWSerializableNode serNode)
	{
		super();
		
		popDescription.addActionListener(this);
		doneState.addActionListener(this);
		
		InitFromSerializable(serNode);
		
		setLayout(new BorderLayout());
		
		add(displayName, BorderLayout.NORTH);
		add(popDescription, BorderLayout.CENTER);
		add(doneState, BorderLayout.EAST);
	}
	
	private void InitFromSerializable(WoWSerializableNode serNode)
	{
		uniqueID = serNode.getUniqueID();
		displayName.setText(serNode.getDisplayName());
		//description.setText(serNode.getDescription());
		description = serNode.getDescription();
		doneState.setSelected(serNode.getDoneState());
		taskDaysEstimate = serNode.getTaskDaysEstimate();
		
		parentWoWNodesIDs = serNode.ListOfParentNodes();
		childWoWNodesIDs = serNode.ListOfChildNodes();
	}
	
	public WoWSerializableNode CreateSerializable()
	{
		WoWSerializableNode serializable = new WoWSerializableNode();
		serializable.setUniqueID(uniqueID);
		serializable.setDisplayName(displayName.getText());
		//serializable.setDescription(description.getText());
		serializable.setDescription(description);
		serializable.setDoneState(doneState.isSelected());
		serializable.setTaskDaysEstimate(taskDaysEstimate);
		
		for(WoWItemJPanel parent : parentWoWNodes)
		{
			serializable.ListOfParentNodes().add(parent.GetUniqueID());
		}

		for(WoWItemJPanel child : childWoWNodes)
		{
			serializable.ListOfChildNodes().add(child.GetUniqueID());
		}
		
		return serializable;
	}
	
	protected void SetControlsEnabled(boolean enabled)
	{
		displayName.setEnabled(enabled);
		popDescription.setEnabled(enabled);
		if(!enabled)
			doneState.setSelected(false);
		doneState.setEnabled(enabled);
	}
	
	public void UpdateEnableWoWItemState(int daysSinceStart)
	{
		boolean allParentsDone = true;
		StringBuilder dependencies = new StringBuilder();
		dependencies.append("Depends on following items:\n");
		for(WoWItemJPanel parent : parentWoWNodes)
		{
			if(!parent.IsWoWItemDone())
			{
				allParentsDone = false;
				dependencies.append(parent.WoWItemName() + "\n");
			}
		}
		
		SetControlsEnabled(allParentsDone);
		
		for(WoWItemJPanel child : childWoWNodes)
		{
			child.UpdateEnableWoWItemState(daysSinceStart);
		}
	}
	
	public void LinkNodes(ArrayList<WoWItemJPanel> allNodesList)
	{
		for(WoWItemJPanel node : allNodesList)
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
	
	public ArrayList<WoWItemJPanel> GetChildren()
	{
		return childWoWNodes;
	}
	
	public ArrayList<WoWItemJPanel> GetParents()
	{
		return parentWoWNodes;
	}
}

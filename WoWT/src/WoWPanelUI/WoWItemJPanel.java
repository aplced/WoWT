package WoWPanelUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import WoWItemDialogs.DisplayWoWItemFrame;
import WoWItemDialogs.IWoWItemEditDoneAction;
import WoWSerialization.WoWSerializableNode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class WoWItemJPanel extends JPanel implements ActionListener, IWoWItemEditDoneAction
{
	WoWSerializableNode serNode;
	
	protected String uniqueID;
	protected JLabel displayName = new JLabel("WoW item");
	//protected JLabel description = new JLabel("WoW description");
	protected String description;
	protected String userNotes;
	protected JButton popDescription = new JButton("?");
	protected JCheckBox doneState = new JCheckBox("Done");
	protected float taskDaysEstimate;
	
	protected ArrayList<WoWItemJPanel> parentWoWNodes = new ArrayList<WoWItemJPanel>();
	protected ArrayList<WoWItemJPanel> childWoWNodes = new ArrayList<WoWItemJPanel>();
	
	protected ArrayList<String> parentWoWNodesIDs = new ArrayList<String>();
	protected ArrayList<String> childWoWNodesIDs = new ArrayList<String>();
	
	protected float elapsedDaysSinceStart = 0;
	protected float estimatedStartDays = 0;
	
	protected Color defColor;
	
	public int TreeDepth = 0;
	
	protected void notifyChildrenOfStateChange(float daysSinceStart)
	{
		for(WoWItemJPanel child : childWoWNodes)
		{
			child.UpdateEnableWoWItemState(estimatedStartDays + taskDaysEstimate, daysSinceStart);
		}
	}
	
	protected void performChangeStateAction()
	{
		notifyChildrenOfStateChange(elapsedDaysSinceStart);
		UpdateEnableWoWItemState();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == popDescription)
		{
			DisplayWoWItemFrame dispInfo = new DisplayWoWItemFrame(CreateSerializable());
			dispInfo.addWoWItemEditListener(this);
			dispInfo.setVisible(true);
		}
		else if(e.getSource() == doneState)
		{
			performChangeStateAction();
		}
	}
	
	public WoWItemJPanel(WoWSerializableNode iSerNode)
	{
		super();
		
		popDescription.addActionListener(this);
		doneState.addActionListener(this);
		
		serNode = iSerNode;
		InitFromSerializable();
		
		setLayout(new BorderLayout());
		
		add(displayName, BorderLayout.NORTH);
		add(popDescription, BorderLayout.CENTER);
		add(doneState, BorderLayout.EAST);
		
		defColor = getBackground();
	}
	
	private void InitFromSerializable()
	{
		uniqueID = serNode.getUniqueID();
		displayName.setText(serNode.getDisplayName());
		description = serNode.getDescription();
		userNotes = serNode.getUserNotes();
		doneState.setSelected(serNode.getDoneState());
		taskDaysEstimate = serNode.getTaskDaysEstimate();
		
		parentWoWNodesIDs = serNode.ListOfParentNodes();
		childWoWNodesIDs = serNode.ListOfChildNodes();
		
		UpdateInfoDisplayBtn();
	}
	
	public WoWSerializableNode CreateSerializable()
	{
		WoWSerializableNode serializable = new WoWSerializableNode();
		serializable.setUniqueID(uniqueID);
		serializable.setDisplayName(displayName.getText());
		serializable.setDescription(description);
		serializable.setUserNotes(userNotes);
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
		//popDescription.setEnabled(enabled);
		if(!enabled)
			doneState.setSelected(false);
		doneState.setEnabled(enabled);
		serNode.setDoneState(enabled);
	}
	
	protected void SetElapsedTimeWarning()
	{		
		if(doneState.isSelected())
		{
			setBackground(Color.GREEN);
		}
		else if(estimatedStartDays + taskDaysEstimate < elapsedDaysSinceStart)
		{
			setBackground(Color.RED);
		}
		else if(estimatedStartDays < elapsedDaysSinceStart)
		{
			setBackground(Color.YELLOW);
		}
		else
		{
			setBackground(defColor);
		}
	}
	
	public void UpdateEnableWoWItemState()
	{
		UpdateEnableWoWItemState(estimatedStartDays, elapsedDaysSinceStart);
	}	
	
	private void UpdateEnableWoWItemState(float startDayAcc, float daysSinceStart)
	{
		estimatedStartDays = startDayAcc;
		elapsedDaysSinceStart = daysSinceStart;
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
		SetElapsedTimeWarning();
		
		for(WoWItemJPanel child : childWoWNodes)
		{
			child.UpdateEnableWoWItemState(startDayAcc + taskDaysEstimate, daysSinceStart);
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
	
	private void UpdateInfoDisplayBtn()
	{
		if(userNotes.isEmpty())
		{
			popDescription.setBackground(defColor);
			popDescription.setText("...");
		}
		else
		{
			popDescription.setBackground(Color.RED);
			popDescription.setText("..!");
		}
	}

	@Override
	public void EditDone(WoWSerializableNode iSerNode) 
	{
		serNode.CopyFrom(iSerNode);
		serNode.FireObjectChangedEvent();
		InitFromSerializable();
	}
}

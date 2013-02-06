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
		if(doneState.isSelected())
		{
			setEnabled(true);
		}
		else
		{
			setEnabled(false);
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
		
		parentWoWNodesIDs = serNode.getParentNodes();
		childWoWNodesIDs = serNode.getChildNodes();
	}
	
	private CSerWoWNode CreateSerializable()
	{
		CSerWoWNode serializable = new CSerWoWNode();
		serializable.setUniqueID(uniqueID);
		serializable.setDisplayName(displayName.getText());
		serializable.setDescription(description.getText());
		serializable.setDoneState(doneState.isSelected());
		
		ArrayList<String> parentWoWs = new ArrayList<String>();
		for(CWoWJPanel parent : parentWoWNodes)
		{
			parentWoWs.add(parent.GetUniqueID());
		}
		serializable.setParentNodes(parentWoWs);

		ArrayList<String> childWoWs = new ArrayList<String>();
		for(CWoWJPanel child : childWoWNodes)
		{
			childWoWs.add(child.GetUniqueID());
		}
		serializable.setChildNodes(childWoWs);
		
		return serializable;
	}
	
	public void LinkNodes(ArrayList<CWoWJPanel> allNodesList)
	{
		for(CWoWJPanel node : allNodesList)
		{
			for(String parentID : parentWoWNodesIDs)
			{
				if(node.GetUniqueID() == parentID)
				{
					parentWoWNodes.add(node);
					break;
				}
			}
			
			for(String parentID : childWoWNodesIDs)
			{
				if(node.GetUniqueID() == parentID)
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
}

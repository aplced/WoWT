package WoWItemDialogs.WoWValueInput.Implementation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import WoWItemDialogs.DisplayWoWItemFrame;
import WoWItemDialogs.WoWEditors.EditWoWItem.IWoWItemEditDoneAction;
import WoWItemDialogs.WoWValueInput.IWoWValueInput;
import WoWPanelUI.WoWItemJPanels.WoWItemJPanel;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSerializableNode;

@SuppressWarnings("serial")
public class WoWEditableCheckboxInputValue extends JPanel implements IWoWValueInput, IWoWItemEditDoneAction, ActionListener
{
	WoWItemJPanel nodePanel;
	
	JTextField wowItemName;
	JCheckBox partOfBreakdown;
	JButton editWoWItem;
	
	private void PopulateFromWoWSer(WoWSerializableNode wowSer)
	{
		wowItemName.setText(wowSer.getDisplayName() + " - " + wowSer.getTaskDaysEstimate() + " days");
		partOfBreakdown.setSelected(wowSer.getPartOfBreakdown());
		
		wowItemName.setEnabled(partOfBreakdown.isSelected());
	}
	
	public static GridBagConstraints GetGridConstraints()
	{
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 4;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4,4,4,4);
 
        return c;
	}
	
	private void CreateCheckBox()
	{
		partOfBreakdown = new JCheckBox("Claim task");
		partOfBreakdown.addActionListener(this);
		
		GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);
        c.weightx = 0;
        c.weighty = 0;
        
        add(partOfBreakdown, c);
	}
	
	private void CreateTextField()
	{
		wowItemName = new JTextField();
        
		GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4,4,4,4);
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;
        
        add(wowItemName, c);
	}
	
	private void CreateButton()
	{
		editWoWItem = new JButton("Edit");
		editWoWItem.addActionListener(this);
        
		GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(4,4,4,4);
        c.weightx = 0;
        c.weighty = 0;
        
        add(editWoWItem, c);
	}
	
	public WoWEditableCheckboxInputValue(WoWItemJPanel iNodePanel)
	{
		nodePanel = iNodePanel;
		
		setLayout(new GridBagLayout());
		
		CreateCheckBox();
        
		CreateTextField();
        
		CreateButton();
		
		PopulateFromWoWSer(nodePanel.CreateSerializable());
	}

	@Override
	public String GetInputValue() 
	{
		if(partOfBreakdown.isSelected())
		{
			return wowItemName.getText();
		}
		else
		{
			return null;
		}
	}

	@Override
	public void SetInputValue(String value) 
	{
		wowItemName.setText(value);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == editWoWItem)
		{
			DisplayWoWItemFrame dispInfo = new DisplayWoWItemFrame(nodePanel.CreateSerializable());
			dispInfo.addWoWItemEditListener(nodePanel);
			dispInfo.addWoWItemEditListener(this);
			dispInfo.setVisible(true);
		}
		else if(e.getSource() == partOfBreakdown)
		{
			wowItemName.setEnabled(partOfBreakdown.isSelected());
		}
	}

	@Override
	public void EditDone(WoWSerializableNode serNode)
	{
		PopulateFromWoWSer(serNode);
	}
}

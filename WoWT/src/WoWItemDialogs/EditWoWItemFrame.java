package WoWItemDialogs;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import WoWSerialization.WoWSerializableNode;


@SuppressWarnings("serial")
public class EditWoWItemFrame extends WoWEditorFrame implements ActionListener
{
	JButton apply;
	JButton cancel;
	
	WoWValueInput uniqueId;
	WoWValueInput displayName;
	WoWValueInput taskEstimatedDuration;
	WoWValueInput description;
	WoWValueInput userNotes;
	WoWValueInput invkLst;
	
	private JPanel CreateInputPanelsGrid()
	{
		int row = 0;
        JPanel inputPnl = new JPanel();
        inputPnl.setLayout(new GridBagLayout());
        
        uniqueId = WoWValueInput.WoWTextFieldInput("The unique identifier of this WoW item - used in WoW tree definition", serNode.getUniqueID());
        displayName = WoWValueInput.WoWTextFieldInput("Display name of the WoW item - visible in the main view", serNode.getDisplayName());
        taskEstimatedDuration = WoWValueInput.WoWSpinnerInput("Task duration in days", serNode.getTaskDaysEstimate().toString());
        description = WoWValueInput.WoWTextAreaInput("Description visible in the main view", serNode.getDescription());
        userNotes = WoWValueInput.WoWTextAreaInput("User notes for this process step", serNode.getUserNotes());
        
        StringBuilder invokeList = new StringBuilder();
        for(String invokeable : serNode.getInvokeables())
        {
        	invokeList.append(invokeable + "\n");
        }
        invkLst = WoWValueInput.WoWTextAreaInput("Input invoke list separated by new lines", invokeList.toString());
        
        uniqueId.AddInputToPanel(inputPnl, 0, row++);
        displayName.AddInputToPanel(inputPnl, 0, row++);
        taskEstimatedDuration.AddInputToPanel(inputPnl, 0, row++);
        description.AddInputToPanel(inputPnl, 0, row++);
        userNotes.AddInputToPanel(inputPnl, 0, row++);
        invkLst.AddInputToPanel(inputPnl, 0, row++);
		
		return inputPnl;
	}
	
	private JPanel CreateControlBtnPanel()
	{
        JPanel controlPnl = new JPanel();
        controlPnl.setLayout(new FlowLayout());
        
		apply = new JButton("Overwrite template");
		apply.addActionListener(this);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
	    
		controlPnl.add(apply);
		controlPnl.add(cancel);
		
		return controlPnl;
	}
	
	private JPanel CreateMainNewWoWItemPanel()
	{
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
		mainPanel.add(CreateControlBtnPanel(), BorderLayout.PAGE_END);
		
		JPanel inputPnl = CreateInputPanelsGrid();
    	JScrollPane scrollPane = new JScrollPane(inputPnl);
    	inputPnl.setAutoscrolls(true);
    	scrollPane.setPreferredSize(new Dimension( inputPnl.getPreferredSize().width + 125, inputPnl.getPreferredSize().height + 125));        
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		
		return mainPanel;
	}
	
	public EditWoWItemFrame(WoWSerializableNode iSerNode)
	{
		serNode = iSerNode;
	    setTitle("Edit WoW item");
	    setLocationRelativeTo(null);
	    setContentPane(CreateMainNewWoWItemPanel());
	    pack();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == apply)
		{
			String uId = uniqueId.GetInputValue();
			String dispName = displayName.GetInputValue();
			
			if(!uId.isEmpty())
			{
				uId = uId.trim();
			}
			if(uId.isEmpty())
			{
				JOptionPane.showMessageDialog(this, "Unique id cannot be an empty string!");
				return;
			}
			
			if(uId.contains(" ") ||  uId.contains(",") || uId.contains("\\") || uId.contains("?") || 
					uId.contains("/") || uId.contains("+") || uId.contains("*") || uId.contains("%") || 
					uId.contains("!") || uId.contains("@") || uId.contains("#") || uId.contains("$") ||
					uId.contains("^") || uId.contains("&") || uId.contains("(") || uId.contains(")"))
			{
				JOptionPane.showMessageDialog(this, "Unique id cannot contain space or any of the following symbols:\n , \\ ? / + * % ! @ # $ ^ & ( )");
				return;
			}
			
			if(!dispName.isEmpty())
			{
				dispName = dispName.trim();
			}
			if(dispName.isEmpty())
			{
				JOptionPane.showMessageDialog(this, "Display name cannot be an empty string!");
				return;
			}
			
			String invkListItems = invkLst.GetInputValue();
			ArrayList<String> invkLst = new ArrayList<String>();
			for(String invkItm : invkListItems.split("\n"))
			{
				if(!invkItm.isEmpty())
					invkLst.add(invkItm);
			}
			
			serNode.setUniqueID(uId);
			serNode.setTaskDaysEstimate(Float.parseFloat(taskEstimatedDuration.GetInputValue()));
			serNode.setDisplayName(dispName);
			serNode.setDescription(description.GetInputValue());
			serNode.setUserNotes(userNotes.GetInputValue());
			serNode.setInvokeables(invkLst);
			
			NotifyWoWItemEditDone();
			ClearAndClose();
		}
		else if (e.getSource() == cancel) 
		{
			ClearAndClose();
		}		
	}
	
	private void ClearAndClose()
	{
		ClearListeners();
		setVisible(false);
		dispose();
	}
}

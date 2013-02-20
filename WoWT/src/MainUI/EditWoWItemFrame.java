package MainUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import WoWSerialization.WoWSerializableNode;


@SuppressWarnings("serial")
public class EditWoWItemFrame extends WoWEditorFrame implements ActionListener
{
	JButton apply;
	JButton cancel;
	
	JLabel uniqueIdInfo;
	JLabel displayNameInfo;
	JLabel descriptionInfo;
	JLabel userNotesInfo;
	JLabel invkLstInfo;
	JLabel taskEstimatedDurationInfo;
	
	JTextField uniqueIdInput;
	JTextField displayNameInput;
	JTextArea descriptionInput;
	JTextArea userNotesInput;
	JTextArea invkLstInput;
	JSpinner taskEstimatedDurationInput;
	
	private void AddUniqueIDInput(JPanel dispPnl, int col, int row)
	{
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);
        
		uniqueIdInfo = new JLabel("The unique identifier of this WoW item - used in WoW tree definition");
		dispPnl.add(uniqueIdInfo, c);
		
		c.gridx = c.gridx + 1;
		uniqueIdInput = new JTextField(20);
		uniqueIdInput.setText(serNode.getUniqueID());
		dispPnl.add(uniqueIdInput, c);
	}
	
	private void AddDisplayNameInput(JPanel dispPnl, int col, int row)
	{
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);
		
		displayNameInfo = new JLabel("Display name of the WoW item - visible in the main view");
		dispPnl.add(displayNameInfo, c);
		
		c.gridx = c.gridx + 1;
		displayNameInput = new JTextField(20);
		displayNameInput.setText(serNode.getDisplayName());
		dispPnl.add(displayNameInput, c);
	}
	
	private void AddDescriptionInput(JPanel dispPnl, int col, int row)
	{
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4,4,4,4);
		
		descriptionInfo = new JLabel("Description visible in the main view");
		dispPnl.add(descriptionInfo, c);
		
		c.gridx = c.gridx + 1;
		descriptionInput = new JTextArea(5,60);
		descriptionInput.setLineWrap(true);
		descriptionInput.setText(serNode.getDescription());
		dispPnl.add(descriptionInput, c);
	}
	
	private void AddTaskDurationInput(JPanel dispPnl, int col, int row)
	{
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);
        
		taskEstimatedDurationInfo = new JLabel("Task duration in days");
		dispPnl.add(taskEstimatedDurationInfo, c);
		
		c.gridx = c.gridx + 1;
		taskEstimatedDurationInput = new JSpinner();
		taskEstimatedDurationInput.setModel(new SpinnerNumberModel(0, 0, 10, 0.5));
		taskEstimatedDurationInput.setValue(serNode.getTaskDaysEstimate());
		dispPnl.add(taskEstimatedDurationInput, c);
	}
	
	private void AddUserNotesInput(JPanel dispPnl, int col, int row)
	{
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4,4,4,4);
		
		userNotesInfo = new JLabel("User notes for this process step");
		dispPnl.add(userNotesInfo, c);
		
		c.gridx = c.gridx + 1;
		userNotesInput = new JTextArea(5,60);
		userNotesInput.setLineWrap(true);
		userNotesInput.setText(serNode.getUserNotes());
		dispPnl.add(userNotesInput, c);
	}
	
	private void AddInvokeListInput(JPanel dispPnl, int col, int row)
	{
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4,4,4,4);
		
        invkLstInfo = new JLabel("Input invoke list separated by new lines");
        dispPnl.add(invkLstInfo, c);
        
        c.gridx = c.gridx + 1;
        invkLstInput = new JTextArea(5,60);
        invkLstInput.setLineWrap(true);
        
        for(String invokeable : serNode.getInvokeables())
        {
        	invkLstInput.setText(invokeable + "\n");
        }
        
        dispPnl.add(invkLstInput, c);
	}
	
	private JPanel CreateInputPanelsGrid()
	{
		int row = 0;
        JPanel inputPnl = new JPanel();
        inputPnl.setLayout(new GridBagLayout());
		
        AddUniqueIDInput(inputPnl, 0, row++);
        AddDisplayNameInput(inputPnl, 0, row++);
        AddTaskDurationInput(inputPnl, 0, row++);
        AddDescriptionInput(inputPnl, 0, row++);
        AddUserNotesInput(inputPnl, 0, row++);
        AddInvokeListInput(inputPnl, 0, row++);
		
		return inputPnl;
	}
	
	private JPanel CreateControlBtnPanel()
	{
        JPanel controlPnl = new JPanel();
        controlPnl.setLayout(new FlowLayout());
        
		apply = new JButton("Apply");
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
	
	EditWoWItemFrame(WoWSerializableNode iSerNode)
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
			String uId = uniqueIdInput.getText();
			String dispName = displayNameInput.getText();
			
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
			
			String invkListItems = invkLstInput.getText();
			ArrayList<String> invkLst = new ArrayList<String>();
			for(String invkItm : invkListItems.split("\n"))
			{
				if(!invkItm.isEmpty())
					invkLst.add(invkItm);
			}
			
			Number taskDays = (Number)taskEstimatedDurationInput.getValue();
			
			serNode.setUniqueID(uId);
			serNode.setTaskDaysEstimate(taskDays.floatValue());
			serNode.setDisplayName(dispName);
			serNode.setDescription(descriptionInput.getText());
			serNode.setUserNotes(userNotesInput.getText());
			serNode.setInvokeables(invkLst);
			
			NotifyEditDone();
			ClearAndClose();
		}
		else if (e.getSource() == cancel) 
		{
			ClearAndClose();
		}		
	}
	
	private void ClearAndClose()
	{
		listeners.clear();
		setVisible(false);
		dispose();
	}
}

package MainUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import WoWSerialization.WoWSerializableNode;

@SuppressWarnings("serial")
public class DisplayWoWItemFrame extends WoWEditorFrame implements ActionListener, WoWEditDoneAction
{
	JButton ok;
	JButton edit;
	
	JTextArea userNotesInput;
	JPanel mainPanel = new JPanel();
		
	private void AddUniqueID(JPanel dispPnl, int col, int row)
	{       
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        JLabel uniqueIdInfo = new JLabel(serNode.getUniqueID());
        
        dispPnl.add(uniqueIdInfo, c);
	}
	
	private void AddTaskDuration(JPanel dispPnl, int col, int row)
	{
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);  
        
		JLabel taskEstimatedDurationInfo = new JLabel("Task duration " + serNode.getTaskDaysEstimate() + " days");
		dispPnl.add(taskEstimatedDurationInfo, c);
	}
	
	private void AddDisplayName(JPanel dispPnl, int col, int row)
	{
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);
        
        JLabel displayNameInfo = new JLabel(serNode.getDisplayName());
		dispPnl.add(displayNameInfo, c);
		
		c.gridx = c.gridx + 1;
		JLabel uniqueIdInfo = new JLabel("(" + serNode.getUniqueID() + ")");
		dispPnl.add(uniqueIdInfo, c);
	}
	
	private void AddDescription(JPanel dispPnl, int col, int row)
	{
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4,4,4,4);
        
        JTextArea descriptionInput = new JTextArea(5,60);
        descriptionInput.setEditable(false);
		descriptionInput.setLineWrap(true);
				
		descriptionInput.setText(serNode.getDescription());
		
		dispPnl.add(descriptionInput, c);
	}
	
	private void AddUserNotes(JPanel dispPnl, int col, int row)
	{
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4,4,4,4);
		
		userNotesInput = new JTextArea(5,60);
		userNotesInput.setLineWrap(true);
		
		userNotesInput.setText(serNode.getUserNotes());
		
		dispPnl.add(userNotesInput, c);
	}
	
	private void AddInvokeablesButtons(JPanel dispPnl, int col, int row)
	{
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4,4,4,4);
		
        for(String invokeable : serNode.getInvokeables())
        {
        	
        }
	}	
	
	private JPanel CreateWoWDisplayPanel()
	{
		int row = 0;
        JPanel dispPnl = new JPanel();
        dispPnl.setLayout(new GridBagLayout());
		
        //AddUniqueID(dispPnl, 0, row++);
        AddDisplayName(dispPnl, 0, row++);
        AddTaskDuration(dispPnl, 0, row++);
        AddDescription(dispPnl, 0, row++);
        AddUserNotes(dispPnl, 0, row++);
        AddInvokeablesButtons(dispPnl, 0, row++);
		
		return dispPnl;
	}
	
	private JPanel CreateControlBtnPanel()
	{
        JPanel controlPnl = new JPanel();
        controlPnl.setLayout(new FlowLayout());
        
        ok = new JButton("Ok");
        ok.addActionListener(this);
		
        edit = new JButton("Edit");
        edit.addActionListener(this);
	    
		controlPnl.add(ok);
		
		if(!serNode.getUniqueID().equals(WoWSerializableNode.StartableWorkId))
		{
			controlPnl.add(edit);
		}
		
		return controlPnl;
	}
	
	private JPanel CreateMainDisplayWoWPanel()
	{
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
		mainPanel.add(CreateControlBtnPanel(), BorderLayout.PAGE_END);
		
		JPanel inputPnl = CreateWoWDisplayPanel();
    	JScrollPane scrollPane = new JScrollPane(inputPnl);
    	inputPnl.setAutoscrolls(true);
    	scrollPane.setPreferredSize(new Dimension( inputPnl.getPreferredSize().width + 125, inputPnl.getPreferredSize().height + 125));        
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		
		return mainPanel;		
	}
	
	public DisplayWoWItemFrame(WoWSerializableNode iSerNode)
	{
		serNode = iSerNode;
	    setTitle("WoW item/Process step");
	    setLocationRelativeTo(null);
	    setContentPane(mainPanel);
	    mainPanel.add(CreateMainDisplayWoWPanel());
	    pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == ok)
		{
			serNode.setUserNotes(userNotesInput.getText());
						
			NotifyEditDone();
			
			setVisible(false);
			dispose();
		}
		else if (e.getSource() == edit) 
		{
			EditWoWItemFrame createWoWItem = new EditWoWItemFrame(serNode);
			createWoWItem.addListener(this);
			createWoWItem.setVisible(true);
		}				
	}

	@Override
	public void EditDone(WoWSerializableNode serNode) 
	{
		this.serNode = serNode;
		serNode.SaveWoWItemToFile(WoWSerializableNode.WoWItemsFolder);
			
		mainPanel.removeAll();
		mainPanel.add(CreateMainDisplayWoWPanel());
	}
}

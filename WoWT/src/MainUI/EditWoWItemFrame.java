package MainUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
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
public class EditWoWItemFrame extends JFrame implements ActionListener
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
	
	WoWSerializableNode serNode;
	
	private JPanel CreateUniqueIDInputPanel()
	{
        JPanel uniqueIDPnl = new JPanel();
        uniqueIDPnl.setLayout(new FlowLayout());
        
		uniqueIdInfo = new JLabel("The unique identifier of this WoW item - used in WoW tree definition");
		uniqueIdInput = new JTextField(20);
		
		uniqueIdInput.setText(serNode.getUniqueID());
		
		uniqueIDPnl.add(uniqueIdInfo);
		uniqueIDPnl.add(uniqueIdInput);
		
		return uniqueIDPnl;
	}
	
	private JPanel CreateDisplayNameInputPanel()
	{
        JPanel displayNamePnl = new JPanel();
        displayNamePnl.setLayout(new FlowLayout());
		
		displayNameInfo = new JLabel("Display name of the WoW item - visible in the main view");
		displayNameInput = new JTextField(20);
		
		displayNameInput.setText(serNode.getDisplayName());
		
		displayNamePnl.add(displayNameInfo);
		displayNamePnl.add(displayNameInput);
		
		return displayNamePnl;
	}
	
	private JPanel CreateDescriptionInputPanel()
	{
        JPanel descriptionPnl = new JPanel();
        descriptionPnl.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		descriptionInfo = new JLabel("Description visible in the main view");
		descriptionInput = new JTextArea(5,60);
		descriptionInput.setLineWrap(true);
		
		descriptionInput.setText(serNode.getDescription());
		
		descriptionPnl.add(descriptionInfo);
		descriptionPnl.add(descriptionInput);
		
		return descriptionPnl;
	}
	
	private JPanel CreateTaskDurationInputPanel()
	{
		JPanel durationPnl = new JPanel();
		durationPnl.setLayout(new FlowLayout());
        
		taskEstimatedDurationInfo = new JLabel("Task duration in days");
		taskEstimatedDurationInput = new JSpinner();
		taskEstimatedDurationInput.setModel(new SpinnerNumberModel(0, 0, 10, 0.5));
		
		taskEstimatedDurationInput.setValue(serNode.getTaskDaysEstimate());
		
		durationPnl.add(taskEstimatedDurationInfo);
		durationPnl.add(taskEstimatedDurationInput);
		
		return durationPnl;
	}
	
	private JPanel CreateUserNotesInputPanel()
	{
        JPanel userNotesPnl = new JPanel();
        userNotesPnl.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		userNotesInfo = new JLabel("User notes for this process step");
		userNotesInput = new JTextArea(5,60);
		userNotesInput.setLineWrap(true);
		
		userNotesInput.setText(serNode.getUserNotes());
		
		userNotesPnl.add(userNotesInfo);
		userNotesPnl.add(userNotesInput);
		
		return userNotesPnl;
	}
	
	private JPanel CreateInvokeListInputPanel()
	{
        JPanel invokeLstPnl = new JPanel();
        invokeLstPnl.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
        invkLstInfo = new JLabel("Input invoke list separated by new lines");
        invkLstInput = new JTextArea(5,60);
        invkLstInput.setLineWrap(true);
        
        for(String invokeable : serNode.getInvokeables())
        {
        	invkLstInput.setText(invokeable + "\n");
        }
		
		invokeLstPnl.add(invkLstInfo);
		invokeLstPnl.add(invkLstInput);
		
		return invokeLstPnl;
	}
	
	private JPanel CreateInputPanelsGrid()
	{
        JPanel inputPnl = new JPanel();
        inputPnl.setLayout(new GridLayout(0,1));
		
		inputPnl.add(CreateUniqueIDInputPanel());
		inputPnl.add(CreateTaskDurationInputPanel());
		inputPnl.add(CreateDisplayNameInputPanel());
		inputPnl.add(CreateDescriptionInputPanel());
		inputPnl.add(CreateUserNotesInputPanel());
		inputPnl.add(CreateInvokeListInputPanel());
		
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
			
			serNode.setUniqueID(uId);
			serNode.setTaskDaysEstimate((Float)taskEstimatedDurationInput.getValue());
			serNode.setDisplayName(dispName);
			serNode.setDescription(descriptionInput.getText());
			serNode.setUserNotes(userNotesInput.getText());
			serNode.setInvokeables(invkLst);
			
			setVisible(false);
			dispose();
		}
		else if (e.getSource() == cancel) 
		{
			setVisible(false);
			dispose();
		}		
	}
}

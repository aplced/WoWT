package MainUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import WoWSerialization.WoWSerializableNode;

@SuppressWarnings("serial")
public class DisplayWoWItemFrame extends JFrame implements ActionListener, WoWEditDoneAction
{
	JButton ok;
	JButton edit;
	
	JTextArea userNotesInput;
	JPanel mainPanel = new JPanel();
	
	WoWSerializableNode serNode;
	
	private JPanel CreateUniqueIDDisplayPanel()
	{
        JPanel uniqueIDPnl = new JPanel();
        uniqueIDPnl.setLayout(new FlowLayout());
        
        JLabel uniqueIdInfo = new JLabel(serNode.getUniqueID());
			
		uniqueIDPnl.add(uniqueIdInfo);
		
		return uniqueIDPnl;
	}
	
	private JPanel CreateDisplayNameDisplayPanel()
	{
        JPanel displayNamePnl = new JPanel();
        displayNamePnl.setLayout(new FlowLayout());
		
        JLabel displayNameInfo = new JLabel(serNode.getDisplayName());
		
		displayNamePnl.add(displayNameInfo);
		
		return displayNamePnl;
	}
	
	private JPanel CreateDescriptionDisplayPanel()
	{
        JPanel descriptionPnl = new JPanel();
        descriptionPnl.setLayout(new FlowLayout());
		
        JTextArea descriptionInput = new JTextArea(5,60);
		descriptionInput.setLineWrap(true);
		//descriptionInput.setEnabled(false);
				
		descriptionInput.setText(serNode.getDescription());
		
		descriptionPnl.add(descriptionInput);
		
		return descriptionPnl;
	}
	
	private JPanel CreateTaskDurationDisplayPanel()
	{
		JPanel durationPnl = new JPanel();
		durationPnl.setLayout(new FlowLayout());
        
		JLabel taskEstimatedDurationInfo = new JLabel("Task duration " + serNode.getTaskDaysEstimate() + " days");
			
		durationPnl.add(taskEstimatedDurationInfo);
		
		return durationPnl;
	}
	
	private JPanel CreateUserNotesInputPanel()
	{
        JPanel userNotesPnl = new JPanel();
        userNotesPnl.setLayout(new FlowLayout());
		
		userNotesInput = new JTextArea(5,60);
		userNotesInput.setLineWrap(true);
		
		userNotesInput.setText(serNode.getUserNotes());
		
		userNotesPnl.add(userNotesInput);
		
		return userNotesPnl;
	}
	
	private JPanel CreateInvokeableBtnPanel()
	{
        JPanel invokeLstPnl = new JPanel();
        invokeLstPnl.setLayout(new FlowLayout());
		
        for(String invokeable : serNode.getInvokeables())
        {
        	
        }
		
		return invokeLstPnl;
	}
	
	private JPanel CreateWoWDisplayPanel()
	{
        JPanel dispPnl = new JPanel();
        dispPnl.setLayout(new GridLayout(0,1));
		
		dispPnl.add(CreateUniqueIDDisplayPanel());
		dispPnl.add(CreateTaskDurationDisplayPanel());
		dispPnl.add(CreateDisplayNameDisplayPanel());
		dispPnl.add(CreateDescriptionDisplayPanel());
		dispPnl.add(CreateUserNotesInputPanel());
		dispPnl.add(CreateInvokeableBtnPanel());
		
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
	    setTitle("Edit WoW item");
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

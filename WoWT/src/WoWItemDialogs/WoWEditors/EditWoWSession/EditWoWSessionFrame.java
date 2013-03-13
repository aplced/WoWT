package WoWItemDialogs.WoWEditors.EditWoWSession;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import WoWItemDialogs.WoWEditors.WoWEditorFrame;
import WoWItemDialogs.WoWValueInput.WoWValueInput;
import WoWSerialization.WoWFileHelper;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionInfoSerializable;


@SuppressWarnings("serial")
public class EditWoWSessionFrame extends WoWEditorFrame implements ActionListener
{
	JButton apply;
	JButton setDefault;
	JButton cancel;
	
	WoWValueInput devName;
	WoWValueInput devUserName;
	WoWValueInput streamName;
	WoWValueInput taskId;
	WoWValueInput taskName;
	WoWValueInput scrumMasters;
	WoWValueInput components;
	WoWValueInput buildingBlocks;
	WoWValueInput funcClusters;
	
	private JPanel CreateInputPanelsGrid()
	{
		int row = 0;
        JPanel inputPnl = new JPanel();
        inputPnl.setLayout(new GridBagLayout());
        
        devName = WoWValueInput.WoWTextFieldInput("Developer name:", serSession.getDeveloperName());
    	devUserName = WoWValueInput.WoWTextFieldInput("Developer username:", serSession.getUserName());
    	streamName = WoWValueInput.WoWTextFieldInput("Stream name:", serSession.getStreamName());
    	taskId = WoWValueInput. WoWTextFieldInput("Task Id:", serSession.getTaskId().toString());
    	taskName = WoWValueInput.WoWTextFieldInput("Task Name:", serSession.getTaskName());
    	scrumMasters = WoWValueInput.WoWTextAreaInput("Scrum Master:", serSession.getScrumMasters());
    	components = WoWValueInput.WoWTextAreaInput("Components list", serSession.getComponents());
    	buildingBlocks = WoWValueInput.WoWTextAreaInput("Building blocks list", serSession.getBuildingBlocks());
    	funcClusters = WoWValueInput.WoWTextAreaInput("Functional cluster list", serSession.getFuncClusters());
    	
        devName.AddInputToPanel(inputPnl, 0, row++);
        devUserName.AddInputToPanel(inputPnl, 0, row++);
        streamName.AddInputToPanel(inputPnl, 0, row++);
        taskId.AddInputToPanel(inputPnl, 0, row++);
        taskName.AddInputToPanel(inputPnl, 0, row++);
        scrumMasters.AddInputToPanel(inputPnl, 0, row++);
        components.AddInputToPanel(inputPnl, 0, row++);
        buildingBlocks.AddInputToPanel(inputPnl, 0, row++);
        funcClusters.AddInputToPanel(inputPnl, 0, row++);
		
		return inputPnl;
	}
	
	private JPanel CreateControlBtnPanel()
	{
        JPanel controlPnl = new JPanel();
        controlPnl.setLayout(new FlowLayout());
        
		apply = new JButton("Apply");
		apply.addActionListener(this);
		
		setDefault = new JButton("Set default");
		setDefault.addActionListener(this);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
	    
		controlPnl.add(apply);
		controlPnl.add(setDefault);
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
	
	private void CollectUserInput()
	{
		serSession.setDeveloperName(devName.GetInputValue());
		serSession.setUserName(devUserName.GetInputValue());		
		serSession.setStreamName(streamName.GetInputValue());
		serSession.setTaskId(Integer.parseInt(taskId.GetInputValue()));
		serSession.setTaskName(taskName.GetInputValue());
		serSession.setScrumMasters(scrumMasters.GetInputValue());
		serSession.setComponents(components.GetInputValue());
		serSession.setBuildingBlocks(buildingBlocks.GetInputValue());
		serSession.setFuncClusters(funcClusters.GetInputValue());
	}
	
	public EditWoWSessionFrame(WoWSessionInfoSerializable iSerSession)
	{
		serSession = iSerSession;
	    setTitle("Stream delivery session");
	    setLocationRelativeTo(null);
	    setContentPane(CreateMainNewWoWItemPanel());
	    pack();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == apply)
		{
			CollectUserInput();
			
			NotifyWoWSessionEditDone();
			ClearAndClose();
		}
		else if(e.getSource() == setDefault)
		{
			CollectUserInput();
			serSession.SaveToFile(WoWFileHelper.WoWDefaultSessionInfo);
			
			NotifyWoWSessionEditDone();
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

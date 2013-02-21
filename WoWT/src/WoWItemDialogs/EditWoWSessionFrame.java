package WoWItemDialogs;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import WoWSerialization.WoWSessionSerializable;


@SuppressWarnings("serial")
public class EditWoWSessionFrame extends WoWEditorFrame implements ActionListener
{
	JButton apply;
	JButton cancel;
	
	JLabel devName;
	JLabel devUserName;
	JLabel components;
	JLabel buildingBlocks;
	JLabel funcClusters;
	
	JTextField devNameInput;
	JTextField devUserNameInput;
	JTextArea componentsInput;
	JTextArea buildingBlocksInput;
	JTextArea funcClustersInput;
	
	private void AddDevNameInput(JPanel dispPnl, int col, int row)
	{
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);
        
        devName = new JLabel("Developer name:");
		dispPnl.add(devName, c);
		
		c.gridx = c.gridx + 1;
		devNameInput = new JTextField(20);
		devNameInput.setText(serSession.getDeveloperName());
		dispPnl.add(devNameInput, c);
	}
	
	private void AddDevUserNameInput(JPanel dispPnl, int col, int row)
	{
		GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);
		
        devUserName = new JLabel("Developer username:");
		dispPnl.add(devUserName, c);
		
		c.gridx = c.gridx + 1;
		devUserNameInput = new JTextField(20);
		devUserNameInput.setText(serSession.getUserName());
		dispPnl.add(devUserNameInput, c);
	}
	
	private void AddComponentsInput(JPanel dispPnl, int col, int row)
	{
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4,4,4,4);
		
        components = new JLabel("Components list");
		dispPnl.add(components, c);
		
		c.gridx = c.gridx + 1;
		componentsInput = new JTextArea(5,60);
		componentsInput.setLineWrap(true);
		componentsInput.setText(serSession.getComponents());
		dispPnl.add(componentsInput, c);
	}
	
	private void AddBuildingBlocksInput(JPanel dispPnl, int col, int row)
	{
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4,4,4,4);
		
        buildingBlocks = new JLabel("Building blocks list");
		dispPnl.add(buildingBlocks, c);
		
		c.gridx = c.gridx + 1;
		buildingBlocksInput = new JTextArea(5,60);
		buildingBlocksInput.setLineWrap(true);
		buildingBlocksInput.setText(serSession.getBuildingBlocks());
		dispPnl.add(buildingBlocksInput, c);
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
		
        funcClusters = new JLabel("Functional cluster list");
        dispPnl.add(funcClusters, c);
        
		c.gridx = c.gridx + 1;
		funcClustersInput = new JTextArea(5,60);
		funcClustersInput.setLineWrap(true);
		funcClustersInput.setText(serSession.getFuncClusters());
		dispPnl.add(funcClustersInput, c);
	}
	
	private JPanel CreateInputPanelsGrid()
	{
		int row = 0;
        JPanel inputPnl = new JPanel();
        inputPnl.setLayout(new GridBagLayout());
		
        AddDevNameInput(inputPnl, 0, row++);
        AddDevUserNameInput(inputPnl, 0, row++);
        AddComponentsInput(inputPnl, 0, row++);
        AddBuildingBlocksInput(inputPnl, 0, row++);
        AddInvokeListInput(inputPnl, 0, row++);
		
		return inputPnl;
	}
	
	private JPanel CreateControlBtnPanel()
	{
        JPanel controlPnl = new JPanel();
        controlPnl.setLayout(new FlowLayout());
        
		apply = new JButton("Save");
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
	
	public EditWoWSessionFrame(WoWSessionSerializable iSerSession)
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
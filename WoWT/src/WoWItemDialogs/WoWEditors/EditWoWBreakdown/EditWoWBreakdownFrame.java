package WoWItemDialogs.WoWEditors.EditWoWBreakdown;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import WoWItemDialogs.WoWEditors.WoWEditorFrame;
import WoWItemDialogs.WoWValueInput.WoWValueInput;
import WoWPanelUI.WoWItemJPanels.WoWItemJPanel;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSerializableNode;


@SuppressWarnings("serial")
public class EditWoWBreakdownFrame extends WoWEditorFrame implements ActionListener
{
	JButton email;
	JButton cancel;
	
	ArrayList<WoWValueInput> breakdown = new ArrayList<WoWValueInput>();
	
    private void CreateWoWBreakdownItems(WoWItemJPanel wowItem)
    {
    	if(wowItem.GetUniqueID() != WoWSerializableNode.StartableWorkId)
    	{
    		breakdown.add(WoWValueInput.WoWEditableCheckboxInput(wowItem));
    	}
    	
    	for(WoWItemJPanel child : wowItem.GetChildren())
    	{
    		CreateWoWBreakdownItems(child);
    	}
    }
	
	private JPanel CreateInputPanelsGrid()
	{
		int row = 0;
        JPanel inputPnl = new JPanel();
        inputPnl.setLayout(new GridBagLayout());
        
        for(WoWValueInput item : breakdown)
        {
        	item.AddInputToPanel(inputPnl, 0, row++);
        }
        
		return inputPnl;
	}
	
	private JPanel CreateControlBtnPanel()
	{
        JPanel controlPnl = new JPanel();
        controlPnl.setLayout(new FlowLayout());
        
		email = new JButton("e-mail");
		email.addActionListener(this);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
	    
		controlPnl.add(email);
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
	
	public EditWoWBreakdownFrame(WoWItemJPanel wowItem)
	{
	    setTitle("Work breakdown");
	    setLocationRelativeTo(null);
	    CreateWoWBreakdownItems(wowItem);
	    setContentPane(CreateMainNewWoWItemPanel());
	    pack();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == email)
		{
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

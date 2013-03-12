package WoWItemDialogs;

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
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSerializableNode;

@SuppressWarnings("serial")
public class UserPrompt extends WoWEditorFrame implements ActionListener
{	
	JButton ok;
	JButton cancel;
	
	ArrayList<WoWValueInput> promptFields = new ArrayList<WoWValueInput>();
	
	private JPanel CreatePromptPanel()
	{
		int row = 0;
        JPanel dispPnl = new JPanel();
        dispPnl.setLayout(new GridBagLayout());
        		
        for(WoWValueInput field : promptFields)
        {
        	field.AddInputToPanel(dispPnl, 0, row++);
        }
		
		return dispPnl;
	}
	
	private JPanel CreateControlBtnPanel()
	{
        JPanel controlPnl = new JPanel();
        controlPnl.setLayout(new FlowLayout());
        
        ok = new JButton("Ok");
        ok.addActionListener(this);
		
        cancel = new JButton("Edit template");
        cancel.addActionListener(this);
	    
		controlPnl.add(ok);
		controlPnl.add(cancel);
		
		return controlPnl;
	}
	
	private JPanel CreateMainDisplayWoWPanel()
	{
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
		mainPanel.add(CreateControlBtnPanel(), BorderLayout.PAGE_END);
		
		JPanel inputPnl = CreatePromptPanel();
    	JScrollPane scrollPane = new JScrollPane(inputPnl);
    	inputPnl.setAutoscrolls(true);
    	scrollPane.setPreferredSize(new Dimension( inputPnl.getPreferredSize().width + 125, inputPnl.getPreferredSize().height + 125));        
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		
		return mainPanel;		
	}
	
	public UserPrompt(String caption)
	{
	    setTitle(caption);
	    setLocationRelativeTo(null);
	}
	
	public void AddPromptPasswordField(String caption, String defaultVal)
	{
		promptFields.add(WoWValueInput.WoWPasswordFieldInput(caption));
	}
	
	public void AddPromptTextField(String caption, String defaultVal)
	{
		promptFields.add(WoWValueInput.WoWTextFieldInput(caption, defaultVal));
	}
	
	public void AddPromptTextArea(String caption, String defaultVal)
	{
		promptFields.add(WoWValueInput.WoWTextAreaInput(caption, defaultVal));
	}
	
	public void AddPromptCheckbox(String caption, String defaultVal)
	{
		promptFields.add(WoWValueInput.WoWCheckboxInput(caption, defaultVal));
	}
	
	public void AddPromptSpinner(String caption, String defaultVal)
	{
		promptFields.add(WoWValueInput.WoWSpinnerInput(caption, defaultVal));
	}
	
	public void DisplayPrompt()
	{
	    setContentPane(CreateMainDisplayWoWPanel());
	    pack();
		setVisible(true);
	}

	public String GetPrompt(int promptId)
	{
		if(promptId < promptFields.size())
		{
			return promptFields.get(promptId).GetInputValue();
		}
		
		return null;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == ok)
		{
			setVisible(false);
			dispose();
			
			NotifyWoWItemEditDone();
			ClearListeners();
		}
		else if (e.getSource() == cancel) 
		{
			setVisible(false);
			dispose();
		}				
	}
}

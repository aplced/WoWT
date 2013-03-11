package WoWItemDialogs.WoWValueInput;

import java.awt.GridBagConstraints;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import WoWItemDialogs.WoWValueInput.Implementation.WoWSpinnerInputValue;
import WoWItemDialogs.WoWValueInput.Implementation.WoWTextAreaInputValue;
import WoWItemDialogs.WoWValueInput.Implementation.WoWTextFieldInputValue;

public class WoWValueInput
{
	JLabel inpName;
	IWoWValueInput inpValue;
	GridBagConstraints constraints;	
	
	public static WoWValueInput WoWTextFieldInput(String inputName, String defaultValue)
	{
		IWoWValueInput inpValue = new WoWTextFieldInputValue(20);
		inpValue.SetInputValue(defaultValue);
		
		return new WoWValueInput(inputName, inpValue, WoWTextFieldInputValue.GetGridConstraints());
	}
	
	public static WoWValueInput WoWTextAreaInput(String inputName, String defaultValue)
	{
		IWoWValueInput inpValue = new WoWTextAreaInputValue(5,60);
		inpValue.SetInputValue(defaultValue);
		
		return new WoWValueInput(inputName, inpValue, WoWTextAreaInputValue.GetGridConstraints());
	}
	
	public static WoWValueInput WoWSpinnerInput(String inputName, String defaultValue)
	{
		IWoWValueInput inpValue = new WoWSpinnerInputValue();
		inpValue.SetInputValue(defaultValue);
		
		return new WoWValueInput(inputName, inpValue, WoWSpinnerInputValue.GetGridConstraints());
	}
	
	private WoWValueInput(String inputName, IWoWValueInput inputComp, GridBagConstraints c)
	{
		inpName = new JLabel(inputName);
		inpValue = inputComp;
		constraints = c;
	}
	
	public void AddInputToPanel(JPanel dispPnl, int col, int row)
	{
		constraints.gridx = col;
        constraints.gridy = row;
        
		dispPnl.add(inpName, constraints);
		
		constraints.gridx = constraints.gridx + 1;
		
		dispPnl.add((JComponent)inpValue, constraints);
	}
	
	public String GetInputValue()
	{
		return inpValue.GetInputValue();
	}
}

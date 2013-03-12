package WoWItemDialogs.WoWValueInput.Implementation;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPasswordField;

import WoWItemDialogs.WoWValueInput.IWoWValueInput;

@SuppressWarnings("serial")
public class WoWPasswordFieldInputValue extends JPasswordField implements IWoWValueInput
{
	public static GridBagConstraints GetGridConstraints()
	{
		GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);
        
        return c;
	}
	
	public WoWPasswordFieldInputValue(int columns)
	{
		super(columns);
	}
	
	@Override
	public String GetInputValue() 
	{
		return new String(getPassword());
	}

	@Override
	public void SetInputValue(String value) 
	{
		setText(value);
	}

}

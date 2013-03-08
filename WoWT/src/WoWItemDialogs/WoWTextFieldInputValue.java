package WoWItemDialogs;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class WoWTextFieldInputValue extends JTextField implements IWoWValueInput
{
	public static GridBagConstraints GetGridConstraints()
	{
		GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);
        
        return c;
	}
	
	public WoWTextFieldInputValue(int columns)
	{
		super(columns);
	}
	
	@Override
	public String GetInputValue() 
	{
		return getText();
	}

	@Override
	public void SetInputValue(String value) 
	{
		setText(value);
	}

}

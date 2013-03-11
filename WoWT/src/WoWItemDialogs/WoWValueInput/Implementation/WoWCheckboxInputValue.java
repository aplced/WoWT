package WoWItemDialogs.WoWValueInput.Implementation;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;

import WoWItemDialogs.WoWValueInput.IWoWValueInput;

@SuppressWarnings("serial")
public class WoWCheckboxInputValue extends JCheckBox implements IWoWValueInput
{	
	public static GridBagConstraints GetGridConstraints()
	{
		GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);
 
        return c;
	}

	@Override
	public String GetInputValue() 
	{
		return ((Boolean)isSelected()).toString();
	}

	@Override
	public void SetInputValue(String value) 
	{
		setSelected(Boolean.parseBoolean(value));
	}
}

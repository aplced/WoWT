package WoWItemDialogs;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class WoWSpinnerInputValue extends JSpinner implements IWoWValueInput 
{
	public static GridBagConstraints GetGridConstraints()
	{
		GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(4,4,4,4);
        
        return c;
	}
	
	public WoWSpinnerInputValue()
	{
		setModel(new SpinnerNumberModel(0, 0, 10, 0.5));
	}

	@Override
	public String GetInputValue() 
	{
		return getValue().toString();
	}

	@Override
	public void SetInputValue(String value) 
	{
		setValue(Float.parseFloat(value));
	}
}

package WoWItemDialogs.WoWValueInput.Implementation;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextArea;

import WoWItemDialogs.WoWValueInput.IWoWValueInput;

@SuppressWarnings("serial")
public class WoWTextAreaInputValue extends JTextArea implements IWoWValueInput 
{
	public static GridBagConstraints GetGridConstraints()
	{
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4,4,4,4);
        
        return c;
	}
	public WoWTextAreaInputValue(int rows, int columns)
	{
		super(rows, columns);
		
		setLineWrap(true);
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

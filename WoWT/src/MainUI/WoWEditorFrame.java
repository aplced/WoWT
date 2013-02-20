package MainUI;

import java.util.ArrayList;

import javax.swing.JFrame;

import WoWSerialization.WoWSerializableNode;

@SuppressWarnings("serial")
public abstract class WoWEditorFrame extends JFrame
{
	protected WoWSerializableNode serNode;
	
	protected ArrayList<WoWEditDoneAction> listeners = new ArrayList<WoWEditDoneAction>();
	
    public void addListener(WoWEditDoneAction toAdd) 
    {
        listeners.add(toAdd);
    }
    
    public void removeListener(WoWEditDoneAction toRem)
    {
    	listeners.remove(toRem);
    }
    
    protected void NotifyEditDone() 
	{
       for (WoWEditDoneAction hl : listeners)
       {
            hl.EditDone(serNode);
       }
	}    
}

package WoWItemDialogs.WoWEditors;

import java.util.ArrayList;

import javax.swing.JFrame;

import WoWItemDialogs.WoWEditors.EditWoWItem.IWoWItemEditDoneAction;
import WoWItemDialogs.WoWEditors.EditWoWSession.IWoWSessionEditDoneAction;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSerializableNode;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionInfoSerializable;

@SuppressWarnings("serial")
public abstract class WoWEditorFrame extends JFrame
{
	protected WoWSerializableNode serNode;
	protected WoWSessionInfoSerializable serSession;
	
	protected ArrayList<IWoWItemEditDoneAction> wowItemListeners = new ArrayList<IWoWItemEditDoneAction>();
	protected ArrayList<IWoWSessionEditDoneAction> wowSessionListeners = new ArrayList<IWoWSessionEditDoneAction>();
	
	protected void ClearListeners()
	{
		wowItemListeners.clear();
		wowSessionListeners.clear();
	}
	
    public void addWoWItemEditListener(IWoWItemEditDoneAction toAdd) 
    {
        wowItemListeners.add(toAdd);
    }
    
    public void removeWoWItemEditListener(IWoWItemEditDoneAction toRem)
    {
    	wowItemListeners.remove(toRem);
    }
    
    protected void NotifyWoWItemEditDone() 
	{
       for (IWoWItemEditDoneAction hl : wowItemListeners)
       {
            hl.EditDone(serNode);
       }
	} 
    
    public void addWoWSessionEditListener(IWoWSessionEditDoneAction toAdd) 
    {
    	wowSessionListeners.add(toAdd);
    }
    
    public void removeWoWSessionEditListener(IWoWSessionEditDoneAction toRem)
    {
    	wowSessionListeners.remove(toRem);
    }
    
    protected void NotifyWoWSessionEditDone() 
	{
       for (IWoWSessionEditDoneAction hl : wowSessionListeners)
       {
            hl.EditDone(serSession);
       }
	} 
    
}

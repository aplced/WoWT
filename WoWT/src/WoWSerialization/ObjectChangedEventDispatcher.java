package WoWSerialization;

import java.util.ArrayList;

public class ObjectChangedEventDispatcher
{	
	protected ArrayList<IWoWDataChangedAction> wowItemListeners = new ArrayList<IWoWDataChangedAction>();
	
	protected void ClearListeners()
	{
		wowItemListeners.clear();
	}
	
    public void addObjectChangedEventListener(IWoWDataChangedAction toAdd) 
    {
        wowItemListeners.add(toAdd);
    }
    
    public void removeObjectChangedEventEditListener(IWoWDataChangedAction toRem)
    {
    	wowItemListeners.remove(toRem);
    }
    
    public void FireObjectChangedEvent() 
	{
       for (IWoWDataChangedAction hl : wowItemListeners)
       {
            hl.DataChanged();
       }
	} 
}
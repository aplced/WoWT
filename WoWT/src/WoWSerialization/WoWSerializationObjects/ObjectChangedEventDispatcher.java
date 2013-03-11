package WoWSerialization.WoWSerializationObjects;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ObjectChangedEventDispatcher
{	
	Semaphore lock = new Semaphore(1);
	
	protected ArrayList<IWoWDataChangedAction> wowItemListeners = new ArrayList<IWoWDataChangedAction>();
	private ArrayList<IWoWDataChangedAction> removedListeners = new ArrayList<IWoWDataChangedAction>();
	
	protected void ClearListeners()
	{
		try 
    	{
			lock.acquire();
		}
    	catch (InterruptedException e) 
    	{
			e.printStackTrace();
		}
		
		wowItemListeners.clear();
		
		lock.release();
	}
	
    public void addObjectChangedEventListener(IWoWDataChangedAction toAdd) 
    {
    	if(!wowItemListeners.contains(toAdd))
    	{
	    	try 
	    	{
				lock.acquire();
			}
	    	catch (InterruptedException e) 
	    	{
				e.printStackTrace();
			}
	    	
	        wowItemListeners.add(toAdd);
	        
	        RemoveQueuedListeners();
	        
	        lock.release();
    	}
    }
    
    public void removeObjectChangedEventEditListener(IWoWDataChangedAction toRem)
    {
    	if(wowItemListeners.contains(toRem))
    	{
    		if(lock.tryAcquire())
    		{
    			wowItemListeners.remove(toRem);
    	
    			lock.release();
    		}
    		else
    		{
    			removedListeners.add(toRem);
    		}
    	}
    }
    
    public void FireObjectChangedEvent() 
	{
    	try 
    	{
			lock.acquire();
		}
    	catch (InterruptedException e) 
    	{
			e.printStackTrace();
		}
    	
    	for (IWoWDataChangedAction hl : wowItemListeners)
    	{
    		hl.DataChanged();
    	}
    	
    	RemoveQueuedListeners();
    	
    	lock.release();
	} 
    
    private void RemoveQueuedListeners()
    {
    	while(!removedListeners.isEmpty())
    	{
    		IWoWDataChangedAction toRem = removedListeners.get(0);
    		removedListeners.remove(0);
    		wowItemListeners.remove(removedListeners);
    	}
    }
}
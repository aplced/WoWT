package WoWSerialization.WoWSerializationObjects;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ObjectChangedEventDispatcher
{	
	Semaphore listenersLock = new Semaphore(1);
	Semaphore toBeRemovedLock = new Semaphore(1);
	
	protected ArrayList<IWoWDataChangedAction> wowItemListeners = new ArrayList<IWoWDataChangedAction>();
	private ArrayList<IWoWDataChangedAction> removedListeners = new ArrayList<IWoWDataChangedAction>();
	
	protected void ClearListeners()
	{
		if(listenersLock.tryAcquire())
		{
		
			wowItemListeners.clear();
		
			listenersLock.release();
		}
		else
		{
			try 
			{
				toBeRemovedLock.acquire();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}

			removedListeners.addAll(wowItemListeners);
			
			toBeRemovedLock.release();
		}
	}
	
    public void addObjectChangedEventListener(IWoWDataChangedAction toAdd) 
    {
    	if(!wowItemListeners.contains(toAdd))
    	{
	    	try 
	    	{
				listenersLock.acquire();
			}
	    	catch (InterruptedException e) 
	    	{
				e.printStackTrace();
			}
	    	
	        wowItemListeners.add(toAdd);
	        
	        RemoveQueuedListeners();
	        
	        listenersLock.release();
    	}
    }
    
    public void removeObjectChangedEventEditListener(IWoWDataChangedAction toRem)
    {
    	if(wowItemListeners.contains(toRem))
    	{
    		if(listenersLock.tryAcquire())
    		{
    			wowItemListeners.remove(toRem);
    	
    			listenersLock.release();
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
			listenersLock.acquire();
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
    	
    	listenersLock.release();
	} 
    
    private void RemoveQueuedListeners()
    {
    	try 
		{
			toBeRemovedLock.acquire();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
    	
    	while(!removedListeners.isEmpty())
    	{
    		IWoWDataChangedAction toRem = removedListeners.get(0);
    		removedListeners.remove(0);
    		wowItemListeners.remove(toRem);
    	}
    	
    	toBeRemovedLock.release();
    }
}
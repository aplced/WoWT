package WoWPanelUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class WoWTreeTimerUpdate implements ActionListener 
{
	Timer  refresher;
	ArrayList<IRefreshInfo> objRef = new ArrayList<IRefreshInfo>();
	
	public void AddRefreshInfoObject(IRefreshInfo obj)
	{
		synchronized(objRef)
		{
			if(!objRef.contains(obj))
			{
				objRef.add(obj);
			}
		}
	}
	
	public void RemoveRfreshInfoObject(IRefreshInfo obj)
	{
		synchronized(objRef)
		{
			if(objRef.contains(obj))
			{
				objRef.remove(obj);
			}
		}
	}
	
	public void Start(int Period)
	{
		refresher = new Timer(Period, this);
		refresher.start();
	}
	
	public void Stop()
	{
		refresher.stop();
		refresher.removeActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		synchronized(objRef)
		{
			for(IRefreshInfo obj : objRef)
			{
				obj.Refresh();
			}
		}
	}
}

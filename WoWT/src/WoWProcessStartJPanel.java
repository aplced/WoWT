import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@SuppressWarnings("serial")
public class WoWProcessStartJPanel extends WoWItemJPanel 
{	
	public WoWProcessStartJPanel(WoWSerializableNode serNode) 
	{
		super(serNode);
		
		updateStartWorkCheckbox();
		
		elapsedDaysSinceStart = calculateDaysSinceStart();
	}

	private void updateStartWorkCheckbox()
	{
		if(doneState.isSelected())
		{
			doneState.setEnabled(false);
		}
	}
	
	@Override
	protected void performChangeStateAction()
	{
		if(doneState.isSelected())
		{
			updateStartWorkCheckbox();
			elapsedDaysSinceStart = calculateDaysSinceStart();
		}
		notifyChildrenOfStateChange(elapsedDaysSinceStart);
	}
	
	@Override
	protected void SetControlsEnabled(boolean enabled)
	{
		displayName.setEnabled(true);
		popDescription.setEnabled(true);
		doneState.setEnabled(true);
		
		updateStartWorkCheckbox();
	}
	
	private int calculateDaysSinceStart()
	{
		int daysSinceStart = 0;
		Calendar workStart = Calendar.getInstance();		
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
		try 
		{
			workStart.setTime(df.parse(description));
		    Calendar timeNow = Calendar.getInstance();

		    daysSinceStart = (int) ((timeNow.getTimeInMillis() - workStart.getTimeInMillis()) / (24 * 60 * 60 * 1000));
		}
		catch (ParseException e) 
		{
			description = df.format(Calendar.getInstance().getTime());
		}
		
		return daysSinceStart;
	}
	
}

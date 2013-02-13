import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@SuppressWarnings("serial")
public class WoWProcessStartJPanel extends WoWItemJPanel 
{

	public WoWProcessStartJPanel(WoWSerialableNode serNode) 
	{
		super(serNode);
	}

	@Override
	protected void performChangeStateAction()
	{
		int daysSinceStart = 0;
		Calendar workStart = Calendar.getInstance();		
		DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
		try {
			workStart.setTime(df.parse(description));
		    Calendar timeNow = Calendar.getInstance();

		    daysSinceStart = (int) ((timeNow.getTimeInMillis() - workStart.getTimeInMillis()) / (24 * 60 * 60 * 1000));
		} catch (ParseException e) {
			description = df.format(Calendar.getInstance().getTime());
		}
		notifyChildrenOfStateChange(daysSinceStart);
	}
	
}

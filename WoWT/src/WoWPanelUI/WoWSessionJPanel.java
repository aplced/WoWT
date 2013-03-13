package WoWPanelUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import WoWItemDialogs.WoWEditors.EditWoWSession.EditWoWSessionFrame;
import WoWItemDialogs.WoWEditors.EditWoWSession.IWoWSessionEditDoneAction;
import WoWSerialization.WoWSerializationObjects.IWoWDataChangedAction;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionInfoSerializable;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionSerializable;


@SuppressWarnings("serial")
public class WoWSessionJPanel extends JPanel implements ActionListener, IWoWSessionEditDoneAction, IWoWDataChangedAction
{
	JLabel devNameUsr;
	JLabel streamName;
	JLabel affectedCCBBFC;
	
	JButton editSessionInfo;
	
	WoWSessionSerializable sessionRef;
	
	public WoWSessionJPanel(WoWSessionSerializable iSession)
	{
		sessionRef = iSession;
		sessionRef.addObjectChangedEventListener(this);
		
		editSessionInfo = new JButton("Set session info");
		editSessionInfo.addActionListener(this);
		
		devNameUsr = new JLabel("Developer name (username)");
		streamName = new JLabel("Stream name");
		affectedCCBBFC = new JLabel("Affectec Building blocks");

		add(devNameUsr);
		add(streamName);
		add(affectedCCBBFC);
		add(editSessionInfo);
		
		UpdateDisplayInfo(sessionRef.getSessionInfo());
	}
	
	public void SetNewSession(WoWSessionSerializable iSession)
	{
		sessionRef.removeObjectChangedEventEditListener(this);
		
		sessionRef = iSession;
		sessionRef.addObjectChangedEventListener(this);
		
		UpdateDisplayInfo(sessionRef.getSessionInfo());
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == editSessionInfo) 
		{
			EditWoWSessionFrame editWoWSession = new EditWoWSessionFrame(sessionRef.getSessionInfo());
			//editWoWSession.addWoWSessionEditListener(this);
			editWoWSession.setVisible(true);
		}
		
	}
	
	private void UpdateDisplayInfo(WoWSessionInfoSerializable wowSessionInfo)
	{
		devNameUsr.setText(wowSessionInfo.getDeveloperName() + " (" + wowSessionInfo.getUserName() + ")");
		streamName.setText(wowSessionInfo.getStreamName());
		affectedCCBBFC.setText("CC: " + wowSessionInfo.getComponents());
	}

	@Override
	public void EditDone(WoWSessionInfoSerializable serSession) 
	{
		UpdateDisplayInfo(serSession);
	}

	@Override
	public void DataChanged() 
	{
		UpdateDisplayInfo(sessionRef.getSessionInfo());
	}
	
}

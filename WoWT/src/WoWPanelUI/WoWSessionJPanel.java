package WoWPanelUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import WoWItemDialogs.EditWoWSessionFrame;
import WoWItemDialogs.IWoWSessionEditDoneAction;
import WoWSerialization.WoWSessionInfoSerializable;


@SuppressWarnings("serial")
public class WoWSessionJPanel extends JPanel implements ActionListener, IWoWSessionEditDoneAction
{
	JLabel devNameUsr;
	JLabel streamName;
	JLabel affectedCCBBFC;
	
	JButton editSessionInfo;
	
	WoWSessionInfoSerializable sessionRef;
	
	public WoWSessionJPanel(WoWSessionInfoSerializable iSession)
	{
		sessionRef = iSession;
		
		editSessionInfo = new JButton("Set session info");
		editSessionInfo.addActionListener(this);
		
		devNameUsr = new JLabel("Developer name (username)");
		streamName = new JLabel("Stream name");
		affectedCCBBFC = new JLabel("Affectec Building blocks");

		add(devNameUsr);
		add(streamName);
		add(affectedCCBBFC);
		add(editSessionInfo);
		
		UpdateDisplayInfo(sessionRef);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == editSessionInfo) 
		{
			EditWoWSessionFrame editWoWSession = new EditWoWSessionFrame(sessionRef);
			editWoWSession.addWoWSessionEditListener(this);
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
		UpdateDisplayInfo(sessionRef);
	}
	
}

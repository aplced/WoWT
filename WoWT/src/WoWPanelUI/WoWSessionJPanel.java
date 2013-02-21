package WoWPanelUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import WoWItemDialogs.EditWoWSessionFrame;
import WoWItemDialogs.IWoWSessionEditDoneAction;
import WoWSerialization.WoWSessionSerializable;


@SuppressWarnings("serial")
public class WoWSessionJPanel extends JPanel implements ActionListener, IWoWSessionEditDoneAction
{
	JButton sessionInfo;
	
	public WoWSessionJPanel()
	{
		sessionInfo = new JButton("Info");
		sessionInfo.addActionListener(this);
		add(sessionInfo);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == sessionInfo) 
		{
			WoWSessionSerializable tmp = new WoWSessionSerializable();
			EditWoWSessionFrame editWoWSession = new EditWoWSessionFrame(tmp);
			editWoWSession.addWoWSessionEditListener(this);
			editWoWSession.setVisible(true);
		}
		
	}

	@Override
	public void EditDone(WoWSessionSerializable serSession) 
	{
		// TODO Auto-generated method stub
		
	}
	
}

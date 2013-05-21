package MainUI;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import WoWItemDialogs.WoWEditors.EditWoWBreakdown.EditWoWBreakdownFrame;
import WoWItemDialogs.WoWEditors.EditWoWItem.EditWoWItemFrame;
import WoWItemDialogs.WoWEditors.EditWoWItem.IWoWItemEditDoneAction;
import WoWPanelUI.WoWTreeJPanel;
import WoWSerialization.WoWFileHelper;
import WoWSerialization.CustomFileFilter;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSerializableNode;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionSerializable;

@SuppressWarnings("serial")
public class BtnPanel extends JPanel implements ActionListener, IWoWItemEditDoneAction
{
	IMainWoWFrame mainFrame;
	JButton saveSession;
	JButton loadSession;
	JButton newSession;
	JButton createWoWItem;
	JButton prepareWoWBreakdown;
	JButton saveNotes;
	JFileChooser fcSession;
	JFileChooser fcNotes;
	
	public BtnPanel(IMainWoWFrame refMainFrame) 
	{
		setLayout(new FlowLayout());
		
		mainFrame = refMainFrame;
		
		fcSession = new JFileChooser(System.getProperty("user.home"));
		fcSession.addChoosableFileFilter(new CustomFileFilter("xml", "WoW tree"));
		
		fcNotes = new JFileChooser(System.getProperty("user.home"));
		fcNotes.addChoosableFileFilter(new CustomFileFilter("txt", "Text File"));
		
		saveSession = new JButton("Save Session");
		saveSession.addActionListener(this);
		add(saveSession);
		
		loadSession = new JButton("Load Session");
		loadSession.addActionListener(this);
		add(loadSession);
		
		newSession = new JButton("New Session");
		newSession.addActionListener(this);
		add(newSession);
		
		createWoWItem = new JButton("New WoW item");
		createWoWItem.addActionListener(this);
		add(createWoWItem);
		
		prepareWoWBreakdown = new JButton("Work breakdown");
		prepareWoWBreakdown.addActionListener(this);
		add(prepareWoWBreakdown);
		
		saveNotes = new JButton("Save Notes");
		saveNotes.addActionListener(this);
		add(saveNotes);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == newSession)
		{
			mainFrame.SetSession(mainFrame.CreateNewSession(), "New session");
		}
		else if (e.getSource() == createWoWItem) 
		{
			WoWSerializableNode serNode = new WoWSerializableNode();
			EditWoWItemFrame createWoWItem = new EditWoWItemFrame(serNode);
			createWoWItem.addWoWItemEditListener(this);
			createWoWItem.setVisible(true);
		}
		else if(e.getSource() == saveSession)
		{
			int returnVal = fcSession.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fcSession.getSelectedFile();
				mainFrame.SaveSession(file.getAbsolutePath());
			}
		}
		else if(e.getSource() == loadSession)
		{
			int returnVal = fcSession.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fcSession.getSelectedFile();
				WoWSessionSerializable session = WoWSessionSerializable.LoadSessionFromFile(file.getAbsolutePath());
				mainFrame.SetSession(session, file.getAbsolutePath());
			}
		}
		else if(e.getSource() == prepareWoWBreakdown)
		{
			WoWTreeJPanel treePanel = mainFrame.GetWoWTreePanel();
			EditWoWBreakdownFrame editBreakDown = new EditWoWBreakdownFrame(treePanel.GetRootPanel(), treePanel.GetLoadedSession().getSessionInfo());
			editBreakDown.setVisible(true);
		}
		else if(e.getSource() == saveNotes)
		{
			int returnVal = fcNotes.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fcNotes.getSelectedFile();
				mainFrame.SaveNotes(file.getAbsolutePath());
			}
		}
	}

	@Override
	public void EditDone(WoWSerializableNode serNode) 
	{
		serNode.SaveToFile(WoWFileHelper.WoWItemsFolder);
	}
}

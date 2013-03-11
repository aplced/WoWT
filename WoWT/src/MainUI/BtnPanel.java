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
import WoWSerialization.XMLFileFilter;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSerializableNode;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionSerializable;

@SuppressWarnings("serial")
public class BtnPanel extends JPanel implements ActionListener, IWoWItemEditDoneAction
{
	JPanel mainPanel;
	WoWTreeJPanel treePanel;
	IMainWoWFrame mainFrame;
	JButton saveSession;
	JButton loadSession;
	JButton openFile;
	JButton createWoWItem;
	JButton prepareWoWBreakdown;
	JFileChooser fcSession;
	JFileChooser fcWoWTree;;
	
	public BtnPanel(WoWTreeJPanel refTreePanel, JPanel refMainPanel, IMainWoWFrame refMainFrame) 
	{
		setLayout(new FlowLayout());
		
		mainPanel = refMainPanel;
		treePanel = refTreePanel;
		mainFrame = refMainFrame;
		
		fcSession = new JFileChooser(System.getProperty("user.home"));
		fcSession.addChoosableFileFilter(new XMLFileFilter());
		
		fcWoWTree = new JFileChooser(System.getProperty("user.home"));
		
		saveSession = new JButton("Save Session");
		saveSession.addActionListener(this);
		add(saveSession);
		
		loadSession = new JButton("Load Session");
		loadSession.addActionListener(this);
		add(loadSession);
		
		openFile = new JButton("Open WoW tree");
		openFile.addActionListener(this);
		add(openFile);
		
		createWoWItem = new JButton("New WoW item");
		createWoWItem.addActionListener(this);
		add(createWoWItem);
		
		prepareWoWBreakdown = new JButton("Work breakdown");
		prepareWoWBreakdown.addActionListener(this);
		add(prepareWoWBreakdown);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == openFile)
		{
			int returnVal = fcWoWTree.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fcWoWTree.getSelectedFile();
				WoWSessionSerializable session = new WoWSessionSerializable();
				session.CreateFromWoWTree(file.getAbsolutePath());
				treePanel.LoadSession(session);
				mainFrame.SetTitle(file.getAbsolutePath());
				mainPanel.revalidate();
			}
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
				treePanel.SaveSessionToFile(file.getAbsolutePath());
				mainFrame.SetTitle(file.getAbsolutePath());
			}
		}
		else if(e.getSource() == loadSession)
		{
			int returnVal = fcSession.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fcSession.getSelectedFile();
				treePanel.LoadSessionFromFile(file.getAbsolutePath());
				mainFrame.SetTitle(file.getAbsolutePath());
				mainPanel.revalidate();
			}
		}
		else if(e.getSource() == prepareWoWBreakdown)
		{
			EditWoWBreakdownFrame editBreakDown = new EditWoWBreakdownFrame(treePanel.GetRootPanel(), treePanel.GetLoadedSession().getSessionInfo());
			editBreakDown.setVisible(true);
		}
	}

	@Override
	public void EditDone(WoWSerializableNode serNode) 
	{
		serNode.SaveToFile(WoWFileHelper.WoWItemsFolder);
	}
}

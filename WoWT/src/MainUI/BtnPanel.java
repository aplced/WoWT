package MainUI;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import WoWPanelUI.WoWTreeJPanel;
import WoWSerialization.WoWSerializableNode;
import WoWSerialization.XMLFileFilter;

@SuppressWarnings("serial")
public class BtnPanel extends JPanel implements ActionListener
{
	JPanel mainPanel;
	WoWTreeJPanel treePanel;
	MainWoWFrame mainFrame;
	JButton saveSession;
	JButton loadSession;
	JButton openFile;
	JButton createWoWItem;
	JFileChooser fc;
	
	public BtnPanel(WoWTreeJPanel refTreePanel, JPanel refMainPanel, MainWoWFrame refMainFrame) 
	{
		setLayout(new FlowLayout());
		
		mainPanel = refMainPanel;
		treePanel = refTreePanel;
		mainFrame = refMainFrame;
		
		fc = new JFileChooser(System.getProperty("user.home"));
		fc.addChoosableFileFilter(new XMLFileFilter());
		
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
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == openFile)
		{
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				treePanel.LoadPanelFromFile(file.getAbsolutePath());
				mainFrame.SetTitle(file.getAbsolutePath());
				mainPanel.revalidate();
			}
		}
		else if (e.getSource() == createWoWItem) 
		{
			WoWSerializableNode serNode = new WoWSerializableNode();
			EditWoWItemFrame createWoWItem = new EditWoWItemFrame(serNode);
			createWoWItem.setVisible(true);
			serNode.SaveWoWItemToFile(WoWSerializableNode.WoWItemsFolder);
		}
		else if(e.getSource() == saveSession)
		{
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				treePanel.SaveSessionToFile(file.getAbsolutePath());
				mainFrame.SetTitle(file.getAbsolutePath());
			}
		}
		else if(e.getSource() == loadSession)
		{
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				treePanel.LoadSessionFromFile(file.getAbsolutePath());
				mainFrame.SetTitle(file.getAbsolutePath());
				mainPanel.revalidate();
			}
		} 
	}
}

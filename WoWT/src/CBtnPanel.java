import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class CBtnPanel extends JPanel implements ActionListener
{
	JPanel mainPanel;
	CWoWTreePanel treePanel;
	JButton openFile;
	JButton createWoWItem;
	JFileChooser fc;
	
	public CBtnPanel(CWoWTreePanel refTreePanel, JPanel refMainPanel) 
	{
		setLayout(new FlowLayout());
		
		mainPanel = refMainPanel;
		treePanel = refTreePanel;
		
		fc = new JFileChooser(System.getProperty("user.home"));
		fc.addChoosableFileFilter(new XMLFileFilter());
		
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
				mainPanel.revalidate();
			}
		}
		else if (e.getSource() == createWoWItem) 
		{
		}
	}
}

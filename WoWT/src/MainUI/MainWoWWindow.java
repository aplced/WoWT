package MainUI;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import WoWPanelUI.WoWSessionJPanel;
import WoWPanelUI.WoWTreeJPanel;
import WoWSerialization.WoWFileHelper;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionInfoSerializable;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionSerializable;


@SuppressWarnings("serial")
public class MainWoWWindow extends JFrame implements IMainWoWFrame
{
    public static void main(String[] args)
    {
    	MainWoWWindow win = new MainWoWWindow();
        win.setVisible(true);
    }
  
    public MainWoWWindow()
    {
    	super();
    	
        try
        { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        }
        catch (Exception e)
        {
        } 
        
        setTitle("WoW Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        WoWSessionSerializable session = new WoWSessionSerializable();
        WoWSessionInfoSerializable sessionInfo = WoWSessionInfoSerializable.LoadFromFile(WoWFileHelper.WoWDefaultSessionInfo);
        if(sessionInfo != null)
        {
        	session.setSessionInfo(sessionInfo);
        }
        session.CreateFromWoWTree(WoWFileHelper.WoWDefaultTree);
        
        WoWTreeJPanel treePanel = new WoWTreeJPanel();
        treePanel.LoadSession(session);
        
        BtnPanel btnsPanel = new BtnPanel(treePanel, mainPanel, this);
        
        mainPanel.add(btnsPanel, BorderLayout.PAGE_START);
        
    	JScrollPane scrollPane = new JScrollPane(treePanel);
    	treePanel.setAutoscrolls(true);
    	scrollPane.setPreferredSize(new Dimension( treePanel.getPreferredSize().width + 125, treePanel.getPreferredSize().height + 125));        
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(new WoWSessionJPanel(session), BorderLayout.PAGE_END);
               
        setContentPane(mainPanel);
        pack();
    }

	@Override
	public void SetTitle(String title) 
	{
		setTitle("WoW Tool " + title);
	}
}

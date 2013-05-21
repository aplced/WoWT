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
	JPanel mainPanel;
	WoWTreeJPanel treePanel;
	WoWSessionJPanel sessionInfoPanel;
	
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
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        WoWSessionSerializable session = CreateNewSession();
        
        treePanel = new WoWTreeJPanel();
        treePanel.LoadSession(session);
        
        BtnPanel btnsPanel = new BtnPanel(this);
        
        mainPanel.add(btnsPanel, BorderLayout.PAGE_START);
        
    	JScrollPane scrollPane = new JScrollPane(treePanel);
    	treePanel.setAutoscrolls(true);
    	scrollPane.setPreferredSize(new Dimension(treePanel.getPreferredSize().width + 125, treePanel.getPreferredSize().height + 125));        
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        sessionInfoPanel = new WoWSessionJPanel(session);
        mainPanel.add(sessionInfoPanel, BorderLayout.PAGE_END);
               
        setContentPane(mainPanel);
        pack();
    }
    
    @Override
    public WoWSessionSerializable CreateNewSession()
    {
    	WoWSessionSerializable session = new WoWSessionSerializable();
        WoWSessionInfoSerializable sessionInfo = WoWSessionInfoSerializable.LoadFromFile(WoWFileHelper.WoWDefaultSessionInfo);
        if(sessionInfo != null)
        {
        	session.setSessionInfo(sessionInfo);
        }
        session.CreateFromWoWTree(WoWFileHelper.WoWDefaultTree);
        
        return session;
    }
    
    @Override
    public void SetSession(WoWSessionSerializable session, String title)
    {
    	treePanel.LoadSession(session);
		setTitle("WoW Tool " + title);
		mainPanel.revalidate();
    }
    
    @Override
    public void SaveSession(String filePath)
    {
    	treePanel.SaveSessionToFile(filePath);
    	setTitle("WoW Tool " + filePath);
    }
    
    @Override
    public void SaveNotes(String filePath)
    {
    	treePanel.SaveNotesToFile(filePath);
    }
    
    @Override
    public WoWTreeJPanel GetWoWTreePanel()
    {
    	return treePanel;
    }
}

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;


@SuppressWarnings("serial")
public class MainWoWWindow extends JFrame 
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
        
        JPanel allPanes = new JPanel();
        allPanes.setLayout(new FlowLayout());
        ArrayList<CWoWJPanel> allItems = new ArrayList<CWoWJPanel>();
        
        for(int i = 0; i < 10; i++)
        {
	        CSerWoWNode testSer = new CSerWoWNode();
	        testSer.setUniqueID("Test_ID_" + i);
	        testSer.setDisplayName("Test WoW item");
	        testSer.setDescription("Some bogust description");
	        testSer.setDoneState(false);
	        
	        allItems.add(new CWoWJPanel(testSer));
        }
        
        for(CWoWJPanel wowItem : allItems)
        {
        	wowItem.LinkNodes(allItems);
        }
        
        for(CWoWJPanel wowItem : allItems)
        {
        	allPanes.add(wowItem);
        }
        
        setContentPane(allPanes);
        pack();
    }
    
}

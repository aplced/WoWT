import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
        
        setTitle("WoW Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        CWoWTreePanel treePanel = new CWoWTreePanel();
        treePanel.LoadPanelFromFile("WoW/WoWTree.xml");
        CBtnPanel btnsPanel = new CBtnPanel(treePanel, mainPanel);
        
        mainPanel.add(btnsPanel, BorderLayout.PAGE_START);
        
    	JScrollPane scrollPane = new JScrollPane(treePanel);
    	treePanel.setAutoscrolls(true);
    	scrollPane.setPreferredSize(new Dimension( treePanel.getPreferredSize().width + 125, treePanel.getPreferredSize().height + 125));        
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        pack();
    }
    
}

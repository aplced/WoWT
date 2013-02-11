import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class CreateWoWItemFrame extends JFrame implements ActionListener
{
	JButton apply;
	JButton cancel;
	
	JLabel uniqueIdInfo;
	JLabel displayNameInfo;
	JLabel descriptionInfo;
	
	JTextField uniqueIdInput;
	JTextField displayNameInput;
	JTextArea descriptionInput;
	
	private JPanel CreateUniqueIDInputPanel()
	{
        JPanel uniqueIDPnl = new JPanel();
        uniqueIDPnl.setLayout(new FlowLayout());
        
		uniqueIdInfo = new JLabel("The unique identifier of this WoW item - used in WoW tree definition");
		uniqueIdInput = new JTextField(20);
		
		uniqueIDPnl.add(uniqueIdInfo);
		uniqueIDPnl.add(uniqueIdInput);
		
		return uniqueIDPnl;
	}
	
	private JPanel CreateDisplayNameInputPanel()
	{
        JPanel displayNamePnl = new JPanel();
        displayNamePnl.setLayout(new FlowLayout());
		
		displayNameInfo = new JLabel("Display name of the WoW item - visible in the main view");
		displayNameInput = new JTextField(20);
		
		displayNamePnl.add(displayNameInfo);
		displayNamePnl.add(displayNameInput);
		
		return displayNamePnl;
	}
	
	private JPanel CreateDescriptionInputPanel()
	{
        JPanel descriptionPnl = new JPanel();
        descriptionPnl.setLayout(new FlowLayout());
		
		descriptionInfo = new JLabel("Description visible in the main view");
		descriptionInput = new JTextArea(2,40);
		
		descriptionPnl.add(descriptionInfo);
		descriptionPnl.add(descriptionInput);
		
		return descriptionPnl;
	}
	
	private JPanel CreateInputPanelsGrid()
	{
        JPanel inputPnl = new JPanel();
        inputPnl.setLayout(new GridLayout(3,2));
		
		inputPnl.add(CreateUniqueIDInputPanel());
		inputPnl.add(CreateDisplayNameInputPanel());
		inputPnl.add(CreateDescriptionInputPanel());
		
		return inputPnl;
	}
	
	private JPanel CreateControlBtnPanel()
	{
        JPanel controlPnl = new JPanel();
        controlPnl.setLayout(new FlowLayout());
        
		apply = new JButton("Apply");
		apply.addActionListener(this);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
	    
		controlPnl.add(apply);
		controlPnl.add(cancel);
		
		return controlPnl;
	}
	
	private JPanel CreateMainNewWoWItemPanel()
	{
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
		mainPanel.add(CreateControlBtnPanel(), BorderLayout.PAGE_END);
		mainPanel.add(CreateInputPanelsGrid(), BorderLayout.CENTER);
		
		return mainPanel;
	}
	
	CreateWoWItemFrame()
	{
	    setTitle("New WoW item");
	    setLocationRelativeTo(null);
	    setContentPane(CreateMainNewWoWItemPanel());
	    pack();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == apply)
		{
			String uId = uniqueIdInput.getText();
			String dispName = displayNameInput.getText();
			
			if(!uId.isEmpty())
			{
				uId = uId.trim();
			}
			if(uId.isEmpty())
			{
				JOptionPane.showMessageDialog(this, "Unique id cannot be an empty string!");
				return;
			}
			
			if(uId.contains(" ") ||  uId.contains(",") || uId.contains("\\") || uId.contains("?") || 
					uId.contains("/") || uId.contains("+") || uId.contains("*") || uId.contains("%") || 
					uId.contains("!") || uId.contains("@") || uId.contains("#") || uId.contains("$") ||
					uId.contains("^") || uId.contains("&") || uId.contains("(") || uId.contains(")"))
			{
				JOptionPane.showMessageDialog(this, "Unique id cannot contain space or any of the following symbols:\n , \\ ? / + * % ! @ # $ ^ & ( )");
				return;
			}
			
			if(!dispName.isEmpty())
			{
				dispName = dispName.trim();
			}
			if(dispName.isEmpty())
			{
				JOptionPane.showMessageDialog(this, "Display name cannot be an empty string!");
				return;
			}
			
			WoWSerialableNode serNode = new WoWSerialableNode();
			serNode.setUniqueID(uId);
			serNode.setDisplayName(dispName);
			serNode.setDescription(descriptionInput.getText());
			serNode.SaveWoWItemToFile("WoW/WoWItems/");
						
			setVisible(false);
			dispose();
		}
		else if (e.getSource() == cancel) 
		{
			setVisible(false);
			dispose();
		}		
	}
}

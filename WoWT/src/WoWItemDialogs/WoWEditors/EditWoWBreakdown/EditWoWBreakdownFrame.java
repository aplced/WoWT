package WoWItemDialogs.WoWEditors.EditWoWBreakdown;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import WoWItemDialogs.UserPrompt;
import WoWItemDialogs.WoWEditors.WoWEditorFrame;
import WoWItemDialogs.WoWEditors.EditWoWItem.IWoWItemEditDoneAction;
import WoWItemDialogs.WoWValueInput.WoWValueInput;
import WoWPanelUI.WoWItemJPanels.WoWItemJPanel;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSerializableNode;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionInfoSerializable;


@SuppressWarnings("serial")
public class EditWoWBreakdownFrame extends WoWEditorFrame implements ActionListener, IWoWItemEditDoneAction
{
	JButton email;
	JButton cancel;
	
	WoWSessionInfoSerializable sessionInfo;
	
	ArrayList<WoWValueInput> breakdown = new ArrayList<WoWValueInput>();
	
    private void CreateWoWBreakdownItems(WoWItemJPanel wowItem, int treeDepth)
    {   	
    	for(WoWItemJPanel child : wowItem.GetChildren())
    	{
    		if(child.TreeDepth == treeDepth)
    		{
    			breakdown.add(WoWValueInput.WoWEditableCheckboxInput(child));
    			CreateWoWBreakdownItems(child, treeDepth + 1);
    		}
    	}
    }
	
	private JPanel CreateInputPanelsGrid()
	{
		int row = 0;
        JPanel inputPnl = new JPanel();
        inputPnl.setLayout(new GridBagLayout());
        
        for(WoWValueInput item : breakdown)
        {
        	item.AddInputToPanel(inputPnl, 0, row++);
        }
        
		return inputPnl;
	}
	
	private JPanel CreateControlBtnPanel()
	{
        JPanel controlPnl = new JPanel();
        controlPnl.setLayout(new FlowLayout());
        
		email = new JButton("e-mail");
		email.addActionListener(this);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
	    
		controlPnl.add(email);
		controlPnl.add(cancel);
		
		return controlPnl;
	}
	
	private JPanel CreateMainNewWoWItemPanel()
	{
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
		mainPanel.add(CreateControlBtnPanel(), BorderLayout.PAGE_END);
		
		JPanel inputPnl = CreateInputPanelsGrid();
    	JScrollPane scrollPane = new JScrollPane(inputPnl);
    	inputPnl.setAutoscrolls(true);
    	scrollPane.setPreferredSize(new Dimension( inputPnl.getPreferredSize().width + 125, inputPnl.getPreferredSize().height + 125));        
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		
		return mainPanel;
	}
	
	public EditWoWBreakdownFrame(WoWItemJPanel wowItem, WoWSessionInfoSerializable iSessionInfo)
	{
		sessionInfo = iSessionInfo;
		
	    setTitle("Work breakdown");
	    setLocationRelativeTo(null);
	    CreateWoWBreakdownItems(wowItem, 1);
	    setContentPane(CreateMainNewWoWItemPanel());
	    pack();
	}
	
	private String PrepareEmailBody()
	{
		Float totalDuration = 0F;
		StringBuilder body = new StringBuilder();
		
		for(WoWValueInput item : breakdown)
        {
			String itemVal = item.GetInputValue(); 
			if(itemVal != null)
			{
				body.append("\t--" + itemVal + "\n");
				
				String[] durationSplit = itemVal.split(" - days: ");
				if(durationSplit.length > 1)
				{
					totalDuration += Float.parseFloat(durationSplit[1]);
				}
			}
        }
		
		return sessionInfo.getTaskName() + " -- " + sessionInfo.getTaskId() + " -- Total duration: " + totalDuration +"\n" + body.toString();
	}
	
	UserPrompt emailAuth;
	
    private class WoWMailailAuth extends Authenticator
    {
  	  String userName;
  	  String password;
  	  
  	  public WoWMailailAuth(String userName, String password)
  	  {
  		  super();
  		  
  		  this.userName = userName;
  		  this.password = password;
  	  }
  	  
  	  protected PasswordAuthentication getPasswordAuthentication()
  	  {
  		  return new PasswordAuthentication(userName, password);
  	  }
    };
	
	private void GetEMailUserAndPass(String user)
	{
		String userName = sessionInfo.getDeveloperName().toLowerCase().replace(" ", ".") + "@strypes.eu";
	      
		emailAuth = new UserPrompt("E-mail authentication");
		emailAuth.addWoWItemEditListener(this);
		emailAuth.AddPromptTextField("User name", userName);
		emailAuth.AddPromptPasswordField("Password", "");
		
		emailAuth.DisplayPrompt();
	}
	
	@Override
	public void EditDone(WoWSerializableNode serNode)
	{
		String userName = emailAuth.GetPrompt(0);
		String password = emailAuth.GetPrompt(1);
		
		SendWorkBreakdownMail(PrepareEmailBody(), userName, password);
	}
	
	private void SendWorkBreakdownMail(String messageBody, String userName, String password)
	{
	      String[] to = sessionInfo.getScrumMasters().split("\n");
	      
	      final String from = sessionInfo.getDeveloperName().toLowerCase().replace(" ", ".") + "@strypes.eu";
	      
	      Properties properties = System.getProperties();

	      properties.setProperty("mail.smtp.starttls.enable", "true");
	      properties.setProperty("mail.smtp.host", "smtp.gmail.com");
	      properties.setProperty("mail.smtp.port", "587");
	      properties.setProperty("mail.smtp.auth", "true");
	      
	      WoWMailailAuth auth = new WoWMailailAuth(userName, password);
	      
	      Session session = Session.getInstance(properties, auth);

	      try
	      {
	         MimeMessage message = new MimeMessage(session);

	         message.setFrom(new InternetAddress(from));

	         if(to.length == 1)
	         {
	        	 message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[0]));
	         }
	         else
	         {
	        	 Address[] addresses = new Address[to.length];
	        	 for(int i = 0; i < to.length; i++)
	        	 {
	        		 addresses[i] = new InternetAddress(to[i]);
	        	 }
	        	 message.addRecipients(Message.RecipientType.TO,  addresses);
	         }

	         message.setSubject("Workbreakdown " + sessionInfo.getTaskName() + " - " + sessionInfo.getTaskId());

	         message.setText(messageBody);

	         Transport.send(message);
	      }
	      catch (MessagingException mex)
	      {
	         mex.printStackTrace();
	      }
	      
	      ClearAndClose();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == email)
		{
			GetEMailUserAndPass(sessionInfo.getDeveloperName().toLowerCase().replace(" ", ".") + "@strypes.eu");
		}
		else if (e.getSource() == cancel) 
		{
			ClearAndClose();
		}		
	}
	
	private void ClearAndClose()
	{
		ClearListeners();
		setVisible(false);
		dispose();
	}
}

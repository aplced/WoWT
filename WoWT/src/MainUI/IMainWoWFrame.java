package MainUI;

import WoWPanelUI.WoWTreeJPanel;
import WoWSerialization.WoWSerializationObjects.Implementation.WoWSessionSerializable;

public interface IMainWoWFrame 
{
	public WoWSessionSerializable CreateNewSession();
	public void SetSession(WoWSessionSerializable session, String title);
	public void SaveSession(String filePath);
	public void SaveNotes(String filePath);
	public WoWTreeJPanel GetWoWTreePanel();
}

package net.miarma.contaminus.database.entities;

import net.miarma.contaminus.common.Table;

@Table("groups")
@SuppressWarnings("unused")
public class Group {
	private int groupId;
	private String groupName;
	
	public Group() {}
	
	public Group(int groupId, String groupName) {
		this.groupId = groupId;
		this.groupName = groupName;
	}
}

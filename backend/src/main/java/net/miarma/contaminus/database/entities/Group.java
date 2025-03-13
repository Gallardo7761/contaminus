package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;

@Table("groups")
public class Group {

    private int groupId;
    private String groupName;

    public Group() {}
    
    public Group(Row row) {
        this.groupId = row.getInteger("groupId");
        this.groupName = row.getString("groupName");
    }

	public Group(int groupId, String groupName) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(groupId, groupName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		return groupId == other.groupId
				&& Objects.equals(groupName, other.groupName);
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", groupName=" + groupName + "]";
	}

    
}
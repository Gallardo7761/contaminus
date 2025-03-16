package net.miarma.contaminus;

import net.miarma.contaminus.database.QueryBuilder;
import net.miarma.contaminus.database.entities.DeviceSensorValue;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String query = QueryBuilder
				.select(DeviceSensorValue.class)
				.build();
		System.out.println(query);
	}

}

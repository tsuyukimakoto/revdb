package net.everes.revdb.model;

import java.util.List;

public class Table extends DBModel {
	private final int modelType = DBModel.MODE_TABLE;
	
	public int getModelType() {
		return modelType;
	}
}

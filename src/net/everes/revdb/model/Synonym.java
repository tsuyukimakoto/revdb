package net.everes.revdb.model;

import java.util.List;

public class Synonym extends DBModel {
	private final int modelType = DBModel.MODE_SYNONYM;
	
	public int getModelType() {
		return modelType;
	}
}

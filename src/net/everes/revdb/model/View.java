package net.everes.revdb.model;

import java.util.List;

public class View extends DBModel {
	private final int modelType = DBModel.MODE_VIEW;
	
	public int getModelType() {
		return modelType;
	}
}

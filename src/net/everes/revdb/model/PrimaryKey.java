package net.everes.revdb.model;

import java.util.List;
import java.util.ArrayList;

public class PrimaryKey {
	List<Column> pk;
	public PrimaryKey() {
		this.pk = new ArrayList<Column>();
	}

	public void addColumn(Column clm) {
		pk.add(clm);
	}
	
	public void setColumns(List<Column> clms) {
		pk = clms;
	}
	
	public List<Column> getColumns() {
		return this.pk;
	}
	
}

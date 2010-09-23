package net.everes.revdb.model;

import java.util.ArrayList;
import java.util.List;

import net.everes.revdb.exceptions.NotSupportedException;

public abstract class DBModel {
	public static final int MODE_TABLE = 1;
	public static final int MODE_VIEW = 2;
	public static final int MODE_SYNONYM = 3;
	
	List<Column> columns;
	PrimaryKey pk;
	
	String catalog;
	String schema;
	String tableName;
	
	public DBModel() {
		columns = new ArrayList<Column>();
		pk = new PrimaryKey();
	}

	public abstract int getModelType();
	
	public static String getRacoodaName(String str, boolean capital) {
		String name = "";
		boolean bar = capital;
		for(int i = 0;i < str.length();i++) {
			String charcter = str.substring(i, i+1);
			if(charcter.equals("_")) {
				bar = true;
			} else if(bar == true) {
				name += charcter.toUpperCase();
				bar = false;
			} else {
				name += charcter;
			}
		}
		return name;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void addCollumn(Column column) {
		columns.add(column);
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public PrimaryKey getPrimaryKey() {
		return pk;
	}

	public void setPrimaryKey(PrimaryKey pk) {
		this.pk = pk;
	}

	public void addPrimaryKeyColumn(Column column) {
		pk.addColumn(column);
	}

	public boolean hasPrimaryKey() {
		return (pk != null && pk.getColumns().size() > 0);
	}
	
	public boolean isCompositPrimaryKey() {
		return (pk != null && pk.getColumns().size() > 1);
	}
	
	public List<Column> getColumnsExceptPK() {
		if(hasPrimaryKey()) {
			List<Column> normalList = new ArrayList<Column>();
			List<Column> pkList = pk.getColumns();
			for(Column c: columns) {
				if(!pkList.contains(c)) {
					normalList.add(c);
				}
			}
			return normalList;
		} else {
			return columns;
		}
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getClassName() {
		return DBModel.getRacoodaName(this.tableName.toLowerCase(), true);
	}
	
	public String toStringValue() throws NotSupportedException {
		String modelStructure = "";
		if(getModelType() == MODE_TABLE) modelStructure += "TABLE:" + getTableName() + " (" + getClassName() + ")\n";
		else if(getModelType() == MODE_VIEW) modelStructure += "VIEW :" + getTableName() + " (" + getClassName() + ")\n";
		else if(getModelType() == MODE_SYNONYM) modelStructure += "SYNON:" + getTableName() + " (" + getClassName() + ")\n";
		
		for(Column clm: pk.getColumns()) {
			modelStructure += "    PK:" + clm.getColumnName() + "\n";
		}
		for(Column clm: columns) {
			modelStructure += "    COLUMN:" + clm.getColumnName() + " (" + clm.getJavaType() + "," + clm.getJavaName() + ")" + "\n";
		}
		return modelStructure;
	}
}

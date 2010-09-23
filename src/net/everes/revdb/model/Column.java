package net.everes.revdb.model;

import java.sql.Array;
import java.sql.Types;

import net.everes.revdb.exceptions.NotSupportedException;

public class Column {
	private String columnName;

	private short type;
	int size;
	int decimalDigits;

	public Column(String columnName, short type, int size) {
		this.columnName = columnName;
		this.type = type;
		this.size = size;
		this.decimalDigits = 0;
	}
	
	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}
	
	public boolean hasDecimalDigits() {
		return (this.decimalDigits > 0);
	}
	
	
	public String getColumnName() {
		return this.columnName;
	}
	
	public String getJavaName() {
		return DBModel.getRacoodaName(this.columnName.toLowerCase(), false);
	}

	public String getJavaMethodName() {
		return DBModel.getRacoodaName(this.columnName.toLowerCase(), true);
	}

	public String getAsType() throws NotSupportedException {
		switch (this.type) {
			case java.sql.Types.CHAR:
			case java.sql.Types.VARCHAR:
			case java.sql.Types.LONGVARCHAR:
				return "String";
			case java.sql.Types.DATE:
			case java.sql.Types.TIMESTAMP:
				return "Date";
			case java.sql.Types.DECIMAL:
			case java.sql.Types.INTEGER:
			case java.sql.Types.SMALLINT:
			case java.sql.Types.TINYINT:
			case java.sql.Types.BIGINT:
			case java.sql.Types.FLOAT:
			case java.sql.Types.DOUBLE:
				if(size == 1) return "Boolean";
				return "Number";
			case java.sql.Types.BOOLEAN:
			case java.sql.Types.BIT:
				return "boolean";
			case java.sql.Types.OTHER:
				return "Object";
			default:
				throw new NotSupportedException("column:" + columnName + ", type:" + type);
		}
	}

	public String getJavaType() throws NotSupportedException {
		switch (this.type) {
			case java.sql.Types.CHAR:
			case java.sql.Types.VARCHAR:
			case java.sql.Types.LONGVARCHAR:
				return "String";
			case java.sql.Types.DATE:
			case java.sql.Types.TIMESTAMP:
				return "Date";
			case java.sql.Types.DECIMAL:
				if(hasDecimalDigits()) return "double";
			case java.sql.Types.INTEGER:
			case java.sql.Types.SMALLINT:
			case java.sql.Types.TINYINT:
				if(size == 1) return "boolean";
				return "int";
			case java.sql.Types.BIGINT:
				return "long";
			case java.sql.Types.FLOAT:
				return "float";
			case java.sql.Types.DOUBLE:
				return "double";
			case java.sql.Types.BOOLEAN:
			case java.sql.Types.BIT:
				return "boolean";
			case java.sql.Types.OTHER:
				return "Object";
			default:
				throw new NotSupportedException("column:" + columnName + ", type:" + type);
		}
	}

	public String getHbmType() throws NotSupportedException {
		switch (this.type) {
			case java.sql.Types.CHAR:
			case java.sql.Types.VARCHAR:
			case java.sql.Types.LONGVARCHAR:
				return "java.lang.String";
			case java.sql.Types.DATE:
			case java.sql.Types.TIMESTAMP:
				return "java.util.Date";
			case java.sql.Types.DECIMAL:
				if(hasDecimalDigits()) return "double";
			case java.sql.Types.INTEGER:
			case java.sql.Types.SMALLINT:
			case java.sql.Types.TINYINT:
				if(size == 1) return "boolean";
				return "int";
			case java.sql.Types.BIGINT:
				return "long";
			case java.sql.Types.FLOAT:
				return "float";
			case java.sql.Types.DOUBLE:
				return "double";
			case java.sql.Types.BOOLEAN:
			case java.sql.Types.BIT:
				return "boolean";
			case java.sql.Types.OTHER:
				return "java.lang.Object";
			default:
				throw new NotSupportedException("column:" + columnName + ", type:" + type);
		}
	}
}

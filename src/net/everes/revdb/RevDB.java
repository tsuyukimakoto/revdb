package net.everes.revdb;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import net.everes.revdb.exceptions.NotSupportedException;
import net.everes.revdb.model.Column;
import net.everes.revdb.model.DBModel;
import net.everes.revdb.model.Synonym;
import net.everes.revdb.model.Table;
import net.everes.revdb.model.View;

public class RevDB {
	private static Log log = LogFactory.getLog(RevDB.class);
	private String user;
	private String password;
	private String url;
	private String database;
	
	public RevDB(String user, String password, String url, String database) {
		this.user = user;
		this.password = password;
		this.url = url;
		this.database = database;
	}
	
	public List<DBModel> getTableList(String schema) throws SQLException, NotSupportedException, ClassNotFoundException {
		Connection con = null;
		try {
			con = getConnection();
			return getTableList(schema, con);
		} finally {
			if(con != null) {
				try { con.close(); } catch (Exception e) {}
			}
		}
	}
	
	private Connection getConnection() throws NotSupportedException, ClassNotFoundException, SQLException {
		log.debug("database is " + database);
		if(database.toLowerCase().equals("oracle")) {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} else if(database.toLowerCase().equals("postgres")) {
			Class.forName("org.postgresql.Driver");
		} else if(database.toLowerCase().equals("mysql")) {
			Class.forName("org.gjt.mm.mysql.Driver");
		} else {
			throw new NotSupportedException("database(" + database + ") is not supported.");
		}
		return DriverManager.getConnection(url, user, password);
	}

	private List<DBModel> getTableList(String schema, Connection con) throws SQLException{
		ResultSet rs = null;
		List<DBModel> tableList = new ArrayList<DBModel>();
		DatabaseMetaData metaData = null;
		try {
			metaData = con.getMetaData();
			rs = metaData.getTables(null, schema, null, null);
			while(rs.next()) {
				DBModel model = null;
				String type = rs.getString("TABLE_TYPE");
				if(type.equals("TABLE")) {
					model = new Table();
				} else if(type.equals("VIEW")) {
					model = new View();
				}
				if(model != null) {
					tableList.add(model);
					model.setCatalog(rs.getString("TABLE_CAT"));
					model.setSchema(rs.getString("TABLE_SCHEM"));
					model.setTableName(rs.getString("TABLE_NAME"));
					log.info("FIND " + type + "(" + model.getCatalog() + "," + model.getSchema() + "," + model.getTableName() + ")");
				}
			}
			
		} finally {
			rs.close();
		}
		for(DBModel model: tableList) {
			setColumn(model, metaData, con);
			setPrimaryKey(model, metaData, con);
		}
		return tableList;
	}
	
	protected void setColumn(DBModel table, DatabaseMetaData metadata, Connection con) throws SQLException {
		ResultSet rs = null;
		try {
            rs = metadata.getColumns(table.getCatalog(), table.getSchema(), table.getTableName(), null);
			while(rs.next()) {
				Column column = new Column(rs.getString("COLUMN_NAME"), rs.getShort("DATA_TYPE"), rs.getInt("COLUMN_SIZE"));
				column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
				table.addCollumn(column);
			}
		} finally {
			if(rs != null) {
				rs.close();
			}
		}
	}

	
	protected void setPrimaryKey(DBModel table, DatabaseMetaData metadata, Connection con) throws SQLException {
		ResultSet rs = null;
		try {
			rs = metadata.getPrimaryKeys(table.getCatalog(), table.getSchema(), table.getTableName());
			while(rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				List<Column> columnList = table.getColumns();
				for(Column clm: columnList) {
					if(columnName.equals(clm.getColumnName())) {
						table.addPrimaryKeyColumn(clm);
					}
				}
			}
		} finally {
			rs.close();
		}
	}
}

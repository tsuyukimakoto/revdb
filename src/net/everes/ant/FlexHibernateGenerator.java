package net.everes.ant;

import java.util.List;

import net.everes.revdb.ModelWriter;
import net.everes.revdb.RevDB;
import net.everes.revdb.model.DBModel;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class FlexHibernateGenerator extends Task {
	/**
	 * dataabse: oracle,postgres,mysql is permitted
	 */
	private String database;
	/**
	 * user: database user
	 */
	private String user;
	/**
	 * password: database password
	 */
	private String password;
	/**
	 * url: database uri
	 */
	private String url;

	/**
	 * work: work directory. each source is output this folder.
	 */
	private String work;

	/**
	 * modelPackage: java POJO package
	 */
	private String modelPackage;
	/**
	 * asPackage: Action Script model package
	 */
	private String asPackage;
	/**
	 * hbmPackage: hibernate package
	 */
	private String hbmPackage;
	/**
	 * schema: database schema(,separated)
	 */
	private String schema;

    public void execute() throws BuildException {
		try {
			RevDB revDb = new RevDB(getUser(), getPassword(), getUrl(), getDatabase());
			String[] schemas = getSchemaList();
			for(String scm: schemas) {
				if(!scm.trim().equals("")) {
					List<DBModel> result;
						result = revDb.getTableList(scm);
					ModelWriter writer = new ModelWriter(getWork());
					for(DBModel model: result) {
						writer.writeJavaClass(getModelPackage(), getHbmPackage(), model);
						writer.writeAsClass(getAsPackage(), getModelPackage(), model);
						writer.writeHbmClass(getHbmPackage(), model);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			new BuildException("Error", e, getLocation());
		}
    }
    
    public String[] getSchemaList() {
    	return getSchema().split(",");
    }

	public String getAsPackage() {
		return asPackage;
	}

	public void setAsPackage(String asPackage) {
		this.asPackage = asPackage;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getHbmPackage() {
		return hbmPackage;
	}

	public void setHbmPackage(String hbmPackage) {
		this.hbmPackage = hbmPackage;
	}

	public String getModelPackage() {
		return modelPackage;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    
}

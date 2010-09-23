package net.everes.revdb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

import net.everes.revdb.model.DBModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class ModelWriter {
	private static Log log = LogFactory.getLog(ModelWriter.class);
	private static final String templateFileJava = "template/class.vm";
	private static final String templateFileAs = "template/as.vm";
	private static final String templateFileHbm = "template/hbm.vm";
	static {
		Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		p.setProperty("input.encoding", "UTF-8");
		try {
			Velocity.init(p);
		} catch (Exception e) {
        		e.printStackTrace();
		}
	}
	
	String baseDir = "";
	
	public ModelWriter(String baseDir) {
		this.baseDir = baseDir;
	}
	
	public void writeJavaClass(String packageName,String hbmPackage, DBModel model) throws ResourceNotFoundException, ParseErrorException, Exception {
		File directory = new File(baseDir
				+ System.getProperty("file.separator")
				+ getDirectoryFromPackage(packageName)
				);
		if(!directory.exists()) {
			if(!directory.mkdirs()) throw new Exception("can't create src directory");
		}
		File outputFile = new File(baseDir
								+ System.getProperty("file.separator")
								+ getDirectoryFromPackage(packageName)
								+ System.getProperty("file.separator")
								+ model.getClassName() + "Bean.java");
//		if(!outputFile.exists()) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(outputFile, false)); 
				VelocityContext ctx = new VelocityContext();
				ctx.put("packageName", packageName);
				ctx.put("hbmPackage", hbmPackage);
				ctx.put("table", model);
				ctx.put("pkColumns", model.getPrimaryKey().getColumns());
				ctx.put("normalColumns", model.getColumnsExceptPK());
				Template template = Velocity.getTemplate(ModelWriter.templateFileJava);
				template.merge(ctx, bw);
				bw.flush();
				log.info("Generate Class("+ model.getClassName() +"Bean.java) file.");
			} finally {
				if(bw != null) bw.close();
			}
//		} else {
//			log.info("Class("+ model.getClassName() +"Bean.java) file is already exist.");
//		}
	}
	
	public void writeAsClass(String asPackage, String packageName, DBModel model) throws ResourceNotFoundException, ParseErrorException, Exception {
		File directory = new File(baseDir
				+ System.getProperty("file.separator")
				+ getDirectoryFromPackage(asPackage)
				);
		if(!directory.exists()) {
			if(!directory.mkdirs()) throw new Exception("can't create src directory");
		}
		File outputFile = new File(baseDir
								+ System.getProperty("file.separator")
								+ getDirectoryFromPackage(asPackage)
								+ System.getProperty("file.separator")
								+ model.getClassName() + "Bean.as");
//		if(!outputFile.exists()) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(outputFile, false)); 
				VelocityContext ctx = new VelocityContext();
				ctx.put("asPackage", asPackage);
				ctx.put("packageName", packageName);
				ctx.put("table", model);
				Template template = Velocity.getTemplate(ModelWriter.templateFileAs);
				template.merge(ctx, bw);
				bw.flush();
				log.info("Generate Class("+ model.getClassName() +"Bean.as) file.");
			} finally {
				if(bw != null) bw.close();
			}
//		} else {
//			log.info("Class("+ model.getClassName() +"Bean.java) file is already exist.");
//		}
	}

	public void writeHbmClass(String hbmPackage, DBModel model) throws ResourceNotFoundException, ParseErrorException, Exception {
		String hbmDirectory = "hbm";
		File directory = new File(baseDir
				+ System.getProperty("file.separator")
				+ hbmDirectory
				);
		if(!directory.exists()) {
			if(!directory.mkdirs()) throw new Exception("can't create src directory");
		}
		File outputFile = new File(baseDir
								+ System.getProperty("file.separator")
								+ hbmDirectory
								+ System.getProperty("file.separator")
								+ model.getClassName() + ".hbm.xml");
//		if(!outputFile.exists()) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(outputFile, false)); 
				VelocityContext ctx = new VelocityContext();
				ctx.put("hbmPackage", hbmPackage);
				ctx.put("table", model);
				ctx.put("pkColumns", model.getPrimaryKey().getColumns());
				ctx.put("normalColumns", model.getColumnsExceptPK());
				Template template = Velocity.getTemplate(ModelWriter.templateFileHbm);
				template.merge(ctx, bw);
				bw.flush();
				log.info("Generate Class("+ model.getClassName() +".hbm.xml) file.");
			} finally {
				if(bw != null) bw.close();
			}
//		} else {
//			log.info("Class("+ model.getClassName() +"Bean.java) file is already exist.");
//		}
	}
	

	private String getDirectoryFromPackage(String packageName) {
		return packageName.replace(".", System.getProperty("file.separator"));
	}
	
}

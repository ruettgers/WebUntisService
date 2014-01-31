package de.weg.WebUntis.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Declaration of a set of application property keys. All property-keys must
 * exactly match those defined in the ISP.properties file. Avoid using
 * corresponding strings in code. Use the tokens defined here instead. This
 * guarantees single source of change and compile time checking. But be aware it
 * doesn't guarantee the existence of the corresponding key-string in the
 * application property-file.
 */
@SuppressWarnings("serial")
public class WUProperties extends Properties {

	private static final String WU_PROPERTIES_FILE = "/WU.properties";
	public static final String Default_FileDirImport = "FileDirImport";
	public static final String Default_FileDirExport = "FileDirExport";
	public static final String ImportFileName = "ImportFileName";
	public static final String ExportFileName = "ExportFileName";
	public static final String MappingArt = "MappingArt";
//	public static final String TokenMappingFile = "TokenMappingFile";
	public static final String PasswordFileNameName = "PasswordFileName";

	// singleton instance
	/**
	 * @uml.property name="me"
	 * @uml.associationEnd
	 */
	private static WUProperties me;

	private WUProperties() {
	}

	public static WUProperties getInstance() {
		if (me == null) {
			loadProperties();
		}
		return me;
	}

	/**
	 * 
	 */
	private static void loadProperties() {
		me = new WUProperties();
		try {
			me.load(Class.class.getResourceAsStream(WU_PROPERTIES_FILE));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void storeToXML() throws IOException {
		OutputStream os = new java.io.FileOutputStream(new File(
				"WUXMLProperties.properties"));
		me.storeToXML(os, "WU-Properties");
	}

	/**
	 * Erzeuge eine Liste mit filenamen paaren bestehend aus (importname,exportname)
	 * @return 
	 */
	public static List<Properties> getPropertyList() {
		
		List<Properties> propertyList = new ArrayList<Properties>();
		Properties p;
		p = new Properties();

		String filedir = ".";
		String filedirim = filedir+"/"+WUProperties.getInstance().getProperty(WUProperties.Default_FileDirImport);
		p.put(WUProperties.ImportFileName, filedirim+"/"+WUProperties.getInstance().getProperty(WUProperties.ImportFileName));

		String filedirex = filedir+"/"+WUProperties.getInstance().getProperty(WUProperties.Default_FileDirExport);
		p.put(WUProperties.ExportFileName, filedirex+"/"+WUProperties.getInstance().getProperty(WUProperties.ExportFileName));
		
//		String mappingFile = "/"+WUProperties.getInstance().getProperty(WUProperties.TokenMappingFile);
//		p.put(WUProperties.TokenMappingFile, mappingFile);

		String mappingArt = WUProperties.getInstance().getProperty(WUProperties.MappingArt);
		p.put(WUProperties.MappingArt, mappingArt);

		
		propertyList.add(p);
		
		return propertyList;
	}

	
	
}

package gov.nasa.gsfc.dqss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Group;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.*;

/**
 * 
 * @author rstrub
 * 
 */
public  class DQSS {
	
	protected long rows = -1;
	protected long cols = -1;
	protected long layers = -1;
	protected String dataVariable = null;
	public static String ontology = "http://mirador.gsfc.nasa.gov/ontologies/dqss-Instances.owl";
	public static String navigation = "http://mirador.gsfc.nasa.gov/ontologies/DQSSNavigation.xml";
	protected static int  debugLevel=0;
	public static   String stagingDirectory = "/var/tmp/staging";
	public static String NoScreeningString;
	public static String MaskName;
	public static String OrigName;
	public static String RejectName;
	public static String delimiter;
	public static String Q2String0;
	public static String Q2String1;
	public static String Q2String2;
	public static String Q2String3;
	public static String Q1String0;
	public static String Q1String1;
	public static String Q1String2;
	public static String Q1String3;
	public static String prefixesForLocalQuery;
	public static Boolean UseExpressionCaching = false;
	public static Boolean UsePropForDescribeExpr = true;
	public static String OnboardServletConfig;
	public static String OnboardPropertiesFile;
	
	/*
	 * We need NoOntologyFile to signal that if UseServlet=false
	 * and we already have an ontology file, don't run DataQuality.setup();
	 */
	public static boolean NoOntologyFileProvided = false; 
	public static String OntologyCriteriaURL;
	public static boolean UseServlet = true;
	public static boolean UseCache = true;
	public static boolean PassThru = true;
	public static String CacheDir;
	public static float FillValue = -7777;
	public static int SkipBecauseNoScreeningSelected;
	public static String OntFile;
	public static boolean UseReflection = true;
	protected String dir;
	protected String config;
	static Logger logger = Logger.getLogger(DQSS.class.getName());

	public DQSS() {
		  /*
		   * The layer number in the SDS nBestStd is the lowest altitude 
		   * layer for which the quality is Best.
		   * if datalayer >= qualitypixel[i] set data[i] = 1, else fillvalue  
		   */
		 
	}
	public DQSS(String dir, String configPath) {
		if (dir == null) {
			/*
			 * OntologyGetting... so we need to set:
			 */
			NoOntologyFileProvided = true;
		}
		else {
		stagingDirectory = dir;
        SetStagingDirectory(dir);
		}
		ReadConfigFile(configPath);
		
		
        
	}
	
	  
	public static void SetStagingDirectory(String dir)
	{
		stagingDirectory = dir;
	}
	
	 
	 
	

	   public static void ReportError(String Message, int level) {
			if (debugLevel >= 0 && level == 0) {
				System.err.println(Message);
				DQSS.fileAppend("Serious:" + Message);
				logger.error(Message);
			}
			if (debugLevel >= 1 && level == 1) {
				//System.err.println(Message);
				DQSS.fileAppend("Warn:" + Message);
				logger.warn(Message);
			}
			if (debugLevel >= 2 && level == 2) {
				//System.out.println(Message);
				DQSS.fileAppend("Debug:" + Message);
				logger.debug(Message);
			}
			if (debugLevel >= 3 && level == 3) {
				// This is old stuff which prints without EOL char
				// that I don't need right now.
				logger.info(Message);
			}
		}
	   
	   protected static boolean fileAppend(String message) {
			 boolean status = false;
			 String stagingDir = stagingDirectory;
			    try{
			        // Create file 
			    	status = new File(stagingDir).mkdirs(); 
			    	if (!status) {
			    		File logdir = new File (stagingDir);
			    		if (!logdir.exists()) {
			    		   logger.error("Could not make " + stagingDir + " directory");
			    		   return false;
			    		}
			    	}
			    	File log = new File(stagingDir + File.separator + "masker.log");
			    	
			    	if (!log.createNewFile()) {
			    		if (!log.exists()) {
			    			logger.error("Could not create newfile: " + log.getPath());
			    			return false;
			    		}
			    	}
			    	
			        FileWriter fstream = new FileWriter(log,true);
			            BufferedWriter out = new BufferedWriter(fstream);
			        out.write(message + "\n");
			        //Close the output stream
			        out.close();
			        status = true;
			        }catch (Exception e){//Catch exception if any
			          logger.error( e.getMessage());
			        }
			        return status;
		 }
	   protected static boolean fileAppend(String path,String filename, String message) {
			 boolean status = false;
			 String stagingDir = path;
			    try{
			        // Create file 
			    	status = new File(stagingDir).mkdirs(); 
			    	if (!status) {
			    		File logdir = new File (stagingDir);
			    		if (!logdir.exists()) {
			    		   logger.error("Could not make " + stagingDir + " directory");
			    		   return false;
			    		}
			    	}
			    	File log = new File(stagingDir + File.separator + filename);
			    	
			    	if (!log.createNewFile()) {
			    		if (!log.exists()) {
			    			logger.error("Could not create newfile: " + log.getPath());
			    			return false;
			    		}
			    	}
			    	
			        FileWriter fstream = new FileWriter(log,true);
			            BufferedWriter out = new BufferedWriter(fstream);
			        out.write(message + "\n");
			        //Close the output stream
			        out.close();
			        status = true;
			        }catch (Exception e){//Catch exception if any
			          logger.error( e.getMessage());
			        }
			        return status;
		 }
	   protected static boolean fileWriteFile2BCached(String path,String filename, String message) {
           boolean status = false;
           String stagingDir = path;
              try{
                  // Create file 
                  status = new File(stagingDir).mkdirs();
                  if (!status) {
                          File logdir = new File (stagingDir);
                          if (!logdir.exists()) {
                             logger.error("Could not make " + stagingDir + " directory");
                             return false;
                          }
                  }
                  File cache = new File(stagingDir + File.separator + filename);
                  if (cache.exists()) {
                          logger.debug(cache.getName() + " exists already so not writing");
                          return true;
                  }
                  if (!cache.createNewFile()) {
                          if (!cache.exists()) {
                                  logger.error("Could not create newfile: " + cache.getPath());
                                  return false;
                          }
                  }

                  FileWriter fstream = new FileWriter(cache,false);
                      BufferedWriter out = new BufferedWriter(fstream);
                  out.write(message);
                  //Close the output stream
                  out.close();
                  status = true;
                  }catch (Exception e){//Catch exception if any
                    logger.error( e.getMessage());
                  }
                  return status;
   }

	   protected static boolean fileOverWrite(String filename, String message) {
			 boolean status = false;
			 File file = new File(filename);
			 
			 
			    try{
			        // Create file 
			    	String stagingDir = file.getParent();
			    	status = new File(stagingDir).mkdirs(); 
			    	if (!status) {
			    		File logdir = new File (stagingDir);
			    		if (!logdir.exists()) {
			    		   logger.error("Could not make " + stagingDir + " directory");
			    		   return false;
			    		}
			    	}
			    	
			    	if (!file.createNewFile()) {
			    		if (!file.exists()) {
			    			logger.error("Could not create newfile: " + file.getPath());
			    			return false;
			    		}
			    	}
			    	
			        FileWriter fstream = new FileWriter(file,false);
			            BufferedWriter out = new BufferedWriter(fstream);
			        out.write(message);
			        //Close the output stream
			        out.close();
			        status = true;
			        }catch (Exception e){//Catch exception if any
			          logger.error( e.getMessage());
			        }
			        return status;
		 }
	   public void ReadConfigFile(String configdir)
	   {
		  //System.err.println(configdir);
		  PropertyConfigurator.configure(configdir + "/dqsslog.conf");
	      String config = configdir +  "/dqss.config";
	      Properties p = new Properties();
	      File configFile = null;
	      String item = "";
	      try{
	        configFile = new File(config);
	        FileInputStream f = new FileInputStream(configFile);
	        p.load(f);
	        
	        //System.err.println("Reading:" + configFile);
	    	
	    	  item = "ONTOLOGY";
		      DQSS.ontology = p.getProperty("ONTOLOGY");
		      logger.warn("ONTOLOGY:" + ontology.trim());
		      
		      item = "NAVIGATION";
			      this.navigation = p.getProperty("NAVIGATION");
		      logger.warn("NAVIGATION:" + navigation.trim());
		      
		      item = "NOSCREEN";
		      DQSS.NoScreeningString = p.getProperty("NOSCREEN");
		      logger.warn("NOSCREEN:" + NoScreeningString.trim());
		      
		      item = "MASKNAME";
		      DQSS.MaskName = p.getProperty("MASKNAME");
		      logger.warn("MASKNAME:" + MaskName.trim());
		      
		      item = "ORIGNAME";
			    OrigName = p.getProperty("ORIGNAME");
		      logger.warn("ORIGNAME:" + OrigName.trim());
		      
		      item = "REJECTNAME";
			    RejectName = p.getProperty("REJECTNAME");
		      logger.warn("REJECTNAME:" + RejectName.trim());
		      
		      item = "ONTOLOGYURL";
		      DQSS.OntologyCriteriaURL = p.getProperty("ONTOLOGYURL");
		      logger.warn("ONTOLOGYURL:" + OntologyCriteriaURL.trim());
	
		      item = "USECACHE";
		      DQSS.UseCache = new Boolean(p.getProperty("USECACHE"));
		      logger.warn("USECACHE:" + UseCache);
		      
		      item = "USESERVLET";
		      DQSS.UseServlet = new Boolean(p.getProperty("USESERVLET"));
		      logger.warn("USESERVLET:" + UseServlet);
		      
		      item = "PASSTHRU";
		      DQSS.PassThru = new Boolean(p.getProperty("PASSTHRU"));
		      logger.warn("PASSTHRU:" + PassThru);
		      
		      item = "CACHEDIR";
		      DQSS.CacheDir = p.getProperty("CACHEDIR");
		      logger.warn("CACHEDIR:" + CacheDir.trim());
		      
		      item = "USEREFLECTION";
		      DQSS.UseReflection = new Boolean(p.getProperty("USEREFLECTION"));
		      logger.warn("USEREFLECTION:" + UseReflection);
		      
		      item = "EXPRESSIONCACHING";
		      DQSS.UseExpressionCaching = new Boolean(p.getProperty("EXPRESSIONCACHING"));
		      logger.warn("EXPRESSIONCACHING:" + UseExpressionCaching);

		      item = "USEPROPFORDESCRIBEEXPR";
		      DQSS.UsePropForDescribeExpr = new Boolean(p.getProperty("USEPROPFORDESCRIBEEXPR"));
		      logger.warn("USEPROPFORDESCRIBEEXPR:" + UsePropForDescribeExpr);

		      item = "DELIMITER";
		      DQSS.delimiter = p.getProperty("DELIMITER");
		      logger.warn("DELIMITER:" + delimiter.trim());
		      
		      item = "Q1STRING0";
		      DQSS.Q1String0 = p.getProperty("Q1STRING0");
		      logger.warn("Q1STRING0:" + Q1String0.trim());
		      item = "Q1STRING1";
		      DQSS.Q1String1 = p.getProperty("Q1STRING1");
		      logger.warn("Q1STRING1:" + Q1String1.trim());
		      item = "Q1STRING2";
		      DQSS.Q1String2 = p.getProperty("Q1STRING2");
		      logger.warn("Q1STRING2:" + Q1String2.trim());
		      item = "Q1STRING3";
		      DQSS.Q1String3 = p.getProperty("Q1STRING3");
		      logger.warn("Q1STRING3:" + Q1String3.trim());
		      
		      item = "Q2STRING0";
		      DQSS.Q2String0 = p.getProperty("Q2STRING0");
		      logger.warn("Q2STRING0:" + Q2String0.trim());
		      item = "Q2STRING1";
		      DQSS.Q2String1 = p.getProperty("Q2STRING1");
		      logger.warn("Q2STRING1:" + Q2String1.trim());
		      item = "Q2STRING2";
		      DQSS.Q2String2 = p.getProperty("Q2STRING2");
		      logger.warn("Q2STRING2:" + Q2String2.trim());
		      item = "Q2STRING3";
		      DQSS.Q2String3 = p.getProperty("Q2STRING3");
		      logger.warn("Q2STRING3:" + Q2String3.trim());
              
		      DQSS.prefixesForLocalQuery = p.getProperty("PREFIX");
		      logger.warn("PREFIX:" + prefixesForLocalQuery.trim());

		      item = "ONBOARDCONFIG";
		      DQSS.OnboardServletConfig = p.getProperty("ONBOARDCONFIG");
		      logger.warn("ONBOARDCONFIG:" + OnboardServletConfig.trim());

		      item = "ONBOARDPROPERTIES";
		      DQSS.OnboardPropertiesFile = p.getProperty("ONBOARDPROPERTIES");
		      logger.warn("ONBOARDPROPERTIES:" + OnboardPropertiesFile.trim());

	      }
	      catch(Exception e){
	    	  logger.error("Error loading config file: " + configFile + 
	    			  " perhaps " + item + " is missing from config" );
	    	  System.err.println("Error loading config file: " + configFile +
	    			  " perhaps " + item + " is missing from config" );
	    	  
		      System.exit(1);
	      }

	      
	   }
	   public  static float getFillValue() {
			return FillValue;
		}
	   
		public static org.w3c.dom.Document loadXMLFrom(String xml) throws org.xml.sax.SAXException, java.io.IOException {
			return loadXMLFrom(new java.io.ByteArrayInputStream(xml.getBytes()));
	    }

	public static org.w3c.dom.Document loadXMLFrom(java.io.InputStream is) throws org.xml.sax.SAXException, java.io.IOException {
	    javax.xml.parsers.DocumentBuilderFactory factory =
	        javax.xml.parsers.DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    javax.xml.parsers.DocumentBuilder builder = null;
	    try {
	        builder = factory.newDocumentBuilder();
	    }
	    catch (javax.xml.parsers.ParserConfigurationException ex) {
	    }  
	    org.w3c.dom.Document doc = builder.parse(is);
	    //Node node = doc.getFirstChild();
	    //NodeList nodes = node.getChildNodes();
	    //String d = node.toString();
	    is.close();
	    return doc;
	}
	
	public static Document createDomDocument() {
	    try {
	        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document doc = builder.newDocument();
	        return doc;
	    } catch (ParserConfigurationException e) {
	    }
	    return null;
	}

	public static Document addNewElement(Document doc, String tagname) {
	   
	    	 Element element = doc.createElement(tagname);
			 doc.appendChild(element);
			 return doc;
	   
	 
	}
	public static String nodeToString(Node node) {
		
	  String string;
	  Source source = new DOMSource(node);
      StringWriter stringWriter = new StringWriter();
      Result result = new StreamResult(stringWriter);
      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer;
	try {
		transformer = factory.newTransformer();
		 transformer.transform(source, result);
		 transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		 transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		 
	} catch (TransformerConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	
	} catch (TransformerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	/*
	OutputFormat format = new OutputFormat(node);
    format.setLineWidth(150);
    format.setIndenting(true);
    format.setIndent(2);
    format.setOmitXMLDeclaration(true);
    Writer out = new StringWriter();
    XMLSerializer serializer = new XMLSerializer(out, format);
    serializer.serialize(doc);

    valueMap = out.toString();
*/
      string = stringWriter.getBuffer().toString();
      return string;
	}
	/**
	 * 
	 * @param d data variable object
	 * @param dimIndices contains the indices in order which we want to read them. [0,1,2] would mean the original
	 * order. [1,2,0] would mean that the depth variable for some reason was first and so we need to reorder how
	 * we read the dimensions 
	 * @param depth is the indices that we want to be our z variable.
	 *   <p>
	 *   This is where the magic is done. where we make Dataset.read() read 
   	 * a vertical slice, say, instead of just the first layer.  That is
   	 * read perhaps (*,*,0) instead of (0,*,*) Or put another way, if 
   	 * datafield = 7 x 203 x 135 (This is clearly "incorrectly" organized in the sense
   	 * that the 7 layers will be shown in hdfview as rows) and your
   	 * qualityfield is (203 x 135 x 5)
   	 * <p> So far this only works for 3 dimensions.
	 * @throws Exception 
	 * @throws OutOfMemoryError 
	 */
	public static void reorganizeDimensions(Dataset d, int[] dimIndices,int depth) throws OutOfMemoryError, Exception {
		
	
		long[] selected = d.getSelectedDims();
	
			
			 int rank = d.getRank(); // number of dimension of the dataset
			 long[] dims = d.getDims(); // the dimension sizes of the dataset
			 //long[] selected = d.getSelectedDims(); // the selected size of the data set
			 long[] start = d.getStartDims(); // the off set of the selection
			 long[] stride = d.getStride(); // the stride of the dataset
			 int[] selectedIndex = d.getSelectedIndex(); // the selected dimensions for display

			 /*
			  * 1. Set up dim indices as is basic to this re-org:
			  * 2. Make sure selected contains full sizes.
			  */
			 for (int i=0; i < selected.length; ++i) {
				 selectedIndex[i] = dimIndices[i];
				 selected[i] = dims[i]; // this does nothing
			 }
			 if (depth >= 0 ) { // Not sure why I want to do this but I do.
				 selected[depth] = 1;
			 }

			 
			 if (d.read() == null) {
					logger.error("d.read() failed AFTER reorganizeDimensions " + d.getName());
					
			 }
			
			// reset the selection arrays...remember rank is number of dimensions, not size of last dimension
		
			 for (int i = 0; i < d.getRank(); i++) {
			     start[i] = 0;
			 }
			 /* 
			 * One of these MUST be one because multiplied together gives you the size of the array
			 * And if you want your array to be one layer, not matter which way you are reading it,UsePropForDescribeExpr
			 * then one must be one.
			 */
	}
	/**
	 * 
	 * @param d  screeningvariable or datavariable
	 * @param qtype datatype held in a string.
	 * @param slice no longer using this 
	 * @return DQSSNumber - the class which holds numbers for any datatype. (Stores them as doubles
	 * and returns them with specified type)
	 */
	public static DQSSNumber populateWithType(Dataset d, String qtype, 
			boolean slice, long layerindex) {
		DQSSNumber qdata = null;
		
		if (slice) {
			
			 long[] selected = d.getSelectedDims();
			 int rank = d.getRank(); // number of dimension of the dataset
			 long[] dims = d.getDims(); // the dimension sizes of the dataset
			 //long[] selected = d.getSelectedDims(); // the selected size of the data set
			 long[] start = d.getStartDims(); // the off set of the selection
			 long[] stride = d.getStride(); // the stride of the dataset
			 int[] selectedIndex = d.getSelectedIndex(); // the selected dimensions for display

			 selectedIndex[0] = 1;
			 selectedIndex[1] = 2;
			 selectedIndex[2] = 0;
			
			// reset the selection arrays...remember rank is number of dimensions, not size of last dimension
			 for (int i = 0; i < d.getRank(); i++) {
			     start[i] = 0;
			     selected[i] = 1;
			     stride[i] = 1;
			 }
			 
			 // set stride to 2 on dim1 and dim2 so that every other data points are selected.
			 stride[1] = 1;
			 stride[2] = 1;
			 
			 // set the selection size of dim1 and dim2
			 // this might be selected[0] and [1]
			 selected[1] = dims[1] / stride[1];
			 selected[2] = dims[2] / stride[2];

		}
		
		try {
		long[] start = d.getStartDims();
		if (start.length > 2) {
			start[2] = layerindex; // sometimes the flag is in the second layer.
		}
		if (d.read() == null) {
			logger.error("reorganizeDimensions were done incorrectly for " + d.getName());
			return null;
		}
		if (qtype.contains("INT8")) {
    		qdata = new DQSSNumber( (byte[]) d.read());
    	}
		else if (qtype.contains("INT16")) {
    		qdata = new DQSSNumber( (short[]) d.read());
    	}
		else if (qtype.contains("INT32")) {
    		qdata = new DQSSNumber( (int[]) d.read());
    	}
		else if (qtype.contains("FLOAT32")) {
    		qdata = new DQSSNumber( (float[]) d.read());
    	}
		else {
			logger.error("No handler yet for datatype:" + qtype );
		}
		}
		 catch (Exception e) {
			 logger.error("Something happened in populateWithType");
		 }
		 if (qdata != null) {
			 qdata.setType(qtype);
		 }
		 else {
			 logger.error("Type could not be found for " + d.getName());
		 }
	   	 return qdata;
	}

}

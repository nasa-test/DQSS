package gov.nasa.gsfc.dqss;

import gov.nasa.gsfc.G4DataObject;
import gov.nasa.gsfc.G4DataObjectManager;
import gov.nasa.gsfc.dqss.castor.expression.Root;
import gov.nasa.gsfc.giovanni.DataFetcher;
import gov.nasa.gsfc.ontology.ConfigurationReader;
import gov.nasa.gsfc.ontology.OntologyModel;
import gov.nasa.gsfc.ontology.QueryOntology;
import gov.nasa.gsfc.ontology.XPathGenerator;
import gov.nasa.gsfc.webservice.SparqlResult;
import gov.nasa.gsfc.webservice.OnboardInterface;
import gov.nasa.gsfc.webservice.MODISResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;





public class DataQuality   {

	Document doc = null;
	int criteria = 0;
	String prefix = null;
	ConfigurationReader cr = null;
	OntologyModel om = null;
	QueryOntology qo = null;
	//String XmlNav = "http://mirador-ts2.gsfc.nasa.gov/DQSSNavigation.xml";
	String parameter;
	String Dataset;
	String Criteria;
	String queryString = null;
	String results;
	private Root rootCastor;
	static Logger logger = Logger.getLogger(DataQuality.class.getName());
	G4DataObjectManager  cacheMgr;
	DataFetcher fetchMgr;
	public DataQuality(){}
	OnboardInterface ws;
	boolean onboardSetupFlag = false;
	
	/*
	 * Don't need this with ontology servlet.
	 */
	public void setup(String ontology, String navigation) 
	{
		  if (!DQSS.NoOntologyFileProvided) {
			  logger.debug("already have ontology file");
			  return;
		  }
		  logger.debug("Not using servlet:" + ontology);
		  logger.debug("Not using servlet:" + navigation);
		  
		  om = new OntologyModel();
	      om.setOntologyUrls(ontology);
	      //15771 contains setOntologyUrls
	      //10238125 contains setMiradorUrl

	     
	      
	      
	      om.readOntology();
	      qo = new QueryOntology();
	      qo.setModel(om);
	      

	     
	      prefix= DQSS.prefixesForLocalQuery;
	      

	      cr = ConfigurationReader.getInstance();
	      try {
			doc = cr.getDocument(navigation);
			logger.debug(doc.toString());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
		
		public void OnboardExpressionServletSetup(String snvid) {
		

	
		FileInputStream f = null;
		try {
			f = new FileInputStream(DQSS.OnboardPropertiesFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (snvid.contains("MOD") || snvid.contains("MYD") ) {
			//ws = new MODISResult();
			ws = new SparqlResult();
		}
		else if (snvid.contains("ML")) {
			ws = new SparqlResult();
		}
		else if (snvid.contains("AIR")) {
			
		}
		else {
			logger.error("Onboard ontology referencing cannot handle dataset:" + snvid);
			
		}
		onboardSetupFlag = ws.setInitParameters(DQSS.OnboardServletConfig, 
			       f, DQSS.UsePropForDescribeExpr, DQSS.OnboardPropertiesFile, logger);
		/* To be called for each SA.
		String parameter = "Corrected_Optical_Depth_Land";
		String[] criteria = {"QualityAssuranceLand_AOTCF47=GOOD"};
		String reload = null;
		ws.doGet(parameter,criteria, JustDatasetName, reload, configFile, f, flag);
		*/
	}
	public String getCriteria()
	{
		return Criteria;
	}
	
	public void doQuery() throws TransformerException
	{
		String xmlString;
		 qo.setQueryStatement(prefix,queryString);
		 logger.debug("prefix:" + prefix);
		 logger.debug("queryString:" + queryString);
         results = qo.getQueryResult();
         
         results = results.replaceAll("\\^\\^http://www.w3.org/2001/XMLSchema#string", "");
         results = results.replaceAll("\\^\\^http://www.w3.org/2001/XMLSchema#integer", "");
         results = results.replaceAll("\\^\\^http://www.w3.org/2001/XMLSchema#nonNegativeInteger", "");
         String printing = results.replaceAll("><", ">\n<");
         logger.debug(printing);
         
	}
	
	public String ModisStub(String parameter, List<String> criteria) 
		 {

		  HashMap<String,String> crit = new HashMap<String,String>();  
		  crit.put("VeryGood","3");
		  crit.put("Good","2");
		  crit.put("Marginal","1");
		  crit.put("NoConfidence","0");
		  crit.put("DefinitelyCloudy","0");
		  crit.put("ProbablyCloudy","1");
		  crit.put("ProbablyClear","2");
		  crit.put("DefinitelyClear","3");

		  String stub = "<datavar>" + 
		     "<queryOutput>" +
		        "<QualityView>DATAVARIABLEView</QualityView>" +
		                "<ScreeningVariable>QUALITYVARIABLE</ScreeningVariable>" +
		                "<ScreenVarType>QualityLevelVariable</ScreenVarType>" +
		                "<dim>StdPressureLev</dim><min>11</min><max>28</max>" +
		                "<direction>positive</direction>" +
		                "<Level>QNIRLEVEL</Level>" +
		                "<constraint_relationship></constraint_relationship>" +
		      "</queryOutput>" +
		       "CLOUDMASK" + 
		     "</datavar>";

		  String cloudmask = 
		     "<queryOutput>" +
		        "<QualityView>Water_Vapor_Near_InfraredView</QualityView>" +
		                "<ScreeningVariable>Cloud_Mask_QA</ScreeningVariable>" +
		                "<ScreenVarType>QualityLevelVariable</ScreenVarType>" +
		                "<dim>StdPressureLev</dim><min>11</min><max>28</max>" +
		                "<direction>positive</direction>" +
		                "<Level>CLOUDLEVEL</Level>" +
		                "<constraint_relationship></constraint_relationship>" +
		      "</queryOutput>" ;

		  if (crit.containsKey(criteria.get(0))) {
			  stub = stub.replace("QNIRLEVEL",crit.get(criteria.get(0)));
		  }
		  else {
			  logger.error(criteria.get(0) +  " is not a valid criteria for " + "MOD05_L2/" + parameter  );
			  stub = stub.replace("QNIRLEVEL", criteria.get(0) + " is not a valid criteria for " + "MOD05_L2/" + parameter);
		  }
		  if (parameter.contains("Water_Vapor_Near_Infrared") ) {
		     stub = stub.replace("DATAVARIABLE",parameter);
		     stub = stub.replace("QUALITYVARIABLE","Quality_Assurance_Near_Infrared");
		     if (criteria.size() > 1) {
		     cloudmask = cloudmask.replace("CLOUDLEVEL",crit.get(criteria.get(1)));
		     stub = stub.replace("CLOUDMASK",cloudmask);
		     } 
		     else {
		    	 cloudmask = cloudmask.replace("CLOUDLEVEL","EXPECTING SECOND CRITERIA LEVEL for datavariable:" + parameter);
		         stub = stub.replace("CLOUDMASK",cloudmask);
		     }
		  }
		  else if (parameter.contains("Water_Vapor_Infrared")) {
		     stub = stub.replace("DATAVARIABLE",parameter);
		     stub = stub.replace("QUALITYVARIABLE","Quality_Assurance_Infrared");
		     stub = stub.replace("CLOUDMASK","");
		  }
		   return stub; 
		  
		
		
	}
	 public String prettyXml(String xml) {
	    	
	    	OutputFormat format = OutputFormat.createPrettyPrint();
	    	XMLWriter writer=null;
			try {
				 StringWriter buffer = new StringWriter();

				writer = new XMLWriter(buffer, format );
				writer.write( xml );
		    	writer = new XMLWriter(buffer, format );
		    	writer.write( xml );
		    	return buffer.toString();

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 return null;
  }
	/* 
	 * This only works with earlier architecture where the ontology data is gotten
	 * as processing proceeds rather than with MODAPS Arch where all the ontology dat
	 * is retrieved up front.
	 public void parseAirsOntologyResults(AIRSDataFile file) {
		  
			
			
		    QueryOutput[] qo = getQueryOutput();
		    file.nCoordCons=0;
		    file.nQualLevel=0;
		    file.nConsRef=0;
		    file.nRef=0;
		    int nQSvars = getQueryOutputCount();
		    file.qs_names.clear();
		    file.ref_names.clear();
		    file.qs_types.clear();
		    file.qs_reftypes.clear();
		    file.mins.clear();
		    file.maxs.clear();
		    file.crit2num.clear();
		    for (int i = 0; i < nQSvars; ++i) {
		    	file.updateMember(file.qs_names,"ScreeningVariable Name",  qo[i].getScreeningVariable());
		        file.updateMember(file.ref_names,"ReferenceVariable Name",  qo[i].getReferenceVariable());
		    	file.updateMember(file.qs_types,"ScreeningVariable Type",     qo[i].getScreenVarType());
		    	file.updateMember(file.qs_reftypes,"ReferenceVariable Type",  qo[i].getRefVarType());
		    	file.updateMember(file.dims,"Dimension", qo[i].getDim());
		    	file.updateLongMember(file.mins, "Minimum DataSlice", qo[i].getMin());
		    	file.updateLongMember(file.maxs, "Max DataSlice", qo[i].getMax());
		    	file.updateLongMember(file.crit2num,"User's Criteria", qo[i].getLevel());
		    	logger.debug(":" + file.nConsRef + ":" + file.nRef + ":" + file.nCoordCons + ":" + file.nQualLevel);
		    	
		    }	
		    if (file.qs_reftypes.size() > 0) {
		    	file.qs_names.add(file.ref_names.get(0));
		    	file.qs_types.add(file.qs_reftypes.get(0));
		    }
	  }
	  
	  
	  public int XML2Castor() {
		
		int status = 0;
		// Create a new Unmarshaller
		InputSource is = new InputSource();
		logger.debug("|" + results + "|");
        is.setCharacterStream(new StringReader(results.trim()));
        
        
		try {
		    rootCastor =
		    (Root) Unmarshaller.unmarshal(Root.class, is);
		} catch (MarshalException e) {
		    // TODO Auto-generated catch block
		    //e.printStackTrace();
		    logger.error(parameter + ":Failed to parse:" + results);
		} catch (ValidationException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		if (rootCastor == null) {
			logger.error("XML could not be unmarshaled");
			status = 1;
			return status ;
		}
		logger.debug("QO count:" + rootCastor.getQueryOutputCount());
		logger.debug("Validity:" + rootCastor.isValid());
		QueryOutput[] qoCastor = rootCastor.getQueryOutput();
		
		Enumeration<? extends QueryOutput> items = rootCastor.enumerateQueryOutput();
		for (; items.hasMoreElements()  ;)  {
		    QueryOutput it = items.nextElement();
		    
		    logger.debug("CASTOR:" + it.getQualityView());
		    logger.debug("CASTOR:" + it.getScreeningVariable());
		   

		}

		for (int i = 0; i < qoCastor.length; ++i) {
			String qv = qoCastor[i].getQualityView();
			String s  = qoCastor[i].getScreeningVariable();
			logger.debug("Castor output:" + qv + " " + s );
		}
		return status;
		
	}
	public QueryOutput[] getQueryOutput()
	{
		return rootCastor.getQueryOutput();
	}
	
	public int getQueryOutputCount() {
		return rootCastor.getQueryOutputCount(); 
	}
	
	*/
	public void getSparqlQuery(String dataset,String param, String crit) throws TransformerException, XPathExpressionException
	{
		XPathGenerator dqssXpath = new DqssXPath();
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr=null;

		 HashMap<String,Object> inputHash = new HashMap<String,Object>();
         inputHash.put("dataset", stripVersion(dataset));
         inputHash.put("datavariable",param);
         logger.debug(dataset + "," + param + "," + crit);
         /*
          * forms xpath query:
          */
         List<Object> xpathResult=dqssXpath.getXpath(doc,inputHash);
         
         if (xpathResult == null || xpathResult.isEmpty()) {
        	 logger.error("xpath result is empty ");
        	 return;
         }
         logger.debug("xpath result : " + xpathResult.get(0));
         
         Element newXpath = (Element) xpathResult.get(1);
         newXpath = (Element)newXpath.getParentNode();
		 logger.debug("parent tag name:" + newXpath.getTagName());
		 
		 if (newXpath != null) {	
			 Node queryNode = (Node) newXpath.getParentNode();
			 queryString  = queryNode.getFirstChild().getTextContent(); // This is where it gets the SPARQL query!!
		 }
		 else {
			 logger.debug("Could not find anything at " + newXpath.getTagName());
			 
		 }
		 List<String> propList = new ArrayList<String>();
		 propList.add(stripVersion(dataset));
		 propList.add(param);
         /*
          * This is for when we are using the MODIS ontology
          */
         handleMultipleCriteria(crit,propList);
         /*
          * At this point the sparql query we are trying to get is history anyway (with the new arch)
          */
         queryString = dqssXpath.interpolateSparqlQuery(queryString, propList);
         
         }
   public String stripVersion(String dataset) {
	 	   int dot = dataset.indexOf(".");
	 	   if (dot > 0) {
	 		   return dataset.substring(0,dot);
	 	   }
	 	   else {
	 		   return dataset;
	 	   }
	    }
	private void handleMultipleCriteria(String input, List<String>propList) {
		String output = "";
		if (!input.contains(DQSS.delimiter)) {
			propList.add(input);
		}
		String[] crits = input.split(DQSS.delimiter);
		
		if (!input.contains("=")) {
			for (int i = 0; i < crits.length; ++i) {
				propList.add(crits[i]);
			}
		}
		else {
			/*
			 * Handle ScreeningAssertion1=QualityLevel for Expression Ontology
			 */
			for (int i = 0; i < crits.length; ++i) {
				String[] pairs = crits[i].split("=");
				output +=  pairs[0] + "=" + pairs[1] + "&";
			}
		}

	}
	
	public String getQueryString()
	{
		return queryString;
	}
	
	public G4DataObject checkCache(String address) {
		
		cacheMgr = new G4DataObjectManager();
    	cacheMgr.setupCacheType("");
    	cacheMgr.setCacheKey(address);
    	
    	
        G4DataObject obj = cacheMgr.getCacheG4Data(cacheMgr.getCacheKey());
        return obj;
	}
	public String checkFetch(String address) {
		
		fetchMgr = new DataFetcher();
		fetchMgr.setRegion("DQSS");
		fetchMgr.setDebugLevel(Level.DEBUG_INT);
		fetchMgr.setMaxRetryCount(1);
		fetchMgr.setRetryInterval(5);
		fetchMgr.setTimeout(20);
		fetchMgr.setCacheOn("true");
    	//cacheMgr.setupCacheType("");
    	//cacheMgr.setCacheKey(address);
    	
		byte[] byteArray = fetchMgr.getData(address);
		if (byteArray == null) {
			return null;
		}
		String data = new String(byteArray);
        return data;
        
	}
	public void getOntologyResults(String address, boolean UseCache,
			String tmp, String snvid, String dataVariable, String criteria)  throws Exception{
		
		results = null;
		boolean cacheTheResults = false;
		this.Criteria = criteria;
		
        if (UseCache && tmp != null) {
        	//logger.debug("CACHE:" + obj.getKey());
        	//logger.debug("CACHE:" + obj.getCacheFile(obj.getKey()));
        	//byte[] retVal = cacheMgr.getBinFileCache(cacheMgr.getCacheKey());
        	try {
        		//String tmp = new String(retVal);
        		/*
        		 * fileAppend was putting something on the end of the string Castor doesn't like.
				 * so I don't really need this anymore.
        		 */
        		if (!tmp.endsWith(">")) {
        			results = tmp.substring(0,tmp.length()-1);
        		}
        		else {
        			results = tmp;
        		}
        		logger.debug("Results returned from cache:" + results);
        	}
        	catch (NullPointerException e){
        		logger.error("cacheMgr.getBinFileCache returned null");
        	}
        	
        }
        if (DQSS.UseServlet && results == null) {
        	/*
			 * In this case the address is built from
			 * DQSS.OntologyUrl
			 */
        	logger.debug("address:" + address);
        	URL url = new URL(address);
        	
        	try {
        		
		        logger.debug(address);
		        URLConnection conn = url.openConnection();
		     
		        BufferedReader in = new BufferedReader(
		                                new InputStreamReader(
		                                conn.getInputStream()));
		        String inputLine;
		        results = "";
		        while ((inputLine = in.readLine()) != null) 
		        {
		        	results += inputLine +  System.getProperty("line.separator");
		        	if (inputLine.contains("\"\"") && inputLine.contains("value")) {
		        		logger.error("Missing Value in " + inputLine);
		        	}
		        }
		        
		        in.close();
		        
		        logger.debug("Results returned from Servlet:" + results);
		        cacheTheResults = true;
        	}
        	/*
        	 * Not exiting in these cases because onboard ontology parser
        	 * will be invoked if this fails (DataQuality.results is null)
        	 */
            catch (UnknownHostException e) {
            	logger.error("Servlet " + address +  " does not exist");

            }
            catch (FileNotFoundException e ) {
            	logger.error("Ser:vlet address:" + address + " is incorrect");
 
            }
            catch (IOException e ) {
            	logger.error("Servlet parameters in " + address + " are incorrect");
 
            }
	        catch (Exception e) {
	        	e.printStackTrace();
	        	logger.error("Servlet called failed for: " + address);

	        	
	        
        	}
        	/*
        	 * Caching using the Servlet URL as key
        	 * Now done elsewhere.
        	 */
	        if (UseCache && cacheTheResults && results != null) {
	        	/*
		        String path = DQSS.CacheDir;
		        String keypath = url.getHost() + url.getQuery();
		        DQSS.fileWriteFile2BCached(path,keypath,results); // this is probably what is unnecessary
		        String key = cacheMgr.getCacheKey();
		        logger.debug("populating CACHE:" + key);
		        cacheMgr.setBinFileCache(path + File.separator +  keypath);
		        */
	        }
        }
        /*
         * If results from cache and Servlet are null
         * then use local Ontology object.
         */
        if (results == null ) {
        	
        	/*
    		 * Now using Venku's new MODAPS servlet.
    		 */
        	
        	if (DQSS.UseServlet) { // then setup has not been run
        		logger.warn("Nothing in cache and Servlet failed so using local ontology object");
        		OnboardExpressionServletSetup(snvid);
        	}
        	//String parameter = "Corrected_Optical_Depth_Land";
    		//String[] criteria = {"QualityAssuranceLand_AOTCF47=GOOD"};
        	String[] crits = handleMultipleCriteriaForOnboardServlet(criteria);
    		results = ws.doGet(dataVariable,crits,snvid, null /*reload*/, 
    				DQSS.OnboardServletConfig, onboardSetupFlag);
    		DQSS.fileOverWrite("output.xml", results);
    	    
    		if (results == null || results.length() < 1000) {
    			logger.error("Nothing returned from Ontology for " + dataVariable 
    					+ "/" + Criteria + "/" + snvid);
    			logger.error("results returned from ontology interface:" + results);
    			return;
    			
    		}
    		else {
    			
    			/*
            	 * Caching using the ontology and parameters as key
            	 */
    	        if (UseCache && cacheTheResults && 
    	        		results != null ) {
    		        String path = DQSS.CacheDir;
    		        URL url = null;
    				try {
    					/*
    					 * In this case the address ( an invalid url) is built from
    					 * DQSS.ontology
    					 */
    					url = new URL(address); 
    				} catch (MalformedURLException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    		        String keypath = url.getHost() + url.getQuery();
    		         
    		        DQSS.fileWriteFile2BCached(path,keypath,results);
    		        String cachekey = cacheMgr.getCacheKey();
    		        logger.debug("populating CACHE:" + address);
    		        cacheMgr.setBinFileCache(path + File.separator +  keypath);
    	        }
    		}
        }
 }
	
	private String[] handleMultipleCriteriaForOnboardServlet(String input) {
		String[] crits = input.split(DQSS.delimiter);
		
		
		return crits;
	}
}

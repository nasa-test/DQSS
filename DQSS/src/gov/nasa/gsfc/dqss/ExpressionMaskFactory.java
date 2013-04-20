package gov.nasa.gsfc.dqss;

import gov.nasa.gsfc.G4DataObject;
import gov.nasa.gsfc.giovanni.DataFetcher;
import gov.nasa.gsfc.Stopwatch;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.*;

import org.xml.sax.SAXException;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.Attribute;

public class ExpressionMaskFactory extends MaskFactory {

	Stopwatch s;
	DataQuality dataQuality;
	
	public ExpressionMaskFactory(String dir, String configPath) {
		super();
		dqss = new DQSS(dir,configPath);
		DQSS.SkipBecauseNoScreeningSelected = 7;
		/*
		 * This is where the DataQuality constructor should have been in the first
		 * place. Not in startMasking to reopen the ontology each time!
		 * We will always need a DataQuality instance but not necessarily one
		 * with it's own Ontology object.
		 */
		  dataQuality = new DataQuality();  
        /*
         * If we are not using the servlet for our ontology:
         */
        if (!DQSS.UseServlet) {
        	logger.warn("Calling dataQuality...Not expecting to use Servlet for Ontology");
        	dataQuality.setup(DQSS.ontology, DQSS.navigation);
        }
	}
	/*
	 * Queries for ontology stuff in various ways so this is not needed by
	 * Masker in MODAPS Arch. Just by the Web Services prep bit in MODAPS Arch
	 */ 
	 
	public void PrepMasking(DataFile file, String snvid, String dataVariable, 
			String criteria, DQSSNumber fillvalue){
		
		
		logger.warn("HDF File:" + file.getFileName());
        logger.warn("Dataset:" + snvid);
        logger.warn("DataVariable:" + dataVariable);
        logger.warn("Criteria:" + criteria);
        logger.warn("Staging Dir:" + DQSS.stagingDirectory);
        logger.warn("FillValue:" + fillvalue);
        
		  
	       //  Ontology Servlet Architecture:
	       
	    	boolean UseCache = DQSS.UseCache;
	    	boolean cacheTheResults = false;
	        String OntologyURL = DQSS.OntologyCriteriaURL + "?";
	        OntologyURL += "dataset=" + snvid + "&" +
	                       "parameter=" + dataVariable + "&" +
	                       "criteria=" + criteria;
	        
	        logger.debug("OntologyURL:" + OntologyURL);
	        G4DataObject obj=null;
            String data = null;
	        String address = OntologyURL;
	        if (UseCache) {
	        	 
	        	 if (DQSS.UseServlet) {
	        		 address = OntologyURL;
	        	 }
	        	 else {
	        		 address = DQSS.ontology + "?dataset=" + snvid + "&" +
	                 "parameter=" + dataVariable + "&" +
	                 "criteria=" + criteria;
	        	 }
	        	 logger.debug("USING CACHE");
	        	 
	        	  // Cache key is set here:
	        	  
	        	 //obj = dataQuality.checkCache(address);
                 data = dataQuality.checkFetch(address);
	        	 if (obj != null) {
	        		 logger.debug("We found cache key:" + obj.getKey());
	        	 }
	        }
	        else {
	        	logger.debug("NOT USING CACHE");
	        }
	       
	        
	        // This now encapsulates Servlet and local Jena algorithms.
	         
	    	try {
	    	 dataQuality.getOntologyResults(address,UseCache,data,snvid,dataVariable,criteria);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			s.stop();
		    logger.debug("Get Ontology Data : elapsed time: " + s.getElapsedTime());
		    s.reset();
		    s.start();
		   
		    /*
	        int status = dataQuality.XML2Castor();
	        if (status > 0) {
	           logger.error("XML Returned from Ontology does not match what castor was expecting for dataVariable:" + dataVariable);
	           return ;
	        }
	       */
	        
	        
	         //  Put ONTOLOGY DATA:
	         //  into file object
	         
	        //dataQuality.parseAirsOntologyResults((AIRSDataFile) file);
	        
		
	}
	
	
	public  Statistics StartMasking(AggregateExpression he,   DQSSNumber fillvalue){
		
		s = new Stopwatch();
		s.start();
	    
		
		
        if (NoScreening(he)) {
        	return null;
        }
        he.populateCriteria();
        dataVariable = he.getDataVariable();
        Group newg   = he.file.getGroupOfDataset(dataVariable);
        if (newg == null) {
        	logger.warn("getH4Attribute call failed. This can happen sometimes because not all MOD04" +
	 		" have the deep blue variables. So we need to skip it and move on to the next.");
        	return new Statistics(dataVariable);
        }
        if (he.file.DF != null) {
        	he.file.DF.destory();
        	he.file.DF = null;
        }
	    he.populateDataset(dataVariable);
	    if (false ) {
	    	String swathname = "_FV_" + dataVariable;
	    
	    	he.file.NavigateSwathGroup() ;
	    	Group swathg = he.file.getGroupOfDataset(swathname);
	    	if (swathg == null) {
	    			logger.error("Could not find group of swath:" + swathname);
	    	System.exit(3);
	    	}
	    	Dataset swath = he.file.copyDatasetWithSuffix(swathname,swathg, DQSS.MaskName);
	    }
	    String[] newdimnames = he.file.getDimensionNames(he.data.getName());
	    Dataset[] quality = he.populateQSVars_byScreeningField();
	    if (quality == null) { // here quality is just being used to determine if we found any or not.
	    	logger.fatal("Could not find any quality datasets for " + 
	    			dataVariable + " File " + he.file.getFileName() + " may not have been opened/exist");
	    	return null;
	    }
	    
	    FillValue = fillvalue;
	    DQSS.logger.debug("Using FillValue:" + fillvalue);
	    Mask mask = null;
	    if (quality != null) {
	    	logger.debug("I Found qs_var:" + quality[0].getName());
	    }
	    else {
	    	logger.error("I could not find a qs_var");
	    	return null;
	    }
	    if (he.data != null) {
	    	logger.debug("I Found dataVariable:" + he.data.getName());
	    }
	    else {
	    	logger.error("I could not find the dataVariable:" + dataVariable);
	    	return null;
	    }
	    s.stop();
	    logger.debug("Get Ontology Data postprocessing: elapsed time: " + s.getElapsedTime());
	    s.reset();
	    s.start();
	
		 try {
			 mask = new ExpressionMask(he, dir);
			 if (DQSS.UseReflection) {
			     mask.doMask_reflect();
			 }
			 else {
				 mask.doMask(); 
			 }
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				
		   		logger.error("prepQualityView found NullPointerException " + he.getDataVariable() );
				e.printStackTrace();
			} catch (ArrayIndexOutOfBoundsException e) {
				
				e.printStackTrace();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       
           
        s.stop();
        logger.debug(mask.getClass().getName() + ", " + he.data.getName() 
        		+ " masking elapsed time: " + s.getElapsedTime());
		return mask.getStats();
         
	}
	

	public  String LoadOntology(String snvid, String dataVariable, 
			
			String criteria){

		logger.warn("Dataset:" + snvid);
        logger.warn("DataVariable:" + dataVariable);
        logger.warn("Criteria:" + criteria);
        logger.warn("Staging Dir:" + DQSS.stagingDirectory);
        if (criteria.contains(DQSS.delimiter)) {
        	
        }
          /*
       *  Ontology Servlet Architecture:
       */
    	boolean UseCache = DQSS.UseCache;
    	boolean cacheTheResults = false;
        String OntologyURL = DQSS.OntologyCriteriaURL + "?";
        OntologyURL += "dataset=" + snvid + "&" +
                       "parameter=" + dataVariable + "&" +
                       handleMultipleCriteriaForURLs(criteria);
        
        logger.info("OntologyURL:" + OntologyURL);
        G4DataObject obj=null;
        String address = OntologyURL;
        String data =  null;
        if (UseCache) {
        	 
        	 if (DQSS.UseServlet) {
        		 address = OntologyURL;
        	 }
        	 else {
        		 address = DQSS.ontology + "?dataset=" + snvid + "&" +
                 "parameter=" + dataVariable + "&" +
                 "criteria=" + criteria;
        	 }
        	 
        	 /*
        	  * Cache key is set here:
        	  */
        	 //obj = dataQuality.checkCache(address);
        	  data = dataQuality.checkFetch(address);
        	 
        	 if (data != null) {
        		 logger.debug("USING CACHE");
        		 //logger.debug("We found cache key:" + obj.getKey());
        	 }
        	 else {
        		 logger.warn("Could not get data from cache");
        	 }
        }
        else {
        	logger.debug("NOT USING CACHE");
        }
       
        /*
         * Internal Ontology SPARQLing. This now encapsulates Servlet and local Jena algorithms.
         */
    	try {
    	 dataQuality.getOntologyResults(address,UseCache,data,snvid,dataVariable,criteria);
    	 //dataQuality.getOntologyResults(address,UseCache,obj,snvid,dataVariable,criteria);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//arrangexml();xml = xml.substring(xml.indexOf("<QualityView>"));
		try {
			Document doc = DQSS.loadXMLFrom(dataQuality.results);
			OutputFormat format = new OutputFormat(doc);
		    format.setLineWidth(150);
		    format.setIndenting(true);
		    format.setIndent(2);
		    format.setOmitXMLDeclaration(true);
		    Writer writer = new StringWriter();
		    XMLSerializer serializer = new XMLSerializer(writer, format);
		    serializer.serialize(doc);

		    String xmlString = writer.toString();
		    if (xmlString.length() > 0 ) {
		    	return xmlString;
		    }
			NodeList nodes = doc.getElementsByTagName("QualityView");
			

			if (nodes.getLength() < 1) {
				String errorstr = "We are now expecting each xml result to be bounded with <QualityView>." +
	        	" This was not found." + 
	        	" We are only adding a base tag <root> after xml is returned from all datavariables."  +
	        	" Perhaps old code is being referenced";
	        	logger.error(errorstr);
	        	System.err.println(errorstr);
	        	dataQuality.results = "<datavar>invalid results</datavar>";
	        	return null;
			}
			else if (nodes.getLength() == 1) {
				//return nodes.item(0);
			}
			else {
				logger.error("nodes has length of "  + nodes.getLength());
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return null;
	}
	
	private String handleMultipleCriteriaForURLs(String input) {
		String output = "";
		if (!input.contains(DQSS.delimiter)) {
			if (!input.contains("=")) {
				return "criteria=" + input; //pre Expr
			}
			else {
				return input; //Expr
			}
		}
		String[] crits = input.split(DQSS.delimiter);
		if (!input.contains("=")) {
			for (int i = 0; i < crits.length; ++i) {
				output += "criteria=" + crits[i] + "&";
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
	
		
		return output;
	}
    void arrangexml() {
    	String xml = dataQuality.results;
    	xml = xml.substring(xml.indexOf("<QualityView>"));
    	try {
			Document doc = DQSS.loadXMLFrom(xml);
			NodeList nodes = doc.getElementsByTagName("QualityView");
			if (nodes.getLength() < 1) {
				String errorstr = "We are now expecting each xml result to be bounded with <QualityView>." +
	        	" This was not found." + 
	        	" We are only adding a base tag <root> after xml is returned from all datavariables."  +
	        	" Perhaps old code is being referenced";
	        	logger.error(errorstr);
	        	System.err.println(errorstr);
	        	dataQuality.results = "<datavar>invalid results</datavar>";
			}
			else if (nodes.getLength() == 1){
	           dataQuality.results = DQSS.nodeToString(nodes.item(0));
			}
			else if (nodes.getLength() > 1) {
				logger.error("more that one node returned.");	
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        
    	
    }
    
	boolean NoScreening(AggregateExpression he) {
		
		if (he.NoScreening) {
		
			logger.warn("Skipping this parameter because no screening  was selected");
		
			return true;
		}
		return false;
	}
	
	

}


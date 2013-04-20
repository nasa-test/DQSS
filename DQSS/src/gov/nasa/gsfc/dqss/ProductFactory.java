package gov.nasa.gsfc.dqss;

import gov.nasa.gsfc.Stopwatch;
import gov.nasa.gsfc.dqss.castor.expression.QualityView;
import gov.nasa.gsfc.dqss.castor.expression.Root;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ncsa.hdf.hdflib.HDFException;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.h5.H5Datatype;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*
 * This might be overkill, I could have gone straight to AirsMaskFactory, 
 * but let's see how it pans out.
 */
public class ProductFactory {
	
	DataFile file;
	String skip;
	MaskFactory MF;
	List<String> params;
	List<String> criteria;
	List<Statistics> stats4metadata;
	List<DQSSNumber> FillValues = null;
	String snvid;
	String inputFile;
	/*
	 * If the MOD05_L2 NIR has all fills, hrepack fails with
	 * "Could not get chunking information for SDS"
	 * So we want to exit with a special status not to 
	 * run hrepack. 
	 */
	boolean AllFillsInData = false;
	static Logger logger = Logger.getLogger(ProductFactory.class.getName());
	
	/*
	 * Constructor for GES-DISC operating under MODAPS Arch
	 * Ontology data file has not been created yet
	 */
	public ProductFactory(List<String> params,
			List<String> criteria, 
			List<Statistics> stats,
			
			String StagingDirectory,
			String ConfigDir,String S4PA_DatasetVersionID, String inputFile ) {
		
		 this.snvid = S4PA_DatasetVersionID;
		 this.params = params;
		 this.criteria = criteria;
		
		this.stats4metadata = stats;
		this.inputFile = inputFile;
		 
		MF = new ExpressionMaskFactory(StagingDirectory,ConfigDir); 
		file = new ExpressionDataFile(inputFile,inputFile);
		file.FileType = "Refactored";
		/*
		if (DataFile.DetermineProduct(inputFile).equals("AIRS")) {
			 this.snvid = S4PA_DatasetVersionID;
			 this.stats4metadata = stats;
			 this.params = params;
			 this.criteria = criteria;
			 this.inputFile = inputFile;
			 
			 MF = new AirsMaskFactory(StagingDirectory,ConfigDir); 
			
			 // define logger first before doing this:
		
		     FillValues = SDPreProcessing(inputFile,params);
		     
			 file = new AIRSDataFile(inputFile,inputFile);
			 file.FileType = "AIRS";
			
		 }
		else if (DataFile.DetermineProduct(inputFile).equals("MODIS")) {
			 this.snvid = S4PA_DatasetVersionID;
			 this.stats4metadata = stats;
			 this.params = params;
			 this.criteria = criteria;
			 this.inputFile = inputFile;
			 

			 MF = new ModisMaskFactory(StagingDirectory,ConfigDir); 
			
			 // define logger first before doing this:
		
		     FillValues = SDPreProcessing(inputFile,params);
		
			 file = new MODISDataFile(inputFile,inputFile);
			 file.FileType = "MODIS";
			
		 }
		else if (DataFile.DetermineProduct(inputFile).equals("MLS")) {
			 this.snvid = S4PA_DatasetVersionID;
			 this.stats4metadata = stats;
			 this.params = params;
			 this.criteria = criteria;
			 this.inputFile = inputFile;
			 
			 MF = new ModisMaskFactory(StagingDirectory,ConfigDir); 
			
			 // define logger first before doing this:

			
		     FillValues = SDPreProcessing(inputFile,params);
		    
			 file = new MODISDataFile(inputFile,inputFile);
			 file.FileType = "MODIS";
			
		 }
		 else {
			 logger.fatal("Unable to identify type of inputfile from:" + inputFile);
			 System.out.println("Unable to identify type of inputfile from:" + inputFile);
		 }
		 */
		
	}	/*
	 * For MODAPS under MODAPS Arch:
	 * Ontology file has already been created.
	 * Used with new EXPRESSION architecture
	 */
	public ProductFactory(List<Statistics> stats,
			String StagingDirectory, String ConfigDir, 
			String inputFile ) {
		
		params = new ArrayList<String>();
		criteria = new ArrayList<String>();
		this.stats4metadata = stats;
		this.inputFile = inputFile;
		 
		MF = new ExpressionMaskFactory(StagingDirectory,ConfigDir); 
		file = new ExpressionDataFile(inputFile,inputFile);
		
		/*
		if (DataFile.DetermineProduct(inputFile).equals("AIRS")) {
			 this.stats4metadata = stats;
			 this.inputFile = inputFile;
			 MF = new AirsMaskFactory(StagingDirectory,ConfigDir); 
			 file = new AIRSDataFile(inputFile,inputFile);
			 file.FileType = "AIRS";
		 }
		else if (DataFile.DetermineProduct(inputFile).equals("MODIS")) {
			 this.stats4metadata = stats;
			 this.inputFile = inputFile;
			 MF = new ModisMaskFactory(StagingDirectory,ConfigDir); 
			 file = new MODISDataFile(inputFile,inputFile);
			 file.FileType = "MODIS";
		 }
		 else {
			 logger.fatal("Unable to identify type of inputfile from:" + inputFile);
			 System.out.println("Unable to identify type of inputfile from:" + inputFile);
			 System.exit(10);
		 }
		 */
	}
	/*
	 * Constructor for MODAPS operating under MODAPS Arch
	 * used by OntologyGetter
	 */
	public ProductFactory(List<String> params,List<String> criteria, 	
			String ConfigDir,String stagingdir, String S4PA_DatasetVersionID,
			String inputfile) {
			 this.snvid = S4PA_DatasetVersionID;
			 this.params = params;
			 this.criteria = criteria;
			 this.inputFile = inputfile;
			 
			 MF = new ExpressionMaskFactory(null,ConfigDir); 

			
	}
		 /**
		  * this also happens to be where our Castor unmarshaller is.
		  * @return
		  */
	public Root unmarshalXMLAndgetParams() {
		
	
		FileReader reader = null;
	    try {
	    	
	        reader = new FileReader(DQSS.OntFile);
	        
	    } catch (FileNotFoundException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        logger.error("ontology file not found:" + DQSS.OntFile);
	        System.exit(2);
	    } catch (NullPointerException e) {
	    	logger.warn("Ontology file was null");
	    	System.exit(2);
	    }
	     
	   
	    Root castorClasses = null;
		try {
		    castorClasses =
		    (Root) Unmarshaller.unmarshal(Root.class, reader);
		} catch (MarshalException e) {
		    // TODO Auto-generated catch block
			String explan = "Data in ontology file not quite right. Check arguments sent to ontology";
			logger.error(explan);
			System.err.println(explan);
		    e.printStackTrace();
		    System.err.println(explan);
		    System.exit(3);
		} catch (ValidationException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		try {
			castorClasses.validate();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
        QualityView[] qv = castorClasses.getQualityView();
        if (params == null || params.size() == 0) {
        	for (int view = 0 ; view < qv.length; ++view) {
        		params.add(qv[view].getDatavar().getDataset().getVariable().getName());
        	}
        }
        return castorClasses;
	
	}
	

    

	
	
	public void runWithOntologyFile() {
		runWithExpressionOntologyFile();
		
		/*
		if (file.FileType == "AIRS") {
			runWithAirsOntologyFile();
		}
		else if (file.FileType == "MODIS") {
			runWithModisOntologyFile();
		}
		*/
		
		/*
		 * All H4File/Dataset library stuff has been run. It needs to be closed
		 * so that HDFLibrary stuff can reopen for write (it opens it in a different way)
		 */
		
	}
	
	void writeDatasetLevelStats(	List<Statistics> stats)
    {
	

	if (params == null) {
		logger.error("No params array available");
		return;
	}
	if (FillValues == null) {
		logger.warn("No FillValues array available");
		return;
	}
	
	    Iterator<Statistics> statsitr = stats.iterator();
	    Iterator<String> itr = params.iterator();
	    
	    Iterator<DQSSNumber> fvitr   = FillValues.iterator();
	    if (criteria.size() == 0) {
	    	logger.debug("Lost criteria (" +
	    			"don't think I use this anymore");
	    }
	    int i = 0;
	    while(itr.hasNext()) {
	    	String name = (String) itr.next();
	    	/*
	    	 * fill in dimension names for newly created masks
	    	 */
	    	Statistics stat = statsitr.next();		    
	    	String check[] = null;
	    	if (name.length() > 0 && (file.getDataset(name, file.topGroup) != null)) {
		   
			    	String[] newdimnames = file.getDimensionNames(name);
			    	/*
			    	 * updateDimensionNames is not needed if there aren't any. 
			    	 * *_qcmask has what it needs already.
			    	 */
			    	//sid.UpdateDimensionNames(name + DQSS.MaskName, newdimnames);
			    	/*
			    	 * Set the quality level attribute
			    	 */
			    
			    	DQSSNumber fillvalue = (DQSSNumber) fvitr.next(); 
			    	String qualityAttr = ": ";
	    			/*
	    			 * Some data variables have more than one quality variable
	    			 */
			    	for (int q=0; q< stat.qualityLevel.size(); ++ q) {
	    				if (stat.getQualityVariableType(q).contains("QualityLevelVariable")) {
	    					qualityAttr += stat.datavariable.get(q) + ":" +
	    					               stat.getQualityLevel(q) + ", ";
	    				}
	    			}
		    		if (file.HDFType.contains("HDF4")) {

	    				
				    	file.setHE4Attribute("Quality",	name + DQSS.MaskName,  
				    			qualityAttr.substring(0,qualityAttr.length()-2));
				    	logger.debug("fill:" + fillvalue);
				    	file.setHE4Attribute("_FillValue",name + DQSS.MaskName, fillvalue.toString());
		    			
		    		}
		    		else {
				    	file.setHE5StringAttribute("Quality", name + DQSS.MaskName,
				    			qualityAttr.substring(0,qualityAttr.length()-2));
				    	logger.debug("fill:" + fillvalue);
				    	file.setHE5StringAttribute("_FillValue",name + DQSS.MaskName, fillvalue.toString());
		    			
		    		}
	
	    	}
	    	++i;
	    }
	
	    //sid.destroy();
    
  
    }
	

	
	public void runWithExpressionOntologyFile() {
		
		ExpressionDataFile afile = (ExpressionDataFile) file;
		FileReader reader = null;
		Root castorClasses = null;
		/*
		  *  do SDPreProcessing before opening file:
		  *  This is done already when ges-disc arch is used.
		  */
		
		 castorClasses = unmarshalXMLAndgetParams();
		 
		 if (FillValues == null || FillValues.size() == 0 ) {
			 if (afile.HDFType == "HDF4") {
				 //FillValues = SDPreProcessing(inputFile,params);
				
				 FillValues = afile.getH4FillValues(params); 
			 }
			 else {
				 FillValues = afile.getHEFillValues(params); 
			 }
			 if (FillValues == null) {
				 logger.error("No fill values found for:" + params.get(0)  + ". Probably doesn't exist in this file.");
				 return;
			 }
			 
		}
        QualityView[] Views = castorClasses.getQualityView();
				
        /**
         * Here now is where we are doing each QualityView in the input XML
         */
        for (int view =0 ; view < Views.length; ++ view) {
        	AggregateExpression he = new AggregateExpression(Views[view]);
        	he.file = afile;
            
        	//he.InvestigateExpression();
	    
        	DQSSNumber fillvalue =  FillValues.get(view);
        	Statistics stats = MF.StartMasking(he, fillvalue);
		    if (stats != null) {
	    		   stats4metadata.add(stats);
	    	}
        }
	
	    writeGlobalStatisticsWithDataFile(stats4metadata,file);
	    if (file.HDFType.contains("HDF4")) {
	    	writeDatasetLevelStats(stats4metadata);
			file.close();  
			//AddH4DatasetLevelAttributes(stats4metadata);

			//SetSpecificAttributesInMask(inputFile);
		}
		else {
			writeDatasetLevelStats(stats4metadata);
			file.close();  
		}
	   
		if (AllFillsInData) {
			//System.exit(99);
		}
	}
	  
	
	/*
	 * Get the ontology data for all of the datavariables to be screened.
	 */
	public void LoadOntologyData() {
	
		   	Iterator itr = params.iterator();
		   	Iterator crititr = criteria.iterator();
		   	Document doc = null;
		   	String xml = "";
		   
			
           
		   	while(itr.hasNext()) {
		   		
		    	String param = (String) itr.next();
		    	String criterion = (String) crititr.next(); 
		    	
		    	    if (snvid == null) {
		    	    	logger.fatal("snvid is null");
		    	    	return;
		    	    }
		    	    if (param == null) {
		    	    	logger.fatal("datavariable is null for:" + snvid);
		    	    	return;
		    	    }
		    	    if (criterion == null) {
		    	    	logger.fatal("usercritia  is null for:" + snvid + ", " + param);
		    	    	return;
		    	    }
		    	    
			    	if (paramtest(param, criterion)) {
			    		logger.debug("Sending:" + snvid + "," + param + "," + criterion + " for ontology loading");
			    	    //doc.appendChild( MF.LoadOntology(snvid,  param, criterion));
			    	    xml +=  MF.LoadOntology(snvid,  param, criterion);
			    	    //xml += DQSS.nodeToString(node);
			    	   
			    	    
			    	}
			    	
		    }
		   
			
		   	/* 
		   	 * In AIRS, we are getting the data each time so we get:
		   	 * <xml version...
		   	 * 	<root>
		   	 * 		<datavar>
		   	 * We go straight to datavar and clip out that part and add it
		   	 * to what we already have. That's why the output does not include it.
		   	 * The mistake we were making in MODIS is that we were wrapping the xml
		   	 * in <root> and so the <xml version... was INSIDE the root tag.
		   	 */
		    xml = arrangexml(xml);
		    DQSS.fileOverWrite(DQSS.OntFile, xml); 
	}
	
	
  	private String arrangexml(String xml) {
    	logger.debug("XML so far:" + xml);
    	int i = xml.indexOf("<QualityView>");
        if (i < 0) {
        	
        	String errorstr = "We are now expecting each xml result to be bounded with <QualityView>." +
        	" This was not found." + 
        	" We are only adding a base tag <root> after xml is returned from all datavariables."  +
        	" Perhaps old code is being referenced";
        	logger.error(errorstr);
        	System.err.println(errorstr);
        	xml = "<datavar>invalid results</datavar>";
        }
        else {
        	xml =   "<root>" +  System.getProperty("line.separator") + 
        			xml.substring(i, xml.length()) + 
        			System.getProperty("line.separator")+ "</root>" ;
        }
        logger.debug("XML after:" + xml);
        return xml;
}
	
	

	public boolean paramtest(String param, String criterion) {
		if (param.length() < 1) {
			logger.error("datavariable to screen has length 0");
			return false;
		}
		if (criterion.length() < 1) {
			logger.error("criterion has length 0");
			return false;
		}
		if (criterion.equals(DQSS.NoScreeningString)) {
			logger.debug("Not Screening " + param);
			return false;
		}
		logger.debug("Screening datavariable:" + param + " with " + criterion);
		return true;
	}
	
	/*
	 * Using ncsa.hdf.object.FileFormat for this operation works!
	 */
	void writeGlobalStatisticsWithDataFile (List<Statistics> stats4metadata, DataFile file)  {
		String ScreeningStats = "\n";
		String ScreeningAttribute = "Quality Screening";
		 Iterator statsitr = stats4metadata.iterator();
		
		 
		 while(statsitr.hasNext()) {
			 Statistics nextstats = (Statistics) statsitr.next();
			 HashMap<String,Boolean> RCRepeats = new HashMap<String,Boolean>();
			 int qlv = 0;
			 for (int i = 0; i < nextstats.datavariable.size(); ++i) {
				 
				 ScreeningStats += nextstats.getVariable() + " was filtered using " + 
			     nextstats.getQualityVariable(i).trim() ;
				 if (nextstats.getQualityName(i) != null) {
					 if (nextstats.getQualityVariableType(i).contains("RetrievalConditionFlag")) { 
						 /*
						  * Retrieval Condition is a combination of flags from many screening fields.
						  */
						 if (RCRepeats.isEmpty() || !RCRepeats.get(nextstats.getQualityName(i))) {
							 ScreeningStats += "(" + nextstats.getQualityName(i) + ")" ;
						 }
						 else {
							 int lastline = ScreeningStats.lastIndexOf("\n");
							 ScreeningStats = ScreeningStats.substring(0,lastline);
						 }
						 RCRepeats.put(nextstats.getQualityName(i), true);
						 
					 }
					 else {
						 ScreeningStats += " based on ";
					 }
					 if (nextstats.getQualityVariableType(i).contains("SurfaceTypeFlag")) { 
						 ScreeningStats += nextstats.getQualityName(i) + " SurfaceType Value ";
					 }
					 if (nextstats.getQualityLevel(qlv) != null) {
						 logger.debug("QualityLevel:" + nextstats.getQualityLevel(qlv));
						 if (! nextstats.getQualityVariableType(i).contains("RetrievalConditionFlag")) { 
						     ScreeningStats +=  nextstats.getQualityLevel(qlv).trim()  ;
						 }
						 if (nextstats.getQualityVariableType(i).contains("QualityLevelVariable")) {
							 ScreeningStats += "(" + nextstats.getQualityName(i) + ")";
						 }
						 ++qlv;
					 }
				 }
				 
				 ScreeningStats += ".\n";
				 
			 }
			    
                 if (nextstats.prefill == nextstats.total) {
                	 this.AllFillsInData = true;
                	 logger.warn("No non-fill data in " + nextstats.getVariable() +
                			 ". Exiting with special status");
                 }
                 else {
                	 this.AllFillsInData = false;
                 }
                 if ( nextstats.getTotal() >= 0) {
                	 ScreeningStats += "  Non-fill data before screening: " + nextstats.getPrePercentage() + "%;" + 
                	 " after screening " + nextstats.getPercentage()  + "%. ";
                	 int discarded = nextstats.getDiscarded();
                	 if (discarded > 0) {
                		 ScreeningStats += "( " + nextstats.getDiscarded() + " pixels )";
                	 }
                	 ScreeningStats += "\n";
                	 logger.warn(ScreeningStats);
                 }
                 else  {
                	 logger.warn("No stats for un-found dataset: " + nextstats.getVariable() );
                 }

		 }
		 Attribute stats_attr;
		if (ScreeningStats.length() > 1) { 
			try {
				String[] StatsArray = {ScreeningStats};
				if (file.HDFType.contains("HDF4")) {
					stats_attr = file.getYourHDF4Attribute(ScreeningAttribute,StatsArray );
				}
				else {
					stats_attr = file.getYourHDF5Attribute(ScreeningAttribute,StatsArray );
				}
				logger.debug("Screening Stats length:" + ScreeningStats.length());
				
				file.writeStatsToMetadata(stats_attr,StatsArray);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			logger.warn("No screening statistics. Probably no screening was attempted.");
		}
		 
				
	}
	/*
	 * MODAPS Arch:
	 */
	
	
	
	
	void reflect(List<String> dirs) throws SecurityException, NoSuchMethodException, IllegalArgumentException, Exception, Throwable {
		
		Stopwatch s = new Stopwatch();

	    s.start();
	 	int temp2 = 0;
	 	for (int pixel = 0 ; pixel < 250000; ++pixel ) {
	          
	 		 if (pixel < 250000) {
	 				 temp2 = pixel;
	 					  
	 		 }
	 			   
	 	}
	    System.out.println("Elapsed Time in millisecs: " + s.getElapsedTime());
	           
		ReflectDirections rf = new ReflectDirections();
		Class c = null;
		try {
			c = Class.forName("gov.nasa.gsfc.dqss.ReflectDirections");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Method m[] = c.getDeclaredMethods();
		 Class partypes[] = new Class[2];
         partypes[0] = Integer.TYPE;
         partypes[1] = Integer.TYPE;
           
		 Method meth = c.getMethod("lt", partypes);
		 
		   s.reset();
		 s.start();
		 int temp = 0;
		 boolean retval;
		 Object arglist[] = new Object[2];
		 for (int pixel = 0 ; pixel < 250000; ++pixel ) {
         	 
        	 arglist[0] = pixel;
        	 arglist[1] = 9980;
			 Object resp = meth.invoke(rf,arglist);
		     retval = (Boolean)resp;
			 if (retval) {
				 temp = pixel;
					   //System.out.println(pixel);
			 }
			   
		  }
          System.out.println("Elapsed Time in millisecs: " + s.getElapsedTime());
		
       
	}
   
	
	
	
}

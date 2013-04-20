package gov.nasa.gsfc.dqss;

import java.util.List;

import org.apache.log4j.Logger;

public class OntologyGetter {

	/**
	 * @param args
	 */
	static Logger logger = Logger.getLogger(MaskFactory.class.getName());
	
	public OntologyGetter(String DatasetName, 
						  String datavariables,
						  String usercriteria,
						  String config,
						  String stagingDir,
						  String inputfile) {
		
		    // don't need this one... DQSS dqss = new DQSS(stagingDir,config);
			// done in AirsMaskFactoryDQSS dqss = new DQSS(stagingDir,config);
			Masker masker = new Masker();
			
			List<String> params    = masker.getDataVariables(datavariables);
			List<String> criteria  = masker.getDataVariables(usercriteria);
			logger.debug("Number of criteria:" + criteria.size());
			logger.debug("Number of datavariables:" + params.size());
			ProductFactory PF = new ProductFactory(params,criteria,
					config,stagingDir, DatasetName, inputfile);
			masker.LabelOntologyFile(inputfile);
			PF.LoadOntologyData();
		 
	}
	public static void main(String[] args) {
		       
		  		 if (args.length == 6) {
		    	   String inputfile    = args[0]; 
		    	   String snvid        = args[1];
		           String datavariables = args[2];
		    	   String criteria     = args[3];
		    	   String stagingDir   = args[4];
		    	   String config       = args[5];
		    	   
		           OntologyGetter og = new OntologyGetter(snvid, datavariables,
		        		   criteria, config, stagingDir, inputfile);
		           
		       }
		  		 else {
		  			 logger.error("arguments needed: DatasetName, commadelimitedListOfDataVariables," +  
				 " commaDelimitedListOfCriteria, configPath, stagingdir, inputfile");
	 }
	}
	
	  public static void argumentsCheck(String args[]) {
		   logger.debug("I have the following constructors:");
		   logger.debug("inputFile stagingdir ");
		   logger.debug("inputFile SNVID datavar criteria stagindir ");
		   logger.debug("inputFile SNVID datavar criteria stagingdir doSD");
		   
		   for (int i = 0 ; i < args.length; ++i ) {
			   logger.debug(args[i]);
		   }
	   }
}

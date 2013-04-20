package gov.nasa.gsfc.dqss;



import gov.nasa.gsfc.Stopwatch;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ncsa.hdf.hdflib.HDFConstants;
import ncsa.hdf.hdflib.HDFException;
import ncsa.hdf.hdflib.HDFLibrary;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h4.H4Datatype;
import ncsa.hdf.object.h4.H4File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * <p>Title: Reading HDF4 Files with JAVA HDF4 Dataset class</p>
 * <p>Description: This class will:
 * 1. Read in a file.
 * 2. Read in 3 parameters
 * 3. Call the visualizer
 * 4. Apply a simple quality dataset to it's parameter
 * 5. Write out the new datasets
 * 6. Insert the visualized jpeg.
 *  
 * <pre>
 *     "not sure what I should put here yet.
 * </pre>
 * </p>
 *
 * @author Richard Strub GES-DISC
 * @version 1.0
 */
public class Masker 
{
   
	/*
	 * Constructor that does 3D TSurfAirStd and
	 * TAirMWOnlyStd w/Qual_MW_Only_Temp_Strat and Tropo
	 * 
	 * main() is at bottom, only constructor so far is at top
	 * 
	 */
	DataFile file;
	DataQuality dataQuality;
	//DataQualityTransition dataQualityT;
	String stagingDirectory;
	Statistics stats;
	static Logger logger = Logger.getLogger(Masker.class.getName());
	
	/*
	 * all of the parameters we can handle so far.
	 */
	
	/*
	 * original constructor. Not used after MODAPS Arch
	 */

/* 
 * Used by OntologyGetter under MODAPS Arch so that it can use
 * getDataVariables...
 */
public Masker() {}
/*
 *
 * MODAPS Arch in the GES-DISC environment where we create the ontology file
 * at Masking time.
 */
public Masker(String snvid , String param, String criterion, String inputFile, 
	     String dir, String config, String sendToStdOut) throws ClassNotFoundException
{
/*
* One or many data variables:
*/
//DQSS early = new DQSS(dir,config); // to debug input parameters.
List<String> params    = getDataVariables(param);
List<String> criteria  = getDataVariables(criterion);
List<Statistics> stats4metadata = new ArrayList<Statistics>();

/*
* Get fill values.
* SDProcessing opens the HDF file in a different way and needs
* to be closed before other processing proceeds
* (and vice versa)
* I ended up using a simpler API:
* http://www.hdfgroup.org/hdf-java-html/javadocs/ncsa/hdf/object/Dataset.html
* rather than the SD (c-like) API:
* http://www.hdfgroup.org/hdf-java-html/javadocs/ncsa/hdf/hdflib/HDFLibrary.html#SDsetdimname(int,%20java.lang.String)
* ...for most things.
* Unfortunately I needed to do a couple of things with SD (HDFLibrary) that
* I couldn't do otherwise
*/
    DQSS.NoOntologyFileProvided=true;
	ProductFactory pf = new ProductFactory(params,criteria,stats4metadata,
			dir,config,snvid,inputFile);
	/*
	 * Moving these slightly later after Logging system is established:
	 */
	logger.info("Number of criteria:" + criteria.size());
	logger.info("Number of datavariables:" + params.size());
	logger.info("param string:" + param);
	logger.info("We found " + params.size() + " params");
	LabelOntologyFile(inputFile);
	pf.LoadOntologyData();
	/*
	 * original GES-DISC arch  where we get ontology data as we go
	 */
	pf.runWithOntologyFile();

/*
* Update dimension names:
* SDProcessing opens the HDF file in a different way and needs
* to be opened after the above processing closes the file
* (and vice versa)
* I ended up using a simpler API:
* http://www.hdfgroup.org/hdf-java-html/javadocs/ncsa/hdf/object/Dataset.html
* rather than the SD (c-like) API:
* http://www.hdfgroup.org/hdf-java-html/javadocs/ncsa/hdf/hdflib/HDFLibrary.html#SDsetdimname(int,%20java.lang.String)
* ...for most things.
* Unfortunately I needed to do a couple of things with SD (HDFLibrary) that
* I couldn't do otherwise
*/
Stopwatch s = new Stopwatch();
s.start();

//SDPostProcessing(inputFile,params,criteria,  pf.FillValues);
//pf.SDPostProcessing(inputFile);
if (pf.AllFillsInData) {
	//System.exit(99);
}

s.stop();
logger.debug("SDPostProcessing: elapsed time: " + s.getElapsedTime());

if (sendToStdOut != null) {
	SendToSTDOUT(inputFile);
}


}

/*
 * MODAPS Arch with a previously written ontology file
 * and ontologyFile in not in default location
 */
public Masker( String inputFile, 
	     String dir, String config, String ontologyFile) throws ClassNotFoundException
{
	List<Statistics> stats4metadata = new ArrayList<Statistics>();

	ProductFactory pf = new ProductFactory(stats4metadata,dir,config,inputFile);
	//pf.LoadOntologyData();
	
	/*
	 * MODAPS arch -  use ontology file just produced.
	 */
   //LabelOntologyFile(inputFile);	
	DQSS.OntFile = ontologyFile;
    pf.runWithOntologyFile();



Stopwatch s = new Stopwatch();
s.start();




s.stop();
logger.debug("SDPostProcessing: elapsed time: " + s.getElapsedTime());

}
/*
 * MODAPS Arch with a previously written ontology file
 * and ontologyFile in default location
 */
public Masker( String inputFile, String dir, String config) throws ClassNotFoundException
{
	List<Statistics> stats4metadata = new ArrayList<Statistics>();

	ProductFactory pf = new ProductFactory(stats4metadata,dir,config,inputFile);
	//pf.LoadOntologyData();
	
	/*
	 * MODAPS arch -  use ontology file just produced.
	 */
 
   LabelOntologyFile(inputFile);	
   // DQSS.OntFile = DQSS.stagingDirectory + File.separator + DQSS.OntFile;
   // DQSS.OntFile = DQSS.OntFile;
     
   pf.runWithOntologyFile();


/*
* SDPostProcessing has to be after file has been H4File.closed()
*/
//  pf.SDPostProcessing(inputFile); 
//  pf.SetSpecificAttributesInMask(inputFile);
}

/* 
 * Update the dimension names of all newly created datasets:
 * dataVariableMask
 * dataVariableOriginal
 */
void LabelOntologyFile(String inputfile) {
	String hdf = new File(inputfile).getName();
	int i = hdf.lastIndexOf(".");
	String ext = hdf.substring(i+1,hdf.length());
	hdf = hdf.replace(ext, "xml");
	DQSS.OntFile =   DQSS.stagingDirectory + File.separator + "DQSS_" + hdf;
	logger.debug("Ontology file is:" + DQSS.OntFile);
}


void WriteAlgorithmsWithDataFile (List<Statistics> stats4metadata, DataFile file)  {
	String AlgStats = "\n";
	String AlgAttribute = "Algorithms Used";
	 Iterator statsitr = stats4metadata.iterator();
	 while(statsitr.hasNext()) {
		 Statistics nextstats = (Statistics) statsitr.next();
		 if (nextstats.getAlgorithm() != null) {
			 AlgStats += nextstats.getVariable() + ": " + nextstats.getAlgorithm() + "\n";
		 }
		 else {
			 logger.warn("No algorithm retrieved for " + nextstats.getVariable());
		 }
	 }
	 Attribute stats_attr;
	try {
		String[] StatsArray = {AlgStats};
		if (file.HDFType.contains("HDF4")) {
			stats_attr = file.getYourHDF4Attribute(AlgAttribute, StatsArray);
		}
		else {
			stats_attr = file.getYourHDF5Attribute(AlgAttribute, StatsArray );
		}
		
		file.writeStatsToMetadata(stats_attr,StatsArray);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
			
}



public List<String> getDataVariables(String param) {
	
	List<String> params = new ArrayList<String>();   
	StringTokenizer paramsT = null;
	
	
	
	if (param.contains(",")) {
		paramsT = new StringTokenizer(param,",");
	
		while (paramsT.hasMoreTokens()) {
			params.add(paramsT.nextToken());
		}
	}
	else {
		params.add(param); 
	}
	
	return params;
}


 
   
   public static void argumentsCheck(String args[]) {
	   
	   
	   for (int i = 0 ; i < args.length; ++i ) {
		   logger.error(args[i]);
	   }
   }
   public static void main( String args[] ) 
   {
   
   	String inputFile = null;
   
   	try { 
   		/*
   		 * MODAPS arch default OntFile location
   		 */
       if (args.length == 3) {
    	           inputFile = args[0];
    	   String stagingDir = args[1];
    	   String config     = args[2];
    	   Masker inst = new Masker(inputFile, stagingDir, config);
       }
       else if (args.length == 4) {
           	       inputFile = args[0];
           String stagingDir = args[1];
           String config     = args[2];
           String ontologyfile=args[3];
           Masker inst = new Masker(inputFile, stagingDir, config, ontologyfile);
       }
       /*
        * Original GES-DISC Testing and Production
        * (We only send to stdout in screener)
        */
       else if (args.length == 6) {
    	    inputFile = args[0];
    	    
    	   String snvid        = args[1];
           String datavariable = args[2];
    	   String criteria     = args[3];
    	   String stagingDir   = args[4];
    	   String config       = args[5];
           Masker inst = new Masker(snvid,datavariable,criteria,inputFile,
        		                    stagingDir,config,null);
       }
       /*
        * 
        */
       else if (args.length == 7) {
    	   inputFile           = args[0];
    	   
           String snvid        = args[1];
           String datavariable = args[2];
    	   String criteria     = args[3];
    	   String stagingDir   = args[4];
    	   String config       = args[5];
    	   String sendToStdOut = args[6];
           Masker inst = new Masker(snvid, datavariable,criteria,inputFile,
        		   stagingDir,config,sendToStdOut);
       }
      
       else {
    	   argumentsCheck(args);
    	   System.err.println("Proper number of arguments not received.");
    	   System.exit(1);
       }
   	}
   	catch (ClassNotFoundException e) {
   		System.err.println("Could not find needed jar files.");
   		e.printStackTrace();
   		System.err.println("Could not find needed jar files.");
   	   	}
       
   	
   }  // end main
   
   public static void SendToSTDOUT(String maskedFile) {
       int status;
       int BUFFERSIZE = 4096*16;
       
       File file = new File(maskedFile);
	   FileInputStream fis = null;
	try {
		fis = new FileInputStream(maskedFile);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       
       byte[] data = new byte[BUFFERSIZE];
       String sample = data.toString();
       int count;    
       
       try {
    	   while ( (count =   fis.read( data )) != -1 ) {
		           System.out.write( data, 0, count );
		           
		   }
    	   fis.close();
       } catch (IOException e) {
		// TODO Auto-generated catch block
		// probably just EOFe.printStackTrace();
       }
        
        
       
       
       /*
	   try {
		   File file = new File(maskedFile);
		   FileInputStream  fis = new FileInputStream(file);

		      // Here BufferedInputStream is added for fast reading.
		   BufferedInputStream bis = new BufferedInputStream(fis);
		   DataInputStream dis = new DataInputStream(bis);

		      // dis.available() returns 0 if the file does not have more lines.
		      while (dis.available() != 0) {

		      // this statement reads the line from the file and print it to
		        // the console.
		    	  status = dis.readByte();
		        System.out.write(dis.);
		        
		      }

		      // dispose all the resources after using them.
		      fis.close();
		      bis.close();
		      dis.close();

		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		    	// EOF
		      //e.printStackTrace();
		    }
		   */
		    
   }
   private String fileRead(String filepath) {
	   
	   String lastline = null;
	   try {
		   File file = new File(filepath);
		   FileInputStream  fis = new FileInputStream(file);

		      // Here BufferedInputStream is added for fast reading.
		   BufferedInputStream bis = new BufferedInputStream(fis);
		   DataInputStream dis = new DataInputStream(bis);

		      // dis.available() returns 0 if the file does not have more lines.
		      while (dis.available() != 0) {

		      // this statement reads the line from the file and print it to
		        // the console.
		    	  lastline = dis.readLine();
		        System.out.println(lastline);
		        
		      }

		      // dispose all the resources after using them.
		      fis.close();
		      bis.close();
		      dis.close();

		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    return lastline;
   }
   private void fileWrite(String filepath, String contents ) {
	   try{
		    // Create file 
		    FileWriter fstream = new FileWriter(filepath);
		        BufferedWriter out = new BufferedWriter(fstream);
		    out.write(contents);
		    //Close the output stream
		    out.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
   }
 
 
       
}

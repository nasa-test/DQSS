package gov.nasa.gsfc.dqss;

import gov.nasa.gsfc.dqss.castor.expression.ScreeningField;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ncsa.hdf.object.Dataset;


public abstract class Mask {
    
	protected Dataset data;
	protected Dataset[] quality;
	protected Dataset[] reference;
	protected long rows = -1;
	protected long cols = -1;
	protected long layers = 1; // default
	protected long dims4 = -1;
	protected long dims5 = -1;
	
	protected String dataVariable = null;
	private   String stagingDirectory = null;
	private   String statisticsFilename = "stats.dat";
	protected Statistics stats;
	protected int indexoffset = 1;
	protected long pCrit = 3;
	DataQuality dataQuality;
	protected long rank = -1;
	protected int indexOffset = 1;
	protected float FillValue = -9999;
	protected String Algorithm;
	protected DataFile file;
	
	static Logger logger = Logger.getLogger(Mask.class.getName());
	/*
	 * Most reference this constructor.
	 */
	public  Mask(AggregateExpression he,  String dir ) {
		
		this.file = he.file;
		this.data = he.data;
		this.quality = he.QSVars;
		this.rank = data.getRank();
	    he.file.DF.setRank(data.getRank());
	    this.stagingDirectory = dir;
	    dataVariable = data.getName();

	    this.stats = new Statistics(statisticsFilename, dataVariable, he,
	    		he.QSVars);
	    data.init();
	    long[] dims = data.getDims();
	    /*
	     * sets dimensions and layers
	     */
	    for (int i = 0; i < rank; ++i) {
	    	he.file.DF.setDimSize(i, dims[i]);
	    }
	    if (he.file.DF.getLayers() == null) {
	    	layers = 1;
	    }
	    else {
	    	layers = he.file.DF.getLayers()[0];
	    }
	    rows = he.file.DF.getDimSize(0);
	    cols = he.file.DF.getDimSize(1);
//	    evaluateDimensions(he);
	    
		if (he.file.crit2num.size() > 0) {
			for (int i = 0; i < he.file.crit2num.size(); ++i) {
				pCrit = he.file.crit2num.get(i);
				String qlname = he.file.sf_names.get(i);
				logger.debug("Setting QualityLevel:" + pCrit + " for " + data.getName() + 
				" of type " + (he.file.get_qs_types()).get(i)); 
				stats.setQualityLevel(pCrit,i,qlname);
			}
		}
		else if (he.sfs == null){
			logger.error("No criteria extracted from ontology for " + dataVariable);
		}
		else {
			// expression ontology
		}
		
		logger.debug("Dims.length = " + dims.length);
	}
	/*
	 * no references
	 */
	
	public  Mask(Dataset data, Dataset[] qual, Dataset[] ref,  String dir) {
		
		this.data = data;
		this.quality = qual;
		this.reference = ref;
		this.rank = data.getRank();
	    
	    this.stagingDirectory = dir;
	    dataVariable = data.getName();
	    
	    // need to change args to use this: this.stats = new Statistics(statisticsFilename, dataVariable);
	    long[] dims = data.getDims();
	    if (dims.length >= 3) {
	    	rows = dims[0];
	    	cols = dims[1];
	    	layers = dims[2];
	    }
	    else if (dims.length == 2) {
	    	rows = dims[0];
	    	cols = dims[1];
	    }
	    else {
	    	logger.error("Dims.length = " + dims.length);
	    }
	    if (dims.length >= 2) {
	    	layers = dims[2];
	    }
	    if (dims.length > 3) {
	    	dims4 = dims[3];
	    	dims5 = dims[4];
	    }
		logger.debug("Dims.length = " + dims.length);
	}
	/*
	 * Perhaps param is not necessary as it is in data.getName();
	 * 1 reference
	 */
	public  Mask(Dataset data, Dataset[] qual, 
			 long criteria, String dir) {
		this.data = data;
		this.quality = qual;
		this.layers = data.getRank();
		
		pCrit = criteria;
		this.stagingDirectory = dir;
		dataVariable = data.getName();
		
		// need to change args to use this: this.stats = new Statistics(statisticsFilename, dataVariable);
	    long[] dims = data.getDims();
	    if (dims.length >= 1) {
	    	rows = dims[0];
	    	cols = dims[1];
	    	
	    }
	    else {
	    	logger.error("Dims.length = " + dims.length);
	    }
	    
	    if (dims.length > 2) {
	    	layers = dims[2];
	    }
	    if (dims.length > 3) {
	    	dims4 = dims[3];
	    	dims5 = dims[4];
	    }
		logger.debug("Dims.length = " + dims.length);
	    
	}
	
	
	public  abstract void doMask() throws Exception ;
	public  abstract void doMask_reflect() throws Exception ;
/*	
	private void evaluateDimensions(AggregateExpression he) {
		for (int i=0; i < rank; ++i) {
			String dfDim = he.file.DF.getDimension(i);
			for (int j=0; j < he.file.SFs.size(); ++j) {
				if (!dfDim.contains(he.file.SFs.get(j).getDimension(i))) {
					logger.error("Dimension issue. datafield(" +  
					i + ") = " + dfDim);
					logger.error("Dimension issue. screeningfield(" +  
							i + ") = " + he.file.SFs.get(j).getDimension(i));
							
				}
			}
		}
	}
	*/
	 public boolean writeStats() {
		 String percentage = stats.getPercentage();	 
		 String output = dataVariable  + " mask retained " +
		 stats.kept + " of " + stats.total + " pixels. " + percentage + "%";
		 //String currentcontents = fileRead();
		 return DQSS.fileAppend(output);
	 }
	 
	 private boolean fileAppend(String newStats) {
		 boolean status = false;
		    try{
		        // Create file 
		        FileWriter fstream = new FileWriter(stagingDirectory + File.separator + this.statisticsFilename,true);
		            BufferedWriter out = new BufferedWriter(fstream);
		        out.write(newStats + "\n");
		        //Close the output stream
		        out.close();
		        status = true;
		        }catch (Exception e){//Catch exception if any
		          logger.error("Error: " + e.getMessage());
		        }
		        return status;
	 }
	 
	 private String fileRead() {
		   
		   String lastline = null;
		   try {
			   File file = new File(stagingDirectory + File.separator + this.statisticsFilename);
			   if (file.exists()) {
				   FileInputStream  fis = new FileInputStream(file);
               
			      // Here BufferedInputStream is added for fast reading.
				   BufferedInputStream bis = new BufferedInputStream(fis);
				   DataInputStream dis = new DataInputStream(bis);

			      // dis.available() returns 0 if the file does not have more lines.
			      while (dis.available() != 0) {

			      // this statement reads the line from the file and print it to
			        // the console.
			    	  lastline += dis.readLine();
			       
			        
			      }

			      // dispose all the resources after using them.
			      fis.close();
			      bis.close();
			      dis.close();
			   }
			    } catch (FileNotFoundException e) {
			      e.printStackTrace();
			    } catch (IOException e) {
			      e.printStackTrace();
			    }
			    return lastline;
	   }
	   private boolean fileWrite(String current, String additionalStatsForNewParameter ) {
		   boolean status = false;
		   try{
			    // Create file 
			    FileWriter fstream = new FileWriter(stagingDirectory + File.separator + this.statisticsFilename);
			        BufferedWriter out = new BufferedWriter(fstream);
			    if (current != null) {
			    	out.write(current );
			    	out.newLine();
			    }
			    else  {
			    	logger.debug("Mask::fileWrite current was null");
			    }
			    if (additionalStatsForNewParameter != null) {
			    	out.write(additionalStatsForNewParameter );
			    	out.newLine();
			    }
			    else  {
			    	logger.error("Mask::fileWrite  additionalStatsForNewParameter was null");
			    }
			    //Close the output stream
			    out.close();
			    }catch (Exception e){//Catch exception if any
			      logger.error("Mask::fileWrite Error: " + e.getMessage());
			      return status;
			    }
			    status = true;
			    return status;
	   }
	   
	 public Statistics getStats() {
		 return this.stats;
	 }
	 

	 /**
	  * Endian: byte order, not bit order: Big: byte & 0x0000000E
	  * In Big Endian systems the most significant bit is the 0th or first bit. So in the chart
	  * below the surface type has startbit 0 and stop bit 1.
	  * The surface type flag is bits 0 and 1. The usefulness flag is bit 1
	  * The problem I was having resolving negative numbers (in evaluating the surface type flag)
	  * completely disappeared when I &'ed it with a 4 byte integer. 
	  * 11000101 & 0x000000C0 threw the 8th bit way into the number past any consideration of 
	  * a sign bit.
	  *
	 
	  * Quality_Assurance_Near_Infrared: Length: 1 byte (8 bits)
	   Flag Name						Number of Bits		Bit Values		Definitions
	   Total Precipitable Water (NIR)	1					0,1				Not useful, Useful (startbit: 7  stopbit:7 )(right most bit)
			   Usefulness flag
		byte & 0x00000001 >> 0;
			   
		Total Precipitable Water (NIRT) 3					0,1,2,3,		Bad(Fill) Marginal,Good,VG (startbit: 4, stopbit: 6)
		byte & 0x0000000E >> 1;
		
		Confidence Flag
		Inversion Method Used (NIR)     2                   0,1,2           2 chan ratio, 3 chan ratio, no retrival (startbit:2, stopbit:3)
		byte & 0x00000030 >> 4;
		
		Surface Type                    2					0,1,2,3			Bright Land, Clear Ocean, Cloud, Sunglint (startbit: 0, stopbit:1)
		byte & 0x000000C0 >> 6;
		
		Note. For the sake of making this programmatic (based on received start and stop bits:
		int  org = (a & 0x0000C0) >> 4;    is same as:  int  usint = (a & 192) >> 4;
             org = (a & 0x0000000E) >> 1;  is same as:  usint = (a & 15) >> 1;
		
	*
	 *
	  * So if shifting first:
	  *       startbit:7  stopbit:7 (1 bit)  mask = 0x00000001 (nbits) shift 0 (7-stopbit) (usefulness)
	  *       startbit:4, stopbit:6 (3 bits) mask = 0x00000007         shift 1             (totprecipwater)
	  *       startbit:2, stopbit:3 (2 bits) mask = 0x00000003         shift 4             (inversion)
	  *       startbit:0, stopbit:1 (2 bits) mask = 0x00000003         shift 6             (surface)
	  *       1 -> 1 (2 ^ x - 1)
	  *       2 -> 3
	  *       3 -> 7
	  *       4 -> 15
	  */
	 protected int evaluateQualityFlag(Byte data, int startBit, int stopBit
			 , boolean endian) {
		 if (data == 0 ) {
			 return 0; // let's not waste time if we don't have to...
		 }
		
		 if (endian) {
		 int shift = startBit; // note this only works if the least significant bit is called bit 7
		 int nbits =  (stopBit - startBit)+1 ; //inclusive
		// int mask  =  (2 ^ nbits) - 1;
		 int mask = (int) java.lang.Math.pow ( 2, nbits) - 1;
		 
		 int preshift = data;
		 int shifted = preshift >> shift;
		 
		 int flag  = shifted & mask;
		 return flag;
		 }
		 else {
			 logger.error("Not handling SmallEndian yet");
		 }
		 return -1;
	 }

	 protected void testShiftingFirst (Byte qa, Byte cld) {
		 int official = evaluateCloudinessFlag(cld);
		 int preshift = cld;
		 int shifted = preshift >> 6;
		 int cldMask = 0x00000003;
		 int lateshift = shifted & cldMask;
		 if (lateshift != official) {
			 logger.debug ("evaluateCloudinessFlag official:" + official + " preshifted:" + lateshift);
		 }
		 official = evaluateSurfaceBits(qa) ;
		 preshift = qa;
		 shifted = preshift >> 6;
		 cldMask = 0x00000003;
		 lateshift = shifted & cldMask;
		 if (lateshift != official) {
			 logger.debug ("evaluateSurfaceBits official:" + official + " preshifted:" + lateshift);
		 }
		 
		 official = evaluateTotalPrecipConfFlag(qa);
		 preshift = qa;
		 shifted = preshift >> 1;
		 cldMask = 0x00000007;
		 lateshift = shifted & cldMask;
		 if (lateshift != official) {
			 logger.debug ("evaluateTotalPrecipConfFlag official:" + official + " preshifted:" + lateshift);
		 }
		
		 
	 }
	 
	 
	 private int shiftfirstevaluateSurfaceBits(Byte a) {

	        int surfaceMask = 0x000000C0;
			int surface = (a & surfaceMask) >> 6;
	  	    return surface;
		}
	 private int evaluateSurfaceBits(Byte a) {
		 int preshift = a;
		 int shifted = preshift >> 6;
		 int cldMask = 0x00000003;
		 int surface  = shifted & cldMask;
	  	 return surface;
		}
	 protected int shiftlaterevaluateCloudinessFlag(Byte a) {

	        int cldMask = 0x000000C0;
			int cld = (a & cldMask) >> 6;
	  	    return cld;
		}
	 protected int evaluateCloudinessFlag(Byte a) {
		 int preshift = a;
		 int shifted = preshift >> 1;
		 int cld = shifted &  0x00000003;
  	     return cld;
		}
		private int evaluateConfidenceBits(Byte a) {

	        int confidenceMask = 0x0000000E;
			int confidence = (a & confidenceMask) >> 1;
	  	    return confidence;
		}
		protected int evaluateUsefulnessBits(Byte a) {

	        int usefulnessMask = 0x00000001;
			int usefulness = (a & usefulnessMask) ;
	  	    return usefulness;
		}
		protected int shiftfirstevaluateTotalPrecipConfFlag(Byte a) {
			
		    int confidenceMask = 0x0000000E;
		    int confidence = (a & confidenceMask) >> 1;
	  	    return confidence;
		}
		protected int evaluateTotalPrecipConfFlag(Byte a) {
			 int preshift = a;
			 int shifted = preshift >> 1;
			 int confidenceMask = 0x00000007;
		     int confidence = (shifted & confidenceMask);
	  	     return confidence;
		}
		private int evaluateInversionBits(Byte a) {

	        int usefulnessMask = 0x00000030;
			int usefulness = (a & usefulnessMask) >> 4;
	  	    return usefulness;
		}
	     
	    /**
	     * 
	     * @param DFdimname name of datavariable dimensions we are trying to match
	     * @param sfs ncsa.hdf.Datasets
	     * @param ith = parent iterator number - 1
	     * @param SF = all the quality dataset information from ontology
	     * @return the index of the quality variable dimension which matches the the given datavariable dimension 
	     *  
	     * General stuff: the array containing a list of integers of matching dimensions where 
	     * the last array in list contains the ones that don't match. So if 
	     * there is one DF and one SF then {[1,2], [0,1], [0,2]} Means that DF plane made up
	     * of dim 1,0 matches SF plane made up of dim 2,1. 
	     * <p> [0,2] at the end means that DF dim 0 and SF dim 2 did not match up.
	     * 
	     */
	    
		private int getMatchingIthDimension(String DFdimname, Dataset[] sfs , 
				int ith, HashMap<String,screeningfield> SF) {
			int pairings = -1;
			String[] sfnames = file.getDimNamesGen(sfs[ith]);
			//String[] sfnames =  sfs[ith].getDimNames();
			
			if (sfnames == null) {
				sfnames = getAssignedNames(SF,sfs[ith].getName());
			}
			for (int i=0; i < sfnames.length; ++i) {
				if (sfnames[i].contains(DFdimname)) {
					return i;
				}
			}
			
			return pairings;
		}
		/**
		 * If the HDF file does not have dimension names, (MLS), we have assinged them in the SF
		 * @param SF
		 * @param datasetname
		 * @return
		 */
		private String[] getAssignedNames(HashMap<String,screeningfield> SF, String datasetname) {
			 Iterator it = SF.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry SFpair = (Map.Entry)it.next();
			        screeningfield sf = (screeningfield) SFpair.getValue();
			       if (sf.getVariable().contains(datasetname)) {
			    	   String[] sfnames = new String[sf.getDimensions().size()];
			    	   for (int i=0; i < sf.getDimensions().size(); ++i) {
			    		   sfnames[i] = sf.getDimensions().get(i);
			    	   }
			    	   return sfnames;
			       }
			    }
			return null;    
		}
		public List<int[]> org_matchDimensions(datafield DF, Dataset[] sfs, HashMap<String,screeningfield> SF) {
			List<String> datafieldDims = DF.getDimensions();
			List<int[]> hash = new ArrayList<int[]>();
			int DFmissmatch = -1;
			
			
			
			for (int dfdim=0; dfdim < datafieldDims.size(); ++dfdim) {
				int[] dfDims = new int[sfs.length + 1];
				dfDims[0] = dfdim;
				boolean found = false;
				for (int i = 1; i <= sfs.length; ++i) { // each screeningfield
                    // SFIthDim is the ith dimension in SF.
                    // i is which SF
                    // dfdim    is the ith dimension in DF
					int SfIthDim = getMatchingIthDimension(datafieldDims.get(dfdim),sfs,i-1,SF);
					if (SfIthDim > -1 ) {
		               dfDims[i] = SfIthDim;
		               found = true;
					}
					
					
				}
				if (found) {
					hash.add(dfDims);
				}
				else {
					DFmissmatch = dfdim;
				}
			}
			
			/*
			 * Find missmatch screeningfield.
			 */
			int[] dfDims = new int[sfs.length + 1];
			for (int i=0; i < dfDims.length; ++i) {dfDims[i] = -1;}
			dfDims[0] = DFmissmatch;
			
			for (int i = 0; i < sfs.length; ++i) { // each screeningfield
				String[] sfnames = sfs[i].getDimNames();
				if (sfnames == null) {
					sfnames =  getAssignedNames(SF,sfs[i].getName());
				}
				for (Integer sfcnt=0; sfcnt < sfnames.length; ++sfcnt) { // each sf dimension name
					int counter = 0;
		    		for (Integer dfcnt=0; dfcnt < datafieldDims.size(); ++dfcnt) { // each datafield dimension name
			    	   	if (sfnames[sfcnt].contains(datafieldDims.get(dfcnt))) {
			    	   		++counter;
			    	   	}
		    		}
		    		if (counter == 0) {
		    			dfDims[i+1] = sfcnt;
		    		}
				}
			}
			hash.add(dfDims);
			return hash;
		}
		/**
		 * 
		 * @param DF is the object populated by the information from the ontology for the datavariable. We
		 * use it to get the dimensions (their order) 
		 * @param sfs The array of (quality) Datasets
		 * 
		 * @param SF is the object populated by the ontology for information for each quality variable (here
		 * stored in a hash)
		 * @return
		 */
		public List<int[]> matchDimensions(datafield DF, Dataset[] sfs, HashMap<String,screeningfield> SF) {
			List<String> datafieldDims = DF.getDimensions();
			List<int[]> dvqvDimPairings = new ArrayList<int[]>();
			int DFmissmatch = -1;
			
			
			/**
			 * For each of the dimensions in the datavariable...
			 * If dvqvDimPairings is something like  [1,0] [2,1] [0,2] means that dvdims(1,2) correspond
			 * with qvdims(0,1) and dv(0) matches no qvdims and qv(2) matches no dvdim. So the last pair
			 * in the List means something different than the other pairs.
			 */
			for (int dfdim=0; dfdim < datafieldDims.size(); ++dfdim) {
				int[] dfDims = new int[sfs.length + 1]; // holds the matching dimension pairings
				dfDims[0] = dfdim;
				boolean found = false;
				for (int i = 1; i <= sfs.length; ++i) { // each screeningfield
                    // SFIthDim is the ith dimension in SF. (screeningfield)
                    // i is which SF
                    // dfdim    is the ith dimension in DF (datavariable)
					int SfIthDim = getMatchingIthDimension(datafieldDims.get(dfdim),sfs,i-1,SF);
					dfDims[i] = SfIthDim;
					if (SfIthDim > -1 ) {
		              
		               found = true;
					}
					
					
				}
				if (found) {
					dvqvDimPairings.add(dfDims); // if dfDims = [1,0] it means datavar dimname[1] = quality var dimname[0]
				}
				else {
					DFmissmatch = dfdim; // stores which datavariable dimension didn't get matched (Z axis)
				}
			}
			
			/*
			 * Find missmatch screeningfield.
			 */
			int[] dfDims = new int[sfs.length + 1];
			for (int i=0; i < dfDims.length; ++i) {dfDims[i] = -1;}
			dfDims[0] = DFmissmatch; // if DFmissmatch >=0 then we know it has an unmatched dim name.
			
			for (int i = 0; i < sfs.length; ++i) { // each screeningfield
				String[] sfnames = file.getDimNamesGen(sfs[i]);
				if (sfnames == null) {
					sfnames =  getAssignedNames(SF,sfs[i].getName());
				}
				for (Integer sfcnt=0; sfcnt < sfnames.length; ++sfcnt) { // each sf dimension name
					int counter = 0;
		    		for (Integer dfcnt=0; dfcnt < datafieldDims.size(); ++dfcnt) { // each datafield dimension name
			    	   	if (sfnames[sfcnt].contains(datafieldDims.get(dfcnt))) {
			    	   		++counter;
			    	   	}
		    		}
		    		if (counter == 0) { // no match was found
		    			dfDims[i+1] = sfcnt; // resulting set is those dims which are not matching
		    		}
				}
			}
			dvqvDimPairings.add(dfDims);// resulting set being added to the hash is those dims which are not matching
			return dvqvDimPairings;
		}
		public List<int[]> OrgmatchDimensions(datafield DF, Dataset[] sfs) {
			List<String> datafieldDims = DF.getDimensions();
			List<int[]> hash = new ArrayList<int[]>();
			int listsize = sfs.length + 1;
			int DFmissmatch = -1;
			int[] SFmissmatch = new int[sfs.length];
			for (int cnt=0; cnt < sfs.length; ++cnt) {SFmissmatch[cnt] = -1;}
			
			for (int i = 0; i < sfs.length; ++i) { // each screeningfield
				String[] sfnames = sfs[i].getDimNames();
			    for (Integer dfcnt=0; dfcnt < datafieldDims.size(); ++dfcnt) { // each datafield dimension name
		    		int[] grp = new int[listsize];
		    		for (int cnt=0; cnt < listsize; ++cnt) {grp[cnt] = -1;}
		    		int counter = 0;
			    	for (int sfcnt=0; sfcnt < sfnames.length; ++sfcnt) { // each sf dimension name
				    	if (sfnames[sfcnt].contains(datafieldDims.get(dfcnt))) {
				    		if (grp[0] == -1) {
				    			grp[0] = dfcnt;
				    			++counter;
				    		}
				    		grp[counter] = sfcnt;
				    		++counter;
				    		
				    	}
				    	
				    
				    }
			    	if (grp[0] != -1) {
			    		hash.add(grp);
			    	}
			    	else {
			    		 DFmissmatch = dfcnt;
			    	}
			    	
			    }
			}
			/*
			 * Find missmatch screeningfield.
			 */
			for (int i = 0; i < sfs.length; ++i) { // each screeningfield
				String[] sfnames = sfs[i].getDimNames();
				for (Integer sfcnt=0; sfcnt < sfnames.length; ++sfcnt) { // each sf dimension name
					int counter = 0;
		    		for (Integer dfcnt=0; dfcnt < datafieldDims.size(); ++dfcnt) { // each datafield dimension name
			    	   	if (sfnames[sfcnt].contains(datafieldDims.get(dfcnt))) {
			    	   		++counter;
			    	   	}
		    		}
		    		if (counter == 0) {
		    			SFmissmatch[i] = sfcnt;
		    		}
				}
			}
			int[] grp = new int[listsize];
			grp[0] = DFmissmatch;
			for (int cnt=1; cnt <= sfs.length; ++cnt) {grp[cnt] = SFmissmatch[cnt-1];}
			
			hash.add(grp);
					
					
			
			
    	
			return hash;
			}
		public List<int[]> norgmatchDimensions(datafield DF, Dataset[] sfs) {
			List<String> datafieldDims = DF.getDimensions();
			List<int[]> hash = new ArrayList<int[]>();
			int listsize = sfs.length + 1;
			int DFmissmatch = -1;
			int[] SFmissmatch = new int[sfs.length];
			for (int cnt=0; cnt < sfs.length; ++cnt) {SFmissmatch[cnt] = -1;}
			
				
			    for (Integer dfcnt=0; dfcnt < datafieldDims.size(); ++dfcnt) { // each datafield dimension name
			    	// create grp and initialize
			    	int[] grp = new int[listsize];
		    		for (int cnt=0; cnt < listsize; ++cnt) {grp[cnt] = -1;}
		    		// get ith dimension of each SF int array:
			    	String[] sfnames = new String[sfs.length];
					for (int cnt=0; cnt < sfs.length; ++cnt) {sfnames[cnt] = sfs[cnt].getDimNames()[dfcnt];}
    		
		    		int counter = 0;
			    	for (int sfcnt=0; sfcnt < sfnames.length; ++sfcnt) { // each sf dimension name
				    	if (sfnames[sfcnt].contains(datafieldDims.get(dfcnt))) {
				    		if (grp[0] == -1) {
				    			grp[0] = dfcnt;
				    			++counter;
				    		}
				    		grp[counter] = dfcnt;
				    		++counter;
				    	}
				    }
			    	if (grp[0] != -1) {
			    		hash.add(grp);
			    	}
			    	else {
			    		 DFmissmatch = dfcnt;
			    	}
			    	
			    }
			
			/*
			 * Find missmatch screeningfield.
			 */
			for (int i = 0; i < sfs.length; ++i) { // each screeningfield
				String[] sfnames = sfs[i].getDimNames();
				for (Integer sfcnt=0; sfcnt < sfnames.length; ++sfcnt) { // each sf dimension name
					int c = 0;
		    		for (Integer dfcnt=0; dfcnt < datafieldDims.size(); ++dfcnt) { // each datafield dimension name
			    	   	if (sfnames[sfcnt].contains(datafieldDims.get(dfcnt))) {
			    	   		++c;
			    	   	}
		    		}
		    		if (c == 0) {
		    			SFmissmatch[i] = sfcnt;
		    		}
				}
			}
			int[] grp = new int[listsize];
			grp[0] = DFmissmatch;
			for (int cnt=1; cnt <= sfs.length; ++cnt) {grp[cnt] = SFmissmatch[cnt-1];}
			
			hash.add(grp);
					
					
			
			
    	
			return hash;
			}
}



package gov.nasa.gsfc.dqss;

import gov.nasa.gsfc.dqss.castor.expression.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ncsa.hdf.object.Dataset;

/**
 * @author Richard Strub
 * @version 1.5
 * 
 * MOD05_L2 NIR - the only one using for MOD05
 * Not sure I want to use this class extension yet...
 */
public class ExpressionMask extends Mask {

	/* The procedure from going from hdfview QA NIR byte to Surface Type value:
	 * 1. Take positive of given number: -121 -> 121 = 01111001
	 * 2. Take two's complement:                       10000111
	 * 3. So since surface type is bits 7 and eight -121 translates to 2
	 *    (as does -123,-125 they vary because they have other conf values)
	 *    -61 -> 3 (00111101 -> 11000011)
	 *    +67 -> 1 (01000011)
	 * To obtain NIR TotPrecipConf which is bytes 2,3,4 you look at those bits after above procedure
	 * -125 -> 10000011 = 1
	 *  +67 -> 01000011 = 1
	 * -121 -> 10000111 = 3 
	 * To obtain Cloud Mask Cloudiness Flag bits 2,3
	 * 57 = 0 , 59 = 1, 61 = 2, 63 = 3
	 */    
	/*
	 * This class won't take care of both the IR and the NIR because the
	 * Cloud Mask is not needed for the IR. This is good because the Cloud_Mask_QA
	 * is 2030x1354 whereas IR is that divided by 5 or 406x270
	 * So that's how we can remember that CMQA is not needed for IR.
	 * Chris has modeled in his XML stub that the 
	 * IR QA is Total Precip Water Confidence Flag and the 
	 * MODIS Atmosphere QA Plan shows that the this info is in the first byte of the 
	 * Quality_Assurance_Infrared
	 * Chris has modeled that NIR will have quality variables 
	 * Quality_Assurance_Near_Infrared/Total Precip Water Confidence Flag 
	 * AND Cloud_Mask_QA/Cloud Mask Cloudiness flag. 
	 * The MODIS QA doc shows that the Total Precip Conf flag is in nits 2,3,4
	 * and the Cloudiness Flag is in bits 2,3. 
	 */
	/*
	 * Debugging statistical parameters:
	 */
	
	int masked = 0;
	
	
	 String[] direction = null;
	 DataFile file = null;
	 AggregateExpression baggage;
	 
	 /**
	  * 
	  * @param data the datavariable being screened
	  * @param qual the array of screening variables being used in the screening
	  * @param File the hdf DataFile object which has opened the hdf input file and contains
	  * many of the class members being used.
	  * @param directory not sure why I need this.
	  */
	public ExpressionMask(AggregateExpression he, String directory) {
		super(he,directory);
		file = he.file;
		baggage = he;
		if  (file.get_direction() != null) {
			direction = new String[file.get_direction().size()];
		}
		else {
			logger.error("No direction obtained for " + data.getName());
		}
	}

	 /**
	  *  MOD05_L2 is so fast we can live with reflection
	  */
	 public void doMask_reflect() throws Exception
	  {
		
		  doMask();
	  }
	 /**
	  * This code is the business logic for matching up dimensions and applying the
	  * screening to the datavariable.
	  * 
	  */
	public void doMask() throws Exception {
		
    
    	if (quality != null ) {
    		for (int  i = 0 ; i < quality.length; ++i) {
    			logger.debug("qa1:" + quality[i].getName());
    		}
    	}
    	else {
    		logger.error("No quality variables passed");
    	}
    	
    	/*
    	 * Set up all artifacts needed to run through the expression for each pixel.
    	 * reflection methods, dimension alignment
    	 */
    	baggage.prepQualityView();
 
    	/**
    	 * create 1-2 or 3 quality variables that are being applied to the
    	 * single data variable
    	 */
    	/* For example (MLS) dvqvDimPairings that look like:
    	 * 0  1  -1
    	 * 0 -1  -1	Status
    	 * 0  1  -1 H2O
    	 * 0 -1  -1 Status
    	 * -1 0  -1 Pressure
    	 * 0 -1  -1 Status
    	 * 0 -1  -1 Convergence
    	 * 0  1  -1 L2gpPrecision
    	 * -1 0  -1 Pressure
    	 * 0 -1  -1 H2O
    	 * The -1's in the last column just mean that there are no extra dimensions. See notes with matchDimensions method
    	 * Otherwise this means that Status Dim[0] matches H2O (the DV) Dim[0] 
    	 * H2O dims match H2O dims
    	 * Convergence dim[0] matches H2O dim[0]
    	 * L2gpPrecision dims match H2O dims
    	 * Pressure dim[0] matches H2o dim[1] (which is not true - which is why I am writing these notes -
    	 * as it turns out... Pressure dim[0] matches H2O dim[1] so this array is correct!)
    	 */
    	List<int[]> dvqvDimPairings = matchDimensions(baggage.file.DF,baggage.QSVars, baggage.sfs);
    	
    	//DQSSNumber[] qaData = new DQSSNumber[baggage.sfs.size()];
    	HashMap<String,DQSSNumber> qaData = new HashMap<String,DQSSNumber>();
    	
 
      
    	DQSSNumber fill = MaskFactory.getFillValue();
    	/*
    	 * This organizes the dimensions of our datafield and qualityfields 
    	 * For example, for 1 datafield and 2 qualityfields
    	 * an array of 1,0,2 means that dimension 1 of the datafield matches
    	 * dimension 0 of the quality[0] and dimension 2 of quality[1]
    	 */
    	//List<int[]> hash = matchDimensions(file.DF,quality);
    	/*
    	 * We need to reconfigure the dimensions BEFORE we start reading them one at a time...
    	 */
    	
    	int[] DFdims = new int[data.getRank()];
       	long[] DFsizes = new long[data.getRank()];
        for (int i=0; i < data.getRank(); ++i) {
        	/*
        	 * dvqvDimPairings.get(i)[0] contains the order in which we want to read the datavariable.
        	 * (if, originally the dv dimsizes are (2,135,203), we want to read it like (134,203,2)
        	 * and dvqvDimPairings.get(i)[0] contains this order
        	 */
    		DFdims[i] = dvqvDimPairings.get(i)[0];
    		DFsizes[i] =  data.getDims()[DFdims[i]]; //match up proper sizes
    		
    	}
    
    	long[] start = data.getStartDims();
    	/*
    	 * This is where the magic is done. where we make Dataset.read() read 
    	 * a vertical slice, say, instead of just the first layer.  That is
    	 * read perhaps (*,*,0) instead of (0,*,*) Or put another way, if 
    	 * datafield = 7 x 203 x 135 (This is clearly "incorrectly" organized in the sense
    	 * that the 7 layers will be shown in hdfview as rows) and your
    	 * qualityfield is (203 x 135 x 5)
    	 */
    	/*
    	 * the last item in the (dvqvDimPairings.size()-1) contains the dimension indices that didn't match up
    	 * therefore these are the depth indices for their respective Datasets
    	 */
    	int[] nonmatchingDims = dvqvDimPairings.get(dvqvDimPairings.size()-1);
        //file.checkThatDataWasWrittenToHDFLayer(file.DF.type, data, -1,1);
    	DQSS.reorganizeDimensions(data,DFdims,nonmatchingDims[0]);
    	//file.checkThatDataWasWrittenToHDFLayer(file.DF.type, data, -1,-1);
    	/*
    	 * Do the same for each screening field:
    	 * For now we are assuming one quality field
    	 * 
    	 */
    	
    	int[][]  sfdims = new int[baggage.QualityCount][dvqvDimPairings.size()];
       	long[][] sfsizes = new long[baggage.QualityCount][dvqvDimPairings.size()];
        for (int nSFs =0; nSFs < baggage.QualityCount; ++nSFs) {
        	for (int ndim =0; ndim < dvqvDimPairings.size(); ++ndim) {
        		sfdims[nSFs][ndim]  =  dvqvDimPairings.get(ndim)[nSFs+1];
        		if (quality[nSFs].getRank() > ndim) {
        			sfsizes[nSFs][ndim] =  quality[nSFs].getDims()[ndim];
        		}
        		else {
        			sfsizes[nSFs][ndim] =  -1;
        		}
        	}
    	}
      
    	/*
    	 * So far, the only way that I know how to find my screening flag is that
    	 * it is in the z direction of however I end up reading it.
    	 * 
    	 */
        for (int nSFs=0; nSFs < baggage.QualityCount; ++nSFs) {	
        	
        	DQSS.reorganizeDimensions(quality[nSFs],sfdims[nSFs],nonmatchingDims[nSFs+1]);
        }
       	/*
    	 * Since Dataset.read() must be cast to the proper type we are
    	 * forced to use populateWithType() to fill DQSSNumber.
    	 */
        Iterator it = baggage.sfs.entrySet().iterator();
        int i = 1;
	    while (it.hasNext()) {
	    	
	        
	        Map.Entry qpair = (Map.Entry)it.next();
	        screeningfield sf = (screeningfield) qpair.getValue();
	        Dataset q = (Dataset) baggage.quality.get(sf.getName());
	        
	        HashMap<String,String> dvdim = new HashMap<String,String>();  
			baggage.file.determineDatatype(q,dvdim);
			String qtype =  dvdim.get("type");
			if (baggage.sfs.get((String) qpair.getKey()).getRank() == 1) {
				screeningfield qfield = baggage.sfs.get((String) qpair.getKey());
				/*
				 * A match below means that the quality field's single dimension matches the number of rows
				 * in the datavariable. This means that the quality field's single value for each of it's rows
				 * should be applied to that of all of the values in the datavariable's row.
				 */
				if (dvqvDimPairings.get(0)[i] == dvqvDimPairings.get(0)[0]) { 
					qfield.set1DType("row");
				}
				else if ( dvqvDimPairings.get(1)[i] == dvqvDimPairings.get(0)[0] ) {
					qfield.set1DType("col");
					
				}
				else {
					logger.error("No col or row matchup for 1D quality variable:" + (String) qpair.getKey());
					System.exit(22);
				}
			}
			/*
    		 * This does the Dataset.read()
    		 * 
    		 */
	        qaData.put((String) qpair.getKey(), DQSS.populateWithType(q,qtype,false,
	        baggage.SF.get(sf.getName()).getFieldValueType().getHasStartLayerIndex()));
	        ++i;
        }
    	
    	/*
    	 * This allows us to increment through the third dimension designated in
    	 * reorganizeDimensions() (doing a Dataset.read()  each time)
    	 */
        int debugpassed=0;
    	/*
    	 * size of the Dataset.read() matrix
    	 */
    	long Ysize = DFsizes[0];
    	long Xsize = DFsizes[1];
	    long planeSize = Xsize * Ysize;
	    long npixels = planeSize;
	    layers = 1;
	    if (DFdims.length > 2) {
	    	layers = DFsizes[DFdims.length-1]; // number of layers in the 'proper' z direction
	    	npixels *= DFsizes[DFdims.length-1]; //plane size * z direction
	    }
    	logger.debug("DV: " + dataVariable + " X:" + DFdims[0] + ":" + Ysize + 
    			    " Y:" + DFdims[1] + ":" + Xsize +
    			    " Z:" + layers );

    	/*
    	 * Each 'layer' each of the third dimensions.
    	 */
    	for (int layer = 0; layer < layers ; ++layer) {
    		if (start.length > 2) {
        		start[DFdims[DFdims.length-1]] = layer;
        		//start[2] = layer; That is normally, in a 3D dataset the z direction is 2 but MODIS has unusually arranged datasets
            }
    		/*
    		 * This does the Dataset.read()
    		 */
    	    DQSSNumber dataData = DQSS.populateWithType(data, baggage.file.DF.getDatatype(),false,0);
    	    //short[] check = (short[]) data.read();
    	    stats.setpixels(Xsize, Ysize);
    	    int ith = 0;
 
    	    /*
    	     * Now, down to business on the layer at hand:
    	     */
    	    for (int row = 0; row < Ysize ; ++row) {     // remember rows are really vertical
    	    	// This is significant for MLS because it has data values which apply to an entire row.
    	    	//logger.debug("row:" + row + "data:" + dataData.getArray((int)(row * Xsize)) + " quality:" +
    	    	//qaData.get("H2O_QualityLevel").ArrayValue((int)(row )) 		);
    	    	/*
    	    	 * Note for MLS the quality value is a float not a byte and only one col
    	    	 */
        	    for (int col = 0; col < Xsize ; ++col) { // remember cols are really horizontal
        	    	ith = (int) ((row * (Xsize)) + col);
        	    	
    	            if (dataData.compare(fill.doubleValue(), ith) ) {
    	            	/*
    	            	 * fill value in data so skip it.
    	            	 */
	    					stats.countfill();
	    					dataData.setArray(ith, fill.doubleValue());
	    		    }
	    		    else  {
	    		    	//logger.debug(dataData.ArrayValue(ith)); // remove this later
	    		    	boolean screenit = baggage.TraverseExpression( qaData, ith, row, col);
	    		    	
	    		       if (screenit) {
	    		    	   ++masked;
	    		    	   dataData.setArray(ith, fill.doubleValue());
	    		       }
	    		       else {
	    		    	   dataData.setArray(ith, 1);
	    		    	   stats.inc();
	    		       }
			     		
	    		    }//if fill value
        	    }//cols
    	    }//rows
    	   
    	   
    	    baggage.DisplayDebugStats();
    	    logger.info("Layer:" + layer + " Masking: pixel total:" + masked);
    	    logger.info("Number of 1's in mask:" + stats.kept);
    	    dataData.reInstate(data,file.DF);
    	}//foreach layer 
    	stats.setTotal((int)npixels);
    	 //stats.publishQAFlags();		
 
	}
	
	/* Java internally uses (or emulates) twos complement arithmetic. 
	 * Positive numbers are stored in binary. That means that for example the number 13 would be written 
	 * 0x0d = 00001101 = 1*8 + 1*4 + 0*2 + 1*1. 
	 * The high order bit is called the sign bit. For signed quantities such as byte, short, int and long, 
	 * when that high order bit is a 1, we consider the number negative. 
	 * To represent a negative number we first invert each bit, then add 1. 
	 * We would write -13 this way: 0x0d -  00001101 -> 11110010 -> 11110011 -> 0xf3.
	 * 
	 * two's complement of a number is obtained by taking negative (-num)
	 * 
	 * If number is negative then bit 7 (see MODAPS Atmosphere QA Plan for 005)
	 * is 1. To get the rest of the numbers take the negative and and it.
	 */
	
	/* The Water Vapor (or Total Precipitable Water) product combines results from
       both the NIR (1x1 km) and IR algorithms (5 x 5 km). For near infrared (NIR) total
       precipitable water, the first byte contains cloud mask QA (1 x 1 km), and the second
       byte (a separate array from the cloud mask QA) contains NIR product quality and
       retrieval processing flags. 
       For the five bytes of IR total precipitable water results, only
       product quality and retrieval processing flags are stored. Since IR total precipitable
       water results are copied from 07_L2, the cloud mask related QA flags can be retrieved
       from 07_L2, and therefore it will not be duplicated here in the 05_L2 product. 
    */
	/* Quality_Assurance_Near_Infrared: Length: 1 byte (8 bits)
	   Flag Name						Number of Bits		Bit Values		Definitions
	   Total Precipitable Water (NIR)	1					0,1				Not useful, Useful
			   Usefulness flag
		Total Precipitable Water (NIRT) 3					0,1,2,3,		Bad(Fill) Marginal,Good,VG
		Confidence Flag
		Inversion Method Used (NIR)     2                   0,1,2           2 chan ratio, 3 chan ratio, no retrival
		Surface Type                    2					0,1,2,3			Bright Land, Clear Ocean, Cloud, Sunglint
		
	*/
	private boolean evaluateQualityAssuranceNearInfraredBits(Byte a) {
		//byte[] bits = {1,2,4,8,16,32,64,-128};
		byte[] bits = {-128,64,32,16,8,4,2,1};
		byte usefulnessMask = 1;
		byte surfaceMask    = -64 ; // and the sign of the byte.
		byte confidenceMask = 2 + 4 + 8;
		byte test = 32 + 64;
		byte alwayszeroMask = 8;
		Integer b = a & 0xFF;
		
		
		int surface = a & surfaceMask;
		if (surface == 64) {
			surface = 1;
		}
		else if (surface == -128) {
			surface = 2;
		}
		else if (surface == -64) {
			surface = 3;
		}
		
		int usefulness = a & usefulnessMask;
		
		/* Confidence */
		int confidence = a & confidenceMask;
		if (confidence == 6) {
			confidence = 3;
		}
		else if (confidence == 2) {
			confidence = 1;
		}
		else if (confidence == 4) {
			confidence = 2;
		}
		else  if (confidence == 0) {
			
		}
		else {
			confidence = 9999;
		}
		if (confidence > 1 && usefulness == 1) {
			return true;
		}
		return false;
}

	private void evaluateBits(Byte a, int count) {
		//byte[] bits = {1,2,4,8,16,32,64,-128};
		byte[] bits = {-128,64,32,16,8,4,2,1};
		byte usefulnessMask = 1;
		byte surfaceMask    = -64 ; // and the sign of the byte.
		byte confidenceMask = 2 + 4 + 8;
		byte test = 32 + 64;
		byte alwayszeroMask = 8;
		Integer b = a & 0xFF;
		int line = count / 270;
		int pixel = count - 270 * line;
		int surface = a & surfaceMask;
		if (surface == 64) {
			surface = 1;
		}
		else if (surface == -128) {
			surface = 2;
		}
		else if (surface == -64) {
			surface = 3;
		}
		//System.out.print(a  + " ->  " + b + " = " );
		/* alwayszero test 4th bit is not used as confidence Flag is 0-3 but uses 3 bits*/
		int alwayszero = count & alwayszeroMask;
		if (alwayszero == 0) {
			//System.out.print (count + ",");
		}
		else {
			//System.out.print (" ,");
		}
		/* Usefulness: */
		int usefulness = a & usefulnessMask;
		//logger.info ("Usefulness:" + usefulness);
		
		/* Confidence */
		int confidence = a & confidenceMask;
		if (confidence == 6) {
			confidence = 3;
		}
		else if (confidence == 2) {
			confidence = 1;
		}
		else if (confidence == 4) {
			confidence = 2;
		}
		else  if (confidence == 0) {
			
		}
		else {
			confidence = 9999;
		}
		logger.info ("Confidence:" + confidence + " Usefulness:" + usefulness + 
				" surface:" + surface + " cnt:" + count + " :" + line + ":" + pixel);
		for (int i = 0; i < 8; ++i) {
			Integer value = bits[i] & confidenceMask; // this gives the twos complement
			//System.out.print(value  + ", ");
			
		}
		/* this gives the two's complement of the stored number which is already the two's complement
		 * of the actual number so it's what we want -- except -- signed bit... You get value of this
		 * bit by whether number is positive or negative.
		 */
		if (confidence == 9999 || surface == 3) {
		for (int i = 0; i < 8; ++i) {
			if (i == 0 ) {
				System.out.print(a + ":");
			}
			Integer value = bits[i] & a; // this gives the twos complement
			System.out.print(value  + ", ");
			if (i == 7 ) {
				logger.info(" - " + count);		
			}
		}
		}
	}
	
}

package gov.nasa.gsfc.dqss;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ncsa.hdf.hdflib.HDFConstants;
import ncsa.hdf.hdflib.HDFException;
import ncsa.hdf.hdflib.HDFLibrary;

import ncsa.hdf.object.Dataset;
import org.apache.log4j.Logger;

public class SDDataFile {
    int sd_id;
    static Logger logger = Logger.getLogger(SDDataFile.class.getName());

	public SDDataFile () {} // for examples like getFillValue which open the files on their own.
	
	public SDDataFile(String inputFilename) throws HDFException {
			sd_id = HDFLibrary.SDstart(inputFilename, HDFConstants.DFACC_WRITE);
	}
	public SDDataFile(String inputFilename,String readonly) throws HDFException {
		sd_id = HDFLibrary.SDstart(inputFilename, HDFConstants.DFACC_READ);
    }
    public void destroy() throws HDFException{
		if (!HDFLibrary.SDend(sd_id)) {
			logger.warn("Error SDend");
		}
	}
	public void closeSDS(int sds_id) throws HDFException {
		if (!HDFLibrary.SDendaccess(sds_id)) {
			logger.warn("Error SDendaccess");
		}
	   
	}
	
	/* for experimentation
	 * 
	 */
	public int getID() {
		return sd_id;
	}
	private int openSDS(String sdsname) throws HDFException {
		
		int some_id = HDFLibrary.SDnametoindex(sd_id, sdsname);
		int  sds_id = HDFLibrary.SDselect(sd_id, some_id);
		if (sds_id < 0) {
			logger.warn("Error opening SDS:" + sdsname);
			
		}
		return sds_id;
	}
	
	private int getRankFromGetInfo(int sds_id) throws HDFException {
		 String[] sdsname={""}; 
		/*
		 *  I should be able to get the rank from somewhere before this. 
		 *  For now this seems to work for < 5 also
		 */
	     int[] sizes = new int[5]; 
	     
		 int[] args = new int[3];
	     logger.debug("SDS ID:" + sds_id);
		 HDFLibrary.SDgetinfo(sds_id, sdsname, sizes, args);
		 for (int i = 0; i < args[0]; ++i) {
			 logger.debug(sdsname[0] + " dimsize:" + sizes[i]);
			
		 }
		 
		 logger.debug(sdsname[0] + " rank:    "     + args[0]);
		 logger.debug(sdsname[0] + " datatype:"     + args[1]);
		 logger.debug(sdsname[0] + " nAttributes:"     + args[2]);
		 return args[0];
	}
	
	public String[] GetDimensionNames(String name) 
	{
	    boolean status;
	    String[] dimnames = null;
	    logger.debug("Getting Dimension Names from:" + name);
		try {
			int sds_id = openSDS(name);
			if (sds_id < 0) {
				logger.error("openSDS failed for " + name );
				return null;
			}
			int rank = getRankFromGetInfo(sds_id);
			dimnames = new String[rank];
			for (int dimindex=0; dimindex < rank; ++dimindex) {
				
			  	int dim_id = HDFLibrary.SDgetdimid (sds_id, dimindex);
		  		if (dim_id > 0) {
			  		String[] dimname = {""};
			  		int[] args = new int[3];
			  		HDFLibrary.SDdiminfo(dim_id, dimname, args);
			  		logger.debug("dimname:" + dimname[0] + ", " + dim_id);
			  	    dimnames[dimindex] =  dimname[0];
		  	    
		  		}
		  		else {
		  			logger.debug("SDgetdimid("+sds_id+","+dimindex+") failed");
		  		}
		  	
			  		
			  }
			closeSDS(sds_id);
			
			} catch (HDFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return dimnames;
	}
	
	public void SetFill(String name,DQSSNumber fill) throws HDFException {
		 int sds_id = openSDS(name);
		 boolean status = false;
		 if (sds_id < 0) {
				logger.error("openSDS failed for " + name );
			}
		 String dtype = getDatatype(sds_id);
		 if (dtype.contains("FLOAT32")) {
			 Float[] f = new Float[1];
			 int count = 1;
			 f[0] = fill.floatValue();
			 status = HDFLibrary.SDsetfillvalue(sds_id, f);
		 }
		 if (dtype.contains("INT16")) {
			 Short[] f = new Short[1];
			 int count = 1;
			 f[0] = fill.shortValue();
			 status = HDFLibrary.SDsetfillvalue(sds_id, f);
		 }
		 
		 logger.debug("SDsetfillvalue returned:" + status + " for " + name + ", " + fill);
		 closeSDS(sds_id);
	}
	public void SetFillValueAttribute(String name,String attr_name, Float fill) throws HDFException {
		int sds_id = openSDS(name);
		if (sds_id < 0) {
			logger.error("openSDS failed for " + name );
		}
		byte[] data = new byte[1];
		int num_type = 5; // string is 4 float32 is 5
		
		data[0] = fill.byteValue();
		Float[] f = new Float[1];
		int count = 1;
		f[0] = fill;
		boolean status;      
		status = HDFLibrary.SDsetfillvalue(sds_id, f);
		//if ( status = HDFLibrary.SDsetattr(sds_id, attr_name, num_type, count, data)) {
		//	logger.debug("SDsetattr:" + name + ":" + attr_name + ":" + fill + "returned" +  status);
		//}
		 closeSDS(sds_id);
	
	}
	
	 public  byte[] floatToByteArray(float f) {
		         int i = Float.floatToRawIntBits(f);
		   return intToByteArray(i);
    }
	 public  byte[] intToByteArray(int param) {
		  final int MASK = 0xff;
		 	        byte[] result = new byte[4];
		 	        for (int i = 0; i < 4; i++) {
		 	            int offset = (result.length - 1 - i) * 8;
		 	            result[i] = (byte) ((param >>> offset) & MASK);
		 	        }
		 	        return result;
    }
	/*
	 * I named this GetFillValue instead of a more general GetAttribute
	 * because of the datatype of data below:
	 */
	public DQSSNumber getFillValue(String sdsname, String attr_name) throws HDFException
	{
		int sds_id = openSDS(sdsname);
		if (sds_id < 0) {
			logger.error("openSDS failed for " + sdsname + " return default" );
			DQSSNumber n = new DQSSNumber(-9999);
			return n;
		}
		String dtype = getDatatype(sds_id);
		
		float fillvalue = -9999;
		int attr_index = HDFLibrary.SDfindattr(sds_id, attr_name);
		if (attr_index >= 0) {
			int[] args = new int[2];
			String[] out_attr_name = {""};
			if (dtype.contains("FLOAT32")) {
				
				Float[] data = new Float[1];
				
				if (! HDFLibrary.SDreadattr(sds_id, attr_index, data)){
					logger.warn("SDreadattr failed for " + attr_name + " in " + sdsname);
					
				}
				else {
					logger.debug(sdsname + "'s " + attr_name + " is " + data[0]);
					fillvalue = data[0];
				}
			}
			else if (dtype.contains("INT16")) {
				
				Short[] data = new Short[1];
				
				if (! HDFLibrary.SDreadattr(sds_id, attr_index, data)){
					logger.warn("SDreadattr failed for " + attr_name + " in " + sdsname);
					
				}
				else {
					logger.debug(sdsname + "'s " + attr_name + " is " + data[0]);
					fillvalue = data[0];
				}
			}
		}
		else {
			logger.warn("SDfindattr failed for " + attr_name + " in " + sdsname);
		}
		
		closeSDS(sds_id);
		return new DQSSNumber(fillvalue);
	}
	
	public void GetListOfAttributeNames(String sdsname, HashMap<String,Integer[]> sizes) throws HDFException {
		int sds_id = openSDS(sdsname);
		if (sds_id < 0) {
			logger.error("openSDS failed for " + sdsname + " return default" );
			return;
		}
		
		boolean attr_info = true;
		int[] typesize = new int[2];
		int attr_index = 0;
		String[] attr_name={""};
		
		while (attr_info) {
			/*
			 * This returns the attribute name, datatype and string length
			 */
		   attr_info = HDFLibrary.SDattrinfo(sds_id, attr_index, attr_name, typesize);
		   if (attr_info) {
			   Integer[] hashint  = new Integer[2];
			   hashint[0] = typesize[0];
			   hashint[1] = typesize[1];
			   
			   sizes.put(attr_name[0], hashint);
			   logger.debug("Found attribute:" + attr_name[0]);
		   }
		   ++attr_index;
		}
		
	}
	public Object GetAttribute(String sdsname, String attr_name, Integer[] typesize) throws HDFException
	{
		int sds_id = openSDS(sdsname);
		if (sds_id < 0) {
			logger.error("openSDS failed for " + sdsname + " return default" );
			
			return null;
		}
		Object out = null;
		String[] sds_name={""};
		int[] sizes = new int[5];
	    int[] args = new int[3];
		HDFLibrary.SDgetinfo(sds_id, sds_name, sizes, args);
		 
		int attr_index = HDFLibrary.SDfindattr(sds_id, attr_name);
		if (attr_index >= 0) {
			if (typesize[0] == 4) {
				out = getStringAttribute(sdsname,attr_name,sds_id,attr_index,typesize[1]);
			}
			else if (typesize[0] == 22) {
				 out = getShortAttribute(sdsname,attr_name,sds_id,attr_index,typesize[1]);
			}
			else if (typesize[0] == 24) {
				 out = getIntegerAttribute(sdsname,attr_name,sds_id,attr_index,typesize[1]);
			}
			else if (typesize[0] == 6) {
				 out = getDoubleAttribute(sdsname,attr_name,sds_id,attr_index,typesize[1]);
			}
			else if (typesize[0] == 5) {
				 out = getFloatAttribute(sdsname,attr_name,sds_id,attr_index,typesize[1]);
			}
			else  {
				 logger.error("Trying to find an attribute with type:" + typesize[0] + ". But not found");
			}
		}
		else {
			logger.warn("SDfindattr failed for " + attr_name + " in " + sdsname);
		}
		
		closeSDS(sds_id);
		return out;
	}	
	private Object getStringAttribute(String sdsname, String attr_name,
			int sds_id,int attr_index,Integer size) throws HDFException 
	{
		byte data[] = new byte[size];
		String out = null;
		if (! HDFLibrary.SDreadattr(sds_id, attr_index, data)){
			
			return null;
		}
		out = new String(data);
		logger.debug(sdsname + "'s " + attr_name + " is " + out + "");
		return out;
	}
	private Object getShortAttribute(String sdsname, String attr_name,
			int sds_id,int attr_index, Integer size) throws HDFException 
	{
		short data[] = new short[size];
		Short[] out = new Short[size];
		if (! HDFLibrary.SDreadattr(sds_id, attr_index, data)){
			return null;
		}
		for (int i = 0; i < size; ++i) {
			out[i] = data[i];
		}
		return out;
	}
	private Object getIntegerAttribute(String sdsname, String attr_name,
			int sds_id,int attr_index, Integer size) throws HDFException 
	{
		int data[] = new int[size];
		Integer[] out = new Integer[size];
		if (! HDFLibrary.SDreadattr(sds_id, attr_index, data)){
			return null;
		}
		for (int i = 0; i < size; ++i) {
			out[i] = data[i];
		}
		return out;
	}
	private Object getDoubleAttribute(String sdsname, String attr_name,
			int sds_id,int attr_index, Integer size) throws HDFException 
	{
		double data[] = new double[size];
		Double[] out = new Double[size];
		if (! HDFLibrary.SDreadattr(sds_id, attr_index, data)){
			return null;
		}
		for (int i = 0; i < size; ++i) {
			out[i] = data[i];
		}
		return out;
	}
	private Object getFloatAttribute(String sdsname, String attr_name,
			int sds_id,int attr_index, Integer size) throws HDFException 
	{
		float data[] = new float[size];
		Float[] out = new Float[size];
		if (! HDFLibrary.SDreadattr(sds_id, attr_index, data)){
			return null;
		}
		for (int i = 0; i < size; ++i) {
			out[i] = data[i];
		}
		return out;
	}
	private String getDatatype(int sds_id) throws HDFException {
		 String[] sdsname={""}; 
		 int[] sizes = new int[5];
	     int[] args = new int[3];
		 HDFLibrary.SDgetinfo(sds_id, sdsname, sizes, args);
		 for (int i = 0; i < 3; ++i) {
			 logger.debug(sdsname[0] + " dimsize:" + sizes[i]);
			
		 }
		 if (args[1] == 5) {
			 return "FLOAT32";
		 }
		 else if (args[1] == 22) {
			 return "INT16";
		 }
		 else {
			 logger.error("Could not determine datatype");
			 return "not defined yet";
		 }
		 /*
		 DFNT_UCHAR		 3
		 DFNT_CHAR		 4
		 DFNT_FLOAT32	 5
		 DFNT_FLOAT64	 6
		 DFNT_INT8		 20
		 DFNT_UINT8		 21
		 DFNT_INT16		 22
		 DFNT_UINT16	 23
		 DFNT_INT32		 24
		 DFNT_UINT32	 25
		 DFNT_INT64		 26
		 DFNT_UINT64	 27
		 */
	}
	public short getModisFillValue(String sdsname, String attr_name) throws HDFException
	{
		int sds_id = openSDS(sdsname);
		if (sds_id < 0) {
			logger.error("openSDS failed for " + sdsname + " return default" );
			return -9999;
		}
		short fillvalue = -9999;
		int attr_index = HDFLibrary.SDfindattr(sds_id, attr_name);
		if (attr_index >= 0) {
			int[] args = new int[2];
			String[] out_attr_name = {""};
			short[] data = new short[1];
			
			if (! HDFLibrary.SDreadattr(sds_id, attr_index, data)){
				logger.warn("SDreadattr failed for " + attr_name + " in " + sdsname);
				return fillvalue;
			}
			else {
				logger.debug(sdsname + "'s " + attr_name + " is " + data[0]);
				fillvalue = data[0];
			}
		}
		else {
			logger.warn("SDfindattr failed for " + attr_name + " in " + sdsname);
		}
		
		closeSDS(sds_id);
		return fillvalue;
	}
	
	/* 
	 * Used by Screener Only
	 */
	public DQSSNumber[] getFillValues(Dataset[] datavars) throws HDFException {
		DQSSNumber[] FillValues =  new DQSSNumber[datavars.length];
		for (int i = 0; i < datavars.length; ++i) {
			String sdsname = datavars[i].getName();
			FillValues[i] = (getFillValue(sdsname,"_FillValue"));
		}
		return FillValues;
	}
	public void SetAttribute(String name,String attr_name, String stringvalue) throws HDFException {
		int sds_id = openSDS(name);
		if (sds_id < 0) {
			logger.error("openSDS failed for " + name );
		}
		int num_type = 4; // string is 4 float32 is 5
		int count = stringvalue.length();
		byte[] values = new byte[stringvalue.length()];
		values = stringvalue.getBytes();
		boolean status;      
		if ( status = HDFLibrary.SDsetattr(sds_id, attr_name, num_type, count, values)) {
			logger.debug("SDsetattr:" + name + ":" + attr_name + ":" + stringvalue + "returned" +  status);
		}
		 closeSDS(sds_id);
	
	}
	public void SetAllAttributes(String name , String attr_name ,Integer[] dimensions, Object attributevalue) throws HDFException
		{
			int sds_id = openSDS(name);
			if (sds_id < 0) {
				logger.error("openSDS failed for " + name );
			}
			int num_type = dimensions[0]; // string is 4 float32 is 5
			boolean status;
			if (num_type == 4) {
					
				String stringvalue = (String) attributevalue;
				int count = dimensions[1];
				byte[] values = new byte[stringvalue.length()];
				values = stringvalue.getBytes();
				      
				if ( status = HDFLibrary.SDsetattr(sds_id, attr_name, num_type, count, values)) {
				logger.debug("SDsetattr:" + name + ":" + attr_name + ":" + stringvalue + "returned" +  status);
				}
			}
			if (num_type == 22) {
				
				Short[] values = (Short[]) attributevalue;
				int count = dimensions[1];
				
				if ( status = HDFLibrary.SDsetattr(sds_id, attr_name, num_type, count, values)) {
					logger.debug("SDsetattr:" + name + ":" + attr_name + ":" + "returned" +  status);
				}
			}
			if (num_type == 24) {
				
				Integer[] values = (Integer[]) attributevalue;
				int count = dimensions[1];
				
				if ( status = HDFLibrary.SDsetattr(sds_id, attr_name, num_type, count, values)) {
					logger.debug("SDsetattr:" + name + ":" + attr_name + ":" + "returned" +  status);
				}
			}
			if (num_type == 6) {
				
				Double[] values = (Double[]) attributevalue;
				int count = dimensions[1];
				
				if ( status = HDFLibrary.SDsetattr(sds_id, attr_name, num_type, count, values)) {
					logger.debug("SDsetattr:" + name + ":" + attr_name + ":" + "returned" +  status);
				}
			}
			if (num_type == 5) {
				
				Float[] values = (Float[]) attributevalue;
				int count = dimensions[1];
				
				if ( status = HDFLibrary.SDsetattr(sds_id, attr_name, num_type, count, values)) {
					logger.debug("SDsetattr:" + name + ":" + attr_name + ":" + "returned" +  status);
				}
			}
		 closeSDS(sds_id);
	
	}
	 public int byteArrayToInt(byte[] b, int offset) {
	        int value = 0;
	        for (int i = 0; i < 4; i++) {
	            int shift = (4 - 1 - i) * 8;
	            value += (b[i + offset] & 0x000000FF) << shift;
	        }
	        return value;
	    }

	

	public void UpdateDimensionNames(String name , String[] newnames)
	{
     logger.debug("Updating Dimension Names of:" + name);	
	 try {
		 int sds_id = openSDS(name);
		 if (sds_id < 0) {
				logger.error("openSDS failed for " + name );
		 }
		 int rank = getRankFromGetInfo(sds_id);
	    
		boolean status = false;
		for (int dimindex=0; dimindex < rank; ++dimindex) {
		  	int dim_id = HDFLibrary.SDgetdimid (sds_id, dimindex);
		  	status = HDFLibrary.SDsetdimname (dim_id, newnames[dimindex]  );
		  	if (status) { 
		  		String[] dimname = {""};
		  		int[] args = new int[3];
		  		HDFLibrary.SDdiminfo(dim_id, dimname, args);
		  		logger.debug("I set dimname to:" + dimname[0]);
		  	}
		  	else {
		  		logger.debug("SDsetdimname failed");
		  	}
		  	 
		  	 
		  }
		  closeSDS(sds_id);
		} catch (HDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	
	public void CreateVData(String datasetName, String classname) {
	    String[] vDatasetName = {""};
	    String[] vClassName = {""};
		try {
			short databuf[] = new short[1];
			databuf[0] = -9999;
			
			int vdata_ref = HDFLibrary.VHstoredata(sd_id, "AttrValues", databuf, 1, 22, datasetName, classname);
		
            System.out.println(vdata_ref);
			//status = HDFLibrary.VSsetinterlace(vdata_id, 0);


			
		} catch (HDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


  }
}

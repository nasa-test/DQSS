package gov.nasa.gsfc.dqss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdflib.HDFException;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.h5.H5ScalarDS;
import ncsa.hdf.object.h4.*;

public class ExpressionDataFile extends DataFile {
    

	
	public ExpressionDataFile(String inputFile) {
		super(inputFile);
		// TODO Auto-generated constructor stub
	}
	public ExpressionDataFile(String inputFile, String outputFile) {
		super(inputFile,outputFile);
		// TODO Auto-generated constructor stub
	}

	public ExpressionDataFile(String inputFile, int doSD) {
		super(inputFile, doSD);
		// TODO Auto-generated constructor stub
	}

	 public List<DQSSNumber> getHEFillValues( List<String> params) {
		 List<DQSSNumber> FillValues = new ArrayList<DQSSNumber>();
		 for (int i = 0; i < params.size(); ++i) {
			 Attribute attr = getHE5Attribute("_FillValue", params.get(i));
			 if (attr == null) {
				 logger.error("getHE5Attribute call failed");
				 return null;
			 }
			 Datatype dt = attr.getType();
			    int type = dt.getDatatypeClass();
			    if (type == Datatype.CLASS_FLOAT) {
			       float[] attrvalue = new float[2];
			       attrvalue = (float[]) attr.getValue();
			      
			       logger.debug("Attribute " + attr.getName() + " value:" + attrvalue[0]  );
				   FillValues.add(new DQSSNumber(attrvalue[0]));
			    }
			    else if (type == Datatype.CLASS_INTEGER) {
				       int[] attrvalue = new int[2];
				       attrvalue = (int[]) attr.getValue();
				       logger.debug("Attribute " + attr.getName() + " value:" + attrvalue[0]  );
					   FillValues.add(new DQSSNumber(attrvalue[0]));
				    }
			
		 }
		 return FillValues;
	 }
	 public List<DQSSNumber> getH4FillValues( List<String> params) {
		 List<DQSSNumber> FillValues = new ArrayList<DQSSNumber>();
		 for (int i = 0; i < params.size(); ++i) {
			 Attribute attr = getH4Attribute("_FillValue", params.get(i));
			 if (attr == null) {
				 logger.warn("getH4Attribute call failed. This can happen sometimes because not all MOD04" +
				 		" have the deep blue variables. So we need to skip it and move on to the next.");
				 FillValues.add(new DQSSNumber(-9999));
			 }
			 else {
			 Datatype dt = attr.getType();
			    int type = dt.getDatatypeClass();
			    if (type == Datatype.CLASS_FLOAT) {
			       float[] attrvalue = new float[2];
			       attrvalue = (float[]) attr.getValue();
			      
			       logger.debug("Attribute " + attr.getName() + " value:" + attrvalue[0]  );
				   FillValues.add(new DQSSNumber(attrvalue[0]));
			    }
			    else if (type == Datatype.CLASS_INTEGER) {
				       short[] attrvalue = new short[2];
				        attrvalue = (short[]) attr.getValue();
				       logger.debug("Attribute " + attr.getName() + " value:" + attrvalue[0]  );
					   FillValues.add(new DQSSNumber(attrvalue[0]));
				    }
			 }
		 }
		 return FillValues;
	 }
	 
	 public Attribute getH4Attribute(String attr, String DatasetName) {

			Dataset d = getDataset(DatasetName, topGroup);
				H4SDS dh4 = (H4SDS) d;
				if (d == null) {
					logger.error("Could not find dataset:" + DatasetName);
					return null;
				}
		 	    List<Attribute> list = new ArrayList<Attribute>();
		 	    try {
					list = dh4.getMetadata();
				} catch (HDFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for (int j = 0; j < list.size(); ++j) {
					if (attr.equals( list.get(j).getName())) {
						return list.get(j);
					}
				}
				return null;
		}
	 
 /*
  * For populating _orig with all of the attributes in the original
  */
 public Attribute getHE5Attribute(String attr, String DatasetName) {

	Dataset d = getDataset(DatasetName, topGroup);
		H5ScalarDS dh5 = (H5ScalarDS) d;
		if (d == null) {
			logger.error("Could not find dataset:" + DatasetName);
			return null;
		}
 	    List<Attribute> list = new ArrayList<Attribute>();
		try {
			list = dh5.getMetadata();
		} catch (HDF5Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("HDF5 getMetadata() call failed for:" + attr + ", " + DatasetName );
		}
		for (int j = 0; j < list.size(); ++j) {
			if (attr.equals( list.get(j).getName())) {
				return list.get(j);
			}
		}
		return null;
}
 /*
  * For populating _orig with all of the attributes in the original
  */
 public List<Attribute> getAllHE5Attributes(String DatasetName) {

		Dataset d = getDataset(DatasetName, topGroup);
			H5ScalarDS dh5 = (H5ScalarDS) d;
			if (d == null) {
				logger.error("Could not find dataset:" + DatasetName);
				return null;
			}
	 	    List<Attribute> list = new ArrayList<Attribute>();
			try {
				list = dh5.getMetadata();
			} catch (HDF5Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("HDF5 getMetadata() call failed for " +  DatasetName );
				return null;
			}
			return list;
			
	}

/*
	  public void updateMember(List<String> list, String name,String castorValue) {

		  if (castorValue == null) {
			  return;
		  }
		  if (castorValue.length() > 0) {
			  list.add(castorValue);
			  logger.debug(name + 
					  " size:" + list.size() + " added value:" + castorValue + ".");
			  
			  if (castorValue.contains("CoordinateConstraintVariable")) {
		    		++nCoordCons;
		    		logger.debug("nCoordCons:"+ nCoordCons);
		      }
			  else if (castorValue.contains("QualityLevelVariable")) {
		    		++nQualLevel;
		    		logger.debug("nQualLevel:" + nQualLevel);
		    	}
			  else if (castorValue.contains("ReferenceConstraintVariable")) {
		    		++nConsRef;
		    		logger.debug("nConsRef:"+ nConsRef);
		    	}
			  else if (castorValue.contains("ReferenceVariable")) {
		    		++nRef;
		    		logger.debug("nRef:" + nRef);
		    	}
			  else if (castorValue.contains("constraint_relationship")) {
				  
			  }
		  }
	  }
	  public void updateLongMember(List<Long> list, String name, String castorValue) {
		  logger.debug(name + ":" + castorValue);
		   if (castorValue != null) {
			   if (castorValue.length() > 0) {
				   long x = Long.parseLong(castorValue);
				   list.add(x);
				   logger.debug(name + 
							  " size:" + list.size() + " added value:" + castorValue + ".");
				  
				  
			   }
		   }
		 
	  }
	  public void updateLongMember(List<Long> list, String name, long castorValue) {
		  logger.debug(name + ":" + castorValue);
				   list.add(castorValue);
				   logger.debug(name + 
							  " size:" + list.size() + " added value:" + castorValue + ".");
	  }
	  */
	/*
	    public Dataset[] getQSVars() {
	 
	    	 
	    	Dataset[] qsDatasets = new Dataset[SFs.size()]; 
	    	
	    	logger.debug("Number of quality datasets:" + SFs.size());
	    
	    	qualityNames = "";
	    	for (int i = 0; i < SFs.size(); i++) {
	    		if (SFs.get(i) != null) {
	    	    	 logger.debug("Doing:" + SFs.get(i).getName());
	    	    	 qualityNames +=  SFs.get(i).getName() + " ";
	    	         qsDatasets[i] = getDataset(SFs.get(i).getName(), topGroup);
	    	         if (qsDatasets[i] == null) {
	    		        if (i < nDatasets) {
	    			         logger.error("Quality dataset:" + sf_names.get(i) + " does not exist in the hdf file");
	    		            return null;
	    		        }
	    	         }
	    	         if (qsDatasets[i] == null) {
	    	        	 logger.error("Quality dataset:" + sf_names.get(i) + " does not exist in the hdf file");
	    	        	 return null;
	    	         }
	    	         HashMap<String,String> dvdim = new HashMap<String,String>();  
	    	 		 try {
						determineDatatype(qsDatasets[i],dvdim);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SFs.addDatatype(dvdim.get("type"),i);
	    	         logger.debug("getQSVars:" + ((Dataset) qsDatasets[i]).getName());
	    	     }
	    	
	    	}	
	    	return  qsDatasets;
	  }
*/	  
	
}


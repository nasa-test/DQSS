package gov.nasa.gsfc.dqss;

import java.util.List;

import ncsa.hdf.hdflib.HDFException;
import ncsa.hdf.hdflib.HDFLibrary;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h4.H4File;
import ncsa.hdf.object.h4.H4Vdata;

import org.apache.log4j.Logger;

public class VDataManagement {
	
	  H4Vdata fileobj;
	  H4File file4;
	  private int fid;
	  private  FileFormat FFH4Instance  = null;
	  static Logger logger = Logger.getLogger(VDataManagement.class.getName());
	  int file_id;
	  int vgroup_id;
	  int vgroup_ref;
	  int vdata_id;
	  int vgdata_id;
	  String groupName = "Swath Attributes";
	  boolean status = false;
	  String classname = null;
	  String access;
	  
	  public VDataManagement(String inputFile, String access_mode) {
		  access = access_mode;
		  try {
			  file_id = HDFLibrary.Hopen(inputFile);

			  boolean status = HDFLibrary.Vstart(file_id);
			  if (status) {
				  vgroup_ref = HDFLibrary.Vfind(file_id, "Swath Attributes");
              
				  String vg_access_mode = access;
			      vgroup_id = HDFLibrary.Vattach(file_id, vgroup_ref, vg_access_mode);
                  vgdata_id  = HDFLibrary.Vgetid(file_id, vgroup_ref);
                
			  }
		  }
		  catch (Exception e) {
	        	logger.error("Problem with:" + inputFile );
	        	e.printStackTrace();
	        	return;
	        }
	  }
	  public String ReadSomeVData(String vdataset) 
	  {
          int tags[] = new int[200];
          int refs[] = new int[200];
          int iargs[] = new int[3];
          String sargs[] = new String[2];
          try {
	          int some_id = HDFLibrary.Vgettagrefs(vgroup_id, tags,refs,200);
	          // There are three valid version numbers: VSET_OLD_VERSION (or 2), VSET_VERSION (or 3), and VSET_NEW_VERSION (or 4)
	          int vver = HDFLibrary.Vgetversion(vgroup_id);
	          //HDFLibrary.Vattrinfo(vgroup_id, 0, attr_name, &data_type, &n_values, &size);
	          int i = 0;
	          String[] vDatasetName = {""};
	          String[] vClassName = {""};
	          
	          byte[] data = new byte[500];
	          while (refs[i] > 0) {
	        	  int vdata_id  = HDFLibrary.VSattach(file_id, refs[i], "r");
	        	  HDFLibrary.VSgetname(vdata_id, vDatasetName);
	        	  
	        	  if (vDatasetName[0].contains(vdataset)) {
	        		  System.out.println("vDatasetName:" + vDatasetName[0]);
	        		  HDFLibrary.VSgetattr(vdata_id, i,0, data);
	        		  System.out.println("vDataset data:" + data[0]);
	        		  status = HDFLibrary.VSinquire(vdata_id, iargs, sargs);
	        		  int record_pos = HDFLibrary.VSseek(vdata_id, 0);
	        		  status = HDFLibrary.VSsetfields(vdata_id,"AttrValues");
	        		  int n = HDFLibrary.VSread( vdata_id, data, record_pos,iargs[1]);
	        		  HDFLibrary.VSgetclass(vdata_id, vClassName);
	        		  System.out.println("Classname:"  + vClassName[0]);
	        		  classname = vClassName[0];
	        	  }
	        	  ++i;
			  }
          }
      	  catch (Exception e) {
	        	logger.error("Problem with:" + vdataset );
	        	e.printStackTrace();
	        	
	        }
      	return classname;
	  }
	  public void CreateVData(String datasetName, String classname) {
		    String[] vDatasetName = {""};
		    String[] vClassName = {""};
			try {
				short databuf[] = new short[1];
				databuf[0] = -9999;
				
				int vdata_ref = HDFLibrary.VHstoredata(file_id, "AttrValues", databuf, 1, 22, datasetName, classname);
			

				//status = HDFLibrary.VSsetinterlace(vdata_id, 0);
	
	
				HDFLibrary.Vend(file_id);
			} catch (HDFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	  }
	  public void CreateMultiFieldVData(String datasetName, String classname) {
		    String[] vDatasetName = {""};
		    String[] vClassName = {""};
			try {
				vdata_id  = HDFLibrary.VSattach(file_id, -1, "w");
			
				HDFLibrary.VSsetname(vdata_id, datasetName);
				HDFLibrary.VSgetname(vdata_id, vDatasetName);
				  HDFLibrary.Vgetclass(vgroup_id, vClassName);
				HDFLibrary.VSsetclass(vdata_id, classname);
				HDFLibrary.VSgetclass(vdata_id, vClassName);
				
				status = HDFLibrary.VSfdefine(vdata_id, "AttrValues", 22, 1);
				status = HDFLibrary.VSsetfields(vdata_id, "AttrValues");
				
				int record_index=1;
				int record_pos = HDFLibrary.VSseek(vdata_id, record_index);
				short databuf[] = new short[1];
				databuf[0] = -9999;
				int n_records = 1;
				int num_of_recs = HDFLibrary.VSwrite(vdata_id, databuf, n_records, 0);


				//status = HDFLibrary.VSsetinterlace(vdata_id, 0);
	
				HDFLibrary.VSdetach(vdata_id);
	
				HDFLibrary.Vend(file_id);
			} catch (HDFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	  }
	  
	  
	 public void close() 
	 {
		 try {
			HDFLibrary.Hclose(file_id);
		} catch (HDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
		 
	        
}

package gov.nasa.gsfc.dqss;

import gov.nasa.gsfc.Stopwatch;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdflib.HDFConstants;
import ncsa.hdf.hdflib.HDFException;
import ncsa.hdf.hdflib.HDFLibrary;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.ScalarDS;
import ncsa.hdf.object.h4.H4Datatype;
import ncsa.hdf.object.h4.H4File;
import ncsa.hdf.object.h4.H4SDS;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5ScalarDS;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: Reading HDF4 Files with JAVA HDF4 Dataset class
 * </p>
 * <p>
 * Description: This class will: 1. Read in a file. 2. Read in 3 parameters 3.
 * Call the visualizer 4. Apply a simple quality dataset to it's parameter 5.
 * Write out the new datasets 6. Insert the visualized jpeg.
 * 
 * <pre>
 *     "not sure what I should put here yet.
 * </pre>
 * 
 * </p>
 * 
 * @author Richard Strub GES-DISC
 * @version 1.0
 */
public class DataFile {

	/*
	 * Constructor that does 3D TSurfAirStd and TAirMWOnlyStd
	 * w/Qual_MW_Only_Temp_Strat and Tropo
	 * 
	 * main() is at bottom, only constructor so far is at top
	 */
	public String FileType = null;
	public Group topGroup = null;
	private H4File file4obj = null;
	private H5File file5obj = null;
	String SDFileName = null;
	private int fid;
	protected FileFormat FFH4Instance = null;
	private int sd_id = 0;
	protected long rows;
	int nQualityLevelDatasets = 0;
	int nCoordinateConstraintDatasets = 0;
	int nReferenceDatasets = 0;
	int nConstraintReferenceDatasets = 0;
	int nDatasets = 0;
	List<String> qNames = new ArrayList<String>();
	List<String> qTypes = new ArrayList<String>();
	List<String> qRefTypes = new ArrayList<String>();
	List<String> sf_names = new ArrayList<String>();
	List<String> ref_names = new ArrayList<String>();
	List<String> dims = new ArrayList<String>();
	List<Long> mins = new ArrayList<Long>();
	List<Long> maxs = new ArrayList<Long>();
	List<String> sf_types = new ArrayList<String>();
	List<String> sf_reftypes = new ArrayList<String>();
	List<String> qualev_direction = new ArrayList<String>();
	List<String> sf_cnrel = new ArrayList<String>();
	int nCoordCons;
	int nQualLevel;
	int nConsRef;
	int nRef;
	List<Long> crit2num = new ArrayList<Long>();
	public String qualityNames;
	static String HDFType = "HDF4";
	static Logger logger = Logger.getLogger(DataFile.class.getName());
	//ScreeningFields SFs;
	datafield DF; // for screener. gets instantiated by AggregateExpression.

	// public AggregateExpression he;
	public DataFile(String inputFile) {
		/*
		 * Deprecated. As of 2.4, replaced by createFile(String, int)
		 * 
		 * The replacement method has an additional parameter that controls the
		 * behavior if the file already exists. Use
		 * FileFormat.FILE_CREATE_DELETE as the second argument in the
		 * replacement method to mimic the behavior originally provided by this
		 * method.
		 */
		try {
			FFH4Instance = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4);
			logger.warn("HDF Lib Version:" + FFH4Instance.getLibversion());

			file4obj = (H4File) FFH4Instance.createInstance(inputFile,
					FileFormat.WRITE);

			logger.debug("obtained H4File:" + file4obj.getName());
			FileType = file4obj.getName();
			fid = file4obj.open();
			logger.debug("Opened H4File:" + file4obj.getName());
			logger.warn("File:" + inputFile);
		} catch (Exception e) {
			logger.error("Problem with:" + inputFile);
			e.printStackTrace();
			return;
		}
		SDFileName = inputFile;

		if (file4obj != null) {
			topGroup = (Group) ((javax.swing.tree.DefaultMutableTreeNode) file4obj
					.getRootNode()).getUserObject();

		} else {
			return;
		}

	}

	/**
	 * 
	 * @param inputFile
	 * @param outputFile
	 */
	public DataFile(String inputFile, String outputFile) {
		SDFileName = inputFile;
		try {
			FFH4Instance = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4);
			if (FFH4Instance != null) {
				logger.warn("HDF Lib Version:" + FFH4Instance.getLibversion());
				String[] vstr = { "" };
				int[] vargs = new int[3];
				HDFLibrary.Hgetlibversion(vargs, vstr);
				logger.warn("HDF Lib Version:" + vstr[0]);
				file4obj = (H4File) FFH4Instance.createInstance(inputFile,
						FileFormat.WRITE);
				logger.debug("obtained H4File:" + file4obj.getName());
				fid = file4obj.open(); // necessary for getting TopGroup; hdf5
				// exception is tripped here.
				logger.debug("Opened H4File:" + file4obj.getName());
				FileType = file4obj.getName();
			} else {
				logger
						.error("FileFormat retrieval is null! use HDF Lib Version:HDF 4.2.2 or 4.2.5");
				System.exit(1);
			}
		} catch (Exception e_hdf4) {
			logger.debug("looks like we are dealing with an HDF5 file");
			try {
				FFH4Instance = FileFormat
						.getFileFormat(FileFormat.FILE_TYPE_HDF5);
				if (FFH4Instance != null) {
					logger.warn("HDF Lib Version:"
							+ FFH4Instance.getLibversion());
					file5obj = (H5File) FFH4Instance.createInstance(inputFile,
							FileFormat.WRITE);
					logger.debug("obtained H5File:" + file5obj.getName());
					fid = file5obj.open(); // necessary for getting TopGroup
					logger.debug("Opened H5File:" + file5obj.getName());
					FileType = file4obj.getName();
					DataFile.HDFType = "HDF5";
				}
			} catch (Exception e_hdf5) {
				logger.error("Problem with:" + inputFile);
				e_hdf5.printStackTrace();
				System.exit(1);
				return;
			}
		}
		/*
		 * The Group seems to be how you navigate the datasets.
		 * 
		 * An H4Group is a vgroup in HDF4, inheriting from Group. A vgroup is a
		 * structure designed to associate related data objects. The general
		 * structure of a vgroup is similar to that of the UNIX file system in
		 * that the vgroup may contain references to other vgroups or HDF data
		 * objects just as the UNIX directory may contain subdirectories or
		 * files.
		 */

		if (DataFile.HDFType.equals("HDF4")) {
			if (file4obj != null) {
				try {
					topGroup = (Group) ((javax.swing.tree.DefaultMutableTreeNode) file4obj
							.getRootNode()).getUserObject();
				} catch (NullPointerException e) {
					logger.fatal("topGroup could not be found in " + inputFile);
				}
			} else {
				logger
						.fatal("file4obj created with " + inputFile
								+ " was null");
				System.exit(1);
				return;
			}
		} else {
			if (file5obj != null) {
				try {
					topGroup = (Group) ((javax.swing.tree.DefaultMutableTreeNode) file5obj
							.getRootNode()).getUserObject();
				} catch (NullPointerException e) {
					logger.fatal("topGroup could not be found in " + inputFile);
				}
			} else {
				logger
						.fatal("file5obj created with " + inputFile
								+ " was null");
				System.exit(1);
				return;
			}
		}

	}

	/**
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @param HDF4
	 *            this is the only one with no references as of Expression.
	 */
	public DataFile(String inputFile, String outputFile, String HDF4) {
		SDFileName = inputFile;
		try {
			FileFormat h4 = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4);
			FileFormat h5 = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

			FFH4Instance = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

			boolean isH5 = FFH4Instance.isThisType(h5);
			boolean isH4 = FFH4Instance.isThisType(h4);
			String[] vstr = { "" };
			int[] vargs = new int[3];
			HDFLibrary.Hgetlibversion(vargs, vstr);

			// Create an instance of H5File object with read/write access
			// H5File test1 = (H5File) FFH4Instance.createInstance(inputFile,
			// FileFormat.WRITE);
			// Open the file and load the file structure; file id is returned.
			// int fid = test1.open();

			// fid = H5.H5Fopen(inputFile, HDF5Constants.H5F_ACC_RDWR,
			// HDF5Constants.H5P_DEFAULT);

			if (FFH4Instance != null) {
				logger.warn("HDF Lib Version:" + FFH4Instance.getLibversion());
				file5obj = (H5File) FFH4Instance.createInstance(inputFile,
						FileFormat.WRITE);
				logger.debug("obtained H5File:" + file5obj.getName());
				fid = file5obj.open(); // necessary for getting TopGroup
				logger.debug("Opened H5File:" + file5obj.getName());
			} else {
				logger
						.error("FileFormat retrieval is null! use HDF Lib Version:HDF 4.2.2 or 4.2.5");
				System.exit(1);
			}
		} catch (Exception e) {
			logger.error("Problem with:" + inputFile);
			e.printStackTrace();
			System.exit(1);
			return;
		}
		/*
		 * The Group seems to be how you navigate the datasets.
		 * 
		 * An H4Group is a vgroup in HDF4, inheriting from Group. A vgroup is a
		 * structure designed to associate related data objects. The general
		 * structure of a vgroup is similar to that of the UNIX file system in
		 * that the vgroup may contain references to other vgroups or HDF data
		 * objects just as the UNIX directory may contain subdirectories or
		 * files.
		 */

		if (file5obj != null) {
			try {
				topGroup = (Group) ((javax.swing.tree.DefaultMutableTreeNode) file5obj
						.getRootNode()).getUserObject();
			} catch (NullPointerException e) {
				logger.fatal("topGroup could not be found in " + inputFile);
			}
		} else {
			logger.fatal("file5obj created with " + inputFile + " was null");
			System.exit(1);
			return;
		}

	}

	/*
	 * Does SD stuff
	 */
	public DataFile(String inputFile, int doSD) {
		File input = new File(inputFile);
		logger.debug("Opening: " + inputFile);

		FFH4Instance = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4);
		logger.debug("obtained FileFormat:" + FFH4Instance.getFilePath());
		logger.warn("HDF Lib Version:" + FFH4Instance.getLibversion());
		try {
			file4obj = (H4File) FFH4Instance.createInstance(inputFile,
					FileFormat.WRITE);
			logger.debug("obtained H4File:" + file4obj.getName());
			fid = file4obj.open();
			logger.debug("Opened H4File:" + file4obj.getName());
		} catch (Exception e) {
			logger.error("Problem with:" + inputFile);
			e.printStackTrace();
			return;
		}

		try {
			sd_id = HDFLibrary.SDstart(inputFile, HDFConstants.DFACC_WRITE);
		} catch (HDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * The Group seems to be how you navigate the datasets.
		 * 
		 * An H4Group is a vgroup in HDF4, inheriting from Group. A vgroup is a
		 * structure designed to associate related data objects. The general
		 * structure of a vgroup is similar to that of the UNIX file system in
		 * that the vgroup may contain references to other vgroups or HDF data
		 * objects just as the UNIX directory may contain subdirectories or
		 * files.
		 */

		if (file4obj != null) {
			topGroup = (Group) ((javax.swing.tree.DefaultMutableTreeNode) file4obj
					.getRootNode()).getUserObject();
		} else {
			return;
		}

	}

	public void logkeys() {
		Enumeration enumer = FileFormat.getFileFormatKeys();
		while (enumer.hasMoreElements()) {
			String key = (String) enumer.nextElement();

			logger.debug("FileFormat keys:" + key);
			System.out.println(enumer.toString() + "," + key);
			System.out.println(FileFormat.FILE_TYPE_HDF4);
		}
	}

	public static String DetermineProduct(String inputfile) {
		if (inputfile.contains("MOD")) {
			return "MODIS";
		} else if (inputfile.contains("MYD")) {
			return "MODIS";
		} else if (inputfile.contains("AIR")) {
			return "AIRS";
		}
		if (inputfile.contains("MLS")) {
			return "MODIS";
		} else {
			return "Not supporting this type of file:" + inputfile;
		}

	}

	// public abstract void getOntologyResults(DataQuality d);

	// public Group createGroup(String parameter)
	public Group createGroup(Dataset d, String groupName) {
		Group newgroup = null;
		Group groupOfDataset = null;
		try {
			if (d != null) {
				groupOfDataset = getGroupOfDataset(d.getName());
				logger.debug(d.getName() + " is in group:"
						+ groupOfDataset.getName());
			} else {
				groupOfDataset = topGroup;
			}
			newgroup = this.file4obj.createGroup(groupName, groupOfDataset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newgroup;
	}

	/*
	 * I found that if I created the new group at this point rather than in
	 * createDataset(), that I got a lot fewer segment faults. What I am doing
	 * here is getting the address of the quality screening datasets -
	 * getQSVars() -> calls getDataset() and 'data' datasets and creating new
	 * datasets to hold the new masks: - copyDataset()
	 * 
	 * After that I create a new class to do the arithmetic on the new dataset.
	 * I haven't yet decided if it should be a subclass. Perhaps it will be when
	 * I place this method in the subclasses.
	 * 
	 * The mask gets written to the file OK because of createDataset() -
	 * Although there is a lot of housekeeping I am leaving out at this point. -
	 * I know this because of what shows up in the metadata when I view with
	 * ncdump
	 * 
	 * 
	 * At this writing, TAirMWOnlyStd was not finished yet.
	 */

	/*
	 * This is something I wrote to see where the mask turned up.
	 */
	private void checkLocationOfNewDatasets(String newDataset, Group TopGroup) {

		Dataset d = null;
		d = getDatasetRecurse(newDataset, TopGroup);
		if (d != null) {
			logger.debug("Path of current dataset:" + d.getPath());
		}

		// logger.debug("Dataset " + newDataset + " is in group:" +
		// currentGroup.getName());
	}

	/*
	 * Placeholder method to get screening variable from ontology results
	 * 
	 * private String getScreeningVariableFromOntologyResults(String item,
	 * Document doc) { XPath xpathSelector = DocumentHelper.createXPath("//" +
	 * item); List results = xpathSelector.selectNodes(doc); String qs = null;
	 * for (Iterator iter = results.iterator(); iter.hasNext(); ) { Element tag
	 * = (Element)iter.next(); qs = tag.getStringValue();
	 * 
	 * } logger.debug("SQDS Screening variable is:" + qs); return qs; }
	 */
	/*
	 * A probable waste of time but a good exercise for me in various aspects of
	 * java to get the ontology results out for 2nd test case.
	 * 
	 * private HashMap<String,String[]>
	 * getVariablesAndLimitsFromOntologyResults(String item, Document doc) {
	 * HashMap<String,String[]> res = new HashMap<String,String[]>() ; XPath
	 * xpathSelector = DocumentHelper.createXPath("//" + item); List results =
	 * xpathSelector.selectNodes(doc);
	 * 
	 * String qs = null; for (Iterator iter = results.iterator();
	 * iter.hasNext(); ) { Element tag = (Element)iter.next(); qs =
	 * tag.getStringValue(); logger.debug("Found " + qs + " in ontology xml");
	 * String[] sub = qs.split("_");
	 * 
	 * XPath xpat = DocumentHelper.createXPath("//DimensionLimits_" +
	 * sub[sub.length-1]); logger.debug("Looking under://DimensionLimits_" +
	 * sub[sub.length-1]); List dims = xpat.selectNodes(doc); String[] limits =
	 * new String[2]; int i = 0; for (Iterator diter = dims.iterator();
	 * diter.hasNext(); ) { Element dtag = (Element)diter.next(); limits[i] =
	 * dtag.getStringValue(); logger.debug("Found limit " + limits[i] + " for "
	 * + qs); ++i; } res.put(qs, limits); } return res; }
	 */

	/*
	 * Critical in the path of getting the quality datasets.
	 */
	public Dataset[] getQSVars(String[] qs) {

		Dataset[] qsDatasets = new Dataset[qs.length];

		for (int i = 0; i < qs.length; i++) {
			logger.debug("Doing:" + qs[i]);
			qsDatasets[i] = getDataset(qs[i], topGroup);
			if (qsDatasets[i] == null) {
				logger.error("Quality dataset:" + qs[i]
						+ " does not exist in the hdf file");
				return null;
			}
			logger.debug("getQSVars:" + ((Dataset) qsDatasets[i]).getName());
		}

		return qsDatasets;
	}

	public Dataset[] getQSVars() {
		return null;
	};

	/*
	 * critical in the path of getting and creating the 'data' datasets. 1. Use
	 * group to find address of dataset 2. Use createDataset() and
	 * populateDataset() to create a copy of the 'data' dataset. This will be
	 * the mask. Eventually we want to: a) rename the 'data' dataset. b) create
	 * 2 new datasets: 1 for the mask , 1 for the screened dataset. (Lots of
	 * other stuff too but I expect these things to be done at this point)
	 */
	public Dataset old_copyDataset(String datasetName, Group mask) {
		Dataset d = getDatasetRecurse(datasetName, topGroup);

		if (d != null) {

			try {

				logger.debug("Name of current dataset:" + d.getName());
				logger.debug("Path of current dataset:" + d.getPath());
				logger.debug("Type of current dataset:"
						+ d.getDatatype().getDatatypeDescription());
				logger.debug("NDims of current dataset:" + d.getDims().length);
			} catch (NullPointerException e) {
				logger.error("Could not find dataset with name:" + datasetName);
			}
			Dataset copy = createDataset(d.getName(), d.getDatatype(), d
					.getDims(), d.getDimNames(), mask);
			try {
				populateDataset(d, copy);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return copy;
		} else {
			logger.error("getDataset Could not find " + datasetName);

		}
		return d;
	}

	public Dataset copyDatasetWithSuffix(String datasetName, Group mask,
			String Suffix) {
		Dataset d = getDatasetRecurse(datasetName, topGroup);

		if (d != null) {
			long[] localdims = null;
			try {
				/*
				 * For later versions of Dataset than I used at first... The
				 * init() is designed to support lazy operation in dataset
				 * object. When a data object is retrieved from file, the
				 * datatype, dataspace and raw data are not loaded into memory.
				 * When it is asked to read the raw data from file, init() is
				 * first called to get the datatype and dataspace information,
				 * then load the raw data from file. init() is also used to
				 * reset selection of a dataset (start, stride and count) to the
				 * default, which is the entire dataset for 1D or 2D datasets.
				 * In the following example, init() at step 1) retrieve datatype
				 * and dataspace information from file. getData() at step 3)
				 * read only one data point. init() at step 4) reset the
				 * selection to the whole dataset. getData() at step 4) reads
				 * the values of whole dataset into memory.
				 */

				d.init();
				logger.debug("Name of current dataset:" + d.getName());
				logger.debug("Rank of current dataset:" + d.getRank());
				logger.debug("Type of current dataset:" + d.getDatatype());

				localdims = d.getDims();
				if (localdims == null) {
					logger.error("Call to Dataset.getDims() failed");
					return null;
				}
				logger.debug("NDims of current dataset:" + localdims.length);

				String type = d.getDatatype().getDatatypeDescription();
				if (type.contains("Unknown")) {
					logger
							.warn("call to Dataset.getDatatype().getDatatypeDescription() failed");
				} else {
					logger.debug("Type of current dataset:" + type);
				}

			} catch (NullPointerException e) {
				logger.error("getDataset Could not find " + datasetName
						+ " More likely...getDims() failed");
			}
			Dataset copy = null;
			if (DataFile.HDFType.equals("HDF5")) {
				copy = createHDF5Dataset(d.getName() + Suffix, d.getDatatype(),
						localdims, getDimNamesGen(d), mask);
			} else {
				copy = createDataset(d.getName() + Suffix, d.getDatatype(),
						localdims, d.getDimNames(), mask);
			}

			if (copy == null) {
				logger.error("Could not create copy of dataset of "
						+ d.getName() + " in " + this.SDFileName);
				System.out.println("Could not create copy of dataset:"
						+ d.getName() + " in " + this.SDFileName);
				System.exit(1);
			}
			try {
				populateDataset(d, copy);
				// populateDatasetWithTrackingValues(d,copy);// for refactoring
				// exploration
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return copy;
		} else {
			logger.error("getDataset Could not find " + datasetName);

		}
		return d;
	}

	private void display5DimDataset(Dataset data) throws Exception {

		long[] start = data.getStartDims(); // get startDims so we can navigate
		// the layers
		/*
		 * If there are 3 dimensions then start[2] = layers (rows and cols in
		 * [0] and [1] 5 Dim: 3x3 = 9 AIRS pixels in single AMSU pixel 2 could
		 * layers. numcloud determines how many of the 2 layers apply. If there
		 * are 5 dimensions (30,45,3,3,2) then start[2] and start[3] are somehow
		 * the 9 AIRS pixels and start[4] are the two layers. But how? How will
		 * I read the 9 pixels?
		 */

		float before;
		for (int layer = 0; layer < 3; ++layer) {
			start[2] = layer; // this sets the layer outside the class as dims
			// are passed by reference.
			for (int airsdim = 0; airsdim < 3; ++airsdim) {
				start[3] = airsdim;
				for (int cldlyr = 0; cldlyr < 2; ++cldlyr) {
					start[4] = cldlyr;
					float[] dataData = (float[]) data.read();

					int count = 0;
					for (int i = 0; i < dataData.length; ++i) {

						if (dataData[i] > 0) {
							// System.out.print(layer+ ": " + i + ":" +
							// dataData[i]+ ", ");
							++count;
						}

					}

					logger.debug(layer + ":" + airsdim + ":" + cldlyr + ":"
							+ dataData.length + ":" + count);

				}

			}

		}

	}

	private void populate5DimDataset(Dataset data, Dataset dnew)
			throws Exception {

		long[] start = data.getStartDims(); // get startDims so we can navigate
		// the layers
		long[] fin = dnew.getStartDims(); // get startDims so we can navigate
		// the layers

		/*
		 * If there are 3 dimensions then start[2] = layers (rows and cols in
		 * [0] and [1] 5 Dim: 3x3 = 9 AIRS pixels in single AMSU pixel 2 could
		 * layers. numcloud determines how many of the 2 layers apply. If there
		 * are 5 dimensions (30,45,3,3,2) then start[2] and start[3] are somehow
		 * the 9 AIRS pixels and start[4] are the two layers. But how? How will
		 * I read the 9 pixels?
		 */
		for (int layer = 0; layer < 3; ++layer) {
			start[2] = layer; // this sets the layer outside the class as dims
			// are passed by reference.
			fin[2] = layer;
			for (int airsdim = 0; airsdim < 3; ++airsdim) {
				start[3] = airsdim;
				fin[3] = airsdim;
				for (int cldlyr = 0; cldlyr < 2; ++cldlyr) {
					start[4] = cldlyr;
					fin[4] = cldlyr;

					float[] dataData = (float[]) data.read();
					dnew.write(dataData);

					float[] dnewData = (float[]) dnew.read();
					int count = 0;
					for (int i = 0; i < dnewData.length; ++i) {
						if (dnewData[i] > 0) {
							++count;
						}
					}
					if (count > 0) {
						logger.debug("populate5d:" + layer + ":" + airsdim
								+ ":" + cldlyr + ":" + dnewData.length + ":"
								+ count);
					}

				}
			}
		}

	}

	private void display5d(Dataset data) throws Exception {

		data.clearData();
		long[] start = data.getStartDims(); // get startDims so we can navigate
		// the layers

		for (int layer = 0; layer < 3; ++layer) {
			start[2] = layer; // this sets the layer outside the class as dims
			// are passed by reference.
			for (int airsdim = 0; airsdim < 3; ++airsdim) {
				start[3] = airsdim;
				for (int cldlyr = 0; cldlyr < 2; ++cldlyr) {
					start[4] = cldlyr;
					data.clearData();
					float[] dataData = (float[]) data.getData();

					int count = 0;
					for (int i = 0; i < dataData.length; ++i) {

						if (dataData[i] != 0) {
							// System.out.print(layer+ ": " + i + ":" +
							// dataData[i]+ ", ");
							++count;
						}

					}

					logger.debug("display:" + layer + ":" + airsdim + ":"
							+ cldlyr + ":" + dataData.length + ":" + count);

				}
			}
		}

	}

	/*
	 * This method was to see if I was able to populate the Dataset objects...
	 */
	private void populateDatasetWithTrackingValues(Dataset old, Dataset dnew) {
		old.init();
		dnew.init();
		HashMap<String, String> dimension = new HashMap<String, String>();
		try {
			determineDatatype(old, dimension);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DF.setDataType(dimension.get("type"));

		long[] dims = old.getDims();
		long oldstart[] = old.getStartDims();
		long newstart[] = dnew.getStartDims();
		for (int layer = 0; layer < dims[2]; ++layer) {
			oldstart[2] = layer;
			newstart[2] = layer;

			DQSSNumber dataData = DQSS.populateWithType(dnew, dimension
					.get("type"), false, 0);

			for (int i = 0; i < dims[0] * dims[1]; ++i) {

				dataData.setArray(i, layer * 100 + i);
			}

			try {
				dataData.reInstate(dnew, DF);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void DataFieldReferencePopulate(Dataset old, Dataset dnew) throws Exception {
		if (DF.type instanceof Short) {
			short[] dataRead = (short[]) old.read();
			dnew.write(dataRead);
			// checkThatDataWasWrittenToHDFLayer(DF.type, dnew,-1,1);
		} else if (DF.type instanceof Integer) {
			int[] dataRead = (int[]) old.read();
			dnew.write(dataRead);
			checkThatDataWasWrittenToHDFLayer(DF.type, dnew, -1, 1);
		} else if (DF.type instanceof Float) {
			float[] dataRead = (float[]) old.read();
			dnew.write(dataRead);
			checkThatDataWasWrittenToHDFLayer(DF.type, dnew, -1, 1);
		} else if (DF.type instanceof Double) {
			double[] dataRead = (double[]) old.read();
			dnew.write(dataRead);
			checkThatDataWasWrittenToHDFLayer(DF.type, dnew, -1, 1);
		}

	}

	void checkThatDataWasWrittenToHDFLayer(Object obj, Dataset d,
			int startindex, int doZero) throws OutOfMemoryError, Exception {

		if (startindex > -1) {
			long[] start = d.getStartDims();
			start[2] = 0;
		}

		if (obj instanceof Short) {
			short[] check = (short[]) d.read();
			if (doZero > -1) {
				for (int i = 0; i < check.length; ++i) {
					if (check[i] == 0) {
						logger.debug("Chk[" + i + "] pop vals:" + check[i]);
					}
				}
			}
			logger.debug("Chk pop vals:" + check[0] + " "
					+ check[check.length / 2] + " " + check[check.length - 1]);
		} else if (obj instanceof Integer) {
			int[] check = (int[]) d.read();
			logger.debug("Checking populated values:" + check[0] + " "
					+ check[check.length / 2] + " " + check[check.length - 1]);

		} else if (obj instanceof Float) {
			float[] check = (float[]) d.read();
			logger.debug("Checking populated values:" + check[0] + " "
					+ check[check.length / 2] + " " + check[check.length - 1]);

		} else if (obj instanceof Double) {
			double[] check = (double[]) d.read();
			logger.debug("Checking populated values:" + check[0] + " "
					+ check[check.length / 2] + " " + check[check.length - 1]);

		}
	}

	private void populateDataset(Dataset old, Dataset dnew) throws Exception {

		logger.debug("old dataset name:" + old.getName());

		old.init();
		dnew.init();

		HashMap<String, String> dimension = new HashMap<String, String>();
		determineDatatype(old, dimension);

		long[] dims = old.getDims();
		if (DF == null) {
			/*
			 * Screener may need to do this.
			 */
			DF = instatiateProperDatafieldType(old.getName());
		}
		logger.debug("data type:" + dimension.get("type"));
		if (DF.getDatatype() == null) {
			DF.setDataType(dimension.get("type")); // does this need to be here?
		}

		if (old.getRank() == 2) {
			DataFieldReferencePopulate(old, dnew);
		} else if (old.getRank() == 3) {
			long oldstart[] = old.getStartDims();
			long newstart[] = dnew.getStartDims();

			for (int i = 0; i < dims[2]; ++i) {
				newstart[2] = i;
				oldstart[2] = i;
				DataFieldReferencePopulate(old, dnew);
			}
		} else if (old.getRank() == 5) {
			populate5DimDataset(old, dnew);

		} else {
			logger.error("Cannot populate a dataset with a rank of:"
					+ old.getRank());
		}

	}

	/*
	 * An attempt to write dimension names...
	 * 
	 */
	private Dataset createCompoundDataset(String name, Datatype dtype, long[] dims,
			String[] dimNames, Group g) {
		Dataset dataset = null;
		try {
			dtype.open();
			logger.debug("Trying to create :" + name);
			logger.debug(file4obj.toString());
			Datatype[] ntypes = new Datatype[dims.length];
			int[] memberSizes = new int[dims.length];
			for (int i=0; i < dims.length; ++i) {
				 ntypes[i] = file4obj.createDatatype(dtype.getDatatypeClass(),
						dtype.getDatatypeSize(), dtype.getDatatypeOrder(), dtype
								.getDatatypeSign());
				 memberSizes[i] = (int) dims[i];
			}
			 

			//dataset = file4obj.createScalarDS(name, g, ntype, dims, dims, null,0, null);
			dataset = file4obj.createCompoundDS(name, g, dims, null, 
			           null, 0, dimNames, ntypes, memberSizes, null);

			logger.debug("Created a new:" + name);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataset;
	}
	/*
	 * critical in path of creating a new dataset for whatever reason.
	 */
	private Dataset createDataset(String name, Datatype dtype, long[] dims,
			String[] dimNames, Group g) {
		Dataset dataset = null;
		try {
			dtype.open();
			logger.debug("Trying to create :" + name);
			logger.debug(file4obj.toString());
			Datatype ntype = file4obj.createDatatype(dtype.getDatatypeClass(),
					dtype.getDatatypeSize(), dtype.getDatatypeOrder(), dtype
							.getDatatypeSign());
			logger.debug("created datatype:" + ntype.getDatatypeDescription());
			checkdims(dims);
			logger.debug("Using Group:" + g.getPath() + g.getName());

			/*
			 * we can use the old name because we are putting it into a new
			 * group.
			 */

			dataset = file4obj.createScalarDS(name, g, ntype, dims, dims, null,
					0, null);
			

			logger.debug("Created a new:" + name);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataset;
	}

	private Dataset createHDF5Dataset(String name, Datatype dtype, long[] dims,
			String[] dimNames, Group g) {
		Dataset dataset = null;
		try {
			dtype.open();
			this.logger.debug("Trying to create :" + name);
			logger.debug(file5obj.toString());
			Datatype ntype = file5obj.createDatatype(dtype.getDatatypeClass(),
					dtype.getDatatypeSize(), dtype.getDatatypeOrder(), dtype
							.getDatatypeSign());
			logger.debug("created datatype:" + ntype.getDatatypeDescription());
			checkdims(dims);
			logger.debug("Using Group:" + g.getPath() + g.getName());

			/*
			 * we can use the old name because we are putting it into a new
			 * group.
			 */

			dataset = file5obj.createScalarDS(name, g, ntype, dims, dims, null,
					0, null);

			logger.debug("Created a new:" + name);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Probably " + name + " already exists.");
			return null;
		}
		return dataset;
	}

	public void UpdateDimensionNames(String name, long[] dims) {

		try {
			sd_id = HDFLibrary.SDstart(SDFileName, HDFConstants.DFACC_WRITE);

			int some_id = HDFLibrary.SDnametoindex(sd_id, name);
			int sds_id = HDFLibrary.SDselect(sd_id, some_id);
			for (int dimindex = 0; dimindex < dims.length; ++dimindex) {
				int dim_id = HDFLibrary.SDgetdimid(sds_id, 0);
				boolean status = HDFLibrary.SDsetdimname(dim_id, "Masked:"
						+ dimindex);
				if (status) {
					String[] dimname = { "" };
					int[] args = new int[3];
					HDFLibrary.SDdiminfo(dim_id, dimname, args);
					logger.debug("I set dimname to:" + dimname[0]);
					String[] info = { "", "", "", "" };
					status = HDFLibrary.SDgetdimstrs(sds_id, info, 20);
					if (status) {
						for (int i = 0; i < 4; ++i) {
							logger.debug("dimstrs:" + info[i]);
						}
					} else {
						logger.debug("SDgetdimstrs failed");
					}

				} else {
					logger.debug("SDsetdimname failed");
				}
				status = HDFLibrary.SDendaccess(sds_id);
				status = HDFLibrary.SDend(sd_id);
			}

		} catch (HDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * This is all that is left after I fixed things. might just put it in
	 * getQSVars()
	 */
	public Dataset getDataset(String datasetName, Group TopGroup) {
		Dataset d = getDatasetRecurse(datasetName, TopGroup);

		return d;
	}

	/*
	 * Critical in the path of locating datasets. This needs to be recursive as
	 * there are groups within groups
	 */
	private Dataset getDatasetRecurse(String datasetName, Group TopGroup) {
		List members = TopGroup.getMemberList();

		int n = members.size();
		HObject obj = null;
		int objectid;
		Dataset looking = null;
		Dataset d = null;

		for (int i = 0; i < members.size(); i++) {
			if (d != null && d instanceof Dataset) {
				logger.debug("Returning:" + d.getName());
				return d;
			}
			obj = (HObject) members.get(i);

			/*
			 * Each Dataset (SDS) is a member of a Group In the AIRS file I
			 * worked with the groups were:
			 * L2_Standard_atmospheric&surface_product Geolocation Fields SDS
			 * Fields Swath Attributes
			 */
			if (obj instanceof Dataset) {
				looking = (Dataset) obj;

				if (looking.getName().equals(datasetName)) {
					d = looking;
					logger.debug("Found Dataset:" + d.getName());
					logger.debug("Found Path:" + d.getPath());

					return (Dataset) obj;
				}
			}
			if (obj instanceof Group && d == null) {
				// currentGroup = (Group) obj;
				d = getDatasetRecurse(datasetName, (Group) obj);
				if (obj instanceof Group) {
					logger.debug("Current Group:" + obj.getName());
				}

			}

		}

		return d;
	}

	/*
	 * This is until we find out how to get fillValue.
	 */
	/*
	 * public static float getFillValue (Dataset d) {
	 */

	// return (float) DQSS.getFillValue();
	/*
	 * The ncsa.hdf.object.ScalarDS.getFillValue() algorithm below did not work
	 * (perhaps for the same reason HDFView properties dialog shows FillValue of
	 * NONE when attribute _FillValue has a value.
	 */
	/*
	 * ScalarDS sds = (ScalarDS) d;
	 * 
	 * float[] fill = (float[]) sds.getFillValue(); if (sds.getFillValue() !=
	 * null) { logger.debug("ScalarDS.getFillValue:" + fill[0] ); return
	 * fill[0]; } else { fill = new float[1]; fill[0] = (float)-9999.0;
	 * logger.debug("Fake FillValue:" + fill[0] ); return fill[0]; }
	 */
	// }
	public static Number getFillValueN(Dataset d) {

		ScalarDS sds = (ScalarDS) d;

		Number fill = (Number) sds.getFillValue();

		if (sds.getFillValue() != null) {
			logger.debug("ScalarDS.getFillValue:" + fill.toString());
			return fill;
		} else {
			fill = -9999;

			logger.debug("Fake FillValue:" + fill.toString());
			return fill;
		}
	}

	private void checkdims(long[] dims) {
		try {
			for (int i = 0; i < dims.length; ++i) {
				logger.debug("Dims:" + dims[i]);
			}
		} catch (NullPointerException e) {
			logger.error("No Dims...Dims were passed in from copyDataset()");
		}
	}

	/*
	 * Gets the Group objected with groupName
	 */
	public Group getGroup(String groupName, Group currentGroup) {

		List members = currentGroup.getMemberList();

		int n = members.size();
		HObject obj = null;
		int objectid;
		Dataset looking = null;
		Dataset d = null;
		logger.debug("looking for group:" + groupName + " in group:"
				+ currentGroup.getName());

		for (int i = 0; i < members.size(); i++) {
			if (currentGroup.getName().equals(groupName)) {
				logger.debug("Returning:" + currentGroup.getName());
				return currentGroup;
			}
			obj = (HObject) members.get(i);

			if (obj instanceof Dataset) {

			}
			if (obj instanceof Group) {
				logger.debug("Current Group before:" + obj.getName());
				currentGroup = getGroup(groupName, (Group) obj);
				if (currentGroup instanceof Group) {
					logger.debug("Current Group:" + obj.getName());
					if (currentGroup.getName().equals(groupName)) {
						return currentGroup;
					}
				}

			}

		}

		return currentGroup;
	}

	public Dataset[] getMaskDatasets(String Suffix) {

		List<Dataset> masks = new ArrayList();
		getSuffixedDatasets(Suffix, topGroup, masks);
		Dataset[] suffixed = new Dataset[masks.size()];
		try {
			for (int i = 0; i < masks.size(); ++i) {
				suffixed[i] = masks.get(i);
				logger.debug("Found Suffixed Dataset:" + suffixed[i].getName());

			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return suffixed;
	}

	private Dataset getSuffixedDatasets(String Suffix, Group TopGroup,
			List<Dataset> suffixed) {
		List members = TopGroup.getMemberList();

		int n = members.size();
		HObject obj = null;
		int objectid;
		Dataset looking = null;
		Dataset d = null;

		for (int i = 0; i < members.size(); i++) {
			if (d != null && d instanceof Dataset) {
				logger.debug("Returning:" + d.getName());
				// return d;
			}
			obj = (HObject) members.get(i);

			/*
			 * Each Dataset (SDS) is a member of a Group In the AIRS file I
			 * worked with the groups were:
			 * L2_Standard_atmospheric&surface_product Geolocation Fields SDS
			 * Fields Swath Attributes
			 */
			if (obj instanceof Dataset) {
				looking = (Dataset) obj;

				if (looking.getName().contains(Suffix)) {
					d = looking;
					logger.debug("Found Dataset:" + d.getName());
					logger.debug("Found Path:" + d.getPath());

					suffixed.add((Dataset) obj);
				}
			}
			if (obj instanceof Group && d == null) {
				// currentGroup = (Group) obj;
				d = getSuffixedDatasets(Suffix, (Group) obj, suffixed);
				if (obj instanceof Group) {
					logger.debug("Current Group:" + obj.getName());
				}

			}
		}
		return d;

	}

	private Dataset getDatasetRecurseRemove(String datasetName, Group TopGroup) {
		List members = TopGroup.getMemberList();

		int n = members.size();
		HObject obj = null;
		int objectid;
		Dataset looking = null;
		Dataset d = null;

		for (int i = 0; i < members.size(); i++) {
			if (d != null && d instanceof Dataset) {
				logger.debug("Returning:" + d.getName());
				return d;
			}
			obj = (HObject) members.get(i);

			/*
			 * Each Dataset (SDS) is a member of a Group In the AIRS file I
			 * worked with the groups were:
			 * L2_Standard_atmospheric&surface_product Geolocation Fields SDS
			 * Fields Swath Attributes
			 */
			if (obj instanceof Dataset) {
				looking = (Dataset) obj;

				if (looking.getName().equals(datasetName)) {
					d = looking;
					logger.debug("Found Dataset:" + d.getName());
					logger.debug("Found Path:" + d.getPath());

					return (Dataset) obj;
				}
			}
			if (obj instanceof Group && d == null) {
				// currentGroup = (Group) obj;
				d = getDatasetRecurse(datasetName, (Group) obj);
				if (obj instanceof Group) {
					logger.debug("Current Group:" + obj.getName());
				}

			}

		}

		return d;
	}

	public Dataset[] getDatasetsInMaskGroup(String group) {
		Group mask = getGroup(group, topGroup);
		List members = mask.getMemberList();

		int n = 0;
		HObject obj = null;
		int objectid;
		Dataset looking = null;
		Dataset d = null;
		Dataset[] masks = new Dataset[members.size()];

		for (int i = 0; i < members.size(); i++) {
			obj = (HObject) members.get(i);
			if (obj instanceof Dataset) {
				masks[n] = (Dataset) obj;
				++n;
			}
		}
		return masks;
	}

	public Dataset[] old_getCorrespondingDatasets(Dataset[] masks) {
		Dataset[] corresponding = new Dataset[masks.length];

		for (int i = 0; i < masks.length; ++i) {
			logger.debug("Looking for dataset with name:" + masks[i].getName());
			// corresponding[i] = getDataset(masks[i].getName(),topGroup);

			/* instead: */
			Dataset d = getDataset(masks[i].getName(), topGroup);
			Group g = getGroupOfDataset(masks[i].getName());
			Dataset copy = createDataset(d.getName(), d.getDatatype(), d
					.getDims(), d.getDimNames(), g);
			try {
				populateDataset(d, copy);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			corresponding[i] = copy;
			try {
				file4obj.delete(d);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.warn(e.getMessage());
				logger.warn("Attempt to delete " + g.getName() + ":"
						+ d.getName() + " from screened file failed.");
			}
			/* end instead */

			if (corresponding[i] != null) {
				logger.debug("Found :" + corresponding[i].getName());
			} else {
				logger.error("Could not find:" + masks[i].getName());
			}
		}
		return corresponding;
	}

	public Dataset[] old_createCorrespondingDatasetsInSameGroup(Dataset[] masks,
			String Suffix, Group g) {
		Dataset[] corresponding = new Dataset[masks.length];

		for (int i = 0; i < masks.length; ++i) {
			/*
			 * We don't want the datasets with the same anem without the suffix.
			 */
			String stripSuffix = masks[i].getName().replaceAll(Suffix, "");
			logger.debug("Looking for dataset with name:" + masks[i].getName());
			// corresponding[i] = getDataset(masks[i].getName(),topGroup);

			/* instead: */
			Dataset d = getDataset(stripSuffix, topGroup);
			// Group g = getGroupOfDataset(stripSuffix);
			Dataset copy = createDataset(stripSuffix, d.getDatatype(), d
					.getDims(), d.getDimNames(), g);
			try {
				populateDataset(d, copy);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			corresponding[i] = copy;

			if (corresponding[i] != null) {
				logger.debug("Found :" + corresponding[i].getName());
			} else {
				logger.error("Could not find:" + stripSuffix);
			}
		}
		return corresponding;
	}

	public Dataset[] getCorrespondingDatasetsInSameGroup(Dataset[] masks,
			String Suffix) {
		Dataset[] corresponding = new Dataset[masks.length];

		for (int i = 0; i < masks.length; ++i) {
			/*
			 * We don't want the datasets with the same anem without the suffix.
			 */
			String stripSuffix = masks[i].getName().replaceAll(Suffix, "");
			logger.debug("Looking for dataset with name:" + masks[i].getName());
			// corresponding[i] = getDataset(masks[i].getName(),topGroup);

			/* instead: */
			Dataset d = getDataset(stripSuffix, topGroup);
			corresponding[i] = d;

			if (corresponding[i] != null) {
				logger.debug("Found :" + corresponding[i].getName());
			} else {
				logger.error("Could not find:" + stripSuffix);
			}
		}
		return corresponding;
	}

	public void old_copyIntoOrig(Dataset[] originals, Group group) {

		for (int i = 0; i < originals.length; ++i) {
			Dataset d = originals[i];
			// Group g = getGroupOfDataset(d);
			if (group != null) {
				logger.debug("Found " + d.getName() + "'s group:"
						+ group.getName());
				Dataset copy = createDataset(d.getName(), d.getDatatype(), d
						.getDims(), d.getDimNames(), group);
				try {
					populateDataset(d, copy);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger.error("Could not find " + d.getName() + "'s group");
			}

		}
	}

	public void copyIntoSameGroup(Dataset[] originals, Group group,
			String suffix) {

		for (int i = 0; i < originals.length; ++i) {
			Dataset d = originals[i];

			if (group != null) {

				d.init(); /* done to put certain data members of Dataset in memory */
				logger.debug("Found " + d.getName() + "'s group:"
						+ group.getName());
				String newname = d.getName() + suffix;
				Dataset copy;
				if (DataFile.HDFType.equals("HDF5")) {
					copy = createHDF5Dataset(newname, d.getDatatype(), d
							.getDims(), getDimNamesGen(d), group);
				} else {
					copy = createDataset(newname, d.getDatatype(), d.getDims(),
							d.getDimNames(), group);
				}

				try {
					populateDataset(d, copy);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Dataset test = getDataset(newname, group);

				if (test != null) {
					logger.debug("Searched and Found new dataset:"
							+ test.getName());

				} else {
					logger.debug("copyIntoSameGroup failed for:" + newname);
				}

			} else {
				logger.error("Could not find " + d.getName() + "'s group");
			}

		}
	}

	public void ApplyMasks(Dataset[] datavars, Dataset[] masks,
			List<DQSSNumber> FillValues) {

		for (int i = 0; i < masks.length; ++i) {
			try {
				if (datavars[i] != null && masks[i] != null) {
					HashMap<String, String> dvdim = new HashMap<String, String>();
					determineDatatype(datavars[i], dvdim);

					HashMap<String, String> maskdim = new HashMap<String, String>();
					determineDatatype(masks[i], maskdim);

					if (maskdim.get("type").equals(dvdim.get("type"))) {
						logger
								.debug("mask datatype and datavar datatype are the same.");
						ApplyMask(datavars[i], masks[i], dvdim, FillValues.get(i));
						if (HDFType.contains("HDF4")) {
							updateStructMetadata(datavars[i].getName(),
									DQSS.MaskName);
							updateStructMetadata(datavars[i].getName(),
									DQSS.OrigName);
						}
						else {
							updateStructMetadata5(datavars[i].getName(),
									DQSS.MaskName);
							updateStructMetadata5(datavars[i].getName(),
									DQSS.OrigName);
						}
						

					} else {
						logger
								.error("mask datatype and datavar datatype are different.");
					}
					logger.debug(maskdim.get("type") + " " + dvdim.get("type"));
				} else {
					if (datavars[i] != null) {
						logger.error("datavar dataset[" + i + "] is null");
					}
					if (masks[i] != null) {
						logger.error("mask dataset[" + i + "] is null");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Catching determineDatatype");
			}
		}

	}

	private void ApplyMaskN(Dataset data, Dataset mask, HashMap dimtype)
			throws Exception {
		int[] dims = (int[]) dimtype.get("dims");
		Number fill = this.getFillValueN(data);
		Number check = 32;

		Number[] dataRead = (Number[]) data.read();
		Number[] maskRead = (Number[]) mask.read();

		logger.debug("Doing  rank 2");
		if (data.getRank() == 2) {
			for (int i = 0; i < dims[0] * dims[1]; ++i) {
				if (maskRead[i].floatValue() == 1) {
					logger.debug("Comparing rank 2");
				} else {
					dataRead[i] = fill;
				}
			}
			data.write(dataRead);

		}
		if (data.getRank() == 3) {

			long maskstart[] = mask.getStartDims();
			long datastart[] = data.getStartDims();
			int x, y;

			for (int layer = 0; layer < dims[2]; ++layer) {
				maskstart[2] = layer;
				datastart[2] = layer;
				dataRead = (Number[]) data.read();
				maskRead = (Number[]) mask.read();
				Number before;

				for (int col = 0; col < dims[0]; col++) {
					for (int row = 0; row < dims[1]; row++) {
						x = col * dims[1] + row;
						before = dataRead[x];
						if (maskRead[x].floatValue() == 1/* mask positive value */) {
							// leave the value there.

						} else { /* mask negative value */
							// This if block should help with stats.
							if (dataRead[x] != fill && x % 100 == 0) {
								dataRead[x] = check;
								logger.debug("Masking Pixel: (Number)"
										+ data.getName() + "::" + layer + ":"
										+ col + ":" + row + "(" + before + ")");
							}
						}
					}
				}
				data.write(dataRead);
			}

		}

	}

	private void ApplyMask(Dataset data, Dataset mask, HashMap dimtype,
			DQSSNumber fill) throws Exception {
		Stopwatch s = new Stopwatch();
		s.start();
		mask.init(); /* done to put certain data members of Dataset in memory */
		data.init();

		long[] dims = mask.getDims();

		float check = (float) 32.0;

		if (dimtype.get("type").equals("INT16")) {
			if (data.getRank() == 2) {
				short[] dataRead = (short[]) data.read();
				short[] maskRead = (short[]) mask.read();

				for (int i = 0; i < dims[0] * dims[1]; ++i) {
					if (maskRead[i] == 1) {

					} else {

						dataRead[i] = fill.shortValue();

					}

				}
				data.write(dataRead);

			}
			else if (data.getRank() == 3) {

				long maskstart[] = mask.getStartDims();
				long datastart[] = data.getStartDims();
				int x;

				for (int layer = 0; layer < dims[2]; ++layer) {
					maskstart[2] = layer;
					datastart[2] = layer;

					short[] dataRead = (short[]) data.read();
					short[] maskRead = (short[]) mask.read();

					for (int col = 0; col < dims[0]; col++) {
						for (int row = 0; row < dims[1]; row++) {
							x = (int) (col * dims[1] + row);

							if (maskRead[x] == 1 /* mask positive value */) {
								// leave the value there.
								// This confirmed that we were writing values to
								// upper layers: dataRead[x] = 44;

							} else { /* mask negative value */
								// This if block should help with stats.
								if (!fill.compare(dataRead[x])) {
									dataRead[x] = fill.shortValue();

								}

							}

						}

					}
					data.write(dataRead);
				}
			}

		}
		if (dimtype.get("type").equals("FLOAT32")) {
			if (data.getRank() == 2) {

				float[] dataRead = (float[]) data.read();
				float[] maskRead = (float[]) mask.read();

				for (int i = 0; i < dims[0] * dims[1]; ++i) {
					if (maskRead[i] == 1) {

					} else {

						dataRead[i] = fill.floatValue();

					}

				}
				data.write(dataRead);

			}

			else if (data.getRank() == 3) {

				long maskstart[] = mask.getStartDims();
				long datastart[] = data.getStartDims();
				int x;

				for (int layer = 0; layer < dims[2]; ++layer) {
					maskstart[2] = layer;
					datastart[2] = layer;

					float[] dataRead = (float[]) data.read();
					float[] maskRead = (float[]) mask.read();

					for (int col = 0; col < dims[0]; col++) {
						for (int row = 0; row < dims[1]; row++) {
							x = (int) (col * dims[1] + row);

							if (maskRead[x] == 1 /* mask positive value */) {
								// leave the value there.
								// This confirmed that we were writing values to
								// upper layers: dataRead[x] = 44;

							} else { /* mask negative value */
								// This if block should help with stats.
								if (!fill.compare(dataRead[x])) {
									dataRead[x] = fill.floatValue();

								}

							}

						}

					}
					data.write(dataRead);
				}

			} else if (data.getRank() == 5) {
				long maskstart[] = mask.getStartDims();
				long datastart[] = data.getStartDims();

				for (int layer = 0; layer < dims[2]; ++layer) {
					datastart[2] = layer; // this sets the layer outside the
					// class as dims are passed by
					// reference.
					maskstart[2] = layer;
					for (int airsdim = 0; airsdim < dims[3]; ++airsdim) {
						datastart[3] = airsdim;
						maskstart[3] = airsdim;
						for (int cldlyr = 0; cldlyr < dims[4]; ++cldlyr) {
							datastart[4] = cldlyr;
							maskstart[4] = cldlyr;
							float[] dataData = (float[]) data.read();
							float[] maskData = (float[]) mask.read();

							for (int x = 0; x < dataData.length; ++x) {
								if (maskData[x] == 1 /* mask positive value */) {
									// leave the value there.
								} else {
									// This if block should help with stats.
									if (!fill.compare(dataData[x])) {
										dataData[x] = fill.floatValue();

									}
								}
							}
							data.write(dataData);
						} // cldlyr
					} // airsdim

				} // layer
			} // if rank == 5
			else {
				logger.error("This dataset: " + data.getName() + " has rank:"
						+ data.getRank() + " we aren't handling");
			}

		} // if FLOAT32
		s.stop();
		logger.debug(data.getName() + ",   rank:" + data.getRank()
				+ ", screening elapsed time: " + s.getElapsedTime());
	}

	/*
	 * revive this if you need to get group by dataset name
	 */
	public Group getGroupOfDataset(String dataVariable) {
		Dataset d = getDataset(dataVariable, topGroup);
		if (d != null) {
			String fullname = d.getFullName();
			String[] parts = fullname.split("/");
			String datasetGroup = parts[parts.length - 2];
			logger.debug("trying to return group:" + datasetGroup);
			Group g = getGroup(datasetGroup, topGroup);
			/*
			 * getGroup returns last group if none found.
			 */
			if (!g.getName().contains(datasetGroup)) {
				g = null;
			}
			return g;
		} else {
			return null;
		}
	}

	/*
	 * All of the methods below (except for main() ) is leftover from my proof
	 * of concept code. Some of the subroutines I actually use -
	 * determineDataType, determineDimension Some I use for debugging =
	 * displayValues...
	 */
	private static void printGroup(Group g, FileFormat testFile)
			throws Exception {
		if (g == null)
			return;

		List members = g.getMemberList();

		int n = members.size();
		HObject obj = null;
		int objectid;
		for (int i = 0; i < members.size(); i++) {
			obj = (HObject) members.get(i);

			/*
			 * Each Dataset (SDS) is a member of a Group In the AIRS file I
			 * worked with the groups were:
			 * L2_Standard_atmospheric&surface_product Geolocation Fields SDS
			 * Fields Swath Attributes
			 */
			if (obj instanceof Dataset) {
				ReadAndDisplaySDS(obj, testFile);
			} else {
				logger.debug("Not trying to print Group:" + obj.getName());
			}
			if (obj instanceof Group) {
				printGroup((Group) obj, testFile);
			}
		}
	}

	/**
	 * create the file and add groups ans dataset into the file, which is the
	 * same as javaExample.H4DatasetCreate
	 * 
	 * @see javaExample.H4DatasetCreate
	 * @throws Exception
	 */

	private static boolean listable(Dataset dataset) {
		if (dataset.hasAttribute()) {
			return true;
		}
		return false;
	}

	public static void determineDatatype(Dataset dataset,
			HashMap<String, String> sizes) throws Exception {
		Datatype HType = null;
		dataset.init(); // needs to be called before Dataset.getDatatype() also
		if (DataFile.HDFType.equals("HDF5")) {
			H5Datatype H5Type = (H5Datatype) dataset.getDatatype();
			HType = H5Type;
		} else {
			H4Datatype H4Type = (H4Datatype) dataset.getDatatype();
			HType = H4Type;
		}
		logger.debug("datatype description:" + HType.getDatatypeDescription());

		if (HType.isUnsigned()) {
			// DQSS.ReportError("dataset " + dataset.getName() +
			// " is unsigned");
		} else {
			// DQSS.ReportError("dataset " + dataset.getName() +
			// " returns that it is NOT unsigned");
		}

		sizes.put("name", dataset.getName());
		if (HType.getDatatypeDescription().contains("float")) {
			if (HType.getDatatypeSize() == 4) {
				sizes.put("type", "FLOAT32");
			} else if (HType.getDatatypeSize() == 8) {
				sizes.put("type", "FLOAT64");
			} else {
				sizes.put("type", "unknown");
			}
		}
		if (HType.getDatatypeDescription().contains("integer")) {
			if (HType.getDatatypeSize() == 1) {
				sizes.put("type", "INT8");
			} else if (HType.getDatatypeSize() == 2) {
				sizes.put("type", "INT16");
			} else if (HType.getDatatypeSize() == 4) {
				sizes.put("type", "INT32");
			} else {
				sizes.put("type", "unknown");
			}
		}

	}

	/*
	 * Not using this anymore. Populating Hashmap only in DetermineDatatype()
	 * with type. getting dims and rank directly from Dataset call.
	 */
	private static void determineDimensions(Dataset dataset, HashMap h) {

		long[] dims = (long[]) dataset.getDims();
		int[] d = new int[dims.length];

		logger.info(dataset.getName());
		logger.debug("\tlength:" + d.length);
		logger.debug("\trank:" + dataset.getRank());
		logger.debug("\trank:" + dataset.getRank());
		logger.debug("\tpath:" + dataset.getPath());
		logger.debug("\tlength:" + d.length);
		for (int i = 0; i < d.length; ++i) {
			if (dims[i] > 0) {
				logger.info("\tdimensions:" + dims[i]);
				d[i] = (int) dims[i];
			}
		}

		h.put("n", d.length);
		h.put("dims", d);

	}

	private static void ReadAndDisplaySDS(HObject obj, FileFormat ff)
			throws Exception {

		Dataset dataset = (Dataset) obj;
		/*
		 * Determine if dataset is listable
		 */
		if (listable(dataset)) {
			HashMap dimension = new HashMap();
			determineDatatype(dataset, dimension);

			long[] dims = dataset.getDims();
			int x = (int) dims[0];
			int y = (int) dims[1];
			int z = -1;
			if (dims.length > 2) {
				z = (int) dims[2];
			}

			logger.debug("SDS:Name:" + dimension.get("name"));
			logger.debug("Type:" + dimension.get("type"));

			if (dimension.get("type") == "INT16") {
				short[] dataRead = (short[]) dataset.read();
				// displayValues(x,y,dataRead);
				displayMinMax(x, y, dataRead);
			}
			if (dimension.get("type") == "INT32") {
				int[] dataRead = (int[]) dataset.read();
				// displayValues(x,y,dataRead);
				displayMinMax(x, y, dataRead);
			}
			if (dimension.get("type") == "FLOAT32") {

				if (dims.length == 3) {
					// display_ncdumpstyle(dataset,x,y,z);
					// displayValues(x,y,z,dataset);
					displayMinMax(x, y, z, dataset);
				} else {
					float[] dataRead = (float[]) dataset.read();
					// displayValues(x,y,dataRead);
					displayMinMax(x, y, dataRead);
				}
			}
			if (dimension.get("type") == "FLOAT64") {
				double[] dataRead = (double[]) dataset.read();
				// displayValues(x,y,dataRead);
				displayMinMax(x, y, dataRead);
			}
		}
	}

	public boolean TheirwriteStatsToMetadata(Dataset dataset, Statistics stats)
			throws HDFException {
		// create 2D 32-bit (4 bytes) integer dataset of 20 by 10
		/*
		 * long[] attDims = { 1 }; String percentage = stats.getPercentage() +
		 * "% pixels retained"; String[] statsValue = { percentage }; Datatype
		 * attrType = new H4Datatype(Datatype.CLASS_STRING,
		 * statsValue[0].length() + 1, -1, -1);
		 */
		try {
			Datatype dtype = file4obj.createDatatype(Datatype.CLASS_INTEGER, 4,
					Datatype.NATIVE, Datatype.NATIVE);
			long[] attrDims = { 1 }; // 1D of size two
			String[] attrValue = { stats.getPercentage() }; // attribute value

			// create a attribute of 1D integer of size two
			Attribute attr = new Attribute(dataset.getName()
					+ " Percentage Retained", dtype, attrDims);
			attr.setValue(attrValue); // set the attribute value

			// attach the attribute to the dataset
			topGroup.writeMetadata(attr);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	/*
	 * We needed completely different methods because HDF4 does not have
	 * a string datatype. 
	 * HDF4 has a char datatype and therefore array size needs to be length
	 * of array
	 * HDF5 has a string datatype and therefore array size is just one.
	 */
	public Attribute getYourHDF5Attribute(String attrname, String[] value)
			throws Exception {
		if (topGroup.hasAttribute()) {
			logger.debug("topGroup has Attributes");
		}
		List<Attribute> attributes = topGroup.getMetadata();
		Iterator itr = attributes.iterator();
		while (itr.hasNext()) {
			Attribute a = (Attribute) itr.next();
			int type = a.getType().getDatatypeClass();

			if (a.getName().equals(attrname)) {
				logger.debug("Found attribute:" + a.getName());

				return a;
			}
		}
		long[] attrDims = { value.length };

		Datatype	attrType = new H5Datatype(Datatype.CLASS_STRING,
					value[0].length() + 1, -1, -1);
		Attribute attr = new Attribute(attrname, attrType, attrDims);
		logger.debug("didn't find attribute so...creating attribute:"
				+ attr.getName());
		return attr;
	}
	/*
	 * We needed completely different methods because HDF4 does not have
	 * a string datatype. 
	 * HDF4 has a char datatype and therefore array size needs to be length
	 * of array
	 * HDF5 has a string datatype and therefore array size is just one.
	 */
	public Attribute getYourHDF4Attribute(String attrname, String[] value)
			throws Exception {
		if (topGroup.hasAttribute()) {
			logger.debug("topGroup has Attributes");
		}
		List<Attribute> attributes = topGroup.getMetadata();
		Iterator itr = attributes.iterator();
		while (itr.hasNext()) {
			Attribute a = (Attribute) itr.next();
			int type = a.getType().getDatatypeClass();

			if (a.getName().equals(attrname)) {
				logger.debug("Found attribute:" + a.getName());

				return a;
			}
			else {
				logger.info("Found attribute:" + a.getName());
			}
		}
		long[] attrDims = { value[0].length() + 1 };

		Datatype	attrType = new H4Datatype(Datatype.CLASS_STRING,
					value[0].length() + 1, -1, -1);
		Attribute attr = new Attribute(attrname, attrType, attrDims);
		logger.debug("didn't find attribute so...creating attribute:"
				+ attr.getName());
		return attr;
	}

	public Attribute getYourAttribute(Group group, String attrname, int dimsize)
			throws Exception {

		List<Attribute> attributes = group.getMetadata();
		Iterator itr = attributes.iterator();
		while (itr.hasNext()) {
			Attribute a = (Attribute) itr.next();
			int type = a.getType().getDatatypeClass();

			if (a.getName().equals(attrname)) {
				logger.debug("Found attribute:" + a.getName());

				return a;
			}
		}
		long[] attrDims = { dimsize };
		Datatype attrType = new H4Datatype(Datatype.CLASS_STRING, dimsize + 1,
				-1, -1);
		Attribute attr = new Attribute(attrname, attrType, attrDims);
		logger.debug("didn't find attribute so...creating attribute:"
				+ attr.getName());
		return attr;
	}

	public boolean writeStatsToMetadata(Attribute stats_attr,
			String[] statsValue) throws HDFException, Exception {

		Group fileAttributes = getGroup("FILE_ATTRIBUTES",topGroup);
		/*
		 * getGroup is recursive and returns the last Group it found if none found.
		 */
		if (!fileAttributes.getName().contains("FILE_ATTRIBUTES")) {
			fileAttributes = null;
		}
		if (stats_attr != null) {
			try {
				stats_attr.setValue(statsValue); // set the attribute value
				/*
				 * Couldn't write to MLS Global metadata. It doesn't have any.
				 * Turns out that HDF objects always need arrays so the 3rd
				 * argument of new Attribute is an array length but it itself
				 * needs to be an array. And the value for the Attribute needs
				 * to be an array also even though it is one value.
				 */
				if (false /* testing */&& !topGroup.hasAttribute()) {
					String[] arrayForHDF = { statsValue[0] };

					long[] lengthOfArray = { arrayForHDF.length };
					Datatype attrType = null;

					attrType = new H5Datatype(Datatype.CLASS_STRING,
							statsValue[0].length() + 1, -1, -1);
					Attribute attr = new Attribute("Quality String", attrType,
							lengthOfArray);
					attr.setValue(arrayForHDF);
					topGroup.writeMetadata(attr);

				} else {
					if (fileAttributes != null) {
						fileAttributes.writeMetadata(stats_attr);
					} else {
						topGroup.writeMetadata(stats_attr);
					}
				}

			} catch (NullPointerException e) {
				e.printStackTrace();
				logger.error("writeAttr failed for:" + stats_attr.getName());
			}
		} else {
			logger.error("Attribute creation failed for:"
					+ stats_attr.getName() + " " + statsValue);
		}

		return true;
	}
/*
 * This method is probably only for MLS... we'll see.
OBJECT=DataField_1
	DataFieldName="L2gpValue"
	DataType=H5T_NATIVE_FLOAT
	DimList=("nTimes","nLevels")
	MaxdimList=("nTimes","nLevels")
END_OBJECT=DataField_1
*/
	public boolean updateStructMetadata5(String datasetName, String suffix)	throws HDFException, Exception {
		Dataset d = getDatasetRecurse("StructMetadata.0", topGroup);
		String data[] = (String[]) d.getData();
		String section = "SwathName=\"" + datasetName + "\"";
		int isearchSection = data[0].indexOf(section);
        
		int isearch = data[0].indexOf("GROUP=DataField", isearchSection);
		int pos = data[0].indexOf("END_GROUP=DataField", isearchSection);
		String search = data[0].substring(isearch,pos);
		Integer count = countOccurrences(search,"DataField_")/2 + 1;
		if (count > 20) {
			logger.warn("Expected 5 or 6 existing DataFields in StructMetadata but found " + count);
		}
		if (count < 1) {
			logger.error("StructMetadata error in Screening process. Incorrect number of existing datasets");
		}
		String newname = datasetName + suffix;
		String n = count.toString();
		String newdatasets = 
"\tOBJECT=DataField_" + n + "\n" +
"\t\t\t\tDataFieldName=\""+ newname + "\"\n" +
"\t\t\t\tDataType=H5T_NATIVE_FLOAT\n" +
"\t\t\t\tDimList=(\"nTimes\",\"nLevels\")\n" +
"\t\t\t\tMaxdimList=(\"nTimes\",\"nLevels\")\n" +
"\t\t\tEND_OBJECT=DataField_"+ n + "\n\t\t" ;
	
		String[] before = new String[1];
		
		before[0] = data[0].substring(0, pos) + newdatasets + 	data[0].substring(pos,data[0].length()); 	
		data[0] = before[0];
		d.write(data);
	    data = (String[]) d.getData();
	    if (! data[0].contains(suffix)) {
	    	logger.error("Addition to StructMetadata failed for " + datasetName + suffix);
	    }
		logger.debug(data[0]);
		return true;
	}
	
	public static int countOccurrences(String haystack, String needle)
	{
	    int count = 0;
	    for (int i=0; i < haystack.length(); i++)
	    {
	    	String part = haystack.substring(i);
	        if (part.startsWith(needle))
	        {
	             count++;
	        }
	    }
	    return count;
	}

	public boolean updateStructMetadata(String datasetName, String suffix)
			throws HDFException, Exception {
		Dataset d = getDatasetRecurse(datasetName, topGroup);
		d.init();
		String[] dims = d.getDimNames();
		
		/*
		 * MLS doesn't have dimension names.
		 */
		if (dims == null) {
			int rank = d.getRank();
			String[] unnamed = new String[rank]; 
			for (int i=0; i< rank; ++i) {
				unnamed[i] = "unnamed" + i;
			}
			dims = unnamed;
		}
		HashMap<String, String> dvdim = new HashMap<String, String>();
		determineDatatype(d, dvdim);
		String type = "DFNT_" + dvdim.get("type");

		String dimensions = "";
		if (dims != null) {
			//Dataset x = getDatasetRecurse("StructMetadata.0", topGroup);
			//String data[] = (String[]) x.getData();
			for (int i = 0; i < dims.length; ++i) {
				int pos = dims[i].indexOf(":");
				if (pos > 0) {
					String removeAfterColon = dims[i].substring(0, pos);
					dimensions += "\"" + removeAfterColon + "\",";
				}
				else {
					dimensions += "\"" + dims[i] + "\",";
				}
			}
		}
		dimensions = dimensions.substring(0, dimensions.length() - 1);
		//"Cell_Along_Swath","Cell_Across_Swath"
		/*
		 * DFNT_UCHAR 3 DFNT_CHAR 4 DFNT_FLOAT32 5 DFNT_FLOAT64 6 DFNT_INT8 20
		 * DFNT_UINT8 21 DFNT_INT16 22 DFNT_UINT16 23 DFNT_INT32 24 DFNT_UINT32
		 * 25 DFNT_INT64 26 DFNT_UINT64 27
		 */
		Group newg = this.getGroupOfDataset(datasetName);
		Integer nMembers = newg.getMemberList().size();
		logger.debug("Group " + newg.getName() + " has " + nMembers
				+ " members ");

		String location = "\t\tEND_GROUP=DataField\n";

		String tmplt = "\t\t\tOBJECT=DataField_MEMBERP1\n"
				+ "\t\t\t\tDataFieldName=\"DATASETNAME\"\n"
				+ "\t\t\t\tDataType=TYPE\n" + "\t\t\t\tDimList=(DIMENSIONS)\n"
				+ "\t\t\tEND_OBJECT=DataField_MEMBERP1\n\t";

		// "\t\t\t\tDimList=(\"Cell_Along_Swath_1km\",\"Cell_Across_Swath_1km\")\n"
		// +

		String datavariableMetadata = tmplt.replace("MEMBERP1", nMembers
				.toString());
		datavariableMetadata = datavariableMetadata.replace("DATASETNAME",
				datasetName);

		// String assocVarMetadata = tmplt.replace("MEMBERP1",
		// nMembers.toString());
		String assocVarMetadata = tmplt.replace("DATASETNAME", datasetName
				+ suffix);
		assocVarMetadata = assocVarMetadata.replace("DIMENSIONS", dimensions);
		assocVarMetadata = assocVarMetadata.replace("TYPE", type);

		if (datasetName != null) {
			try {

				List globalMetadata = topGroup.getMetadata();
				logger.debug("getmetadata worked for:" + datasetName);
				for (int i = 0; i < globalMetadata.size(); ++i) {
					Attribute item = (Attribute) globalMetadata.get(i);
					if (item.getName().contains("StructM")) {
						logger.debug("Found StructMetadata");
						String[] data = (String[]) item.getValue();
						if (data[0].contains(location)) {
							String str4count = data[0];
							nMembers = NumberOfOccurences(str4count,
									"END_OBJECT=DataField_");
							logger.debug("StructMetadata.0 contains "
									+ datasetName);
							int before = data[0].indexOf(location);
							String sub1 = data[0].substring(0, before);
							String sub2 = data[0].substring(before + 1, data[0]
									.length());
							assocVarMetadata = assocVarMetadata.replace(
									"MEMBERP1", nMembers.toString());
							String newdata = sub1 + assocVarMetadata + sub2;
							data[0] = newdata;
							logger.debug(data[0]);
							// item.setValue(data);
							long[] attrDims = { newdata.length() };
							Datatype attrType = new H4Datatype(
									Datatype.CLASS_STRING,
									newdata.length() + 1, -1, -1);
							Attribute attr = new Attribute(item.getName(),
									attrType, attrDims);
							attr.setValue(data);
							topGroup.writeMetadata(attr);
							return true;
						}
					}
				}

			} catch (NullPointerException e) {
				e.printStackTrace();
				logger.error("writeAttr failed for:" + datasetName);
			}
		} else {
			logger.error("Attribute creation failed for:" + datasetName);
		}

		return true;
	}

	private int NumberOfOccurences(String data, String item) {
		int count = 0;
		String tmp = "";
		int match = 1;

		while (match > 0 & match < 1000) {
			++match;
			tmp = data.replaceFirst(item, "");
			if (tmp.equals(data)) {
				match = 0;
			}
			++count;
			data = tmp;

		}

		return count;

	}

	private void display_ncdumpstyle(Dataset dataset, int x, int y, int z)
			throws Exception {
		int rank = dataset.getRank(); // number of dimension of the dataset
		long[] dims2 = dataset.getDims(); // the dimension sizes of the dataset
		long[] selected = dataset.getSelectedDims(); // the selected size of the
		// dataet
		long[] start = dataset.getStartDims(); // the off set of the selection
		long[] stride = dataset.getStride(); // the stride of the dataset
		int[] selectedIndex = dataset.getSelectedIndex(); // the selected
		// dimensions for
		// display

		// select dim1 and dim2 as 2D data for display,and slice through dim0
		start[0] = 0;
		start[1] = 0;
		start[2] = 2;
		logger.debug("start:" + start[0] + " " + start[1] + " " + start[2]);
		// Dataset.read() reads orthogonally to ncdump. I'll know better when I
		// ask
		// Young-In about what ncdump.
		float[][] dataRead = new float[z][x * y];
		for (int startdim = 0; startdim < z; ++startdim) {
			// when dataset.read() is called, the slection above will be used
			// since
			// the dimension arrays is passed by reference. Changes of these
			// arrays
			// outside the dataset object directly change the values of these
			// array
			// in the dataset object.
			start[2] = startdim;
			float[] data = (float[]) dataset.read();
			for (int i = 0; i < data.length; ++i) {
				dataRead[startdim][i] = data[i];
				if (i == 0) {
					logger.info(data[i] + ",");
				}
			}
		}
		for (int c = 0; c < x * y; ++c) {
			for (int w = 0; w < z; ++w) {
				logger.info(dataRead[w][c] + ",");
			}

		}
	}

	private static void displayValues(int x, int y, short[] dataRead) {
		for (int i = 0; i < y; i++) {
			DQSS.ReportError("\n" + dataRead[i * x], 3);
			for (int j = 1; j < x; j++) {

			}
		}

	}

	private static void displayValues(int x, int y, int[] dataRead) {
		for (int i = 0; i < y; i++) {
			// DQSS.ReportError("\n"+dataRead[i*x],3);
			for (int j = 1; j < x; j++) {
				// DQSS.ReportError(", "+dataRead[i*x+j],3);
			}
		}

	}

	private static void displayValues(int x, int y, float[] dataRead) {
		for (int i = 0; i < x * y; i++) {

			// DQSS.ReportError(dataRead[i] + ", ",3);
			if (i % 5 == 0) {
				// DQSS.ReportError("\n",3);
			}
		}

	}

	private static void displayValues(int x, int y, int z, Dataset dataset)
			throws Exception {
		long[] start = dataset.getStartDims(); // the off set of the selection
		for (int layer = 0; layer < z; ++layer) {
			start[2] = layer; // get to next layer
			for (int i = 0; i < x * y; i++) {
				float[] dataRead = (float[]) dataset.read();
				if (dataRead[i] > 0) {
					// DQSS.ReportError(dataRead[i] + ", ",3);
				}
				if (i % (x * y - 1) == 0) {
					// DQSS.ReportError("\n layer " + layer,3);
				}
			}
		}

	}

	private static void displayValues(int x, int y, double[] dataRead) {
		int f = 1;
		for (int i = 0; i < x * y; i++) {

			DQSS.ReportError(dataRead[i] + ", ", 3);
			if (f % 4 == 0) {
				DQSS.ReportError("\n", 3);
			}
			if ((f) % 30 == 0) {
				DQSS.ReportError("\n\n", 3);
				f = 0;
			}
			++f;
		}

	}

	private static void displayMinMax(int x, int y, short[] dataRead) {
		if (dataRead.length > 1) {

			short max = dataRead[0];
			short min = dataRead[0];
			for (int i = 0; i < y; i++) {
				for (int j = 1; j < x; j++) {
					if (dataRead[i * x + j] < min) {
						min = dataRead[i * x + j];
					}
					if (dataRead[i * x + j] > max) {
						max = dataRead[i * x + j];
					}
				}
			}
			logger.debug("Min:" + min + " Max:" + max);

		}
	}

	private static void displayMinMax(int x, int y, int[] dataRead) {
		int max = dataRead[0];
		int min = dataRead[0];
		for (int i = 0; i < y; i++) {
			for (int j = 1; j < x; j++) {
				if (dataRead[i * x + j] < min) {
					min = dataRead[i * x + j];
				}
				if (dataRead[i * x + j] > max) {
					max = dataRead[i * x + j];
				}
			}
		}
		logger.debug("Min:" + min + " Max:" + max);

	}

	private static void displayMinMax(int x, int y, float[] dataRead) {
		float max = -1000;
		float min = 1000;
		for (int i = 0; i < y; i++) {
			for (int j = 1; j < x; j++) {
				if (dataRead[i * x + j] < min) {
					min = dataRead[i * x + j];
				}
				if (dataRead[i * x + j] > max) {
					max = dataRead[i * x + j];
				}
			}
		}
		logger.debug("Min:" + min + " Max:" + max);

	}

	private static void displayMinMax(int x, int y, int z, Dataset dataset)
			throws Exception {
		float max = -9999;
		float min = 10000;
		long[] start = dataset.getStartDims(); // the off set of the selection
		int f = 1;
		for (int layer = 0; layer < z; ++layer) {
			start[2] = layer; // get to next layer
			float[] dataRead = (float[]) dataset.read();
			for (int i = 0; i < x * y; i++) {
				if (dataRead[i] < min) {
					min = dataRead[i];
				}
				if (dataRead[i] > max) {
					max = dataRead[i];
				}
			}
		}
		logger.debug("Min:" + min + " Max:" + max);

	}

	private static void displayMinMax(int x, int y, double[] dataRead) {
		double max = dataRead[0];
		double min = dataRead[0];
		for (int i = 0; i < y; i++) {
			for (int j = 1; j < x; j++) {
				if (dataRead[i * x + j] < min) {
					min = dataRead[i * x + j];
				}
				if (dataRead[i * x + j] > max) {
					max = dataRead[i * x + j];
				}
			}
		}
		logger.debug("Min:" + min + " Max:" + max);

	}

	public void NavigateSwathGroup() {

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) file4obj
				.getRootNode();
		H4SDS sds = null;
		DefaultMutableTreeNode node = null;
		if (root != null) {
			TreeNode rootnode = root.getLastChild();

			Enumeration groups = rootnode.children();

			while (groups.hasMoreElements()) {
				node = (DefaultMutableTreeNode) groups.nextElement();
				TreeNode parent = node.getParent();
				Enumeration swaths = node.children();

				Enumeration swch = node.depthFirstEnumeration();
				if (swaths == null) {
					return;
				}
				while (swaths.hasMoreElements()) {
					node = (DefaultMutableTreeNode) swaths.nextElement();
					Object obj = node.getUserObject();
					if (obj instanceof H4SDS) {
						sds = (H4SDS) obj;
						System.out.println(sds);

						// test H4CompoundDS attributes
						Attribute attr = null;
						List info = null;
						try {
							info = sds.getMetadata();
						} catch (Exception ex) {
							System.out.println(ex);
						}

						int n = 0;
						if (info != null) {
							n = info.size();
							for (int i = 0; i < n; i++) {
								attr = (Attribute) info.get(i);
								System.out.println(attr);
								if (attr.getName().contains("FV")) {
									System.out.println(attr);
								}
							}
						}

						// data
						Object data = null;
						try {
							data = sds.read();
						} catch (Exception ex) {
							System.out.println(ex);
						}

						if ((data != null) && data.getClass().isArray()) {
							// print out the first 1000 data points
							n = Math.min(Array.getLength(data), 1000);
							StringBuffer sb = new StringBuffer();
							for (int j = 0; j < n; j++) {
								sb.append(Array.get(data, j));
								sb.append(" ");
							}
							System.out.println(sb.toString());
						}
					} // if (obj instanceof H4Group
				} // while (nodes.hasMoreElements())
			}
		}// if (root != null)
	}

	public boolean deleteDataset(Dataset d) {
		try {
			file4obj.delete(d);
		} catch (UnsupportedOperationException e) {
			logger.error("Deletion of Datasets is unsupported");
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void close() {
		try {
			if (file4obj != null) {
				file4obj.close();
			}
			if (file5obj != null) {
				file5obj.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void end() {
		try {
			HDFLibrary.SDend(sd_id);
		} catch (HDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getFileName() {
		return file4obj.getName();
	}

	/*
	 * public abstract int get_nConsRef(); public abstract int get_nRef();
	 * public abstract int get_nCoordCons(); public abstract int
	 * get_nQualLevel(); public abstract List<Long> get_mins(); public abstract
	 * List<Long> get_maxs(); public abstract List<Long> get_criteria(); public
	 * abstract List<String> get_qs_types();
	 */
	public int get_nConsRef() {
		return this.nConsRef;
	}

	public int get_nRef() {
		return this.nRef;
	}

	public int get_nCoordCons() {
		return this.nCoordCons;
	}

	public int get_nQualLevel() {
		return this.nQualLevel;
	}

	public List<Long> get_mins() {
		return this.mins;
	}

	public List<Long> get_maxs() {
		return this.maxs;
	}

	public List<Long> get_criteria() {
		return this.crit2num;
	}

	public List<String> get_qs_types() {
		return this.sf_types;
	}

	public List<String> get_direction() {
		return this.qualev_direction;
	}

	public List<String> get_ccrel() {
		return this.sf_cnrel;
	}

	public String[] getDimensionNames(String DatasetName) {

		Dataset d = getDataset(DatasetName, topGroup);
		if (d == null) {
			logger.warn("getDataset call failed. This can happen sometimes because not all MOD04" +
			" have the deep blue variables. So we need to skip it and move on to the next.");
			return null;
		}
		d.init();
		String[] dimensionNames = new String[d.getRank()];
		dimensionNames = d.getDimNames();
		if (dimensionNames == null) {
			/*
			 * If they don't exist in the hdf file (MLS) get them from the StructMeta.0 dataset 
			 */
			dimensionNames = getDimNamesFromStructMeta(d.getRank(),DatasetName) ;
			if (dimensionNames == null) {
			
				dimensionNames = new String[DF.dims.size()];
					for (int i = 0; i < DF.dimsize.length; ++i) {
						/*
						 * If they don't exist in the hdf file (MLS) get the ones you
						 * created for the datafield class
						 */
						dimensionNames[i] = DF.dims.get(i);
					}
			}
		}
		return dimensionNames;
	}

	public String[] getDimNamesFromStructMeta(int rank, String datasetName) {

		Dataset d = getDatasetRecurse("StructMetadata.0", topGroup);
		String[] dimensionNames = new String[rank];
		String data[];
		/*
		 * Even if this is used in an unexpected context, the dimnames
		 * of x_orig and x_qcmask will always be the same as with x.
		 */
		if (datasetName.contains(DQSS.MaskName)) {
			datasetName = datasetName.replace(DQSS.MaskName, "");
		}
		if (datasetName.contains(DQSS.OrigName)) {
			datasetName = datasetName.replace(DQSS.OrigName, "");
		}
		try {
			data = (String[]) d.getData();
			int isearch = data[0].indexOf(datasetName);
			if (isearch < 0) {
				isearch = data[0].indexOf("L2gpValue");
			}
			if (isearch > 0) {
				
				String dsStruct = data[0].substring(isearch,data[0].length());
				
			
		        // Create a pattern to match breaks
		        
				//Pattern p = Pattern.compile("DimList=\\(\"(\\w+)\"\\)");
				/* DimList=("ntimes","nLevels")
				 * Pattern Matching in Java
				 * It looks like you group the things you want with () lie in perl
				 * You escape " with single backslash
				 * escape parens with double backslash
				 * change \w to \\w
				 */
				if (rank == 1) {
					Pattern p = Pattern.compile("DimList=\\(\"(\\w+)\"\\)");
			        Matcher m = p.matcher(dsStruct);
			        if ( m.find() ) {
			            dimensionNames[0] = m.group(1);

			        }
				}
				else if (rank == 2) {
					Pattern p = Pattern.compile("DimList=\\(\"(\\w+)\",\"(\\w+)\"\\)");
			        Matcher m = p.matcher(dsStruct);
			        if ( m.find() ) {
			            dimensionNames[0] = m.group(1);
			            dimensionNames[1] = m.group(2);
			        }
				}
			}

			
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dimensionNames;
	}
	public String[] oldgetDimensionNames(String DatasetName) {

		Dataset d = getDataset(DatasetName, topGroup);
		d.init();
		String[] dimensionNames = new String[d.getRank()];
		dimensionNames = d.getDimNames();
		if (dimensionNames == null) {
			dimensionNames = new String[dims.size()];
			long[] h5dims = d.getDims();
			Long ldims[] = new Long[h5dims.length];

			for (int i = 0; i < d.getRank(); ++i) {
				ldims[i] = h5dims[i];
				dimensionNames[i] = ldims[i].toString();
				++i;
			}

			logger.debug("Extracted dims include:" + h5dims[0] + ", "
					+ h5dims[1]);
		}
		return dimensionNames;
	}

	public void setHE5StringAttribute(String attr, String DatasetName, String value) {
		Dataset d = getDataset(DatasetName, topGroup);
		if (d == null) {
			logger.warn("getDataset call failed. This can happen sometimes because not all MOD04" +
			" have the deep blue variables. So we need to skip it and move on to the next.");
			return;
		}
		d.init();
		H5ScalarDS dh5 = (H5ScalarDS) d;
		List<Attribute> list = new ArrayList<Attribute>();
		try {
			list = dh5.getMetadata();
		} catch (HDF5Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("HDF5 getMetadata() call failed for:" + attr + ", "
					+ DatasetName);
		}
		/*
		 * New Attribute:
		 */

		long[] attrDims = { 1 };
		String attrName = attr;
		String[] classValue = { value };
		Datatype attrType = new H5Datatype(Datatype.CLASS_STRING, classValue[0]
				.length() + 1, -1, -1);
		Attribute newAttr = new Attribute(attrName, attrType, attrDims);
		newAttr.setValue(classValue);
		try {
			// writeStatsToMetadata(newAttr,StatsArray);
			dh5.writeMetadata(newAttr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setHE5Attribute(String DatasetName, Attribute attr) {
		Dataset d = getDataset(DatasetName, topGroup);
		if (d == null) {
			logger.warn("getDataset call failed. This can happen sometimes because not all MOD04" +
			" have the deep blue variables. So we need to skip it and move on to the next.");
			return;
		}
		d.init();
		H5ScalarDS dh5 = (H5ScalarDS) d;
		List<Attribute> list = new ArrayList<Attribute>();
		try {
			list = dh5.getMetadata();
		} catch (HDF5Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("HDF5 getMetadata() call failed for:" + attr + ", "
					+ DatasetName);
		}
		/*
		 * New Attribute from another dataset:
		 */	
		try {
	
	
				dh5.writeMetadata(attr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * We needed completely different methods because HDF4 does not have
	 * a string datatype. 
	 * HDF4 has a char datatype and therefore array size needs to be length
	 * of array
	 * HDF5 has a string datatype and therefore array size is just one.
	 */
	public void setHE4Attribute(String attr, String DatasetName, String value) {
		Dataset d = getDataset(DatasetName, topGroup);
		if (d == null) {
			logger.warn("getH4Attribute call failed. This can happen sometimes because not all MOD04" +
			" have the deep blue variables. So we need to skip it and move on to the next.");
			return;
		}
		d.init();
		ScalarDS dh4 = (ScalarDS) d;
		List<Attribute> list = new ArrayList<Attribute>();
		try {
			list = dh4.getMetadata();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("HDF4 getMetadata() call failed for:" + attr + ", "
					+ DatasetName);
		}
		/*
		 * New Attribute:
		 */

		long[] attrDims = { value.length() +1};
		String attrName = attr;
		String[] classValue = { value };
		Datatype attrType = new H4Datatype(Datatype.CLASS_STRING,
				classValue.length + 1, -1, -1);
		Attribute newAttr = new Attribute(attrName, attrType, attrDims);
		newAttr.setValue(classValue);
		try {
			// writeStatsToMetadata(newAttr,StatsArray);
			d.writeMetadata(newAttr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * We needed completely different methods because HDF4 does not have
	 * a string datatype. 
	 * HDF4 has a char datatype and therefore array size needs to be length
	 * of array
	 * HDF5 has a string datatype and therefore array size is just one.
	 */
	public datafield instatiateProperDatafieldType(String datavariable) {
		if (DF != null) {
			return DF;
		}
		HashMap<String, String> dvdim = new HashMap<String, String>();
		Dataset d = getDataset(datavariable, topGroup);

		try {
			determineDatatype(d, dvdim);
			if (dvdim.get("type").contains("INT8")) {
				datafield df = new ShortDataField(datavariable);
				df.setDataType(dvdim.get("type"));
				return df;
			} else if (dvdim.get("type").contains("INT16")) {
				datafield df = new ShortDataField(datavariable);
				df.setDataType(dvdim.get("type"));
				return df;
			} else if (dvdim.get("type").contains("FLOAT32")) {
				datafield df = new FloatDataField(datavariable);
				df.setDataType(dvdim.get("type"));
				long[] dims = (long[]) d.getDims();
				df.setRank(dims.length);
				for (int i = 0; i < dims.length; ++i) {
					df.setDimSize(i, dims[i]);
				}
				return df;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public  String[] getDimNamesGen(Dataset d) {
		String[] names = d.getDimNames();
		if (names == null) {
			names = getDimNamesFromStructMeta(d.getRank(),d.getName());
		}
		return names;
	}
}

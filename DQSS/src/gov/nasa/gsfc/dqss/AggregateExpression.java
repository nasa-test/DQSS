package gov.nasa.gsfc.dqss;

import gov.nasa.gsfc.dqss.castor.expression.*;
import gov.nasa.gsfc.dqss.castor.expression.descriptors.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.Logger;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Group;

/**
 * 
 * @author rstrub Purpose: 1. Navigate through the expression, execute and
 *         compare the results of each Screening Assertion. 2. aggregate into
 *         Hashmaps the unique ScreeningFields, screeningfields, quality
 *         Datasets etc.
 */

public class AggregateExpression {

	HashMap<String, String> criteria;
	Expression expr;
	Boolean NoScreening = false;
	QualityView qView;
	Dataset data;
	Dataset[] QSVars;
	DQSSNumber eachDataPixel;
	HashMap<String, DQSSNumber> eachQualityPixel;
	int thisPixel = -1;
	int row = -1;
	int column = -1;
	int n_ocean_pixels = 0;
	int n_coast_pixels = 0;
	int n_desert_pixels = 0;
	int n_land_pixels = 0;
	int unknown_surface_type_pixels = 0;
	int totalflag = 0;
	int bitflag = 0;

	/*
	 * By ScreeningField name
	 */
	HashMap<String, Dataset> quality;
	/*
	 * by ScreeningField name
	 */
	HashMap<String, screeningfield> sfs;

	/*
	 * aggregate SAs for later populate of methods dimensions etc.
	 */

	HashMap<String, ScreeningAssertion> SA;
	HashMap<String, ScreeningField> SF;
	HashMap<String, String[]> SATypes;

	HashMap<String, Method> methods; // (key is SAkey)
	DataFile file;
	datafield DF;
	int QualityCount = 0;
	ReflectDirections rf;
	static Logger logger = Logger.getLogger(ProductFactory.class.getName());

	public AggregateExpression(QualityView QV) {
		rf = new ReflectDirections();
		qView = QV;
		expr = QV.getExpression();

		criteria = new HashMap<String, String>();
		quality = new HashMap<String, Dataset>();
		sfs = new HashMap<String, screeningfield>();
		SA = new HashMap<String, ScreeningAssertion>();
		methods = new HashMap<String, Method>();
		SF = new HashMap<String, ScreeningField>();
		SATypes = new HashMap<String, String[]>();
		if (ViewIsNotOK()) {
			logger.error("Something wrong with the view");
			System.err.println("Something wrong is the view");
			System.exit(31);
		}
	}

	private boolean ViewIsNotOK() {
		boolean status = false;
		if (qView == null) {
			logger
					.fatal("QualityView is empty - XML is empty, something is seriously wrong");
			status = true;
		}
		if (qView.getDatavar() == null) {
			logger.fatal("Datavar is empty -  something is seriously wrong");
			status = true;
		}
		if (qView.getDatavar().getDataset() == null) {
			logger
					.fatal("Datavar/Dataset is empty - something is seriously wrong");
			status = true;
		}
		if (qView.getDatavar().getDataset().getVariable() == null) {
			logger
					.fatal("Datavar/Dataset/Variable is empty - something is seriously wrong");
			status = true;
		}

		return status;
	}

	/*
	 * Set up all artifacts needed to run through the expression for each pixel.
	 * reflection methods, dimension alignment
	 */
	public void prepQualityView() throws NullPointerException {
		/*
		 * Iterate over collected screeningfields.
		 */
		Iterator it = SA.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry SApair = (Map.Entry) it.next();
			logger.info("SAPair key:" + SApair.getKey());
			ScreeningAssertion sa = (ScreeningAssertion) SApair.getValue();
			screeningfield sf = sfs.get(sa.getScreeningField().getName());
			if (sa.getType().equals(
					"MinimumQualityInterpretationScreeningAssertion")) {
				/*
				 * For quality SA we are comparing QualityAssurance variable
				 * with the user's criteria which can be any type I want.
				 */
				screeningfield sfield = sfs.get(sa.getScreeningField()
						.getName());
				if (sa.getScreeningField().getFieldValueType().getDirection() == null) {
					logger.error(sa.getScreeningField().getName()
							+ " has a FieldValueType without a direction");
				}
				if (sa.getScreeningField().getFieldValueType().getDirection()
						.equals("true")) {
					// methods.put(sa.getName(),
					// rf.getMethod(file.SFs.get(i).getDirection(),
					// file.SFs.get(i).getClassDatatype() , Long.TYPE);
					// It's Long.TYPE because that is the type we are assigning
					// the user criteria. ALGORITHM:
					/*  if(((CM_QA->SurfFlag == Ocean) && (QA_Ocean->AerosolParmssAvgSolnConfFlag < Marginal)) 
							  || ((CM_QA->SurfFlag == Land) && (QA_Land->0.47micronAOTFlag < VeryGood))
							  || ((CM_QA->SurfFlag == Coast) && (QA_Land->0.47micronAOTFlag < VeryGood)) 
							  || ((CM_QA->SurfFlag == Land) && (QA_Land->RetrCond == Water_pixels_in_10_x_10_box))
							  || ((CM_QA->SurfFlag == Land) && (QA_Land->RetrCond == Cirrus_present))
							  || ((CM_QA->SurfFlag == Land) && (QA_Land->RetrCond == Fitting_error_%3E_0.25))
							  || ((CM_QA->SurfFlag == Coast) && (QA_Land->RetrCond == Water_pixels_in_10_x_10_box))
							  || ((CM_QA->SurfFlag == Coast) && (QA_Land->RetrCond == Cirrus_present))
							  || ((CM_QA->SurfFlag == Coast) && (QA_Land->RetrCond == Fitting_error_%3E_0.25)))
							      : SCREEN
							}

					 */
					methods.put(sa.getName(), rf.getMethod("lessThan", sfield
							.getClassDatatype(), Float.TYPE));
				} else {
					methods.put(sa.getName(), rf.getMethod("positive", sfield
							.getClassDatatype(), Float.TYPE));
				}
			} else if (sa.getType().equals(
					"RetrievalConditionScreeningAssertion")) {
				/*
				 * for retrieval condition was is being compared is whether or
				 * not a bit has been set So somewhere we need to know we are
				 * doing a Retrieval type of compare and
				 */
				methods.put(sa.getName(), rf.getMethod("equals", Byte.TYPE,
						Byte.TYPE));
			} else if (sa.getType().equals("ThresholdScreeningAssertion")) {
				/*
				 * for retrieval condition was is being compared is whether or
				 * not a bit has been set So somewhere we need to know we are
				 * doing a Retrieval type of compare and XIAOPENG - we need to
				 * generalize these data types being passed to getmethod.
				 */
				if (sa.getHasAttribute().getHasMaxThreshold() != null) {
					if (sa.getHasAttribute().getHasMinThreshold() != null) {
						methods.put(sa.getName(), rf.get3Method("notbetween",
								Float.TYPE, Float.TYPE, Float.TYPE));
					} else {
						methods.put(sa.getName(), rf.getMethod("positive",
								Float.TYPE, Float.TYPE));
					}
				} else if (sa.getHasAttribute().getHasMinThreshold() != null) {
					methods.put(sa.getName(), rf.getMethod("negative",
							Float.TYPE, Float.TYPE));
				} else {
					logger.error("Threshold ScreeningAssertion " + sa.getName()
							+ " has no min or max threshold!");
				}
			}
		}

	}

	public void populateDataset(String dataVariable) {

		DF = file.instatiateProperDatafieldType(dataVariable);
		file.DF = DF;
		Group newg = file.getGroupOfDataset(dataVariable);
		data = file.copyDatasetWithSuffix(dataVariable, newg, DQSS.MaskName);

		// Note dimensions in ontology are not always in order
		Dimension dims[] = qView.getDatavar().getDataset().getVariable()
				.getDimension();
		for (int i = 0; i < dims.length; ++i) {
			int index = (int) dims[i].getIndex();
			// for MLS DF.setDimSize(index, dims[i].getLength());
			file.DF.addDimension(dims[i].getName(), index);
		}
		file.DF.cleanUpDims();
	}

	public String getDataVariable() {
		return qView.getDatavar().getDataset().getVariable().getName();

	}

	public boolean TraverseExpression(HashMap<String, DQSSNumber> qaData,
			int ith, int row, int col) {

		this.eachQualityPixel = qaData;
		this.thisPixel = ith;
		this.column = col;
		this.row = row;
		OR top = expr.getOR();
		logger.debug("OR [");
		ORItem[] oritems = top.getORItem();
		if (row == 9 && column == 44) {
			logger.debug("debug");
		}
		HashMap<String, Boolean> saCache = new HashMap<String, Boolean>();
		boolean status = ExpressionRecurse(oritems, saCache);

		logger.debug("Status of c:" + row + ", r:" + col + ", " + ith
				+ " pixel: " + status);
		return status;
	}

	private boolean ExpressionRecurse(ORItem[] oritems,
			HashMap<String, Boolean> saCache) {
		boolean subResult = false;

		for (int i = 0; i < oritems.length; ++i) {
			OR sub = oritems[i].getOR();
			if (sub != null) {
				logger.trace("OR [");
				ORItem[] moreitems = sub.getORItem();
				if (moreitems != null) {
					subResult = ExpressionRecurse(moreitems, saCache);
					/*
					 * The expression is structure around a series of ORs. So if
					 * any of the OR sub-expressions return TRUE then we are
					 * done (we know we should Screen it)
					 */
					if (subResult) {
						return subResult;
					}
				}
			} else {
				boolean[] eachResult = new boolean[2];
				AND anditem = oritems[i].getAND();
				ScreeningAssertion sa[];
				boolean[] saboolean = new boolean[2];
				Arrays.fill(saboolean, Boolean.TRUE);
				if (anditem != null) {
					/* handle NOTS */
					sa = getAndedScreeningAssertions(anditem,saboolean);
					logger.trace("AND");
				} else {
					ScreeningAssertion sa1 = oritems[i].getScreeningAssertion();
					sa = new ScreeningAssertion[1];
					sa[0] = sa1;
					eachResult[1] = true; // there is only one SA in this 'AND'
				}
				for (int nsa = 0; nsa < sa.length; ++nsa) {
					/*
					 * Screening Assertion is evaluated here and only here or we
					 * use cached result
					 */
					if (!DQSS.UseExpressionCaching) {
						if (saboolean[nsa] == true) {
							eachResult[nsa] = executeScreeningAssertion(sa[nsa]);
						}
						else {
							eachResult[nsa] = !(executeScreeningAssertion(sa[nsa]));								
						}
						
					} else {
						// CACHING ACTUALY TOOK 7 SECS INSTEAD OF 6...
						if (saCache.containsKey(sa[nsa].getName())) {
							// Use cache
							eachResult[nsa] = saCache.get(sa[nsa].getName());
						} else {
							// get new value and log
							eachResult[nsa] = executeScreeningAssertion(sa[nsa]);
							saCache.put(sa[nsa].getName(), eachResult[nsa]);

						}
					}
					logger.debug("row:" + row + " col:" + column + " "
							+ sa[nsa].getType().substring(0, 10) + " == "
							+ sa[nsa].getScreeningField().getName() + " == "
							+ getHasAttribute(sa[nsa].getHasAttribute())
							+ " => " + eachResult[nsa] + " flag(" + totalflag
							+ "," + bitflag + ")");
					/*
					 * we'll turn this on once we have tested the first L&O
					 * case. Since we are exiting if any of the AND's return
					 * TRUE then if either operand of the AND returns false then
					 * we know we don't have to do the second one.
					 */
					if (!eachResult[0]) {
						break;
					}

				}
				boolean AndResult = eachResult[0] && eachResult[1];
				/*
				 * The expression is structure around a series of ORs. So if any
				 * of the OR sub-expressions return TRUE then we are done (we
				 * know we should Screen it)
				 */
				if (AndResult) {
					return AndResult;
				}
				// return AndResult;
				logger.trace("]");

			}
		}
		return false;
	}

	private ScreeningAssertion[] getAndedScreeningAssertions(AND anditem , boolean saboolean[]) {
		ScreeningAssertion sa[] = anditem.getScreeningAssertion();
		ScreeningAssertion notted;
		ScreeningAssertion both[];
		NOT not  =  anditem.getNOT();
		if (not != null) {
			notted = anditem.getNOT().getScreeningAssertion();
			both = new ScreeningAssertion[2];
			both[0] = sa[0];
			both[1] = notted;
			if (saboolean != null) {
				saboolean[1] = false;   
			}
			return both;
		}
		if (sa.length < 2) {
			
		}
		return sa;
	}

	/*
	 * Populate screeningfield, quality hashmap, screeningassertion hashmap
	 */
	public Dataset[] populateQSVars_byUniqueQualityVariable() {

		OR top = expr.getOR();

		ORItem[] oritems = top.getORItem();
		getQSVars_byUniqueQualityVariable(oritems);
		/*
		 * backwards compatibility:
		 */
		QSVars = new Dataset[QualityCount];
		Iterator it = quality.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry qualitypair = (Map.Entry) it.next();
			QSVars[i] = (Dataset) qualitypair.getValue();
			++i;
		}
		return QSVars;
	}

	private Dataset[] getQSVars_byUniqueQualityVariable(ORItem[] oritems) {

		for (int i = 0; i < oritems.length; ++i) {
			OR sub = oritems[i].getOR();
			if (sub != null) {
				ORItem[] moreitems = sub.getORItem();
				if (moreitems != null) {
					getQSVars_byUniqueQualityVariable(moreitems);
				}
			} else {
				AND anditem = oritems[i].getAND();
				ScreeningAssertion[] sa = getAndedScreeningAssertions(anditem,null);
				for (int nsa = 0; nsa < sa.length; ++nsa) {
					String SAType = sa[nsa].getType();
					String SAName = sa[nsa].getName();
					String SFName = sa[nsa].getScreeningField().getName();

					if (!SA.containsKey(SAName)) {
						SA.put(SAName, sa[nsa]);
					}
					String variable = sa[nsa].getScreeningField().getVariable()
							.getName();
					Dataset qvar = file.getDataset(variable, file.topGroup);
					if (!quality.containsKey(variable)) {

						if (qvar != null) {
							quality.put(variable, qvar);
							++QualityCount;
						}
					}
					if (!sfs.containsKey(SFName)) {
						screeningfield sf = new screeningfield(SFName);
						sf.setRank(sa[nsa].getScreeningField().getVariable()
								.getDimension().length);
						sf.setVariable(sa[nsa].getScreeningField()
								.getVariable().getName());
						HashMap<String, String> dimension = new HashMap<String, String>();
						try {
							file.determineDatatype(qvar, dimension);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						sf.setDatatype(dimension.get("type"));
						Dimension dims[] = sa[nsa].getScreeningField()
								.getVariable().getDimension();
						for (int j = 0; j < dims.length; ++j) {
							int index = (int) dims[j].getIndex();
							sf.setDimSize(index, dims[j].getLength());
							sf.addDimension(dims[j].getName(), index);
						}
						sf.setStartBit((long) sa[nsa].getScreeningField()
								.getFieldValueType().getHasStartBitIndex());
						sf.setStopBit((long) sa[nsa].getScreeningField()
								.getFieldValueType().getHasStopBitIndex());
						sf.setStartLayer((long) sa[nsa].getScreeningField()
								.getFieldValueType().getHasStartLayerIndex());
						sf.setStopLayer((long) sa[nsa].getScreeningField()
								.getFieldValueType().getHasStartBitIndex());

						sfs.put(SFName, sf);

					}

				}

			}
		}
		return null;
	}

	public Dataset[] populateQSVars_byScreeningField() {

		OR top = expr.getOR();

		ORItem[] oritems = top.getORItem();
		getQSVars_byScreeningField(oritems);
		/*
		 * For global metadata statistics:
		 */
		Iterator it = SA.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry SApair = (Map.Entry) it.next();
			ScreeningAssertion sa = (ScreeningAssertion) SApair.getValue();
			populateTypeForStatistics(sa.getScreeningField().getName(),
					(ScreeningAssertion) SApair.getValue(), sa
							.getScreeningField().getVariable().getName());
		}
		/*
		 * backwards compatibility:
		 */
		QSVars = new Dataset[sfs.size()];
		it = quality.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry qpair = (Map.Entry) it.next();
			QSVars[i] = (Dataset) qpair.getValue();
			++i;
		}
		return QSVars;
	}

	private Dataset[] getQSVars_byScreeningField(ORItem[] oritems) {

		for (int i = 0; i < oritems.length; ++i) {
			OR sub = oritems[i].getOR();
			if (sub != null) {
				ORItem[] moreitems = sub.getORItem();
				if (moreitems != null) {
					getQSVars_byScreeningField(moreitems);
				}
			} else {
				AND anditem = oritems[i].getAND();
				ScreeningAssertion sa[];
				if (anditem != null) {
					sa = getAndedScreeningAssertions(anditem,null);
				} else {
					ScreeningAssertion sa1 = oritems[i].getScreeningAssertion();
					sa = new ScreeningAssertion[1];
					sa[0] = sa1;
				}

				for (int nsa = 0; nsa < sa.length; ++nsa) {
					String SAType = sa[nsa].getType();
					String SAName = sa[nsa].getName();
					String SFName = sa[nsa].getScreeningField().getName();

					if (!SA.containsKey(SAName)) {
						SA.put(SAName, sa[nsa]);
					}
					String variable = sa[nsa].getScreeningField().getVariable()
							.getName();
					/*
					 * Sometimes we need two copies of the same QA variable
					 * because 2 layers of it are used at the same time in
					 * different sfs.
					 */
					Dataset qvar = file.getDataset(variable, file.topGroup);
					if (!quality.containsKey(SFName)) {

						if (qvar != null) {
							quality.put(SFName, qvar);
							++QualityCount;
						}
					}
					if (!sfs.containsKey(SFName)) {
						SF.put(SFName, sa[nsa].getScreeningField());

						screeningfield sf = new screeningfield(SFName);
						sf.setRank(sa[nsa].getScreeningField().getVariable()
								.getDimension().length);
						sf.setVariable(sa[nsa].getScreeningField()
								.getVariable().getName());
						HashMap<String, String> dimension = new HashMap<String, String>();
						try {
							file.determineDatatype(qvar, dimension);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						sf.setDatatype(dimension.get("type"));
						Dimension dims[] = sa[nsa].getScreeningField()
								.getVariable().getDimension();

						for (int j = 0; j < dims.length; ++j) {

							/*
							 * Need indices to be in order
							 */
							int x = 0;
							int index = 0;
							try {
								while (dims[x].getIndex() != j)
									++x;
								index = (int) dims[x].getIndex();
							} catch (ArrayIndexOutOfBoundsException e) {
								int n = x - 1;
								String err = "Ontology has inconsistent number of dimensions for "
										+ sa[nsa].getScreeningField().getName();
								System.out.println(err);
								logger.error(err);
								System.exit(1);
							}
							/*
							 * MLS doesn't have dimension names.
							 */
							if (dims[x].getName().isEmpty()
									|| dims[x].getName().contains("none")) {
								sf.setDimSize(index, qvar.getDims()[x]);
								sf.addDimension("Dim" + qvar.getDims()[x],
										index);
							} else {
								sf.setDimSize(index, dims[x].getLength());
								sf.addDimension(dims[x].getName(), index);
							}
						}
						sf.setStartBit((long) sa[nsa].getScreeningField()
								.getFieldValueType().getHasStartBitIndex());
						sf.setStopBit((long) sa[nsa].getScreeningField()
								.getFieldValueType().getHasStopBitIndex());
						sf.setStartLayer((long) sa[nsa].getScreeningField()
								.getFieldValueType().getHasStartLayerIndex());
						sf.setStopLayer((long) sa[nsa].getScreeningField()
								.getFieldValueType().getHasStartBitIndex());

						sfs.put(SFName, sf);

					}

				}

			}
		}
		return null;
	}

	/*
	 * Only operates on MinimumQualityInterpretationScreeningAssertion
	 */
	public void populateCriteria() {
		OR top = expr.getOR();

		ORItem[] oritems = top.getORItem();
		getCriteria(oritems);

	}

	public OR getCriteria(ORItem[] oritems) {

		for (int i = 0; i < oritems.length; ++i) {
			OR sub = oritems[i].getOR();
			if (sub != null) {
				ORItem[] moreitems = sub.getORItem();
				if (moreitems != null) {
					getCriteria(moreitems);
				}
			} else {
				AND anditem = oritems[i].getAND();
				ScreeningAssertion sa[];
				if (anditem != null) {
					sa = anditem.getScreeningAssertion();
				} else {
					ScreeningAssertion sa1 = oritems[i].getScreeningAssertion();
					sa = new ScreeningAssertion[1];
					sa[0] = sa1;
				}

				for (int nsa = 0; nsa < sa.length; ++nsa) {
					String SAType = sa[nsa].getType();

					if (SAType
							.equals("MinimumQualityInterpretationScreeningAssertion")) {

						String variable = sa[nsa].getScreeningField()
								.getVariable().getName();
						if (!criteria.containsKey(variable)) {
							criteria.put(variable, getHasAttribute(sa[nsa]
									.getHasAttribute()));
							/*
							 * XIAOPENG: add all possible values of quality to
							 * xml and then pick lowest one and compare to
							 * getHasAttribute to determine if no screening.
							 * Current Hack:
							 */
							if (variable.equals(getHasAttribute(
									sa[nsa].getHasAttribute()).equals(
									"NO_CONFIDENCE"))) {
								NoScreening = true;
								logger.warn("No Screening set to TRUE");
							} else {
								/*
								 * NoScreening starts out false and remains
								 * false if any of the variables are not lowest
								 * (NO_CONFIDENCE)
								 */
								NoScreening = false;
								logger.debug("No Screening remaining false");
							}

						}
					}
				}

			}
		}
		return null;
	}

	public void InvestigateExpression() {
		OR top = expr.getOR();

		ORItem[] oritems = top.getORItem();
		getDeepestOrInList2(oritems, 1);

		for (int i = 0; i < oritems.length; ++i) {
			OR sub = oritems[i].getOR();
		}
	}

	public OR getDeepestOrInList2(ORItem[] oritems, int depth) {

		for (int i = 0; i < oritems.length; ++i) {
			OR sub = oritems[i].getOR();
			int op = i + 1;
			// logger.info();
			logger.info("level:" + depth + " op:" + op + "  ");
			if (sub != null) {

				ORItem[] moreitems = sub.getORItem();
				if (moreitems != null) {
					getDeepestOrInList2(moreitems, ++depth);
				} else {
					logger.info(i);
				}
			}

			else {
				AND anditem = oritems[i].getAND();
				if (anditem != null) {
					ScreeningAssertion[] sa = anditem.getScreeningAssertion();
					for (int nsa = 0; nsa < sa.length; ++nsa) {
						String SAType = sa[nsa].getType();

						if (SAType
								.equals("MinimumQualityInterpretationScreeningAssertion")) {

							String variable = sa[nsa].getScreeningField()
									.getVariable().getName();
							if (!criteria.containsKey(variable)) {
								criteria.put(variable, getHasAttribute(sa[nsa]
										.getHasAttribute()));
							}

						}
						String hasAttribute = getHasAttribute(sa[nsa]
								.getHasAttribute());
						String type = sa[nsa].getType();
						System.out.print("OR level:" + depth + " op:" + op
								+ " AND " + type + " is:" + hasAttribute
								+ " with ");
					}
					System.out.print(":" + depth + "," + i + "OR ");

				} else {
					/*
					 * No AND operator, just a single AS to be OR'ed.
					 */
					ScreeningAssertion sa = oritems[i].getScreeningAssertion();
					String SAType = sa.getType();
					if (SAType
							.equals("MinimumQualityInterpretationScreeningAssertion")) {

						String variable = sa.getScreeningField().getVariable()
								.getName();
						if (!criteria.containsKey(variable)) {
							criteria.put(variable, getHasAttribute(sa
									.getHasAttribute()));
						}

					}
					String hasAttribute = getHasAttribute(sa.getHasAttribute());
					String type = sa.getType();
					System.out.print("OR level:" + depth + " op:" + op
							+ " just OR " + type + " is:" + hasAttribute
							+ " with ");

					System.out.print(":" + depth + "," + i + "OR ");
				}

			}

		}
		return null;
	}

	public OR getDeepestOrInList(ORItem[] oritems, int depth) {

		for (int i = 0; i < oritems.length; ++i) {
			OR sub = oritems[i].getOR();
			int op = i + 1;
			// logger.info();
			logger.info("level:" + depth + " op:" + op + "  ");
			if (sub != null) {

				ORItem[] moreitems = sub.getORItem();
				if (moreitems != null) {
					getDeepestOrInList(moreitems, ++depth);
				} else {
					logger.info(i);
				}
			} else {
				// System.out.print(":" + depth + "," + i + "OR ");
				String hasAttribute = getHasAttribute(oritems[i]
						.getScreeningAssertion().getHasAttribute());
				String type = (String) oritems[i].getScreeningAssertion()
						.getType();
				System.out.print("OR level:" + depth + " op:" + op + " " + type
						+ " is:" + hasAttribute + " with ");
			}

		}
		return null;
	}

	private HashMap<String, Boolean> getAllRetrievalConditions() {
		Iterator it = SA.entrySet().iterator();
		HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
		while (it.hasNext()) {
			Map.Entry SApair = (Map.Entry) it.next();
			logger.info("SAPair key:" + SApair.getKey());
			ScreeningAssertion sa = (ScreeningAssertion) SApair.getValue();
			if (sa.getType().contains("RetrievalConditionScreeningAssertion")) {
				String flagname = getHasAttribute(sa.getHasAttribute());
				if (!flags.containsKey(flagname)) {
					flags.put(flagname, true);
				}
			}
		}
		return flags;
	}

	private String getHasAttribute(HasAttribute ha) {
		HasAttributeChoice attr = ha.getHasAttributeChoice();
		if (attr != null) {
			if (attr.getQualityRecommendation() != null) {
				return attr.getQualityRecommendation().getValue();
			}
			if (attr.getMinimumQualityInterpretation() != null) {
				return attr.getMinimumQualityInterpretation().getValue();
			}
			if (attr.getRetrievalCondition() != null) {
				return attr.getRetrievalCondition().getValue();
			}
			if (attr.getSurfaceType() != null) {
				return attr.getSurfaceType().getValue();
			}
		}
		if (ha.getHasMinThreshold() != null) {
			if (ha.getHasMinThreshold().getValue() != null) {
				return "getHasMinThreshold";
			}
		}
		if (ha.getHasMaxThreshold() != null) {
			if (ha.getHasMaxThreshold().getValue() != null) {
				return "getHasMaxThreshold";
			}

		}

		return "";
	}

	private String getHasAttributeValue(ScreeningAssertion sa)
			throws NullPointerException {
		if (sa.getType().contains(
				"MinimumQualityInterpretationScreeningAssertion")) {
			return sa.getScreeningField().getFieldValueType()
					.getValidQualityLevel().getValue().toString();
		}
		if (sa.getType().contains("RetrievalConditionScreeningAssertion")) {
			return sa.getScreeningField().getFieldValueType()
					.getValidEnumValue().getValue().toString();
		}
		if (sa.getType().contains("SurfaceTypeScreeningAssertion")) {
			Long value = sa.getScreeningField().getFieldValueType()
					.getSurfaceTypeValue().getValue();
			return value.toString();
		}
		if (sa.getType().contains("ThresholdScreeningAssertion")) {
			String min = "";
			String max = "";
			if (sa.getHasAttribute().getHasMinThreshold() != null) {
				min = "min="
						+ sa.getHasAttribute().getHasMinThreshold().getValue();
			}
			if (sa.getHasAttribute().getHasMaxThreshold() != null) {
				max = " max="
						+ sa.getHasAttribute().getHasMaxThreshold().getValue();
			}
			return "Threshold " + min + max;

		}

		return "";
	}

	boolean executeScreeningAssertion(ScreeningAssertion sa) {
		boolean result = false;

		if (thisPixel >= 721) {
			logger.debug(sa.getName() + ":");
		}
		if (sa.getType().contains("SurfaceTypeScreeningAssertion")) {
			result = Do_SurfaceTypeScreeningAssertion(sa);
		} else if (sa.getType().contains(
				"MinimumQualityInterpretationScreeningAssertion")) {
			result = Do_MinimumQualityInterpretationScreeningAssertion(sa);
		} else if (sa.getType()
				.contains("RetrievalConditionScreeningAssertion")) {
			result = Do_RetrievalConditionScreeningAssertion(sa);
		} else if (sa.getType().contains("ThresholdScreeningAssertion")) {
			result = Do_ThresholdScreeningAssertion(sa);
		} else {
			logger.error("Screening Assertion:" + sa.getType()
					+ " is not being handled yet.");
		}
		return result;
	}

	private boolean Do_SurfaceTypeScreeningAssertion(ScreeningAssertion sa) {
		boolean result = false;
		/*
		 * sa IS Surface_OCEAN_CM so we need the surface flag bit of
		 * Cloud_Mask_QA: or sa is x so we need the what of what
		 */

		/*
		 * we need to know that the surface bit for coast is:
		 */
		if (sa.getScreeningField().getFieldValueType().getSurfaceTypeValue() == null) {
			logger.error(sa.getName()
					+ "doesn't have a <SurfaceTypeValue> value attribute");
			return result;
		}
		long SurfaceTypeValue = sa.getScreeningField().getFieldValueType()
				.getSurfaceTypeValue().getValue();
		/*
		 * then we need to extract the Surface Type bit from Cloud_Mask_QA:
		 */
		byte SFflag = (byte) evaluateQualityFlag((byte) eachQualityPixel.get(
				sa.getScreeningField().getName()).byteArrayValue(thisPixel),
				(int) sa.getScreeningField().getFieldValueType()
						.getHasStartBitIndex(), (int) sa.getScreeningField()
						.getFieldValueType().getHasStopBitIndex(), sa
						.getScreeningField().getFieldValueType().hasEndian());
		totalflag = eachQualityPixel.get(sa.getScreeningField().getName())
				.byteArrayValue(thisPixel);
		bitflag = SFflag;
		DoDebugStats(SFflag);
		if (SurfaceTypeValue == SFflag) {
			/*
			 * Surface type is indeed (in this case OCEAN)
			 */
			result = true;
		}
		return result;
	}

	private boolean Do_RetrievalConditionScreeningAssertion(
			ScreeningAssertion sa) {
		boolean result = false;
		/*
		 * sa IS Surface_OCEAN_CM so we need the surface flag bit of
		 * Cloud_Mask_QA: or sa is x so we need the what of what
		 */

		/*
		 * we need to know that the surface bit for coast is:
		 */
		if (sa.getScreeningField().getFieldValueType().getValidEnumValue() == null) {
			logger.error(sa.getName()
					+ "doesn't have a <ValidEnumValue> value attribute");
			return result;
		}
		DQSSNumber RetrievalCondition = new DQSSNumber(sa.getScreeningField()
				.getFieldValueType().getValidEnumValue().getValue()
				.floatValue());
		DQSSNumber SFflag = new DQSSNumber(0);
		/*
		 * This returns which (n/ith) quality variable pixel we should use. The
		 * number of dims of the quality variable don't always equal the number
		 * of dims of even the layer we are working on.
		 */
		int applicablePixel = getPixelForThisDataFieldValue(sa);

		/*
		 * If quality field datatype is float then we aren't going to be pulling
		 * out bits from a byte - so no evaluateQualityFlag
		 */
		if (sfs.get(sa.getScreeningField().getName()).getClassDatatype()
				.toString().contains("float")) {
			SFflag = new DQSSNumber((float) eachQualityPixel.get(
					sa.getScreeningField().getName()).ArrayValue(
					applicablePixel));

		}
		if (sfs.get(sa.getScreeningField().getName()).getClassDatatype()
				.toString().contains("long")) {
			byte qualitypixelflag = (byte) evaluateQualityFlag(
					(long) eachQualityPixel.get(
							sa.getScreeningField().getName()).ArrayValue(
							applicablePixel), (int) sa.getScreeningField()
							.getFieldValueType().getHasStartBitIndex(),
					(int) sa.getScreeningField().getFieldValueType()
							.getHasStopBitIndex(), sa.getScreeningField()
							.getFieldValueType().hasEndian());

			bitflag = qualitypixelflag;
			SFflag = new DQSSNumber((byte) qualitypixelflag);
		}
		if (sfs.get(sa.getScreeningField().getName()).getClassDatatype()
				.toString().contains("byte")) {

			totalflag = eachQualityPixel.get(sa.getScreeningField().getName())
					.byteArrayValue(applicablePixel);

			if (totalflag != 0) {
				// I'm only interested in first 4 bytes make value of 2,3,or 4
				// (Valids are 0 through 11)
				byte mask = 15;
				int flag = totalflag & mask;
				if (flag >= 2 && flag <= 4) {
					logger.info(thisPixel + " (quality) " + totalflag);
				}
			}
			/*
			 * Pulls the flag out of the byte.
			 */
			byte qualitypixelflag = (byte) evaluateQualityFlag(
					(byte) totalflag, (int) sa.getScreeningField()
							.getFieldValueType().getHasStartBitIndex(),
					(int) sa.getScreeningField().getFieldValueType()
							.getHasStopBitIndex(), sa.getScreeningField()
							.getFieldValueType().hasEndian());
			SFflag = new DQSSNumber((byte) qualitypixelflag);
			bitflag = qualitypixelflag; // for some global statistics.
		}

		// bitflag = SFflag;

		if (RetrievalCondition.compare(SFflag.doubleValue())) {
			/*
			 * RetrievalConditionFlag is set.
			 */
			result = true;
		}
		return result;
	}

	private boolean Do_MinimumQualityInterpretationScreeningAssertion(
			ScreeningAssertion sa) {
		boolean result = false;

		/*
		 * From hasImprovingQualityInPositiveDirection I've already figured out
		 * which operator method to use in prepQualityView() So let's go ahead
		 * and pull out the two things we need to compare: 1. the user's
		 * criteria - first we will use field value type. soon we will use
		 * passed in value.
		 */
		float UserCriteria = sa.getScreeningField().getFieldValueType()
				.getValidQualityLevel().getValue().floatValue();
		/*
		 * 2. Quality_Assurance_(Ocean in this case) XIOAPENG : determine what
		 * datatype this is. might not be byte.
		 */
		// this is gotten below:byte qualitypixel =
		// eachQualityPixel.get(sa.getScreeningField().getVariable().getName()).byteArrayValue(thisPixel);
		Method rmethod = methods.get(sa.getName());
		Object list[] = new Object[2];
		list[1] = UserCriteria;
		int applicablePixel = getPixelForThisDataFieldValue(sa);

		if (sfs.get(sa.getScreeningField().getName()).getClassDatatype()
				.toString().contains("float")) {
			list[0] = (float) eachQualityPixel.get(
					sa.getScreeningField().getName()).ArrayValue(
					applicablePixel);

		}
		if (sfs.get(sa.getScreeningField().getName()).getClassDatatype()
				.toString().contains("byte")) {
			totalflag = eachQualityPixel.get(sa.getScreeningField().getName())
					.byteArrayValue(applicablePixel);
			byte qualitypixelflag = (byte) evaluateQualityFlag(
					(byte) eachQualityPixel.get(
							sa.getScreeningField().getName()).byteArrayValue(
							applicablePixel), (int) sa.getScreeningField()
							.getFieldValueType().getHasStartBitIndex(),
					(int) sa.getScreeningField().getFieldValueType()
							.getHasStopBitIndex(), sa.getScreeningField()
							.getFieldValueType().hasEndian());
			list[0] = qualitypixelflag;
			bitflag = qualitypixelflag;
		}
		DoDebugStats((byte) bitflag);
		try {
			/*
			 * this compares (qualitylevelfromQAVariable(QAOcean inthiscase),
			 * UserCriteria)
			 */
			if ((Boolean) rmethod.invoke(rf, list)) { // MQI
				result = true; // this means it is true that
				// Quality_Assurance_Ocean->AerosolParametersAvgSolnConfidenceFlag
				// < UserCriteria
				// this means that
				// Quality_Assurance_Land->0.47micronAerosolOpticalThicknessConfidenceFlag
				// < UserCriteria
				// Quality[x] < 1.3 (or whatever the user specified)
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private boolean Do_ThresholdScreeningAssertion(ScreeningAssertion sa) {
		boolean result = false;

		/*
		 * From hasImprovingQualityInPositiveDirection I've already figured out
		 * which operator method to use in prepQualityView() So let's go ahead
		 * and pull out the two things we need to compare: 1. the user's
		 * criteria - first we will use field value type. soon we will use
		 * passed in value.
		 */

		float max = -9999;
		float min = 9999;
		int listlen = methods.get(sa.getName()).getParameterTypes().length;
		Object list[] = new Object[listlen];
		if (sa.getHasAttribute().getHasMinThreshold() != null) {

			min = sa.getHasAttribute().getHasMinThreshold().getValue()
					.floatValue();
			list[1] = min;
		}
		if (sa.getHasAttribute().getHasMaxThreshold() != null) {
			max = sa.getHasAttribute().getHasMaxThreshold().getValue()
					.floatValue();
			if (sa.getHasAttribute().getHasMinThreshold() != null) {
				list[2] = max;
			} else {
				list[1] = max;
			}
		}

		/*
		 * 2. Quality_Assurance_(Ocean in this case) XIOAPENG : determine what
		 * datatype this is. might not be byte.
		 */
		// this is gotten below:byte qualitypixel =
		// eachQualityPixel.get(sa.getScreeningField().getVariable().getName()).byteArrayValue(thisPixel);
		Method rmethod = methods.get(sa.getName());

		int applicablePixel = getPixelForThisDataFieldValue(sa);

		try {
			if (sfs.get(sa.getScreeningField().getName()).getClassDatatype()
					.toString().contains("float")) {
				// put array out of bounds exception block here if dimension
				// name that
				// has variable dimension is not 'none'
				list[0] = (float) eachQualityPixel.get(
						sa.getScreeningField().getName()).ArrayValue(
						applicablePixel);
			}
			if (sfs.get(sa.getScreeningField().getName()).getClassDatatype()
					.toString().contains("byte")) {
				totalflag = getPixelValueForThisDataFieldValue(sa,
						applicablePixel);
				byte qualitypixelflag = (byte) evaluateQualityFlag(
						(byte) totalflag, (int) sa.getScreeningField()
								.getFieldValueType().getHasStartBitIndex(),
						(int) sa.getScreeningField().getFieldValueType()
								.getHasStopBitIndex(), sa.getScreeningField()
								.getFieldValueType().hasEndian());
				list[0] = qualitypixelflag;
				bitflag = qualitypixelflag;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			String err = "Variable "
					+ sa.getScreeningField().getName()
					+ "'s array does not contain "
					+ applicablePixel
					+ " values."
					+ "This can happen if a variable dimension, a dimension without a name,"
					+ "does not have a dimension name of 'none' in the ontology";
			System.err.println(err);
			System.exit(1);
		}

		try {
			/*
			 * this compares (qualitylevelfromQAVariable(QAOcean inthiscase),
			 * UserCriteria)
			 */
			if ((Boolean) rmethod.invoke(rf, list)) { // Threshold
				result = true; // this means it will be masked... it is true
				// that
				// Quality_Assurance_Ocean->AerosolParametersAvgSolnConfidenceFlag
				// < UserCriteria
				// this means that
				// Quality_Assurance_Land->0.47micronAerosolOpticalThicknessConfidenceFlag
				// < UserCriteria
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/*
	 * XIAOPENG: write code to do a shift and mask for each screening assertion
	 * only once in prepQualityView.
	 */
	protected int evaluateQualityFlag(byte data, int startBit, int stopBit,
			boolean endian) {
		if (data == 0) {
			return 0; // let's not waste time if we don't have to... (because a
			// 0 value for the byte means the bits are all 0)
		}

		if (endian) {
			int shift = startBit; // note this only works if the least
			// significant bit is called bit 7
			int nbits = (stopBit - startBit) + 1; // inclusive
			// int mask = (2 ^ nbits) - 1;
			int mask = (int) java.lang.Math.pow(2, nbits) - 1;

			int preshift = data;
			int shifted = preshift >> shift;

			int flag = shifted & mask;
			return flag;
		} else {
			logger.error("Not handling SmallEndian yet");
		}
		return -1;
	}

	protected long evaluateQualityFlag(long data, int startBit, int stopBit,
			boolean endian) {
		if (data == 0) {
			return 0; // let's not waste time if we don't have to... (because a
			// 0 value for the byte means the bits are all 0)
		}

		if (endian) {
			int shift = startBit; // note this only works if the least
			// significant bit is called bit 7
			int nbits = (stopBit - startBit) + 1; // inclusive
			// int mask = (2 ^ nbits) - 1;
			int mask = (int) java.lang.Math.pow(2, nbits) - 1;

			long preshift = data;
			long shifted = preshift >> shift;

			long flag = shifted & mask;
			return flag;
		} else {
			logger.error("Not handling SmallEndian yet");
		}
		return -1;
	}

	/*
	 * we need to do this because quality fields of rank 1 MLS need to return
	 * not the ith pixel (corresponding to a data x,y plane but either the value
	 * that applies to the entire col of the data or the entire row of the data.
	 * XIAOPENG we need to generalize this return value.
	 */
	private int getPixelForThisDataFieldValue(ScreeningAssertion sa) {
		int applicablePixel;
		if (sfs.get(sa.getScreeningField().getName()).getRank() > 1) {
			return applicablePixel = thisPixel;
			// return
			// eachQualityPixel.get(sa.getScreeningField().getName()).byteArrayValue(thisPixel);
		} else {
			if (sfs.get(sa.getScreeningField().getName()).get1DType().contains(
					"row")) {
				return applicablePixel = row;
				// return
				// eachQualityPixel.get(sa.getScreeningField().getName()).byteArrayValue(row);
			} else if (sfs.get(sa.getScreeningField().getName()).get1DType()
					.contains("col")) {
				return applicablePixel = column;
				// return
				// eachQualityPixel.get(sa.getScreeningField().getName()).byteArrayValue(column);
			} else {
				String err = "No col or row matchup for 1D quality variable:"
						+ sa.getScreeningField().getName();
				logger.error(err);
				System.err.println(err);
				System.exit(22);
			}
		}
		return -1;

	}

	private byte getPixelValueForThisDataFieldValue(ScreeningAssertion sa,
			int applicablePixel) {

		Class type = sfs.get(sa.getScreeningField().getName())
				.getClassDatatype();
		if (type.toString().contains("float")) {

		}

		return eachQualityPixel.get(sa.getScreeningField().getName())
				.byteArrayValue(column);

	}

	void DoDebugStats(byte SFflag) {

		if (SFflag == 0) {
			++n_ocean_pixels;
		} else if (SFflag == 1) {
			++n_coast_pixels;
		} else if (SFflag == 2) {
			++n_desert_pixels;
		} else if (SFflag == 3) {
			++n_land_pixels;
		} else {
			++unknown_surface_type_pixels;
		}
	}

	void DisplayDebugStats() {
		logger.info("ocean   pixels:" + n_ocean_pixels);
		logger.info("coast   pixels:" + n_coast_pixels);
		logger.info("desert  pixels:" + n_desert_pixels);
		logger.info("land    pixels:" + n_land_pixels);
		logger.info("unknown pixels:" + unknown_surface_type_pixels);
	}

	String[] populateTypeForStatistics(String ScreeningFieldName,
			ScreeningAssertion sa, String qDatasetName) {
		String[] localtype = new String[4];
		localtype[0] = qDatasetName;
		String type = sa.getType();
		if (sa.getType().contains(
				"MinimumQualityInterpretationScreeningAssertion")) {
			localtype[1] = "QualityLevelVariable";
			localtype[2] = getHasAttribute(sa.getHasAttribute());
		} else if (sa.getType()
				.contains("RetrievalConditionScreeningAssertion")) {
			localtype[1] = "RetrievalConditionFlag";
			HashMap<String, Boolean> flags = getAllRetrievalConditions();
			Iterator it = flags.entrySet().iterator();
			localtype[2] = "";
			while (it.hasNext()) {
				Map.Entry flagpair = (Map.Entry) it.next();
				localtype[2] += flagpair.getKey() + ", ";
			}
			localtype[2] = localtype[2].substring(0, localtype[2].length() - 2);
		} else if (sa.getType().contains("ThresholdScreeningAssertion")) {
			localtype[1] = "ThresholdLevelVariable";
			localtype[2] = getHasAttribute(sa.getHasAttribute());
		} else if (type.contains("SurfaceTypeScreeningAssertion")) {
			localtype[1] = "SurfaceTypeFlag";
			localtype[2] = getHasAttribute(sa.getHasAttribute());
		}

		try {
			localtype[3] = getHasAttributeValue(sa);
			SATypes.put(ScreeningFieldName, localtype);

		} catch (NullPointerException e) {
			logger.error("getHasAttributeValue returned null for " + sa);
			localtype = null;
		}
		return localtype;
		// SATypes.put(SFName, sa[nsa].);
	}
}

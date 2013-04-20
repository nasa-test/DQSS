package gov.nasa.gsfc.dqss;

import gov.nasa.gsfc.ontology.XPathGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DqssXPath extends XPathGenerator {
    
    static Logger logger = Logger.getLogger(DqssXPath.class.getName());
    @Override
    public  String interpolateSparqlQuery(String queryString, List<String> propList)
    {
    	   //queryString = queryString.replaceAll("\"", "\\\\\"");
    	   String dataset = stripVersion(propList.get(0));
		   queryString = queryString.replaceAll("DATASET", dataset);
		   if (propList.size() >= 2) {
			   queryString = queryString.replaceAll("DATAVARIABLE", propList.get(1));
			   //logger.debug(queryString);
		   }
		   if (propList.size() >= 3) {
			   queryString = queryString.replaceFirst("CRITERIA", propList.get(2));
			   //logger.debug(queryString);
		   }
		   if (propList.size() >= 4) {
			   queryString = queryString.replaceFirst("CRITERIA1", propList.get(3));
		   }
		   logger.debug(queryString);
	  	   return queryString;
    }
    public String stripVersion(String dataset) {
 	   int dot = dataset.indexOf(".");
 	   if (dot > 0) {
 		   return dataset.substring(0,dot);
 	   }
 	   else {
 		   return dataset;
 	   }
    }
   
	@Override
	public List<Object> getXpath(Document doc, HashMap<String, Object> inputHash) {
		// TODO Auto-generated method stub
	//	inputHash = new HashMap<String,Object>();
		List<String> xpath = new ArrayList<String>();
			
		String project_xpath = "/navigationTree/dqssTree/query";
	
		 xpath.add(project_xpath);	
		
		List<Object> nodeXpath=null;
		try {
			nodeXpath = getConfigNodes(doc,xpath);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 String nextnode = (String) nodeXpath.get(0);
		 Element newXpath = (Element) nodeXpath.get(1);
		 logger.debug("newXpath: "+newXpath);
		 logger.debug(nextnode);
		 xpath.clear();
		 String cleanNode = nextnode + "/dataset";
		 if (getInputValue("dataset",inputHash) != null)
		 {
			 
			 String xp = nextnode +  "/dataset" + "[@NAME='"+getInputValue("dataset",inputHash)+"']";
			 logger.debug("dataset : "+xp);
			 xpath.add(xp);
			 try {
				nodeXpath = getConfigNodes(doc,xpath);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 nextnode = (String) nodeXpath.get(0);
			 newXpath = (Element) nodeXpath.get(1); 
		 }
		 xpath.clear();
		 if (getInputValue("datavariable",inputHash) != null)
		 {
			 logger.debug("nextnode:" + nextnode);
			 nextnode = cleanNode + "/" + "datavariable";
			 	 
			 xpath.add(nextnode+"[@NAME='"+getInputValue("datavariable",inputHash)+"']");
			 xpath.add(nextnode+"[@NAME='"+"']");
			
			 logger.debug("xpath:" + xpath.get(0));
			 try {
				nodeXpath = getConfigNodes(doc,xpath);
				 nextnode = (String) nodeXpath.get(0);
				 newXpath = (Element) nodeXpath.get(1); 
				 logger.debug("should be datavariable:" + nextnode);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("Could not find data variable in Ontology Navigation file");
				return null;
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				logger.error(getInputValue("datavariable",inputHash) + " spelling between ontology and DQSSNavigation.xml doesn't match");
			}
			 
			/*
			if (nodeXpath.isEmpty()) {
				logger.error("Could not find data variable in Ontology Navigation file");
				return null;
			}
			
			 newXpath = (Element)newXpath.getParentNode();
			 logger.debug("parent tag name:" + newXpath.getTagName());
			 
			 if (newXpath != null) {	
				 Node queryNode = (Node) newXpath.getParentNode();
				 
				 logger.debug("text:" + queryNode.getFirstChild().getTextContent() + "\n end");
				 
			 }
			 else {
				 logger.debug("Could not find anything at " + nextnode);
				 
			 }
			 
		 */
		 
			 
		 }
		 return nodeXpath;	
	}

}

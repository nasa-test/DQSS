package gov.nasa.gsfc.gesdisc.ontology;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;


public class QueryOntology {
	private String queryStatement;
	private OntologyModel model;
	private Document doc;
	
	private static final String prefix=""; 
	 	
	
	
	public HashMap<String,HashMap<String,String>> getQueryResults() {
		Query query = QueryFactory.create(queryStatement);
		QueryExecution qexec = QueryExecutionFactory.create(query, model.getModel());
		HashMap<String,HashMap<String,String>> valueMap = new HashMap<String, HashMap<String,String>>();
		// System.out.println("List of Labels:");
	      try {
	         
	           ResultSet results = qexec.execSelect() ;
	           List<String> b = results.getResultVars();
	           String name = "";	 
	           
	           HashMap<String,String> countMap = null;
	           // System.out.println(name.toString());
	           int count = 1;
	           for (;results.hasNext();)
	           {
	                QuerySolution soln = results.nextSolution() ;
	                
	                String key ="";
	                countMap = new HashMap<String, String>();
	                for (Iterator<String> it = b.iterator();it.hasNext();)
			        {
			                                 
			          name = it.next(); 
	                  RDFNode x = soln.get(name) ;   
	       
	                  if(x.isResource())
	                  {
	                	  String subSt[] = x.toString().split("#");
	                	  countMap.put(name,subSt[1]);
	                  }
	                  else
	                  {
	                  countMap.put(name,x.toString());
	                  }
	                  
	                 }
	                key = new Integer(count).toString() ;
	                
	                valueMap.put(key, countMap);
	                count++;
		         }  
		           
	       }
	      finally { 
	    	  qexec.close() ; 
	       }
	       
	      
		return valueMap;
	}
	public String getQueryResult() throws TransformerException {
		Query query = QueryFactory.create(queryStatement);
		QueryExecution qexec = QueryExecutionFactory.create(query, model.getModel());
		String valueMap = "Test";
		Element root = null;
		
		// System.out.println("List of Labels:");
	      try {
	    	    DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	            DocumentBuilder docBuilder;
				
				docBuilder = dbfac.newDocumentBuilder();
				
	            doc = docBuilder.newDocument();
	            root = doc.createElement("root");
	            Comment comment = doc.createComment("XML result from SPARQL Query");
	            root.appendChild(comment);
	            doc.appendChild(root);
	      }catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      Element child = null;
	      
          try{
	           ResultSet results = qexec.execSelect() ;
	           List<String> b = results.getResultVars();
	           String name = "";	 
	           
	           // System.out.println(name.toString());
	           int count = 1;
	           for (;results.hasNext();)
	           {
	        	   Element child2 = doc.createElement("queryOutput");
		           root.appendChild(child2);
	                QuerySolution soln = results.nextSolution() ;
	                
	                for (Iterator<String> it = b.iterator();it.hasNext();)
			        {
			                                 
			          name = it.next(); 
				  RDFNode x = soln.get(name) ;   
			try 
			 {
				if (x != null)
				  {
				  child = doc.createElement(name);
				  child2.appendChild(child);
				  if(x.isResource())
				  {
					  String subSt[] = x.toString().split("#");
					  valueMap = valueMap + name + ":" + subSt[1]+",";
					  Text text = doc.createTextNode(subSt[1]);
				      child.appendChild(text);

				  }
				  else
				  {
					  Text text = doc.createTextNode(x.toString());
				      child.appendChild(text);
				   valueMap = valueMap + name + ":" + x.toString()+",";
				  }
				 }
                         } 
                      catch (NullPointerException e)
                      {
                    	  System.out.println(e.getMessage());
                      }
	                 }
	                count++;
		         }  
		           
          }
	       
	      finally { 
	    	  qexec.close() ; 
	       }
	       
	      //set up a transformer
          TransformerFactory transfac = TransformerFactory.newInstance();
          Transformer trans;
		try {
			trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	          trans.setOutputProperty(OutputKeys.INDENT, "no");
		
          

          //create string from xml tree
          StringWriter sw = new StringWriter();
          StreamResult result = new StreamResult(sw);
          DOMSource source = new DOMSource(doc);
          trans.transform(source, result);
          

          valueMap = result.getWriter().toString();
         // System.out.println(result.getWriter().toString());
		}
          catch (TransformerConfigurationException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
        // printToFile();
		return valueMap;
	}
	public void setQueryStatement(String queryStatement) {
		this.queryStatement = /* prefix + " " + */ queryStatement;
	}
	public void setModel(OntologyModel model) {
		this.model = model;
	}
	private void printToFile(){

		try
		{
			//print//create the root element and add it to the document
            

			
			OutputFormat format = new OutputFormat(doc);
			format.setIndenting(true);

			//to generate output to console use this serializer
			// XMLSerializer serializer = new XMLSerializer(System.out, format);


			//to generate a file output use fileoutputstream instead of system.out
			XMLSerializer serializer = new XMLSerializer(
			new FileOutputStream(new File("/var/tmp/sparql.xml")), format);

			serializer.serialize(doc);

		} catch(IOException ie) {
		    ie.printStackTrace();
		}
	}

}

package gov.nasa.gsfc.gesdisc.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.Iterator;

public class OntologyModel {
	private OntModel ontModel;
        private ArrayList<String> ontList;
        private boolean loaded;
	public OntologyModel()
	{
		ontModel = ModelFactory.createOntologyModel(
	               OntModelSpec.OWL_MEM
	             );
		ontList = new ArrayList<String>();
                setLoaded(false);
	}
	public boolean isLoaded() {
		return loaded;
	}
	private void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	public ArrayList<String> getOntologyUrls() {
		return ontList;
	}
	public void setOntologyUrls(String miradorOwlUrl) {
                ontList.add(miradorOwlUrl);
	}
        public void readOntology()
	{
		for (Iterator<String> it = ontList.iterator (); it.hasNext (); ) 
		{
		    String url = it.next();
		    ontModel.read(url);
		}
               setLoaded(true);
	}
	public OntModel getModel() {
		
		return ontModel;
	}
	public void setModel(OntModel m) {
		this.ontModel = m;
	}
	
}

package gov.nasa.gsfc.dqss;

import ncsa.hdf.object.Dataset;

public class SDS  {

	Mask mask;
	Dataset data;
	Dataset[] quality;
	protected long rows = -1;
	protected long cols = -1;
	protected long layers = 1;
    String variableName;
    
	public  SDS(String dataVariableName) {
		variableName = dataVariableName;
	}

private  void determineDimensions() {

    long [] dims = (long []) data.getDims();
    int[] d = new int[dims.length];
    rows = dims[0];
    cols = dims[1];
    if (data.getRank() > 2) {
    	layers = dims[2];
    }
                
     System.err.print(data.getName()); 
     System.err.println("\tlength:" + d.length); 
     System.err.println("\trank:" + data.getRank()); 
     System.out.println("\trank:" + data.getRank()); 
     System.err.println("\tpath:" + data.getPath()); 
     System.out.println("\tlength:" + d.length); 
    for(int i=0 ; i < d.length  ; ++i) {
       if (dims[i] > 0) {
        System.out.print("\tdimensions:" + dims[i]); 
        d[i] = (int)dims[i];
       }
    } 
   

}
}
package gov.nasa.gsfc.dqss;
import ncsa.hdf.object.Dataset;

public class DQSSNumber extends Number {

	private double value;
	private double array[];
	private long size = 1;
	private String type;
	
	public void setType(String t) {
		type = t;
	}
	public DQSSNumber(double init) {
		value = init;
	}
	public DQSSNumber(float init) {
		value = init;
	}
	public DQSSNumber(byte init) {
		value = init;
	}
	public DQSSNumber(byte[] init) {
		size = (long) init.length;
		array = new double[(int) size];
		for (int i = 0; i < init.length; ++i) {
			array[i] = init[i];
			if (init[i] != 0) {
				//System.err.print(i + ":" + init[i] + ",  ");
			}
		}
		//System.err.print("done\n");
	}
	public DQSSNumber(short[] init) {
		size = (long) init.length;
		array = new double[(int) size];
		for (int i = 0; i < init.length; ++i) {
			array[i] = init[i];
			if (init[i] > 0) {
				//System.err.print(i + ":" + init[i] + ",  ");
			}
			
		}
		//System.err.print("done\n");
	}
	public DQSSNumber(int[] init) {
		size = (long) init.length;
		array = new double[(int) size];
		for (int i = 0; i < init.length; ++i) {
			array[i] = init[i];
		}
	}
	public DQSSNumber(float[] init) {
		size = (long) init.length;
		array = new double[(int) size];
		for (int i = 0; i < init.length; ++i) {
			array[i] = init[i];
		}
	}
	public DQSSNumber(double[] init) {
		size = (long) init.length;
		array = new double[(int) size];
		for (int i = 0; i < init.length; ++i) {
			array[i] = init[i];
		}
	}
	public void reInstate(Dataset d ,datafield DF) {
		if (DF.type instanceof Short) {
			short[] arr = new short[(int)size];
			for (int i = 0; i < size; ++i) {
				arr[i] = (short) array[i];
			}
			try {
				d.write(arr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (DF.type instanceof Integer) {
			int[] arr = new int[(int)size];
			for (int i = 0; i < size; ++i) {
				arr[i] = (int) array[i];
			}
			try {
				d.write(arr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (DF.type instanceof Long) {
			long[] arr = new long[(int)size];
			for (int i = 0; i < size; ++i) {
				arr[i] = (long) array[i];
			}
			try {
				d.write(arr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (DF.type instanceof Float) {
			float[] arr = new float[(int)size];
			for (int i = 0; i < size; ++i) {
				arr[i] = (float) array[i];
			}
			try {
				d.write(arr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (DF.type instanceof Double) {
			double[] arr = new double[(int)size];
			for (int i = 0; i < size; ++i) {
				arr[i] = (double) array[i];
			}
			try {
				d.write(arr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public float[] reInstate(float  t) {
		float[] arr = new float[(int)size];
		for (int i = 0; i < size; ++i) {
			arr[i] = (float) array[i];
		}
		return arr;
	}
	
    public void setArray(int ith, double value) {
    	array[ith] = value;
    }
	public String getType() {
		return this.type;
	}
	public final double getArray(int i) {
		return array[i];
	}
	
	public final double get() {
		return value;
	}
	public long getSize () {
		return this.size;
	}
	public String toString(){
		return new Double(doubleValue()).toString();
	}
	@Override
	public double doubleValue() {
	
		return get();
	}

	@Override
	public float floatValue() {
	
		return (float) get();
	}

	@Override
	public int intValue() {
		
		return (int) get();
	}

	public short shortValue() {
		return (short) get();
	}
	
	@Override
	public long longValue() {
		
		return (long) get();
	}
	
	public byte byteValue() {
		return (byte) get();
	}
	public byte byteArrayValue(int i) {
		return (byte) getArray(i);
	}
	/*
	 * One technique might be to use overloading to do 
	 * this
	 */
	public short ArrayValue(int i, short type) {
		return (short) getArray(i);
	}
	
	/*
	 * instead of this:
	 */
	public short shortArrayValue(int i) {
		return (short) getArray(i);
	}
	public double ArrayValue(int i) {
		return (double) getArray(i);
	}
	public void set(double in) {
		value = in;
	}
	
	public boolean compare(double in) {
		//System.out.println("Comparing" + this.doubleValue() + " and " + in);
		if (doubleValue() == in) {
			return true;
		}
		return false;
	}
	public boolean compare(double in, int ith) {
		if (in != -9999.0) {
			// don't show fill value comparisons.
			//System.out.println("Comparing (" + ArrayValue(ith) + ", " + in + ")");
		}
		
		if (ArrayValue(ith) == in) {
			return true;
		}
		return false;
	}
}

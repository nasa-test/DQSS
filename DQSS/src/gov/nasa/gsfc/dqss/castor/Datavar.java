/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id: Datavar.java,v 1.2 2010/11/10 17:59:46 rstrub Exp $
 */

package gov.nasa.gsfc.dqss.castor;

/**
 * Class Datavar.
 * 
 * @version $Revision: 1.2 $ $Date: 2010/11/10 17:59:46 $
 */
@SuppressWarnings("serial")
public class Datavar implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _queryOutputList.
     */
    private java.util.Vector<gov.nasa.gsfc.dqss.castor.QueryOutput> _queryOutputList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Datavar() {
        super();
        this._queryOutputList = new java.util.Vector<gov.nasa.gsfc.dqss.castor.QueryOutput>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vQueryOutput
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addQueryOutput(
            final gov.nasa.gsfc.dqss.castor.QueryOutput vQueryOutput)
    throws java.lang.IndexOutOfBoundsException {
        this._queryOutputList.addElement(vQueryOutput);
    }

    /**
     * 
     * 
     * @param index
     * @param vQueryOutput
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addQueryOutput(
            final int index,
            final gov.nasa.gsfc.dqss.castor.QueryOutput vQueryOutput)
    throws java.lang.IndexOutOfBoundsException {
        this._queryOutputList.add(index, vQueryOutput);
    }

    /**
     * Method enumerateQueryOutput.
     * 
     * @return an Enumeration over all
     * gov.nasa.gsfc.dqss.castor.QueryOutput elements
     */
    public java.util.Enumeration<? extends gov.nasa.gsfc.dqss.castor.QueryOutput> enumerateQueryOutput(
    ) {
        return this._queryOutputList.elements();
    }

    /**
     * Method getQueryOutput.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * gov.nasa.gsfc.dqss.castor.QueryOutput at the given index
     */
    public gov.nasa.gsfc.dqss.castor.QueryOutput getQueryOutput(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._queryOutputList.size()) {
            throw new IndexOutOfBoundsException("getQueryOutput: Index value '" + index + "' not in range [0.." + (this._queryOutputList.size() - 1) + "]");
        }

        return (gov.nasa.gsfc.dqss.castor.QueryOutput) _queryOutputList.get(index);
    }

    /**
     * Method getQueryOutput.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public gov.nasa.gsfc.dqss.castor.QueryOutput[] getQueryOutput(
    ) {
        gov.nasa.gsfc.dqss.castor.QueryOutput[] array = new gov.nasa.gsfc.dqss.castor.QueryOutput[0];
        return (gov.nasa.gsfc.dqss.castor.QueryOutput[]) this._queryOutputList.toArray(array);
    }

    /**
     * Method getQueryOutputCount.
     * 
     * @return the size of this collection
     */
    public int getQueryOutputCount(
    ) {
        return this._queryOutputList.size();
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllQueryOutput(
    ) {
        this._queryOutputList.clear();
    }

    /**
     * Method removeQueryOutput.
     * 
     * @param vQueryOutput
     * @return true if the object was removed from the collection.
     */
    public boolean removeQueryOutput(
            final gov.nasa.gsfc.dqss.castor.QueryOutput vQueryOutput) {
        boolean removed = _queryOutputList.remove(vQueryOutput);
        return removed;
    }

    /**
     * Method removeQueryOutputAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public gov.nasa.gsfc.dqss.castor.QueryOutput removeQueryOutputAt(
            final int index) {
        java.lang.Object obj = this._queryOutputList.remove(index);
        return (gov.nasa.gsfc.dqss.castor.QueryOutput) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vQueryOutput
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setQueryOutput(
            final int index,
            final gov.nasa.gsfc.dqss.castor.QueryOutput vQueryOutput)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._queryOutputList.size()) {
            throw new IndexOutOfBoundsException("setQueryOutput: Index value '" + index + "' not in range [0.." + (this._queryOutputList.size() - 1) + "]");
        }

        this._queryOutputList.set(index, vQueryOutput);
    }

    /**
     * 
     * 
     * @param vQueryOutputArray
     */
    public void setQueryOutput(
            final gov.nasa.gsfc.dqss.castor.QueryOutput[] vQueryOutputArray) {
        //-- copy array
        _queryOutputList.clear();

        for (int i = 0; i < vQueryOutputArray.length; i++) {
                this._queryOutputList.add(vQueryOutputArray[i]);
        }
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled gov.nasa.gsfc.dqss.castor.Datavar
     */
    public static gov.nasa.gsfc.dqss.castor.Datavar unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.Datavar) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.Datavar.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}

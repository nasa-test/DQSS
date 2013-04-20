/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id: Root.java,v 1.5 2010/11/10 17:59:46 rstrub Exp $
 */

package gov.nasa.gsfc.dqss.castor;

/**
 * Class Root.
 * 
 * @version $Revision: 1.5 $ $Date: 2010/11/10 17:59:46 $
 */
@SuppressWarnings("serial")
public class Root implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _datavarList.
     */
    private java.util.Vector<gov.nasa.gsfc.dqss.castor.Datavar> _datavarList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Root() {
        super();
        this._datavarList = new java.util.Vector<gov.nasa.gsfc.dqss.castor.Datavar>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDatavar
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDatavar(
            final gov.nasa.gsfc.dqss.castor.Datavar vDatavar)
    throws java.lang.IndexOutOfBoundsException {
        this._datavarList.addElement(vDatavar);
    }

    /**
     * 
     * 
     * @param index
     * @param vDatavar
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDatavar(
            final int index,
            final gov.nasa.gsfc.dqss.castor.Datavar vDatavar)
    throws java.lang.IndexOutOfBoundsException {
        this._datavarList.add(index, vDatavar);
    }

    /**
     * Method enumerateDatavar.
     * 
     * @return an Enumeration over all
     * gov.nasa.gsfc.dqss.castor.Datavar elements
     */
    public java.util.Enumeration<? extends gov.nasa.gsfc.dqss.castor.Datavar> enumerateDatavar(
    ) {
        return this._datavarList.elements();
    }

    /**
     * Method getDatavar.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the gov.nasa.gsfc.dqss.castor.Datavar
     * at the given index
     */
    public gov.nasa.gsfc.dqss.castor.Datavar getDatavar(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._datavarList.size()) {
            throw new IndexOutOfBoundsException("getDatavar: Index value '" + index + "' not in range [0.." + (this._datavarList.size() - 1) + "]");
        }

        return (gov.nasa.gsfc.dqss.castor.Datavar) _datavarList.get(index);
    }

    /**
     * Method getDatavar.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public gov.nasa.gsfc.dqss.castor.Datavar[] getDatavar(
    ) {
        gov.nasa.gsfc.dqss.castor.Datavar[] array = new gov.nasa.gsfc.dqss.castor.Datavar[0];
        return (gov.nasa.gsfc.dqss.castor.Datavar[]) this._datavarList.toArray(array);
    }

    /**
     * Method getDatavarCount.
     * 
     * @return the size of this collection
     */
    public int getDatavarCount(
    ) {
        return this._datavarList.size();
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
    public void removeAllDatavar(
    ) {
        this._datavarList.clear();
    }

    /**
     * Method removeDatavar.
     * 
     * @param vDatavar
     * @return true if the object was removed from the collection.
     */
    public boolean removeDatavar(
            final gov.nasa.gsfc.dqss.castor.Datavar vDatavar) {
        boolean removed = _datavarList.remove(vDatavar);
        return removed;
    }

    /**
     * Method removeDatavarAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public gov.nasa.gsfc.dqss.castor.Datavar removeDatavarAt(
            final int index) {
        java.lang.Object obj = this._datavarList.remove(index);
        return (gov.nasa.gsfc.dqss.castor.Datavar) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vDatavar
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDatavar(
            final int index,
            final gov.nasa.gsfc.dqss.castor.Datavar vDatavar)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._datavarList.size()) {
            throw new IndexOutOfBoundsException("setDatavar: Index value '" + index + "' not in range [0.." + (this._datavarList.size() - 1) + "]");
        }

        this._datavarList.set(index, vDatavar);
    }

    /**
     * 
     * 
     * @param vDatavarArray
     */
    public void setDatavar(
            final gov.nasa.gsfc.dqss.castor.Datavar[] vDatavarArray) {
        //-- copy array
        _datavarList.clear();

        for (int i = 0; i < vDatavarArray.length; i++) {
                this._datavarList.add(vDatavarArray[i]);
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
     * @return the unmarshaled gov.nasa.gsfc.dqss.castor.Root
     */
    public static gov.nasa.gsfc.dqss.castor.Root unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.Root) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.Root.class, reader);
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

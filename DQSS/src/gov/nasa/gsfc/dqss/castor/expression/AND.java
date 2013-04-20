/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package gov.nasa.gsfc.dqss.castor.expression;

/**
 * Class AND.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class AND implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _NOT.
     */
    private gov.nasa.gsfc.dqss.castor.expression.NOT _NOT;

    /**
     * Field _screeningAssertionList.
     */
    private java.util.Vector<gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion> _screeningAssertionList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AND() {
        super();
        this._screeningAssertionList = new java.util.Vector<gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vScreeningAssertion
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addScreeningAssertion(
            final gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion vScreeningAssertion)
    throws java.lang.IndexOutOfBoundsException {
        this._screeningAssertionList.addElement(vScreeningAssertion);
    }

    /**
     * 
     * 
     * @param index
     * @param vScreeningAssertion
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addScreeningAssertion(
            final int index,
            final gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion vScreeningAssertion)
    throws java.lang.IndexOutOfBoundsException {
        this._screeningAssertionList.add(index, vScreeningAssertion);
    }

    /**
     * Method enumerateScreeningAssertion.
     * 
     * @return an Enumeration over all
     * gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion
     * elements
     */
    public java.util.Enumeration<? extends gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion> enumerateScreeningAssertion(
    ) {
        return this._screeningAssertionList.elements();
    }

    /**
     * Returns the value of field 'NOT'.
     * 
     * @return the value of field 'NOT'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.NOT getNOT(
    ) {
        return this._NOT;
    }

    /**
     * Method getScreeningAssertion.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion at
     * the given index
     */
    public gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion getScreeningAssertion(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._screeningAssertionList.size()) {
            throw new IndexOutOfBoundsException("getScreeningAssertion: Index value '" + index + "' not in range [0.." + (this._screeningAssertionList.size() - 1) + "]");
        }

        return (gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion) _screeningAssertionList.get(index);
    }

    /**
     * Method getScreeningAssertion.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion[] getScreeningAssertion(
    ) {
        gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion[] array = new gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion[0];
        return (gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion[]) this._screeningAssertionList.toArray(array);
    }

    /**
     * Method getScreeningAssertionCount.
     * 
     * @return the size of this collection
     */
    public int getScreeningAssertionCount(
    ) {
        return this._screeningAssertionList.size();
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
    public void removeAllScreeningAssertion(
    ) {
        this._screeningAssertionList.clear();
    }

    /**
     * Method removeScreeningAssertion.
     * 
     * @param vScreeningAssertion
     * @return true if the object was removed from the collection.
     */
    public boolean removeScreeningAssertion(
            final gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion vScreeningAssertion) {
        boolean removed = _screeningAssertionList.remove(vScreeningAssertion);
        return removed;
    }

    /**
     * Method removeScreeningAssertionAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion removeScreeningAssertionAt(
            final int index) {
        java.lang.Object obj = this._screeningAssertionList.remove(index);
        return (gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion) obj;
    }

    /**
     * Sets the value of field 'NOT'.
     * 
     * @param NOT the value of field 'NOT'.
     */
    public void setNOT(
            final gov.nasa.gsfc.dqss.castor.expression.NOT NOT) {
        this._NOT = NOT;
    }

    /**
     * 
     * 
     * @param index
     * @param vScreeningAssertion
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setScreeningAssertion(
            final int index,
            final gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion vScreeningAssertion)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._screeningAssertionList.size()) {
            throw new IndexOutOfBoundsException("setScreeningAssertion: Index value '" + index + "' not in range [0.." + (this._screeningAssertionList.size() - 1) + "]");
        }

        this._screeningAssertionList.set(index, vScreeningAssertion);
    }

    /**
     * 
     * 
     * @param vScreeningAssertionArray
     */
    public void setScreeningAssertion(
            final gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion[] vScreeningAssertionArray) {
        //-- copy array
        _screeningAssertionList.clear();

        for (int i = 0; i < vScreeningAssertionArray.length; i++) {
                this._screeningAssertionList.add(vScreeningAssertionArray[i]);
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
     * @return the unmarshaled
     * gov.nasa.gsfc.dqss.castor.expression.AND
     */
    public static gov.nasa.gsfc.dqss.castor.expression.AND unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.expression.AND) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.expression.AND.class, reader);
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

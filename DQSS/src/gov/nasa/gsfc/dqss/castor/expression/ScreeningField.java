/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package gov.nasa.gsfc.dqss.castor.expression;

/**
 * Class ScreeningField.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ScreeningField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _dimensionList.
     */
    private java.util.Vector<gov.nasa.gsfc.dqss.castor.expression.Dimension> _dimensionList;

    /**
     * Field _variable.
     */
    private gov.nasa.gsfc.dqss.castor.expression.Variable _variable;

    /**
     * Field _fieldValueType.
     */
    private gov.nasa.gsfc.dqss.castor.expression.FieldValueType _fieldValueType;


      //----------------/
     //- Constructors -/
    //----------------/

    public ScreeningField() {
        super();
        this._dimensionList = new java.util.Vector<gov.nasa.gsfc.dqss.castor.expression.Dimension>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDimension
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDimension(
            final gov.nasa.gsfc.dqss.castor.expression.Dimension vDimension)
    throws java.lang.IndexOutOfBoundsException {
        this._dimensionList.addElement(vDimension);
    }

    /**
     * 
     * 
     * @param index
     * @param vDimension
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDimension(
            final int index,
            final gov.nasa.gsfc.dqss.castor.expression.Dimension vDimension)
    throws java.lang.IndexOutOfBoundsException {
        this._dimensionList.add(index, vDimension);
    }

    /**
     * Method enumerateDimension.
     * 
     * @return an Enumeration over all
     * gov.nasa.gsfc.dqss.castor.expression.Dimension elements
     */
    public java.util.Enumeration<? extends gov.nasa.gsfc.dqss.castor.expression.Dimension> enumerateDimension(
    ) {
        return this._dimensionList.elements();
    }

    /**
     * Method getDimension.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * gov.nasa.gsfc.dqss.castor.expression.Dimension at the given
     * index
     */
    public gov.nasa.gsfc.dqss.castor.expression.Dimension getDimension(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._dimensionList.size()) {
            throw new IndexOutOfBoundsException("getDimension: Index value '" + index + "' not in range [0.." + (this._dimensionList.size() - 1) + "]");
        }

        return (gov.nasa.gsfc.dqss.castor.expression.Dimension) _dimensionList.get(index);
    }

    /**
     * Method getDimension.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public gov.nasa.gsfc.dqss.castor.expression.Dimension[] getDimension(
    ) {
        gov.nasa.gsfc.dqss.castor.expression.Dimension[] array = new gov.nasa.gsfc.dqss.castor.expression.Dimension[0];
        return (gov.nasa.gsfc.dqss.castor.expression.Dimension[]) this._dimensionList.toArray(array);
    }

    /**
     * Method getDimensionCount.
     * 
     * @return the size of this collection
     */
    public int getDimensionCount(
    ) {
        return this._dimensionList.size();
    }

    /**
     * Returns the value of field 'fieldValueType'.
     * 
     * @return the value of field 'FieldValueType'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.FieldValueType getFieldValueType(
    ) {
        return this._fieldValueType;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'variable'.
     * 
     * @return the value of field 'Variable'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.Variable getVariable(
    ) {
        return this._variable;
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
    public void removeAllDimension(
    ) {
        this._dimensionList.clear();
    }

    /**
     * Method removeDimension.
     * 
     * @param vDimension
     * @return true if the object was removed from the collection.
     */
    public boolean removeDimension(
            final gov.nasa.gsfc.dqss.castor.expression.Dimension vDimension) {
        boolean removed = _dimensionList.remove(vDimension);
        return removed;
    }

    /**
     * Method removeDimensionAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public gov.nasa.gsfc.dqss.castor.expression.Dimension removeDimensionAt(
            final int index) {
        java.lang.Object obj = this._dimensionList.remove(index);
        return (gov.nasa.gsfc.dqss.castor.expression.Dimension) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vDimension
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDimension(
            final int index,
            final gov.nasa.gsfc.dqss.castor.expression.Dimension vDimension)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._dimensionList.size()) {
            throw new IndexOutOfBoundsException("setDimension: Index value '" + index + "' not in range [0.." + (this._dimensionList.size() - 1) + "]");
        }

        this._dimensionList.set(index, vDimension);
    }

    /**
     * 
     * 
     * @param vDimensionArray
     */
    public void setDimension(
            final gov.nasa.gsfc.dqss.castor.expression.Dimension[] vDimensionArray) {
        //-- copy array
        _dimensionList.clear();

        for (int i = 0; i < vDimensionArray.length; i++) {
                this._dimensionList.add(vDimensionArray[i]);
        }
    }

    /**
     * Sets the value of field 'fieldValueType'.
     * 
     * @param fieldValueType the value of field 'fieldValueType'.
     */
    public void setFieldValueType(
            final gov.nasa.gsfc.dqss.castor.expression.FieldValueType fieldValueType) {
        this._fieldValueType = fieldValueType;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'variable'.
     * 
     * @param variable the value of field 'variable'.
     */
    public void setVariable(
            final gov.nasa.gsfc.dqss.castor.expression.Variable variable) {
        this._variable = variable;
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
     * gov.nasa.gsfc.dqss.castor.expression.ScreeningField
     */
    public static gov.nasa.gsfc.dqss.castor.expression.ScreeningField unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.expression.ScreeningField) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.expression.ScreeningField.class, reader);
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

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package gov.nasa.gsfc.dqss.castor.expression;

/**
 * Class HasAttribute.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class HasAttribute implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _hasAttributeChoice.
     */
    private gov.nasa.gsfc.dqss.castor.expression.HasAttributeChoice _hasAttributeChoice;

    /**
     * Field _hasMinThreshold.
     */
    private gov.nasa.gsfc.dqss.castor.expression.HasMinThreshold _hasMinThreshold;

    /**
     * Field _hasMaxThreshold.
     */
    private gov.nasa.gsfc.dqss.castor.expression.HasMaxThreshold _hasMaxThreshold;


      //----------------/
     //- Constructors -/
    //----------------/

    public HasAttribute() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'hasAttributeChoice'.
     * 
     * @return the value of field 'HasAttributeChoice'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.HasAttributeChoice getHasAttributeChoice(
    ) {
        return this._hasAttributeChoice;
    }

    /**
     * Returns the value of field 'hasMaxThreshold'.
     * 
     * @return the value of field 'HasMaxThreshold'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.HasMaxThreshold getHasMaxThreshold(
    ) {
        return this._hasMaxThreshold;
    }

    /**
     * Returns the value of field 'hasMinThreshold'.
     * 
     * @return the value of field 'HasMinThreshold'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.HasMinThreshold getHasMinThreshold(
    ) {
        return this._hasMinThreshold;
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
     * Sets the value of field 'hasAttributeChoice'.
     * 
     * @param hasAttributeChoice the value of field
     * 'hasAttributeChoice'.
     */
    public void setHasAttributeChoice(
            final gov.nasa.gsfc.dqss.castor.expression.HasAttributeChoice hasAttributeChoice) {
        this._hasAttributeChoice = hasAttributeChoice;
    }

    /**
     * Sets the value of field 'hasMaxThreshold'.
     * 
     * @param hasMaxThreshold the value of field 'hasMaxThreshold'.
     */
    public void setHasMaxThreshold(
            final gov.nasa.gsfc.dqss.castor.expression.HasMaxThreshold hasMaxThreshold) {
        this._hasMaxThreshold = hasMaxThreshold;
    }

    /**
     * Sets the value of field 'hasMinThreshold'.
     * 
     * @param hasMinThreshold the value of field 'hasMinThreshold'.
     */
    public void setHasMinThreshold(
            final gov.nasa.gsfc.dqss.castor.expression.HasMinThreshold hasMinThreshold) {
        this._hasMinThreshold = hasMinThreshold;
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
     * gov.nasa.gsfc.dqss.castor.expression.HasAttribute
     */
    public static gov.nasa.gsfc.dqss.castor.expression.HasAttribute unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.expression.HasAttribute) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.expression.HasAttribute.class, reader);
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

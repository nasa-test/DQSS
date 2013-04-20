/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package gov.nasa.gsfc.dqss.castor.expression;

/**
 * Class NOT.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class NOT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _screeningAssertion.
     */
    private gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion _screeningAssertion;


      //----------------/
     //- Constructors -/
    //----------------/

    public NOT() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'screeningAssertion'.
     * 
     * @return the value of field 'ScreeningAssertion'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion getScreeningAssertion(
    ) {
        return this._screeningAssertion;
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
     * Sets the value of field 'screeningAssertion'.
     * 
     * @param screeningAssertion the value of field
     * 'screeningAssertion'.
     */
    public void setScreeningAssertion(
            final gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion screeningAssertion) {
        this._screeningAssertion = screeningAssertion;
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
     * gov.nasa.gsfc.dqss.castor.expression.NOT
     */
    public static gov.nasa.gsfc.dqss.castor.expression.NOT unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.expression.NOT) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.expression.NOT.class, reader);
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

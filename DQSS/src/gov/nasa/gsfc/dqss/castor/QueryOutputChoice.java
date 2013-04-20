/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id: QueryOutputChoice.java,v 1.2 2010/11/10 17:59:46 rstrub Exp $
 */

package gov.nasa.gsfc.dqss.castor;

/**
 * Class QueryOutputChoice.
 * 
 * @version $Revision: 1.2 $ $Date: 2010/11/10 17:59:46 $
 */
@SuppressWarnings("serial")
public class QueryOutputChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _referenceVariable.
     */
    private java.lang.String _referenceVariable;

    /**
     * Field _queryOutputChoiceSequence.
     */
    private gov.nasa.gsfc.dqss.castor.QueryOutputChoiceSequence _queryOutputChoiceSequence;


      //----------------/
     //- Constructors -/
    //----------------/

    public QueryOutputChoice() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'queryOutputChoiceSequence'.
     * 
     * @return the value of field 'QueryOutputChoiceSequence'.
     */
    public gov.nasa.gsfc.dqss.castor.QueryOutputChoiceSequence getQueryOutputChoiceSequence(
    ) {
        return this._queryOutputChoiceSequence;
    }

    /**
     * Returns the value of field 'referenceVariable'.
     * 
     * @return the value of field 'ReferenceVariable'.
     */
    public java.lang.String getReferenceVariable(
    ) {
        return this._referenceVariable;
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
     * Sets the value of field 'queryOutputChoiceSequence'.
     * 
     * @param queryOutputChoiceSequence the value of field
     * 'queryOutputChoiceSequence'.
     */
    public void setQueryOutputChoiceSequence(
            final gov.nasa.gsfc.dqss.castor.QueryOutputChoiceSequence queryOutputChoiceSequence) {
        this._queryOutputChoiceSequence = queryOutputChoiceSequence;
    }

    /**
     * Sets the value of field 'referenceVariable'.
     * 
     * @param referenceVariable the value of field
     * 'referenceVariable'.
     */
    public void setReferenceVariable(
            final java.lang.String referenceVariable) {
        this._referenceVariable = referenceVariable;
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
     * gov.nasa.gsfc.dqss.castor.QueryOutputChoice
     */
    public static gov.nasa.gsfc.dqss.castor.QueryOutputChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.QueryOutputChoice) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.QueryOutputChoice.class, reader);
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

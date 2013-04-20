/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id: QueryOutput.java,v 1.5 2010/11/10 17:59:46 rstrub Exp $
 */

package gov.nasa.gsfc.dqss.castor;

/**
 * Class QueryOutput.
 * 
 * @version $Revision: 1.5 $ $Date: 2010/11/10 17:59:46 $
 */
@SuppressWarnings("serial")
public class QueryOutput implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _qualityView.
     */
    private java.lang.String _qualityView;

    /**
     * Field _screeningVariable.
     */
    private java.lang.String _screeningVariable;

    /**
     * Field _screenVarType.
     */
    private java.lang.String _screenVarType;

    /**
     * Field _queryOutputChoice.
     */
    private gov.nasa.gsfc.dqss.castor.QueryOutputChoice _queryOutputChoice;

    /**
     * Field _direction.
     */
    private java.lang.String _direction;

    /**
     * Field _level.
     */
    private java.lang.String _level;

    /**
     * Field _refVarType.
     */
    private java.lang.String _refVarType;

    /**
     * Field _constraint_relationship.
     */
    private java.lang.String _constraint_relationship;


      //----------------/
     //- Constructors -/
    //----------------/

    public QueryOutput() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'constraint_relationship'.
     * 
     * @return the value of field 'Constraint_relationship'.
     */
    public java.lang.String getConstraint_relationship(
    ) {
        return this._constraint_relationship;
    }

    /**
     * Returns the value of field 'direction'.
     * 
     * @return the value of field 'Direction'.
     */
    public java.lang.String getDirection(
    ) {
        return this._direction;
    }

    /**
     * Returns the value of field 'level'.
     * 
     * @return the value of field 'Level'.
     */
    public java.lang.String getLevel(
    ) {
        return this._level;
    }

    /**
     * Returns the value of field 'qualityView'.
     * 
     * @return the value of field 'QualityView'.
     */
    public java.lang.String getQualityView(
    ) {
        return this._qualityView;
    }

    /**
     * Returns the value of field 'queryOutputChoice'.
     * 
     * @return the value of field 'QueryOutputChoice'.
     */
    public gov.nasa.gsfc.dqss.castor.QueryOutputChoice getQueryOutputChoice(
    ) {
        return this._queryOutputChoice;
    }

    /**
     * Returns the value of field 'refVarType'.
     * 
     * @return the value of field 'RefVarType'.
     */
    public java.lang.String getRefVarType(
    ) {
        return this._refVarType;
    }

    /**
     * Returns the value of field 'screenVarType'.
     * 
     * @return the value of field 'ScreenVarType'.
     */
    public java.lang.String getScreenVarType(
    ) {
        return this._screenVarType;
    }

    /**
     * Returns the value of field 'screeningVariable'.
     * 
     * @return the value of field 'ScreeningVariable'.
     */
    public java.lang.String getScreeningVariable(
    ) {
        return this._screeningVariable;
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
     * Sets the value of field 'constraint_relationship'.
     * 
     * @param constraint_relationship the value of field
     * 'constraint_relationship'.
     */
    public void setConstraint_relationship(
            final java.lang.String constraint_relationship) {
        this._constraint_relationship = constraint_relationship;
    }

    /**
     * Sets the value of field 'direction'.
     * 
     * @param direction the value of field 'direction'.
     */
    public void setDirection(
            final java.lang.String direction) {
        this._direction = direction;
    }

    /**
     * Sets the value of field 'level'.
     * 
     * @param level the value of field 'level'.
     */
    public void setLevel(
            final java.lang.String level) {
        this._level = level;
    }

    /**
     * Sets the value of field 'qualityView'.
     * 
     * @param qualityView the value of field 'qualityView'.
     */
    public void setQualityView(
            final java.lang.String qualityView) {
        this._qualityView = qualityView;
    }

    /**
     * Sets the value of field 'queryOutputChoice'.
     * 
     * @param queryOutputChoice the value of field
     * 'queryOutputChoice'.
     */
    public void setQueryOutputChoice(
            final gov.nasa.gsfc.dqss.castor.QueryOutputChoice queryOutputChoice) {
        this._queryOutputChoice = queryOutputChoice;
    }

    /**
     * Sets the value of field 'refVarType'.
     * 
     * @param refVarType the value of field 'refVarType'.
     */
    public void setRefVarType(
            final java.lang.String refVarType) {
        this._refVarType = refVarType;
    }

    /**
     * Sets the value of field 'screenVarType'.
     * 
     * @param screenVarType the value of field 'screenVarType'.
     */
    public void setScreenVarType(
            final java.lang.String screenVarType) {
        this._screenVarType = screenVarType;
    }

    /**
     * Sets the value of field 'screeningVariable'.
     * 
     * @param screeningVariable the value of field
     * 'screeningVariable'.
     */
    public void setScreeningVariable(
            final java.lang.String screeningVariable) {
        this._screeningVariable = screeningVariable;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled gov.nasa.gsfc.dqss.castor.QueryOutput
     */
    public static gov.nasa.gsfc.dqss.castor.QueryOutput unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.QueryOutput) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.QueryOutput.class, reader);
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

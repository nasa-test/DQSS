/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package gov.nasa.gsfc.dqss.castor.expression;

/**
 * Class HasAttributeChoice.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class HasAttributeChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _minimumQualityInterpretation.
     */
    private gov.nasa.gsfc.dqss.castor.expression.MinimumQualityInterpretation _minimumQualityInterpretation;

    /**
     * Field _qualityRecommendation.
     */
    private gov.nasa.gsfc.dqss.castor.expression.QualityRecommendation _qualityRecommendation;

    /**
     * Field _retrievalCondition.
     */
    private gov.nasa.gsfc.dqss.castor.expression.RetrievalCondition _retrievalCondition;

    /**
     * Field _surfaceType.
     */
    private gov.nasa.gsfc.dqss.castor.expression.SurfaceType _surfaceType;


      //----------------/
     //- Constructors -/
    //----------------/

    public HasAttributeChoice() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'minimumQualityInterpretation'.
     * 
     * @return the value of field 'MinimumQualityInterpretation'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.MinimumQualityInterpretation getMinimumQualityInterpretation(
    ) {
        return this._minimumQualityInterpretation;
    }

    /**
     * Returns the value of field 'qualityRecommendation'.
     * 
     * @return the value of field 'QualityRecommendation'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.QualityRecommendation getQualityRecommendation(
    ) {
        return this._qualityRecommendation;
    }

    /**
     * Returns the value of field 'retrievalCondition'.
     * 
     * @return the value of field 'RetrievalCondition'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.RetrievalCondition getRetrievalCondition(
    ) {
        return this._retrievalCondition;
    }

    /**
     * Returns the value of field 'surfaceType'.
     * 
     * @return the value of field 'SurfaceType'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.SurfaceType getSurfaceType(
    ) {
        return this._surfaceType;
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
     * Sets the value of field 'minimumQualityInterpretation'.
     * 
     * @param minimumQualityInterpretation the value of field
     * 'minimumQualityInterpretation'.
     */
    public void setMinimumQualityInterpretation(
            final gov.nasa.gsfc.dqss.castor.expression.MinimumQualityInterpretation minimumQualityInterpretation) {
        this._minimumQualityInterpretation = minimumQualityInterpretation;
    }

    /**
     * Sets the value of field 'qualityRecommendation'.
     * 
     * @param qualityRecommendation the value of field
     * 'qualityRecommendation'.
     */
    public void setQualityRecommendation(
            final gov.nasa.gsfc.dqss.castor.expression.QualityRecommendation qualityRecommendation) {
        this._qualityRecommendation = qualityRecommendation;
    }

    /**
     * Sets the value of field 'retrievalCondition'.
     * 
     * @param retrievalCondition the value of field
     * 'retrievalCondition'.
     */
    public void setRetrievalCondition(
            final gov.nasa.gsfc.dqss.castor.expression.RetrievalCondition retrievalCondition) {
        this._retrievalCondition = retrievalCondition;
    }

    /**
     * Sets the value of field 'surfaceType'.
     * 
     * @param surfaceType the value of field 'surfaceType'.
     */
    public void setSurfaceType(
            final gov.nasa.gsfc.dqss.castor.expression.SurfaceType surfaceType) {
        this._surfaceType = surfaceType;
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
     * gov.nasa.gsfc.dqss.castor.expression.HasAttributeChoice
     */
    public static gov.nasa.gsfc.dqss.castor.expression.HasAttributeChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.expression.HasAttributeChoice) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.expression.HasAttributeChoice.class, reader);
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

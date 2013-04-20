/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package gov.nasa.gsfc.dqss.castor.expression;

/**
 * Class FieldValueType.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class FieldValueType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _direction.
     */
    private java.lang.Object _direction;

    /**
     * Field _endian.
     */
    private boolean _endian;

    /**
     * keeps track of state for field: _endian
     */
    private boolean _has_endian;

    /**
     * Field _hasStartBitIndex.
     */
    private long _hasStartBitIndex;

    /**
     * keeps track of state for field: _hasStartBitIndex
     */
    private boolean _has_hasStartBitIndex;

    /**
     * Field _hasStartLayerIndex.
     */
    private long _hasStartLayerIndex;

    /**
     * keeps track of state for field: _hasStartLayerIndex
     */
    private boolean _has_hasStartLayerIndex;

    /**
     * Field _hasStopBitIndex.
     */
    private long _hasStopBitIndex;

    /**
     * keeps track of state for field: _hasStopBitIndex
     */
    private boolean _has_hasStopBitIndex;

    /**
     * Field _hasStopLayerIndex.
     */
    private long _hasStopLayerIndex;

    /**
     * keeps track of state for field: _hasStopLayerIndex
     */
    private boolean _has_hasStopLayerIndex;

    /**
     * Field _samplingdim.
     */
    private java.lang.Object _samplingdim;

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Field _surfaceTypeValue.
     */
    private gov.nasa.gsfc.dqss.castor.expression.SurfaceTypeValue _surfaceTypeValue;

    /**
     * Field _validEnumValue.
     */
    private gov.nasa.gsfc.dqss.castor.expression.ValidEnumValue _validEnumValue;

    /**
     * Field _validQualityLevel.
     */
    private gov.nasa.gsfc.dqss.castor.expression.ValidQualityLevel _validQualityLevel;


      //----------------/
     //- Constructors -/
    //----------------/

    public FieldValueType() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteEndian(
    ) {
        this._has_endian= false;
    }

    /**
     */
    public void deleteHasStartBitIndex(
    ) {
        this._has_hasStartBitIndex= false;
    }

    /**
     */
    public void deleteHasStartLayerIndex(
    ) {
        this._has_hasStartLayerIndex= false;
    }

    /**
     */
    public void deleteHasStopBitIndex(
    ) {
        this._has_hasStopBitIndex= false;
    }

    /**
     */
    public void deleteHasStopLayerIndex(
    ) {
        this._has_hasStopLayerIndex= false;
    }

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     * 
     * @return the value of field 'ChoiceValue'.
     */
    public java.lang.Object getChoiceValue(
    ) {
        return this._choiceValue;
    }

    /**
     * Returns the value of field 'direction'.
     * 
     * @return the value of field 'Direction'.
     */
    public java.lang.Object getDirection(
    ) {
        return this._direction;
    }

    /**
     * Returns the value of field 'endian'.
     * 
     * @return the value of field 'Endian'.
     */
    public boolean getEndian(
    ) {
        return this._endian;
    }

    /**
     * Returns the value of field 'hasStartBitIndex'.
     * 
     * @return the value of field 'HasStartBitIndex'.
     */
    public long getHasStartBitIndex(
    ) {
        return this._hasStartBitIndex;
    }

    /**
     * Returns the value of field 'hasStartLayerIndex'.
     * 
     * @return the value of field 'HasStartLayerIndex'.
     */
    public long getHasStartLayerIndex(
    ) {
        return this._hasStartLayerIndex;
    }

    /**
     * Returns the value of field 'hasStopBitIndex'.
     * 
     * @return the value of field 'HasStopBitIndex'.
     */
    public long getHasStopBitIndex(
    ) {
        return this._hasStopBitIndex;
    }

    /**
     * Returns the value of field 'hasStopLayerIndex'.
     * 
     * @return the value of field 'HasStopLayerIndex'.
     */
    public long getHasStopLayerIndex(
    ) {
        return this._hasStopLayerIndex;
    }

    /**
     * Returns the value of field 'samplingdim'.
     * 
     * @return the value of field 'Samplingdim'.
     */
    public java.lang.Object getSamplingdim(
    ) {
        return this._samplingdim;
    }

    /**
     * Returns the value of field 'surfaceTypeValue'.
     * 
     * @return the value of field 'SurfaceTypeValue'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.SurfaceTypeValue getSurfaceTypeValue(
    ) {
        return this._surfaceTypeValue;
    }

    /**
     * Returns the value of field 'validEnumValue'.
     * 
     * @return the value of field 'ValidEnumValue'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.ValidEnumValue getValidEnumValue(
    ) {
        return this._validEnumValue;
    }

    /**
     * Returns the value of field 'validQualityLevel'.
     * 
     * @return the value of field 'ValidQualityLevel'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.ValidQualityLevel getValidQualityLevel(
    ) {
        return this._validQualityLevel;
    }

    /**
     * Method hasEndian.
     * 
     * @return true if at least one Endian has been added
     */
    public boolean hasEndian(
    ) {
        return this._has_endian;
    }

    /**
     * Method hasHasStartBitIndex.
     * 
     * @return true if at least one HasStartBitIndex has been added
     */
    public boolean hasHasStartBitIndex(
    ) {
        return this._has_hasStartBitIndex;
    }

    /**
     * Method hasHasStartLayerIndex.
     * 
     * @return true if at least one HasStartLayerIndex has been adde
     */
    public boolean hasHasStartLayerIndex(
    ) {
        return this._has_hasStartLayerIndex;
    }

    /**
     * Method hasHasStopBitIndex.
     * 
     * @return true if at least one HasStopBitIndex has been added
     */
    public boolean hasHasStopBitIndex(
    ) {
        return this._has_hasStopBitIndex;
    }

    /**
     * Method hasHasStopLayerIndex.
     * 
     * @return true if at least one HasStopLayerIndex has been added
     */
    public boolean hasHasStopLayerIndex(
    ) {
        return this._has_hasStopLayerIndex;
    }

    /**
     * Returns the value of field 'endian'.
     * 
     * @return the value of field 'Endian'.
     */
    public boolean isEndian(
    ) {
        return this._endian;
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
     * Sets the value of field 'direction'.
     * 
     * @param direction the value of field 'direction'.
     */
    public void setDirection(
            final java.lang.Object direction) {
        this._direction = direction;
    }

    /**
     * Sets the value of field 'endian'.
     * 
     * @param endian the value of field 'endian'.
     */
    public void setEndian(
            final boolean endian) {
        this._endian = endian;
        this._has_endian = true;
    }

    /**
     * Sets the value of field 'hasStartBitIndex'.
     * 
     * @param hasStartBitIndex the value of field 'hasStartBitIndex'
     */
    public void setHasStartBitIndex(
            final long hasStartBitIndex) {
        this._hasStartBitIndex = hasStartBitIndex;
        this._has_hasStartBitIndex = true;
    }

    /**
     * Sets the value of field 'hasStartLayerIndex'.
     * 
     * @param hasStartLayerIndex the value of field
     * 'hasStartLayerIndex'.
     */
    public void setHasStartLayerIndex(
            final long hasStartLayerIndex) {
        this._hasStartLayerIndex = hasStartLayerIndex;
        this._has_hasStartLayerIndex = true;
    }

    /**
     * Sets the value of field 'hasStopBitIndex'.
     * 
     * @param hasStopBitIndex the value of field 'hasStopBitIndex'.
     */
    public void setHasStopBitIndex(
            final long hasStopBitIndex) {
        this._hasStopBitIndex = hasStopBitIndex;
        this._has_hasStopBitIndex = true;
    }

    /**
     * Sets the value of field 'hasStopLayerIndex'.
     * 
     * @param hasStopLayerIndex the value of field
     * 'hasStopLayerIndex'.
     */
    public void setHasStopLayerIndex(
            final long hasStopLayerIndex) {
        this._hasStopLayerIndex = hasStopLayerIndex;
        this._has_hasStopLayerIndex = true;
    }

    /**
     * Sets the value of field 'samplingdim'.
     * 
     * @param samplingdim the value of field 'samplingdim'.
     */
    public void setSamplingdim(
            final java.lang.Object samplingdim) {
        this._samplingdim = samplingdim;
    }

    /**
     * Sets the value of field 'surfaceTypeValue'.
     * 
     * @param surfaceTypeValue the value of field 'surfaceTypeValue'
     */
    public void setSurfaceTypeValue(
            final gov.nasa.gsfc.dqss.castor.expression.SurfaceTypeValue surfaceTypeValue) {
        this._surfaceTypeValue = surfaceTypeValue;
        this._choiceValue = surfaceTypeValue;
    }

    /**
     * Sets the value of field 'validEnumValue'.
     * 
     * @param validEnumValue the value of field 'validEnumValue'.
     */
    public void setValidEnumValue(
            final gov.nasa.gsfc.dqss.castor.expression.ValidEnumValue validEnumValue) {
        this._validEnumValue = validEnumValue;
        this._choiceValue = validEnumValue;
    }

    /**
     * Sets the value of field 'validQualityLevel'.
     * 
     * @param validQualityLevel the value of field
     * 'validQualityLevel'.
     */
    public void setValidQualityLevel(
            final gov.nasa.gsfc.dqss.castor.expression.ValidQualityLevel validQualityLevel) {
        this._validQualityLevel = validQualityLevel;
        this._choiceValue = validQualityLevel;
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
     * gov.nasa.gsfc.dqss.castor.expression.FieldValueType
     */
    public static gov.nasa.gsfc.dqss.castor.expression.FieldValueType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.expression.FieldValueType) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.expression.FieldValueType.class, reader);
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

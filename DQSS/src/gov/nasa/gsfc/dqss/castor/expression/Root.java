/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package gov.nasa.gsfc.dqss.castor.expression;

/**
 * Class Root.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Root implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _qualityViewList.
     */
    private java.util.Vector<gov.nasa.gsfc.dqss.castor.expression.QualityView> _qualityViewList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Root() {
        super();
        this._qualityViewList = new java.util.Vector<gov.nasa.gsfc.dqss.castor.expression.QualityView>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vQualityView
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addQualityView(
            final gov.nasa.gsfc.dqss.castor.expression.QualityView vQualityView)
    throws java.lang.IndexOutOfBoundsException {
        this._qualityViewList.addElement(vQualityView);
    }

    /**
     * 
     * 
     * @param index
     * @param vQualityView
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addQualityView(
            final int index,
            final gov.nasa.gsfc.dqss.castor.expression.QualityView vQualityView)
    throws java.lang.IndexOutOfBoundsException {
        this._qualityViewList.add(index, vQualityView);
    }

    /**
     * Method enumerateQualityView.
     * 
     * @return an Enumeration over all
     * gov.nasa.gsfc.dqss.castor.expression.QualityView elements
     */
    public java.util.Enumeration<? extends gov.nasa.gsfc.dqss.castor.expression.QualityView> enumerateQualityView(
    ) {
        return this._qualityViewList.elements();
    }

    /**
     * Method getQualityView.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * gov.nasa.gsfc.dqss.castor.expression.QualityView at the
     * given index
     */
    public gov.nasa.gsfc.dqss.castor.expression.QualityView getQualityView(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._qualityViewList.size()) {
            throw new IndexOutOfBoundsException("getQualityView: Index value '" + index + "' not in range [0.." + (this._qualityViewList.size() - 1) + "]");
        }

        return (gov.nasa.gsfc.dqss.castor.expression.QualityView) _qualityViewList.get(index);
    }

    /**
     * Method getQualityView.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public gov.nasa.gsfc.dqss.castor.expression.QualityView[] getQualityView(
    ) {
        gov.nasa.gsfc.dqss.castor.expression.QualityView[] array = new gov.nasa.gsfc.dqss.castor.expression.QualityView[0];
        return (gov.nasa.gsfc.dqss.castor.expression.QualityView[]) this._qualityViewList.toArray(array);
    }

    /**
     * Method getQualityViewCount.
     * 
     * @return the size of this collection
     */
    public int getQualityViewCount(
    ) {
        return this._qualityViewList.size();
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
    public void removeAllQualityView(
    ) {
        this._qualityViewList.clear();
    }

    /**
     * Method removeQualityView.
     * 
     * @param vQualityView
     * @return true if the object was removed from the collection.
     */
    public boolean removeQualityView(
            final gov.nasa.gsfc.dqss.castor.expression.QualityView vQualityView) {
        boolean removed = _qualityViewList.remove(vQualityView);
        return removed;
    }

    /**
     * Method removeQualityViewAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public gov.nasa.gsfc.dqss.castor.expression.QualityView removeQualityViewAt(
            final int index) {
        java.lang.Object obj = this._qualityViewList.remove(index);
        return (gov.nasa.gsfc.dqss.castor.expression.QualityView) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vQualityView
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setQualityView(
            final int index,
            final gov.nasa.gsfc.dqss.castor.expression.QualityView vQualityView)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._qualityViewList.size()) {
            throw new IndexOutOfBoundsException("setQualityView: Index value '" + index + "' not in range [0.." + (this._qualityViewList.size() - 1) + "]");
        }

        this._qualityViewList.set(index, vQualityView);
    }

    /**
     * 
     * 
     * @param vQualityViewArray
     */
    public void setQualityView(
            final gov.nasa.gsfc.dqss.castor.expression.QualityView[] vQualityViewArray) {
        //-- copy array
        _qualityViewList.clear();

        for (int i = 0; i < vQualityViewArray.length; i++) {
                this._qualityViewList.add(vQualityViewArray[i]);
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
     * gov.nasa.gsfc.dqss.castor.expression.Root
     */
    public static gov.nasa.gsfc.dqss.castor.expression.Root unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.expression.Root) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.expression.Root.class, reader);
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

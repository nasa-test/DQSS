/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package gov.nasa.gsfc.dqss.castor.expression;

/**
 * Class OR.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class OR implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _items.
     */
    private java.util.Vector<gov.nasa.gsfc.dqss.castor.expression.ORItem> _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public OR() {
        super();
        this._items = new java.util.Vector<gov.nasa.gsfc.dqss.castor.expression.ORItem>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vORItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addORItem(
            final gov.nasa.gsfc.dqss.castor.expression.ORItem vORItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.addElement(vORItem);
    }

    /**
     * 
     * 
     * @param index
     * @param vORItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addORItem(
            final int index,
            final gov.nasa.gsfc.dqss.castor.expression.ORItem vORItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.add(index, vORItem);
    }

    /**
     * Method enumerateORItem.
     * 
     * @return an Enumeration over all
     * gov.nasa.gsfc.dqss.castor.expression.ORItem elements
     */
    public java.util.Enumeration<? extends gov.nasa.gsfc.dqss.castor.expression.ORItem> enumerateORItem(
    ) {
        return this._items.elements();
    }

    /**
     * Method getORItem.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * gov.nasa.gsfc.dqss.castor.expression.ORItem at the given inde
     */
    public gov.nasa.gsfc.dqss.castor.expression.ORItem getORItem(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("getORItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }

        return (gov.nasa.gsfc.dqss.castor.expression.ORItem) _items.get(index);
    }

    /**
     * Method getORItem.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public gov.nasa.gsfc.dqss.castor.expression.ORItem[] getORItem(
    ) {
        gov.nasa.gsfc.dqss.castor.expression.ORItem[] array = new gov.nasa.gsfc.dqss.castor.expression.ORItem[0];
        return (gov.nasa.gsfc.dqss.castor.expression.ORItem[]) this._items.toArray(array);
    }

    /**
     * Method getORItemCount.
     * 
     * @return the size of this collection
     */
    public int getORItemCount(
    ) {
        return this._items.size();
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
    public void removeAllORItem(
    ) {
        this._items.clear();
    }

    /**
     * Method removeORItem.
     * 
     * @param vORItem
     * @return true if the object was removed from the collection.
     */
    public boolean removeORItem(
            final gov.nasa.gsfc.dqss.castor.expression.ORItem vORItem) {
        boolean removed = _items.remove(vORItem);
        return removed;
    }

    /**
     * Method removeORItemAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public gov.nasa.gsfc.dqss.castor.expression.ORItem removeORItemAt(
            final int index) {
        java.lang.Object obj = this._items.remove(index);
        return (gov.nasa.gsfc.dqss.castor.expression.ORItem) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vORItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setORItem(
            final int index,
            final gov.nasa.gsfc.dqss.castor.expression.ORItem vORItem)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("setORItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }

        this._items.set(index, vORItem);
    }

    /**
     * 
     * 
     * @param vORItemArray
     */
    public void setORItem(
            final gov.nasa.gsfc.dqss.castor.expression.ORItem[] vORItemArray) {
        //-- copy array
        _items.clear();

        for (int i = 0; i < vORItemArray.length; i++) {
                this._items.add(vORItemArray[i]);
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
     * gov.nasa.gsfc.dqss.castor.expression.OR
     */
    public static gov.nasa.gsfc.dqss.castor.expression.OR unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (gov.nasa.gsfc.dqss.castor.expression.OR) org.exolab.castor.xml.Unmarshaller.unmarshal(gov.nasa.gsfc.dqss.castor.expression.OR.class, reader);
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

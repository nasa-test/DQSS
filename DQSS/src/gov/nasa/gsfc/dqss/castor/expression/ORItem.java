/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package gov.nasa.gsfc.dqss.castor.expression;

/**
 * Class ORItem.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ORItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Field _OR.
     */
    private gov.nasa.gsfc.dqss.castor.expression.OR _OR;

    /**
     * Field _screeningAssertion.
     */
    private gov.nasa.gsfc.dqss.castor.expression.ScreeningAssertion _screeningAssertion;

    /**
     * Field _AND.
     */
    private gov.nasa.gsfc.dqss.castor.expression.AND _AND;


      //----------------/
     //- Constructors -/
    //----------------/

    public ORItem() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'AND'.
     * 
     * @return the value of field 'AND'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.AND getAND(
    ) {
        return this._AND;
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
     * Returns the value of field 'OR'.
     * 
     * @return the value of field 'OR'.
     */
    public gov.nasa.gsfc.dqss.castor.expression.OR getOR(
    ) {
        return this._OR;
    }

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
     * Sets the value of field 'AND'.
     * 
     * @param AND the value of field 'AND'.
     */
    public void setAND(
            final gov.nasa.gsfc.dqss.castor.expression.AND AND) {
        this._AND = AND;
        this._choiceValue = AND;
    }

    /**
     * Sets the value of field 'OR'.
     * 
     * @param OR the value of field 'OR'.
     */
    public void setOR(
            final gov.nasa.gsfc.dqss.castor.expression.OR OR) {
        this._OR = OR;
        this._choiceValue = OR;
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
        this._choiceValue = screeningAssertion;
    }

}

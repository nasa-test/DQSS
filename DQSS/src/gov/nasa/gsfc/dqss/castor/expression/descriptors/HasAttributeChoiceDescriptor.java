/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package gov.nasa.gsfc.dqss.castor.expression.descriptors;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import gov.nasa.gsfc.dqss.castor.expression.HasAttributeChoice;

/**
 * Class HasAttributeChoiceDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class HasAttributeChoiceDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _elementDefinition.
     */
    private boolean _elementDefinition;

    /**
     * Field _nsPrefix.
     */
    private java.lang.String _nsPrefix;

    /**
     * Field _nsURI.
     */
    private java.lang.String _nsURI;

    /**
     * Field _xmlName.
     */
    private java.lang.String _xmlName;

    /**
     * Field _identity.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor _identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public HasAttributeChoiceDescriptor() {
        super();
        _elementDefinition = false;

        //-- set grouping compositor
        setCompositorAsChoice();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors

        //-- initialize element descriptors

        //-- _minimumQualityInterpretation
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(gov.nasa.gsfc.dqss.castor.expression.MinimumQualityInterpretation.class, "_minimumQualityInterpretation", "MinimumQualityInterpretation", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                HasAttributeChoice target = (HasAttributeChoice) object;
                return target.getMinimumQualityInterpretation();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    HasAttributeChoice target = (HasAttributeChoice) object;
                    target.setMinimumQualityInterpretation( (gov.nasa.gsfc.dqss.castor.expression.MinimumQualityInterpretation) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new gov.nasa.gsfc.dqss.castor.expression.MinimumQualityInterpretation();
            }
        };
        desc.setSchemaType("gov.nasa.gsfc.dqss.castor.expression.MinimumQualityInterpretation");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _minimumQualityInterpretation
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _qualityRecommendation
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(gov.nasa.gsfc.dqss.castor.expression.QualityRecommendation.class, "_qualityRecommendation", "QualityRecommendation", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                HasAttributeChoice target = (HasAttributeChoice) object;
                return target.getQualityRecommendation();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    HasAttributeChoice target = (HasAttributeChoice) object;
                    target.setQualityRecommendation( (gov.nasa.gsfc.dqss.castor.expression.QualityRecommendation) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new gov.nasa.gsfc.dqss.castor.expression.QualityRecommendation();
            }
        };
        desc.setSchemaType("gov.nasa.gsfc.dqss.castor.expression.QualityRecommendation");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _qualityRecommendation
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _retrievalCondition
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(gov.nasa.gsfc.dqss.castor.expression.RetrievalCondition.class, "_retrievalCondition", "RetrievalCondition", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                HasAttributeChoice target = (HasAttributeChoice) object;
                return target.getRetrievalCondition();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    HasAttributeChoice target = (HasAttributeChoice) object;
                    target.setRetrievalCondition( (gov.nasa.gsfc.dqss.castor.expression.RetrievalCondition) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new gov.nasa.gsfc.dqss.castor.expression.RetrievalCondition();
            }
        };
        desc.setSchemaType("gov.nasa.gsfc.dqss.castor.expression.RetrievalCondition");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _retrievalCondition
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _surfaceType
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(gov.nasa.gsfc.dqss.castor.expression.SurfaceType.class, "_surfaceType", "SurfaceType", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                HasAttributeChoice target = (HasAttributeChoice) object;
                return target.getSurfaceType();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    HasAttributeChoice target = (HasAttributeChoice) object;
                    target.setSurfaceType( (gov.nasa.gsfc.dqss.castor.expression.SurfaceType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new gov.nasa.gsfc.dqss.castor.expression.SurfaceType();
            }
        };
        desc.setSchemaType("gov.nasa.gsfc.dqss.castor.expression.SurfaceType");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _surfaceType
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode.
     * 
     * @return the access mode specified for this class.
     */
    @Override()
    public org.exolab.castor.mapping.AccessMode getAccessMode(
    ) {
        return null;
    }

    /**
     * Method getIdentity.
     * 
     * @return the identity field, null if this class has no
     * identity.
     */
    @Override()
    public org.exolab.castor.mapping.FieldDescriptor getIdentity(
    ) {
        return _identity;
    }

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    @Override()
    public java.lang.Class getJavaClass(
    ) {
        return gov.nasa.gsfc.dqss.castor.expression.HasAttributeChoice.class;
    }

    /**
     * Method getNameSpacePrefix.
     * 
     * @return the namespace prefix to use when marshaling as XML.
     */
    @Override()
    public java.lang.String getNameSpacePrefix(
    ) {
        return _nsPrefix;
    }

    /**
     * Method getNameSpaceURI.
     * 
     * @return the namespace URI used when marshaling and
     * unmarshaling as XML.
     */
    @Override()
    public java.lang.String getNameSpaceURI(
    ) {
        return _nsURI;
    }

    /**
     * Method getValidator.
     * 
     * @return a specific validator for the class described by this
     * ClassDescriptor.
     */
    @Override()
    public org.exolab.castor.xml.TypeValidator getValidator(
    ) {
        return this;
    }

    /**
     * Method getXMLName.
     * 
     * @return the XML Name for the Class being described.
     */
    @Override()
    public java.lang.String getXMLName(
    ) {
        return _xmlName;
    }

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that
     * of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition(
    ) {
        return _elementDefinition;
    }

}

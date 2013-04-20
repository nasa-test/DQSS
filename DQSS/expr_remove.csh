#!/bin/csh
# This program is called from ANT script build.xml
# The  purpose of this script is:
# This program will not help build the original AIRS DQSS masking code.
# 1. build.xml compile target is *.java and this removes the pre-expression files that won't compile anymore.
# 2. removes the hdf libraries that are in CVS
# 3. sets up the 32bit 64bit scheme the build.xml is expecting
# 4. builds the castor source files needed based on xml/ontology.xsd

# 1.
rm src/gov/nasa/gsfc/dqss/test/*.java
rm src/gov/nasa/gsfc/dqss/*1D*.java
rm src/gov/nasa/gsfc/dqss/Modis*.java
rm src/gov/nasa/gsfc/dqss/AIR*.java
rm src/gov/nasa/gsfc/dqss/Mask*D*.java
rm src/gov/nasa/gsfc/dqss/Mask*Q*.java
rm src/gov/nasa/gsfc/dqss/SQDS.java
rm -rf src/gov/nasa/gsfc/dqss/ontology_stub/*
rm src/gov/nasa/gsfc/dqss/MODISDataFile.java
rm src/gov/nasa/gsfc/dqss/AirsMaskFactory.java
rm -rf src/gov/nasa/gsfc/dqss/gui/*

# 2.
rm lib/jh*
rm -rf linux

#3. 
mkdir -p lib/hdf4.2.5/32bit
# Location of your install of HDFView:
ln -s $HDFVIEW/lib lib/hdf4.2.5/64bit

#4.
java -cp lib/castor-1.3.1-codegen.jar:lib/castor-1.3.1-xml.jar:lib/castor-1.3.1-xml-schema.jar:lib/castor-1.3.1-core.jar:lib/commons-logging-1.1.jar org.exolab.castor.builder.SourceGeneratorMain -i xml/ontology.xsd -package gov.nasa.gsfc.dqss.castor.expression -f verbose -dest src

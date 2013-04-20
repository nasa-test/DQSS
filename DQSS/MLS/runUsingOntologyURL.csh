#!/bin/csh  

# Purpose: run DQSS Masker/Screener using the Servlet Interface for the Ontology
# or if the Servlet interface is not being used, then the OnBoardInterface.jar
# (the business classes from the Servlet in a jar file instead of war file) 
# This is the software that creates the XML used by runWithOntologyXML.csh


# If using the Servlet make sure that ONTOLOGYURL in dqss.config is set correctly.
# If not, it will default over to the OnBoardInterface.jar

# If the servlet is not used make sure that dqss.config points to appropriate
#ONBOARDPROPERTIES=mod04exp.properties for MOD04 and MOD05 
#ONBOARDPROPERTIES=mlsexp.properties   for MLS H2O and O3
# Runtime Sparql DESCRIBE has not been tested enough to commit at the time of this writing.

# If not using the Servlet make sure that the items in OnBoard.cfg (ONBOARDCONFIG=OnBoard.cfg in dqss.config) 
# point to 
#'$DQSS_QUALITY_DEBUG' = 'false'
# '$DQSS_PAR_ONT' = 'http://mirador.gsfc.nasa.gov/ontologies/dqss-MLS.owl'
# '$DQSS_NAV' = 'http://mirador.gsfc.nasa.gov/ontologies/DQSSNavigation.xml'
# '$DQSS_MODIS_PAR_ONT' = 'http://mirador.gsfc.nasa.gov/ontologies/dqss-MODIS.owl 
# '$DQSS_MLS_PAR_ONT' = 'http://mirador.gsfc.nasa.gov/ontologies/dqss-MLS.owl'
# '$DQSS_MODIS_NAV' = 'http://mirador-ts2.gsfc.nasa.gov/ontologies/modisqueries.xml'
# '$DQSS_MLS_NAV' = 'http://mirador-ts2.gsfc.nasa.gov/ontologies/mlsqueries.xml'
# '$DQSS_SPARQL_CONFIG' = 'DqssSparql.cfg'
# '$DQSS_QUALITY_REGEX' = '[\w\-\.]+'

#  run one of the following sample command lines:
# ./runUsingOntologyURL.csh ../samples/MLS-Aura_L2GP-O3_v03-33-c01_2012d062.he5
 
if ($#argv < 1) then
	echo "USAGE>$0 HDF_FileName"
    echo
    echo "You may need to set  ONBOARDPROPERTIES in ./dqss.config"
    echo "To the servlet for MODIS or MLS"
    echo "You may have to edit this run file to use the java command line with M*D04, M*D05 or MLS (ML2H2O) parameters "
	exit
endif

echo JAVA_HOME is defined as: $JAVA_HOME
if ( $JAVA_HOME == "" ) then
 echo define a JAVA_HOME directory
 exit 1
endif

setenv HDFVIEW ../HDFView/64bit
setenv top ..
set arch = "64bit"
setenv LD_LIBRARY_PATH $HDFVIEW/lib/linux
echo $LD_LIBRARY_PATH
set hrepack = $top/bin/hrepack

set nohrepackflag = 0 
set newname = `basename $1`
set input_xml = $2

setenv dir      $PWD/results 
mkdir -p $dir


setenv masked   $dir/mask_$newname
setenv screened $dir/screened_$newname
setenv unzipped $dir/unzipped_$newname
setenv stage    $dir
setenv config   $PWD

# cp (and while you're at it unzip) the file to staging dir
#setenv input  samples/MOD05_L2.A2007074.0840.005.2007105042354.hdf 
setenv input $1
echo cp   $input  $masked; rm $masked
cp   $input  $masked
if ($status != 0 ) then
	echo "cp failed...Probably could not find: $input"
    exit;
endif
# First get ontology data into file: 
echo get ontology info

# Then mask the data using the ontology file from previous process:
echo masking...

# EXAMPLES:
# See bottom of this script for assistance in editing this list or creating a new one
# It is in the form of java -cp $top/build/dqssMasker.jar gov/nasa/gsfc/dqss/Masker HDF_File DATASET Variables Criteria stagingdir configdir

# FOR M_D04
#java -cp $top/build/dqssMasker.jar:$top/lib/onboardinterface.jar gov/nasa/gsfc/dqss/Masker $masked MOD04_L2 Optical_Depth_Land_And_Ocean,Corrected_Optical_Depth_Land_wav2p1,Corrected_Optical_Depth_Land,Angstrom_Exponent_Land,Angstrom_Exponent_1_Ocean,Angstrom_Exponent_2_Ocean,Effective_Radius_Ocean,Optical_Depth_Small_Average_Ocean,Effective_Optical_Depth_Average_Ocean,Effective_Optical_Depth_Best_Ocean,Optical_Depth_Small_Best_Ocean,Deep_Blue_Single_Scattering_Albedo_Land,Deep_Blue_Angstrom_Exponent_Land,Deep_Blue_Aerosol_Optical_Depth_550_Land,Deep_Blue_Aerosol_Optical_Depth_Land              QualityAssuranceLand_AOTCF47=VERY_GOOD-QualityAssuranceOcean_AvgConfFlag=MARGINAL,QualityAssuranceLand_AOTCF47=VERY_GOOD,QualityAssuranceLand_AOTCF47=VERY_GOOD,QualityAssuranceLand_AOTCF47=VERY_GOOD,QualityAssuranceOcean_AvgConfFlag=MARGINAL,QualityAssuranceOcean_AvgConfFlag=MARGINAL,QualityAssuranceOcean_AvgConfFlag=MARGINAL,QualityAssuranceOcean_AvgConfFlag=MARGINAL,QualityAssuranceOcean_AvgConfFlag=MARGINAL,QualityAssuranceOcean_BestConfFlag=MARGINAL,QualityAssuranceOcean_BestConfFlag=MARGINAL,QualityAssuranceLand_DeepBlueAConfFlag=VERY_GOOD,QualityAssuranceLand_DeepBlueAConfFlag=VERY_GOOD,QualityAssuranceLand_DeepBlueAConfFlag=VERY_GOOD,QualityAssuranceLand_DeepBlueAConfFlag=VERY_GOOD  $stage  $config

# FOR M_D05:
#java   -cp $top/build/dqssMasker.jar:$top/lib/onboardinterface.jar  gov/nasa/gsfc/dqss/Masker $masked MOD05_L2 Water_Vapor_Near_Infrared WaterVaporQAPrecipWaterConfidence=GOOD-WaterVaporCloudiness=ProbablyClear $stage $config

# FOR MLS:
#java -cp $top/build/dqssMasker.jar:$top/lib/onboardinterface.jar  gov/nasa/gsfc/dqss/Masker $masked ML2H2O H2O H2O_Quality=1.3 $stage $config
#java -cp $top/build/dqssMasker.jar:$top/lib/onboardinterface.jar  gov/nasa/gsfc/dqss/Masker $masked ML2O3 O3 O3_Quality=0.9 $stage $config
java  -cp $top/build/dqssMasker.jar:$top/lib/onboardinterface.jar  gov/nasa/gsfc/dqss/Masker $masked ML2O3 O3 O3_Quality=0.43-O3_Convergence=1.8-PressureLimits_O3=0.022:215 $stage $config

if ($status != 0 ) then
	echo "masking failed..."
    exit 1
endif

# then screen it.
date
echo unpacking...;  $hrepack  -i $masked -o $unzipped -t '*:NONE'
if ($status != 0 ) then
set nohrepackflag = 1 
    echo copying, not hrepacking;rm $unzipped; cp $masked $unzipped
endif

echo screening...
java   -cp $top/build/dqssMasker.jar    gov/nasa/gsfc/dqss/Screener $unzipped $stage $config

# then zip it up
date

if ($nohrepackflag == 0) then 
	echo hrepacking; $hrepack  -i $unzipped -o $screened -t '*:GZIP 4'
else 
	echo copying, not hrepacking;rm $screened; cp $unzipped $screened
endif

$HDFVIEW/bin/hdfview.sh $screened


#Both
#   Optical_Depth_Land_And_Ocean
#
#Land ( land qa, land processing flags)
#   Corrected_Optical_Depth_Land_wav2p1
#   Corrected_Optical_Depth_Land
#   Angstrom_Exponent_Land
#
#Ocean (ocean qa)
#   Angstrom_Exponent_1_Ocean
#   Angstrom_Exponent_2_Ocean
#   Effective_Radius_Ocean
#   Optical_Depth_Small_Average_Ocean
#   Effective_Optical_Depth_Average_Ocean
#   Effective_Optical_Depth_Best_Ocean
#   Optical_Depth_Small_Best_Ocean
#
#Deep Blue Land (land qa,, land processing flags) (deep blue has different flag)
#   Deep_Blue_Single_Scattering_Albedo_Land
#   Deep_Blue_Angstrom_Exponent_Land
#   Deep_Blue_Aerosol_Optical_Depth_550_Land
#   Deep_Blue_Aerosol_Optical_Depth_Land

#QualityAssuranceLand_AOTCF47=VERY_GOOD-QualityAssuranceOcean_AvgConfFlag=MARGINAL, 
#QualityAssuranceLand_AOTCF47=VERY_GOOD, 
#QualityAssuranceOcean_AvgConfFlag=MARGINAL, 
#QualityAssuranceLand_DeepBlueAConfFlag=VERY_GOOD, 
#QualityAssuranceOcean_BestConfFlag=MARGINAL  

#QualityAssuranceLand_AOTCF47=VERY_GOOD-QualityAssuranceOcean_AvgConfFlag=MARGINAL, 
#QualityAssuranceLand_AOTCF47=VERY_GOOD, 
#QualityAssuranceLand_AOTCF47=VERY_GOOD, 
#QualityAssuranceLand_AOTCF47=VERY_GOOD, 
#QualityAssuranceOcean_AvgConfFlag=MARGINAL, 
#QualityAssuranceOcean_AvgConfFlag=MARGINAL, 
#QualityAssuranceOcean_AvgConfFlag=MARGINAL, 
#QualityAssuranceOcean_AvgConfFlag=MARGINAL, 
#QualityAssuranceOcean_AvgConfFlag=MARGINAL, 
#QualityAssuranceOcean_BestConfFlag=MARGINAL  
#QualityAssuranceOcean_BestConfFlag=MARGINAL,  
#QualityAssuranceLand_DeepBlueAConfFlag=VERY_GOOD, 
#QualityAssuranceLand_DeepBlueAConfFlag=VERY_GOOD, 
#QualityAssuranceLand_DeepBlueAConfFlag=VERY_GOOD, 
#QualityAssuranceLand_DeepBlueAConfFlag=VERY_GOOD, 

#Optical_Depth_Land_And_Ocean,
#Corrected_Optical_Depth_Land_wav2p1,
#Corrected_Optical_Depth_Land,
#Angstrom_Exponent_Land,
#Angstrom_Exponent_1_Ocean,
#Angstrom_Exponent_2_Ocean,
#Effective_Radius_Ocean,
#Optical_Depth_Small_Average_Ocean,
#Effective_Optical_Depth_Average_Ocean,
#Effective_Optical_Depth_Best_Ocean,
#Optical_Depth_Small_Best_Ocean,
#Deep_Blue_Single_Scattering_Albedo_Land,
#Deep_Blue_Angstrom_Exponent_Land,
#Deep_Blue_Aerosol_Optical_Depth_550_Land,
#Deep_Blue_Aerosol_Optical_Depth_Land

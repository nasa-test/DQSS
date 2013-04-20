#!/bin/csh -x

# This script runs DQSS for AIRS Level 2 products:
# Examples:
#./runUsingOntologyURL.csh AIRX2RET totO3Std,O3VMRStd,TAirMWOnlyStd,clrolr,TSurfStd Best,Best,Best,Best,Good ../samples/AIRS.2011.12.31.240.L2.RetStd.v5.2.2.0.G12001131525.hdf
#./runUsingOntologyURL.csh AIRX2RET TSurfAir,totH2OMWOnlyStd,TAirStd,TSurfStd,totH2OStd,H2OMMRStd,H2OMMRSat,H2OMMRSat_liquid,totO3Std,O3VMRStd,CO_VMR_eff,TAirMWOnlyStd,CldFrcStd,PCldTopStd,olr,clrolr,CO_total_column,CH4_VMR_eff,CH4_total_column,emisIRStd Good,Good,Good,Good,Good,Good,Good,Good,Best,Best,Good,Best,Good,Good,Good,Best,Best,Good,Good,Good ../samples/AIRS.2011.12.31.240.L2.RetStd.v5.2.2.0.G12001131525.hdf

# dqss.config:
# ONTOLOGYURL points to the servlet interface to the ontology. If the servlet is not available it will use classes in dqssMasker.csh
# ONTOLOGY points to the actual ontology. This needs to be set correctly if the servlet at ONTOLOGYURL is not working

if ($#argv < 3 ) then
   echo USAGE $0 dataset datavariable criteria hdf4File
   echo USAGE $0 $1 $2 $3 $4 
   exit
endif

echo JAVA_HOME is defined as: $JAVA_HOME
if ( $JAVA_HOME == "" ) then
 echo define a JAVA_HOME directory
 exit 1
endif

setenv HDFVIEW ../HDFView/64bit
setenv jhdf $HDFVIEW/lib
setenv jena lib
setenv castor lib
setenv dir . 
setenv ont $dir/lib
setenv dqss $dir/lib
setenv logging $dir/lib

setenv LD_LIBRARY_PATH $jhdf/linux


set dataset      = $1
set dataVariable = $2
set criteria     = $3
set newname = `basename $4`

set dirpart = `/bin/date +"%m%d%y%H%M%S"`$$
set stagingdir = results
/bin/mkdir -p $stagingdir
set masked = $stagingdir/$newname
set unzipped = $stagingdir/tmp_$newname
set screened = $stagingdir/Screened_$newname
set toStdOut = 1
set config = $dir

if (-e $4 ) then
        /bin/cp $4 $masked
else
        echo $4 does not exist
        exit 1
endif

#cd $stagingdir

java   -cp lib/dqssMasker.jar:$jhdf/jhdf.jar:$jhdf/jhdfobj.jar:$jhdf/jhdf4obj.jar:$jena/jena-2.6.3.jar:$jena/arq-2.8.4.jar:$dqss/commons-logging-1.1.jar:$jena/xercesImpl-2.7.1.jar:$jena/iri-0.8.jar:$jena/icu4j-3.4.4.jar:$dqss/dom4j-1.4.jar:$ont/ontologyInterface.jar:$castor/castor-1.3.1-core.jar:$castor/castor-1.3.1-codegen.jar:$castor/castor-1.3.1-xml.jar:$castor/castor-1.3.1-xml-schema.jar:$castor/castor-1.3.1-jdo.jar:$dqss/log4j-1.2.12.jar:$ont/jcs-1.3.3.5-RC.jar:$ont/G4CacheManager.jar:$ont/concurrent.jar:$jena/slf4j-api-1.5.8.jar:$jena/slf4j-log4j12-1.5.8.jar  gov/nasa/gsfc/dqss/Masker $masked $dataset $dataVariable $criteria $stagingdir $config > /dev/null

bin/hrepack  -i $masked -o $unzipped -t '*:NONE'


java   -cp lib/dqssMasker.jar:$jhdf/jhdf.jar:$jhdf/jhdfobj.jar:$jhdf/jhdf4obj.jar:$logging/log4j-1.2.12.jar   gov/nasa/gsfc/dqss/Screener $unzipped $stagingdir $config  > /dev/null

bin/hrepack  -i $unzipped -o $screened -t '*:GZIP 1' 

$HDFVIEW/bin/hdfview.sh $screened



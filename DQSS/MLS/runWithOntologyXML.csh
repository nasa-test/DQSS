#!/bin/csh  

# Purpose: run DQSS Masker/Screener using a pre-supplied ontology file.
# This is a script to run right out of the can.
# .1 run ant with no arguments in this directory. It should build ./build/dqssMasker.jar using
# files under src/ and lib/
# 2. Sample command lines:
# runWithOntologyXML.csh ../samples/MLS-Aura_L2GP-H2O_v03-33-c01_2011d285.he5 ../testing/xml/MLS/ML2H2O.xml
 
if ($#argv < 2) then
	echo "USAGE>$0 HDF_FileName full_path_of_input_xml"
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

set dump  = "hdp dumpsds -c"
#set dump  = "h5dump -A "
set hrepack = $top/bin/hrepack

set nohrepackflag = 0 
set newname = `basename $1`
set full_path_of_input_xml = $2

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
if (-e $masked) then
  rm $masked
endif
echo copying   $input  $masked;
cp   $input  $masked
if ($status != 0 ) then
    echo "cp failed($status)...Probably could not find: $input"
    exit 1;
endif

# First get ontology data into file: 
echo get ontology info

# Then mask the data using the ontology file from previous process:
echo masking...
date
$JAVA_HOME/bin/java   -cp $top/build/dqssMasker.jar:$top/lib/onboardinterface.jar gov/nasa/gsfc/dqss/Masker $masked $stage $config $full_path_of_input_xml
if ($status != 0 ) then
	echo "masking failed"
    exit 1
endif

# then screen it.
date
echo unpacking...;  $hrepack  -i $masked -o $unzipped -t '*:NONE'
if ($status != 0 ) then
set nohrepackflag = 1 
    echo copying, not hrepacking; cp $masked $unzipped
endif
if ($status != 0 ) then
	echo "post masking cp failed"
    exit 1
endif

echo screening...
$JAVA_HOME/bin/java   -cp $top/build/dqssMasker.jar    gov/nasa/gsfc/dqss/Screener $unzipped $stage $config

if ($status != 0 ) then
	echo "screening failed"
    exit 1
endif
# then zip it up
date

if ($nohrepackflag == 0) then 
	echo hrepacking; $hrepack  -i $unzipped -o $screened -t '*:GZIP 4'
else 
	echo copying, not hrepacking; cp $unzipped $screened
endif
if ($status != 0 ) then
	echo "post screening cp failed (only a warning)"
    exit 1
endif

$HDFVIEW/bin/hdfview.sh $screened



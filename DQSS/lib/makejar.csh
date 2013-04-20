#!/bin/csh 
# Start in lib dir:

rm dqssMasker.jar
# Then adding the external jars with these foreach loops
foreach f ( `egrep "lib|castor" README | grep jar`)
  cp  $f .
  setenv bn `basename $f`
  jar -xvf $bn 
  rm $bn
end
foreach f ( `ls -1 *.jar | grep -v dqssMasker` )
  jar -xvf  $f
end
  jar -cvf dqssMasker.jar  EDU arq com jena ncsa org gov 
  rm -rf META-INF EDU arq com jena ncsa org META-INF gov etc vocabularies

cd ../src

foreach f (`grep java ../lib/README | cut -d"." -f1`)
if (-e ../lib/dqssMasker.jar) then
  jar -uvf ../lib/dqssMasker.jar $f.java
  jar -uvf ../lib/dqssMasker.jar $f.class
else 
  echo creating ../lib/dqssMasker.jar
  jar -cvf ../lib/dqssMasker.jar $f.java
  jar -uvf ../lib/dqssMasker.jar $f.class
endif
end
# Descriptor class names aren't kosher:
jar -uvf ../lib/dqssMasker.jar gov/nasa/gsfc/dqss/castor/descriptors/*.class

cd ../lib



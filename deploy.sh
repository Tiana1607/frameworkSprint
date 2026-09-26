#!/bin/bash
SRC="src/main/java"
BUILD="build/classes"
JAR_OUT="jar/framework.jar"
LIB="lib/servlet-api.jar"

TOMCAT_WEBAPPS="/home/rovatians/Téléchargements/logiciel/tomcat/apache-tomcat-10.0.16/webapps"

mkdir -p $BUILD jar

echo "==== [1/4] COMPILATION FRAMEWORK ===="
javac -cp $LIB -d $BUILD $(find $SRC -name "*.java")
if [ $? -ne 0 ]; then echo "Erreur compilation framework"; exit 1; fi

echo "==== [2/4] CREATION JAR ===="
jar cf $JAR_OUT -C $BUILD .
echo "framework.jar créé"

echo "==== [3/4] INSTALL FRAMEWORK DANS MAVEN LOCAL ===="
mvn install:install-file \
    -Dfile=$JAR_OUT \
    -DgroupId=mg.itu \
    -DartifactId=framework \
    -Dversion=1.0 \
    -Dpackaging=jar -q
if [ $? -ne 0 ]; then echo "Erreur install Maven"; exit 1; fi
echo "framework.jar installé dans Maven local"

echo "==== [4/4] BUILD MAVEN + DEPLOIEMENT TOMCAT ===="
cd test
mvn clean package -q
if [ $? -ne 0 ]; then echo "Erreur build Maven"; exit 1; fi

rm -rf "$TOMCAT_WEBAPPS/monapp"
rm -f "$TOMCAT_WEBAPPS/monapp.war"
cp target/monapp.war $TOMCAT_WEBAPPS/
echo "Déployé dans $TOMCAT_WEBAPPS"

cd ..
echo ""
echo "==== DONE ===="
echo "→ http://localhost:8081/monapp/app/home"
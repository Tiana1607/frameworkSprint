#!/bin/bash
SRC="src/main/java"
BUILD="build/classes"
JAR_OUT="jar/framework.jar"
LIB="lib/servlet-api.jar"

TEST_SRC="test/src/main/java"
TEST_BUILD="test/webapp/WEB-INF/classes"   # ← Tomcat lit les .class ici
TEST_WEBAPP="test/webapp"
WAR_OUT="test/build/monapp.war"

TOMCAT_WEBAPPS="/home/rovatians/Téléchargements/logiciel/tomcat/apache-tomcat-10.0.16/webapps"

mkdir -p $BUILD jar $TEST_WEBAPP/WEB-INF/lib $TEST_BUILD test/build

echo "==== [1/5] COMPILATION FRAMEWORK ===="
javac -cp $LIB -d $BUILD $(find $SRC -name "*.java")
if [ $? -ne 0 ]; then echo "Erreur compilation framework"; exit 1; fi

echo "==== [2/5] CREATION JAR ===="
jar cf $JAR_OUT -C $BUILD .
cp $JAR_OUT $TEST_WEBAPP/WEB-INF/lib/framework.jar
echo "framework.jar créé"

echo "==== [3/5] COMPILATION CONTROLLERS DE TEST ===="
javac -cp "$LIB:$JAR_OUT" -d $TEST_BUILD $(find $TEST_SRC -name "*.java")
if [ $? -ne 0 ]; then echo "Erreur compilation test"; exit 1; fi

echo "==== [4/5] CREATION WAR ===="
jar cf $WAR_OUT -C $TEST_WEBAPP .
echo "monapp.war créé"

echo "==== [5/5] DEPLOIEMENT TOMCAT ===="
if [ -d "$TOMCAT_WEBAPPS" ]; then
    cp $WAR_OUT $TOMCAT_WEBAPPS/
    echo "Déployé dans $TOMCAT_WEBAPPS"
else
    echo "Tomcat introuvable à : $TOMCAT_WEBAPPS"
fi

echo "→ http://localhost:8081/monapp/"
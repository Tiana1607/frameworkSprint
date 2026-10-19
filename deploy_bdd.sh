#!/bin/bash
SRC="src/main/java"
BUILD="build/classes"
JAR_OUT="jar/framework.jar"
LIB="lib/servlet-api.jar"

TEST_SRC="test/src/main/java"
TEST_WEBAPP="test/webapp"
TEST_BUILD="$TEST_WEBAPP/WEB-INF/classes"
WAR_OUT="test/build/monapp.war"

TOMCAT_WEBAPPS="/home/rovatians/Téléchargements/logiciel/tomcat/apache-tomcat-10.0.16/webapps"

mkdir -p $BUILD jar $TEST_BUILD $TEST_WEBAPP/WEB-INF/lib test/build

echo "==== [1/4] COMPILATION FRAMEWORK ===="
CLASSPATH="$LIB:lib/json-20240303.jar:$(find lib -name 'spring-*.jar' | tr '\n' ':')"
javac -cp "$CLASSPATH" -parameters -d $BUILD $(find $SRC -name "*.java")
if [ $? -ne 0 ]; then echo "Erreur compilation framework"; exit 1; fi

echo "==== [2/4] CREATION JAR ===="
jar cf $JAR_OUT -C $BUILD .
cp $JAR_OUT $TEST_WEBAPP/WEB-INF/lib/framework.jar
echo "framework.jar créé"

echo "==== [3/4] COMPILATION TEST ===="
TEST_CP="$LIB:$JAR_OUT:$(find $TEST_WEBAPP/WEB-INF/lib -name '*.jar' | tr '\n' ':')"
javac -cp "$TEST_CP" -parameters -d $TEST_BUILD $(find $TEST_SRC -name "*.java")
if [ $? -ne 0 ]; then echo "Erreur compilation test"; exit 1; fi
cp -r test/src/main/resources/. "$TEST_BUILD/"

echo "==== [4/4] CREATION WAR + DEPLOIEMENT TOMCAT ===="
jar cf $WAR_OUT -C $TEST_WEBAPP .
echo "monapp.war créé"

rm -rf "$TOMCAT_WEBAPPS/monapp"
rm -f "$TOMCAT_WEBAPPS/monapp.war"
cp $WAR_OUT $TOMCAT_WEBAPPS/
echo "Déployé dans $TOMCAT_WEBAPPS"

echo ""
echo "==== DONE ===="
echo "→ http://localhost:8081/monapp/app/home"
#!/bin/bash

# ─── CONFIG ───────────────────────────────────────────
SRC="src/main/java"
BUILD="build/classes"
JAR_OUT="jar/framework.jar"
LIB="lib/servlet-api.jar"

TEST_WEBAPP="test/webapp"
TEST_BUILD="test/build"
WAR_NAME="monapp.war"
WAR_OUT="$TEST_BUILD/$WAR_NAME"

# Adapte ce chemin à ton installation Tomcat
TOMCAT_WEBAPPS="/home/rovatians/Téléchargements/logiciel/tomcat/apache-tomcat-10.0.16/webapps"
# ─────────────────────────────────────────────────────

# Créer les dossiers si absents
mkdir -p $BUILD
mkdir -p jar
mkdir -p $TEST_WEBAPP/WEB-INF/lib
mkdir -p $TEST_BUILD

# 1. Compiler le framework
echo "==== [1/4] COMPILATION FRAMEWORK ===="
mkdir -p $BUILD
javac -cp $LIB -d $BUILD $(find $SRC -name "*.java")
if [ $? -ne 0 ]; then echo "Erreur compilation framework"; exit 1; fi

# 2. Créer le JAR du framework
echo "==== [2/4] CREATION JAR ===="
mkdir -p jar
jar cf $JAR_OUT -C $BUILD .
cp $JAR_OUT $TEST_WEBAPP/WEB-INF/lib/framework.jar
echo "framework.jar créé"

# 3. Créer le WAR de l'app de test
echo "==== [3/4] CREATION WAR ===="
mkdir -p $TEST_BUILD
jar cf $WAR_OUT -C $TEST_WEBAPP .
echo "$WAR_NAME créé"

# 4. Déployer dans Tomcat
echo "==== [4/4] DEPLOIEMENT TOMCAT ===="
if [ -d "$TOMCAT_WEBAPPS" ]; then
    cp $WAR_OUT $TOMCAT_WEBAPPS/
    echo " Déployé dans $TOMCAT_WEBAPPS"
else
    echo " Tomcat introuvable à : $TOMCAT_WEBAPPS"
    echo " Copie manuelle : cp $WAR_OUT /home/rovatians/Téléchargements/logiciel/tomcat/apache-tomcat-10.0.16/webapps/"
fi

echo ""
echo "==== DONE ===="
echo "→ http://localhost:8081/monapp/"
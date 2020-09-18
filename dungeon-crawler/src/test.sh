
# #######################################
# Clean up previous tests               #
# #######################################

if test -d "../bin"
then
    rm -fr ../bin
fi
if test -d "../testbin"
then
    rm -fr ../testbin
fi

# #######################################
# Running tests                         #
# #######################################
# Requirements to run scripts below:
#   - jacoco : https://www.eclemma.org/jacoco/
#   - junit jupiter: https://mvnrepository.com/artifact/org.junit.platform/junit-platform-launcher/1.7.0-M1
#   - hamcrest :  https://mvnrepository.com/artifact/org.hamcrest/hamcrest-core/1.3
echo "Running tests & coverage report"
javac -cp ".:../lib/*" -d "./bin" unsw/dungeon/*.java
javac -cp ".:../lib/junit-platform-console-standalone-1.7.0-M1.jar:../lib/*" -d "./testbin" test/*.java
java -jar ../lib/junit-platform-console-standalone-1.7.0-M1.jar --reports-dir "./report" -cp "../lib/*:../lib/javafx.base.jar:./testbin" --class-path "testbin:../lib/*:." --scan-class-path
# java -javaagent:../lib/jacocoagent.jar -cp ../lib/junit-platform-console-standalone-1.7.0-M1.jar:test:bin:testbin:testbin/test:../lib/*  --module-path "../lib/:../testbin:bin:unsw/dungeon" --add-modules=javafx.controls,javafx.fxml org.junit.runner.JUnitCore test.TestCollectable
# java -jar ../lib/junit-platform-console-standalone-1.7.0-M1.jar --include-engine=junit-jupiter --include-tag=@{tag} --include-classname=.* --scan-classpath=@{scan.classpath}

# -----------------------
# Using junit-4.13.jar:
sh tojunit4.sh
javac -cp ".:../lib/junit-4.13.jar:../lib/*" -d "./test-junit4-bin" test-junit4/*.java
ls test-junit4/*.java > l
while IFS= read -r line
do
    name=`basename "$line" | sed "s/.java//g"`
    java -javaagent:../lib/jacoco-0.8.4/lib/jacocoagent.jar -cp ../lib/junit-4.13.jar:hamcrest-core-1.3.jar:bin:test-junit4-bin:test-junit4-bin/test-junit4:../lib/*  --module-path "../lib/:../test-junit4-bin:unsw/dungeon" --add-modules=javafx.controls,javafx.fxml org.junit.runner.JUnitCore test."$name"
done < ./l
rm ./l
java -jar ../lib/jacoco-0.8.4/lib/jacococli.jar report jacoco.exec --classfiles test-junit4-bin --sourcefiles "." --html report
echo "See coverage report at `pwd`/report/index.html"
cd ../

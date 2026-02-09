#build it with Java 17
./gradlew -Dorg.gradle.java.home=/opt/homebrew/opt/openjdk@17 clean build -x test
#run it with Java 17
./gradlew -Dorg.gradle.java.home=/opt/homebrew/opt/openjdk@17 bootRun

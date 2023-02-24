# Fetch Rewards Coding Exercise
Take home assessment as part of Fetch Rewards Summer 2023 internship
Exercise at: https://fetch-hiring.s3.us-east-1.amazonaws.com/points-intern.pdf

## Installation Instructions:
The program is written in **Java** language with **Maven** as the build tool. You will need to setup Java and Maven as mentioned below in order to compile and run the program. 
### Prerequisites
Before you install Maven, you should ensure Java Development Kit (JDK) is installed in your computer. Maven requires a JDK to be installed on your machine to function. You can download the latest JDK from the official Oracle website at https://www.oracle.com/java/technologies/javase-downloads.html. Maven 3.9+ requires JDK 8 or above to execute.

Once you have ensured that you have a JDK installed, and have set the **JAVA_HOME** environment variable pointing to your JDK installation, you can proceed with the steps to install Maven.

### Install Maven
- You can download the latest version of Maven from here: https://maven.apache.org/download.cgi.
- Steps to install Maven can be found here: https://maven.apache.org/install.html

After following these steps verify if Maven has been installed properly by running the below command in a new shell:
```
mvn -v
```
The result should look similar to:
```
Apache Maven 3.9.0 (9b58d2bad23a66be161c4664ef21ce219c2c8584)
Maven home: /opt/apache-maven-3.9.0
Java version: 1.8.0_45, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "10.8.5", arch: "x86_64", family: "mac"
```

## Command Instructions
Clone this repository and 'cd' into the working directory - **FetchRewardsTest**

Copy the **transactions.csv** file to the working directory.

Run the below command to compile and execute the program:
```
mvn compile exec:java -Dexec.mainClass="org.fetch.FetchTest" -Dexec.arguments="5000,transactions.csv"
```
We provide the 2 arguments using -Dexec.arguments flag:
- pointsToSpend (5000 in the above command)
- CSV file name. (transactions.csv in the above command)
- 
The output JSON should appear above the ***BUILD SUCCESS*** message as shown here:
```
{
    "UNILEVER": 0,
    "MILLER COORS": 5300,
    "DANNON": 1000
}
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.742 s
[INFO] Finished at: 2023-02-23T05:42:21-06:00
[INFO] ------------------------------------------------------------------------
```

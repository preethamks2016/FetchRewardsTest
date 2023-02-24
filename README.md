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
- pointsToSpend ("5000" in the above command)
- CSV file name. ("transactions.csv" in the above command)

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

## Approach
Using the example provided in the problem statement 
We first make a list of transactions
```
List {
DANNON, 300, 1604138400000
UNILEVER, 200, 1604142000000
DANNON, -200, 1604156400000
MILLER COORS, 10000, 1604239200000
DANNON, 1000, 1604325600000
}
```
We sort this list and convert this into a HashMap with Payer as key. Refer class **TransactionProcessor**
```
// Map of positive transactions
UNILEVER: 200, 
MILLER COORS: 10000, 
DANNON: 300, 1000
// Map of negative values
UNILEVER: 0, 
MILLER COORS: 0, 
DANNON: -200
```
We then deduct the negative points from the positive points starting from oldest positive transaction first. In this case for Dannon we subtract 200 from his oldest points of 300 resulting in 100.
```
 DANNON: {300, 1000} -> DANNON: {100, 1000}
```
A final sorted list of transaction is generated
```
DANNON, 100, 1604138400000
UNILEVER, 200, 1604142000000
MILLER COORS, 10000, 1604239200000
DANNON, 1000, 1604325600000
```
Refer class: **AccountingService**.
Points are spent starting from oldest timestamp. For 5000 spend points we use 100 points from Dannon, 200 from Unilever and rest from Miller resulting in final balances as shown below:
```
{
    "UNILEVER": 0,
    "MILLER COORS": 5300,
    "DANNON": 1000
}
```

### Edge Scenario
Consider this example with points to spend as 500
```agsl
DANNON, 200, 1604138400000
MILLER COORS, 10000, 1604152800000
DANNON, -300, 1604156400000
DANNON, 1000, 1604325600000
```
In a naive implementation Dannon's 200 points would be used first and the remaining 300 out of 500 points would be spent using Miller's points. 
But this would be wrong since Dannon has negative points later on. In our approach since we process the payer wise transaction map we end up with:
```agsl
 DANNON: {200, 1000} -> DANNON: {900}
```
Processed sorted transaction list would then be:
```agsl
MILLER COORS, 10000, 1604152800000
DANNON, 900, 1604325600000
```
Output:
```agsl
{
    "MILLER COORS": 9500,
    "DANNON": 900
}
```

Further details of the solution, choice of tools are summarised in the **summary.txt** file
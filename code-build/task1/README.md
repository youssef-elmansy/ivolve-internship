# Task 01: Building and Testing a Java Application with Gradle

This task demonstrates how to *install a compatible Gradle version, run **unit tests*, build a Java application, and execute the generated artifact.

The instructions are written to be executed directly from the *terminal (Bash)*.

---

## Objective

* Install *Gradle 8.x* (compatible with Java 17)
* Run unit tests using Gradle
* Build the application
* Run the generated JAR file

---

## Prerequisites

* Linux / WSL environment
* Java 17 installed

Verify Java installation:

bash
java -version


Expected output should indicate *Java 17*.

---

## Step 1: Install Gradle (Recommended Method)

The default Gradle package available via apt is outdated and incompatible with Java 17.
This task uses the *official Gradle binary distribution*.

### 1.1 Remove old Gradle version (if exists)

bash
sudo apt remove gradle -y


---

### 1.2 Download Gradle 8.5

bash
wget https://services.gradle.org/distributions/gradle-8.5-bin.zip


---

### 1.3 Extract Gradle

bash
sudo unzip gradle-8.5-bin.zip -d /opt


---

### 1.4 Add Gradle to PATH

bash
echo 'export PATH=$PATH:/opt/gradle-8.5/bin' >> ~/.bashrc
source ~/.bashrc


---

### 1.5 Verify Gradle Installation

bash
gradle -v


Expected output:

text
Gradle 8.5
JVM: 17


---

## Step 2: Clone the Application Source Code

bash
git clone https://github.com/Ibrahim-Adel15/build1.git
cd build1


---

## Step 3: Run Unit Tests

bash
gradle test


This step validates the application logic using JUnit tests.

---

## Step 4: Build the Application

bash
gradle build


Generated artifact:

text
build/libs/ivolve-app.jar


---

## Step 5: Run the Application

bash
java -jar build/libs/ivolve-app.jar


Expected output:

text
Hello iVolve Trainee


---

> ### Screenshot Placeholder (Lab1 Execution Result)
![Lab 1 Result](lab1.png)

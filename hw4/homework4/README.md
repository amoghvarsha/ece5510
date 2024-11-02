# CS/ECE 5510 Homework 4 Template

## Contact

Contact the course TA for any questions. You will be notified when the contents of this repository changes.

## Overview

Use this template to submit your solution to Part II of Homework 4. 
Your assignment may not be graded otherwise. 
**Submit your solution as a zip file on Canvas.** 

Note that this template may undergo modification throughout the homework. 
The template consists of two packages: `edu.vt.ece.spin` and `edu.vt.ece.hw4`. 
The former consists of various implementations of Spin Locks provided in the textbook, 
while the latter is the template for this homework. 
You may use ideas and code snippets from the former but you should only modify the latter package.

## Downloading this template

If you know `git`, you can clone this repository and work on it.

Otherwise, click the `Clone or download` button on the repository page and then click `Download zip` to download a zip file.   

## Submission

You must zip this folder and submit to Canvas

## Building and Running the template

The below instructions should be run from the top-level directory.

### Running Unit Test

Run the following command to run the unit test:

Linux: `./gradlew test`

Windows: `./gradlew.bat test`

There is no requirement to write or satisfy unit tests for this homework. 
However, as you may have learnt in Homework 2, they are useful for checking the correctness of your implementations. 

### Building the project and producing binaries:

Linux: ` ./gradlew build -x test`

Windows: `./gradlew.bat build -x test`

This will produce the binaries in the build folder.

### Running Benchmark main programs:

__Benchmark__:
`java -cp build/libs/homework4.jar edu.vt.ece.hw4.Benchmark <YOUR_ARGS>`

Replace `<YOUR_ARGS>` with the arguments you would like to pass to the respective main programs. 
Note that the TA will only run the above command to verify if the submission works. 
Furthermore, the TA will also inspect your respective implementations.

## Intellij

This gradle project can be imported into Intellj by going to `File -> Open` and choosing this directory. As soon as you open the project, a pop-up dialog may appear in the bottom right of the screen asking to import the project with Gradle. Accept it.

For more instructions, go to https://www.jetbrains.com/help/idea/gradle.html#gradle_import_project_start

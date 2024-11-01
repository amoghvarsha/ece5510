# ECE/CS-5510 Homework 3 Part II Reference code (Fall 2024)

## Contact

Please use the Homework3 discussion forum on Canvas for any questions.

## Overview

Please refer to the provided skeletal code and implement your solution to Part II of Homework 3. 
Please submit the code along with a screenshot of the linearizability sequence visualization as part of the submission for Homework 3.
**Submit your solution as a zip file on Canvas.** 

You are free to modify any part of the provided source code.

## Dependencies

Install [go](https://go.dev/) using your distribution's package manager or refer to the official [documentation](https://go.dev/doc/install) to install the latest stable compiler version.

## Steps

- Install the dependencies.
- Clone the [porcupine](https://github.com/anishathalye/porcupine) lineraizability checker repository.
- Create a new directory within the cloned porcupine repository.
- Copy the provided skeletal code into the new directory and modify it as per the problem specification.
- Refer to the [usage section](https://github.com/anishathalye/porcupine?tab=readme-ov-file#usage) of the documentation to implement a model (sequential specification of the event) and to construct histories. 
- Compile and run the program to check the histories for linearizability with `go run hw3.go`.
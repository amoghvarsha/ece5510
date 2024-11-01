package main

import (
    "fmt"
    "github.com/anishathalye/porcupine"
)

type registerInput struct {
	op    bool // false = put, true = get
	value int
}

// a sequential specification of a register
var registerModel = porcupine.Model{
	Init: func() interface{} {
		return 0
	},
	// step function: takes a state, input, and output, and returns whether it
	// was a legal operation, along with a new state
	Step: func(state, input, output interface{}) (bool, interface{}) {
		regInput := input.(registerInput)
		if regInput.op == false {
			return true, regInput.value // always ok to execute a put
		} else {
			readCorrectValue := output == state
			return readCorrectValue, state // state is unchanged
		}
	},
	DescribeOperation: func(input, output interface{}) string {
		inp := input.(registerInput)
		switch inp.op {
		case true:
			return fmt.Sprintf("get() -> '%d'", output.(int))
		case false:
			return fmt.Sprintf("put('%d')", inp.value)
		}
		return "<invalid>" // unreachable
	},
}

func HistoryHBar() {

	fmt.Printf("\nChecking if the shared history (H) in HW3 P5 is linearizable...\n")
	
	A := 0
	B := 1
	C := 2

	eventsRegR := []porcupine.Event{
		{B, porcupine.CallEvent, registerInput{false, 1}, 1}, // r:write(1)
		{A, porcupine.CallEvent, registerInput{ true, 0}, 2}, // r:read()
		{C, porcupine.CallEvent, registerInput{false, 2}, 3}, // r:write(2)
		{A, porcupine.ReturnEvent, 1, 2}, 					  // r:1
		{C, porcupine.ReturnEvent, 0, 3}, 					  // r:void
		{B, porcupine.ReturnEvent, 0, 1}, 					  // r:void
		{B, porcupine.CallEvent, registerInput{ true, 0}, 4}, // r:read()
		{B, porcupine.ReturnEvent, 1, 4},				      // r:1
		{C, porcupine.CallEvent, registerInput{ true, 0}, 6}, // r:read()
		{C, porcupine.ReturnEvent, 1, 6},				      // r:1	
	}

	eventsRegQ := []porcupine.Event{
		{A, porcupine.CallEvent, registerInput{false, 3}, 5}, // q:write(3)
		{A, porcupine.ReturnEvent, 0, 5},                     // q:void
	}

	resultRegR, infoRegR := porcupine.CheckEventsVerbose(registerModel, eventsRegR, 0)
	resultRegQ, infoRegQ := porcupine.CheckEventsVerbose(registerModel, eventsRegQ, 0)

	porcupine.VisualizePath(registerModel, infoRegR, "/home/mogi/Github-Repos/porcupine/Homework3/hw3_p5_regR.html")
	porcupine.VisualizePath(registerModel, infoRegQ, "/home/mogi/Github-Repos/porcupine/Homework3/hw3_p5_regQ.html")
	
	if resultRegR == porcupine.Ok {
		fmt.Printf("  - Shared Register 'R' History is linearizable\n")
	} else {
		fmt.Printf("  - Shared Register 'R History is NOT linearizable\n")
	}

	if resultRegQ == porcupine.Ok {
		fmt.Printf("  - Shared Register 'Q' History is linearizable\n")
	} else {
		fmt.Printf("  - Shared Register 'Q' History is NOT linearizable\n")
	}

}

func HistoryHBarSwapped() {

	fmt.Printf("\nChecking if the shared history (H') in HW3 P5 is linearizable...\n")
	
	A := 0
	B := 1
	C := 2

	eventsRegR := []porcupine.Event{
		{A, porcupine.CallEvent, registerInput{ true, 0}, 2}, // r:read()
		{B, porcupine.CallEvent, registerInput{false, 1}, 1}, // r:write(1)
		{C, porcupine.CallEvent, registerInput{false, 2}, 3}, // r:write(2)
		{A, porcupine.ReturnEvent, 1, 2}, 					  // r:1
		{C, porcupine.ReturnEvent, 0, 3}, 					  // r:void
		{B, porcupine.ReturnEvent, 0, 1}, 					  // r:void
		{B, porcupine.CallEvent, registerInput{ true, 0}, 4}, // r:read()
		{B, porcupine.ReturnEvent, 1, 4},				      // r:1
		{C, porcupine.CallEvent, registerInput{ true, 0}, 6}, // r:read()
		{C, porcupine.ReturnEvent, 1, 6},				      // r:1	
	}

	eventsRegQ := []porcupine.Event{
		{A, porcupine.CallEvent, registerInput{false, 3}, 5}, // q:write(3)
		{A, porcupine.ReturnEvent, 0, 5},                     // q:void
	}

	resultRegR, infoRegR := porcupine.CheckEventsVerbose(registerModel, eventsRegR, 0)
	resultRegQ, infoRegQ := porcupine.CheckEventsVerbose(registerModel, eventsRegQ, 0)

	porcupine.VisualizePath(registerModel, infoRegR, "/home/mogi/Github-Repos/porcupine/Homework3/hw3_p5_regR_swapped.html")
	porcupine.VisualizePath(registerModel, infoRegQ, "/home/mogi/Github-Repos/porcupine/Homework3/hw3_p5_regQ_swapped.html")
	
	if resultRegR == porcupine.Ok {
		fmt.Printf("  - Shared Register 'R' History is linearizable\n")
	} else {
		fmt.Printf("  - Shared Register 'R' History is NOT linearizable\n")
	}

	if resultRegQ == porcupine.Ok {
		fmt.Printf("  - Shared Register 'Q' History is linearizable\n")
	} else {
		fmt.Printf("  - Shared Register 'Q' History is NOT linearizable\n")
	}

}

func main() {

	HistoryHBar()
	HistoryHBarSwapped()

}
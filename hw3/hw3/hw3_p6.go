package main

import (
    "fmt"
    "github.com/anishathalye/porcupine"
)

// Define the input for queue operations
type queueInput struct {
	op    bool // false = enq, true = deq
	value int  // value for enq, ignored for deq
}

// Queue model for enq/deq operations
var queueModel = porcupine.Model{
	Init: func() interface{} {
		return []int{} // Queue starts empty
	},
	Step: func(state, input, output interface{}) (bool, interface{}) {
		queueState := state.([]int)
		queueInput := input.(queueInput)

		if queueInput.op == false { // Enqueue operation
			newQueue := append(queueState, queueInput.value)
			return true, newQueue // enq always succeeds
		} else { // Dequeue operation
			if len(queueState) == 0 {
				// Queue is empty, deq should return void
				return output == nil, queueState
			} else {
				expected := queueState[0]
				newQueue := queueState[1:]
				return output == expected, newQueue
			}
		}
	},
	DescribeOperation: func(input, output interface{}) string {
		inp := input.(queueInput)
		if inp.op == false {
			return fmt.Sprintf("enq('%c') -> void", inp.value)
		} else {
			if output == nil {
				return "deq() -> void"
			} else {
				return fmt.Sprintf("deq() -> '%c'", output.(int))
			}
		}
	},
}

func HistoryQueue() {

	fmt.Printf("\nChecking if the queue history in HW3 P6 is linearizable...\n")

	A := 0
	B := 1

	var x int = int('x')
	var y int = int('y')

	eventsQueue := []porcupine.Event{
		{A, porcupine.CallEvent, queueInput{false, x}, 1}, 	// A enq(x)
		{A, porcupine.ReturnEvent, nil, 1},                 // A r:void
		{B, porcupine.CallEvent, queueInput{false, y}, 2}, 	// B enq(y)
		{A, porcupine.CallEvent, queueInput{true, 0}, 3},   // A deq()
		{B, porcupine.ReturnEvent, nil, 2},                 // B r:void
		{A, porcupine.ReturnEvent, y, 3},                 	// A deq() -> y
	}

	resultQueue, infoQueue := porcupine.CheckEventsVerbose(queueModel, eventsQueue, 0)

	porcupine.VisualizePath(queueModel, infoQueue, "hw3_p6.html")
	
	if resultQueue == porcupine.Ok {
		fmt.Printf("  - Queue history is linearizable\n")
	} else {
		fmt.Printf("  - Queue history is NOT linearizable\n")
	}
}

func main() {
	HistoryQueue()
}

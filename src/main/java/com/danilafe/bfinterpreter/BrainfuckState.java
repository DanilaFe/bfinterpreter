package com.danilafe.bfinterpreter;

import java.util.Stack;

/**
 * A Brainfuck program state.
 * Holds all necessary information to interpret a Brainfuck program. 
 * @author vanilla
 *
 */
public class BrainfuckState {

	/**
	 * Return code that represents the interpreter reaching the set limit of instructions
	 */
	public static final int ERR_LIMIT_REACHED = 1;
	/**
	 * Return code that represents a successfully completed program
	 */
	public static final int ERR_PROGRAM_COMPLETED = 2;
	/**
	 * Return code that represents an invalid instruction in the program text.
	 */
	public static final int ERR_INVALID_INSTRUCTION = 3;
	/**
	 * Return code that represents the data pointer moving outside of data bounds.
	 */
	public static final int ERR_BOUNDS = 4;
	/**
	 * Return code that represents mismatched braces
	 */
	public static final int ERR_MISMATCHED = 5;
	/**
	 * The initial size of the interpreter memory.
	 */
	public static final int INITIAL_SIZE = 1024;
	int[] memory;
	char[] program;
	int remainingInstructions;
	int programIndex;
	int dataPointer;
	Stack<Integer> openBraces;
	String output;

	/**
	 * Creates a new Brainfuck state.
	 * 
	 * @param program
	 *            the program to interpret.
	 * @param remainingInstructions
	 *            the max number of instructions to execute.
	 */
	public BrainfuckState(String program, int remainingInstructions) {
		this.program = program.toCharArray();
		this.memory = new int[INITIAL_SIZE];
		this.remainingInstructions = remainingInstructions;
		this.programIndex = 0;
		dataPointer = 0;
		this.openBraces = new Stack<Integer>();
		output = "";
	}

	void growMemory() {
		int[] newMemory = new int[memory.length * 2];
		System.arraycopy(memory, 0, newMemory, 0, memory.length);
	}

	/**
	 * Executes a single instruction from the program.
	 * @return the return code signifying the result of the instruction.
	 */
	int executeInstruction() {
		int returnCode = 0;
		if (remainingInstructions == 0) {
			returnCode = ERR_LIMIT_REACHED;
		} else {
			if (remainingInstructions > 0) {
				remainingInstructions--;
			}
			if (programIndex >= program.length) {
				returnCode = ERR_PROGRAM_COMPLETED;
			} else {
				char instructionChar = program[programIndex];
				if (instructionChar == '>') {
					if (++dataPointer >= memory.length)
						growMemory();
				} else if (instructionChar == '<') {
					if (--dataPointer < 0)
						returnCode = ERR_BOUNDS;
				} else if (instructionChar == '+') {
					memory[dataPointer]++;
				} else if (instructionChar == '-') {
					memory[dataPointer]--;
				} else if (instructionChar == '.') {
					output += (char) memory[dataPointer];
				} else if (instructionChar == '[') {
					if (memory[dataPointer] == 0) {
						int numBraces = 1;
						while (numBraces != 0) {
							char instruction = program[++programIndex];
							if (instruction == '[') {
								numBraces++;
							} else if (instruction == ']') {
								numBraces--;
							}
						}
					} else {
						openBraces.push(programIndex);
					}
				} else if (instructionChar == ']') {
					if (openBraces.isEmpty()) {
						returnCode = ERR_MISMATCHED;
					} else {
						if (memory[dataPointer] != 0) {
							programIndex = openBraces.peek();
						} else {
							openBraces.pop();
						}
					}
				} else {
					returnCode = ERR_INVALID_INSTRUCTION;
				}
				if (returnCode == 0) {
					programIndex++;
				}
			}
		}
		return returnCode;
	}

	/**
	 * Runs as many instructions as the parameters allow.
	 * @return the first nonzero return code.
	 */
	public int executeInstructions() {
		int returnCode = 0;
		while (!Thread.currentThread().isInterrupted() && (returnCode = executeInstruction()) == 0)
			;
		return returnCode;
	}

	/**
	 * Gets the output of the program so far.
	 * @return the string containing the output of the interpreter.
	 */
	public String getOutput() {
		return output;
	}

}

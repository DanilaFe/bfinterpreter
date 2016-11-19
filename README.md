# bfinterpreter
A simple Brainfuck interpreter written in about 40 mins as a Java library.
Because your programming career is incomplete until you've at least _interpreted_ it.
## Usage
This project has a pom.xml and can be added as a dependency for maven. The setup code-wise is quite simple, and takes some three lines of code:
```Java
BrainfuckState state = new BrainfuckState("program", instructionLimit);
System.out.println("Program completed with return code: " + state.executeInstructions());
System.out.println("The program output was: " + state.getOutput());
```
## Limitations
Because this is intended to be a library without a set use case, this interpreter does not take input. I might add it later on should the need to do so arise, but it satisfies its purpose at the moment.

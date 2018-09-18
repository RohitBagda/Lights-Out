# Lights-Out
The game of Lights Out begins with an nxn board of buttons that double as light bulbs, where n ≥ 2.  In our implementation, we begin with all lights on and the objective is to turn them off. The catch is that when a button is toggled, its immediately adjacent neighbors are also toggled:

<p align="center">
  <img src="assets/LightsOutExplaination.png" alt="Lights Out Explaination"/>
</p>

We can use a variation of the Gaussian Elimination algorithm to find a solution to any square board. We focus on square boards in particular because they are guaranteed to have at least one state from which a solution state can be achieved. Additionally, we use Java to code an interactive visualization of the game up to a 99x99 board size. 
For further details about the process please refer to the Lights Out Paper available in the repository.

Almost half of the solutions we found for different Lights Out configurations were vertically and laterally symmetrical and they created some interesting patterns we may observe in real life. 
These can be found in the Symmetrical Solution Patterns directory

Requirements:

The only requirement to run the applet is Java. The Applet can be used by simply opening the Jar file provided in the repository. 
The source code is also provided in the src directory.

 

# Picross-Solver
A simple Java picross solver, downloading content from given hanjie-star.com game and solving it.

The solver utilizes a somewhat brute - force approach.
For each row and column the solver iterates through every possible combination based on corresponding border code.
Cells with the same value across every single matching combination are marked updated and marked on the matrix.

Class pool contains a constructor taking url to a given picross from site hanjie-star.com as the only argument.



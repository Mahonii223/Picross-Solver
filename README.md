# Picross-Solver

A simple Java picross solver, downloading content from given hanjie-star.com game and solving it.

## The Algorythm

The solver utilizes a somewhat brute - force approach.
For each row and column the solver iterates through every possible combination based on corresponding border code.
Cells with the same value across every single matching combination are marked updated and marked on the matrix.
The program continues to iterate through rows and columns until either the picross is solved or the patterns are evaluated as incorrect (insufficient and/or conflicting)

## Example use

Class pool contains a constructor taking url to a given picross from site hanjie-star.com as the only argument.
The constructor retrieves html content and parses it into top and side border data.
```
  Pool pool = new Pool("http://www.hanjie-star.com/picross/not-a-houndsooth-22157.html");
```
Solve the picross:
```
  pool.solve(false);
```
Where the only argument indicates whether or not the process of solving the picross should be displayed.


## Authors
* **Marcin Pham** - [mailto:Mahonii223](mailto://marcin.kamo@gmail.com)

## License
This project is licensed under the MIT License - see the [license.md](license.md) file for details

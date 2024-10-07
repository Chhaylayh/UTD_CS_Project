% Importing the example.pl file for
:- consult('example.pl').

% Predicate to check if a given position is a valid move
valid_move(X, Y, Maze) :-
    nth0(Y, Maze, Row),
    nth0(X, Row, Cell),
    (Cell = 'f' ; Cell = 's' ; Cell = 'e').

% Predicate for Valid Coordinates
valid_coordinates(X, Y, Maze) :-
    nth0(Y, Maze, Row),
    nth0(X, Row, Cell),
    member(Cell, ['f', 's', 'e']).

% Base case: Reached the exit
find_exit(X, Y, _, []) :-
    nth0(Y, X, Row),
    nth0(X, Row, 'e').

% Recursive rules for movement
find_exit(X, Y, Maze, [Action | Rest]) :-
    valid_move(X, Y, Maze),
    move(Action, X, Y, X1, Y1),
    find_exit(X1, Y1, Maze, Rest).

find_exit(X, Y, Maze, Actions) :-
    valid_coordinates(X, Y, Maze),
    nth0(Y, Maze, Row),
    nth0(X, Row, 's'),
    find_exit(X, Y, Maze, Actions).

% Define possible movements
move(left, X, Y, X1, Y) :- X > 0, X1 is X - 1.
move(right, X, Y, X1, Y) :- X < 3, X1 is X + 1.
move(up, X, Y, X, Y1) :- Y > 0, Y1 is Y - 1.
move(down, X, Y, X, Y1) :- Y < 3, Y1 is Y + 1.

% Predicate to test find_exit
test_find_exit(Actions) :-
    example_maze(Maze),
    find_exit(0, 0, Maze, Actions).

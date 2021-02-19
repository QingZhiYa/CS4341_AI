README

Group members:
Yaru Gong, Lyu Yang (Jimmy), and Zhifei Ma (Mona)

Team name:
GLM

Instructions on compiling and running your program:
1. edit run configuration and change the working directory 
to where referee created the GLM.go file
(e.g. C:\Users\Mona Ma\IdeaProjects\Gomoku-referee-CS4341-A20\gomoku)
2. run Main.java before runing referee.py
3. run referee.py

The utility function that your program uses:
For a given board, we use a function called oneLineScore to accumulate 
scores for each row, each column and two diagonals, oneLineScore will 
reference a scoreChart the records every corresponding score to a situation, 
like scoreChart.put(new Key(1, 1 , 1, 1, 1), 10000); represents wining.

The evaluation function that your program uses:
Our evaluation function takes three arguments: a node, whether it's in 
max/min layer, and its depth, return the max/min value among all child 
nodes based on a given turn.

The heuristics and/or strategies that you employed to decide how to expand the minimax tree without exceeding your time limit:
When adding children to a given Node, we implements alpha beta pruning
that get rid of children who don't have potential to win, by updating alpha and
beta (current min & max) value when building the tree.

Did your program play against human players? 
Yes. we play with our program by changing the move file. It's able to 
win human player.

Did your program play against itself? 
Yes. We mainly tested our program by playing against a copy of itself. 
We include the output of the result and it's 57 pages long.

Did your program play against other programs? 
Not any other program rather than itself, but we are confident in wining
the tornament.

How did your program do during those games?
Not bad, we think our program plays better than ourselves because it 
generates next move so fast while it's hard for us to make a decision
within 10 seconds.

describe the strengths and the weaknesses of your program:
Strengths: Fast and accurate
Weakness: may perform better with some randomness because right 
now the result of our program playing with itself is deterministic, 
maybe we will set the first move more randomly in the future.

A discussion of why the evaluation function and the heuristic(s) you picked are good choices:
1. Adding alpha beta pruning when adding children ensures that we can 
explore greater depths because some useless branch are pruned away. 
Alpha–beta needs to examine only O(bm/2) nodes to pick the best move, 
instead of O(bm) for minimax. The branching factor becomes √ b instead of b, 
alpha–beta can solve a tree roughly twice as deep as minimax in 
the same amount of time.
2. We add a has_neighbor function that checks whether a given move has allies
around its surrounding 8 positions, if not, then it's considered as an isolated 
child that will not be considered as one of the optimal move, we get rid of it 
directly and save time to explore other promising nodes.

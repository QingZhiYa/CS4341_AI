package com.company;

import java.util.ArrayList;

public class Node {
    private int[] move;
    private ArrayList<Node> children;
    private int[][] futureBoard = new int[15][15];
    private int score;
    private int[] bestMove;

    public Node(){
        move = new int[2];
        children = new ArrayList<Node>();
        score = -1;
    }

    public void setFutureBoard(int[][] board){
        futureBoard = board;
        //futureBoard[move[0]][move[1]] = turn;
    }

    public int[][] getFutureBoard(){
        return futureBoard;
    }

    //expand children
    public Node addChild(Node child){
        children.add(child);
        return this;
    }


    public ArrayList<Node> getChildren(){
        return children;
    }

    public Node setMove(int[] move){
        this.move = move;
        return this;
    }

    public int[] getMove(){
        return move;
    }

    public Node setScore(int s){
        score = s;
        return this;
    }

    public int getScore(){
        return score;
    }

    public void setBestMove(int[] bestM){
        bestMove = bestM;
    }

    public int[] getBestMove(){
        return bestMove;
    }

}

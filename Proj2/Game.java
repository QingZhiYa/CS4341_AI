package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Game {
    int[][] board = new int[15][15];
    //int score = 0;
    //Node tree;
    HashMap<Key, Integer> scoreChart = new HashMap<>();
    boolean firstMove = false;
    String teamName = "";
    boolean secondMove = true;
    //int turn = 1;


    Game(String name){
        //System.out.println(move);
        teamName = name;
        setUpChart();
        setUpBoard();

    }


    private boolean setUpBoard(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                board[i][j] = 0;
            }
        }return true;
    }

    private boolean setUpChart(){
        scoreChart.put(new Key(1, 0 , 1, 0, 0), 2);
        scoreChart.put(new Key(1, 1 , 0, 0, 0), 5);
        scoreChart.put(new Key(0, 0 , 0, 1, 1), 5);
        scoreChart.put(new Key(1, 1 , 1, 0, 0), 30);
        scoreChart.put(new Key(0, 0 , 1, 1, 1), 30);
        scoreChart.put(new Key(1, 1 , 1, 1, 0), 50);
        scoreChart.put(new Key(0, 1 , 1, 1, 1), 50);
        scoreChart.put(new Key(1, 1 , 1, 1, 1), 10000); //win

        scoreChart.put(new Key(1, 1 , 0, 1, 0), 10);
        scoreChart.put(new Key(1, 1 , 0, 0, 1), 7);
        scoreChart.put(new Key(1, 0 , 0, 0, 1), 3);

        scoreChart.put(new Key(1, -1 , -1, -1, 1), 25);
        scoreChart.put(new Key(1, -1 , -1, 0, 0), 10);
        scoreChart.put(new Key(1, -1 , -1, -1, 0), 30);
        scoreChart.put(new Key(1, -1 , -1, -1, -1), 50);
        scoreChart.put(new Key(-1, -1 , -1, -1, 1), 50);
        scoreChart.put(new Key(-1, 1, 0, 0, 0), 10);
        scoreChart.put(new Key(0, 0, 0, 1, -1), 10);

        //scoreChart.put(new Key(1, -1, 0, 0, 0), -10);
        //scoreChart.put(new Key(0, 0, 0, -1, 1), -10);
        scoreChart.put(new Key(-1, -1 , 0, 0, 0), -5);
        scoreChart.put(new Key(0, 0 , 0, -1, -1), -5);
        scoreChart.put(new Key(-1, -1 , -1, 0, 0), -30);
        scoreChart.put(new Key(0, 0 , -1, -1, -1), -30);
        scoreChart.put(new Key(1, 1 , 1, 1, -1), -10);
        scoreChart.put(new Key(-1, 1 , 1, 1, 1), -10);
        scoreChart.put(new Key(-1, -1 , -1, -1, 0), -50);
        scoreChart.put(new Key(0, -1 , -1, -1, -1), -50);
        scoreChart.put(new Key(-1, -1 , -1, -1, -1), -10000);

        //...
        return true;
    }

    public String makeMove(File file){
        //move
        String move = "";
        //first move
        if(firstMove){
            System.out.println("First Move!");
            move = centerMove();
        }else if(secondMove){
            //second move
            System.out.println("Second Move!");
            move = centerMove();
        }else{
            move = minimax(2);
        }


        rewriteFile(file, move);
        return move;

    }

    public boolean updateBoard(File move){
        String content = readMove(move);
        if(content == "" && secondMove == true){
            firstMove = true;
            secondMove = false;
            return true;
        }
        if(content == ""){
            return false;
        }
        String[] parse =  content.split(" ");

        if(parse.length == 3 && Integer.parseInt(parse[2]) != -1){
            char c = parse[1].charAt(0);
            if(parse[0].equals(teamName) || c == Character.toUpperCase(c)){
                return false;
            }

            //System.out.println(c);
            c = Character.toUpperCase(c);
            if(c-64-1 >= 15 || c-64-1 < 0 ||
                    Integer.parseInt(parse[2])-1 >=15 || Integer.parseInt(parse[2])-1 < 0){
                System.out.println("The move_file includes move range > 15 or < 1");
                return false;
            }
            System.out.println(content);
            board[Integer.parseInt(parse[2])-1][c-64-1] = -1;

            return true;
        }else{
            System.out.println("move_file includes wrong format input: "+ content);

            return false;
        }
    }


    public String readMove(File move){
        try {
            //System.out.println(move);
            Scanner myReader = new Scanner(move);
            String content = "";
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                content += data;
            }
            //myReader.close();
            return content;
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            return null;
        }
    }

    private String centerMove(){
        //put it in the center
        board[7][7] = 1;

        firstMove = false;
        secondMove = false;
        return teamName + " H 8";
    }


    private int oneLineScore(int[] oneLine){
        if(oneLine.length < 5){return 0;}
        int point = 0;
        for(int i = 0; i < oneLine.length - 5; i++){

            int[] five = Arrays.copyOfRange(oneLine, i, i+5);
            Key key = new Key(five);

            if(scoreChart.containsKey(key)){
                //System.out.println("Add Points");
                point+=scoreChart.get(key);
            }
        }
        //System.out.println("Points: "+point);
        return point;
    }

    private int[] getColumn(int[][] array, int index){
        int[] column = new int[array[0].length];
        for(int i=0; i<column.length; i++){
            column[i] = array[i][index];
        }
        return column;
    }

    private int[][] horizontalTransform(int[][] array){
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length / 2; j++) {
                int temp = array[i][j];
                array[i][j] = array[i][array.length - 1 - j];
                array[i][array.length - 1 -j] = temp;
            }
        }return array;
    }

    private int[][] diagonalOrder(int matrix[][]) {
        int[][] diagArray = new int[29][15];

        for (int line = 1; line <= (15 + 15 - 1); line++) {
            int start_col = Math.max(0, line - 15);

            int count = Math.min(Math.min(line, (15 - start_col)), 15);

            for (int j = 0; j < count; j++)
                diagArray[line-1][j] = matrix[Math.min(15, line) - j - 1][start_col + j];

        }return diagArray;
    }

    //Utility function of a given board
    private int utilityFunc(Node n){
        //System.out.println("Move: "+ n.getMove()[0]+ " "+ n.getMove()[1]);
        int point = 0;
        int[][] b = n.getFutureBoard();

        //each row score
        for(int r = 0; r < 15; r++){
            //System.out.println("Row: "+r);
            point += oneLineScore(b[r]);
        }

        //each col score
        for(int c = 0; c < 15; c++){
            //System.out.println("Col: "+c);
            point += oneLineScore(getColumn(b, c));
        }
        //"/" Diagonal score
        int[][] diag = diagonalOrder(b);
        for(int i = 0; i < 29; i++){
            //System.out.println("Diag / : "+i);
            point += oneLineScore(diag[i]);
        }
        //"\" Diagonal score
        //flip horizontally
        int[][] diag2 = diagonalOrder(horizontalTransform(b));
        for(int j = 0; j < 15; j++){
            //System.out.println("Diag : "+j);
            point += oneLineScore(diag2[j]);
        }
        //System.out.println(point);

        return point;


    }



    private boolean has_neighbor(int row, int column, Node n){
//        int column = n.getMove()[0];
//        int row = n.getMove()[1];


        if (column == 0 || column == 14 ||
                row == 0 || row == 14){
            return true;
        }
        else{
            /* check all these 0 position in the furture board
                     0 0 0
                     0 1 0
                     0 0 0 */
            return n.getFutureBoard()[row][column + 1] != 0 ||
                    n.getFutureBoard()[row][column - 1] != 0 ||
                    n.getFutureBoard()[row - 1] [column - 1]!= 0 ||
                    n.getFutureBoard()[row + 1][column - 1] != 0 ||
                    n.getFutureBoard()[row - 1][column + 1] != 0 ||
                    n.getFutureBoard()[row + 1][column + 1] != 0 ||
                    n.getFutureBoard()[row + 1][column] != 0 ||
                    n.getFutureBoard()[row - 1][column] != 0;
        }
    }


    //Heuristic evaluation function that implements minimax
    private int evaluateFunc(Node n, int turn, int depth){
        if(depth <= 0){
            return utilityFunc(n);
        }
        ArrayList<Node> children = n.getChildren();
        if(turn == 1){
            int max = Integer.MIN_VALUE;
            for(Node child : children){

                if(child.getScore() > max){
                    max = child.getScore();
                    n.setBestMove(child.getMove());
                    //System.out.println("MAX point: "+max);
                }
            }return max;
        }else{
            int min = Integer.MAX_VALUE;
            for(Node child : children){

                if(child.getScore() < min){
                    min = child.getScore();
                    n.setBestMove(child.getMove());
                    //System.out.println("MIN point: "+min);
                }
            }return min;

        }
    }

    private int flipTurn(int turn){
        int t = turn;
        if(turn == 1){
            t = -1;
        }
        if(turn == -1){
            t = 1;
        }
        return t;
    }



    private int[][] copyArray(int[][] ary){
        int[][] copy = new int[15][15];
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                copy[i][j] = ary[i][j];
            }
        }return copy;
    }

    //alpha-beta pruning before adding a child
    private int addChildren(Node node, int alpha, int beta, int turn, int depth){
        if(depth <= 0){
            int score = evaluateFunc(node, turn, depth);
            node.setScore(score);
            //return score;
            return score;
        }

        if(turn == 1){
            for(int i = 0; i < 15; i++){
                for(int j = 0; j < 15; j++){
                    //empty
                    if(node.getFutureBoard()[i][j] == 0 && has_neighbor(i, j, node)){
                        Node child = new Node();
                        child.setMove(new int[]{i,j});
                        //child.turn = flipTurn(node.turn);
                        int[][] newBoard = copyArray(node.getFutureBoard());
                        newBoard[i][j] = turn;
                        child.setFutureBoard(newBoard);

                        int v = addChildren(child, alpha, beta, flipTurn(turn), depth-1);

                            if (v > alpha) {
                                alpha = v;
                                node.setScore(v);
                                node.setBestMove(new int[]{i,j});
                            }
                            if(alpha >= beta){return alpha;}
                            node.addChild(child);

                        //addChildren(child, -beta, -alpha, flipTurn(turn),depth-1);


                        //node.setScore(addChildren(node, flipTurn(turn),depth-1));
                    }
                }
            }
            //node.setScore(evaluateFunc(node, depth, turn));
            return alpha;
        }else {
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    //empty

                    if (node.getFutureBoard()[i][j] == 0 && has_neighbor(i, j, node)) {
                        Node child = new Node();
                        child.setMove(new int[]{i, j});
                        //child.turn = flipTurn(node.turn);
                        int[][] newBoard = copyArray(node.getFutureBoard());
                        newBoard[i][j] = turn;
                        child.setFutureBoard(newBoard);

                        int v = addChildren(child, alpha, beta, flipTurn(turn), depth - 1);
                        //node.setScore(evaluateFunc(node, depth, turn));
                        if(v < beta){
                            beta = v;
                            node.setBestMove(new int[]{i,j});
                        }
                        if(beta <= alpha){return beta;}


                        node.addChild(child);

                        //addChildren(child, -beta, -alpha, flipTurn(turn),depth-1);


                        //node.setScore(addChildren(node, flipTurn(turn),depth-1));
                    }
                }
            }

            return beta;

        }
    }

    //minimax implementation
    private String minimax(int depth){
        Node node = new Node();
        node.setFutureBoard(copyArray(board));
        int turn = 1;
        //node.turn = turn;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        addChildren(node, alpha, beta, turn, depth);
        int[] move = node.getBestMove();


        board[move[0]][move[1]] = 1;
        //System.out.println(move[0]+" "+move[1]);
        return printMove(move);
    }


    private String printMove(int[] move){
        String print = teamName + " ";
        char letter = (char) (move[1]+1+64);
        print += letter;
        print += " ";
        print += Integer.toString(move[0]+1);

        return print;
    }

    public boolean rewriteFile(File file, String content){
        try{
            FileOutputStream fooStream = new FileOutputStream(file, false);
            byte[] myBytes = content.getBytes();
            fooStream.write(myBytes);
            fooStream.close();
            return true;

        }catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


}

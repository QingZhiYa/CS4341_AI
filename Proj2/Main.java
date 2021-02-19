package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    //Group name stand for Yaru Gong, Lyu Yang, and Zhifei Ma
    public static String teamName = "GLM";

    public static void main(String[] args) {
        Game game = new Game(teamName);
        //String lastMove = "INITIATE";
        while(true){

            //if it is my turn
            if(readFile(teamName + ".go") != null){
                //System.out.println("variable:"+lastMove);
                //System.out.println("Inside the file:"+game.readMove(readFile("move_file")));
                File moveF = readFile("move_File");
                //team name can not be the same
                //System.out.println(game.readMove(moveF));
                String content = game.readMove(moveF);
                String[] parse =  content.split(" ");
                if(moveF != null && (content == "" || !parse[0].equals(teamName))){
                    //System.out.println(readFile("end_game"));
                    if(readFile("end_game") == null){
                        //System.out.println(".go file founded");
                        if(game.updateBoard(readFile("move_file")) == true){
                            String Move = game.makeMove(readFile("move_file"));
                            System.out.println("My Move: "+Move);
                        }
                        continue;

                    }
                    else{
                        System.out.println("Game Ended");
                        System.exit(0);
                        break;
                    }
                }

            }
            if(readFile("end_game") != null){
                System.out.println("***************end_game file found ***************");
                System.out.println("Game Ended");
                System.exit(0);
                break;
            }
        }
    }


    private static File readFile(String fileN){
        try {
            String path = "././" + fileN;
            File file = new File(path);
            Scanner myReader = new Scanner(file);
            String content = "";
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                content += data;
            }
            myReader.close();
            return file;
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            return null;
        }
    }


}

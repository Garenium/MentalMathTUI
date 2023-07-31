package com.gikezian;

import java.text.DecimalFormat;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import com.harium.hci.espeak.*;
import org.apache.commons.lang3.math.NumberUtils;

class MentalMath{

  // Reset Colour
  public static final String ANSI_RESET = "\033[0m";  // Text Reset

  // Regular Colors
  public static final String ANSI_RED = "\033[0;31m";     // RED
  public static final String ANSI_GREEN = "\033[0;32m";   // GREEN
  public static final String ANSI_YELLOW = "\033[0;33m";  // YELLOW

  //Name for the file
  private static final String FILE_NAME = "quiz.txt";
  private static final DecimalFormat decimalFormat = new DecimalFormat("0.0");

  private static double returnSolution(double n1, double n2, int op){

      double solution = 0.0;

      switch (op) {
          case 0:
              solution = n1 + n2;
              break;
          case 1:
              solution = n1 - n2;
              break;
          case 2:
              solution = n1 * n2;
              break;
          case 3:
              solution = Double.parseDouble(decimalFormat.format(n1 / n2)); //MUST TRUNCATE to 0.0
              break;
      }
        return solution;
  }

  public static void clearConsole()
  {
      try
      {
          final String os = System.getProperty("os.name");

          if (os.contains("Windows"))
          {
              new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
          }
          else
          {
              System.out.print("\033\143");
          }
      }
      catch (final Exception e)
      {
          System.out.println("Console not properly cleaned");
          e.printStackTrace();
      }
  }

  public static void main(String[] args) throws IOException {

    Espeak espeak = new Espeak();
    Random rand = new Random();

    //clearConsole();

    char[] charOps = {'+', '-', 'x', '/'};

    int n1;
    int n2;
    int op; // op is short for operator (where 0 is +, 1 is -, 2 is *, and 3 is /)
    char charOp;
    double solution; //typed the full name "solution" to stand out
    int score = 0;
    int questionNos = 10;
    int maxRange = 10;


    //bash will care of this
    ////look at the arguments
    //  if(args.length == 2){
    //        //args[0] is the max range (max 100)
    //        //args[1] is the number of questions (max 50)
    //        try{
    //            questionNos = Integer.parseUnsignedInt(args[0]);
    //            maxRange = Integer.parseUnsignedInt(args[1]);
    //           // if(questionNos > 50 && maxRange > 99){
    //           //     System.exit(-1);
    //           // }
    //        }
    //        catch(NumberFormatException ex){
    //            System.out.println("Error: Invalid parameter(s)");
    //            System.exit(-1);
    //        }
    //  }
    //  else if(args.length == 1){
    //      if(args[0].equals("-h")){
    //          String help = "\n" +
    //                  "To get help: ./run -h\n" +
    //                  "./run [num of questions] [max range]\n\n" +
    //                  "Made by Garenium (2022)\n";
    //          System.out.println(help);
    //          System.exit(0);
    //      }
    //      else{
    //          System.out.println("Invalid argument: \""+args[0]+"\"");
    //          System.exit(-1);
    //      }

    //  }



      String greetings = "Welcome to MentalMathTUI. Your mental math coach.\n" +
              "Non-integer answers are not approximated and are in tenths place\n";

      System.out.println(greetings);

      System.out.println("There will be "+questionNos+" questions.");
      System.out.println(String.format("Range: [0, %d]\n", maxRange));

      Scanner inp = new Scanner(System.in);
      System.out.println("Press any input to start: ");
      espeak.speak("Press any input to start");
      inp.nextLine();

      try{
          FileWriter fw = new FileWriter(FILE_NAME);
        fw.write("");

        int questionNo = 0;
        while(questionNo < questionNos) {
            //INITIALIZE THE OPERANDS AND THE OPERATOR
            //I thought the operands n1 and n2 are better names than x and y (or a and b).
            n1 = rand.nextInt(maxRange);    //Numbers from [0..100]
            n2 = rand.nextInt(maxRange)+1;  //Numbers from [1..100] (from 1 to disallow n1/0)
            op = rand.nextInt(4);      //[0..3]
            charOp = charOps[op]; //Initialize the character for the equation operator
            //Debugging purposes
            // n1 = 3;
            //n2 = 6;
            //op = 1;
            solution = returnSolution(n1,n2,op);

            String operation = "";
            switch(charOp){
                case '+':
                    operation = "plus";
                    break;
                case '-':
                    operation = "minus";
                    break;
                case 'x':
                    operation = "times";
                    break;
                case '/':
                    operation = "divided by";
                    break;
            }
            String question = String.format("%d %s %d", n1, operation, n2);
            fw.write((questionNo+1) + ". " + question);

            //OUTPUT EQUATION
            String expression = String.format("  %s\n%c %s\n=====\n", String.format("%1$02d",n1), charOp, String.format("%1$02d",n2));
            String answer;

            //DO WHILE UNTIL USER INPUT IA A VALID NUMBER
            do {
                espeak.speak(question); //AUDIO FOR THE EQUATION
                System.out.printf("%s  ",ANSI_YELLOW+expression+ANSI_RESET); //PRINT THE EQUATION
                //USER INPUT
                answer = inp.nextLine();
            }while(!(NumberUtils.isCreatable(answer)));


            fw.write(" = " + answer + '\n');

            //CHECK IF THE INPUT IS CORRECT
            if( (solution == Double.parseDouble(answer)) ){
                System.out.println(ANSI_GREEN+"Correct!"+ANSI_RESET);
                System.out.println("The answer was " + answer+'\n');
                fw.write(question + " = " + answer + " [V]" +"\n\n");
                ++score;
            }
            else{
                System.out.println(ANSI_RED+"Incorrect!"+ANSI_RESET);
                System.out.println("The answer was " + solution);
                fw.write(question + " = " + answer + " [X]" +"\n\n");
            }

            //ADD questionNo BY ONE UNTIL THE NUMBER OF QUESTIONS ARE ALL MET (VIA WHILE)
            questionNo = questionNo + 1;
        }

        //SCORE OUTPUT
        fw.write("Score: " + score + "/" + questionNo + '\n');
        fw.close();

        System.out.println("You scored: " + score + "/" + questionNo);
    }
    catch(IOException e){
      System.out.println("File-related exception: ");
      e.printStackTrace();
    }
    catch(Exception e){
        System.out.println("Exception: ");
        e.printStackTrace();
    }


  }
}



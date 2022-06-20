package com.gikezian;

import java.util.Random;
import java.io.FileWriter;
//import java.io.File;
import java.io.IOException;
import java.util.Scanner;
// import junit;
import com.harium.hci.espeak.*;
// import org.slf4j.*;

class MentalMath{

  private static final String FILE_NAME = "quiz.txt";

  private static double returnSolution(int n1, int n2, int op){

      double solution = 0.0;
      //swap if the dividend is smaller than the divisor

      if(op == 4){
          if (((int) (Math.log10(n1) + 1) == 1)) {
              System.out.println("n1 was small");
              n1 = n1 ^ n2;
              n2 = n1 ^ n2;
              n1 = n2 ^ n1;
          }
      }

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
              solution = n1 / n2;
              break;
          //deal change the case 3 part to deal with non-terminating nums later
      }
        return solution;
  }

  public static void main(String[] args)
  {
    String greetings = "Hello, this is MentalMathTUI. This program enables the user to practice calculating numbers mentally.\n" +
            "";
    System.out.println(greetings);
    Espeak espeak = new Espeak();
    Random rand = new Random();
    char[] charOps = {'+', '−', '×', '÷'};

    int n1;
    int n2;
    int op; // op is short for operator (where 0 is +, 1 is -, 2 is *, and 3 is /)
    char charOp;
    double solution; //typed the full name "solution" to stand out
    int score;
    int questionNo = 1;

    try{
        FileWriter fw = new FileWriter(FILE_NAME);
        //File file = new File(FILE_NAME);
        Scanner inp = new Scanner(System.in);
        fw.write("");

        while(questionNo < 4) {
            n1 = rand.nextInt(10);  //Numbers from [0..100]
            n2 = rand.nextInt(10)+1;  //Numbers from [1..100]
            //op = rand.nextInt(4);    //[0..3]
            op = 1; //Debugging purposes
            charOp = charOps[op]; //For writing to the file and the console
            solution = returnSolution(n1,n2,op);

            String question = String.format("%d %c %d", n1, charOp, n2);
            fw.write(questionNo+". " + question);

            //write to System.out (FIX THE FORMATTING) (Still not satisfied)
            String expression = String.format("  %s\n%c %s\n‾‾‾‾‾\n", String.format("%1$02d",n1), charOp, String.format("%1$02d",n2));
            System.out.printf("%s", expression);
            espeak.speak(question);

            //User input
            String answer = inp.nextLine();
            fw.write(" = " + answer + '\n');


            if( (solution == Double.parseDouble(answer)) ){
                System.out.println("Correct!");
                System.out.println("The answer was " + answer);
                fw.write(question + " = " + answer + " [V]" +'\n');
            }
            else{
                System.out.println("Incorrect!");
                System.out.println("The answer was " + solution);
                fw.write(question + " = " + answer + " [X]" +"\n\n");
            }

            questionNo = questionNo + 1;
        }
        fw.close();
    }
    catch(IOException e){
      System.out.println("File-related exception: ");
      e.printStackTrace();
    }
    catch(Exception e){
        e.printStackTrace();
    }



  }
}



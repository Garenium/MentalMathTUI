package com.gikezian;

import java.util.Random;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
// import junit;
import com.harium.hci.espeak.*;
// import org.slf4j.*;

class MentalMath{

  private static final String FILE_NAME = "quiz.txt";

  public static void main(String[] args)
  {
//    String greetings = "Hello\n";
//    System.out.println(greetings);

    Espeak espeak = new Espeak();

    int n1 = -1; //n1 and n2 are operands
    int n2 = -1;
    int op = 0; //op for "operator" (0 for +, 1 for -, 2 for *, 3 for /)
    char charOp = '+'; //For writing to the file and the console
    int solution = -1; //typed the full name "solution" to stand out
    int score = -1;

    int questionNo = 1;

    Random rand = new Random();
    try{
        FileWriter fw = new FileWriter(FILE_NAME);
        File f = new File(FILE_NAME);
        Scanner inp = new Scanner(System.in);
        boolean madeInput = true;

        fw.write("");

        while(questionNo < 4) {
            n1 = rand.nextInt(101);  //Numbers from [0..100]
            n2 = rand.nextInt(101);  //Numbers from [0..100]
            /* op is short for operator (where 0 is +, 1 is -, 2 is *, and 3 is /) */
            op = rand.nextInt(4);    //[0..3]
            // op = 3;  //later debugging

            //swap if the dividend is smaller than the divisor
            if (((int) (Math.log10(n1) + 1) == 1)) {
                System.out.println("n1 was small");
                n1 = n1 ^ n2;
                n2 = n1 ^ n2;
                n1 = n2 ^ n1;
            }

            switch (op) {
                case 0:
                    charOp = '+';
                    solution = n1 + n2;
                    break;
                case 1:
                    charOp = '−';
                    solution = n1 - n2;
                    break;
                case 2:
                    charOp = '×';
                    solution = n1 * n2;
                    break;
                case 3:
                    charOp = '÷';
                    solution = n1 / n2;
                    break;
                //deal change the case 3 part to deal with non-terminating nums later
            }

            //speak (problem)
            // String speak = String.format("espeak -f \"%d %c %d\"", n1, charOp, n2);
            // Runtime.getRuntime().exec(speak.toString());

            String question = String.format("%d %c %d", n1, charOp, n2);
            fw.write(question + '\n');
            //Runtime.getRuntime().exec("espeak -f quiz.txt");

            //write to System.out (FIX THE FORMATTING)
            String expression = String.format("  %d\n%c %d\n‾‾‾‾‾\n", n1, charOp, n2);
            System.out.printf("%s", expression);
            espeak.speak(question);

            String answer = "";
            answer = inp.nextLine();

            if(Integer.toString(solution).equals(answer)){
                System.out.println("Correct!");
                System.out.println("The answer was " + solution);
            }
            else{
                System.out.println("Incorrect!");
                System.out.println("The answer was " + solution);
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



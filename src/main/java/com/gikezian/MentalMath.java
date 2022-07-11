package com.gikezian;

import java.text.DecimalFormat;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
// import junit;
import com.harium.hci.espeak.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

class MentalMath{

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
    String greetings = "Hello, this is MentalMathTUI. This program enables the user to practice calculating numbers mentally.\n" +
            "";
    System.out.println(greetings);
    Espeak espeak = new Espeak();
    AnsiConsole.systemInstall();
    Random rand = new Random();

   clearConsole();

    char[] charOps = {'+', '-', 'x', '/'};

    int n1;
    int n2;
    int op; // op is short for operator (where 0 is +, 1 is -, 2 is *, and 3 is /)
    char charOp;
    double solution; //typed the full name "solution" to stand out
    int score = 0;
    int questionNo = 0;

    try{
        FileWriter fw = new FileWriter(FILE_NAME);
        Scanner inp = new Scanner(System.in);
        fw.write("");


        while(questionNo < 4) {
            n1 = rand.nextInt(100);  //Numbers from [0..100]
            n2 = rand.nextInt(100)+1;  //Numbers from [1..100]
            op = rand.nextInt(4);    //[0..3]
/*            n1 = 3;
            n2 = 6;
            op = 1; //Debugging purposes*/
            charOp = charOps[op]; //For writing to the file and the console
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
/*            System.out.println(question);*/
            fw.write((questionNo+1) + ". " + question);

            //write to System.out (FIX THE FORMATTING) (Still not satisfied)
            //horrible output
            //My thought: make a "config formatting" object/class in a
            // separate file dedicated to format output for windows and linux respectively

            String expression = String.format("  %s\n%c %s\n=====\n", String.format("%1$02d",n1), charOp, String.format("%1$02d",n2));
            String answer;

            do {
                espeak.speak(question);
                System.out.printf("%s  ", ansi().fg(YELLOW).a(expression));

                //User input
                answer = inp.nextLine();
            }while(!(NumberUtils.isCreatable(answer)));


            fw.write(" = " + answer + '\n');


            if( (solution == Double.parseDouble(answer)) ){
                System.out.println(ansi().fg(GREEN).a("Correct!"));
                System.out.println("The answer was " + answer);
                fw.write(question + " = " + answer + " [V]" +"\n\n");
                ++score;
            }
            else{
                System.out.println(ansi().fg(RED).a("Incorrect!"));
                System.out.println("The answer was " + solution);
                fw.write(question + " = " + answer + " [X]" +"\n\n");
            }

            questionNo = questionNo + 1;
        }

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

    AnsiConsole.systemUninstall();

  }
}



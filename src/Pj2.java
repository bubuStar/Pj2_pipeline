import java.io.IOException;
import java.util.Scanner;

public class Pj2 {

    public static void main(String []args) throws IOException {

        new Pj2().execute();

    }

    public void execute() throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Initialing CPU");

//        int modeNumber = sc.nextInt();
//        System.out.println("Please input mode number for calculating miss: /k ");
//
//        System.out.println("mode number for calculating miss is : "+modeNumber+"\n");
//
//        Simulator simulator = new Simulator(modeNumber);

        Simulator simulator = new Simulator();

        Decoder decoder = new Decoder();
        decoder.getInstructionFromFile();

        simulator.instructionList = decoder.instructionArrayList;
    }
}


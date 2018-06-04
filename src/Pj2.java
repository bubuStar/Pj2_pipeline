import java.io.IOException;
import java.util.Scanner;

public class Pj2 {

    public static void main(String []args) throws IOException {
while(true) {
    new Pj2().execute();
}
    }

    public void execute() throws IOException {

    Scanner sc = new Scanner(System.in);
//    System.out.println("Initialing CPU");
    System.out.println("Please select experiment parameters:" +"\n"+
            "input 1 to select experiment 1 (mode number: 128&16)"+"\n"+
            "input 2 to select experiment 2 (mode number: 64&16)"+"\n"+
            "input 3 to select experiment 3 (mode number: 128&32)"+"\n"+
            "input 4 exit");
    int modeNumber = sc.nextInt();
//        System.out.println("Please input mode number for calculating miss: /k ");
//
//        System.out.println("mode number for calculating miss is : "+modeNumber+"\n");


    Simulator simulator = new Simulator(modeNumber);

    Decoder decoder = new Decoder();
    decoder.getInstructionFromFile();

    simulator.instructionList = decoder.instructionArrayList;
    simulator.run();
}

}


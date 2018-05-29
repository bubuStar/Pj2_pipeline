import java.io.IOException;
import java.util.Scanner;

public class Pj2 {

    public static void main(String []args) throws IOException {

        new Pj2().execute();

    }

    public void execute() throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Initialing CPU");
//        int cacheSize = sc.nextInt();
//        System.out.println("Please input block size /k Byte");
//        int blockSize = sc.nextInt();
//        System.out.println("Please input way associate parameter");
//        int way = sc.nextInt();
//        System.out.println("This cache parameter is ");
//        System.out.println("cache size : "+cacheSize+"\n"+
//                "block size："+blockSize+"\n"+"way associate "+way);

        Decoder decoder = new Decoder();
//        decoder.getAddressFromFile();
        decoder.getInstructionFromFile();
    }
}


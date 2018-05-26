import java.io.IOException;

public class Decoder {

    static final String R0 = "00000";

    static String instructionFilePath = "inst_data_riscv_trace_project_2_short.txt";
    static String addressFilePath = "inst_addr_riscv_trace_project_2_short.txt";

    public static void getAddressFromFile() throws IOException {
        int[] addressArray = helper.readAddressFile(addressFilePath);
        //System.out.println(addressArray[19]);
    }

    public static void getInstructionFromFile() throws IOException {

        String dataStringFromFile = helper.readTXTFile(instructionFilePath);

        byte[] instbyte = helper.hexStringToByte(dataStringFromFile);
        int[][] instBits = helper.getBits(instbyte, 20, 32);
        int[][] instBitsReversed = reverseIntArray(instBits);
        char[][] instBitsChar = new char[instBits.length][instBits[0].length]; //row=20;column=32

        for (int i = 0; i < instBitsChar.length; i++) {
            for (int j = 0; j < instBits[0].length; j++) {
                instBitsChar[i][j] = (char) (instBitsReversed[i][j] + '0');
                //   System.out.print(addBitsChar[i][j]);
            }
            // System.out.println(addBitsChar[i]);
        }

        String newInstruction = String.valueOf(instBitsChar[0]);
        System.out.println("reversed instruction code = "+ newInstruction);
        String opCode = newInstruction.substring(0,7);
        //System.out.println(opCode);
        parseOpcode(newInstruction,opCode);
    }

    public static void parseOpcode (String instCode, String opcode) {
        if (opcode.equals("0000011")==true) { //I type load instructions:
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = R0;
            String Rd = parseRdFrom7to11(instCode);
            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode +
                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
        }else if (opcode.equals("0010011")==true){ //I type arithmetic instructions:
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = R0;
            String Rd = parseRdFrom7to11(instCode);
            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode +
                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
        }else if (opcode.equals("0100011")==true){//S type store instructions
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = parseRs2From20to24(instCode);
            String Rd = R0;
            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode +
                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
        }else if (opcode.equals("1100011")==true | opcode.equals("1110011")==true ){
            //SB/B type branch instructions or SYSTEM treat as SB type branch instructions
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = parseRs2From20to24(instCode);
            String Rd = R0;
            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode +
                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
        }else if (opcode.equals("1100111")==true){//I type jump instructions JALR
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = R0;
            String Rd = parseRdFrom7to11(instCode);
            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode +
                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
        }else if (opcode.equals("1101111")==true){//J type jump instructions JAL
            String Rs1 = R0;
            String Rs2 = R0;
            String Rd = parseRdFrom7to11(instCode);
            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode +
                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
        }else if (opcode.equals("0110111")==true| opcode.equals("0010111")==true){
                                        //U type load upper immediate instructions
            String Rs1 = R0;
            String Rs2 = R0;
            String Rd = parseRdFrom7to11(instCode);
            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode +
                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
        }else if (opcode.equals("0110011")==true){//R type arithmetic instructions
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = parseRs2From20to24(instCode);
            String Rd = parseRdFrom7to11(instCode);
            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode +
                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
        }else if (opcode.equals("0001111")==true | opcode.equals("1110011")==true){//treat as nop
            String Rs1 = R0;
            String Rs2 = R0;
            String Rd = R0;
            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode +
                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
        }
    }

    public static String parseRs1From15to19(String instCode){
        String Rs1 = instCode.substring(15,19);
        return Rs1;
    }

    public static String parseRs2From20to24(String instCode){
        String Rs2 = instCode.substring(20,24);
        return Rs2;
    }

    public static String parseRdFrom7to11(String instCode){
        String Rd = instCode.substring(7,11);
        return Rd;
    }

    public static int[][] reverseIntArray(int[][] intArray){
        for(int i = 0;i < intArray.length; i ++){
            reverseArray(intArray[i], 0, intArray[0].length-1);
        }
        return intArray;
    }

    public static void swap(int[] A, int a, int b){
        int temp;
        temp = A[a];
        A[a] = A[b];
        A[b] = temp;
    }

    public static void reverseArray(int A[], int start, int end){

        if(start == end || start > end){
            return;
        }
        if(start < end){
            swap(A,start, end);
        }
        reverseArray(A, ++start, --end);
    }
}


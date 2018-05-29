import java.io.IOException;
import java.util.*;

public class Decoder {

    static final String R0 = "00000";

    static String instructionFilePath = "inst_data_riscv_trace_project_2.txt";
    static String addressFilePath = "inst_addr_riscv_trace_project_2.txt";
    static int numberOfLw = 0;
    static int numberOfBranch = 0;
    static int numberOfJAL = 0;
    static int numberOfJALR = 0;
    static int numberOfR_ALU = 0;
    static int numberOfI_ALU = 0;
    static int numberOfSW = 0;
    static int numberOfNop = 0;
    static int numberOfULw = 0;
    static int numberOfTakenBranch = 0;
//    public static void getAddressFromFile() throws IOException {
//
//
//
//    }

    public static void getInstructionFromFile() throws IOException {

        int[] addressArray = helper.readAddressFile(addressFilePath);
       // System.out.println(addressArray[0]);

        List<String> dataStringList = helper.readInstructionFile(instructionFilePath);
        System.out.println("data file size = "+ dataStringList.size());

//        ArrayList instructionArrayList = new ArrayList();
        List<Instruction> instructionArrayList = new ArrayList<>();

        for (int i = 0; i < dataStringList.size(); i ++){
            String newInstruction = dataStringList.get(i);
            Instruction instruction = new Instruction(newInstruction);
            instruction.addressValue = addressArray[i];

            if (i != (dataStringList.size() - 1 )){
                instruction.nextAddressValue = addressArray[i + 1];
                instruction.setIsBranch(instruction.typeCode, instruction.addressValue, instruction.nextAddressValue);
            } else {
                instruction.nextAddressValue = 12296;
                instruction.isTakenBranch = false;
            }

            instructionArrayList.add(instruction);
//            System.out.println("============================================="+"\n" + "Instruction: "+newInstruction+"\n" + "instCode : "+instruction.binaryCode+"\n"+ "opcode : " + instruction.opcode + "\n"+
//                    "Rs1："+instruction.rs1+"\n"+"Rs2: "+instruction.rs2+"\n"+"Rd: "+instruction.rd+"\n"
//                    +"Type: "+instruction.type+"\n" +"TypeCode: "+instruction.typeCode+"\n"
//                    +"isBranch: "+instruction.isTakenBranch+"\n"+"nextAddressValue: "+instruction.nextAddressValue+"\n"
//                    +"============================================="+"\n");

        }
        System.out.println(
                "Branch_Number      : " + numberOfBranch + "  " + (double)numberOfBranch / 123657 + "  \n"
                + "R_ALU_Number       : " + numberOfR_ALU+ "  " + (double)numberOfR_ALU / 123657 + "\n"
                + "I_ALU_Number       : " + numberOfI_ALU + "  " + (double)numberOfI_ALU / 123657 + "\n"
                + "Total ALU Number  : " + String.valueOf(numberOfI_ALU + numberOfR_ALU) + "  " + (double)(numberOfI_ALU + numberOfR_ALU) / 123657+"\n"
                + "LW_Number          : " + numberOfLw + "  " + (double)numberOfLw / 123657 + "\n"
                + "UL_WNumber         : " + numberOfULw+"   " + (double)numberOfULw/ 123657 + "\n"
                + "JAL_Number         : " + numberOfJAL +"   " + (double)numberOfJAL/ 123657 + "\n"
                + "JALR_Number        : " + numberOfJALR+"   " + (double)numberOfJALR/ 123657 + "\n"
                + "sw_Number          : " + numberOfSW+"  " + (double)numberOfSW/ 123657 + "\n"
                + "Nop_Number         : " + numberOfNop+"   " + (double)numberOfNop/ 123657 + "\n"
                + "TakenBranch_Number : " + numberOfTakenBranch+"      " + (double)numberOfBranch / 123657 + "\n"
                + "instruction cache accesses: " +String.valueOf (123657 + numberOfJAL + numberOfJALR /*taken branch*/)+"     \n");//taken branch 个数未定，睡醒讨论

        Instruction testInstruction = instructionArrayList.get(0);

//        System.out.println("instructionArrayList test item : "+testInstruction.hexCode);



        //String dataStringFromFile = helper.readTXTFile(instructionFilePath);
//
//        byte[] instbyte = helper.hexStringToByte(dataStringFromFile);
//        int[][] instBits = helper.getBits(instbyte, 20, 32);
//        //int[][] instBitsReversed = reverseIntArray(instBits);
//        char[][] instBitsChar = new char[instBits.length][instBits[0].length]; //row=20;column=32
//
//        for (int i = 0; i < instBitsChar.length; i++) {
//            for (int j = 0; j < instBits[0].length; j++) {
//                instBitsChar[i][j] = (char) (instBits[i][j] + '0');
//                //   System.out.print(addBitsChar[i][j]);
//            }
//            // System.out.println(addBitsChar[i]);
//        }

//        String newInstruction = dataStringList.get(2);
//        System.out.println("non-reversed instruction hex = "+ newInstruction);
//        String newInstrucBinary = helper.hexToBinary(newInstruction);
//        Instruction instruction = new Instruction(newInstrucBinary);


//        System.out.println("Instruction: "+newInstruction+"\n" + "instCode : "+instruction.binaryCode+"\n"+ "opcode : " + instruction.opcode + "\n"+
//                "Rs1："+instruction.rs1+"\n"+"Rs2: "+instruction.rs2+"\n"+"Rd: "+instruction.rd+"\n"
//                +"Type: "+instruction.type+"\n" +"TypeCode: "+instruction.typeCode+"\n");



//        List<String> dataStringList = new List<String>;

//        String opCode = newInstrucBinary.substring(25,32);
        //System.out.println(opCode);
//        parseOpcode(newInstrucBinary,opCode);
    }

//    public static void parseOpcode (String instCode, String opcode) {
//        if (opcode.equals("0000011")==true) { //I type load instructions:
//            String Rs1 = parseRs1From15to19(instCode);
//            String Rs2 = R0;
//            String Rd = parseRdFrom7to11(instCode);
//            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode + "\n"+
//                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
//        }else if (opcode.equals("0010011")==true){ //I type arithmetic instructions:
//            String Rs1 = parseRs1From15to19(instCode);
//            String Rs2 = R0;
//            String Rd = parseRdFrom7to11(instCode);
//            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode + "\n"+
//                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
//        }else if (opcode.equals("0100011")==true){//S type store instructions
//            String Rs1 = parseRs1From15to19(instCode);
//            String Rs2 = parseRs2From20to24(instCode);
//            String Rd = R0;
//            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode + "\n"+
//                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
//        }else if (opcode.equals("1100011")==true | opcode.equals("1110011")==true ){
//            //SB/B type branch instructions or SYSTEM treat as SB type branch instructions
//            String Rs1 = parseRs1From15to19(instCode);
//            String Rs2 = parseRs2From20to24(instCode);
//            String Rd = R0;
//            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode + "\n"+
//                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
//        }else if (opcode.equals("1100111")==true){//I type jump instructions JALR
//            String Rs1 = parseRs1From15to19(instCode);
//            String Rs2 = R0;
//            String Rd = parseRdFrom7to11(instCode);
//            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode + "\n"+
//                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
//        }else if (opcode.equals("1101111")==true){//J type jump instructions JAL
//            String Rs1 = R0;
//            String Rs2 = R0;
//            String Rd = parseRdFrom7to11(instCode);
//            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode + "\n"+
//                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
//        }else if (opcode.equals("0110111")==true| opcode.equals("0010111")==true){
//                                        //U type load upper immediate instructions
//            String Rs1 = R0;
//            String Rs2 = R0;
//            String Rd = parseRdFrom7to11(instCode);
//            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode + "\n"+
//                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
//        }else if (opcode.equals("0110011")==true){//R type arithmetic instructions
//            String Rs1 = parseRs1From15to19(instCode);
//            String Rs2 = parseRs2From20to24(instCode);
//            String Rd = parseRdFrom7to11(instCode);
//            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode + "\n"+
//                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
//        }else {//treat as nop
//            String Rs1 = R0;
//            String Rs2 = R0;
//            String Rd = R0;
//            System.out.println("instCode : "+instCode+"\n"+ "opcode : " + opcode + "\n"+
//                    "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
//        }
//    }
//
//    public static String parseRs1From15to19(String instCode){
//        String Rs1 = instCode.substring(12,17);
//        return Rs1;
//    }
//
//    public static String parseRs2From20to24(String instCode){
//        String Rs2 = instCode.substring(7,12);
//        return Rs2;
//    }
//
//    public static String parseRdFrom7to11(String instCode){
//        String Rd = instCode.substring(20,25);
//        return Rd;
//    }

//    public static int[][] reverseIntArray(int[][] intArray){
//        for(int i = 0;i < intArray.length; i ++){
//            reverseArray(intArray[i], 0, intArray[0].length - 1);
//        }
//        return intArray;
//    }

//    public static void swap(int[] A, int a, int b){
//        int temp;
//        temp = A[a];
//        A[a] = A[b];
//        A[b] = temp;
//    }
//
//    public static void reverseArray(int A[], int start, int end){
//
//        if(start == end || start > end){
//            return;
//        }
//        if(start < end){
//            swap(A,start, end);
//        }
//        reverseArray(A, ++start, --end);
//    }
}


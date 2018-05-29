public class Instruction {

    static final String R0 = "00000";

    public String hexCode;
    public String binaryCode;
//    public String address;
    public int addressValue;
//    public String nextAddress;
    public int nextAddressValue;

    public int timeStamp;

    public String type;
    public int typeCode; // 0: 1: 2: 3: 4: 5: 6: 7:
    public String opcode;
    public String rs1;
    public String rs2;
    public String rd;

    public boolean isTakenBranch; //只代表不连续的地址 不是真正的taken branch的数量

    public Instruction(String hexCode)
    {
        this.hexCode = hexCode;
        this.binaryCode = helper.hexToBinary(hexCode);
        parseOpcode(binaryCode);
    }

    public void displayInstruction() {

    }

    public void setIsBranch(int typeCode, int addressValue, int nextAddressValue) {
        if (nextAddressValue - addressValue != 4){//去掉了this,如果是this就不会调用形参了
            isTakenBranch = true;
            if (3 == typeCode){
                    Decoder.numberOfTakenBranch++;
            }
        } else {
            isTakenBranch = false;
        }

        //last one 0010011 I-ALU
    }

    private void parseOpcode (String instCode) {
        String opcode = instCode.substring(25,32);
        if (opcode.equals("0000011")) { //I type load instructions:
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = R0;
            String Rd = parseRdFrom7to11(instCode);
            setInstruction(Rs1,Rs2,Rd, opcode, "I-Load",0);
            Decoder.numberOfLw++;
        }else if (opcode.equals("0010011")){ //I type arithmetic instructions:
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = R0;
            String Rd = parseRdFrom7to11(instCode);
            setInstruction(Rs1,Rs2,Rd, opcode, "I-ALU",1);
            Decoder.numberOfI_ALU++;
        }else if (opcode.equals("0100011")){//S type store instructions
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = parseRs2From20to24(instCode);
            String Rd = R0;
            setInstruction(Rs1,Rs2,Rd, opcode, "S-SW",2);
            Decoder.numberOfSW++;
        }else if (opcode.equals("1100011")|| opcode.equals("1110011") ){
            //SB/B type branch instructions or SYSTEM treat as SB type branch instructions
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = parseRs2From20to24(instCode);
            String Rd = R0;
            setInstruction(Rs1,Rs2,Rd, opcode, "B-branch",3);
            Decoder.numberOfBranch++;

        }else if (opcode.equals("1100111")){//I type jump instructions JALR
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = R0;
            String Rd = parseRdFrom7to11(instCode);
            setInstruction(Rs1,Rs2,Rd, opcode, "I-JALR",4);
            Decoder.numberOfJALR++;
        }else if (opcode.equals("1101111")){//J type jump instructions JAL
            String Rs1 = R0;
            String Rs2 = R0;
            String Rd = parseRdFrom7to11(instCode);
            setInstruction(Rs1,Rs2,Rd, opcode, "J-JAL",5);
            Decoder.numberOfJAL++;
        }else if (opcode.equals("0110111")|| opcode.equals("0010111")){
            //U type load upper immediate instructions
            String Rs1 = R0;
            String Rs2 = R0;
            String Rd = parseRdFrom7to11(instCode);
            setInstruction(Rs1,Rs2,Rd, opcode, "U-LW",6);
            Decoder.numberOfULw++;
        }else if (opcode.equals("0110011")){//R type arithmetic instructions
            String Rs1 = parseRs1From15to19(instCode);
            String Rs2 = parseRs2From20to24(instCode);
            String Rd = parseRdFrom7to11(instCode);

            setInstruction(Rs1,Rs2,Rd, opcode, "R-ALU",7);
            Decoder.numberOfR_ALU++;
        }else {//treat as nop
            String Rs1 = R0;
            String Rs2 = R0;
            String Rd = R0;
            setInstruction(Rs1,Rs2,Rd, opcode, "NOP",8);
            Decoder.numberOfNop++;
        }
    }

    private void setInstruction(String Rs1, String Rs2, String Rd, String opcode, String typeString,
                                int typeCode){
        this.rs1 = Rs1;
        this.rs2 = Rs2;
        this.rd = Rd;
        this.opcode = opcode;
        this.type = typeString;
        this.typeCode = typeCode;
//        System.out.println("instCode : "+this.binaryCode+"\n"+ "opcode : " + opcode + "\n"+
//                "Rs1："+Rs1+"\n"+"Rs2: "+Rs2+"\n"+"Rd: "+Rd+"\n");
    }

    private String parseRs1From15to19(String instCode){
        String Rs1 = instCode.substring(12,17);
        return Rs1;
    }

    private String parseRs2From20to24(String instCode){
        String Rs2 = instCode.substring(7,12);
        return Rs2;
    }

    private static String parseRdFrom7to11(String instCode){
        String Rd = instCode.substring(20,25);
        return Rd;
    }

}

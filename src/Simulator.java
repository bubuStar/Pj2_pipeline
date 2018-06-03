import java.util.HashMap;
import java.util.List;

public class Simulator {

    static final String R0 = "00000";

    List<Instruction> instructionList;

    public int modeNumberInstCache;
    public int modeNumberDataCache;

    int[][] scoreboard;
    static final int MAX_CYCLE_TIME = 400000;

    int clockCycle;

    int instructionCount;
    int ID_stalledCycles;
    int total_stalledCycles;
    int forwardCount;
    int hitCountInstCache;
    int hitCountDataCache;

    Instruction IF_instruction;
    Instruction ID_instruction;
    Instruction EX_instruction;
    Instruction MEM_instruction;
    Instruction WB_instruction;

    Instruction IF_exinstruction;
    Instruction ID_exinstruction;
    Instruction EX_exinstruction;
    Instruction MEM_exinstruction;
    Instruction WB_exinstruction;

    int IF_time;
    int ID_time;
    int EX_time;
    int MEM_time;
    int WB_time;

    Instruction exStage_IF_instruction;
    Instruction exStage_ID_instruction;
    Instruction exStage_EX_instruction;
    Instruction exStage_MEM_instruction;
    Instruction exStage_WB_instruction;

    boolean forward_ready;
    boolean halt;

    boolean IF_halt;
    boolean ID_halt;
    boolean EX_halt;
    boolean MEM_halt;
    boolean WB_halt;

    Simulator(int cases){//初始化
        switch (cases){
            case 1: modeNumberInstCache = 128;
                modeNumberDataCache = 16; break;
            case 2: modeNumberInstCache = 64;
                modeNumberDataCache = 16;break;
            case 3: modeNumberInstCache = 128;
                modeNumberDataCache = 32;break;
            case 4: System.exit(0);
        }

        halt = false;
        forward_ready = false;

        scoreboard = new int [32][4];
        scoreboard[0][0] = MAX_CYCLE_TIME; //设置R0的SCB
        scoreboard[0][1] = MAX_CYCLE_TIME;
        scoreboard[0][2] = MAX_CYCLE_TIME;
        scoreboard[0][3] = MAX_CYCLE_TIME;
        clockCycle = 1 ;
    }

    public void run(){

        while (!halt ) { //&& clockCycle < 20
            doWBstage();
            doMEMstage();
            doEXstage();
            doIDstage();
            doIFstage();
            clockCycle ++;
        }
        int totalCycles = clockCycle + 3;
        double IPC = (double) 123657 / totalCycles;
        System.out.println("total cycles : "+totalCycles);
        System.out.println("instructionCount ： "  +instructionCount);
        System.out.println("ID_stalledCycles ： "  +ID_stalledCycles);
        System.out.println("total_stalledCycles ： "  +total_stalledCycles);
        System.out.println(" forwardCount ： "  + forwardCount);
        System.out.println(" hit in Inst cache ： "  + hitCountInstCache);
        System.out.println(" hit in Data cache ： "  + hitCountDataCache);
        System.out.println(" hit Ratio of Inst Cache ： "  + (double)hitCountInstCache / Decoder.instCacheAccess);
        System.out.println(" hit Ratio of Data Cache ： "  + (double)hitCountDataCache / Decoder.numberOfLw);
        System.out.println(" IPC ： "  + IPC);
        System.out.println(" Percentage of stalled cycles over total cycles ： "  + (double)total_stalledCycles/totalCycles);
    }

    private void doIFstage(){

        if (clockCycle == 1){
            IF_time = 1 ;
            ID_time = 2;
            EX_time = 3;
            MEM_time = 4;
            WB_time = 5;
            instructionCount = 0;
            this.IF_instruction = this.getNewInstruction(0);
            //this.IF_instruction.timeStamp = 2;
            //System.out.println("IF in cycle : "+clockCycle + "  IF_instruction : "+ IF_instruction.hexCode +" inst_time : "+ IF_instruction.timeStamp+ " IF_time: "+ IF_time);
            instructionCount += 1;
        } else {
            if (clockCycle < IF_time){
                IF_instruction = new Instruction("0018081b"); //let IF_istruction = nop
                IF_instruction.timeStamp = IF_time + 1;
                //System.out.println("IF in cycle : "+clockCycle + "  IF_instruction : "+ IF_instruction.hexCode+" inst_time : "+ IF_instruction.timeStamp + " IF_time: "+ IF_time);

                //        ID_time = IF_time + 1;
                ID_instruction = IF_instruction;
//                IF_time ++;
                return;
            }
            if (instructionCount == instructionList.size()){
                halt = true;
                return;
            }

            this.getNewInstruction(instructionCount);
            this.IF_instruction = this.getNewInstruction(instructionCount);

            //IF judge miss
            boolean instCacheMiss;
            if (55 == clockCycle % modeNumberInstCache){
                instCacheMiss = true;
            } else {
                hitCountInstCache ++;
                instCacheMiss = false;
            }
            if (instCacheMiss){
                IF_time += 15;
                total_stalledCycles += 15;
            }
            instructionCount += 1;
        }
        IF_instruction.timeStamp = IF_time + 1;
//        ID_time = IF_time + 1;
        //System.out.println("IF in cycle : "+clockCycle +  "  IF_instruction : "+ IF_instruction.hexCode +" inst_time : "+ IF_instruction.timeStamp+ " IF_time: "+ IF_time);
        ID_instruction = IF_instruction;
        IF_time ++;
    }

    private void doIDstage(){
        if (clockCycle < 2){
            return;
        }

        if (ID_instruction.timeStamp < ID_time){
            ID_instruction.timeStamp += 1;
            //System.out.println("ID in cycle : "+clockCycle + "  ID_instruction : "+ ID_instruction.hexCode +" inst_time : "+ ID_instruction.timeStamp+ " ID_time: "+ ID_time+ "  stall");
            return;
        }

        else {
            //System.out.println("ID in cycle : "+clockCycle + "  ID_instruction : "+ ID_instruction.hexCode+" inst_time : "+ ID_instruction.timeStamp+ " ID_time: "+ ID_time);
            this.updateScb(ID_instruction.timeStamp,ID_instruction.rd,"ID");



            if (3 == ID_instruction.typeCode & ID_instruction.isTakenBranch){
                //taken branch
                IF_time += 2;
                total_stalledCycles += 2;

                boolean instCacheMiss;
                if (55 == clockCycle % modeNumberInstCache){
                    instCacheMiss = true;
                } else {
                    instCacheMiss = false;
                    hitCountInstCache ++;
                }
                if (instCacheMiss){
                    IF_time += 15;
                    total_stalledCycles += 15;
                }
            }else if (3 == ID_instruction.typeCode & !ID_instruction.isTakenBranch){
                //non-taken branch
                IF_time += 1;
                total_stalledCycles += 1;
            }else if (ID_instruction.typeCode == 4 || ID_instruction.typeCode == 5){
                //JAL + JALR
                IF_time += 1;
                total_stalledCycles ++;

                boolean instCacheMiss;
                if (55 == clockCycle % modeNumberInstCache){
                    instCacheMiss = true;
                } else {
                    hitCountInstCache++;
                    instCacheMiss = false;
                }
                if (instCacheMiss){
                    IF_time += 15;
                    total_stalledCycles += 15;
                }
            } else if (ID_instruction.typeCode == 0){
                //load
                boolean dataCacheMiss;
                if (11 == clockCycle % modeNumberDataCache){
                    dataCacheMiss = true;
                } else {
                    hitCountDataCache ++;
                    dataCacheMiss = false;
                }
                if (dataCacheMiss){
                    total_stalledCycles += 15;
                    ID_stalledCycles += 15;
                    IF_time += 15;
                }
            } else {
                //other types of instructions
            }

            ID_instruction.timeStamp = ID_time + 1;
//            IF_time = ID_time;
//            EX_time = ID_time + 1;
            EX_instruction = ID_instruction;
            ID_time ++;
        }

    }

    private void doEXstage(){
        if (clockCycle < 3) {
            return;
        }
        //System.out.println("EX in cycle : "+clockCycle + "  EX_instruction : "+ EX_instruction.hexCode);

        if (EX_instruction.timeStamp < EX_time){
            EX_instruction.timeStamp += 1;
            return;
        }


        boolean needStall;
        boolean needStallRs1 = readScb(EX_instruction.timeStamp, EX_instruction.rs1);
        boolean needStallRs2 = readScb(EX_instruction.timeStamp, EX_instruction.rs2);
        needStall = needStallRs1 || needStallRs2;

        if (needStall) {
            total_stalledCycles += 1;
            ID_stalledCycles += 1;
            EX_time += 1;
            ID_time += 1;
            IF_time ++;
            EX_instruction.timeStamp += 1;
            return;
        } else {//继续执行


            if (forward_ready) {
                forwardCount ++;
                forward_ready = false;
            }
            this.updateScb(EX_instruction.timeStamp,EX_instruction.rd,"EX");
            EX_instruction.timeStamp = EX_time + 1;
//            MEM_time = EX_time + 1;
            MEM_instruction = EX_instruction;
            EX_time ++;
        }
    }

    private void doMEMstage(){
        if (clockCycle < 4) {
//            MEM_instruction = EX_instruction;
            //System.out.println("MEM in cycle <4 : "+clockCycle + "MEM_instruction : "+ MEM_instruction.hexCode);
            return;
        }
     //   System.out.println("Test clock cycle : " + clockCycle);
       // System.out.println("Test pointer : " + MEM_instruction.timeStamp );


        if (MEM_instruction.timeStamp < MEM_time){
            MEM_instruction.timeStamp += 1;
            return;
        }

        //System.out.println("MEM in cycle : "+clockCycle + "  MEM_instruction : "+ MEM_instruction.hexCode);

        this.updateScb(MEM_instruction.timeStamp,MEM_instruction.rd,"MEM");
        //judge data cache miss

        MEM_instruction.timeStamp = MEM_time + 1;
//        WB_time = MEM_time + 1;
        WB_instruction = MEM_instruction;
        MEM_time ++;
    }

    private void doWBstage(){

        if (clockCycle < 5) {
//            WB_instruction = MEM_instruction;
            return;
        }
        if (WB_instruction.timeStamp < WB_time){
            WB_instruction.timeStamp += 1;
            return;
        }
        //System.out.println("WB in cycle : "+clockCycle + "  WB_instruction : "+ WB_instruction.hexCode);
        this.updateScb(WB_instruction.timeStamp,WB_instruction.rd,"WB");
//        if (WB_instruction.addressValue == 12296){
//            halt = true;
//        }
        WB_time++;
    }

    private Instruction getNewInstruction(int index){
        Instruction instruction = null;
        if(index < 123657)
        instruction = instructionList.get(index);
        return instruction;
    }

    private boolean readScb(int InstructionTimeStamp, String rs){
        int rsInt = Integer.valueOf(rs,2);
        if (rs.equals(R0)){
            return false;
        } else {
            if (InstructionTimeStamp <= scoreboard[rsInt][0]){
                return true;
            }
            else if (InstructionTimeStamp == scoreboard[rsInt][1] || InstructionTimeStamp == scoreboard[rsInt][2]){
                forward_ready = true;
                return false;
            }
            else if (InstructionTimeStamp >= scoreboard[rsInt][3]){
                return false;
            }
            else {
                return false;
            }
        }
    }


    private void updateScb (int InstructionTimeStamp, String rd, String stage){
        int rdInt = Integer.valueOf(rd,2);
        if (rd.equals(R0)) {
            return;
        } else {

            if (stage.equals("ID")){ scoreboard[rdInt][0] = InstructionTimeStamp; }
            else if (stage.equals("EX")){ scoreboard[rdInt][1] = InstructionTimeStamp;}
            else if (stage.equals("MEM")){ scoreboard[rdInt][2] = InstructionTimeStamp;}
            else if (stage.equals("WB")){ scoreboard[rdInt][3] = InstructionTimeStamp;}
            else {return;}
        }
    }


}

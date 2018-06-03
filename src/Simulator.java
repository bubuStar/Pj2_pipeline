import java.util.HashMap;
import java.util.List;

public class Simulator {

    static final String R0 = "00000";

    List<Instruction> instructionList;

    public int modeNumber;

    int[][] scoreboard;
    static final int MAX_CYCLE_TIME = 400000;

    int clockCycle;

    int instructionCount;
    int ID_stalledCycles;
    int total_stalledCycles;
    int forwardCount;

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

    Simulator(){//初始化
        scoreboard = new int [32][4];
        scoreboard[0][0] = MAX_CYCLE_TIME; //设置R0的SCB
        scoreboard[0][1] = MAX_CYCLE_TIME;
        scoreboard[0][2] = MAX_CYCLE_TIME;
        scoreboard[0][3] = MAX_CYCLE_TIME;
        clockCycle = 0 ;
    }

    private void run(int clockCycle){

        doWBstage();
        doMEMstage();
        doEXstage();
        doIDstage();
        doIFstage();

    }

    private void doIFstage(){
        if (clockCycle = 0){
            IF_time = 1 ;
            instructionCount = 0;
            this.IF_instruction = this.getNewInstruction(0);
            //TODO: IF judde miss
            this.IF_instruction.timeStamp = 2;
            instructionCount += 1;
        } else {
            this.getNewInstruction(instructionCount);
            this.IF_instruction = this.getNewInstruction(instructionCount);
            //TODO: IF judde miss
            this.IF_instruction.timeStamp = IF_time + 1;
            instructionCount += 1;
        }
        ID_time = IF_time + 1;
        ID_instruction = IF_instruction;
        IF_time += 1;
    }

    private void doIDstage(){
        if (clockCycle < 2){
            return;
        }else {
            if (ID_instruction.timeStamp < ID_time){
                ID_instruction.timeStamp += 1;
                return;
            }
            else {
                this.updateScb(ID_instruction.timeStamp,ID_instruction.rd,"ID");
                MEM_time = ID_time + 1;
                MEM_instruction = ID_instruction;
                ID_time += 1;
            }
        }
    }

    private void doEXstage(){
        //TODO: compare Timestamp
        boolean needStall;
        boolean needStallRs1 = readScb(EX_instruction.timeStamp, EX_instruction.rs1);
        boolean needStallRs2 = readScb(EX_instruction.timeStamp, EX_instruction.rs2);
        needStall = needStallRs1 || needStallRs2;
        if (needStall) {
            total_stalledCycles += 1;
            ID_stalledCycles += 1;
            EX_time += 1;
        } else {//继续执行
            this.updateScb(EX_instruction.timeStamp,EX_instruction.rd,"EX");
            if (forward_ready) {
                forwardCount += 1;
                forward_ready = false;
            }
            if (3 == EX_instruction.typeCode & EX_instruction.isTakenBranch){
                //taken branch
                IF_time += 2;
                total_stalledCycles += 1;
                //TODO: IF judde miss

            }else if (3 == EX_instruction.typeCode & !EX_instruction.isTakenBranch){
                //non-taken branch
                IF_time += 1;
                total_stalledCycles += 1;
            }else if (EX_instruction.typeCode == 4 || EX_instruction.isTakenBranch == 5){
                //JAL + JALR
                IF_time += 1;
                //TODO: IF judde miss
            } else {
            }
            MEM_time = EX_time + 1;
            MEM_instruction = EX_instruction;
            MEM_time += 1;
        }
    }

    private void doMEMstage(){
        this.updateScb(MEM_instruction.timeStamp,MEM_instruction.rd,"MEM");
        //TODO: judge data cache miss
        
    }

    private void doWBstage(){
        this.updateScb(WB_instruction.timeStamp,WB_instruction.rd,"WB");
    }

    private Instruction getNewInstruction(int index){
        Instruction instruction = instructionList.get(index);
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
            //TODO: 用Inst_time更新scb对不对?
            if (stage.equals("ID")){ scoreboard[rdInt][0] = InstructionTimeStamp; }
            else if (stage.equals("EX")){ scoreboard[rdInt][1] = InstructionTimeStamp;}
            else if (stage.equals("MEM")){ scoreboard[rdInt][2] = InstructionTimeStamp;}
            else if (stage.equals("WB")){ scoreboard[rdInt][3] = InstructionTimeStamp;}
            else {return;}
        }
    }


}

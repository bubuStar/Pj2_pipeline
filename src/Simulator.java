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
            this.IF_instruction.timeStamp = 2;
            instructionCount += 1;
        } else {
            this.getNewInstruction(instructionCount);
            this.IF_instruction = this.getNewInstruction(instructionCount);
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
                MEM_time = ID_time + 1;
                MEM_instruction = ID_instruction;
                ID_time += 1;
            }
        }
    }

    private void doEXstage(){

        boolean needStall;
        boolean needStallRs1 = readScb(EX_instruction.timeStamp, EX_instruction.rs1);
        boolean needStallRs2 = readScb(EX_instruction.timeStamp, EX_instruction.rs2);
        if (needStall) {
            total_stalledCycles += 1;

        }

    }

    private void doMEMstage(){

    }

    private void doWBstage(){

    }

    private Instruction getNewInstruction(int index){
        Instruction instruction = instructionList.get(index);
        return instruction;
    }

    private boolean readScb(int InstructionTimeStamp, String rs){
        int rsInt = Integer.valueOf(rs,2);
        if (rs == R0){
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
        if (rd == R0) {
            return;
        } else {
            if (stage == "ID"){ scoreboard[rdInt][0] = InstructionTimeStamp; }
            else if (stage == "EX"){ scoreboard[rdInt][1] = InstructionTimeStamp;}
            else if (stage == "MEM"){ scoreboard[rdInt][2] = InstructionTimeStamp;}
            else if (stage == "WB"){ scoreboard[rdInt][3] = InstructionTimeStamp;}
            else {return;}
        }
    }


}

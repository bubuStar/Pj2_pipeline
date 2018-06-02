import java.util.HashMap;

public class Simulator {

    static final String R0 = "00000";

    public int modeNumber;

    int[][] scoreboard;
    static final int MAX_CYCLE_TIME = 400000;
    int clockCycle;

    Instruction current_IF_instruction;
    Instruction current_ID_instruction;
    Instruction current_EX_instruction;
    Instruction current_MEM_instruction;
    Instruction current_WB_instruction;

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

    }

    private void doIDstage(){

    }

    private void doEXstage(){

    }

    private void doMEMstage(){

    }

    private void doWBstage(){

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


    private void writeScb (int InstructionTimeStamp, String rd, String stage){
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

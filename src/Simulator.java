import java.util.HashMap;
import java.util.List;

public class Simulator {

    static final String R0 = "00000";

    static List<Instruction> instructionList;

    public int modeNumber;

    int[][] scoreboard;
    static final int MAX_CYCLE_TIME = 400000;

    int clockCycle;

    int instructionCount = 0;


    Instruction IF_instruction;
    Instruction ID_instruction;
    Instruction EX_instruction;
    Instruction MEM_instruction;
    Instruction WB_instruction;

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
            instructionCount = 0;
         this.getNewInstruction(0);
         instructionCount += 1;
        } else {
            this.getNewInstruction(instructionCount);
            instructionCount += 1;
        }

    }

    private void doIDstage(){

    }

    private void doEXstage(){

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

import java.util.HashMap;

public class Simulator {

    int[][] scoreboard;
    static final int MAX_CYCLE_TIME = 400000;
    int clockCycle;
    Instruction current_EX_instruction;


    Simulator(){//初始化
        scoreboard = new int [32][4];
        scoreboard[0][0] = MAX_CYCLE_TIME; //设置R0的SCB
        scoreboard[0][1] = MAX_CYCLE_TIME;
        scoreboard[0][2] = MAX_CYCLE_TIME;
        scoreboard[0][3] = MAX_CYCLE_TIME;
        clockCycle = 0 ;
    }

    private void readScb(int timeStamp, String rs1, String rs2){//r1  r2是传入的寄存器
        int r1 = Integer.valueOf(rs1,2);
        int r2 = Integer.valueOf(rs2,2);
        if(scoreboard[r1][1] > timeStamp && scoreboard[r2][1] > timeStamp){
            current_EX_instruction.timeStamp = Math.max(scoreboard[r1][1],scoreboard[r2][1]);
        }
        else if(scoreboard[r1][2] > timeStamp && scoreboard[r2][2] > timeStamp){
            current_EX_instruction.timeStamp = Math.max(scoreboard[r1][2],scoreboard[r2][2]);
        }
        else if(scoreboard[r1][0] > timeStamp && scoreboard[r2][0] > timeStamp){
            current_EX_instruction.timeStamp = Math.max(scoreboard[r1][0],scoreboard[r2][0]);
        }
    }
}

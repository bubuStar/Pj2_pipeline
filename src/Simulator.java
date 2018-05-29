public class Simulator {

    int[][] scoreboard;
    int clockCycle;

    Simulator(){//初始化
        scoreboard = new int [32][4];
        clockCycle = 0 ;
    }
    private void readScb(int r1, int r2){//r1  r2是传入的寄存器
        if(scoreboard[r1][1] > clockCycle && scoreboard[r2][1] > clockCycle){
            clockCycle = Math.max(scoreboard[r1][1],scoreboard[r2][1]);
        }
        else if(scoreboard[r1][2] > clockCycle && scoreboard[r2][2] > clockCycle){
            clockCycle = Math.max(scoreboard[r1][2],scoreboard[r2][2]);
        }
        else if(scoreboard[r1][0] > clockCycle && scoreboard[r2][0] > clockCycle){
            clockCycle = Math.max(scoreboard[r1][0],scoreboard[r2][0]);
        }
    }
}

import java.io.*;
import java.util.*;

public class helper {
//this is a test for github 1:00 am 29th,May


    public static String hexToBinary(String instr){
        int[] map = {10, 11, 12, 13, 14, 15};
        char[] chs = instr.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c : chs){
            int i = c - '0';
            if(c-'a' >=0 && c-'a' <= 5){
                i = map[c-'a'];
            }
            String a = Integer.toBinaryString(i);
            int n = a.length();
            while(n < 4){
                sb.append("0");
                n++;
            }
            sb.append(a);
        }
        return sb.toString();
    }

    public static List<String> readInstructionFile(String filePath) throws IOException {
        List<String> res = new ArrayList<>();     //hashMap
        FileReader f = new FileReader(filePath);
        BufferedReader br = new BufferedReader(f);
        String line = ""; // 用来保存每行读取的内容
        //line = reader.readLine(); // 读取第一行
        while ((line = br.readLine()) != null) {
            res.add(line);
        }
        br.close();
        return res;
    }

    public List<String> ReadNonSizeFile(File f) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(f));
        List<String> ret = new ArrayList<>();
        String s = "";
        while((s = br.readLine()) != null){
            ret.add(s);
        }
        br.close();
        return ret;
    }


    public static byte[] hexStringToByte(String hex) {//hex String to byte, output decimal value of the byte if print
        byte[] b = new byte[hex.length() / 2];
        //  System.out.println(hex.length());
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hex.charAt(j++);
            char c1 = hex.charAt(j++);
            b[i] = (byte)((parse(c0) << 4) | parse(c1));
        //        System.out.print(b[i]+" ");
        }
        return b;
    }

    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }


    public static String reverseString(String inputString){
        String res = "";
        for(int i = 0;i < 20; i ++){
            StringBuilder sb = new StringBuilder(inputString.substring( i * 8 , (i + 1) * 8));
            res += sb.reverse().toString();
           //    System.out.println(sb.toString());
        }
        return res ;
    }

    public static byte [][] ArrayToBytesMatrix(byte[] bytes){
        byte [][] res = new byte [20][8];
        int count = 0;
        for(int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                res[i][j] = bytes[count++];
            //     System.out.print(res[i][j] +" ");
            }
            //System.out.println();
        }
        return res;
    }


    public static int getUnsignedByte (byte data){      //将data字节型数据转换为0~255 (0xFF 即BYTE)。
        return data&0x0FF ;
    }

    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            //    buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }

    public static String readTXTFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        helper.readToBuffer(sb, filePath);
        return sb.toString();
    }

    public static int [][] getBits(byte[] bytes, int row, int column){
        int [][] bits = new int[row][column];
        int count = 0;
        int carry = 0;
        for(int i = 0 ;i < row; i++){
            // System.out.print(i+" line :");

            for(int j = 0 ; j < column ;j++){

                if(carry == 8) {
                    count++;
                    carry = 0;
                }
                if(carry == 0)
                {
                    bits[i][j] = (bytes[count] & 0x80) == 0x80 ?1:0;
                }
                else
                    bits[i][j] = (bytes[count] & (byte)(Math.pow(2,7-carry) )) == Math.pow(2,7-carry) ?1:0;
                carry ++;
            //    System.out.print(bits[i][j]);
            }
            //   System.out.println();

        }
        // System.out.println(bits);
        return bits;
    }

    public static int [] readAddressFile(String filePath) throws IOException {
        int [] res = new int[123657];
        StringBuffer sb = new StringBuffer();
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        int l = 0;
        while (line != null) { // 如果 line 为空说明读完了
            sb.append(line); // 将读到的内容添加到 buffer 中
            res[l] = Integer.valueOf(sb.toString(),16);
            l++;
            sb = new StringBuffer();
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        return res;
    }


}

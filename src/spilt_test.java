import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
public class spilt_test {
    public static void main(String[] args) {
        String str="i love you";
        String[] str_ary=str.split(" ");
        ArrayList<String> str_arylist=new ArrayList<>(Arrays.asList(str.split(" ")));

        System.out.println(str_arylist.get(1));
        System.out.println(str_ary[1]);

        String num="day12";
        Integer inter=Integer.valueOf(num.substring(3,num.length()))-1;
        System.out.println(inter+3);
        // for(inter++;inter<=15;inter++){  //why need ++??
        //     System.out.println(inter);
        // }

        String num2="8.0800";
        BigDecimal value = new BigDecimal(num2);
        System.out.println(value.stripTrailingZeros().toString());
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RegExp {
    
    public static void main(String[] args) {
        String str1 = args[1];
        String str2 = args[2];
        int s2Count = Integer.parseInt(args[3]);

        //For your testing of input correctness
        System.out.println("The input file:"+args[0]);
        System.out.println("str1="+str1);
        System.out.println("str2="+str2);
        System.out.println("num of repeated requests of str2 = "+s2Count);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String line;
            String upper_result;
            String upper_str1;
            int row=1;
            while ((line = reader.readLine()) != null) {
                upper_result=line.toUpperCase();
                System.out.print(row);
                //System.out.println(upper_result);
                //conditon 1
                //System.out.println("length"+line.length());
                StringBuilder front_sec=new StringBuilder();
                StringBuilder back_sec=new StringBuilder();
                if(upper_result.length()%2==0){
                    front_sec.append(upper_result.substring(0,upper_result.length()/2));
                    back_sec.append(upper_result.substring(upper_result.length()/2, upper_result.length()));
                    if(front_sec.toString().equals(back_sec.reverse().toString())){
                        System.out.print("Y,");
                    }
                    else{
                        System.out.print("N,");
                    }
                }
                else if(upper_result.length()%2==1){
                    front_sec.append(upper_result.substring(0,upper_result.length()/2));
                    back_sec.append(upper_result.substring(upper_result.length()/2+1, upper_result.length()));
                    back_sec.reverse();
                    if(front_sec.toString().equals(back_sec.toString())){
                        System.out.print("Y,");
                    }
                    else{
                        System.out.print("N,");
                    }
                }
                //condition 2
                upper_str1=str1.toUpperCase();
                if(upper_result.length()<str1.length())
                {
                    System.out.print("N,");
                }
                else
                {
                    for(int i_2=0;i_2<=upper_result.length()-str1.length();i_2++){
                        if(upper_result.charAt(i_2)==upper_str1.charAt(0)){
                            if(upper_result.substring(i_2,i_2+str1.length()).equals(upper_str1)){
                                System.out.print("Y,");
                                break;
                            }
    
                        }
                        if(i_2==upper_result.length()-str1.length()){
                            System.out.print("N,");
                        }
                    
                    }

                }
               

                //condition 3

                //condition 4
                row++;
                System.out.println();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

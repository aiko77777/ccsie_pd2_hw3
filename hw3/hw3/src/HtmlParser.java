
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
public class HtmlParser{
    static ArrayList<String> simple_moving_avg(ArrayList<String> values){
        ArrayList<String> output_values=new ArrayList<>();
        for(int start_day=0;start_day<=values.size()-5;start_day++){
            double sum=0;
            for(int i =0;i<5;i++){
                sum+=Double.valueOf(values.get(i+start_day));
            }
            BigDecimal result = new BigDecimal(sum/5.0);
            output_values.add(start_day,result.setScale(2,RoundingMode.HALF_UP).stripTrailingZeros().toString());
        }
        return output_values;
    }
    public static double squareRoot(double value)   {  
        //temporary variable  
        double t;  
        double sqrtroot=value/2;  
        do{  
        t=sqrtroot;  
        sqrtroot=(t+(value/t))/2;  
        }while((t-sqrtroot)!= 0);
    
        return sqrtroot;  
        }
    public static double average(ArrayList<String> values){
        double sum=0;
        for(int i=0;i<values.size();i++){
            sum+=Double.valueOf(values.get(i));
        }
    
        return sum/Double.valueOf(values.size());
    }

    public static String standard_Deviation(ArrayList<String> values){
        double sum_of_Squared_Deviations=0;
        for(int i=0;i<values.size();i++){
            sum_of_Squared_Deviations+=(Double.valueOf(values.get(i))-average(values))*(Double.valueOf(values.get(i))-average(values));
        }
        BigDecimal result = new BigDecimal(squareRoot(sum_of_Squared_Deviations/(values.size()-1)));
        return result.setScale(2,RoundingMode.HALF_UP).toString();
        
    }


    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://pd2-hw3.netdb.csie.ncku.edu.tw/").get();
            //System.out.println(doc.title());
            //System.out.println(doc.body().select("tbody").text());

//input "0":read the <th> and <td> elements from the web
//if there is no data.csv exists creat a data.csv and write in,
//else,next line and continue writing in with value lines.
            int name_count=0;
            int value_count=0;
            if (args[0].equals("0")){
                File file=new File("./data.csv");
                Elements stock_name_lines = doc.body().select("th");
                Elements value_lines=doc.body().select("td");
                int name_size=stock_name_lines.size();
                
                if(!file.exists()){
                    PrintWriter writer = new PrintWriter(new File("data.csv"));
                    for (Element stock_name_line : stock_name_lines) {//write stock_names
                    //System.out.println(stock_name_line.text());
                        name_count++;
                        writer.append(stock_name_line.text());
                        if(name_count!=name_size){
                            writer.append(",");
                        }
                        
                    }
                    
                    writer.append("\n");
                    writer.append(doc.title());
                    writer.append("\n");
                    for (Element value_line : value_lines) {    //write values and strip the excess zeros behide "."
                    //System.out.println(value_line.text());
                        value_count++;
                        BigDecimal value = new BigDecimal(value_line.text());
                        writer.append(value.stripTrailingZeros().toString());//STRIP ZERO!!
                        if(value_count!=name_size){
                            writer.append(",");

                        }
                    }
                    writer.append("\n");
                    writer.close();
                    }
                    else{
                    Path filePath = Paths.get("data.csv");
                    Files.write(filePath,doc.title().getBytes(), StandardOpenOption.APPEND);
                    Files.write(filePath,"\n".getBytes(), StandardOpenOption.APPEND);
                    for (Element value_line : value_lines) {    //if data.csv has been existed,we need to just write values to new line.
                        //System.out.println(value_line.text());
                        value_count++;
                        BigDecimal value = new BigDecimal(value_line.text());
                        Files.write(filePath,value.stripTrailingZeros().toString().getBytes(), StandardOpenOption.APPEND);//STRIP ZERO!!
                        if(value_count!=name_size){
                            Files.write(filePath,",".getBytes(), StandardOpenOption.APPEND);
                        }
                        }
                    Files.write(filePath,"\n".getBytes(), StandardOpenOption.APPEND);
                    }
            }
//if input are {1} {0},read the lines in current data.csv and write in output.csv 
            else if(args[0].equals("1") && args[1].equals("0")){
                PrintWriter writer = new PrintWriter(new File("output.csv"));
                BufferedReader reader=new BufferedReader(new FileReader("data.csv"));
                String data_line=null;
                while ((data_line=reader.readLine())!= null) {
                    if(!data_line.contains("day")){
                        writer.append(data_line);
                        writer.append("\n");
                    }
                    
                }
                reader.close();
                writer.close();
            }
            else if(args[0].equals("1")&& args[1].equals("1")){
                BufferedReader reader=new BufferedReader(new FileReader("data.csv"));
                String data_line=null; 
                int row=1;                
                int stock_posi=0;
                ArrayList<String> spec_value=new ArrayList<>();
                ArrayList<String> input_days=new ArrayList<>();
                
//append the stock names and values in Arraylist so that we can find the specific stock and its value we want.
                while((data_line=reader.readLine())!=null){
                    if(row==1){
                        ArrayList<String> stock_names_array=new ArrayList<String>(Arrays.asList(data_line.split(",")));
                        //find the position of args[2]
                        stock_posi=stock_names_array.indexOf(args[2]);
                    }
                    else{
                        if(data_line.contains("day")){
                            input_days.add(data_line.substring(3,data_line.length()));
                        }
                        else{
                            ArrayList<String> value_array=new ArrayList<>(Arrays.asList(data_line.split(",")));
                            spec_value.add(value_array.get(stock_posi));
                        }   
                    }
                    row++;
                }
                
                System.out.println(spec_value);

                ArrayList<String> spec_value2=new ArrayList<>();
                System.out.println(Integer.valueOf(args[3])-1);
                System.out.println(Integer.valueOf(args[4]));
                for(int i=Integer.valueOf(args[3])-1;i<Integer.valueOf(args[4]);i++){
                    spec_value2.add(spec_value.get(i));
                }
                File file =new File("./output.csv");
                if(!file.exists()){
                    PrintWriter writer = new PrintWriter(new File("output.csv"));
//when {1} {1} we only need:
//args[2],args[3],args[4] 
//values    
                    writer.append(args[2]);
                    writer.append(",");
                    writer.append(args[3]);
                    writer.append(",");
                    writer.append(args[4]);
                    writer.append("\n");
                    for(int count=0;count<simple_moving_avg(spec_value2).size();count++){ //put in svm!!
                        writer.append(simple_moving_avg(spec_value2).get(count));
                        value_count++;
                        if(count!=simple_moving_avg(spec_value2).size()-1){
                            writer.append(",");

                        }
                    }
                    writer.append("\n");
                    writer.close();
                }
                else{
                    Path filePath = Paths.get("output.csv");
                    Files.write(filePath,args[2].getBytes(),StandardOpenOption.APPEND);
                    Files.write(filePath,",".getBytes(),StandardOpenOption.APPEND);
                    Files.write(filePath,args[3].getBytes(),StandardOpenOption.APPEND);
                    Files.write(filePath,",".getBytes(),StandardOpenOption.APPEND);
                    Files.write(filePath,args[4].getBytes(),StandardOpenOption.APPEND);
                    Files.write(filePath,"\n".getBytes(), StandardOpenOption.APPEND);
                    for(int count=0;count<simple_moving_avg(spec_value2).size();count++){
                        Files.write(filePath,simple_moving_avg(spec_value2).get(count).getBytes(),StandardOpenOption.APPEND);
                        if(count!=simple_moving_avg(spec_value2).size()-1){
                            Files.write(filePath,",".getBytes(),StandardOpenOption.APPEND);
                        }
                    }
                    Files.write(filePath,"\n".getBytes(), StandardOpenOption.APPEND);
                }
                //System.out.println(value_lines.get(1).text()); //Q:why can we use get?
                
            }
            else if(args[0].equals("1")&& args[1].equals("2")){
                BufferedReader reader=new BufferedReader(new FileReader("data.csv"));
                String data_line=null; 
                int row=1;                
                int stock_posi=0;
                ArrayList<String> spec_value=new ArrayList<>();                
//append the stock names and values in Arraylist so that we can find the specific stock and its value we want.
                while((data_line=reader.readLine())!=null){
                    if(row==1){
                        ArrayList<String> stock_names_array=new ArrayList<String>(Arrays.asList(data_line.split(",")));
                        //find the position of args[2]
                        stock_posi=stock_names_array.indexOf(args[2]);
                    }
                    else{
                        if(data_line.contains("day")){
                                ;                        }
                        else{
                            ArrayList<String> value_array=new ArrayList<>(Arrays.asList(data_line.split(",")));
                            spec_value.add(value_array.get(stock_posi));
                        }   
                    }
                    row++;
                }
                System.out.println(spec_value);
                ArrayList<String> spec_value2=new ArrayList<>();
                System.out.println(Integer.valueOf(args[3])-1);
                System.out.println(Integer.valueOf(args[4]));
                for(int i=Integer.valueOf(args[3])-1;i<Integer.valueOf(args[4]);i++){
                    spec_value2.add(spec_value.get(i));
                }

                File file =new File("./output.csv");
                if(!file.exists()){
                    PrintWriter writer = new PrintWriter(new File("output.csv"));
                    writer.append(args[2]);
                    writer.append(",");
                    writer.append(args[3]);
                    writer.append(",");
                    writer.append(args[4]);
                    writer.append("\n");
                    
                    writer.append(standard_Deviation(spec_value2));
                    writer.append("\n");
                    writer.close();

                }
                else{
                    Path filePath = Paths.get("output.csv");
                    Files.write(filePath,args[2].getBytes(),StandardOpenOption.APPEND);
                    Files.write(filePath,",".getBytes(),StandardOpenOption.APPEND);
                    Files.write(filePath,args[3].getBytes(),StandardOpenOption.APPEND);
                    Files.write(filePath,",".getBytes(),StandardOpenOption.APPEND);
                    Files.write(filePath,args[4].getBytes(),StandardOpenOption.APPEND);
                    Files.write(filePath,"\n".getBytes(), StandardOpenOption.APPEND);
                    
                    Files.write(filePath,standard_Deviation(spec_value2).getBytes(), StandardOpenOption.APPEND);
                    Files.write(filePath,"\n".getBytes(), StandardOpenOption.APPEND);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
// JsoupExample.java

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
public class HtmlParser{
    
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://pd2-hw3.netdb.csie.ncku.edu.tw/").get();
            //System.out.println(doc.title());
            //System.out.println(doc.body().select("tbody").text());

//input "0":read the <th> and <td> elements from the web
//if there is no data.csv exists creat a data.csv and write in,
//else,next line and continue writing in with value lines.
            if (args[0].equals("0")){
                File file=new File("./data.csv");
                Elements stock_name_lines = doc.body().select("th");
                Elements value_lines=doc.body().select("td");
                if(!file.exists()){
                    PrintWriter writer = new PrintWriter(new File("data.csv"));
                    for (Element stock_name_line : stock_name_lines) {//write stock_names
                    //System.out.println(stock_name_line.text());
                        writer.append(stock_name_line.text());
                        writer.append(",");
                    }
                    writer.append("\n");
                    writer.append(doc.title());
                    writer.append("\n");
                    for (Element value_line : value_lines) {    //write values and strip the excess zeros behide "."
                    //System.out.println(value_line.text());
                        BigDecimal value = new BigDecimal(value_line.text());
                        writer.append(value.stripTrailingZeros().toString());
                        writer.append(",");
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
                        BigDecimal value = new BigDecimal(value_line.text());
                        Files.write(filePath,value.stripTrailingZeros().toString().getBytes(), StandardOpenOption.APPEND);
                        Files.write(filePath,",".getBytes(), StandardOpenOption.APPEND);
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
                ArrayList<Integer> needed_days=new ArrayList<>();
                ArrayList<String> input_days=new ArrayList<>();

                Integer day_begin=Integer.valueOf(args[3])-1;   //modification because there is day_begin++ in for loop
                Integer day_end=Integer.valueOf(args[4]);
                for(day_begin++;day_begin<=day_end;day_begin++){//the specific days we need to find
                    needed_days.add(day_begin);
                }
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
////day_filter:
//input_days = all days from the crawling
//if input days of each line not in the range we need,find the index of it,then delete the same index in spec_value we're going to output.
                for(String each_input_day : input_days){
                    if(!needed_days.contains(Integer.valueOf(each_input_day))){
                        spec_value.remove(input_days.indexOf(each_input_day));
                    }
                }

////
                File file =new File("./output.csv");
                if(!file.exists()){
                    PrintWriter writer = new PrintWriter(new File("output.csv"));
//when {1} {1} we only need:
//args[2],args[3],args[4] 
//values
                    writer.append(args[2]);
                    //writer.append(args[3]);
                    //writer.append(args[4]);
                    writer.append("\n");
                    for(int count=0;count<spec_value.size();count++){
                        writer.append(spec_value.get(count));
                        writer.append(",");
                    }
                    writer.close();
                }
                else{
                    Path filePath = Paths.get("output.csv");
                    Files.write(filePath,args[2].getBytes(),StandardOpenOption.APPEND);
                    for(int count=0;count<spec_value.size();count++){
                        Files.write(filePath,spec_value.get(count).getBytes(),StandardOpenOption.APPEND);
                        Files.write(filePath,",".getBytes(),StandardOpenOption.APPEND);

                    }
                }
                
//modified file
                //System.out.println(value_lines.get(1).text()); //Q:why can we use get?
                
                // writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
public class writer_test {
    public static void main(String[] args) {
        try{
            File file=new File("./test.csv");
            if(file.exists()){
                System.out.println("exist");
                Path filePath = Paths.get("test.csv");
                Files.write(filePath,"Text to be added\n".getBytes(), StandardOpenOption.APPEND);

            }
            else{
                PrintWriter writer = new PrintWriter(new File("test.csv"));

            }
            // writer.append("hello");
            // writer.close();



        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}

//it can continue writing content in a existed file.

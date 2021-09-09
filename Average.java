import java.io.*;
import java.util.*;

public class Average extends CW1_GA{
    public static void main(String[] args) {

        File file = new File("Results");
        ArrayList<Double> results = new ArrayList<Double>();
        Scanner in = null;
        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (in.hasNextDouble()){

            results.add(in.nextDouble());
        }
        Collections.sort(results);



        Double average = results.stream().mapToDouble(val -> val).average().orElse(0.0);

        System.out.println(average);


    }
}


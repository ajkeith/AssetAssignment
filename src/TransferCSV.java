import java.io.*;

/**
 * Created by Andrew on 2/26/2017.
 */
public class TransferCSV {

    String[][] cats;
    double[][] nums;

    public void readCSV(){
        cats = null;
        nums = null;
    }

    public void importCSV(String filename, int numRuns, int numCats, int numNums){
        String csvFile = filename; // one header line, categorical vars followed by numerical vars
        String line = "";
        String cvsSplitBy = ",";
        String[] inputs = null;
        cats = new String[numRuns][numCats];
        nums = new double[numRuns][numNums];

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            int ind = 0;
            br.readLine(); //skip header
            while ((line = br.readLine()) != null) {
                inputs = line.split(cvsSplitBy);
                for (int i=0;i<numCats;i++){
                    cats[ind][i] = inputs[i];
                }
                for (int i=numCats;i<(numNums+numCats);i++){
                    nums[ind][i-numCats] = Double.parseDouble(inputs[i]);
                }
//                System.out.println("Cats " + cats[ind][0] + " Nums " + nums[ind][0] + " " + nums[ind][1]);
                ind++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportCSV(String filename, double[][] results, int numRuns, int numResponses){
        String[][] strResponses = new String[numRuns][numResponses];

        StringBuilder sb = new StringBuilder();
        for (int i=0;i<numRuns;i++){
            strResponses[i][0] = Double.toString(results[i][0]);
            strResponses[i][1] = Double.toString(results[i][1]);
            strResponses[i][2] = Double.toString(results[i][2]);
            for (String element : strResponses[i]) {
                sb.append(element);
                sb.append(",");
            }
            sb.append("\n");
        }
        try (BufferedWriter br = new BufferedWriter(new FileWriter(filename))) {
            br.write("Compute Time (ms),Total Expected Value (Precise),Total Expected Value (ROC)\n");
            br.write(sb.toString());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[][] getCats(){
        return cats;
    }

    public double[][] getNums(){
        return nums;
    }

}

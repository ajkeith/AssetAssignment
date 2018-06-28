import java.io.*;

public class Main {

    public static void main(String[] args) {

        // RUN OPTIONS -- MUST UPDATE TO RUN PROGRAM
        String searchType = "SA";
        String filepath = "C:\\Users\\Andrew\\Google Drive\\02 - School\\AFIT\\CSCE 899\\AssetAssignment\\src\\";
        String filename = filepath+"augment_3_mini.csv";
        String fileout = "temp_results.csv";
        int numRuns = 5;
        int numCats = 1;
        int numNums = 13;
        int numResponses = 3;

        long totalTime;
        totalTime = System.currentTimeMillis();

//        String filename = "C:\\Users\\Andrew\\Google Drive\\02 - School\\AFIT\\CSCE 899\\AssetAssignment\\src\\doe_test.csv";
//        String filename = filepath+"screening_input.csv";
//        String fileout = "results_screen_1.csv";

        TransferCSV transfer = new TransferCSV();
        transfer.importCSV(filename,numRuns,numCats,numNums);
        String[][] cats = transfer.getCats();
        double[][] nums = transfer.getNums();
        double[][] responses = new double[numRuns][numResponses];


//        String heuristic = "ROC";
//        int numSteps = 10000;
//        int width = 7;
//        int height = 7;
//        double[] gridParameters = new double[5];
//        double[] assetParameters = new double[4];
//        double[] searchParameters = new double[2];
//        gridParameters[0] = 0.8; //grid density
//        gridParameters[1] = 0.5; //grid mix
//        gridParameters[2] = 0.1; //grid location clustering
//        gridParameters[3] = 0.2; //grid type clustering
//        gridParameters[4] = 2; //lambda (exp rand var)
//        assetParameters[0] = 0.8; //asset density
//        assetParameters[1] = 0.5; //asset mix
//        assetParameters[2] = 0.3; //asset location clustering
//        assetParameters[3] = 0.2; //asset type clustering
//        searchParameters[0] = 1; //SA starting temperature
//        searchParameters[1] = 2; //SA alpha (power)
//        long startTime1;
//        long startTime2;
//        Assignment nextLaydown = new Assignment(width,height,gridParameters,assetParameters);
//        startTime1 = System.currentTimeMillis();
//        Assignment finalLaydownTrue = runSearch(searchType, searchParameters, "Pair", nextLaydown, numSteps);
//        startTime1 = System.currentTimeMillis() - startTime1;
//        double totalValueTrueTrue = finalLaydownTrue.totalValue("Pair");
//        double totalValueTrueROC = finalLaydownTrue.totalValue("ROC");
//        startTime2 = System.currentTimeMillis();
//        Assignment finalLaydownROC = runSearch(searchType, searchParameters, "ROC", nextLaydown, numSteps);
//        startTime2 = System.currentTimeMillis() - startTime2;
//        double totalValueROCTrue = finalLaydownROC.totalValue("Pair");
//        double totalValueROCROC = finalLaydownROC.totalValue("ROC");
//        System.out.println("Pair Hueristic: Search Time: "+startTime1+" Value TT: "+totalValueTrueTrue+" Value TR: "+totalValueTrueROC);
//        System.out.println("ROC Hueristic: Search Time: "+startTime2+" Value RT: "+totalValueROCTrue+" Value RR: "+totalValueROCROC);

        System.out.println("Starting runs...");
        for (int i=0; i<numRuns; i++) {
            double[] gridParameters = new double[5];
            double[] assetParameters = new double[4];
            double[] searchParameters = new double[2];
            String heuristic = cats[i][0];
            int numSteps = (int) nums[i][0]; //number of SA reps
            int width = (int) nums[i][1]; //size
            int height = (int) nums[i][1]; //size
            gridParameters[0] = nums[i][2]; //grid density
            gridParameters[1] = nums[i][3]; //grid mix
            gridParameters[2] = nums[i][4]; //grid location clustering
            gridParameters[3] = nums[i][5]; //grid type clustering
            gridParameters[4] = nums[i][6]; //lambda (exp rand var)
            assetParameters[0] = nums[i][7]; //asset density
            assetParameters[1] = nums[i][8]; //asset mix
            assetParameters[2] = nums[i][9]; //asset location clustering
            assetParameters[3] = nums[i][10]; //asset type clustering
            searchParameters[0] = nums[i][11]; //SA starting temperature
            searchParameters[1] = nums[i][12]; //SA alpha (power)
            long compTime;
            Assignment nextLaydown = new Assignment(width, height, gridParameters, assetParameters);
            compTime = System.currentTimeMillis();
            Assignment finalLaydown = runSearch(searchType, searchParameters, heuristic, nextLaydown, numSteps);
            compTime = System.currentTimeMillis() - compTime;
            double totalValueTrue = finalLaydown.totalValue("Pair");
            double totalValueROC = finalLaydown.totalValue("ROC");
            responses[i][0] = compTime;
            responses[i][1] = totalValueTrue;
            responses[i][2] = totalValueROC;
            System.out.print(".");
        }
        System.out.println(".");
        System.out.println("Starting Export...");
        transfer.exportCSV(fileout,responses,numRuns,numResponses);
        totalTime = System.currentTimeMillis() - totalTime;
        System.out.println("Complete. Total Time: " + (totalTime/1000.0));

    }

    public static Assignment runSearch(String searchType, double[] searchParameters, String heuristic, Assignment nextLaydown, int numSteps){
        switch (searchType){
            case "HC":
//                System.out.println("--- Initial Configuration ---");
//                for (int j=0; j<nextLaydown.assets.size(); j++){
//                    Asset a = nextLaydown.assets.get(j);
//                    System.out.println("Collector Location " + j + ": " + a.getLocation().x + " " + a.getLocation().y);
//                }
                HillSearch searchHC = new HillSearch();
                for (int i=0; i<numSteps; i++){
                    nextLaydown = searchHC.findBestMove(nextLaydown, heuristic);
//                    if(searchHC.noBetterMove()){
//                      System.out.println("--- Configuration " + i + " -----------------");
//                      for (int j=0; j<nextLaydown.assets.size(); j++){
//                          Asset a = nextLaydown.assets.get(j);
//                          System.out.println("Collector Location " + j + ": " + a.getLocation().x + " " + a.getLocation().y);
//                      }
//                        System.out.println("Local Maximum Achieved (" + i + " moves)");
//                        i=numSteps;
//                    }
                }
                break;
            case "SA":
                SimAnnealingSearch searchSA = new SimAnnealingSearch();
                nextLaydown = searchSA.findBestMove(nextLaydown, heuristic, numSteps, searchParameters[0], searchParameters[1]);
//                System.out.println("--- Final Configuration -----------------");
//                System.out.println("Number of steps: " + searchSA.t + " Temperature: " + searchSA.temp);
//                int numTotal = searchSA.numHops+searchSA.numClimbs+searchSA.numStays;
//                System.out.println("Hops: " + searchSA.numHops + " Climbs: " + searchSA.numClimbs + " Stays: " + searchSA.numStays + " Total: " + numTotal);
//                for (int j=0; j<nextLaydown.assets.size(); j++){
//                    Asset a = nextLaydown.assets.get(j);
//                    System.out.println("Collector Location " + j + ": " + a.getLocation().x + " " + a.getLocation().y);
//                }
//                for (int j=0; j<5; j++){
//                    Asset a = nextLaydown.assets.get(j);
//                    System.out.println("Collector Location " + j + ": " + a.getLocation().x + " " + a.getLocation().y);
//                }
//                System.out.println("...");
//                for (int j=(nextLaydown.assets.size()-5); j<nextLaydown.assets.size(); j++){
//                    Asset a = nextLaydown.assets.get(j);
//                    System.out.println("Collector Location " + j + ": " + a.getLocation().x + " " + a.getLocation().y);
//                }
                break;
        }
        return nextLaydown;
    }

}

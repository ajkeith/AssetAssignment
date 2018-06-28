import ec.util.MersenneTwister;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Andrew on 2/20/2017.
 */
public class SimAnnealingSearch {

    boolean noBetterMove;
    int numHops;
    int numClimbs;
    int numStays;
    double temp;
    int t;

    public Assignment findBestMove(Assignment laydown, String heuristic, int maxSteps, double T0, double alpha) {
        noBetterMove = true;
        HashMap<Integer, Asset> assets = laydown.getAssets();
        numHops = 0;
        numClimbs = 0;
        numStays = 0;
        temp =99;
        t =1;
        MersenneTwister r = new MersenneTwister();
        while (t <= maxSteps){
            temp = tempSchedule(t,maxSteps,T0,alpha);
            if (temp==0){
                return laydown;
            }
            Integer randAssetIndex = r.nextInt(assets.size());
            int randGridIndex = r.nextInt(laydown.width * laydown.height);
            Asset currentAsset = assets.get(randAssetIndex);
            Grid currentLocation = currentAsset.getLocation();
            Grid nextLocation = laydown.fullGrid.get(randGridIndex);
            Asset nextAsset = nextLocation.asset;
            double noSwapV = laydown.value(currentAsset, currentLocation, heuristic) + laydown.value(nextAsset, nextLocation, heuristic);
            double swapV = laydown.value(currentAsset, nextLocation, heuristic) + laydown.value(nextAsset, currentLocation, heuristic);
            double deltaV = swapV - noSwapV;
//                        System.out.println("----- Location: " + i + " " + nextLocation.x + " " + nextLocation.y);
//                        System.out.println("Asset Type: " + nextAsset.type);
//                        System.out.println("No Swap Value: " + noSwapV);
//                        System.out.println("Swap Value: " + swapV);
//                        System.out.println("Delta Value: " + deltaV);
            if (deltaV > 0) {
                numClimbs++;
                laydown.assign(randAssetIndex, randGridIndex);
                noBetterMove = false;
            } else{
                double p = Math.exp(deltaV/temp);
                double randP = r.nextDouble();
//                System.out.println("Delta Value: " + deltaV + " Probability: " + p + " Rand Value: " + randP);
                if (randP < p){
                    numHops++;
                    laydown.assign(randAssetIndex, randGridIndex);
                    noBetterMove = false;
                }else{
                    numStays++;
                }
            }
            t++;
        }
        return laydown;
    }

    public boolean noBetterMove(){
        return noBetterMove;
    }

    public double tempSchedule(int t, int maxT, double T0, double alpha){
//        double T0 = 100;
//        double alpha = 0.999;
//        double power = ((double) t)/10;
        double eps = 0.00001;
        double temp = T0*Math.pow((1-((double)t/(double)maxT)),alpha)+eps;
//        System.out.println("Temparature "+t+" : "+temp);
        return temp;
    }

}

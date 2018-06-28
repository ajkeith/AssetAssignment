import ec.util.MersenneTwister;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * Created by Andrew on 2/12/2017.
 */
public class Assignment {

    int width;
    int height;

    double[][] PROBS = new double[][]{
            { 0, 0, 0, 0, 0},
            { 0, 0.65, 0.4, 0.2, 0.1},
            { 0, 0.001, 0.3, 0.3, 0.3},
            { 0, 0.05, 0.2, 0.2, 0.6},
    };

    HashMap<Integer,Asset> assets = new HashMap<Integer,Asset>();
    LinkedList<Grid> openGrid = new LinkedList<>();
    LinkedList<Grid> fullGrid = new LinkedList<>();
    SortedMap<Double,Integer> tgtValueMap = new TreeMap<Double, Integer>();

    /**
     * Constructor.
     *
     * Randomly assign collection assets to grid square locations without repetition.
     */
    public Assignment(int w, int h, double[] gridParameters, double[] assetParameters) {
        MersenneTwister r = new MersenneTwister();
        double densityG = gridParameters[0];
        double mixG = gridParameters[1];
        double clusterLocG = gridParameters[2];
        double clusterTgtG = gridParameters[3];
        double lambda = gridParameters[4];
        double densityA = assetParameters[0];
        double mixA = assetParameters[1];
        double clusterLocA = assetParameters[2];
        double clusterTgtA = assetParameters[3];
        int collectors = (int)(densityA*(w*h));
        int k = 0;
        int gridIndex = 0;
        int lastTypeG = 0;
        int lastTypeA = 1;
        int lastLocG = 0;
        double tgtValue = 0;
        double tgtValueSum = 0;
        width = w;
        height = h;
        // Create Grid World with target type and value
        for (int i=1;i<=width;i++){
            for (int j=1;j<=height;j++){
                Grid nextSquare = new Grid(i,j,k);
                // If the next square is part of a cluster, it has 0 or positive based on the last square
                lastLocG = (int) Math.signum(lastTypeG);
                if (r.nextDouble() < clusterLocG){
                    if (r.nextDouble() < clusterTgtG){
                        tgtValue = -1*Math.log(r.nextDouble())/lambda;
                        nextSquare.setParameters(lastTypeG*lastLocG,tgtValue*lastLocG*lastTypeG);
                    } else if (r.nextDouble() < mixG){
                        tgtValue = -1*Math.log(r.nextDouble())/lambda;
                        nextSquare.setParameters(1*lastLocG,tgtValue*lastLocG);
                    } else if (r.nextDouble() < mixG){
                        tgtValue = -1*Math.log(r.nextDouble())/lambda;
                        nextSquare.setParameters(2*lastLocG,tgtValue*lastLocG);
                    } else{
                        tgtValue = -1*Math.log(r.nextDouble())/lambda;
                        nextSquare.setParameters(3*lastLocG,tgtValue*lastLocG);
                    }
                    // Otherwise, next square has 0 or positive based on the density
                } else if (r.nextDouble() < densityG){
                    if (r.nextDouble() < clusterTgtG){
                        tgtValue = (-1*Math.log(r.nextDouble())/lambda)*Math.signum(lastTypeG);
                        nextSquare.setParameters(lastTypeG,tgtValue);
                    } else if (r.nextDouble() < mixG){
                        tgtValue = (-1*Math.log(r.nextDouble())/lambda);
                        nextSquare.setParameters(1,tgtValue);
                    } else if (r.nextDouble() < mixG){
                        tgtValue = (-1*Math.log(r.nextDouble())/lambda);
                        nextSquare.setParameters(2,tgtValue);
                    } else{
                        tgtValue = (-1*Math.log(r.nextDouble())/lambda);
                        nextSquare.setParameters(3,tgtValue);
                    }
                }
                openGrid.add(nextSquare);
                fullGrid.add(nextSquare);
                if (nextSquare.tgtValue>0) {
                    tgtValueMap.put(nextSquare.tgtValue, k);
                }
                tgtValueSum = tgtValueSum + nextSquare.tgtValue;
//                System.out.println("Grid "+k+" Type: "+nextSquare.tgtType+ " Value: "+nextSquare.tgtValue+" Last Loc: "+lastLocG+" Last Type: "+lastTypeG);
                lastTypeG = nextSquare.tgtType;
                k++;
            }
        }
        // Calculate value weight, rank, and rank weight from sorted target values
        double maxRank = tgtValueMap.size();
        double currRank = maxRank;
        double coeffRankWeight = 0;
        for (SortedMap.Entry entry : tgtValueMap.entrySet()) {
            coeffRankWeight = (1/currRank)+coeffRankWeight;
            fullGrid.get((int) entry.getValue()).setValueWeight((double) entry.getKey()/tgtValueSum);
            fullGrid.get((int) entry.getValue()).setRank(currRank);
            fullGrid.get((int) entry.getValue()).setRankWeight((1/maxRank)*coeffRankWeight);
            openGrid.get((int) entry.getValue()).setValueWeight((double) entry.getKey()/tgtValueSum);
            openGrid.get((int) entry.getValue()).setRank(currRank);
            openGrid.get((int) entry.getValue()).setRankWeight((1/maxRank)*coeffRankWeight);
            currRank--;
//            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
        }
//        for (int i=0;i<width*height;i++) {
//            System.out.println("Grid " + i + " -- Value: " + fullGrid.get(i).tgtValue + " ValueWeight : " + fullGrid.get(i).tgtValueWeight + " Rank : " + fullGrid.get(i).tgtRank + " RankWeight : " + fullGrid.get(i).tgtRankWeight);
//            System.out.println("Grid " + i + " -- Value: " + openGrid.get(i).tgtValue + " ValueWeight : " + openGrid.get(i).tgtValueWeight + " Rank : " + openGrid.get(i).tgtRank + " RankWeight : " + openGrid.get(i).tgtRankWeight);
//        }
        // Create assets
        for (int i=0;i<collectors;i++){
            if (r.nextDouble() < clusterLocA){
                gridIndex = Math.min(gridIndex,((width*height)-i-1));
               } else {
                gridIndex = (int)(r.nextDouble()*((width*height) - i));
            }
            if (r.nextDouble() < clusterTgtA){
                assignNew(i,gridIndex,lastTypeA);
            } else if (r.nextDouble() < mixA) {
                assignNew(i, gridIndex, 1);
                lastTypeA = 1;
            } else if (r.nextDouble() < mixA){
                assignNew(i, gridIndex, 2);
                lastTypeA = 2;
            } else if (r.nextDouble() < mixA) {
                assignNew(i, gridIndex, 3);
                lastTypeA = 3;
            } else {
                assignNew(i, gridIndex, 4);
                lastTypeA = 4;
            }
//            System.out.println("Asset "+i+" Type: "+assets.get(i).type);
        }
    }

    public HashMap<Integer, Asset> getAssets(){
        return assets;
    }

    public LinkedList<Grid> getOpenGrid(){
        return openGrid;
    }

    public LinkedList<Grid> getFullGrid(){
        return fullGrid;
    }

    public void assign(Integer assetIndex, int gridIndex){
        Asset oldAsset = assets.get(assetIndex);
        Grid oldSquare = oldAsset.getLocation();
        Grid newSquare = fullGrid.get(gridIndex);
        Asset newAsset = newSquare.asset;
        oldSquare.setAsset(newAsset);
        newAsset.setLocation(oldSquare);
        newSquare.setAsset(oldAsset);
        oldAsset.setLocation(newSquare);
//        System.out.println("Swap old asset "+assetIndex+" of type "+oldAsset.type+" at ("+oldSquare.x+","+oldSquare.y+") with new asset of type "+newAsset.type+" at ("+newSquare.x+","+newSquare.y+")");
    }

    public void assignNew(Integer assetIndex, int gridIndex, int type){
        Grid newSquare = openGrid.remove(gridIndex);
        Asset a = new Asset(newSquare);
        a.type = type;
        assets.put(assetIndex,a);
        fullGrid.get(a.getLocation().id).setAsset(a);
    }

    public double value(Asset a, Grid g, String heuristic){
        double pAT = PROBS[g.tgtType][a.type];
        double expValue = pAT * g.tgtValue;
        switch (heuristic) {
            case "Pair":
                expValue = pAT * g.tgtValueWeight;
                break;
            case "ROC":
                expValue = pAT * g.tgtRankWeight;
                break;
        }
        return expValue;
    }

    public double totalValue(String heuristic){
        double sum = 0;
        for (int i=0;i<assets.size();i++) {
            Asset a = assets.get(i);
            Grid g = a.getLocation();
            double expValue = value(a,g,heuristic);
//            System.out.println(heuristic+" "+g.x+" "+g.y+" "+expValue);
            sum = sum + expValue;
        }
        return sum;
    }

}

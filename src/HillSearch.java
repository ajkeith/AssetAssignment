import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Andrew on 2/17/2017.
 */
public class HillSearch {

    boolean noBetterMove;

    public Assignment findBestMove(Assignment laydown, String heuristic){
        noBetterMove = true;
        HashMap<Integer, Asset> assets = laydown.getAssets();
        LinkedList<Grid> openGrid = laydown.getOpenGrid();
        double bestV = 0;
        Integer bestAsset = -1;
        int bestGrid = -1;
        for (Integer i=0;i<assets.size();i++){
            Asset currentAsset = assets.get(i);
            Grid currentLocation = currentAsset.getLocation();
            double currentV = laydown.value(currentAsset,currentLocation, heuristic);
            int gridIndex = 0;
            for (int j=1;j<=laydown.width;j++){
                for (int k=1;k<=laydown.height;k++) {
                    if (gridIndex != currentLocation.id) {
                        Grid nextLocation = laydown.fullGrid.get(gridIndex);
                        Asset nextAsset = nextLocation.asset;
                        double noSwapV = currentV + laydown.value(nextAsset, nextLocation, heuristic);
                        double swapV = laydown.value(currentAsset, nextLocation, heuristic) + laydown.value(nextAsset, currentLocation, heuristic);
                        double deltaV = swapV - noSwapV;
//                        System.out.println("----- Location: " + i + " " + nextLocation.x + " " + nextLocation.y);
//                        System.out.println("Asset Type: " + nextAsset.type);
//                        System.out.println("No Swap Value: " + noSwapV);
//                        System.out.println("Swap Value: " + swapV);
//                        System.out.println("Delta Value: " + deltaV);
                        if (deltaV > bestV) {
                            bestV = deltaV;
                            bestAsset = i;
                            bestGrid = gridIndex;
                            noBetterMove=false;
                        }
                    }
                    gridIndex++;
                }
            }

        }
        if (!noBetterMove) {
            laydown.assign(bestAsset, bestGrid);
        }
        return laydown;
    }

    public boolean noBetterMove(){
        return noBetterMove;
    }

}

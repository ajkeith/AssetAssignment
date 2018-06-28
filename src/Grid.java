/**
 * Created by Andrew on 2/16/2017.
 */
public class Grid {
    Grid next;
    int tgtType;
    int x;
    int y;
    int id;
    Asset asset;
    double tgtValue;
    double tgtValueWeight;
    double tgtRank;
    double tgtRankWeight;

    public Grid(int xLocation, int yLocation, int ind){
        next = null;
        asset = new Asset(this);
        x = xLocation;
        y = yLocation;
        id = ind;
        tgtType = 0;
        tgtValue = 0;
        tgtValueWeight =0;
        tgtRank = 0;
        tgtRankWeight = 0;
    }

    public void setAsset(Asset a){
        asset = a;
    }

    public Asset getAsset(){
        return asset;
    }

    public void setParameters(int type, double value){
        tgtType = type;
        tgtValue = value;
    }

    public void setValueWeight(double value){
        tgtValueWeight = value;
    }

    public void setRank(double value){
        tgtRank = value;
    }

    public void setRankWeight(double value){
        tgtRankWeight = value;
    }

}

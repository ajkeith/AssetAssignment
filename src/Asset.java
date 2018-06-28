/**
 * Created by Andrew on 2/16/2017.
 */
public class Asset {
    Asset next;
    Grid location;
    int type;

    public Asset(){
        next = null;
        location = null;
        type = 0;
    }

    public Asset(Grid g){
        next = null;
        location = g;
        type = g.tgtType; // this gets overridden so I think there's no negative effect, but this is incorrect
    }

    public Grid getLocation(){
        return location;
    }

    public void setLocation(Grid g){
        location = g;
    }

}

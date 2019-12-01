package parcles;
import java.io.Serializable;

public class Parcel implements Serializable {
    String message = "";

    public Parcel(String message){
        this.message = message;
    }
}

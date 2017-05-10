package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Korisnik on 06.05.2017..
 */

public class BodyRating {
    @SerializedName("value")
    private Double value;

    public BodyRating(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}

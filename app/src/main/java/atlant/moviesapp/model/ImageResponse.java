package atlant.moviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Korisnik on 08.06.2017..
 */

public class ImageResponse  {


    @SerializedName("backdrops")
    @Expose
    private List<Backdrop> backdrops = null;

    public ImageResponse() {
    }

    public List<Backdrop> getBackdrops() {
        return backdrops;
    }
}
package atlant.moviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Korisnik on 08.06.2017..
 */

public class Backdrop
{

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @SerializedName("file_path")
     private String filePath;

    public Backdrop() {
    }

    public Backdrop(Backdrop b) {
        this.filePath = b.filePath;
    }

}
package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Korisnik on 06.05.2017..
 */

public class PostResponse {

    @SerializedName("status_code")
     private Integer statusCode;

    @SerializedName("status_message")
    private  String statusMessage;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}


package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Korisnik on 04.05.2017..
 */

public class Token {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("request_token")
    private String requestToken;

    public Token(Boolean success, String requestToken) {
        this.success = success;
        this.requestToken = requestToken;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
}

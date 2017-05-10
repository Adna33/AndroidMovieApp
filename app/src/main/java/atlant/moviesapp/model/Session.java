package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Korisnik on 04.05.2017..
 */

public class Session {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("session_id")
    private String sessionId;

    public Session(Boolean success, String sessionId) {
        this.success = success;
        this.sessionId = sessionId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}

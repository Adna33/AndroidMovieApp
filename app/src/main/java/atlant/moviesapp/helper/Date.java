package atlant.moviesapp.helper;

import android.content.Context;

/**
 * Created by Korisnik on 07.05.2017..
 */

public class Date {
    Context mContext;

    public Date(Context mContext) {
        this.mContext = mContext;
    }

    private String getMonth(int monthNum) {
        switch (monthNum){
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return "Unknown";
    }
    public String getFormatedDate(String date) {

        String[] separated = date.split("-");
        if(separated.length>0){
            String year= separated[0];
            String month= separated[1];
            String day= separated[2];
            return day+" "+getMonth(Integer.parseInt(month))+" "+year;

        }else {
            return null;
        }
    }
}

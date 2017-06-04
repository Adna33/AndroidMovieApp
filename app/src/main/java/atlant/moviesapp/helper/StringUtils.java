package atlant.moviesapp.helper;

import android.content.Context;
import android.content.res.Resources;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.TvShowDetail;

/**
 * Created by Korisnik on 02.06.2017..
 */

public class StringUtils {
    Context mContext;

    public StringUtils(Context mContext) {
        this.mContext = mContext;
    }

    public String emptyString() {
        return "";
    }

    public String getTitle(String title, String year) {
        return title + " (" + year.substring(0, Math.min(year.length(), 4)) + ")";
    }

    public String getCast(String cast, String castMemeber) {
        return cast + castMemeber + emptyString();
    }

    public String getDirectorString(List<Crew> crew) {
        String director = emptyString();
        for (int i = 0; i < crew.size(); i++) {
            if (crew.get(i).getJob().equals(mContext.getString(R.string.director))) {
                director = director + crew.get(i).getName() + " ";
            }

        }
        return director;
    }

    public String getWritersString(List<Crew> crew) {
        String writersString = emptyString();
        int num = 0;
        for (int i = 0; i < crew.size(); i++) {
            if (crew.get(i).getDepartment().equals(mContext.getString(R.string.writing)) && num < 3) {
                num++;
                writersString = writersString + crew.get(i).getName() + " (" + crew.get(i).getJob() + ")  ";
            }

        }
        return writersString;
    }
    public String getGenre(TvShowDetail series)
    {
        String genre=emptyString();
        for (int i = 0; i < series.getGenres().size(); i++)
            genre+= series.getGenres().get(i).getName() + ", ";
        return genre;
    }

    public String getActorBirthData(String date, String place)
    {
        return date + ", " + place;
    }

    public String getId(int series,int season)
    {
        return series+emptyString()+season;
    }

}

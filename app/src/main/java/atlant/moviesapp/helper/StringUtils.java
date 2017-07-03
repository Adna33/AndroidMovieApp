package atlant.moviesapp.helper;

import android.content.Context;
import android.content.res.Resources;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.TvShowDetail;

/**
 * Created by Korisnik on 02.06.2017..
 */

public class StringUtils {

    private StringUtils(){}


    public static String getTitle(String title, String year) {
        return title + " (" + year.substring(0, Math.min(year.length(), 4)) + ")";
    }

    public static String showCast(final List<Cast> cast) {
        String castString = "";
        for (int i = 0; i < cast.size(); i++) {
            if (i < 4) {
                castString += cast.get(i).getName();
            }
        }
        return castString;
    }


    public static String getDirectorString(List<Crew> crew, Context mContext) {
        String director = "";
        for (int i = 0; i < crew.size(); i++) {
            if (crew.get(i).getJob().equals(mContext.getString(R.string.director))) {
                director = director + crew.get(i).getName() + " ";
            }

        }
        return director;
    }

    public static String getWritersString(List<Crew> crew, Context mContext) {
        String writersString = "";
        int num = 0;
        for (int i = 0; i < crew.size(); i++) {
            if (crew.get(i).getDepartment().equals(mContext.getString(R.string.writing)) && num < 3) {
                num++;
                writersString = writersString + crew.get(i).getName() + " (" + crew.get(i).getJob() + ")  ";
            }

        }
        return writersString;
    }
    public static String getGenre(TvShowDetail series)
    {
        String genre="";
        for (int i = 0; i < series.getGenres().size(); i++)
            genre+= series.getGenres().get(i).getName() + ", ";
        return genre;
    }

    public static String getId(int series,int season)
    {
        return series+""+season;
    }

}

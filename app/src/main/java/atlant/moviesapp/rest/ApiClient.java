package atlant.moviesapp.rest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Korisnik on 07.04.2017..
 */

public class ApiClient {
    private static final String BASE_URL="https://api.themoviedb.org/3/";
    private static final String NEWS_FEED_BASE_URL="http://www.boxofficemojo.com/";
    private static Retrofit retrofit=null;
    private static Retrofit retrofitFeed=null;

    public static Retrofit getClient()
    {
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    public static Retrofit getClientNewsFeed()
    {
        if(retrofitFeed==null)
        {

            retrofitFeed = new Retrofit.Builder().baseUrl(NEWS_FEED_BASE_URL).client(new OkHttpClient()).addConverterFactory(SimpleXmlConverterFactory.create()).build();

        }
        return retrofitFeed;
    }

}

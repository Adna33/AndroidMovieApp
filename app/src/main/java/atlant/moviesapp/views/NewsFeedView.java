package atlant.moviesapp.views;

import java.util.List;

import atlant.moviesapp.model.News;
import atlant.moviesapp.model.TvShow;

/**
 * Created by Korisnik on 14.04.2017..
 */

public interface NewsFeedView {
    void showNews(List<News> data);
    void showProgress();

    void hideProgress();
}

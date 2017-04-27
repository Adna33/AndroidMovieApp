package atlant.moviesapp.views;

import java.util.List;

import atlant.moviesapp.model.Episode;

/**
 * Created by Korisnik on 19.04.2017..
 */

public interface SeasonsView {
    void ShowSeasonList(Integer seasonNum, Integer id);
    void ShowEpisodes(List<Episode> episodes);

}

package atlant.moviesapp.realm;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.model.Actor;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Episode;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.News;
import atlant.moviesapp.model.Review;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.model.TvShowDetail;
import atlant.moviesapp.realm.models.RealmAccount;
import atlant.moviesapp.realm.models.RealmActor;
import atlant.moviesapp.realm.models.RealmEpisode;
import atlant.moviesapp.realm.models.RealmInt;
import atlant.moviesapp.realm.models.RealmLists;
import atlant.moviesapp.realm.models.RealmMovie;
import atlant.moviesapp.realm.models.RealmPostMovie;
import atlant.moviesapp.realm.models.RealmPostSeries;
import atlant.moviesapp.realm.models.RealmSeason;
import atlant.moviesapp.realm.models.RealmSeries;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Korisnik on 24.05.2017..
 */

public class RealmUtil {

    private static RealmUtil instance = null;


    public RealmUtil() {
    }

    public static RealmUtil getInstance() {
        if (instance == null) instance = new RealmUtil();
        return instance;
    }

    public Realm realm = Realm.getDefaultInstance();


    // MOVIE / TVSHOW LIST
    public void createRealmList(int id) {
        realm.beginTransaction();
        realm.createObject(RealmLists.class, id);
        realm.commitTransaction();
    }

    public RealmLists getRealmList(int id) {
        realm.beginTransaction();
        RealmLists movielist = realm.where(RealmLists.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return movielist;
    }

    //Movie Lists

    public void addPopularMovies(final int id, final List<Movie> movies) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmLists movielist = realm.where(RealmLists.class).equalTo("id", id).findFirst();
                movielist.getMostPopularMovies().clear();
                for (Movie m : movies) {
                    if (!movielist.getMostPopularMovies().contains(m))
                        movielist.getMostPopularMovies().add(realm.copyToRealmOrUpdate(m));
                }
            }
        });
    }

    public List<Movie> getPopularMovies(int id) {
        realm.beginTransaction();
        RealmLists movielist = realm.where(RealmLists.class).equalTo("id", id).findFirst();
        List<Movie> movies = new ArrayList<>(movielist.getMostPopularMovies());
        realm.commitTransaction();
        return movies;
    }

    public void addHighestRatedMovies(final int id, final List<Movie> movies) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmLists movielist = realm.where(RealmLists.class).equalTo("id", id).findFirst();
                movielist.getHighestRatedMovies().clear();
                for (Movie m : movies) {
                    if (!movielist.getHighestRatedMovies().contains(m))
                        movielist.getHighestRatedMovies().add(realm.copyToRealmOrUpdate(m));
                }
            }
        });
    }

    public List<Movie> getHighestRatedMovies(int id) {
        realm.beginTransaction();
        RealmLists movielist = realm.where(RealmLists.class).equalTo("id", id).findFirst();
        List<Movie> movies = new ArrayList<>(movielist.getHighestRatedMovies());
        realm.commitTransaction();
        return movies;
    }

    public void addLatestMovies(final int id, final List<Movie> movies) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmLists movielist = realm.where(RealmLists.class).equalTo("id", id).findFirst();
                movielist.getLatestMovies().clear();
                for (Movie m : movies) {
                    if (!movielist.getLatestMovies().contains(m))
                        movielist.getLatestMovies().add(realm.copyToRealmOrUpdate(m));
                }
            }
        });
    }

    public List<Movie> getLatestMovies(int id) {
        realm.beginTransaction();
        RealmLists movielist = realm.where(RealmLists.class).equalTo("id", id).findFirst();
        List<Movie> movies = new ArrayList<>(movielist.getLatestMovies());
        realm.commitTransaction();
        return movies;
    }

    //Series Lists
    public void addPopularSeries(final int id, final List<TvShow> series) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmLists movielist = realm.where(RealmLists.class).equalTo("id", id).findFirst();
                movielist.getMostPopularSeries().clear();
                for (TvShow s : series) {
                    if (!movielist.getMostPopularSeries().contains(s))
                        movielist.getMostPopularSeries().add(realm.copyToRealmOrUpdate(s));
                }
            }
        });
    }

    public List<TvShow> getPopularSeries(int id) {
        realm.beginTransaction();
        RealmLists result = realm.where(RealmLists.class).equalTo("id", id).findFirst();
        List<TvShow> list = new ArrayList<>(result.getMostPopularSeries());
        realm.commitTransaction();
        return list;
    }

    public void addLatestSeries(final int id, final List<TvShow> series) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmLists movielist = realm.where(RealmLists.class).equalTo("id", id).findFirst();
                movielist.getLatestSeries().clear();
                for (TvShow s : series) {
                    if (!movielist.getLatestSeries().contains(s))
                        movielist.getLatestSeries().add(realm.copyToRealmOrUpdate(s));
                }
            }
        });
    }

    public List<TvShow> getLatesSeries(int id) {
        realm.beginTransaction();
        RealmLists result = realm.where(RealmLists.class).equalTo("id", id).findFirst();
        List<TvShow> list = new ArrayList<>(result.getLatestSeries());
        realm.commitTransaction();
        return list;
    }

    public void addHighestRatedSeries(final int id, final List<TvShow> series) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmLists movielist = realm.where(RealmLists.class).equalTo("id", id).findFirst();
                movielist.getHighestRatedSeries().clear();
                for (TvShow s : series) {
                    if (!movielist.getHighestRatedSeries().contains(s))
                        movielist.getHighestRatedSeries().add(realm.copyToRealmOrUpdate(s));
                }
            }
        });
    }

    public List<TvShow> getHighestRatedSeries(int id) {
        realm.beginTransaction();
        RealmLists result = realm.where(RealmLists.class).equalTo("id", id).findFirst();
        List<TvShow> list = new ArrayList<>(result.getHighestRatedSeries());
        realm.commitTransaction();
        return list;
    }

    public void addAiringSeries(final int id, final List<TvShow> series) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmLists movielist = realm.where(RealmLists.class).equalTo("id", id).findFirst();
                movielist.getAiringSeries().clear();
                for (TvShow s : series) {
                    if (!movielist.getAiringSeries().contains(s))
                        movielist.getAiringSeries().add(realm.copyToRealmOrUpdate(s));
                }
            }
        });
    }

    public List<TvShow> getAiringSeries(int id) {
        realm.beginTransaction();
        RealmLists result = realm.where(RealmLists.class).equalTo("id", id).findFirst();
        List<TvShow> list = new ArrayList<>(result.getAiringSeries());
        realm.commitTransaction();
        return list;
    }


    //NEWSFEED
    public List<News> getNewsFromRealm() {
        realm.beginTransaction();
        List<News> news = new ArrayList<>(realm.where(News.class).findAll());
        realm.commitTransaction();
        return news;
    }

    public void deleteNewsFeed() {
        final RealmResults<News> results = realm.where(News.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }

    public void addNewsToRealm(List<News> news) {
        deleteNewsFeed();
        for (final News n : news) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(n);
                }
            });
        }
    }


    //MOVIE DETAILS
    public Movie getMovieFromRealm(int id) {
        realm.beginTransaction();
        Movie movie = realm.where(Movie.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return movie;
    }

    public RealmMovie getMovieDetailsFromRealm(int id) {
        realm.beginTransaction();
        RealmMovie movie = realm.where(RealmMovie.class).equalTo("id", id).findFirst();
        realm.commitTransaction();

        return movie;
    }

    public void createRealmMovieObject(int id) {
        deleteRealmMovieObject(id);
        realm.beginTransaction();
        realm.createObject(RealmMovie.class, id);
        realm.commitTransaction();
    }

    public void deleteRealmMovieObject(int id) {
        final RealmResults<RealmMovie> realmMovies = realm.where(RealmMovie.class).equalTo("id", id).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmMovies.deleteAllFromRealm();
            }
        });
    }

    public void addRealmMovieReviews(final int id, final List<Review> reviews) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmMovie realmMovie = realm.where(RealmMovie.class).equalTo("id", id).findFirst();
                realmMovie.getReviews().clear();
                if (realmMovie != null) {
                    for (Review r : reviews) {
                        if (!realmMovie.getReviews().contains(r))
                            realmMovie.getReviews().add(realm.copyToRealmOrUpdate(r));
                    }
                }
            }
        });
    }


    public void addRealmMovieActors(final int id, final List<Cast> actors) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmMovie realmMovie = realm.where(RealmMovie.class).equalTo("id", id).findFirst();
                realmMovie.getActors().clear();
                if (realmMovie != null) {
                    for (Cast a : actors) {
                        if (!realmMovie.getActors().contains(a))
                            realmMovie.getActors().add(realm.copyToRealmOrUpdate(a));
                    }
                }
            }
        });
    }

    public void addRealmMovieWriters(final int id, final List<Crew> writers) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmMovie realmMovie = realm.where(RealmMovie.class).equalTo("id", id).findFirst();
                realmMovie.getWriters().clear();
                if (realmMovie != null) {
                    for (Crew a : writers) {
                        if (!realmMovie.getWriters().contains(a))
                            realmMovie.getWriters().add(realm.copyToRealmOrUpdate(a));
                    }
                }
            }
        });
    }


    //TV SHOWS
    public TvShow getTvShowFromRealm(int id) {
        realm.beginTransaction();
        TvShow show = realm.where(TvShow.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return show;
    }

    public TvShowDetail getTvShowDetailFromRealm(int id) {
        realm.beginTransaction();
        TvShowDetail show = realm.where(TvShowDetail.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return show;
    }

    public RealmSeries getShowDetailsFromRealm(int id) {
        realm.beginTransaction();
        RealmSeries series = realm.where(RealmSeries.class).equalTo("id", id).findFirst();
        realm.commitTransaction();

        return series;
    }

    public void createRealmSeriesObject(int id) {
        realm.beginTransaction();
        //  deleteRealmSeriesObject(id);
        realm.createObject(RealmSeries.class, id);
        realm.createObject(TvShowDetail.class, id);
        realm.commitTransaction();
    }

    public void addRealmSeriesDetails(final int id, final TvShowDetail series) {
        realm.beginTransaction();
        TvShowDetail show = realm.where(TvShowDetail.class).equalTo("id", id).findFirst();
        show.setTvShow(series);
        realm.commitTransaction();
    }

    public void addRealmSeriesActors(final int id, final List<Cast> actors) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmSeries realmSeries = realm.where(RealmSeries.class).equalTo("id", id).findFirst();
                realmSeries.getActors().clear();
                if (realmSeries != null) {
                    for (Cast a : actors) {
                        if (!realmSeries.getActors().contains(a))
                            realmSeries.getActors().add(realm.copyToRealmOrUpdate(a));
                    }
                }
            }
        });
    }

    public void addRealmSeriesWriters(final int id, final List<Crew> writers) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmSeries realmSeries = realm.where(RealmSeries.class).equalTo("id", id).findFirst();
                realmSeries.getWriters().clear();
                if (realmSeries != null) {
                    for (Crew a : writers) {
                        if (!realmSeries.getWriters().contains(a))
                            realmSeries.getWriters().add(realm.copyToRealmOrUpdate(a));
                    }
                }
            }
        });
    }


    //TV SHOW SEASONS
    public RealmSeason getRealmSeason(String id) {
        realm.beginTransaction();
        RealmSeason season = realm.where(RealmSeason.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return season;
    }

    public void createRealmSeason(String id) {
        realm.beginTransaction();
        realm.createObject(RealmSeason.class, id);
        realm.commitTransaction();
    }


    public void addEpisodesToRealmSeason(final String id, final List<Episode> episodes) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmSeason season = realm.where(RealmSeason.class).equalTo("id", id).findFirst();
                season.getEpisodes().clear();
                if (season != null) {
                    for (Episode e : episodes) {
                        if (!season.getEpisodes().contains(e))
                            season.getEpisodes().add(realm.copyToRealmOrUpdate(e));
                    }
                }
            }
        });
    }


    //EPISODE
    public RealmEpisode getRealmEpisode(String id) {
        realm.beginTransaction();
        RealmEpisode episode = realm.where(RealmEpisode.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return episode;
    }

    public void createRealmEpisode(String id) {
        realm.beginTransaction();
        realm.createObject(RealmEpisode.class, id);
        realm.commitTransaction();
    }

    public void addRealmEpisodeActors(final String id, final List<Cast> actors) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmEpisode realmEpisode = realm.where(RealmEpisode.class).equalTo("id", id).findFirst();
                realmEpisode.getCast().clear();
                if (realmEpisode != null) {
                    for (Cast a : actors) {
                        realmEpisode.getCast().add(realm.copyToRealmOrUpdate(a));
                    }
                }
            }
        });
    }

    //ACTOR
    public RealmActor getRealmActor(int id) {
        realm.beginTransaction();
        RealmActor actor = realm.where(RealmActor.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return actor;
    }

    public Actor getActor(int id) {
        realm.beginTransaction();
        Actor actor = realm.where(Actor.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return actor;
    }

    public void createRealmActor(int id) {
        realm.beginTransaction();
        //deleteRealmActor(id);
        realm.createObject(RealmActor.class, id);
        realm.createObject(Actor.class, id);
        realm.commitTransaction();
    }

    public void addActorDetails(final int id, final Actor actor) {
        realm.beginTransaction();
        realm.where(Actor.class).equalTo("id", id).findFirst().setActor(realm.copyToRealmOrUpdate(actor));
        realm.commitTransaction();
    }

    public List<Movie> getRealmActorMovies(int id) {
        realm.beginTransaction();
        RealmActor actor = realm.where(RealmActor.class).equalTo("id", id).findFirst();
        List<Movie> actorMovies = new ArrayList<>(actor.getMovies());
        realm.commitTransaction();
        return actorMovies;
    }

    public void addRealmActorMovies(final int id, final List<Movie> movies) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmActor actor = realm.where(RealmActor.class).equalTo("id", id).findFirst();
                actor.getMovies().clear();
                if (actor != null) {
                    for (Movie m : movies) {
                        if (!actor.getMovies().contains(m))
                            actor.getMovies().add(realm.copyToRealmOrUpdate(m));
                    }
                }
            }
        });
    }


    //ACCOUNT

    public void createRealmAccount() {
        deleteRealmAccount();
        realm.beginTransaction();
        realm.createObject(RealmAccount.class);
        realm.commitTransaction();
    }

    public RealmAccount getRealmAccount() {
        realm.beginTransaction();
        RealmAccount account = realm.where(RealmAccount.class).findFirst();
        realm.commitTransaction();
        return account;
    }

    public void deleteRealmAccount() {
        final RealmResults<RealmAccount> results = realm.where(RealmAccount.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }


    //RATING
    public void addMovieRatings(final List<Integer> ratings) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();
                realmAccount.getRatedMovies().clear();
                for (Integer f : ratings) {
                    RealmInt rating = new RealmInt(f);
                    realmAccount.getRatedMovies().add(realm.copyToRealmOrUpdate(rating));
                }
            }
        });
    }

    public void addMovieRating(final Integer r) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();
                RealmInt rating = new RealmInt(r);
                realmAccount.getRatedMovies().add(realm.copyToRealmOrUpdate(rating));

            }
        });
    }

    public void addSeriesRatings(final List<Integer> ratings) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();
                realmAccount.getRatedSeries().clear();

                for (Integer f : ratings) {
                    RealmInt rating = new RealmInt(f);
                    realmAccount.getRatedSeries().add(realm.copyToRealmOrUpdate(rating));
                }
            }
        });
    }

    public void addSeriesRatings(final Integer r) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();
                RealmInt rating = new RealmInt(r);
                realmAccount.getRatedSeries().add(realm.copyToRealmOrUpdate(rating));

            }
        });
    }

    //WATCHLIST
    public void addMovieWatchlist(final List<Integer> watchlist) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();
                realmAccount.getWatchlistMovies().clear();
                for (Integer f : watchlist) {
                    RealmInt w = new RealmInt(f);
                    realmAccount.getWatchlistMovies().add(realm.copyToRealmOrUpdate(w));
                }
            }
        });
    }

    public void addMovieWatchlist(final Integer watchlist) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();
                RealmInt w = new RealmInt(watchlist);
                realmAccount.getWatchlistMovies().add(realm.copyToRealmOrUpdate(w));

            }
        });
    }

    public void addSeriesWatchlist(final List<Integer> watchlist) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();
                realmAccount.getWatchlistSeries().clear();
                for (Integer f : watchlist) {
                    RealmInt w = new RealmInt(f);
                    realmAccount.getWatchlistSeries().add(realm.copyToRealmOrUpdate(w));
                }
            }
        });
    }

    public void addShowWatchlist(final Integer watchlist) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();

                RealmInt w = new RealmInt(watchlist);
                realmAccount.getWatchlistSeries().add(realm.copyToRealmOrUpdate(w));

            }
        });
    }

    //FAVORITE
    public void addFavoriteMovies(final List<Integer> favorites) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();
                realmAccount.getFavoriteMovies().clear();
                for (Integer f : favorites) {
                    RealmInt favorite = new RealmInt(f);
                    realmAccount.getFavoriteMovies().add(realm.copyToRealmOrUpdate(favorite));
                }
            }
        });
    }


    public void addFavoriteMovie(final Integer favorites) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();

                RealmInt favorite = new RealmInt(favorites);
                realmAccount.getFavoriteMovies().add(realm.copyToRealmOrUpdate(favorite));

            }
        });
    }

    public void addFavoriteSeries(final List<Integer> favorites) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();
                realmAccount.getFavoriteSeries().clear();
                for (Integer f : favorites) {
                    RealmInt favorite = new RealmInt(f);
                    realmAccount.getFavoriteSeries().add(realm.copyToRealmOrUpdate(favorite));
                }
            }
        });
    }

    public void addFavoriteSeries(final Integer favorites) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmAccount realmAccount = realm.where(RealmAccount.class).findFirst();
                RealmInt favorite = new RealmInt(favorites);
                realmAccount.getFavoriteSeries().add(realm.copyToRealmOrUpdate(favorite));

            }
        });
    }

    public void deleteRealmInt(final int id) {
        final RealmResults<RealmInt> results = realm.where(RealmInt.class).equalTo("id", id).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }


    //POST MODEL

    public RealmPostMovie getPostMovie(int id) {
        realm.beginTransaction();
        RealmPostMovie m = realm.where(RealmPostMovie.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return m;
    }

    public void deletePostMovie(int id) {
        final RealmResults<RealmPostMovie> postModels = realm.where(RealmPostMovie.class).equalTo("id", id).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                postModels.deleteAllFromRealm();
            }
        });
    }

    public void deleteAllPostMovies() {
        final RealmResults<RealmPostMovie> postModels = realm.where(RealmPostMovie.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                postModels.deleteAllFromRealm();
            }
        });
    }

    public RealmPostSeries getPostSeries(int id) {
        realm.beginTransaction();
        RealmPostSeries m = realm.where(RealmPostSeries.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return m;
    }

    public List<RealmPostSeries> getAllPostSeries() {
        realm.beginTransaction();
        List<RealmPostSeries> series = new ArrayList<>(realm.where(RealmPostSeries.class).findAll());
        realm.commitTransaction();
        return series;
    }

    public List<RealmPostMovie> getAllPostMovies() {
        realm.beginTransaction();
        List<RealmPostMovie> movies = new ArrayList<>(realm.where(RealmPostMovie.class).findAll());
        realm.commitTransaction();
        return movies;
    }

    public void deleteAllPostSeries() {
        final RealmResults<RealmPostSeries> postModels = realm.where(RealmPostSeries.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                postModels.deleteAllFromRealm();
            }
        });
    }

    public void createPostMovie(final int id) {
        realm.beginTransaction();
        realm.createObject(RealmPostMovie.class, id);
        realm.commitTransaction();
    }

    public void createPostSeries(final int id) {
        realm.beginTransaction();
        realm.createObject(RealmPostSeries.class, id);
        realm.commitTransaction();
    }

    public void setMovieRating(RealmPostMovie p, String s) {
        realm.beginTransaction();
        if (p != null)
            p.setRating(s);
        realm.commitTransaction();
    }

    public void setMovieWatchlist(RealmPostMovie p, boolean b) {
        realm.beginTransaction();
        if (p != null)
            p.setInWatchlist(b);
        realm.commitTransaction();
    }

    public void setMovieFavorite(RealmPostMovie p, boolean b) {
        realm.beginTransaction();
        if (p != null)
            p.setFavorite(b);
        realm.commitTransaction();
    }

    public void setSeriesRating(RealmPostSeries p, String s) {
        realm.beginTransaction();
        if (p != null)
            p.setRating(s);
        realm.commitTransaction();
    }

    public void setSeriesWatchlist(RealmPostSeries p, boolean b) {
        realm.beginTransaction();
        if (p != null)
            p.setInWatchlist(b);
        realm.commitTransaction();
    }

    public void setSeriesFavorite(RealmPostSeries p, boolean b) {
        realm.beginTransaction();
        if (p != null)
            p.setFavorite(b);
        realm.commitTransaction();
    }

}


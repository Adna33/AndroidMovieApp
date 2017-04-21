package atlant.moviesapp.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Korisnik on 14.04.2017..
 */

@Root(name = "channel", strict = false)
public class Channel {
    @ElementList(inline = true, name="item")
    private List<News> news;

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}

package atlant.moviesapp.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import io.realm.RealmObject;

/**
 * Created by Korisnik on 14.04.2017..
 */

@Root(name = "item")
public class News extends RealmObject{

    @Element(name = "title")
    private String title;

    @Element(name = "link")
    private String link;

    @Element(name = "description", required = false)
    private String description;

    @Element(name = "author")
    private String author;

    @Element(name = "pubDate")
    private String pubdate;

    @Element(name = "guid")
    private String guid;

    public News(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}

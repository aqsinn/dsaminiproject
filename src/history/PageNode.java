package history;

import java.time.LocalDateTime;

public class PageNode {
    private final String url;
    private final String title;
    private final LocalDateTime timestamp;

    PageNode prev;
    PageNode next;

    public PageNode(String url, String title) {
        this.url = url;
        this.title = (title == null || title.isEmpty()) ? url : title;
        this.timestamp = LocalDateTime.now();
    }

    public PageNode(String url) {
        this(url, null);
    }

    public String getUrl()              { return url; }
    public String getTitle()            { return title; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public PageNode getPrev()           { return prev; }
    public PageNode getNext()           { return next; }

    @Override
    public String toString() {
        return title + " (" + url + ")";
    }
}

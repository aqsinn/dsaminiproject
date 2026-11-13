package history;

import java.time.LocalDateTime;

// one visited page
public class PageNode {
    private final String url;
    private final LocalDateTime timestamp;

    // links to neighbours, only the list and history touch these
    PageNode prev;
    PageNode next;

    public PageNode(String url) {
        this.url = url;
        this.timestamp = LocalDateTime.now();
    }

    // used when importing history from a file (keeps the saved time)
    public PageNode(String url, LocalDateTime timestamp) {
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getUrl()              { return url; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public PageNode getPrev()           { return prev; }
    public PageNode getNext()           { return next; }
}

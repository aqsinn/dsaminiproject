package history;

import java.util.List;

public class BrowserHistory {

    private static final int MAX_HISTORY = 50;

    private DoublyLinkedList history;
    private PageNode current;

    public BrowserHistory(String homepage) {
        history = new DoublyLinkedList();
        PageNode home = new PageNode(homepage);
        history.insertAfter(null, home);
        current = home;
    }

    // drop forward pages, add new one, move to it
    public void visit(String url) {
        history.deleteAfter(current);
        PageNode newPage = new PageNode(url);
        history.insertAfter(current, newPage);
        current = newPage;

        // drop oldest page when over the cap
        if (history.size() > MAX_HISTORY) {
            history.removeFirst();
        }
    }

    // go back up to steps pages
    public String back(int steps) {
        for (int i = 0; i < steps; i++) {
            if (current.getPrev() == null) {
                break;
            }
            current = current.getPrev();
        }
        return current.getUrl();
    }

    // go forward up to steps pages
    public String forward(int steps) {
        for (int i = 0; i < steps; i++) {
            if (current.getNext() == null) {
                break;
            }
            current = current.getNext();
        }
        return current.getUrl();
    }

    // current page, no state change
    public PageNode current_page() {
        return current;
    }

    public boolean canGoBack() {
        return current.getPrev() != null;
    }

    public boolean canGoForward() {
        return current.getNext() != null;
    }

    // all pages, oldest first
    public List<PageNode> getAllPages() {
        return history.toList();
    }

    // print history, current page marked
    public void display_history() {
        PageNode temp = history.getHead();
        System.out.println("Browsing History:");
        while (temp != null) {
            if (temp == current) {
                System.out.println(temp.getUrl() + " [<]");
            } else {
                System.out.println(temp.getUrl());
            }
            temp = temp.getNext();
        }
    }
}

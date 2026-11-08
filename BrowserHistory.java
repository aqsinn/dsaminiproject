package history;

public class BrowserHistory {

    private DoublyLinkedList history;
    private PageNode current;
    private static final int MAX_HISTORY = 50;
    // Constructor
    public BrowserHistory(String homepage) {

        history = new DoublyLinkedList();

        PageNode home = new PageNode(homepage);

        history.insertAfter(null, home);

        current = home;
    }

    // Visit a new page
       public void visit(String url) {

    history.deleteAfter(current);

    PageNode newPage = new PageNode(url);

    history.insertAfter(current, newPage);

    current = newPage;

    // Check maximum history capacity
    if (history.size() > MAX_HISTORY) {
        history.removeFirst();
    }
}

    // Move backward
    public String back(int steps) {

        for (int i = 0; i < steps; i++) {

            if (current.getPrev() == null) {
                break;
            }

            current = current.getPrev();
        }

        return current.getUrl();
    }

    // Move forward
    public String forward(int steps) {

        for (int i = 0; i < steps; i++) {

            if (current.getNext() == null) {
                break;
            }

            current = current.getNext();
        }

        return current.getUrl();
    }

    // Return current page
    public PageNode current_page() {
        return current;
    }

    // Display complete history
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

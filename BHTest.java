package history;

public class BrowserHistoryTest {

    public static void main(String[] args) {

        BrowserHistory browser =
                new BrowserHistory("google.com");

        browser.visit("youtube.com");
        browser.visit("facebook.com");
        browser.visit("instagram.com");

        System.out.println("Initial history:");
        browser.display_history();

        System.out.println("\nBack 2 steps:");
        System.out.println(browser.back(2));

        browser.display_history();

        System.out.println("\nForward 1 step:");
        System.out.println(browser.forward(1));

        browser.display_history();

        System.out.println("\nVisit a new page:");
        browser.visit("linkedin.com");

        browser.display_history();

        System.out.println("\nCurrent page information:");

        PageNode page = browser.current_page();

        System.out.println("URL: " + page.getUrl());
        System.out.println("Title: " + page.getTitle());
        System.out.println("Timestamp: " + page.getTimestamp());
    }
}

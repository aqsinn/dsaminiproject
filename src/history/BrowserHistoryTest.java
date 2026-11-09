package history;

public class BrowserHistoryTest {
    public static void main(String[] args) {
        BrowserHistory b = new BrowserHistory("home.com");

        // fresh browser
        assert b.current_page().getUrl().equals("home.com");
        assert !b.canGoBack() && !b.canGoForward();
        assert b.back(5).equals("home.com");
        assert b.forward(5).equals("home.com");

        // visit a, b, c
        b.visit("a.com");
        b.visit("b.com");
        b.visit("c.com");
        assert b.current_page().getUrl().equals("c.com");
        assert b.canGoBack() && !b.canGoForward();
        assert b.getAllPages().size() == 4;

        // back 2, then visit drops forward history
        assert b.back(2).equals("a.com");
        assert b.canGoBack() && b.canGoForward();
        b.visit("d.com");
        assert b.current_page().getUrl().equals("d.com");
        assert !b.canGoForward();
        assert b.forward(1).equals("d.com");
        assert b.getAllPages().size() == 3;

        // boundaries
        assert b.back(100).equals("home.com");
        assert !b.canGoBack() && b.canGoForward();
        assert b.forward(100).equals("d.com");

        // zero and negative steps
        assert b.back(0).equals("d.com");
        assert b.forward(0).equals("d.com");
        assert b.back(-3).equals("d.com");

        // current_page does not change state
        b.back(1);
        b.current_page();
        assert b.current_page().getUrl().equals("a.com");

        // 50 page cap
        BrowserHistory c = new BrowserHistory("p0");
        for (int i = 1; i <= 60; i++) c.visit("p" + i);
        assert c.getAllPages().size() == 50;
        assert c.back(1000).equals("p11");
        assert c.forward(1000).equals("p60");

        System.out.println("all browser history tests passed");
    }
}

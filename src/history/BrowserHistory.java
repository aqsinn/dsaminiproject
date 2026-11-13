package history;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class BrowserHistory {

    private static final int MAX_HISTORY = 50;

    private final String homepage;
    private DoublyLinkedList history;
    private PageNode current;
    private final DeletedStack deleted = new DeletedStack();   // for restore

    public BrowserHistory(String homepage) {
        this.homepage = homepage;
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
    public PageNode[] getAllPages() {
        return history.toArray();
    }

    // print the whole chain head to tail, [<] marks the current page
    public void displayHistory() {
        int i = 1;
        for (PageNode n = history.getHead(); n != null; n = n.getNext(), i++) {
            System.out.println(i + ". " + n.getUrl() + (n == current ? "  [<]" : ""));
        }
    }

    // delete all history, start over at the homepage (restore stack is cleared too)
    public void deleteHistory() {
        history.clear();
        deleted.clear();
        PageNode home = new PageNode(homepage);
        history.insertAfter(null, home);
        current = home;
    }

    // delete the page we are on, cursor moves to the previous page (or next if first)
    // the last remaining page can't be deleted, returns false then
    public boolean deleteCurrentPage() {
        if (history.size() <= 1) return false;
        PageNode victim = current;
        PageNode anchor = victim.getPrev();
        current = (anchor != null) ? anchor : victim.getNext();
        history.remove(victim);
        deleted.push(victim, anchor);
        return true;
    }

    // delete every page with this url, returns how many were deleted
    // always keeps at least one page
    public int deleteUrl(String url) {
        int count = 0;
        PageNode n = history.getHead();
        while (n != null && history.size() > 1) {
            PageNode nxt = n.getNext();
            if (n.getUrl().equals(url)) {
                PageNode anchor = n.getPrev();
                if (n == current) {
                    current = (anchor != null) ? anchor : n.getNext();
                }
                history.remove(n);
                deleted.push(n, anchor);
                count++;
            }
            n = nxt;
        }
        return count;
    }

    // put the most recently deleted page back where it was
    // returns its url, or null if nothing to restore
    public String restoreLastDeleted() {
        DeletedStack.Entry e = deleted.pop();
        if (e == null) return null;
        if (e.anchor != null && history.contains(e.anchor)) {
            history.insertAfter(e.anchor, e.page);
        } else {
            history.insertFirst(e.page);
        }
        enforceCapacity();
        return e.page.getUrl();
    }

    public boolean canRestore() {
        return !deleted.isEmpty();
    }

    // save history to a txt file: first line is the cursor index,
    // then one "timestamp <tab> url" line per page, oldest first
    public void exportHistory(String path) throws IOException {
        int idx = 0;
        int curIdx = 0;
        for (PageNode n = history.getHead(); n != null; n = n.getNext(), idx++) {
            if (n == current) curIdx = idx;
        }
        try (BufferedWriter w = new BufferedWriter(new FileWriter(path))) {
            w.write("CURRENT " + curIdx);
            w.newLine();
            for (PageNode n = history.getHead(); n != null; n = n.getNext()) {
                w.write(n.getTimestamp() + "\t" + n.getUrl());
                w.newLine();
            }
        }
    }

    // load history from a file made by exportHistory
    // the file is fully checked first, so a bad file leaves current history untouched
    public void importHistory(String path) throws IOException {
        DoublyLinkedList loaded = new DoublyLinkedList();
        PageNode last = null;
        int curIdx;

        try (BufferedReader r = new BufferedReader(new FileReader(path))) {
            String header = r.readLine();
            if (header == null || !header.startsWith("CURRENT ")) {
                throw new IOException("Not a history file");
            }
            try {
                curIdx = Integer.parseInt(header.substring(8).trim());
            } catch (NumberFormatException ex) {
                throw new IOException("Bad cursor position in header");
            }

            String line;
            while ((line = r.readLine()) != null) {
                if (line.isEmpty()) continue;
                int tab = line.indexOf('\t');
                if (tab < 0) throw new IOException("Bad line: " + line);
                LocalDateTime ts;
                try {
                    ts = LocalDateTime.parse(line.substring(0, tab));
                } catch (DateTimeParseException ex) {
                    throw new IOException("Bad timestamp in line: " + line);
                }
                PageNode p = new PageNode(line.substring(tab + 1), ts);
                loaded.insertAfter(last, p);
                last = p;
            }
        }

        if (loaded.isEmpty() || curIdx < 0 || curIdx >= loaded.size()) {
            throw new IOException("History file is empty or cursor is out of range");
        }

        PageNode cur = loaded.getHead();
        for (int i = 0; i < curIdx; i++) cur = cur.getNext();

        history = loaded;
        current = cur;
        deleted.clear();
        enforceCapacity();
    }

    // prune oldest pages until we fit the cap again
    private void enforceCapacity() {
        while (history.size() > MAX_HISTORY) {
            PageNode removed = history.removeFirst();
            if (removed == current) current = history.getHead();
        }
    }
}

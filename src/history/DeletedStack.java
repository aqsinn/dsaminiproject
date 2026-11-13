package history;

// stack of deleted pages, built by hand (linked nodes, no java.util)
// each entry also remembers the page that was just before it,
// so restore can put it back in the same spot
public class DeletedStack {

    static class Entry {
        final PageNode page;
        final PageNode anchor;   // page that came before it (null if it was first)
        Entry below;

        Entry(PageNode page, PageNode anchor) {
            this.page = page;
            this.anchor = anchor;
        }
    }

    private Entry top;
    private int size;

    public boolean isEmpty() { return size == 0; }
    public int size()        { return size; }

    public void push(PageNode page, PageNode anchor) {
        Entry e = new Entry(page, anchor);
        e.below = top;
        top = e;
        size++;
    }

    // returns null when empty
    Entry pop() {
        if (top == null) return null;
        Entry e = top;
        top = e.below;
        e.below = null;
        size--;
        return e;
    }

    public void clear() {
        top = null;
        size = 0;
    }
}

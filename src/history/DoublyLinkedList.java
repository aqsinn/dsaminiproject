package history;

// doubly linked list of pages (no java.util collections used)
public class DoublyLinkedList {
    private PageNode head;
    private int size;

    public boolean isEmpty() { return size == 0; }
    public int size()        { return size; }
    public PageNode getHead() { return head; }

    // insert newNode after node, o(1)
    public PageNode insertAfter(PageNode node, PageNode newNode) {
        if (newNode == null) throw new IllegalArgumentException("newNode is null");

        // first node of an empty list
        if (node == null) {
            if (!isEmpty()) throw new IllegalStateException("node is null but list is not empty");
            head = newNode;
            newNode.prev = newNode.next = null;
            size = 1;
            return newNode;
        }

        newNode.prev = node;
        newNode.next = node.next;
        if (node.next != null) node.next.prev = newNode;
        node.next = newNode;
        size++;
        return newNode;
    }

    // insert at the front of the list, o(1)
    public PageNode insertFirst(PageNode newNode) {
        if (newNode == null) throw new IllegalArgumentException("newNode is null");
        newNode.prev = null;
        newNode.next = head;
        if (head != null) head.prev = newNode;
        head = newNode;
        size++;
        return newNode;
    }

    // remove the oldest page, o(1)
    public PageNode removeFirst() {
        if (head == null) return null;
        PageNode removed = head;
        head = head.next;
        if (head != null) head.prev = null;
        removed.next = null;
        size--;
        return removed;
    }

    // unlink one node from anywhere in the list, o(1)
    // node must already be in this list
    public PageNode remove(PageNode node) {
        if (node == null) return null;
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;
        if (node.next != null) node.next.prev = node.prev;
        node.prev = null;
        node.next = null;
        size--;
        return node;
    }

    // remove everything after node, returns how many
    public int deleteAfter(PageNode node) {
        if (node == null) return 0;
        int count = 0;
        PageNode cur = node.next;
        while (cur != null) {
            PageNode nxt = cur.next;
            cur.prev = null;
            cur.next = null;
            cur = nxt;
            count++;
        }
        node.next = null;
        size -= count;
        return count;
    }

    // delete every node
    public void clear() {
        PageNode cur = head;
        while (cur != null) {
            PageNode nxt = cur.next;
            cur.prev = null;
            cur.next = null;
            cur = nxt;
        }
        head = null;
        size = 0;
    }

    // is this exact node still in the list? o(n)
    public boolean contains(PageNode node) {
        for (PageNode n = head; n != null; n = n.next) {
            if (n == node) return true;
        }
        return false;
    }

    // plain array copy of the list, head to tail
    public PageNode[] toArray() {
        PageNode[] out = new PageNode[size];
        int i = 0;
        for (PageNode n = head; n != null; n = n.next) out[i++] = n;
        return out;
    }
}

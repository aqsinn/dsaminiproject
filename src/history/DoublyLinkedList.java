package history;

import java.util.ArrayList;
import java.util.List;

// doubly linked list of pages
public class DoublyLinkedList {
    private PageNode head;
    private int size;

    public boolean isEmpty() { return size == 0; }
    public int size()        { return size; }

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

    // copy of the list, head to tail
    public List<PageNode> toList() {
        List<PageNode> out = new ArrayList<>(size);
        for (PageNode n = head; n != null; n = n.next) out.add(n);
        return out;
    }
}

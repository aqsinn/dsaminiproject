package history;

import java.util.ArrayList;
import java.util.List;

public class DoublyLinkedList {
    private PageNode head;
    private PageNode tail;
    private int size;

    public boolean isEmpty()    { return size == 0; }
    public int size()           { return size; }
    public PageNode getHead()   { return head; }
    public PageNode getTail()   { return tail; }


    public PageNode insertAfter(PageNode node, PageNode newNode) {
        if (newNode == null) throw new IllegalArgumentException("newNode is null");

        if (node == null) {
            if (!isEmpty()) throw new IllegalStateException("node is null but list is not empty");
            head = tail = newNode;
            newNode.prev = newNode.next = null;
            size = 1;
            return newNode;
        }

        newNode.prev = node;
        newNode.next = node.next;
        if (node.next != null) node.next.prev = newNode;
        else tail = newNode;                 // inserted at the end
        node.next = newNode;
        size++;
        return newNode;
    }


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
        tail = node;
        size -= count;
        return count;
    }

    public void clear() {
        if (head != null) {
            PageNode cur = head;
            while (cur != null) {
                PageNode nxt = cur.next;
                cur.prev = cur.next = null;
                cur = nxt;
            }
        }
        head = tail = null;
        size = 0;
    }

    public List<PageNode> toList() {
        List<PageNode> out = new ArrayList<>(size);
        for (PageNode n = head; n != null; n = n.next) out.add(n);
        return out;
    }
}

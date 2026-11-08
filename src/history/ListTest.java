package history;

public class ListTest {
    public static void main(String[] args) {
        DoublyLinkedList list = new DoublyLinkedList();
        assert list.isEmpty() && list.size() == 0;
        assert list.deleteAfter(null) == 0;

        PageNode a = list.insertAfter(null, new PageNode("a.com", "A"));
        assert list.getHead() == a && list.getTail() == a && list.size() == 1;
        assert a.getPrev() == null && a.getNext() == null;
        assert list.deleteAfter(a) == 0;

        PageNode b = list.insertAfter(a, new PageNode("b.com"));
        PageNode c = list.insertAfter(b, new PageNode("c.com"));
        assert list.size() == 3 && list.getTail() == c;
        assert a.getNext() == b && b.getPrev() == a && b.getNext() == c && c.getPrev() == b;

        PageNode x = list.insertAfter(a, new PageNode("x.com"));
        assert a.getNext() == x && x.getPrev() == a && x.getNext() == b && b.getPrev() == x;
        assert list.size() == 4 && list.toList().size() == 4;

        assert list.deleteAfter(x) == 2;
        assert x.getNext() == null && list.getTail() == x && list.size() == 2;
        assert b.getPrev() == null && c.getPrev() == null;   // fully detached

        list.clear();
        assert list.isEmpty() && list.getHead() == null && list.getTail() == null;

        System.out.println("All list tests passed");
    }
}

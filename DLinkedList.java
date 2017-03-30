package cse2010.assignment2;

public class DLinkedList<T> {

    private Node<T> header;
    private Node<T> trailer;
    private int size = 0;

    public DLinkedList() {
        /**/
        size = 0;
        header = new Node(null , null , null);
        trailer = new Node(null , header , null);
        header.setNext(trailer);

    }

    public void setHeaderInfo(T info) {
        header.setItem(info);
    }

    public void setTrailerInfo(T info) {
        trailer.setItem(info);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() { return size; }

    public Node<T> getFirst() {
        return header.getNext();
    }

    public Node<T> getLast() {
        return trailer.getPrev();
    }

    public void addFirst(Node<T> n) {
        /**/
        addAfter(header , n);
    }

    public void addLast(Node<T> n) {
        /**/
        addBefore(trailer , n);
    }

    public T removeFirst(Node<T> n) {
        /**/
        remove(header.getNext());
        return null;
    }

    public T removeLast() {
        /**/
        remove(trailer.getPrev());
        return null;
    }

    public void addAfter(Node<T> p, Node<T> n) {
       /**/
        n.setNext(p.getNext());
        n.setPrev(p);
        p.getNext().setPrev(n);
        p.setNext(n);
        size++;
    }

    public void addBefore(Node<T> p, Node<T> n) {
        /**/
        n.setNext(p);
        n.setPrev(p.getPrev());
        p.getPrev().setNext(n);
        p.setPrev(n);
        size++;
    }

    public T remove(Node<T> n) {
        /**/
        n.getPrev().setNext(n.getNext());
        n.getNext().setPrev(n.getPrev());
        n.setNext(null);
        n.setPrev(null);
        size--;
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(
            "List: size = " + size + " [");
        Node<T> current = header.getNext();

        while (current != trailer) {
            builder.append(current.getItem().toString());
            current = current.getNext();
        }
        builder.append("]");

        return builder.toString();
    }
}

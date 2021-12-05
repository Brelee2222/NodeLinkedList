import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedArray<ElementType> implements LinkedArrayMethods<ElementType>, Iterable<ElementType>{
    private NodeLinked<ElementType> head = null;
    private NodeLinked<ElementType> tail = null;
    private NodeLinked<ElementType> trashedNodes = null;

    @Override
    public NodeLinked<ElementType> nodeReuse() {
        NodeLinked<ElementType> node;
        if(trashedNodes == null) {
            node = new NodeLinked<ElementType>();
        } else {
            node = trashedNodes;
            trashedNodes = trashedNodes.nodeNext;
            node.nodeNext = null;
            node.nodePrevious = null;
        }
        return node;
    }

    @Override
    public void nodeRecycle(NodeLinked node) {
        node.nodeNext = trashedNodes;
        trashedNodes = node;
    }

    @Override
    public void nodeRecycleList() {
        NodeLinked node = trashedNodes;
        trashedNodes = head;
        tail.nodeNext = trashedNodes;
    }

    @Override
    public void addFirst(ElementType value) {
        NodeLinked<ElementType> node = nodeReuse();
        node.value = value;
        if(isEmpty()) {
            tail = node;
        } else {
            node.nodeNext = head;
            if(node.nodeNext != null)
                node.nodeNext.nodePrevious = node;
        }
        head = node;
    }

    @Override
    public void addLast(ElementType value) {
        NodeLinked<ElementType> node = nodeReuse();
        node.value = value;
        if(isEmpty()) {
            head = node;
        } else {
            node.nodePrevious = tail;
            if(node.nodePrevious != null)
                node.nodePrevious.nodeNext = node;
        }
        tail = node;
    }

    @Override
    public void add(int index, ElementType value) {
        if(!isEmpty() && index > 0) {
            if(index >= size()) {
                if(index != size())
                    throw new IndexOutOfBoundsException();
                addLast(value);
            } else {
                NodeLinked<ElementType> node = nodeReuse();
                node.value = value;
                NodeLinked next = getNode(index);
                NodeLinked prev = getNode(index-1);
                prev.nodeNext = node;
                next.nodePrevious = node;
                node.nodeNext = next;
                node.nodePrevious = prev;
            }

        } else {
            if(index < 0)
                throw new IndexOutOfBoundsException();
            addFirst(value);
        }
    }

    @Override
    public void add(ElementType value) {
        addLast(value);
    }

    @Override
    public void addFirstRecover() {
        NodeLinked<ElementType> node = nodeReuse();
        if(isEmpty()) {
            tail = node;
        } else {
            node.nodeNext = head;
            if(node.nodeNext != null)
                node.nodeNext.nodePrevious = node;
        }
        head = node;
    }

    @Override
    public void addLastRecover() {
        NodeLinked<ElementType> node = nodeReuse();
        if(isEmpty()) {
            head = node;
        } else {
            node.nodePrevious = tail;
            if(node.nodePrevious != null)
                node.nodePrevious.nodeNext = node;
        }
        tail = node;
    }

    @Override
    public void set(int index, ElementType value) {
        NodeLinked node = getNode(index);
        node.value = value;
    }

    @Override
    public int size() {
        NodeLinked node = head;
        int length = 0;
        while (node != null) {
            node = node.nodeNext;
            length++;
        }
        return length;
    }

    @Override
    public ElementType get(int index) {
        return (ElementType) getNode(index).value;
    }

    @Override
    public NodeLinked getNode(int index) {
        if (size() <= index || index < 0)
            throw new IndexOutOfBoundsException("Index out of bounds");
        int point = 0;
        NodeLinked<ElementType> node = head;
        while (point < index){
            node = node.nodeNext;
            point++;
        }
        return node;
    }

    @Override
    public ElementType getFirst() {
        if(isEmpty())
            throw new NoSuchElementException();
        return head.value;
    }

    @Override
    public ElementType getLast() {
        if(isEmpty())
            throw new NoSuchElementException();
        return tail.value;
    }

    @Override
    public NodeLinked<ElementType> getFirstNode() {
        return head;
    }

    @Override
    public NodeLinked<ElementType> getLastNode() {
        return tail;
    }

    @Override
    public void remove(int index) {
        NodeLinked node = getNode(index);
        if(head == node) {
            removeFirst();
        } else {
            if(tail != node) {
                node.nodeNext.nodePrevious = node.nodePrevious;
                node.nodePrevious.nodeNext = node.nodeNext;
                nodeRecycle(node);
            } else {
                removeLast();
            }
        }
    }

    @Override
    public void removeFirst() {
        if(head == tail && !isEmpty()) {
            nodeRecycle(head);
            head = null;
            tail = null;
        } else {
            NodeLinked node = head;
            head = head.nodeNext;
            head.nodePrevious = null;
            nodeRecycle(node);
        }
    }

    @Override
    public void removeLast() {
        if(tail == head && !isEmpty()) {
            nodeRecycle(head);
            head = null;
            tail = null;
        } else {
            NodeLinked node = tail;
            tail = tail.nodePrevious;
            tail.nodeNext = null;
            nodeRecycle(node);
        }
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void clear() {
        nodeRecycleList();
        head = null;
        tail = null;
    }

    @Override
    public int indexOf(ElementType value) {
        NodeLinked node = head;
        int index = 1;
        while(node != null) {
            if(value != node.value) {
                node = node.nodeNext;
                index++;
            } else {
                break;
            }
        }
        if(node != null)
            return index;
        else
            return -1;
    }

    @Override
    public boolean contains(ElementType value) {
        return indexOf(value) != -1;
    }

    @Override
    public Iterator<ElementType> iterator() {
        return new Iterator() {
            NodeLinked<ElementType> node = head;
            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public Object next() {
                NodeLinked nodeOld = node;
                node = node.nodeNext;
                return nodeOld.value;
            }
        };
    }
}

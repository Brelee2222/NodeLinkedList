import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedArray<ElementType> implements LinkedArrayMethods<ElementType>, Iterable<ElementType>{
    private NodeLinked<ElementType> head = null;
    private NodeLinked<ElementType> tail = null;
    private NodeLinked<ElementType> trashedNodes = null;

    //debug to check if there's a problem with being empty (Fixed)
    public boolean nodeMessCheck() {
        return head == null && null != tail;
    }

    //Checks if trashed Nodes is empty, if so, it makes a new Class of type NodeLinked, otherwise, it returns trashedNodes (Fixed Speed)
    private NodeLinked<ElementType> nodeReuse() {
        NodeLinked<ElementType> node;
        if(trashedNodes == null) {
            node = new NodeLinked();
        } else {
            node = trashedNodes;
            trashedNodes = trashedNodes.nodeNext;
            node.nodeNext = null;
            node.nodePrevious = null;
        }
        return node;
    }

    //Stores a given class address of type NodeLinked (Fixed Speed)
    private void nodeRecycle(NodeLinked node) {
        node.nodeNext = trashedNodes;
        trashedNodes = node;
    }

    //moves trashedNodes to the end of the list so that it can move head to trashedNodes (Fixed Speed)
    private void nodeRecycleList() {
        tail.nodeNext = trashedNodes;
        trashedNodes = head;
    }

    //Checks if isEmpty, if so head and tail will be mutual, if not do add first/last (Fixed Speed)
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

    //checks if index is 0 or size (T = O*N)
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

    //(Fixed Speed)
    @Override
    public void add(ElementType value) {
        addLast(value);
    }

    //because there's no point in removing the value of a node, it does add first, but without setting a value (Fixed Speed)
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

    //checks if index is out of range, if not set value of node to value (T = O*N)
    @Override
    public void set(int index, ElementType value) {
        NodeLinked node = getNode(index);
        node.value = value;
    }

    //counts size. there's no need in making a new variable for size, because the return type is restricted by int. (T = O*N)
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

    //gets node, and returns node's value (T = O*N)
    @Override
    public ElementType get(int index) {
        return (ElementType) getNode(index).value;
    }

    //gets node (T = O*N)
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

    //(Fixed Speed)
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

    //removes links of node, then sends it to recycle (T = O*N)
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

    //because head and tail are private, if head is null, you can't get to tail therefore if head == null, isEmpty() == true (Fixed Speed)
    @Override
    public boolean isEmpty() {
        return head == null;
    }

    //sets head and tail to null, and sends list to recycle
    @Override
    public void clear() {
        nodeRecycleList();
        head = null;
        tail = null;
    }

    //same concept as isPrime() (T = O*N)
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

    //runs indexOf, and checks is value is not equal to -1
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

    @Override
    public LinkedArray<ElementType> listCopy() {
        LinkedArray<ElementType> ListCopy = new LinkedArray<>();
        for(ElementType node : this) {
            ListCopy.addLast(node);
        }
        return ListCopy;
    }
}

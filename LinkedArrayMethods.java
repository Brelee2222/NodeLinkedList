public interface LinkedArrayMethods<ElementType> {
    //Node Recycle
    void nodeRecycle(NodeLinked node);
    void nodeRecycleList();
    NodeLinked<ElementType> nodeReuse();

    //*Node Add (Add, First, Last, FirstRecover, LastRecover, Set)

    /** Recovers are special adds that do not set node values*/
    void addFirst(ElementType value);
    void addLast(ElementType value);
    void add(ElementType value);
    void add(int index, ElementType value);
    void addFirstRecover();
    void addLastRecover();
    void set(int index, ElementType value);

    //Node Get (Get, First, Last, Node, NodeFirst, NodeLast)

    /**Get Node gets the entire node address. Found in places like remove*/
    ElementType get(int index);
    ElementType getFirst();
    ElementType getLast();
    NodeLinked<ElementType> getFirstNode();
    NodeLinked<ElementType> getLastNode();
    NodeLinked getNode(int index);

    //Node Remove (Remove, First, Last, Clear)
    void remove(int index);
    void removeFirst();
    void removeLast();
    void clear();

    //Node Others
    int size();
    int indexOf(ElementType value);
    boolean contains(ElementType value);
    boolean isEmpty();
}

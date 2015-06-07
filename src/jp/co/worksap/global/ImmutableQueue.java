package jp.co.worksap.global;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ImmutableQueue<E> {
    private List<E> data;

    public ImmutableQueue() {
        data = new ArrayList<E>();
    }

    public ImmutableQueue(List<E> data) {
        this.data = data;
    }

    private List<E> addClone(E e) {
        List<E> newData = new ArrayList<E>();
        newData.addAll(data);
        newData.add(e);
        return newData;
    }

    private List<E> removeClone() {
        List<E> newData = new ArrayList<E>();
        newData.addAll(data);
        newData.remove(0);
        return newData;
    }

    public ImmutableQueue<E> enqueue(E e) {
        if (e == null) {
            throw new IllegalArgumentException();
        }

        return new ImmutableQueue<E>(addClone(e));
    }

    public ImmutableQueue<E> dequeue() {
        if (this.data.isEmpty()) {
            throw new NoSuchElementException();
        }
        return new ImmutableQueue<E>(removeClone());
    }

    public E peek() {
        if (this.data.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.data.get(0);
    }

    public int size() {
        return data.size();
    }
}
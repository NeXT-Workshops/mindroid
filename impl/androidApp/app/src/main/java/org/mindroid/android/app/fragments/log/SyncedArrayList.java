package org.mindroid.android.app.fragments.log;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.*;

public class SyncedArrayList<T> implements List<T>,Serializable {

    private static final long serialVersionUID = -6390286979392093028L;

    private final ArrayList<T> list;

    public SyncedArrayList(final ArrayList<T> list){
        this.list = list;
    }

    @Override
    public synchronized int size() {
        return list.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
        return list.contains(o);
    }

    @NonNull
    @Override
    public synchronized Iterator iterator() {
        return list.iterator();
    }

    @NonNull
    @Override
    public synchronized Object[] toArray() {
        return list.toArray();
    }

    @Override
    public synchronized boolean add(T o) {
        return list.add(o);
    }

    @Override
    public synchronized boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public synchronized boolean addAll(@NonNull Collection collection) {
        return list.addAll(collection);
    }

    @Override
    public synchronized boolean addAll(int i, @NonNull Collection collection) {
        return list.addAll(i,collection);
    }

    @Override
    public synchronized void clear() {
        list.clear();
    }

    @Override
    public synchronized T get(int i) {
        return list.get(i);
    }

    @Override
    public synchronized T set(int i, T o) {
        return list.set(i,o);
    }

    @Override
    public synchronized void add(int i, T o) {
        list.add(i,o);
    }

    @Override
    public synchronized T remove(int i) {
        return list.remove(i);
    }

    @Override
    public synchronized int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @NonNull
    @Override
    public synchronized ListIterator listIterator() {
        return list.listIterator();
    }

    @NonNull
    @Override
    public synchronized ListIterator listIterator(int i) {
        return list.listIterator(i);
    }

    @NonNull
    @Override
    public synchronized List subList(int i, int i1) {
        return list.subList(i,i1);
    }

    @Override
    public synchronized boolean retainAll(@NonNull Collection collection) {
        return list.retainAll(collection);
    }

    @Override
    public synchronized boolean removeAll(@NonNull Collection collection) {
        return list.removeAll(collection);
    }

    @Override
    public synchronized boolean containsAll(@NonNull Collection collection) {
        return list.containsAll(collection);
    }

    @NonNull
    @Override
    public synchronized Object[] toArray(@NonNull Object[] objects) {
        return list.toArray(objects);
    }
}

package com.game.main.engine.util;

import java.util.List; /**
 * Created by SuperRainbowNinja on 23/01/2018.
 */
public class Queue<T> {
    public static final int DEFAULT_SIZE = 20;
    private Object[] objects;
    private int start;
    private int end;
    private int elementCount;

    public Queue() {
        this(DEFAULT_SIZE);
    }

    public Queue(int startSize) {
        objects = new Object[startSize];
        start = 0;
        end = 0;
        elementCount = 0;
    }

    public boolean isEmpty() {
        return elementCount == 0;
    }

    public T remove() {
        if (elementCount == 0) {
            return null;
        } else {
            elementCount--;
            int old = start;
            start = (start + 1) % objects.length;
            return (T)objects[old];
        }
    }

    public void add(T object) {
        elementCount++;
        if (elementCount > objects.length) {
            Object[] old = objects;
            objects = new Object[objects.length * 2];
            System.arraycopy(old, start, objects, start, old.length - start);

            if (end != 0) {
                int j = start + old.length - 1;
                System.arraycopy(old, 0, objects, j, end);
                end = (j + end)  % objects.length;
            } else {
                end = (start + old.length) % objects.length;
            }
        }
        objects[end] = object;
        end = (end + 1) % objects.length;
    }

    public void addAll(List<T> list) {
        list.forEach(this::add);
    }
}

/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xbup.web.xbcatalogweb.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Abstract class for paging.
 *
 * @version 0.1.23 2014/04/04
 * @author ExBin Project (http://exbin.org)
 * @param <T> entity class
 */
public abstract class PagedList<T> implements List<T> {

    @Override
    public boolean add(T e) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public void add(int index, T element) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public void clear() {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public boolean contains(Object o) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public int indexOf(Object o) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public Iterator<T> iterator() {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public T remove(int index) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public T set(int index, T element) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public Object[] toArray() {
        throw new RuntimeException("Operation not allowed");
    }

    @Override
    public <TT> TT[] toArray(TT[] a) {
        throw new RuntimeException("Operation not allowed");
    }
}

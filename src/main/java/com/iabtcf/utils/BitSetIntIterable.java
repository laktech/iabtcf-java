package com.iabtcf.utils;

/*-
 * #%L
 * IAB TCF Core Library
 * %%
 * Copyright (C) 2020 IAB Technology Laboratory, Inc
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An implementation of the IntIterable based on BitSet.
 */
public class BitSetIntIterable implements IntIterable {
    private final BitSet bs;

    public static final BitSetIntIterable EMPTY = new BitSetIntIterable(new BitSet());

    public BitSetIntIterable(BitSet bs) {
        this.bs = bs;
    }

    @Override
    public boolean isEmpty() {
        return bs.isEmpty();
    }

    @Override
    public boolean contains(int value) {
        try {
            return bs.get(value);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean containsAll(int... source) {
        for (int i = 0; i < source.length; i++) {
            if (!contains(source[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public IntIterator intIterator() {
        return new IntIterator() {
            int currentIndex = start();

            public int start() {
                if (bs.isEmpty()) {
                    return -1;
                }

                return bs.nextSetBit(0);
            }

            @Override
            public boolean hasNext() {
                return currentIndex != -1;
            }

            @Override
            public Integer next() {
                return nextInt();
            }

            @Override
            public int nextInt() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                int next = currentIndex;
                currentIndex = bs.nextSetBit(currentIndex + 1);
                return next;
            }
        };
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            final IntIterator internal = intIterator();

            @Override
            public boolean hasNext() {
                return internal.hasNext();
            }

            @Override
            public Integer next() {
                return internal.next();
            }
        };
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bs == null) ? 0 : bs.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BitSetIntIterable other = (BitSetIntIterable) obj;
        if (bs == null) {
            if (other.bs != null) {
                return false;
            }
        } else if (!bs.equals(other.bs)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return bs.toString();
    }
}

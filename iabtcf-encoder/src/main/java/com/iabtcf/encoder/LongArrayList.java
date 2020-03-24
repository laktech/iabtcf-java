package com.iabtcf.encoder;

/*-
 * #%L
 * IAB TCF Java Encoder Library
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

import java.util.Arrays;

class LongArrayList {
    private long[] array;
    private int size;

    public LongArrayList() {
        this(10);
    }

    public LongArrayList(int initialCapacity) {
        array = new long[initialCapacity];
    }

    public long getLong(int i) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return array[i];
    }

    public void clear() {
        size = 0;
    }

    public int size() {
        return size;
    }

    public void add(LongArrayList o) {
        if (array.length - size <= o.size()) {
            int m = Math.min(array.length, o.array.length) * 2;
            array = Arrays.copyOf(array, array.length + o.array.length + m);
        }
        System.arraycopy(o.array, 0, array, size, o.size());
        size += o.size();
    }

    public void add(long value) {
        if (size == array.length) {
            array = Arrays.copyOf(array, size * 2);
        }
        array[size++] = value;
    }
}

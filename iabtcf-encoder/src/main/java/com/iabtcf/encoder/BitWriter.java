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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.BitSet;

import com.iabtcf.FieldDefs;
import com.iabtcf.utils.IntIterable;
import com.iabtcf.utils.IntIterator;

/**
 * Provides the ability to construct a byte array that is iabtcf compliant. The BitWriter provides
 * the ability to append bits of various types and sizes in an effort to construct the byte array.
 */
class BitWriter {
    private static final long[] LONG_MASKS = new long[Long.SIZE + 1];

    static {
        for (int i = 0; i < Long.SIZE; i++) {
            LONG_MASKS[i] = (1L << i) - 1;
        }
        LONG_MASKS[Long.SIZE] = ~0L;
    }

    private final LongArrayList buffer = new LongArrayList();
    private int bitsRemaining = Long.SIZE;
    private long pending = 0L;
    private int precision = 0;

    public BitWriter() {
        this(0);
    }

    /**
     * The 'precision' parameter can be used for encoding fields that must occupy a fixed-number of
     * bits. These padding bits are only honored in {@link BitWriter#write(BitWriter)} and
     * {@link BitWriter#toByteArray()}
     */
    public BitWriter(int precision) {
        this.precision = precision;
    }

    public void write(boolean value) {
        write(value ? 1 : 0, 1);
    }

    /**
     * Writes an iabtcf encoded String of length specified by 'field'.
     */
    public void write(String str, FieldDefs field) {
        assert (field.getLength() / FieldDefs.CHAR.getLength() == str.length());
        byte[] b = str.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < b.length; i++) {
            write(b[i] - 'A', FieldDefs.CHAR);
        }
    }

    /**
     * Writes 'length' number of bits whose set bits are indicated by the position of the ints in
     * 'of'. The least significant bit starts at 1.
     *
     * @throws IndexOutOfBoundsException if 'of' contains an invalid index, <= 0.
     */
    public void write(IntIterable of, int length) {
        BitWriter bw = new BitWriter(length);
        BitSet bs = new BitSet();
        for (IntIterator i = of.intIterator(); i.hasNext();) {
            int nextInt = i.nextInt();
            if (nextInt <= 0) {
                throw new IndexOutOfBoundsException("invalid index: " + nextInt);
            }
            bs.set(nextInt - 1);
        }
        for (int i = 0; i < bs.length(); i++) {
            bw.write(bs.get(i));
        }
        write(bw);
    }

    /**
     * Writes a series of bits of 'field' length whose set bits are indicated by the position of the
     * ints in 'of'. The least significant bit starts at 1.
     *
     * @throws IndexOutOfBoundsException if 'of' contains an invalid index, <= 0.
     */
    public void write(IntIterable of, FieldDefs field) {
        write(of, field.getLength());
    }

    public void write(boolean data, FieldDefs field) {
        assert (field.getLength() == 1);
        write(data);
    }

    /**
     * Writes an iabtcf encoded instant value.
     */
    public void write(Instant i, FieldDefs field) {
        write(i, field.getLength());
    }

    /**
     * Writes an iabtcf encoded instant value.
     */
    public void write(Instant i, int length) {
        write(i.toEpochMilli() / 100, length);
    }

    /**
     * Writes up to 'field' length number of bits from 'data'.
     */
    public void write(long data, FieldDefs field) {
        write(data, field.getLength());
    }

    /**
     * Writes up to 'length' number of bits from 'data'.
     */
    public void write(long data, int length) {
        data &= LONG_MASKS[length];
        bitsRemaining -= length;
        precision -= length;

        if (bitsRemaining >= 0) {
            pending |= data << bitsRemaining;
        } else {
            buffer.add(pending |= data >>> -bitsRemaining);
            bitsRemaining += Long.SIZE;
            pending = data << bitsRemaining;
        }
    }

    /**
     * Writes bits encoded by the specified BitWriter. Padding bits, if any, are also appended.
     */
    public void write(BitWriter bw) {
        enforcePrecision();

        for (int i = 0; i < bw.buffer.size(); i++) {
            write(bw.buffer.getLong(i), Long.SIZE);
        }
        write(bw.pending >>> bw.bitsRemaining, Long.SIZE - bw.bitsRemaining);

        enforcePrecision(bw.precision);
    }

    /**
     * Returns the number of bits, including any padding bits that are to be written.
     */
    public int length() {
        return buffer.size() * Long.SIZE + (Long.SIZE - bitsRemaining) + ((precision >= 0) ? precision : 0);
    }

    /**
     * Returns the byte array.
     */
    public byte[] toByteArray() {
        enforcePrecision();

        int bytesToWrite = (Long.SIZE + (Byte.SIZE - 1) - bitsRemaining) >> 3;

        ByteBuffer bb = ByteBuffer.allocate(buffer.size() * (Long.SIZE / Byte.SIZE) + bytesToWrite);

        for (int i = 0; i < buffer.size(); i++) {
            bb.putLong(buffer.getLong(i));
        }
        for (int i = 0; i < bytesToWrite; i++) {
            bb.put((byte) (pending >> (Long.SIZE - Byte.SIZE - i * Byte.SIZE)));
        }

        return bb.array();
    }

    protected void enforcePrecision(int p) {
        if (p <= 0) {
            return;
        }

        for (int i = 0; i < p / Long.SIZE; i++) {
            write(0L, Long.SIZE);
        }

        write(0L, p % 64);
    }

    private void enforcePrecision() {
        enforcePrecision(precision);
        precision = 0;
    }
}

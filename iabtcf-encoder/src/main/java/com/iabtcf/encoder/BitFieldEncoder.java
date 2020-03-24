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

import java.util.BitSet;
import java.util.Iterator;

import com.iabtcf.FieldDefs;
import com.iabtcf.utils.IntIterable;
import com.iabtcf.utils.IntIterator;

/**
 * Encodes a bit / range field, typically used for encoding vendor lists.
 */
class BitFieldEncoder {
    private final BitSet vendors;
    private int maxVendorId;

    public BitFieldEncoder() {
        this(new BitSet(), -1);
    }

    public BitFieldEncoder(BitFieldEncoder prototype) {
        this(prototype.vendors.length() == 0 ? new BitSet() : prototype.vendors.get(0, prototype.vendors.length()),
                prototype.maxVendorId);
    }

    private BitFieldEncoder(BitSet vendors, int maxVendorId) {
        this.vendors = vendors;
        this.maxVendorId = maxVendorId;
    }

    public BitFieldEncoder add(int vendorId) {
        vendors.set(vendorId - 1);

        return this;
    }

    public BitFieldEncoder add(Iterable<Integer> vendorIds) {
        for (Iterator<Integer> i = vendorIds.iterator(); i.hasNext();) {
            add(i.next());
        }

        return this;
    }

    public BitFieldEncoder add(int... vendorIds) {
        for (int i = 0; i < vendorIds.length; i++) {
            add(vendorIds[i]);
        }

        return this;
    }

    public BitFieldEncoder add(IntIterable ii) {
        for (IntIterator i = ii.intIterator(); i.hasNext();) {
            add(i.nextInt());
        }

        return this;
    }

    /**
     * Emit the specified max vendor id. By default, the maximum vendorId that was added is emitted.
     */
    public BitFieldEncoder setMaxVendorId(int maxVendorId) {
        this.maxVendorId = maxVendorId;

        return this;
    }

    /**
     * Returns a BitWriter that represents the encoded vendor ids. The BitWriter may either encode a
     * bit field or a range encoding; depending on which is smaller.
     */
    public BitWriter build() {
        BitWriter bv = new BitWriter();

        if (vendors.length() == 0) {
            bv.write(0, FieldDefs.CORE_VENDOR_MAX_VENDOR_ID);
            bv.write(false, FieldDefs.CORE_VENDOR_IS_RANGE_ENCODING);
            return bv;
        }

        maxVendorId = maxVendorId == -1 ? vendors.length() : maxVendorId;

        // create the range bit section
        BitWriter rangeBits = new BitWriter();
        int idxSet = vendors.get(0) ? 0 : vendors.nextSetBit(0);
        int idxClr;
        int numEntries = 0;
        do {
            idxClr = vendors.nextClearBit(idxSet);
            int length = idxClr - idxSet;

            if (length == 1) {
                rangeBits.write(false, FieldDefs.CORE_VENDOR_IS_RANGE_ENCODING);
                rangeBits.write(idxSet + 1, FieldDefs.START_OR_ONLY_VENDOR_ID);
            } else {
                rangeBits.write(true, FieldDefs.CORE_VENDOR_IS_RANGE_ENCODING);
                rangeBits.write(idxSet + 1, FieldDefs.START_OR_ONLY_VENDOR_ID);
                rangeBits.write(length, FieldDefs.END_VENDOR_ID);
            }
            numEntries++;
        } while ((idxSet = vendors.nextSetBit(idxClr)) > 0 && rangeBits.length() < vendors.length());

        // emit max vendor id
        bv.write(maxVendorId, FieldDefs.CORE_VENDOR_MAX_VENDOR_ID);

        if (rangeBits.length() < vendors.length()) {
            // emit range bits
            bv.write(true, FieldDefs.IS_A_RANGE);

            bv.write(numEntries, FieldDefs.NUM_ENTRIES);
            bv.write(rangeBits);
        } else {
            // emit bit field
            bv.write(false, FieldDefs.IS_A_RANGE);

            byte[] bits = vendors.toByteArray();
            int rem = vendors.length() % Byte.SIZE;
            int n = bits.length - 1;
            if (rem == 0) {
                rem = Byte.SIZE;
            }

            for (int i = 0; i < n; i++) {
                bv.write(reverse(bits[i]), Byte.SIZE);
            }

            bv.write(reverse(bits[n]) >> (Byte.SIZE - rem), rem);
        }

        return bv;
    }

    static byte[] lookup = new byte[] {
            0x0, 0x8, 0x4, 0xC, 0x2, 0xA, 0x6, 0xE,
            0x1, 0x9, 0x5, 0xD, 0x3, 0xB, 0x7, 0xF};

    /**
     * Reverse the bits in n using lookup table technique.
     */
    private static byte reverse(byte n) {
        return (byte) ((lookup[n & 0xF] << 4) | lookup[n >>> 4]);
    }

}

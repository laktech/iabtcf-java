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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Base64;

import org.junit.Test;

import com.iabtcf.FieldDefs;
import com.iabtcf.decoder.TCString;
import com.iabtcf.utils.BitSetIntIterable;

public class BitFieldEncoderTest {
    /**
     * BOOzQoAOOzQoAAPAFSENCW-AIBACBAAABCA=
     *
     * @formatter:off
     *
     * {
     *  "cookieVersion": 1,
     *  "created": "2018-06-04T00:00:00.000Z",
     *  "lastUpdated": "2018-06-04T00:00:00.000Z",
     *  "cmpId": 15,
     *  "cmpVersion": 5,
     *  "consentScreen": 18,
     *  "consentLanguage": "EN",
     *  "vendorListVersion": 150,
     *  "purposeIdBitString": "111110000000001000000001",
     *  "maxVendorId": 32,
     *  "isRange": false,
     *  "vendorIdBitString": "10000000000000000000000010000100"
     * }
     * @formatter:on
     */
    @Test
    public void testVendorEncode() {
        BitWriter bw = new BitWriter();
        bw.write(1, 6);
        bw.write(Instant.parse("2018-06-04T00:00:00Z"), 36);
        bw.write(Instant.parse("2018-06-04T00:00:00Z"), 36);
        bw.write(15, FieldDefs.CORE_CMP_ID);
        bw.write(5, FieldDefs.CORE_CMP_VERSION);
        bw.write(18, FieldDefs.CORE_CONSENT_SCREEN);
        bw.write("EN", FieldDefs.CORE_CONSENT_LANGUAGE);
        bw.write(150, FieldDefs.CORE_VENDOR_LIST_VERSION);
        bw.write(BitSetIntIterable.of(1, 2, 3, 4, 5, 15, 24), FieldDefs.V1_PURPOSES_ALLOW);

        BitFieldEncoder bfe = new BitFieldEncoder()
            .setMaxVendorId(32)
            .add(1)
            .add(25, 30);

        bw.write(bfe.build());

        byte[] rv = bw.toByteArray();
        String str = Base64.getUrlEncoder().encodeToString(rv);

        TCString tcModel = TCString.decode(str);
        assertTrue(tcModel.getVendorConsent().contains(1));
        assertTrue(tcModel.getVendorConsent().contains(25));
        assertTrue(tcModel.getVendorConsent().contains(30));

        assertEquals("BOOzQoAOOzQoAAPAFSENCW-AIBACBAAABCA=", str);
    }
}

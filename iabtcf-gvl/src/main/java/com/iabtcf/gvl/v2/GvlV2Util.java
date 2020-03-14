package com.iabtcf.gvl.v2;

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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iabtcf.gvl.v2.dao.GvlData;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

public class GvlV2Util {
    public static final String DEFAULT_BASE_GVL_URL = "https://vendorlist.consensu.org/v2/archives/vendor-list-v";
    public static final String DEFAULT_GVL_URL_SUFFIX = ".json";

    /**
     * Given an http url, it can fetch the global vendor list in json format and
     * parse the json and convert it to a POJO
     *
     * @param url http url to fetch the global vendor list
     * @return GvlData object
     * @see GvlData
     */
    public static GvlData initializeVendorList(String url) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            URL gvlUrl = new URL(url);
            GvlData gvlData = objectMapper.readValue(gvlUrl, GvlData.class);
            return cleanup(gvlData);
        } catch (IOException ioe) {
            return null;
        }
    }

    /**
     * Converts global vendor list as a json byte array into a POJO
     *
     * @param jsonData global vendor list in json format as a byte array
     * @return GvlData object
     * @see GvlData
     */
    public static GvlData initializeVendorList(byte[] jsonData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GvlData gvlData = objectMapper.readValue(jsonData, GvlData.class);
        return cleanup(gvlData);
    }

    private static GvlData cleanup(GvlData gvlData) {
        gvlData.setVendors(
            gvlData.getVendors().entrySet().stream()
                .filter(e -> e.getValue().isActive())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
        return gvlData;
    }
}

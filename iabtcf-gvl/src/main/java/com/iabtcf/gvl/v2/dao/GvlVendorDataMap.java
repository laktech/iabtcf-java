package com.iabtcf.gvl.v2.dao;

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

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * Represent a immutable Vendor Data object for a specific vendor list version and a specific vendor.
 * All the fields are provided as Map for easier and faster access to the values
 */
public final class GvlVendorDataMap {

    private final int vendorId;
    private final Map<Integer, Integer> purposesMap;
    private final Map<Integer, Integer> legIntPurposesMap;
    private final Map<Integer, Integer> flexiblePurposesMap;

    public GvlVendorDataMap(int vendorId, List<Integer> purposes, List<Integer> liPurposes, List<Integer> flexiblePurposes) {
        this.vendorId = vendorId;
        if (purposes != null) {
            purposesMap = toMap(purposes);
        } else {
            purposesMap = null;
        }
        if (liPurposes != null) {
            legIntPurposesMap = toMap(liPurposes);
        } else {
            legIntPurposesMap = null;
        }
        if (flexiblePurposes != null) {
            flexiblePurposesMap = toMap(flexiblePurposes);
        } else {
            flexiblePurposesMap = null;
        }
    }

    public int getVendorId() {
        return vendorId;
    }

    public Map<Integer, Integer> getPurposesMap() {
        return purposesMap;
    }

    public Map<Integer, Integer> getLegIntPurposesMap() {
        return legIntPurposesMap;
    }

    public Map<Integer, Integer> getFlexiblePurposesMap() {
        return flexiblePurposesMap;
    }

    private <T> Map<T, T> toMap(List<T> list) {
        return list.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
    }
}

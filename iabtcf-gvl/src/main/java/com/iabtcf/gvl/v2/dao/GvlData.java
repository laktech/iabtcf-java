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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GvlData {

    private int gvlSpecificationVersion;
    private int vendorListVersion;
    private int tcfPolicyVersion;
    private Date lastUpdated;
    @JsonIgnore
    private Map<Integer, GvlPurposeData> purposes;
    @JsonIgnore
    private Map<Integer, GvlPurposeData> specialPurposes;
    @JsonIgnore
    private Map<Integer, GvlPurposeData> features;
    @JsonIgnore
    private Map<Integer, GvlPurposeData> specialFeatures;
    private Map<Integer, GvlVendorData> vendors;

    public int getGvlSpecificationVersion() {
        return gvlSpecificationVersion;
    }

    public void setGvlSpecificationVersion(final int gvlSpecificationVersion) {
        this.gvlSpecificationVersion = gvlSpecificationVersion;
    }

    public int getVendorListVersion() {
        return vendorListVersion;
    }

    public void setVendorListVersion(final int vendorListVersion) {
        this.vendorListVersion = vendorListVersion;
    }

    public int getTcfPolicyVersion() {
        return tcfPolicyVersion;
    }

    public void setTcfPolicyVersion(final int tcfPolicyVersion) {
        this.tcfPolicyVersion = tcfPolicyVersion;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Map<Integer, GvlPurposeData> getPurposes() {
        return purposes;
    }

    public void setPurposes(final Map<Integer, GvlPurposeData> purposes) {
        this.purposes = purposes;
    }

    public Map<Integer, GvlPurposeData> getSpecialPurposes() {
        return specialPurposes;
    }

    public void setSpecialPurposes(final Map<Integer, GvlPurposeData> specialPurposes) {
        this.specialPurposes = specialPurposes;
    }

    public Map<Integer, GvlPurposeData> getFeatures() {
        return features;
    }

    public void setFeatures(final Map<Integer, GvlPurposeData> features) {
        this.features = features;
    }

    public Map<Integer, GvlPurposeData> getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(final Map<Integer, GvlPurposeData> specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public Map<Integer, GvlVendorData> getVendors() {
        return vendors;
    }

    public void setVendors(final Map<Integer, GvlVendorData> vendors) {
        this.vendors = vendors;
    }
}


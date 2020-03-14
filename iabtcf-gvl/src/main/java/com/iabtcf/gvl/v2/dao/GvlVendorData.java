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

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GvlVendorData {

    private int id;
    @JsonIgnore
    private String name;
    private List<Integer> purposes;
    private List<Integer> legIntPurposes;
    private List<Integer> flexiblePurposes;
    @JsonIgnore
    private List<Integer> specialPurposes;
    @JsonIgnore
    private List<Integer> features;
    @JsonIgnore
    private List<Integer> specialFeatures;
    @JsonIgnore
    private String policyUrl;
    private String deletedDate;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Integer> getPurposes() {
        return purposes;
    }

    public void setPurposes(final List<Integer> purposes) {
        this.purposes = purposes;
    }

    public List<Integer> getLegIntPurposes() {
        return legIntPurposes;
    }

    public void setLegIntPurposes(final List<Integer> legIntPurposes) {
        this.legIntPurposes = legIntPurposes;
    }

    public List<Integer> getFlexiblePurposes() {
        return flexiblePurposes;
    }

    public void setFlexiblePurposes(final List<Integer> flexiblePurposes) {
        this.flexiblePurposes = flexiblePurposes;
    }

    public List<Integer> getSpecialPurposes() {
        return specialPurposes;
    }

    public void setSpecialPurposes(final List<Integer> specialPurposes) {
        this.specialPurposes = specialPurposes;
    }

    public List<Integer> getFeatures() {
        return features;
    }

    public void setFeatures(final List<Integer> features) {
        this.features = features;
    }

    public List<Integer> getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(final List<Integer> specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public String getPolicyUrl() {
        return policyUrl;
    }

    public void setPolicyUrl(final String policyUrl) {
        this.policyUrl = policyUrl;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(final String deletedDate) {
        this.deletedDate = deletedDate;
    }

    public boolean isDeleted() {
        return Optional.ofNullable(this.deletedDate)
            .map(Instant::parse)
            .map(deleteAfterDate -> deleteAfterDate.isBefore(Instant.now()))
            .orElse(false);
    }

    public boolean isActive() {
        return !isDeleted();
    }
}

package com.news.Model.ResponseClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuardianResponse {

    @JsonProperty("response")
    private GuardianResponseData response;

    public GuardianResponseData getResponseData() {
        return response;
    }

    public void setResponseData(GuardianResponseData responseData) {
        this.response = responseData;
    }

    public GuardianResponse(GuardianResponseData responseData) {
        this.response = responseData;
    }

    public GuardianResponse() {
    }

    @Override
    public String toString() {
        return "GuardianResponse{" +
                "responseData=" + response +
                '}';
    }
}

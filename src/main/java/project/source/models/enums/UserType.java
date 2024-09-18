package project.source.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserType {
    @JsonProperty("hotel")
    HOTEL,
    @JsonProperty("admin")
    ADMIN,
    @JsonProperty("user")
    USER;
}
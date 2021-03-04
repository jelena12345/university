package com.foxminded.dto;

import javax.validation.constraints.NotBlank;

public class AccountCredentials {

    @NotBlank(message = "PersonalId can't be blank.")
    private String personalId;

    public AccountCredentials(String personalId) {
        this.personalId = personalId;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}

package org.atostest.services.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class ProfileDTO {
    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private Integer age;
    @NotNull
    private String email;

    private String identification;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileDTO profileDTO = (ProfileDTO) o;
        return username.equals(profileDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}

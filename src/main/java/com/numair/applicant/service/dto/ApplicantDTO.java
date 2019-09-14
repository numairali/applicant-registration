package com.numair.applicant.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.numair.applicant.domain.Applicant} entity.
 */
public class ApplicantDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String first_name;

    @NotNull
    @Size(min = 2, max = 50)
    private String last_name;

    @NotNull
    private String email;

    @NotNull
    @Size(min = 10, max = 10)
    private String phone_number;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApplicantDTO applicantDTO = (ApplicantDTO) o;
        if (applicantDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applicantDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplicantDTO{" +
            "id=" + getId() +
            ", first_name='" + getFirst_name() + "'" +
            ", last_name='" + getLast_name() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone_number='" + getPhone_number() + "'" +
            "}";
    }
}

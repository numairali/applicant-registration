package com.numair.applicant.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.numair.applicant.domain.Applicant} entity. This class is used
 * in {@link com.numair.applicant.web.rest.ApplicantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /applicants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ApplicantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter first_name;

    private StringFilter last_name;

    private StringFilter email;

    private StringFilter phone_number;

    public ApplicantCriteria(){
    }

    public ApplicantCriteria(ApplicantCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.first_name = other.first_name == null ? null : other.first_name.copy();
        this.last_name = other.last_name == null ? null : other.last_name.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone_number = other.phone_number == null ? null : other.phone_number.copy();
    }

    @Override
    public ApplicantCriteria copy() {
        return new ApplicantCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirst_name() {
        return first_name;
    }

    public void setFirst_name(StringFilter first_name) {
        this.first_name = first_name;
    }

    public StringFilter getLast_name() {
        return last_name;
    }

    public void setLast_name(StringFilter last_name) {
        this.last_name = last_name;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(StringFilter phone_number) {
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
        final ApplicantCriteria that = (ApplicantCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(first_name, that.first_name) &&
            Objects.equals(last_name, that.last_name) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone_number, that.phone_number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        first_name,
        last_name,
        email,
        phone_number
        );
    }

    @Override
    public String toString() {
        return "ApplicantCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (first_name != null ? "first_name=" + first_name + ", " : "") +
                (last_name != null ? "last_name=" + last_name + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phone_number != null ? "phone_number=" + phone_number + ", " : "") +
            "}";
    }

}

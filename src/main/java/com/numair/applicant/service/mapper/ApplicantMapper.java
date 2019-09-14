package com.numair.applicant.service.mapper;

import com.numair.applicant.domain.*;
import com.numair.applicant.service.dto.ApplicantDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Applicant} and its DTO {@link ApplicantDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicantMapper extends EntityMapper<ApplicantDTO, Applicant> {



    default Applicant fromId(Long id) {
        if (id == null) {
            return null;
        }
        Applicant applicant = new Applicant();
        applicant.setId(id);
        return applicant;
    }
}

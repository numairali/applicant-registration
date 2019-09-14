package com.numair.applicant.service;

import com.numair.applicant.service.dto.ApplicantDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.numair.applicant.domain.Applicant}.
 */
public interface ApplicantService {

    /**
     * Save a applicant.
     *
     * @param applicantDTO the entity to save.
     * @return the persisted entity.
     */
    ApplicantDTO save(ApplicantDTO applicantDTO);

    /**
     * Get all the applicants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApplicantDTO> findAll(Pageable pageable);


    /**
     * Get the "id" applicant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApplicantDTO> findOne(Long id);

    /**
     * Delete the "id" applicant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

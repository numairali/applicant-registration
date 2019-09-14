package com.numair.applicant.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.numair.applicant.domain.Applicant;
import com.numair.applicant.domain.*; // for static metamodels
import com.numair.applicant.repository.ApplicantRepository;
import com.numair.applicant.service.dto.ApplicantCriteria;
import com.numair.applicant.service.dto.ApplicantDTO;
import com.numair.applicant.service.mapper.ApplicantMapper;

/**
 * Service for executing complex queries for {@link Applicant} entities in the database.
 * The main input is a {@link ApplicantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ApplicantDTO} or a {@link Page} of {@link ApplicantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApplicantQueryService extends QueryService<Applicant> {

    private final Logger log = LoggerFactory.getLogger(ApplicantQueryService.class);

    private final ApplicantRepository applicantRepository;

    private final ApplicantMapper applicantMapper;

    public ApplicantQueryService(ApplicantRepository applicantRepository, ApplicantMapper applicantMapper) {
        this.applicantRepository = applicantRepository;
        this.applicantMapper = applicantMapper;
    }

    /**
     * Return a {@link List} of {@link ApplicantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicantDTO> findByCriteria(ApplicantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Applicant> specification = createSpecification(criteria);
        return applicantMapper.toDto(applicantRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ApplicantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicantDTO> findByCriteria(ApplicantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Applicant> specification = createSpecification(criteria);
        return applicantRepository.findAll(specification, page)
            .map(applicantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApplicantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Applicant> specification = createSpecification(criteria);
        return applicantRepository.count(specification);
    }

    /**
     * Function to convert ApplicantCriteria to a {@link Specification}.
     */
    private Specification<Applicant> createSpecification(ApplicantCriteria criteria) {
        Specification<Applicant> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Applicant_.id));
            }
            if (criteria.getFirst_name() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirst_name(), Applicant_.first_name));
            }
            if (criteria.getLast_name() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLast_name(), Applicant_.last_name));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Applicant_.email));
            }
            if (criteria.getPhone_number() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone_number(), Applicant_.phone_number));
            }
        }
        return specification;
    }
}

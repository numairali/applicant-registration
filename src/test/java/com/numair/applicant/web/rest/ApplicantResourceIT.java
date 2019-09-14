package com.numair.applicant.web.rest;

import com.numair.applicant.ApplicantRegistrationApp;
import com.numair.applicant.domain.Applicant;
import com.numair.applicant.repository.ApplicantRepository;
import com.numair.applicant.service.ApplicantService;
import com.numair.applicant.service.dto.ApplicantDTO;
import com.numair.applicant.service.mapper.ApplicantMapper;
import com.numair.applicant.web.rest.errors.ExceptionTranslator;
import com.numair.applicant.service.dto.ApplicantCriteria;
import com.numair.applicant.service.ApplicantQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.numair.applicant.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ApplicantResource} REST controller.
 */
@SpringBootTest(classes = ApplicantRegistrationApp.class)
public class ApplicantResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private ApplicantMapper applicantMapper;

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private ApplicantQueryService applicantQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restApplicantMockMvc;

    private Applicant applicant;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicantResource applicantResource = new ApplicantResource(applicantService, applicantQueryService);
        this.restApplicantMockMvc = MockMvcBuilders.standaloneSetup(applicantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applicant createEntity(EntityManager em) {
        Applicant applicant = new Applicant()
            .first_name(DEFAULT_FIRST_NAME)
            .last_name(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phone_number(DEFAULT_PHONE_NUMBER);
        return applicant;
    }

    @BeforeEach
    public void initTest() {
        applicant = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicant() throws Exception {
        int databaseSizeBeforeCreate = applicantRepository.findAll().size();

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);
        restApplicantMockMvc.perform(post("/api/applicants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isCreated());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeCreate + 1);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getFirst_name()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testApplicant.getLast_name()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testApplicant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testApplicant.getPhone_number()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void createApplicantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicantRepository.findAll().size();

        // Create the Applicant with an existing ID
        applicant.setId(1L);
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicantMockMvc.perform(post("/api/applicants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirst_nameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setFirst_name(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc.perform(post("/api/applicants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLast_nameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setLast_name(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc.perform(post("/api/applicants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setEmail(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc.perform(post("/api/applicants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhone_numberIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setPhone_number(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc.perform(post("/api/applicants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicants() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList
        restApplicantMockMvc.perform(get("/api/applicants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicant.getId().intValue())))
            .andExpect(jsonPath("$.[*].first_name").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].last_name").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone_number").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }
    
    @Test
    @Transactional
    public void getApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get the applicant
        restApplicantMockMvc.perform(get("/api/applicants/{id}", applicant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applicant.getId().intValue()))
            .andExpect(jsonPath("$.first_name").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.last_name").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone_number").value(DEFAULT_PHONE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getAllApplicantsByFirst_nameIsEqualToSomething() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where first_name equals to DEFAULT_FIRST_NAME
        defaultApplicantShouldBeFound("first_name.equals=" + DEFAULT_FIRST_NAME);

        // Get all the applicantList where first_name equals to UPDATED_FIRST_NAME
        defaultApplicantShouldNotBeFound("first_name.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllApplicantsByFirst_nameIsInShouldWork() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where first_name in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultApplicantShouldBeFound("first_name.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the applicantList where first_name equals to UPDATED_FIRST_NAME
        defaultApplicantShouldNotBeFound("first_name.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllApplicantsByFirst_nameIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where first_name is not null
        defaultApplicantShouldBeFound("first_name.specified=true");

        // Get all the applicantList where first_name is null
        defaultApplicantShouldNotBeFound("first_name.specified=false");
    }

    @Test
    @Transactional
    public void getAllApplicantsByLast_nameIsEqualToSomething() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where last_name equals to DEFAULT_LAST_NAME
        defaultApplicantShouldBeFound("last_name.equals=" + DEFAULT_LAST_NAME);

        // Get all the applicantList where last_name equals to UPDATED_LAST_NAME
        defaultApplicantShouldNotBeFound("last_name.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllApplicantsByLast_nameIsInShouldWork() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where last_name in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultApplicantShouldBeFound("last_name.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the applicantList where last_name equals to UPDATED_LAST_NAME
        defaultApplicantShouldNotBeFound("last_name.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllApplicantsByLast_nameIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where last_name is not null
        defaultApplicantShouldBeFound("last_name.specified=true");

        // Get all the applicantList where last_name is null
        defaultApplicantShouldNotBeFound("last_name.specified=false");
    }

    @Test
    @Transactional
    public void getAllApplicantsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where email equals to DEFAULT_EMAIL
        defaultApplicantShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the applicantList where email equals to UPDATED_EMAIL
        defaultApplicantShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllApplicantsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultApplicantShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the applicantList where email equals to UPDATED_EMAIL
        defaultApplicantShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllApplicantsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where email is not null
        defaultApplicantShouldBeFound("email.specified=true");

        // Get all the applicantList where email is null
        defaultApplicantShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllApplicantsByPhone_numberIsEqualToSomething() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where phone_number equals to DEFAULT_PHONE_NUMBER
        defaultApplicantShouldBeFound("phone_number.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the applicantList where phone_number equals to UPDATED_PHONE_NUMBER
        defaultApplicantShouldNotBeFound("phone_number.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllApplicantsByPhone_numberIsInShouldWork() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where phone_number in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultApplicantShouldBeFound("phone_number.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the applicantList where phone_number equals to UPDATED_PHONE_NUMBER
        defaultApplicantShouldNotBeFound("phone_number.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllApplicantsByPhone_numberIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList where phone_number is not null
        defaultApplicantShouldBeFound("phone_number.specified=true");

        // Get all the applicantList where phone_number is null
        defaultApplicantShouldNotBeFound("phone_number.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApplicantShouldBeFound(String filter) throws Exception {
        restApplicantMockMvc.perform(get("/api/applicants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicant.getId().intValue())))
            .andExpect(jsonPath("$.[*].first_name").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].last_name").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone_number").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restApplicantMockMvc.perform(get("/api/applicants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApplicantShouldNotBeFound(String filter) throws Exception {
        restApplicantMockMvc.perform(get("/api/applicants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicantMockMvc.perform(get("/api/applicants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingApplicant() throws Exception {
        // Get the applicant
        restApplicantMockMvc.perform(get("/api/applicants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Update the applicant
        Applicant updatedApplicant = applicantRepository.findById(applicant.getId()).get();
        // Disconnect from session so that the updates on updatedApplicant are not directly saved in db
        em.detach(updatedApplicant);
        updatedApplicant
            .first_name(UPDATED_FIRST_NAME)
            .last_name(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone_number(UPDATED_PHONE_NUMBER);
        ApplicantDTO applicantDTO = applicantMapper.toDto(updatedApplicant);

        restApplicantMockMvc.perform(put("/api/applicants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isOk());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getFirst_name()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testApplicant.getLast_name()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApplicant.getPhone_number()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicantMockMvc.perform(put("/api/applicants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeDelete = applicantRepository.findAll().size();

        // Delete the applicant
        restApplicantMockMvc.perform(delete("/api/applicants/{id}", applicant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Applicant.class);
        Applicant applicant1 = new Applicant();
        applicant1.setId(1L);
        Applicant applicant2 = new Applicant();
        applicant2.setId(applicant1.getId());
        assertThat(applicant1).isEqualTo(applicant2);
        applicant2.setId(2L);
        assertThat(applicant1).isNotEqualTo(applicant2);
        applicant1.setId(null);
        assertThat(applicant1).isNotEqualTo(applicant2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicantDTO.class);
        ApplicantDTO applicantDTO1 = new ApplicantDTO();
        applicantDTO1.setId(1L);
        ApplicantDTO applicantDTO2 = new ApplicantDTO();
        assertThat(applicantDTO1).isNotEqualTo(applicantDTO2);
        applicantDTO2.setId(applicantDTO1.getId());
        assertThat(applicantDTO1).isEqualTo(applicantDTO2);
        applicantDTO2.setId(2L);
        assertThat(applicantDTO1).isNotEqualTo(applicantDTO2);
        applicantDTO1.setId(null);
        assertThat(applicantDTO1).isNotEqualTo(applicantDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(applicantMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(applicantMapper.fromId(null)).isNull();
    }
}

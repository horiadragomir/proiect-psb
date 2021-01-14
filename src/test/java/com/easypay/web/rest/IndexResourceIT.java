package com.easypay.web.rest;

import com.easypay.EasypayApp;
import com.easypay.domain.Index;
import com.easypay.domain.Location;
import com.easypay.repository.IndexRepository;
import com.easypay.service.IndexService;
import com.easypay.service.dto.IndexDTO;
import com.easypay.service.mapper.IndexMapper;
import com.easypay.service.dto.IndexCriteria;
import com.easypay.service.IndexQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link IndexResource} REST controller.
 */
@SpringBootTest(classes = EasypayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class IndexResourceIT {

    private static final Long DEFAULT_VALUE = 1L;
    private static final Long UPDATED_VALUE = 2L;
    private static final Long SMALLER_VALUE = 1L - 1L;

    private static final Long DEFAULT_MONTH = 1L;
    private static final Long UPDATED_MONTH = 2L;
    private static final Long SMALLER_MONTH = 1L - 1L;

    private static final Long DEFAULT_YEAR = 1L;
    private static final Long UPDATED_YEAR = 2L;
    private static final Long SMALLER_YEAR = 1L - 1L;

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private IndexMapper indexMapper;

    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexQueryService indexQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexMockMvc;

    private Index index;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Index createEntity(EntityManager em) {
        Index index = new Index()
            .value(DEFAULT_VALUE)
            .month(DEFAULT_MONTH)
            .year(DEFAULT_YEAR);
        return index;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Index createUpdatedEntity(EntityManager em) {
        Index index = new Index()
            .value(UPDATED_VALUE)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR);
        return index;
    }

    @BeforeEach
    public void initTest() {
        index = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndex() throws Exception {
        int databaseSizeBeforeCreate = indexRepository.findAll().size();
        // Create the Index
        IndexDTO indexDTO = indexMapper.toDto(index);
        restIndexMockMvc.perform(post("/api/indices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(indexDTO)))
            .andExpect(status().isCreated());

        // Validate the Index in the database
        List<Index> indexList = indexRepository.findAll();
        assertThat(indexList).hasSize(databaseSizeBeforeCreate + 1);
        Index testIndex = indexList.get(indexList.size() - 1);
        assertThat(testIndex.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testIndex.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testIndex.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void createIndexWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = indexRepository.findAll().size();

        // Create the Index with an existing ID
        index.setId(1L);
        IndexDTO indexDTO = indexMapper.toDto(index);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexMockMvc.perform(post("/api/indices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(indexDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Index in the database
        List<Index> indexList = indexRepository.findAll();
        assertThat(indexList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = indexRepository.findAll().size();
        // set the field null
        index.setValue(null);

        // Create the Index, which fails.
        IndexDTO indexDTO = indexMapper.toDto(index);


        restIndexMockMvc.perform(post("/api/indices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(indexDTO)))
            .andExpect(status().isBadRequest());

        List<Index> indexList = indexRepository.findAll();
        assertThat(indexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = indexRepository.findAll().size();
        // set the field null
        index.setMonth(null);

        // Create the Index, which fails.
        IndexDTO indexDTO = indexMapper.toDto(index);


        restIndexMockMvc.perform(post("/api/indices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(indexDTO)))
            .andExpect(status().isBadRequest());

        List<Index> indexList = indexRepository.findAll();
        assertThat(indexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = indexRepository.findAll().size();
        // set the field null
        index.setYear(null);

        // Create the Index, which fails.
        IndexDTO indexDTO = indexMapper.toDto(index);


        restIndexMockMvc.perform(post("/api/indices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(indexDTO)))
            .andExpect(status().isBadRequest());

        List<Index> indexList = indexRepository.findAll();
        assertThat(indexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIndices() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList
        restIndexMockMvc.perform(get("/api/indices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(index.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.intValue())));
    }
    
    @Test
    @Transactional
    public void getIndex() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get the index
        restIndexMockMvc.perform(get("/api/indices/{id}", index.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(index.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.intValue()));
    }


    @Test
    @Transactional
    public void getIndicesByIdFiltering() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        Long id = index.getId();

        defaultIndexShouldBeFound("id.equals=" + id);
        defaultIndexShouldNotBeFound("id.notEquals=" + id);

        defaultIndexShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIndexShouldNotBeFound("id.greaterThan=" + id);

        defaultIndexShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIndexShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllIndicesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where value equals to DEFAULT_VALUE
        defaultIndexShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the indexList where value equals to UPDATED_VALUE
        defaultIndexShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllIndicesByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where value not equals to DEFAULT_VALUE
        defaultIndexShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the indexList where value not equals to UPDATED_VALUE
        defaultIndexShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllIndicesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultIndexShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the indexList where value equals to UPDATED_VALUE
        defaultIndexShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllIndicesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where value is not null
        defaultIndexShouldBeFound("value.specified=true");

        // Get all the indexList where value is null
        defaultIndexShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllIndicesByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where value is greater than or equal to DEFAULT_VALUE
        defaultIndexShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the indexList where value is greater than or equal to UPDATED_VALUE
        defaultIndexShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllIndicesByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where value is less than or equal to DEFAULT_VALUE
        defaultIndexShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the indexList where value is less than or equal to SMALLER_VALUE
        defaultIndexShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    public void getAllIndicesByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where value is less than DEFAULT_VALUE
        defaultIndexShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the indexList where value is less than UPDATED_VALUE
        defaultIndexShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllIndicesByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where value is greater than DEFAULT_VALUE
        defaultIndexShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the indexList where value is greater than SMALLER_VALUE
        defaultIndexShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }


    @Test
    @Transactional
    public void getAllIndicesByMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where month equals to DEFAULT_MONTH
        defaultIndexShouldBeFound("month.equals=" + DEFAULT_MONTH);

        // Get all the indexList where month equals to UPDATED_MONTH
        defaultIndexShouldNotBeFound("month.equals=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllIndicesByMonthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where month not equals to DEFAULT_MONTH
        defaultIndexShouldNotBeFound("month.notEquals=" + DEFAULT_MONTH);

        // Get all the indexList where month not equals to UPDATED_MONTH
        defaultIndexShouldBeFound("month.notEquals=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllIndicesByMonthIsInShouldWork() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where month in DEFAULT_MONTH or UPDATED_MONTH
        defaultIndexShouldBeFound("month.in=" + DEFAULT_MONTH + "," + UPDATED_MONTH);

        // Get all the indexList where month equals to UPDATED_MONTH
        defaultIndexShouldNotBeFound("month.in=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllIndicesByMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where month is not null
        defaultIndexShouldBeFound("month.specified=true");

        // Get all the indexList where month is null
        defaultIndexShouldNotBeFound("month.specified=false");
    }

    @Test
    @Transactional
    public void getAllIndicesByMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where month is greater than or equal to DEFAULT_MONTH
        defaultIndexShouldBeFound("month.greaterThanOrEqual=" + DEFAULT_MONTH);

        // Get all the indexList where month is greater than or equal to UPDATED_MONTH
        defaultIndexShouldNotBeFound("month.greaterThanOrEqual=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllIndicesByMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where month is less than or equal to DEFAULT_MONTH
        defaultIndexShouldBeFound("month.lessThanOrEqual=" + DEFAULT_MONTH);

        // Get all the indexList where month is less than or equal to SMALLER_MONTH
        defaultIndexShouldNotBeFound("month.lessThanOrEqual=" + SMALLER_MONTH);
    }

    @Test
    @Transactional
    public void getAllIndicesByMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where month is less than DEFAULT_MONTH
        defaultIndexShouldNotBeFound("month.lessThan=" + DEFAULT_MONTH);

        // Get all the indexList where month is less than UPDATED_MONTH
        defaultIndexShouldBeFound("month.lessThan=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllIndicesByMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where month is greater than DEFAULT_MONTH
        defaultIndexShouldNotBeFound("month.greaterThan=" + DEFAULT_MONTH);

        // Get all the indexList where month is greater than SMALLER_MONTH
        defaultIndexShouldBeFound("month.greaterThan=" + SMALLER_MONTH);
    }


    @Test
    @Transactional
    public void getAllIndicesByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where year equals to DEFAULT_YEAR
        defaultIndexShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the indexList where year equals to UPDATED_YEAR
        defaultIndexShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllIndicesByYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where year not equals to DEFAULT_YEAR
        defaultIndexShouldNotBeFound("year.notEquals=" + DEFAULT_YEAR);

        // Get all the indexList where year not equals to UPDATED_YEAR
        defaultIndexShouldBeFound("year.notEquals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllIndicesByYearIsInShouldWork() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultIndexShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the indexList where year equals to UPDATED_YEAR
        defaultIndexShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllIndicesByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where year is not null
        defaultIndexShouldBeFound("year.specified=true");

        // Get all the indexList where year is null
        defaultIndexShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    public void getAllIndicesByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where year is greater than or equal to DEFAULT_YEAR
        defaultIndexShouldBeFound("year.greaterThanOrEqual=" + DEFAULT_YEAR);

        // Get all the indexList where year is greater than or equal to UPDATED_YEAR
        defaultIndexShouldNotBeFound("year.greaterThanOrEqual=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllIndicesByYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where year is less than or equal to DEFAULT_YEAR
        defaultIndexShouldBeFound("year.lessThanOrEqual=" + DEFAULT_YEAR);

        // Get all the indexList where year is less than or equal to SMALLER_YEAR
        defaultIndexShouldNotBeFound("year.lessThanOrEqual=" + SMALLER_YEAR);
    }

    @Test
    @Transactional
    public void getAllIndicesByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where year is less than DEFAULT_YEAR
        defaultIndexShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the indexList where year is less than UPDATED_YEAR
        defaultIndexShouldBeFound("year.lessThan=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllIndicesByYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        // Get all the indexList where year is greater than DEFAULT_YEAR
        defaultIndexShouldNotBeFound("year.greaterThan=" + DEFAULT_YEAR);

        // Get all the indexList where year is greater than SMALLER_YEAR
        defaultIndexShouldBeFound("year.greaterThan=" + SMALLER_YEAR);
    }


    @Test
    @Transactional
    public void getAllIndicesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        index.setLocation(location);
        indexRepository.saveAndFlush(index);
        Long locationId = location.getId();

        // Get all the indexList where location equals to locationId
        defaultIndexShouldBeFound("locationId.equals=" + locationId);

        // Get all the indexList where location equals to locationId + 1
        defaultIndexShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexShouldBeFound(String filter) throws Exception {
        restIndexMockMvc.perform(get("/api/indices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(index.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.intValue())));

        // Check, that the count call also returns 1
        restIndexMockMvc.perform(get("/api/indices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexShouldNotBeFound(String filter) throws Exception {
        restIndexMockMvc.perform(get("/api/indices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexMockMvc.perform(get("/api/indices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingIndex() throws Exception {
        // Get the index
        restIndexMockMvc.perform(get("/api/indices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndex() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        int databaseSizeBeforeUpdate = indexRepository.findAll().size();

        // Update the index
        Index updatedIndex = indexRepository.findById(index.getId()).get();
        // Disconnect from session so that the updates on updatedIndex are not directly saved in db
        em.detach(updatedIndex);
        updatedIndex
            .value(UPDATED_VALUE)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR);
        IndexDTO indexDTO = indexMapper.toDto(updatedIndex);

        restIndexMockMvc.perform(put("/api/indices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(indexDTO)))
            .andExpect(status().isOk());

        // Validate the Index in the database
        List<Index> indexList = indexRepository.findAll();
        assertThat(indexList).hasSize(databaseSizeBeforeUpdate);
        Index testIndex = indexList.get(indexList.size() - 1);
        assertThat(testIndex.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testIndex.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testIndex.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void updateNonExistingIndex() throws Exception {
        int databaseSizeBeforeUpdate = indexRepository.findAll().size();

        // Create the Index
        IndexDTO indexDTO = indexMapper.toDto(index);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexMockMvc.perform(put("/api/indices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(indexDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Index in the database
        List<Index> indexList = indexRepository.findAll();
        assertThat(indexList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIndex() throws Exception {
        // Initialize the database
        indexRepository.saveAndFlush(index);

        int databaseSizeBeforeDelete = indexRepository.findAll().size();

        // Delete the index
        restIndexMockMvc.perform(delete("/api/indices/{id}", index.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Index> indexList = indexRepository.findAll();
        assertThat(indexList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

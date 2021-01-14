package com.easypay.web.rest;

import com.easypay.EasypayApp;
import com.easypay.domain.Bill;
import com.easypay.domain.Location;
import com.easypay.repository.BillRepository;
import com.easypay.service.BillService;
import com.easypay.service.dto.BillDTO;
import com.easypay.service.mapper.BillMapper;
import com.easypay.service.dto.BillCriteria;
import com.easypay.service.BillQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BillResource} REST controller.
 */
@SpringBootTest(classes = EasypayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BillResourceIT {

    private static final Instant DEFAULT_FIRST_DAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FIRST_DAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_DAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_DAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_VALUE = 1L;
    private static final Long UPDATED_VALUE = 2L;
    private static final Long SMALLER_VALUE = 1L - 1L;

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillService billService;

    @Autowired
    private BillQueryService billQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillMockMvc;

    private Bill bill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bill createEntity(EntityManager em) {
        Bill bill = new Bill()
            .firstDay(DEFAULT_FIRST_DAY)
            .lastDay(DEFAULT_LAST_DAY)
            .value(DEFAULT_VALUE)
            .paid(DEFAULT_PAID);
        return bill;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bill createUpdatedEntity(EntityManager em) {
        Bill bill = new Bill()
            .firstDay(UPDATED_FIRST_DAY)
            .lastDay(UPDATED_LAST_DAY)
            .value(UPDATED_VALUE)
            .paid(UPDATED_PAID);
        return bill;
    }

    @BeforeEach
    public void initTest() {
        bill = createEntity(em);
    }

    @Test
    @Transactional
    public void createBill() throws Exception {
        int databaseSizeBeforeCreate = billRepository.findAll().size();
        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);
        restBillMockMvc.perform(post("/api/bills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isCreated());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeCreate + 1);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getFirstDay()).isEqualTo(DEFAULT_FIRST_DAY);
        assertThat(testBill.getLastDay()).isEqualTo(DEFAULT_LAST_DAY);
        assertThat(testBill.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testBill.isPaid()).isEqualTo(DEFAULT_PAID);
    }

    @Test
    @Transactional
    public void createBillWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = billRepository.findAll().size();

        // Create the Bill with an existing ID
        bill.setId(1L);
        BillDTO billDTO = billMapper.toDto(bill);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillMockMvc.perform(post("/api/bills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = billRepository.findAll().size();
        // set the field null
        bill.setFirstDay(null);

        // Create the Bill, which fails.
        BillDTO billDTO = billMapper.toDto(bill);


        restBillMockMvc.perform(post("/api/bills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = billRepository.findAll().size();
        // set the field null
        bill.setLastDay(null);

        // Create the Bill, which fails.
        BillDTO billDTO = billMapper.toDto(bill);


        restBillMockMvc.perform(post("/api/bills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = billRepository.findAll().size();
        // set the field null
        bill.setValue(null);

        // Create the Bill, which fails.
        BillDTO billDTO = billMapper.toDto(bill);


        restBillMockMvc.perform(post("/api/bills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBills() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList
        restBillMockMvc.perform(get("/api/bills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bill.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstDay").value(hasItem(DEFAULT_FIRST_DAY.toString())))
            .andExpect(jsonPath("$.[*].lastDay").value(hasItem(DEFAULT_LAST_DAY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get the bill
        restBillMockMvc.perform(get("/api/bills/{id}", bill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bill.getId().intValue()))
            .andExpect(jsonPath("$.firstDay").value(DEFAULT_FIRST_DAY.toString()))
            .andExpect(jsonPath("$.lastDay").value(DEFAULT_LAST_DAY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()));
    }


    @Test
    @Transactional
    public void getBillsByIdFiltering() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        Long id = bill.getId();

        defaultBillShouldBeFound("id.equals=" + id);
        defaultBillShouldNotBeFound("id.notEquals=" + id);

        defaultBillShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBillShouldNotBeFound("id.greaterThan=" + id);

        defaultBillShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBillShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBillsByFirstDayIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where firstDay equals to DEFAULT_FIRST_DAY
        defaultBillShouldBeFound("firstDay.equals=" + DEFAULT_FIRST_DAY);

        // Get all the billList where firstDay equals to UPDATED_FIRST_DAY
        defaultBillShouldNotBeFound("firstDay.equals=" + UPDATED_FIRST_DAY);
    }

    @Test
    @Transactional
    public void getAllBillsByFirstDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where firstDay not equals to DEFAULT_FIRST_DAY
        defaultBillShouldNotBeFound("firstDay.notEquals=" + DEFAULT_FIRST_DAY);

        // Get all the billList where firstDay not equals to UPDATED_FIRST_DAY
        defaultBillShouldBeFound("firstDay.notEquals=" + UPDATED_FIRST_DAY);
    }

    @Test
    @Transactional
    public void getAllBillsByFirstDayIsInShouldWork() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where firstDay in DEFAULT_FIRST_DAY or UPDATED_FIRST_DAY
        defaultBillShouldBeFound("firstDay.in=" + DEFAULT_FIRST_DAY + "," + UPDATED_FIRST_DAY);

        // Get all the billList where firstDay equals to UPDATED_FIRST_DAY
        defaultBillShouldNotBeFound("firstDay.in=" + UPDATED_FIRST_DAY);
    }

    @Test
    @Transactional
    public void getAllBillsByFirstDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where firstDay is not null
        defaultBillShouldBeFound("firstDay.specified=true");

        // Get all the billList where firstDay is null
        defaultBillShouldNotBeFound("firstDay.specified=false");
    }

    @Test
    @Transactional
    public void getAllBillsByLastDayIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where lastDay equals to DEFAULT_LAST_DAY
        defaultBillShouldBeFound("lastDay.equals=" + DEFAULT_LAST_DAY);

        // Get all the billList where lastDay equals to UPDATED_LAST_DAY
        defaultBillShouldNotBeFound("lastDay.equals=" + UPDATED_LAST_DAY);
    }

    @Test
    @Transactional
    public void getAllBillsByLastDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where lastDay not equals to DEFAULT_LAST_DAY
        defaultBillShouldNotBeFound("lastDay.notEquals=" + DEFAULT_LAST_DAY);

        // Get all the billList where lastDay not equals to UPDATED_LAST_DAY
        defaultBillShouldBeFound("lastDay.notEquals=" + UPDATED_LAST_DAY);
    }

    @Test
    @Transactional
    public void getAllBillsByLastDayIsInShouldWork() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where lastDay in DEFAULT_LAST_DAY or UPDATED_LAST_DAY
        defaultBillShouldBeFound("lastDay.in=" + DEFAULT_LAST_DAY + "," + UPDATED_LAST_DAY);

        // Get all the billList where lastDay equals to UPDATED_LAST_DAY
        defaultBillShouldNotBeFound("lastDay.in=" + UPDATED_LAST_DAY);
    }

    @Test
    @Transactional
    public void getAllBillsByLastDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where lastDay is not null
        defaultBillShouldBeFound("lastDay.specified=true");

        // Get all the billList where lastDay is null
        defaultBillShouldNotBeFound("lastDay.specified=false");
    }

    @Test
    @Transactional
    public void getAllBillsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where value equals to DEFAULT_VALUE
        defaultBillShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the billList where value equals to UPDATED_VALUE
        defaultBillShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllBillsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where value not equals to DEFAULT_VALUE
        defaultBillShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the billList where value not equals to UPDATED_VALUE
        defaultBillShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllBillsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultBillShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the billList where value equals to UPDATED_VALUE
        defaultBillShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllBillsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where value is not null
        defaultBillShouldBeFound("value.specified=true");

        // Get all the billList where value is null
        defaultBillShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllBillsByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where value is greater than or equal to DEFAULT_VALUE
        defaultBillShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the billList where value is greater than or equal to UPDATED_VALUE
        defaultBillShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllBillsByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where value is less than or equal to DEFAULT_VALUE
        defaultBillShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the billList where value is less than or equal to SMALLER_VALUE
        defaultBillShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    public void getAllBillsByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where value is less than DEFAULT_VALUE
        defaultBillShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the billList where value is less than UPDATED_VALUE
        defaultBillShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllBillsByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where value is greater than DEFAULT_VALUE
        defaultBillShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the billList where value is greater than SMALLER_VALUE
        defaultBillShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }


    @Test
    @Transactional
    public void getAllBillsByPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where paid equals to DEFAULT_PAID
        defaultBillShouldBeFound("paid.equals=" + DEFAULT_PAID);

        // Get all the billList where paid equals to UPDATED_PAID
        defaultBillShouldNotBeFound("paid.equals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllBillsByPaidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where paid not equals to DEFAULT_PAID
        defaultBillShouldNotBeFound("paid.notEquals=" + DEFAULT_PAID);

        // Get all the billList where paid not equals to UPDATED_PAID
        defaultBillShouldBeFound("paid.notEquals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllBillsByPaidIsInShouldWork() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where paid in DEFAULT_PAID or UPDATED_PAID
        defaultBillShouldBeFound("paid.in=" + DEFAULT_PAID + "," + UPDATED_PAID);

        // Get all the billList where paid equals to UPDATED_PAID
        defaultBillShouldNotBeFound("paid.in=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllBillsByPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList where paid is not null
        defaultBillShouldBeFound("paid.specified=true");

        // Get all the billList where paid is null
        defaultBillShouldNotBeFound("paid.specified=false");
    }

    @Test
    @Transactional
    public void getAllBillsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        bill.setLocation(location);
        billRepository.saveAndFlush(bill);
        Long locationId = location.getId();

        // Get all the billList where location equals to locationId
        defaultBillShouldBeFound("locationId.equals=" + locationId);

        // Get all the billList where location equals to locationId + 1
        defaultBillShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBillShouldBeFound(String filter) throws Exception {
        restBillMockMvc.perform(get("/api/bills?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bill.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstDay").value(hasItem(DEFAULT_FIRST_DAY.toString())))
            .andExpect(jsonPath("$.[*].lastDay").value(hasItem(DEFAULT_LAST_DAY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())));

        // Check, that the count call also returns 1
        restBillMockMvc.perform(get("/api/bills/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBillShouldNotBeFound(String filter) throws Exception {
        restBillMockMvc.perform(get("/api/bills?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBillMockMvc.perform(get("/api/bills/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBill() throws Exception {
        // Get the bill
        restBillMockMvc.perform(get("/api/bills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeUpdate = billRepository.findAll().size();

        // Update the bill
        Bill updatedBill = billRepository.findById(bill.getId()).get();
        // Disconnect from session so that the updates on updatedBill are not directly saved in db
        em.detach(updatedBill);
        updatedBill
            .firstDay(UPDATED_FIRST_DAY)
            .lastDay(UPDATED_LAST_DAY)
            .value(UPDATED_VALUE)
            .paid(UPDATED_PAID);
        BillDTO billDTO = billMapper.toDto(updatedBill);

        restBillMockMvc.perform(put("/api/bills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isOk());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getFirstDay()).isEqualTo(UPDATED_FIRST_DAY);
        assertThat(testBill.getLastDay()).isEqualTo(UPDATED_LAST_DAY);
        assertThat(testBill.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testBill.isPaid()).isEqualTo(UPDATED_PAID);
    }

    @Test
    @Transactional
    public void updateNonExistingBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillMockMvc.perform(put("/api/bills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeDelete = billRepository.findAll().size();

        // Delete the bill
        restBillMockMvc.perform(delete("/api/bills/{id}", bill.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

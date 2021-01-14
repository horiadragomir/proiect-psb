package com.easypay.web.rest;

import com.easypay.EasypayApp;
import com.easypay.domain.Shop;
import com.easypay.repository.ShopRepository;
import com.easypay.service.ShopService;
import com.easypay.service.dto.ShopDTO;
import com.easypay.service.mapper.ShopMapper;
import com.easypay.service.dto.ShopCriteria;
import com.easypay.service.ShopQueryService;

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
 * Integration tests for the {@link ShopResource} REST controller.
 */
@SpringBootTest(classes = EasypayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ShopResourceIT {

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Long DEFAULT_WEEK_HOUR_START = 1L;
    private static final Long UPDATED_WEEK_HOUR_START = 2L;
    private static final Long SMALLER_WEEK_HOUR_START = 1L - 1L;

    private static final Long DEFAULT_WEEK_HOUR_END = 1L;
    private static final Long UPDATED_WEEK_HOUR_END = 2L;
    private static final Long SMALLER_WEEK_HOUR_END = 1L - 1L;

    private static final Long DEFAULT_WEEKEND_HOUR_START = 1L;
    private static final Long UPDATED_WEEKEND_HOUR_START = 2L;
    private static final Long SMALLER_WEEKEND_HOUR_START = 1L - 1L;

    private static final Long DEFAULT_WEEKEND_HOUR_END = 1L;
    private static final Long UPDATED_WEEKEND_HOUR_END = 2L;
    private static final Long SMALLER_WEEKEND_HOUR_END = 1L - 1L;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopQueryService shopQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShopMockMvc;

    private Shop shop;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shop createEntity(EntityManager em) {
        Shop shop = new Shop()
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .weekHourStart(DEFAULT_WEEK_HOUR_START)
            .weekHourEnd(DEFAULT_WEEK_HOUR_END)
            .weekendHourStart(DEFAULT_WEEKEND_HOUR_START)
            .weekendHourEnd(DEFAULT_WEEKEND_HOUR_END);
        return shop;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shop createUpdatedEntity(EntityManager em) {
        Shop shop = new Shop()
            .streetAddress(UPDATED_STREET_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .weekHourStart(UPDATED_WEEK_HOUR_START)
            .weekHourEnd(UPDATED_WEEK_HOUR_END)
            .weekendHourStart(UPDATED_WEEKEND_HOUR_START)
            .weekendHourEnd(UPDATED_WEEKEND_HOUR_END);
        return shop;
    }

    @BeforeEach
    public void initTest() {
        shop = createEntity(em);
    }

    @Test
    @Transactional
    public void createShop() throws Exception {
        int databaseSizeBeforeCreate = shopRepository.findAll().size();
        // Create the Shop
        ShopDTO shopDTO = shopMapper.toDto(shop);
        restShopMockMvc.perform(post("/api/shops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isCreated());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeCreate + 1);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testShop.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testShop.getWeekHourStart()).isEqualTo(DEFAULT_WEEK_HOUR_START);
        assertThat(testShop.getWeekHourEnd()).isEqualTo(DEFAULT_WEEK_HOUR_END);
        assertThat(testShop.getWeekendHourStart()).isEqualTo(DEFAULT_WEEKEND_HOUR_START);
        assertThat(testShop.getWeekendHourEnd()).isEqualTo(DEFAULT_WEEKEND_HOUR_END);
    }

    @Test
    @Transactional
    public void createShopWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopRepository.findAll().size();

        // Create the Shop with an existing ID
        shop.setId(1L);
        ShopDTO shopDTO = shopMapper.toDto(shop);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopMockMvc.perform(post("/api/shops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStreetAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopRepository.findAll().size();
        // set the field null
        shop.setStreetAddress(null);

        // Create the Shop, which fails.
        ShopDTO shopDTO = shopMapper.toDto(shop);


        restShopMockMvc.perform(post("/api/shops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isBadRequest());

        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopRepository.findAll().size();
        // set the field null
        shop.setPhoneNumber(null);

        // Create the Shop, which fails.
        ShopDTO shopDTO = shopMapper.toDto(shop);


        restShopMockMvc.perform(post("/api/shops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isBadRequest());

        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeekHourStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopRepository.findAll().size();
        // set the field null
        shop.setWeekHourStart(null);

        // Create the Shop, which fails.
        ShopDTO shopDTO = shopMapper.toDto(shop);


        restShopMockMvc.perform(post("/api/shops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isBadRequest());

        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeekHourEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopRepository.findAll().size();
        // set the field null
        shop.setWeekHourEnd(null);

        // Create the Shop, which fails.
        ShopDTO shopDTO = shopMapper.toDto(shop);


        restShopMockMvc.perform(post("/api/shops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isBadRequest());

        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeekendHourStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopRepository.findAll().size();
        // set the field null
        shop.setWeekendHourStart(null);

        // Create the Shop, which fails.
        ShopDTO shopDTO = shopMapper.toDto(shop);


        restShopMockMvc.perform(post("/api/shops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isBadRequest());

        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeekendHourEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopRepository.findAll().size();
        // set the field null
        shop.setWeekendHourEnd(null);

        // Create the Shop, which fails.
        ShopDTO shopDTO = shopMapper.toDto(shop);


        restShopMockMvc.perform(post("/api/shops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isBadRequest());

        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShops() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList
        restShopMockMvc.perform(get("/api/shops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shop.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].weekHourStart").value(hasItem(DEFAULT_WEEK_HOUR_START.intValue())))
            .andExpect(jsonPath("$.[*].weekHourEnd").value(hasItem(DEFAULT_WEEK_HOUR_END.intValue())))
            .andExpect(jsonPath("$.[*].weekendHourStart").value(hasItem(DEFAULT_WEEKEND_HOUR_START.intValue())))
            .andExpect(jsonPath("$.[*].weekendHourEnd").value(hasItem(DEFAULT_WEEKEND_HOUR_END.intValue())));
    }
    
    @Test
    @Transactional
    public void getShop() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get the shop
        restShopMockMvc.perform(get("/api/shops/{id}", shop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shop.getId().intValue()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.weekHourStart").value(DEFAULT_WEEK_HOUR_START.intValue()))
            .andExpect(jsonPath("$.weekHourEnd").value(DEFAULT_WEEK_HOUR_END.intValue()))
            .andExpect(jsonPath("$.weekendHourStart").value(DEFAULT_WEEKEND_HOUR_START.intValue()))
            .andExpect(jsonPath("$.weekendHourEnd").value(DEFAULT_WEEKEND_HOUR_END.intValue()));
    }


    @Test
    @Transactional
    public void getShopsByIdFiltering() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        Long id = shop.getId();

        defaultShopShouldBeFound("id.equals=" + id);
        defaultShopShouldNotBeFound("id.notEquals=" + id);

        defaultShopShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShopShouldNotBeFound("id.greaterThan=" + id);

        defaultShopShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShopShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllShopsByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultShopShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the shopList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultShopShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllShopsByStreetAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where streetAddress not equals to DEFAULT_STREET_ADDRESS
        defaultShopShouldNotBeFound("streetAddress.notEquals=" + DEFAULT_STREET_ADDRESS);

        // Get all the shopList where streetAddress not equals to UPDATED_STREET_ADDRESS
        defaultShopShouldBeFound("streetAddress.notEquals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllShopsByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultShopShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the shopList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultShopShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllShopsByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where streetAddress is not null
        defaultShopShouldBeFound("streetAddress.specified=true");

        // Get all the shopList where streetAddress is null
        defaultShopShouldNotBeFound("streetAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllShopsByStreetAddressContainsSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where streetAddress contains DEFAULT_STREET_ADDRESS
        defaultShopShouldBeFound("streetAddress.contains=" + DEFAULT_STREET_ADDRESS);

        // Get all the shopList where streetAddress contains UPDATED_STREET_ADDRESS
        defaultShopShouldNotBeFound("streetAddress.contains=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllShopsByStreetAddressNotContainsSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where streetAddress does not contain DEFAULT_STREET_ADDRESS
        defaultShopShouldNotBeFound("streetAddress.doesNotContain=" + DEFAULT_STREET_ADDRESS);

        // Get all the shopList where streetAddress does not contain UPDATED_STREET_ADDRESS
        defaultShopShouldBeFound("streetAddress.doesNotContain=" + UPDATED_STREET_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllShopsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultShopShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the shopList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultShopShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllShopsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultShopShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the shopList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultShopShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllShopsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultShopShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the shopList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultShopShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllShopsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where phoneNumber is not null
        defaultShopShouldBeFound("phoneNumber.specified=true");

        // Get all the shopList where phoneNumber is null
        defaultShopShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllShopsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultShopShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the shopList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultShopShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllShopsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultShopShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the shopList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultShopShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllShopsByWeekHourStartIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourStart equals to DEFAULT_WEEK_HOUR_START
        defaultShopShouldBeFound("weekHourStart.equals=" + DEFAULT_WEEK_HOUR_START);

        // Get all the shopList where weekHourStart equals to UPDATED_WEEK_HOUR_START
        defaultShopShouldNotBeFound("weekHourStart.equals=" + UPDATED_WEEK_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourStart not equals to DEFAULT_WEEK_HOUR_START
        defaultShopShouldNotBeFound("weekHourStart.notEquals=" + DEFAULT_WEEK_HOUR_START);

        // Get all the shopList where weekHourStart not equals to UPDATED_WEEK_HOUR_START
        defaultShopShouldBeFound("weekHourStart.notEquals=" + UPDATED_WEEK_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourStartIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourStart in DEFAULT_WEEK_HOUR_START or UPDATED_WEEK_HOUR_START
        defaultShopShouldBeFound("weekHourStart.in=" + DEFAULT_WEEK_HOUR_START + "," + UPDATED_WEEK_HOUR_START);

        // Get all the shopList where weekHourStart equals to UPDATED_WEEK_HOUR_START
        defaultShopShouldNotBeFound("weekHourStart.in=" + UPDATED_WEEK_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourStart is not null
        defaultShopShouldBeFound("weekHourStart.specified=true");

        // Get all the shopList where weekHourStart is null
        defaultShopShouldNotBeFound("weekHourStart.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourStart is greater than or equal to DEFAULT_WEEK_HOUR_START
        defaultShopShouldBeFound("weekHourStart.greaterThanOrEqual=" + DEFAULT_WEEK_HOUR_START);

        // Get all the shopList where weekHourStart is greater than or equal to UPDATED_WEEK_HOUR_START
        defaultShopShouldNotBeFound("weekHourStart.greaterThanOrEqual=" + UPDATED_WEEK_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourStartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourStart is less than or equal to DEFAULT_WEEK_HOUR_START
        defaultShopShouldBeFound("weekHourStart.lessThanOrEqual=" + DEFAULT_WEEK_HOUR_START);

        // Get all the shopList where weekHourStart is less than or equal to SMALLER_WEEK_HOUR_START
        defaultShopShouldNotBeFound("weekHourStart.lessThanOrEqual=" + SMALLER_WEEK_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourStartIsLessThanSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourStart is less than DEFAULT_WEEK_HOUR_START
        defaultShopShouldNotBeFound("weekHourStart.lessThan=" + DEFAULT_WEEK_HOUR_START);

        // Get all the shopList where weekHourStart is less than UPDATED_WEEK_HOUR_START
        defaultShopShouldBeFound("weekHourStart.lessThan=" + UPDATED_WEEK_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourStartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourStart is greater than DEFAULT_WEEK_HOUR_START
        defaultShopShouldNotBeFound("weekHourStart.greaterThan=" + DEFAULT_WEEK_HOUR_START);

        // Get all the shopList where weekHourStart is greater than SMALLER_WEEK_HOUR_START
        defaultShopShouldBeFound("weekHourStart.greaterThan=" + SMALLER_WEEK_HOUR_START);
    }


    @Test
    @Transactional
    public void getAllShopsByWeekHourEndIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourEnd equals to DEFAULT_WEEK_HOUR_END
        defaultShopShouldBeFound("weekHourEnd.equals=" + DEFAULT_WEEK_HOUR_END);

        // Get all the shopList where weekHourEnd equals to UPDATED_WEEK_HOUR_END
        defaultShopShouldNotBeFound("weekHourEnd.equals=" + UPDATED_WEEK_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourEnd not equals to DEFAULT_WEEK_HOUR_END
        defaultShopShouldNotBeFound("weekHourEnd.notEquals=" + DEFAULT_WEEK_HOUR_END);

        // Get all the shopList where weekHourEnd not equals to UPDATED_WEEK_HOUR_END
        defaultShopShouldBeFound("weekHourEnd.notEquals=" + UPDATED_WEEK_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourEndIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourEnd in DEFAULT_WEEK_HOUR_END or UPDATED_WEEK_HOUR_END
        defaultShopShouldBeFound("weekHourEnd.in=" + DEFAULT_WEEK_HOUR_END + "," + UPDATED_WEEK_HOUR_END);

        // Get all the shopList where weekHourEnd equals to UPDATED_WEEK_HOUR_END
        defaultShopShouldNotBeFound("weekHourEnd.in=" + UPDATED_WEEK_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourEnd is not null
        defaultShopShouldBeFound("weekHourEnd.specified=true");

        // Get all the shopList where weekHourEnd is null
        defaultShopShouldNotBeFound("weekHourEnd.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourEnd is greater than or equal to DEFAULT_WEEK_HOUR_END
        defaultShopShouldBeFound("weekHourEnd.greaterThanOrEqual=" + DEFAULT_WEEK_HOUR_END);

        // Get all the shopList where weekHourEnd is greater than or equal to UPDATED_WEEK_HOUR_END
        defaultShopShouldNotBeFound("weekHourEnd.greaterThanOrEqual=" + UPDATED_WEEK_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourEndIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourEnd is less than or equal to DEFAULT_WEEK_HOUR_END
        defaultShopShouldBeFound("weekHourEnd.lessThanOrEqual=" + DEFAULT_WEEK_HOUR_END);

        // Get all the shopList where weekHourEnd is less than or equal to SMALLER_WEEK_HOUR_END
        defaultShopShouldNotBeFound("weekHourEnd.lessThanOrEqual=" + SMALLER_WEEK_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourEndIsLessThanSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourEnd is less than DEFAULT_WEEK_HOUR_END
        defaultShopShouldNotBeFound("weekHourEnd.lessThan=" + DEFAULT_WEEK_HOUR_END);

        // Get all the shopList where weekHourEnd is less than UPDATED_WEEK_HOUR_END
        defaultShopShouldBeFound("weekHourEnd.lessThan=" + UPDATED_WEEK_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekHourEndIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekHourEnd is greater than DEFAULT_WEEK_HOUR_END
        defaultShopShouldNotBeFound("weekHourEnd.greaterThan=" + DEFAULT_WEEK_HOUR_END);

        // Get all the shopList where weekHourEnd is greater than SMALLER_WEEK_HOUR_END
        defaultShopShouldBeFound("weekHourEnd.greaterThan=" + SMALLER_WEEK_HOUR_END);
    }


    @Test
    @Transactional
    public void getAllShopsByWeekendHourStartIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourStart equals to DEFAULT_WEEKEND_HOUR_START
        defaultShopShouldBeFound("weekendHourStart.equals=" + DEFAULT_WEEKEND_HOUR_START);

        // Get all the shopList where weekendHourStart equals to UPDATED_WEEKEND_HOUR_START
        defaultShopShouldNotBeFound("weekendHourStart.equals=" + UPDATED_WEEKEND_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourStart not equals to DEFAULT_WEEKEND_HOUR_START
        defaultShopShouldNotBeFound("weekendHourStart.notEquals=" + DEFAULT_WEEKEND_HOUR_START);

        // Get all the shopList where weekendHourStart not equals to UPDATED_WEEKEND_HOUR_START
        defaultShopShouldBeFound("weekendHourStart.notEquals=" + UPDATED_WEEKEND_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourStartIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourStart in DEFAULT_WEEKEND_HOUR_START or UPDATED_WEEKEND_HOUR_START
        defaultShopShouldBeFound("weekendHourStart.in=" + DEFAULT_WEEKEND_HOUR_START + "," + UPDATED_WEEKEND_HOUR_START);

        // Get all the shopList where weekendHourStart equals to UPDATED_WEEKEND_HOUR_START
        defaultShopShouldNotBeFound("weekendHourStart.in=" + UPDATED_WEEKEND_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourStart is not null
        defaultShopShouldBeFound("weekendHourStart.specified=true");

        // Get all the shopList where weekendHourStart is null
        defaultShopShouldNotBeFound("weekendHourStart.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourStart is greater than or equal to DEFAULT_WEEKEND_HOUR_START
        defaultShopShouldBeFound("weekendHourStart.greaterThanOrEqual=" + DEFAULT_WEEKEND_HOUR_START);

        // Get all the shopList where weekendHourStart is greater than or equal to UPDATED_WEEKEND_HOUR_START
        defaultShopShouldNotBeFound("weekendHourStart.greaterThanOrEqual=" + UPDATED_WEEKEND_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourStartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourStart is less than or equal to DEFAULT_WEEKEND_HOUR_START
        defaultShopShouldBeFound("weekendHourStart.lessThanOrEqual=" + DEFAULT_WEEKEND_HOUR_START);

        // Get all the shopList where weekendHourStart is less than or equal to SMALLER_WEEKEND_HOUR_START
        defaultShopShouldNotBeFound("weekendHourStart.lessThanOrEqual=" + SMALLER_WEEKEND_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourStartIsLessThanSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourStart is less than DEFAULT_WEEKEND_HOUR_START
        defaultShopShouldNotBeFound("weekendHourStart.lessThan=" + DEFAULT_WEEKEND_HOUR_START);

        // Get all the shopList where weekendHourStart is less than UPDATED_WEEKEND_HOUR_START
        defaultShopShouldBeFound("weekendHourStart.lessThan=" + UPDATED_WEEKEND_HOUR_START);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourStartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourStart is greater than DEFAULT_WEEKEND_HOUR_START
        defaultShopShouldNotBeFound("weekendHourStart.greaterThan=" + DEFAULT_WEEKEND_HOUR_START);

        // Get all the shopList where weekendHourStart is greater than SMALLER_WEEKEND_HOUR_START
        defaultShopShouldBeFound("weekendHourStart.greaterThan=" + SMALLER_WEEKEND_HOUR_START);
    }


    @Test
    @Transactional
    public void getAllShopsByWeekendHourEndIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourEnd equals to DEFAULT_WEEKEND_HOUR_END
        defaultShopShouldBeFound("weekendHourEnd.equals=" + DEFAULT_WEEKEND_HOUR_END);

        // Get all the shopList where weekendHourEnd equals to UPDATED_WEEKEND_HOUR_END
        defaultShopShouldNotBeFound("weekendHourEnd.equals=" + UPDATED_WEEKEND_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourEnd not equals to DEFAULT_WEEKEND_HOUR_END
        defaultShopShouldNotBeFound("weekendHourEnd.notEquals=" + DEFAULT_WEEKEND_HOUR_END);

        // Get all the shopList where weekendHourEnd not equals to UPDATED_WEEKEND_HOUR_END
        defaultShopShouldBeFound("weekendHourEnd.notEquals=" + UPDATED_WEEKEND_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourEndIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourEnd in DEFAULT_WEEKEND_HOUR_END or UPDATED_WEEKEND_HOUR_END
        defaultShopShouldBeFound("weekendHourEnd.in=" + DEFAULT_WEEKEND_HOUR_END + "," + UPDATED_WEEKEND_HOUR_END);

        // Get all the shopList where weekendHourEnd equals to UPDATED_WEEKEND_HOUR_END
        defaultShopShouldNotBeFound("weekendHourEnd.in=" + UPDATED_WEEKEND_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourEnd is not null
        defaultShopShouldBeFound("weekendHourEnd.specified=true");

        // Get all the shopList where weekendHourEnd is null
        defaultShopShouldNotBeFound("weekendHourEnd.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourEnd is greater than or equal to DEFAULT_WEEKEND_HOUR_END
        defaultShopShouldBeFound("weekendHourEnd.greaterThanOrEqual=" + DEFAULT_WEEKEND_HOUR_END);

        // Get all the shopList where weekendHourEnd is greater than or equal to UPDATED_WEEKEND_HOUR_END
        defaultShopShouldNotBeFound("weekendHourEnd.greaterThanOrEqual=" + UPDATED_WEEKEND_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourEndIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourEnd is less than or equal to DEFAULT_WEEKEND_HOUR_END
        defaultShopShouldBeFound("weekendHourEnd.lessThanOrEqual=" + DEFAULT_WEEKEND_HOUR_END);

        // Get all the shopList where weekendHourEnd is less than or equal to SMALLER_WEEKEND_HOUR_END
        defaultShopShouldNotBeFound("weekendHourEnd.lessThanOrEqual=" + SMALLER_WEEKEND_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourEndIsLessThanSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourEnd is less than DEFAULT_WEEKEND_HOUR_END
        defaultShopShouldNotBeFound("weekendHourEnd.lessThan=" + DEFAULT_WEEKEND_HOUR_END);

        // Get all the shopList where weekendHourEnd is less than UPDATED_WEEKEND_HOUR_END
        defaultShopShouldBeFound("weekendHourEnd.lessThan=" + UPDATED_WEEKEND_HOUR_END);
    }

    @Test
    @Transactional
    public void getAllShopsByWeekendHourEndIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where weekendHourEnd is greater than DEFAULT_WEEKEND_HOUR_END
        defaultShopShouldNotBeFound("weekendHourEnd.greaterThan=" + DEFAULT_WEEKEND_HOUR_END);

        // Get all the shopList where weekendHourEnd is greater than SMALLER_WEEKEND_HOUR_END
        defaultShopShouldBeFound("weekendHourEnd.greaterThan=" + SMALLER_WEEKEND_HOUR_END);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShopShouldBeFound(String filter) throws Exception {
        restShopMockMvc.perform(get("/api/shops?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shop.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].weekHourStart").value(hasItem(DEFAULT_WEEK_HOUR_START.intValue())))
            .andExpect(jsonPath("$.[*].weekHourEnd").value(hasItem(DEFAULT_WEEK_HOUR_END.intValue())))
            .andExpect(jsonPath("$.[*].weekendHourStart").value(hasItem(DEFAULT_WEEKEND_HOUR_START.intValue())))
            .andExpect(jsonPath("$.[*].weekendHourEnd").value(hasItem(DEFAULT_WEEKEND_HOUR_END.intValue())));

        // Check, that the count call also returns 1
        restShopMockMvc.perform(get("/api/shops/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShopShouldNotBeFound(String filter) throws Exception {
        restShopMockMvc.perform(get("/api/shops?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShopMockMvc.perform(get("/api/shops/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingShop() throws Exception {
        // Get the shop
        restShopMockMvc.perform(get("/api/shops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShop() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        int databaseSizeBeforeUpdate = shopRepository.findAll().size();

        // Update the shop
        Shop updatedShop = shopRepository.findById(shop.getId()).get();
        // Disconnect from session so that the updates on updatedShop are not directly saved in db
        em.detach(updatedShop);
        updatedShop
            .streetAddress(UPDATED_STREET_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .weekHourStart(UPDATED_WEEK_HOUR_START)
            .weekHourEnd(UPDATED_WEEK_HOUR_END)
            .weekendHourStart(UPDATED_WEEKEND_HOUR_START)
            .weekendHourEnd(UPDATED_WEEKEND_HOUR_END);
        ShopDTO shopDTO = shopMapper.toDto(updatedShop);

        restShopMockMvc.perform(put("/api/shops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isOk());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testShop.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testShop.getWeekHourStart()).isEqualTo(UPDATED_WEEK_HOUR_START);
        assertThat(testShop.getWeekHourEnd()).isEqualTo(UPDATED_WEEK_HOUR_END);
        assertThat(testShop.getWeekendHourStart()).isEqualTo(UPDATED_WEEKEND_HOUR_START);
        assertThat(testShop.getWeekendHourEnd()).isEqualTo(UPDATED_WEEKEND_HOUR_END);
    }

    @Test
    @Transactional
    public void updateNonExistingShop() throws Exception {
        int databaseSizeBeforeUpdate = shopRepository.findAll().size();

        // Create the Shop
        ShopDTO shopDTO = shopMapper.toDto(shop);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopMockMvc.perform(put("/api/shops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShop() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        int databaseSizeBeforeDelete = shopRepository.findAll().size();

        // Delete the shop
        restShopMockMvc.perform(delete("/api/shops/{id}", shop.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

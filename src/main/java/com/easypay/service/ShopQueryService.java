package com.easypay.service;

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

import com.easypay.domain.Shop;
import com.easypay.domain.*; // for static metamodels
import com.easypay.repository.ShopRepository;
import com.easypay.service.dto.ShopCriteria;
import com.easypay.service.dto.ShopDTO;
import com.easypay.service.mapper.ShopMapper;

/**
 * Service for executing complex queries for {@link Shop} entities in the database.
 * The main input is a {@link ShopCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShopDTO} or a {@link Page} of {@link ShopDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShopQueryService extends QueryService<Shop> {

    private final Logger log = LoggerFactory.getLogger(ShopQueryService.class);

    private final ShopRepository shopRepository;

    private final ShopMapper shopMapper;

    public ShopQueryService(ShopRepository shopRepository, ShopMapper shopMapper) {
        this.shopRepository = shopRepository;
        this.shopMapper = shopMapper;
    }

    /**
     * Return a {@link List} of {@link ShopDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShopDTO> findByCriteria(ShopCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Shop> specification = createSpecification(criteria);
        return shopMapper.toDto(shopRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShopDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShopDTO> findByCriteria(ShopCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Shop> specification = createSpecification(criteria);
        return shopRepository.findAll(specification, page)
            .map(shopMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShopCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Shop> specification = createSpecification(criteria);
        return shopRepository.count(specification);
    }

    /**
     * Function to convert {@link ShopCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Shop> createSpecification(ShopCriteria criteria) {
        Specification<Shop> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Shop_.id));
            }
            if (criteria.getStreetAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetAddress(), Shop_.streetAddress));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Shop_.phoneNumber));
            }
            if (criteria.getWeekHourStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeekHourStart(), Shop_.weekHourStart));
            }
            if (criteria.getWeekHourEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeekHourEnd(), Shop_.weekHourEnd));
            }
            if (criteria.getWeekendHourStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeekendHourStart(), Shop_.weekendHourStart));
            }
            if (criteria.getWeekendHourEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeekendHourEnd(), Shop_.weekendHourEnd));
            }
        }
        return specification;
    }
}

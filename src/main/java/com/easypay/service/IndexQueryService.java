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

import com.easypay.domain.Index;
import com.easypay.domain.*; // for static metamodels
import com.easypay.repository.IndexRepository;
import com.easypay.service.dto.IndexCriteria;
import com.easypay.service.dto.IndexDTO;
import com.easypay.service.mapper.IndexMapper;

/**
 * Service for executing complex queries for {@link Index} entities in the database.
 * The main input is a {@link IndexCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IndexDTO} or a {@link Page} of {@link IndexDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexQueryService extends QueryService<Index> {

    private final Logger log = LoggerFactory.getLogger(IndexQueryService.class);

    private final IndexRepository indexRepository;

    private final IndexMapper indexMapper;

    public IndexQueryService(IndexRepository indexRepository, IndexMapper indexMapper) {
        this.indexRepository = indexRepository;
        this.indexMapper = indexMapper;
    }

    /**
     * Return a {@link List} of {@link IndexDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IndexDTO> findByCriteria(IndexCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Index> specification = createSpecification(criteria);
        return indexMapper.toDto(indexRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IndexDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexDTO> findByCriteria(IndexCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Index> specification = createSpecification(criteria);
        return indexRepository.findAll(specification, page)
            .map(indexMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Index> specification = createSpecification(criteria);
        return indexRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Index> createSpecification(IndexCriteria criteria) {
        Specification<Index> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Index_.id));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), Index_.value));
            }
            if (criteria.getMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonth(), Index_.month));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), Index_.year));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Index_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}

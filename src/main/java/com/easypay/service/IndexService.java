package com.easypay.service;

import com.easypay.domain.Index;
import com.easypay.domain.Location;
import com.easypay.repository.IndexRepository;
import com.easypay.service.dto.IndexDTO;
import com.easypay.service.mapper.IndexMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Index}.
 */
@Service
@Transactional
public class IndexService {

    private final Logger log = LoggerFactory.getLogger(IndexService.class);

    private final IndexRepository indexRepository;

    private final IndexMapper indexMapper;

    public IndexService(IndexRepository indexRepository, IndexMapper indexMapper) {
        this.indexRepository = indexRepository;
        this.indexMapper = indexMapper;
    }

    /**
     * Save a index.
     *
     * @param indexDTO the entity to save.
     * @return the persisted entity.
     */
    public IndexDTO save(IndexDTO indexDTO) {
        log.debug("Request to save Index : {}", indexDTO);
        Index index = indexMapper.toEntity(indexDTO);
        index = indexRepository.save(index);
        return indexMapper.toDto(index);
    }

    /**
     * Get all the indices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Indices");
        return indexRepository.findAll(pageable)
            .map(indexMapper::toDto);
    }

    /**
     * Get all the indices. by location
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Index> findAllByLocation(Location location) {
        log.debug("Request to get all Indices");
        return indexRepository.findAllByLocation(location);
    }



    /**
     * Get one index by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexDTO> findOne(Long id) {
        log.debug("Request to get Index : {}", id);
        return indexRepository.findById(id)
            .map(indexMapper::toDto);
    }

    /**
     * Delete the index by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Index : {}", id);
        indexRepository.deleteById(id);
    }
}

package com.easypay.web.rest;

import com.easypay.service.IndexService;
import com.easypay.web.rest.errors.BadRequestAlertException;
import com.easypay.service.dto.IndexDTO;
import com.easypay.service.dto.IndexCriteria;
import com.easypay.service.IndexQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.easypay.domain.Index}.
 */
@RestController
@RequestMapping("/api")
public class IndexResource {

    private final Logger log = LoggerFactory.getLogger(IndexResource.class);

    private static final String ENTITY_NAME = "index";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexService indexService;

    private final IndexQueryService indexQueryService;

    public IndexResource(IndexService indexService, IndexQueryService indexQueryService) {
        this.indexService = indexService;
        this.indexQueryService = indexQueryService;
    }

    /**
     * {@code POST  /indices} : Create a new index.
     *
     * @param indexDTO the indexDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexDTO, or with status {@code 400 (Bad Request)} if the index has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/indices")
    public ResponseEntity<IndexDTO> createIndex(@Valid @RequestBody IndexDTO indexDTO) throws URISyntaxException {
        log.debug("REST request to save Index : {}", indexDTO);
        if (indexDTO.getId() != null) {
            throw new BadRequestAlertException("A new index cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndexDTO result = indexService.save(indexDTO);
        return ResponseEntity.created(new URI("/api/indices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/new/indices")
//    public ResponseEntity<Void> addIndex()


    @GetMapping("new/indices")



    /**
     * {@code PUT  /indices} : Updates an existing index.
     *
     * @param indexDTO the indexDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexDTO,
     * or with status {@code 400 (Bad Request)} if the indexDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/indices")
    public ResponseEntity<IndexDTO> updateIndex(@Valid @RequestBody IndexDTO indexDTO) throws URISyntaxException {
        log.debug("REST request to update Index : {}", indexDTO);
        if (indexDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IndexDTO result = indexService.save(indexDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, indexDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /indices} : get all the indices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indices in body.
     */
    @GetMapping("/indices")
    public ResponseEntity<List<IndexDTO>> getAllIndices(IndexCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Indices by criteria: {}", criteria);
        Page<IndexDTO> page = indexQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /indices/count} : count all the indices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/indices/count")
    public ResponseEntity<Long> countIndices(IndexCriteria criteria) {
        log.debug("REST request to count Indices by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /indices/:id} : get the "id" index.
     *
     * @param id the id of the indexDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/indices/{id}")
    public ResponseEntity<IndexDTO> getIndex(@PathVariable Long id) {
        log.debug("REST request to get Index : {}", id);
        Optional<IndexDTO> indexDTO = indexService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexDTO);
    }

    /**
     * {@code DELETE  /indices/:id} : delete the "id" index.
     *
     * @param id the id of the indexDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/indices/{id}")
    public ResponseEntity<Void> deleteIndex(@PathVariable Long id) {
        log.debug("REST request to delete Index : {}", id);
        indexService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

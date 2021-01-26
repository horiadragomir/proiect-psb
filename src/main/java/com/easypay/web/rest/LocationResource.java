package com.easypay.web.rest;

import com.easypay.domain.Client;
import com.easypay.domain.Location;
import com.easypay.service.ClientService;
import com.easypay.service.LocationService;
import com.easypay.web.rest.errors.BadRequestAlertException;
import com.easypay.service.dto.LocationDTO;
import com.easypay.service.dto.LocationCriteria;
import com.easypay.service.LocationQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
import javax.xml.bind.DatatypeConverter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.easypay.domain.Location}.
 */
@RestController
@RequestMapping("/api")
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    private static final String ENTITY_NAME = "location";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationService locationService;

    private final LocationQueryService locationQueryService;

    private final ClientService clientService;

    private static String SECRET_KEY =
        "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI" +
        "-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n" +
        "-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18" +
        "-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";

    public static Claims decodeJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                            .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                            .parseClaimsJws(jwt).getBody();
        return claims;
    }

    public LocationResource(LocationService locationService,
                            LocationQueryService locationQueryService,
                            ClientService clientService
    ) {
        this.locationService = locationService;
        this.locationQueryService = locationQueryService;
        this.clientService = clientService;
    }

    /**
     * {@code POST  /locations} : Create a new location.
     *
     * @param locationDTO the locationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationDTO, or
     * with status {@code 400 (Bad Request)} if the location has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locations")
    public ResponseEntity<LocationDTO> createLocation(@Valid @RequestBody LocationDTO locationDTO) throws URISyntaxException {
        log.debug("REST request to save Location : {}", locationDTO);
        if (locationDTO.getId() != null) {
            throw new BadRequestAlertException("A new location cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationDTO result = locationService.save(locationDTO);
        return ResponseEntity.created(new URI("/api/locations/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert(
                                 applicationName,
                                 true,
                                 ENTITY_NAME,
                                 result.getId().toString()
                             ))
                             .body(result);
    }

    /**
     * {@code PUT  /locations} : Updates an existing location.
     *
     * @param locationDTO the locationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationDTO,
     * or with status {@code 400 (Bad Request)} if the locationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locations")
    public ResponseEntity<LocationDTO> updateLocation(@Valid @RequestBody LocationDTO locationDTO) throws URISyntaxException {
        log.debug("REST request to update Location : {}", locationDTO);
        if (locationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LocationDTO result = locationService.save(locationDTO);
        return ResponseEntity.ok()
                             .headers(HeaderUtil.createEntityUpdateAlert(
                                 applicationName,
                                 true,
                                 ENTITY_NAME,
                                 locationDTO.getId().toString()
                             ))
                             .body(result);
    }

    /**
     * {@code GET  /locations} : get all the locations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locations in body.
     */
    @GetMapping("/locations")
    public ResponseEntity<List<LocationDTO>> getAllLocations(LocationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Locations by criteria: {}", criteria);
        Page<LocationDTO> page = locationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/new/locations")
    public ResponseEntity<List<Location>> getAllLocationsForAPerson(
        @RequestHeader("Authorization") String jwtToken
    ) {
        if (!jwtToken.startsWith("Bearer")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Claims claims = decodeJWT(jwtToken.substring(7));
        Optional<Client> client = clientService.findByEmail(claims.getId());
        if (!client.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Location> locations = locationService.findAllByClientId(client.get());
        return ResponseEntity.ok().body(locations);
    }

    @PostMapping("/new/locations")
    public ResponseEntity<LocationDTO> addLocation(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody LocationDTO locationDTO
    ) {
        if (!jwtToken.startsWith("Bearer")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Claims claims = decodeJWT(jwtToken.substring(7));
        Optional<Client> client = clientService.findByEmail(claims.getId());
        if (!client.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (locationService.findByClientIdAndStreetAdress(
            client.get().getId(), locationDTO.getStreetAddress()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        locationDTO.setClientId(client.get().getId());
        locationService.save(locationDTO);
        return ResponseEntity.ok().body(locationDTO);

    }

    @DeleteMapping("/new/locations")
    public ResponseEntity<LocationDTO> deleteLocation(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody LocationDTO locationDTO
    ) {
        if (!jwtToken.startsWith("Bearer")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Claims claims = decodeJWT(jwtToken.substring(7));
        Optional<Client> client = clientService.findByEmail(claims.getId());
        if (!client.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Location> location = locationService.findByClientIdAndStreetAdress(
            client.get().getId(), locationDTO.getStreetAddress());
        if(!location.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        locationService.delete(location.get().getId());
        return new ResponseEntity<>(HttpStatus.OK);

    }


    /**
     * {@code GET  /locations/count} : count all the locations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/locations/count")
    public ResponseEntity<Long> countLocations(LocationCriteria criteria) {
        log.debug("REST request to count Locations by criteria: {}", criteria);
        return ResponseEntity.ok().body(locationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /locations/:id} : get the "id" location.
     *
     * @param id the id of the locationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationDTO, or with status
     * {@code 404 (Not Found)}.
     */
    @GetMapping("/locations/{id}")
    public ResponseEntity<LocationDTO> getLocation(@PathVariable Long id) {
        log.debug("REST request to get Location : {}", id);
        Optional<LocationDTO> locationDTO = locationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationDTO);
    }

    /**
     * {@code DELETE  /locations/:id} : delete the "id" location.
     *
     * @param id the id of the locationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete Location : {}", id);
        locationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(
            applicationName,
            true,
            ENTITY_NAME,
            id.toString()
        )).build();
    }
}

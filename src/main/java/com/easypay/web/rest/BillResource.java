package com.easypay.web.rest;

import com.easypay.domain.Bill;
import com.easypay.domain.Client;
import com.easypay.domain.Location;
import com.easypay.service.BillService;
import com.easypay.service.ClientService;
import com.easypay.service.LocationService;
import com.easypay.service.dto.LocBillsDTO;
import com.easypay.service.dto.LocationDTO;
import com.easypay.service.dto.PayBillsDTO;
import com.easypay.web.rest.errors.BadRequestAlertException;
import com.easypay.service.dto.BillDTO;
import com.easypay.service.dto.BillCriteria;
import com.easypay.service.BillQueryService;

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
 * REST controller for managing {@link com.easypay.domain.Bill}.
 */
@RestController
@RequestMapping("/api")
public class BillResource {

    private final Logger log = LoggerFactory.getLogger(BillResource.class);

    private static final String ENTITY_NAME = "bill";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillService billService;

    private final BillQueryService billQueryService;

    private final LocationService locationService;

    private final ClientService clientService;

    public BillResource(BillService billService,
                        BillQueryService billQueryService,
                        LocationService locationService, ClientService clientService
    ) {
        this.billService = billService;
        this.billQueryService = billQueryService;
        this.locationService = locationService;
        this.clientService = clientService;
    }

    /**
     * {@code POST  /bills} : Create a new bill.
     *
     * @param billDTO the billDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billDTO, or with
     * status {@code 400 (Bad Request)} if the bill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bills")
    public ResponseEntity<BillDTO> createBill(@Valid @RequestBody BillDTO billDTO) throws URISyntaxException {
        log.debug("REST request to save Bill : {}", billDTO);
        if (billDTO.getId() != null) {
            throw new BadRequestAlertException("A new bill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillDTO result = billService.save(billDTO);
        return ResponseEntity.created(new URI("/api/bills/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert(
                                 applicationName,
                                 true,
                                 ENTITY_NAME,
                                 result.getId().toString()
                             ))
                             .body(result);
    }

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

    /**
     * {@code PUT  /bills} : Updates an existing bill.
     *
     * @param billDTO the billDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billDTO,
     * or with status {@code 400 (Bad Request)} if the billDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bills")
    public ResponseEntity<BillDTO> updateBill(@Valid @RequestBody BillDTO billDTO) throws URISyntaxException {
        log.debug("REST request to update Bill : {}", billDTO);
        if (billDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BillDTO result = billService.save(billDTO);
        return ResponseEntity.ok()
                             .headers(HeaderUtil.createEntityUpdateAlert(
                                 applicationName,
                                 true,
                                 ENTITY_NAME,
                                 billDTO.getId().toString()
                             ))
                             .body(result);
    }

    /**
     * {@code GET  /bills} : get all the bills.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bills in body.
     */
    @GetMapping("/bills")
    public ResponseEntity<List<BillDTO>> getAllBills(BillCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Bills by criteria: {}", criteria);
        Page<BillDTO> page = billQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bills/count} : count all the bills.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bills/count")
    public ResponseEntity<Long> countBills(BillCriteria criteria) {
        log.debug("REST request to count Bills by criteria: {}", criteria);
        return ResponseEntity.ok().body(billQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bills/:id} : get the "id" bill.
     *
     * @param id the id of the billDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billDTO, or with status
     * {@code 404 (Not Found)}.
     */
    @GetMapping("/bills/{id}")
    public ResponseEntity<BillDTO> getBill(@PathVariable Long id) {
        log.debug("REST request to get Bill : {}", id);
        Optional<BillDTO> billDTO = billService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billDTO);
    }

    @PostMapping("/new/bill")
    public ResponseEntity<List<Bill>> getAllBills(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody LocBillsDTO locBillsDTO
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
            client.get().getId(),
            locBillsDTO.getStreetAddress()
        );
        if (!location.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (locBillsDTO.getPaid() != null) {
            List<Bill> bills = billService.findByLocation(location.get(), locBillsDTO.getPaid());
            return ResponseEntity.ok().body(bills);
        }
        List<Bill> bills = billService.findAllByLocation(location.get());

        return ResponseEntity.ok().body(bills);
    }

    @PutMapping
    public ResponseEntity<List<Bill>> payBills(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody PayBillsDTO payBillsDTO
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
            client.get().getId(),
            payBillsDTO.getStreetAddress()
        );
        if (!location.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        billService.deleteByLocationAndFirstDayAndLastDayAndValue(location.get(), payBillsDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * {@code DELETE  /bills/:id} : delete the "id" bill.
     *
     * @param id the id of the billDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bills/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        log.debug("REST request to delete Bill : {}", id);
        billService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(
            applicationName,
            true,
            ENTITY_NAME,
            id.toString()
        )).build();
    }
}

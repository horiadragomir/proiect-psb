package com.easypay.web.rest;

import com.easypay.domain.Client;
import com.easypay.service.ClientService;
import com.easypay.service.dto.ChangePasswordDTO;
import com.easypay.service.dto.ErrorDTO;
import com.easypay.service.dto.JwtTokenDTO;
import com.easypay.service.dto.LoginDTO;
import com.easypay.service.dto.RegisterDTO;
import com.easypay.service.dto.RequestsJson;
import com.easypay.web.rest.errors.BadRequestAlertException;
import com.easypay.service.dto.ClientDTO;
import com.easypay.service.dto.ClientCriteria;
import com.easypay.service.ClientQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.easypay.domain.Client}.
 */
@RestController
@RequestMapping("/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private static final String ENTITY_NAME = "client";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientService clientService;

    private final ClientQueryService clientQueryService;

    public ClientResource(ClientService clientService, ClientQueryService clientQueryService) {
        this.clientService = clientService;
        this.clientQueryService = clientQueryService;
    }

    private static String SECRET_KEY =
        "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI" +
        "-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n" +
        "-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18" +
        "-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";

    //Sample method to construct a JWT
    public static String createJWT(String id, String issuer, String subject, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                                 .setIssuedAt(now)
                                 .setSubject(subject)
                                 .setIssuer(issuer)
                                 .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                            .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                            .parseClaimsJws(jwt).getBody();
        return claims;
    }


    /**
     * {@code POST  /clients} : Create a new client.
     *
     * @param clientDTO the clientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientDTO, or with
     * status {@code 400 (Bad Request)} if the client has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clients")
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) throws URISyntaxException {
        log.debug("REST request to save Client : {}", clientDTO);
        if (clientDTO.getId() != null) {
            throw new BadRequestAlertException("A new client cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClientDTO result = clientService.save(clientDTO);
        return ResponseEntity.created(new URI("/api/clients/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert(
                                 applicationName,
                                 true,
                                 ENTITY_NAME,
                                 result.getId().toString()
                             ))
                             .body(result);
    }

    /**
     * {@code PUT  /clients} : Updates an existing client.
     *
     * @param clientDTO the clientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientDTO,
     * or with status {@code 400 (Bad Request)} if the clientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clients")
    public ResponseEntity<ClientDTO> updateClient(@Valid @RequestBody ClientDTO clientDTO) throws URISyntaxException {
        log.debug("REST request to update Client : {}", clientDTO);
        if (clientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClientDTO result = clientService.save(clientDTO);
        return ResponseEntity.ok()
                             .headers(HeaderUtil.createEntityUpdateAlert(
                                 applicationName,
                                 true,
                                 ENTITY_NAME,
                                 clientDTO.getId().toString()
                             ))
                             .body(result);
    }

    /**
     * {@code GET  /clients} : get all the clients.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clients in body.
     */
    @GetMapping("/clients")
    public ResponseEntity<List<ClientDTO>> getAllClients(ClientCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Clients by criteria: {}", criteria);
        Page<ClientDTO> page = clientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

//    @GetMapping("/clients")
//    public ResponseEntity<Object> getAllClients() {
//        return ResponseEntity.ok().body("mama");
//    }

    /**
     * {@code GET  /clients/count} : count all the clients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/clients/count")
    public ResponseEntity<Long> countClients(ClientCriteria criteria) {
        log.debug("REST request to count Clients by criteria: {}", criteria);
        return ResponseEntity.ok().body(clientQueryService.countByCriteria(criteria));
    }


    @PostMapping("/clients/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {


        if (clientService.findByEmail(registerDTO.getEmail()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        clientService.registerClient(registerDTO);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/clients/login")
    public ResponseEntity<JwtTokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        Optional<Client> client = clientService.findByEmail(loginDTO.getEmail());

        String jwtToken = createJWT(loginDTO.getEmail(), "mama", "tata", System.currentTimeMillis());


        if (!client.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!client.get().getPassword().equals(loginDTO.getPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
        jwtTokenDTO.setJwtTokenCode(jwtToken);

        return ResponseEntity.ok().body(jwtTokenDTO);
    }

    @PostMapping("/clients/informations")
    public ResponseEntity<Client> getUserInfo(
        @RequestHeader("Authorization") String jwtToken
    ) {
        if (!jwtToken.startsWith("Bearer")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Claims claims = decodeJWT(jwtToken.substring(7));
        Optional<Client> client = clientService.findByEmail(claims.getId());

        return client.map(value -> ResponseEntity.ok().body(value))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/clients/deleteAccount")
    public ResponseEntity<Void> deleteAccount(
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
        clientService.delete(client.get().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/clients/changePassword")
    public ResponseEntity<Void> changePassword(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody ChangePasswordDTO changePasswordDTO
        ) {
        if (!jwtToken.startsWith("Bearer")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Claims claims = decodeJWT(jwtToken.substring(7));
        Optional<Client> client = clientService.findByEmail(claims.getId());
        if (!client.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!changePasswordDTO.getConfirmPassword().equals(changePasswordDTO.getNewPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        client.get().setPassword(changePasswordDTO.getNewPassword());
        clientService.saveAClient(client.get());

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * {@code GET  /clients/:id} : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientDTO, or with status
     * {@code 404 (Not Found)}.
     */
    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        Optional<ClientDTO> clientDTO = clientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientDTO);
    }

    /**
     * {@code DELETE  /clients/:id} : delete the "id" client.
     *
     * @param id the id of the clientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.debug("REST request to delete Client : {}", id);
        clientService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(
            applicationName,
            true,
            ENTITY_NAME,
            id.toString()
        )).build();
    }
}

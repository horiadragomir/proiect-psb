package com.easypay.service;

import com.easypay.domain.Client;
import com.easypay.repository.ClientRepository;
import com.easypay.service.dto.ClientDTO;
import com.easypay.service.dto.JwtTokenDTO;
import com.easypay.service.dto.RegisterDTO;
import com.easypay.service.mapper.ClientMapper;
import liquibase.pro.packaged.J;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Client}.
 */
@Service
@Transactional
public class ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }


    /**
     * Save a client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     */
    public ClientDTO save(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    /**
     * Save a client.
     *
     * @param client the entity to save.
     * @return the persisted entity.
     */
    public void saveAClient(Client client) {
       clientRepository.save(client);
    }

    /**
     * Get all the clients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clients");
        return clientRepository.findAll(pageable)
            .map(clientMapper::toDto);
    }


    /**
     * Get one client by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findById(id)
            .map(clientMapper::toDto);
    }

    /**
     * Delete the client by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }

    public void registerClient(RegisterDTO registerDto) {
        Client client = new Client();
        client.setEmail(registerDto.getEmail());
        client.setFirstName(registerDto.getFirstName());
        client.setLastName(registerDto.getLastname());
        client.setPassword(registerDto.getPassword());
        client.setPhoneNumber(registerDto.getPhoneNumber());
        clientRepository.save(client);
    }

    public Optional<Client> findByEmail(String email) {

        return clientRepository.findClientByEmail(email);
    }
}

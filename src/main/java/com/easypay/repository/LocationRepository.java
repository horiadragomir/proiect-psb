package com.easypay.repository;

import com.easypay.domain.Client;
import com.easypay.domain.Location;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {

    List<Location> findAllByClient(Client client);

    Optional<Location> findByClientIdAndStreetAddress(Long clientId, String streetAdress);
}

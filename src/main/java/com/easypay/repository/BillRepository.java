package com.easypay.repository;

import com.easypay.domain.Bill;
import com.easypay.domain.Location;
import com.easypay.service.dto.LocationDTO;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Bill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {
    List<Bill> findByLocationAndPaid(Location location, Boolean paid);

    List<Bill> findAllByLocation(Location location);

    Optional<Bill> findByLocationAndFirstDayAndLastDayAndValue(Location location, Instant firstDay, Instant lastDay,
                                                               Long value);

    Optional<Bill> findByFirstDayAndLastDayAndValue(Instant firstDay, Instant lastDay,
                                                               Long value);
}

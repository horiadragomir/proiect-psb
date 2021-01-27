package com.easypay.repository;

import com.easypay.domain.Bill;
import com.easypay.domain.Location;
import com.easypay.service.dto.LocationDTO;

import java.util.List;

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
}

package com.easypay.repository;

import com.easypay.domain.Index;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Index entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexRepository extends JpaRepository<Index, Long>, JpaSpecificationExecutor<Index> {
}

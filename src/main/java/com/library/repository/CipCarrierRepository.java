package com.library.repository;

import com.library.entity.CipCarrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CipCarrierRepository extends JpaRepository<CipCarrier,Long> {
}

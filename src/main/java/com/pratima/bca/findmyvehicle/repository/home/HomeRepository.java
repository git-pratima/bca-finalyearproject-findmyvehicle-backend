package com.pratima.bca.findmyvehicle.repository.home;

import com.pratima.bca.findmyvehicle.entity.home.HomeDashData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<HomeDashData, Long> {

    HomeDashData findFirstByOrderByIdAsc();

}

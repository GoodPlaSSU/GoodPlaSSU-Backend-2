package com.ssu.goodplassu.ad.repository;

import com.ssu.goodplassu.ad.entity.Ad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad, Long> {
	Page<Ad> findAllByOrderByIdDesc(Pageable pageable);
}

package com.ssu.goodplassu.ad.service;

import com.ssu.goodplassu.ad.dto.response.AdListResponse;
import com.ssu.goodplassu.ad.entity.Ad;
import com.ssu.goodplassu.ad.repository.AdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdService {
	private final AdRepository adRepository;

	public Page<AdListResponse> getAdList(final int page) {
		Pageable pageable = Pageable.ofSize(10).withPage(page);
		Page<Ad> adList = adRepository.findAllByOrderByIdDesc(pageable);

		Page<AdListResponse> resultAdList = adList.map(
				ad -> AdListResponse.of(ad)
		);

		return resultAdList;
	}
}

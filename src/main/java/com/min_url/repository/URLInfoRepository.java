package com.min_url.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.min_url.entity.URLInfo;

public interface URLInfoRepository extends JpaRepository<URLInfo, Integer> {
	
	@Query(value= "select * from url_info ORDER BY times_accessed DESC LIMIT ?1, ?2", nativeQuery=true)
	public List<URLInfo> getMetrics(int offset, int pageSize);
}

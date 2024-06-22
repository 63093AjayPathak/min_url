package com.min_url.service;

import java.util.List;

import com.min_url.dto.MetricsDTO;
import com.min_url.dto.URLRequestDTO;
import com.min_url.dto.URLResponseDTO;
import com.min_url.entity.URLInfo;

public interface MinURLService {
	public URLResponseDTO createMinUrl(URLRequestDTO urlReq);
	
	public URLInfo getLargeUrl(String minUrl);
	
	public void deleteUrlInfo(int id);
	
	public List<MetricsDTO> getMetrics(int pageNumber, int pageSize);
}

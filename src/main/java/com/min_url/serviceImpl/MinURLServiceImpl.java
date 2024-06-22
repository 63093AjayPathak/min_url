package com.min_url.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.min_url.dto.MetricsDTO;
import com.min_url.dto.URLRequestDTO;
import com.min_url.dto.URLResponseDTO;
import com.min_url.entity.URLInfo;
import com.min_url.repository.URLInfoRepository;
import com.min_url.service.Encoder;
import com.min_url.service.MinURLService;


@Service
public class MinURLServiceImpl implements MinURLService {
	
	@Autowired
	private Encoder encoder;
	
	@Autowired
	private URLInfoRepository urlRepo;

	@Override
	public URLResponseDTO createMinUrl(URLRequestDTO urlReq) {
		
		if(urlReq.getUrl()==null || urlReq.getUrl().trim().length()==0) return null;
		
		LocalDateTime expiration = urlReq.getExpiration()==0 ? LocalDateTime.now().plusDays(14) : LocalDateTime.now().plusDays(urlReq.getExpiration());
		URLInfo url = urlRepo.save(URLInfo.builder().originalUrl(urlReq.getUrl()).expirationDate(expiration).build());
		
		String minUrl = encoder.encode(url.getId());
		return URLResponseDTO.builder().min_url("http://localhost:9090/"+minUrl).original_url(urlReq.getUrl()).expiration(expiration).build();
	}

	@Override
	public URLInfo getLargeUrl(String minUrl) {
		
		System.out.println(minUrl);
		int id = encoder.decode(minUrl);
		System.out.println("url info id is: "+id);
		
		if(id==-1) return null;
		
		URLInfo url = urlRepo.getReferenceById(id);
		
		if(url!=null) { // incrementing the count of times this url has been accessed
			url.setTimesAccessed(url.getTimesAccessed()+1);
			urlRepo.save(url);
		}
		System.out.println(url.getOriginalUrl());
		return url;
	}

	@Override
	public void deleteUrlInfo(int id) {
		urlRepo.deleteById(id);
	}
	
	@Override
	public List<MetricsDTO> getMetrics(int pageNumber, int pageSize){
		List<URLInfo> page = this.urlRepo.getMetrics((pageNumber-1)*(pageSize), pageSize);
		
		List<MetricsDTO> res = new ArrayList<>(); 
		
		for(URLInfo url : page) {
			res.add(MetricsDTO.builder().url(url.getOriginalUrl()).count(url.getTimesAccessed()).build());
		}
		return res;
	}

}

package com.min_url.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.min_url.dto.URLRequestDTO;
import com.min_url.entity.URLInfo;
import com.min_url.service.MinURLService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
@CrossOrigin()
public class MinURLController {
	
	@Autowired
	private MinURLService minService;
	
	@PostMapping("/minimize_url")
	public ResponseEntity<?> getMinURL(@RequestBody URLRequestDTO req){
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.minService.createMinUrl(req));
		
	}
	
	@GetMapping("/{min_url}")
	public ResponseEntity<?>  getOriginalURL(@PathVariable String min_url, HttpServletResponse response) {
		
		URLInfo url = this.minService.getLargeUrl(min_url);
		
		if(url==null) {
			String msg= "Invalid shortened Url: "+min_url;
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(msg);
		}
		
		if(url.getExpirationDate().isBefore(LocalDateTime.now())) { // if minUrl has expired then we delete that entry
			this.minService.deleteUrlInfo(url.getId());
			return ResponseEntity.status(HttpStatus.FOUND).body("Shortened Url has expired");
		}
		
//		try {
//			response.sendRedirect(url.getOriginalUrl());
//		}
//		catch(IOException ex) {
//			throw new RuntimeException(ex);
//		}
		return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, url.getOriginalUrl()).build();
	}
	
	@GetMapping("/metrics")
	public ResponseEntity<?> getMetrics(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "5") int pageSize){
		return ResponseEntity.status(HttpStatus.FOUND).body(this.minService.getMetrics(pageNumber, pageSize));
	}

}

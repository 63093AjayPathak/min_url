package com.min_url.entity;

import java.time.LocalDateTime;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "url_info")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class URLInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Lob
	@Column(nullable = false)
	private String originalUrl;
	
	private LocalDateTime expirationDate;
	
	@Column(name = "times_accessed")
	private long timesAccessed;
	
}

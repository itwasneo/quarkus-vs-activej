package com.itwasneo.activejapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Human (
		@JsonProperty("name") String name,
		@JsonProperty("age") int age,
		@JsonProperty("isTheOne") boolean isTheOne) {}

package com.example.demoelastic.web;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoelastic.model.Person;

@RestController
@RequestMapping("/api/person")
public class PersonController {

	private final ElasticsearchOperations elasticsearchOperations;

	public PersonController(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@PostMapping
	public String save(@RequestBody Person person) {
		IndexQuery indexQuery = new IndexQueryBuilder()
				.withId(person.id())
				.withObject(person)
				.build();
		String documentId = elasticsearchOperations.index(indexQuery, null);
		return documentId;
	}
	
	@GetMapping
	public Person findById(@PathVariable String id) {
		Person person = elasticsearchOperations.get(id, Person.class);
		return person;
	}
}

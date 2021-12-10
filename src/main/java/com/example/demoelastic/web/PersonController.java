package com.example.demoelastic.web;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
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
		String documentId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of("person"));
		return documentId;
	}
	
	@GetMapping("/{id}")
	public Person findById(@PathVariable String id) {
		Person person = elasticsearchOperations.get(id, Person.class, IndexCoordinates.of("person"));
		return person;
	}
	
	@GetMapping("/name/{name}")
	public long findByName(@PathVariable String name) {
		Criteria criteria = new Criteria("name").is(name);
		return elasticsearchOperations.count(new CriteriaQuery(criteria), IndexCoordinates.of("person"));
	}
	
	@GetMapping("/string-query/{name}")
	public SearchHits<Person> searchHitsName(@PathVariable String name) {
		Query query = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchQuery("name", name)).build();
		return elasticsearchOperations.search(query, Person.class, IndexCoordinates.of("person"));
	}
}

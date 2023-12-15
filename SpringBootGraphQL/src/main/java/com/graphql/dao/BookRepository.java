package com.graphql.dao;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import com.graphql.models.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
	
	@Procedure(name="getAllBooks")
	public List<Book> getBooks();
	
	
	@Query(value = "select get_books_fun();" , nativeQuery = true)
	public List<Book> getBooksRepo();
	
	Window<Book> findByAuthor(String author , ScrollPosition position, Limit limit, Sort sort);
		
}

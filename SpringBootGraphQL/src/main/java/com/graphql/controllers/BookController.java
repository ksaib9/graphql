package com.graphql.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.stereotype.Controller;

import com.graphql.dao.BookRepository;
import com.graphql.models.Book;
import com.graphql.models.BookInput;
import com.graphql.service.BookService;

@Controller
public class BookController {
	
	@Autowired
	BookService bookService;
	
	@Autowired
	BookRepository bookRepository;
	
	@QueryMapping("allBooks")
	public List<Book> getBooks(){
		return bookService.simpleGetBooks();
		
	}
	
	@QueryMapping("bookById")
	public Book getBook(@Argument int id) {
		return bookService.getBooksById(id);
	}
	
	@MutationMapping("createBook")
	public Book createBook(@Argument BookInput bookInput) {
		Book book = new Book();
		book.setName(bookInput.getName());
		book.setAuthor(bookInput.getAuthor());
		book.setPrice(bookInput.getPrice());
		return bookService.saveBook(book);
		
	}
	
	@MutationMapping("deleteBook")
	public Boolean deleteBook(@Argument int id) {
		return bookService.deleteBook(id);
	}
	
	@QueryMapping("bookTitle")
	public Window<Book> paginatedBooks(@Argument String title, ScrollSubrange subrange){
		 ScrollPosition scrollPosition = subrange.position().orElse(ScrollPosition.offset());
	        Limit limit = Limit.of(subrange.count().orElse(10));
	        Sort sort = Sort.by("orderedOn").descending();
	        return bookRepository.findByAuthor(title, scrollPosition, limit, sort);
	}

}

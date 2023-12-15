package com.graphql.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.graphql.dao.BookRepository;
import com.graphql.models.Book;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;

@Service
public class BookService {
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	EntityManager entityManager;
	
	@Transactional
	public List<Book> getBooksFun(){
		ResultSet results = (ResultSet) bookRepository.getBooksRepo();
		try {
			while(results.next()) {
				System.out.println(results.getObject(1));
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	public List<Book> getBooksEntity(){
		StoredProcedureQuery spq = entityManager.createNamedStoredProcedureQuery("getAllBooks");
		spq.registerStoredProcedureParameter(1, Object.class, ParameterMode.REF_CURSOR);
		spq.execute();
		return spq.getResultList();
	}
	
	public List<Book> simpleGetBooks(){
		return bookRepository.findAll();
	}
	
	public Book getBooksById(int id) {
		Optional<Book> book = bookRepository.findById(id);
		if(book.isPresent()) {
			return book.get();
		}
		return null;
	}
	
	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}
	
	public Boolean deleteBook(int id) {
		bookRepository.deleteById(id);
		return true;
	}
	
	
	public List<Book> getJDBCBooks(){
		
		List<Book> books = new ArrayList<Book>();
		String runFunction = "{? = call get_books_fun()}";
		try (
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin");
			Statement statement = conn.createStatement();
			CallableStatement cs = conn.prepareCall(runFunction);
		){
			conn.setAutoCommit(false);
			cs.execute();
			ResultSet resultSet = (ResultSet) cs.getObject(1);
			while(resultSet.next()) {
				Book book = new Book();
				book.setId(Integer.parseInt(resultSet.getString("id")));
				book.setName(resultSet.getString("name"));
				book.setAuthor(resultSet.getString("author"));
				book.setPrice(Float.parseFloat(resultSet.getString("price")));
			}
		}catch (SQLException e) {
			System.err.format("SQLState : %s\n%s", e.getSQLState(),e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return books;
	}
	

}

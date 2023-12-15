package com.graphql.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="books" ,schema = "public")

@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "getAllBooks" , procedureName = "get_books_fun", resultClasses = {Book.class} , 
			parameters = {
					@StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, type = void.class)
			})
})


@Data
public class Book {
	
	@Id
	private int id;
	private String name;
	private String author;
	private float price;
	private String orderedOn;

}

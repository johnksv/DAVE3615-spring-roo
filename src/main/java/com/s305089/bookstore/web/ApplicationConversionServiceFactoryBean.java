package com.s305089.bookstore.web;

import com.s305089.bookstore.Author;
import com.s305089.bookstore.Book;
import com.s305089.bookstore.BookOrder;
import com.s305089.bookstore.Category;
import com.s305089.bookstore.Shipping;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	public Converter<Author, String> getAuthorToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.s305089.bookstore.Author, java.lang.String>() {
            public String convert(Author author) {
                return new StringBuilder().append(author.getName()).append(' ').append(author.getAge()).append(' ').append(author.getRatings()).toString();
            }
        };
    }

	public Converter<Long, Author> getIdToAuthorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.s305089.bookstore.Author>() {
            public com.s305089.bookstore.Author convert(java.lang.Long id) {
                return Author.findAuthor(id);
            }
        };
    }

	public Converter<String, Author> getStringToAuthorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.s305089.bookstore.Author>() {
            public com.s305089.bookstore.Author convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Author.class);
            }
        };
    }

	public Converter<Book, String> getBookToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.s305089.bookstore.Book, java.lang.String>() {
            public String convert(Book book) {
                return new StringBuilder().append(book.getTitle()).append(' ').append(book.getIsbn()).append(' ').append(book.getCost()).append(' ').append(book.getQuantity()).toString();
            }
        };
    }

	public Converter<Long, Book> getIdToBookConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.s305089.bookstore.Book>() {
            public com.s305089.bookstore.Book convert(java.lang.Long id) {
                return Book.findBook(id);
            }
        };
    }

	public Converter<String, Book> getStringToBookConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.s305089.bookstore.Book>() {
            public com.s305089.bookstore.Book convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Book.class);
            }
        };
    }

	public Converter<BookOrder, String> getBookOrderToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.s305089.bookstore.BookOrder, java.lang.String>() {
            public String convert(BookOrder bookOrder) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, BookOrder> getIdToBookOrderConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.s305089.bookstore.BookOrder>() {
            public com.s305089.bookstore.BookOrder convert(java.lang.Long id) {
                return BookOrder.findBookOrder(id);
            }
        };
    }

	public Converter<String, BookOrder> getStringToBookOrderConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.s305089.bookstore.BookOrder>() {
            public com.s305089.bookstore.BookOrder convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), BookOrder.class);
            }
        };
    }

	public Converter<Category, String> getCategoryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.s305089.bookstore.Category, java.lang.String>() {
            public String convert(Category category) {
                return new StringBuilder().append(category.getName()).toString();
            }
        };
    }

	public Converter<Long, Category> getIdToCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.s305089.bookstore.Category>() {
            public com.s305089.bookstore.Category convert(java.lang.Long id) {
                return Category.findCategory(id);
            }
        };
    }

	public Converter<String, Category> getStringToCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.s305089.bookstore.Category>() {
            public com.s305089.bookstore.Category convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Category.class);
            }
        };
    }

	public Converter<Shipping, String> getShippingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.s305089.bookstore.Shipping, java.lang.String>() {
            public String convert(Shipping shipping) {
                return new StringBuilder().append(shipping.getName()).append(' ').append(shipping.getAddress()).append(' ').append(shipping.getPostalCode()).toString();
            }
        };
    }

	public Converter<Long, Shipping> getIdToShippingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.s305089.bookstore.Shipping>() {
            public com.s305089.bookstore.Shipping convert(java.lang.Long id) {
                return Shipping.findShipping(id);
            }
        };
    }

	public Converter<String, Shipping> getStringToShippingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.s305089.bookstore.Shipping>() {
            public com.s305089.bookstore.Shipping convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Shipping.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getAuthorToStringConverter());
        registry.addConverter(getIdToAuthorConverter());
        registry.addConverter(getStringToAuthorConverter());
        registry.addConverter(getBookToStringConverter());
        registry.addConverter(getIdToBookConverter());
        registry.addConverter(getStringToBookConverter());
        registry.addConverter(getBookOrderToStringConverter());
        registry.addConverter(getIdToBookOrderConverter());
        registry.addConverter(getStringToBookOrderConverter());
        registry.addConverter(getCategoryToStringConverter());
        registry.addConverter(getIdToCategoryConverter());
        registry.addConverter(getStringToCategoryConverter());
        registry.addConverter(getShippingToStringConverter());
        registry.addConverter(getIdToShippingConverter());
        registry.addConverter(getStringToShippingConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}

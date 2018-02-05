package com.s305089.bookstore;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Calendar;

import org.springframework.format.annotation.DateTimeFormat;

@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findBooksByAuthor", "findBooksByCategory", "findBooksByTitleEquals", "findBooksByIsbnEquals" })
public class Book {

    /**
     */
    @NotNull
    private String title;

    /**
     */
    @NotNull
    private String isbn;

    /**
     */
    @Min(0)
    private float cost;

    /**
     */
    @Min(0)
    private int quantity;

    /**
     */
    @ManyToOne
    private Author author;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Category> category = new HashSet<Category>();

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Calendar timeFactor;

    /**
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 10 * 1024 * 1024) //10 MB limit. Also so we don't get exception if we update/change image.
    private byte[] image;

    /**
     */
    private String contentType;

	public String getTitle() {
        return this.title;
    }

	public void setTitle(String title) {
        this.title = title;
    }

	public String getIsbn() {
        return this.isbn;
    }

	public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

	public float getCost() {
        return this.cost;
    }

	public void setCost(float cost) {
        this.cost = cost;
    }

	public int getQuantity() {
        return this.quantity;
    }

	public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

	public Author getAuthor() {
        return this.author;
    }

	public void setAuthor(Author author) {
        this.author = author;
    }

	public Set<Category> getCategory() {
        return this.category;
    }

	public void setCategory(Set<Category> category) {
        this.category = category;
    }

	public Calendar getTimeFactor() {
        return this.timeFactor;
    }

	public void setTimeFactor(Calendar timeFactor) {
        this.timeFactor = timeFactor;
    }

	public byte[] getImage() {
        return this.image;
    }

	public void setImage(byte[] image) {
        this.image = image;
    }

	public String getContentType() {
        return this.contentType;
    }

	public void setContentType(String contentType) {
        this.contentType = contentType;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("title", "isbn", "cost", "quantity", "author", "category", "timeFactor", "image", "contentType");

	public static final EntityManager entityManager() {
        EntityManager em = new Book().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countBooks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Book o", Long.class).getSingleResult();
    }

	public static List<Book> findAllBooks() {
        return entityManager().createQuery("SELECT o FROM Book o", Book.class).getResultList();
    }

	public static List<Book> findAllBooks(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Book o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Book.class).getResultList();
    }

	public static Book findBook(Long id) {
        if (id == null) return null;
        return entityManager().find(Book.class, id);
    }

	public static List<Book> findBookEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Book o", Book.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Book> findBookEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Book o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Book.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Book attached = Book.findBook(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Book merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Book merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static Long countFindBooksByAuthor(Author author) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        EntityManager em = Book.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Book AS o WHERE o.author = :author", Long.class);
        q.setParameter("author", author);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindBooksByCategory(Set<Category> category) {
        if (category == null) throw new IllegalArgumentException("The category argument is required");
        EntityManager em = Book.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(o) FROM Book AS o WHERE");
        for (int i = 0; i < category.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :category_item").append(i).append(" MEMBER OF o.category");
        }
        TypedQuery q = em.createQuery(queryBuilder.toString(), Long.class);
        int categoryIndex = 0;
        for (Category _category: category) {
            q.setParameter("category_item" + categoryIndex++, _category);
        }
        return ((Long) q.getSingleResult());
    }

	public static Long countFindBooksByIsbnEquals(String isbn) {
        if (isbn == null || isbn.length() == 0) throw new IllegalArgumentException("The isbn argument is required");
        EntityManager em = Book.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Book AS o WHERE o.isbn = :isbn", Long.class);
        q.setParameter("isbn", isbn);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindBooksByTitleEquals(String title) {
        if (title == null || title.length() == 0) throw new IllegalArgumentException("The title argument is required");
        EntityManager em = Book.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Book AS o WHERE o.title = :title", Long.class);
        q.setParameter("title", title);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<Book> findBooksByAuthor(Author author) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        EntityManager em = Book.entityManager();
        TypedQuery<Book> q = em.createQuery("SELECT o FROM Book AS o WHERE o.author = :author", Book.class);
        q.setParameter("author", author);
        return q;
    }

	public static TypedQuery<Book> findBooksByAuthor(Author author, String sortFieldName, String sortOrder) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        EntityManager em = Book.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Book AS o WHERE o.author = :author");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Book> q = em.createQuery(queryBuilder.toString(), Book.class);
        q.setParameter("author", author);
        return q;
    }

	public static TypedQuery<Book> findBooksByCategory(Set<Category> category) {
        if (category == null) throw new IllegalArgumentException("The category argument is required");
        EntityManager em = Book.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Book AS o WHERE");
        for (int i = 0; i < category.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :category_item").append(i).append(" MEMBER OF o.category");
        }
        TypedQuery<Book> q = em.createQuery(queryBuilder.toString(), Book.class);
        int categoryIndex = 0;
        for (Category _category: category) {
            q.setParameter("category_item" + categoryIndex++, _category);
        }
        return q;
    }

	public static TypedQuery<Book> findBooksByCategory(Set<Category> category, String sortFieldName, String sortOrder) {
        if (category == null) throw new IllegalArgumentException("The category argument is required");
        EntityManager em = Book.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Book AS o WHERE");
        for (int i = 0; i < category.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :category_item").append(i).append(" MEMBER OF o.category");
        }
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" " + sortOrder);
            }
        }
        TypedQuery<Book> q = em.createQuery(queryBuilder.toString(), Book.class);
        int categoryIndex = 0;
        for (Category _category: category) {
            q.setParameter("category_item" + categoryIndex++, _category);
        }
        return q;
    }

	public static TypedQuery<Book> findBooksByIsbnEquals(String isbn) {
        if (isbn == null || isbn.length() == 0) throw new IllegalArgumentException("The isbn argument is required");
        EntityManager em = Book.entityManager();
        TypedQuery<Book> q = em.createQuery("SELECT o FROM Book AS o WHERE o.isbn = :isbn", Book.class);
        q.setParameter("isbn", isbn);
        return q;
    }

	public static TypedQuery<Book> findBooksByIsbnEquals(String isbn, String sortFieldName, String sortOrder) {
        if (isbn == null || isbn.length() == 0) throw new IllegalArgumentException("The isbn argument is required");
        EntityManager em = Book.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Book AS o WHERE o.isbn = :isbn");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Book> q = em.createQuery(queryBuilder.toString(), Book.class);
        q.setParameter("isbn", isbn);
        return q;
    }

	public static TypedQuery<Book> findBooksByTitleEquals(String title) {
        if (title == null || title.length() == 0) throw new IllegalArgumentException("The title argument is required");
        EntityManager em = Book.entityManager();
        TypedQuery<Book> q = em.createQuery("SELECT o FROM Book AS o WHERE o.title = :title", Book.class);
        q.setParameter("title", title);
        return q;
    }

	public static TypedQuery<Book> findBooksByTitleEquals(String title, String sortFieldName, String sortOrder) {
        if (title == null || title.length() == 0) throw new IllegalArgumentException("The title argument is required");
        EntityManager em = Book.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Book AS o WHERE o.title = :title");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Book> q = em.createQuery(queryBuilder.toString(), Book.class);
        q.setParameter("title", title);
        return q;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }
}

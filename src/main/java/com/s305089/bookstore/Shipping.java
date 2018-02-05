package com.s305089.bookstore;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Shipping {

    /**
     */
    @NotNull
    private String Name;

    /**
     */
    @NotNull
    private String Address;

    /**
     */
    private int postalCode;

	public String getName() {
        return this.Name;
    }

	public void setName(String Name) {
        this.Name = Name;
    }

	public String getAddress() {
        return this.Address;
    }

	public void setAddress(String Address) {
        this.Address = Address;
    }

	public int getPostalCode() {
        return this.postalCode;
    }

	public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("Name", "Address", "postalCode");

	public static final EntityManager entityManager() {
        EntityManager em = new Shipping().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countShippings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Shipping o", Long.class).getSingleResult();
    }

	public static List<Shipping> findAllShippings() {
        return entityManager().createQuery("SELECT o FROM Shipping o", Shipping.class).getResultList();
    }

	public static List<Shipping> findAllShippings(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Shipping o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Shipping.class).getResultList();
    }

	public static Shipping findShipping(Long id) {
        if (id == null) return null;
        return entityManager().find(Shipping.class, id);
    }

	public static List<Shipping> findShippingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Shipping o", Shipping.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Shipping> findShippingEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Shipping o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Shipping.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Shipping attached = Shipping.findShipping(this.id);
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
    public Shipping merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Shipping merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}

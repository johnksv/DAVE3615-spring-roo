package com.s305089.bookstore.email;
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
public class ContactInfo {

    /**
     */
    @NotNull
    private String fromName;

    /**
     */
    @NotNull
    private String message;

    /**
     */
    @NotNull
    private String subject;

    /**
     */
    @NotNull
    private String fromEmail;

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

	public String getFromName() {
        return this.fromName;
    }

	public void setFromName(String fromName) {
        this.fromName = fromName;
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }

	public String getSubject() {
        return this.subject;
    }

	public void setSubject(String subject) {
        this.subject = subject;
    }

	public String getFromEmail() {
        return this.fromEmail;
    }

	public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("fromName", "message", "subject", "fromEmail");

	public static final EntityManager entityManager() {
        EntityManager em = new ContactInfo().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countContactInfoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ContactInfo o", Long.class).getSingleResult();
    }

	public static List<ContactInfo> findAllContactInfoes() {
        return entityManager().createQuery("SELECT o FROM ContactInfo o", ContactInfo.class).getResultList();
    }

	public static List<ContactInfo> findAllContactInfoes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ContactInfo o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContactInfo.class).getResultList();
    }

	public static ContactInfo findContactInfo(Long id) {
        if (id == null) return null;
        return entityManager().find(ContactInfo.class, id);
    }

	public static List<ContactInfo> findContactInfoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ContactInfo o", ContactInfo.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ContactInfo> findContactInfoEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ContactInfo o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContactInfo.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ContactInfo attached = ContactInfo.findContactInfo(this.id);
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
    public ContactInfo merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ContactInfo merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}

package com.rso.bank.notifications;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "notification")
@NamedQueries(value =
        {
                @NamedQuery(name = "Notification.getAll", query = "SELECT n FROM notification n"),
                @NamedQuery(name = "Notification.findByNotification", query = "SELECT n FROM notification n WHERE n.accountId = " +
                        ":accountId")
        })
@UuidGenerator(name = "idGenerator")
public class Notification {

    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String title;

    private String description;

    private Date submitted;

    @Column(name = "transaction_id")
    private String transactionId;
	
    @Column(name = "account_id")
    private String accountId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }

    public String getAccountId() {
        return accountId;
    }

    public void getTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
	
	public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}

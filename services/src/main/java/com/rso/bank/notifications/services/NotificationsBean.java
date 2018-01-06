package com.rso.bank.notifications.services;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import com.rso.bank.notifications.Notification;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class NotificationsBean {
    private Logger log = Logger.getLogger(NotificationsBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Notification> getNotifications(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, Notification.class, queryParameters);

    }

    public Notification getNotification(String transactionId) {

        Notification transaction = em.find(Notification.class, transactionId);

        if (transaction == null) {
            throw new NotFoundException();
        }

        return transaction;
    }

    public Notification createNotification(Notification transaction) {

        try {
            beginTx();
            em.persist(transaction);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return transaction;
    }

    public Notification putNotification(String transactionId, Notification transaction) {

        Notification c = em.find(Notification.class, transactionId);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            transaction.setId(c.getId());
            transaction = em.merge(transaction);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return transaction;
    }

    public boolean deleteNotification(String transactionId) {

        Notification transaction = em.find(Notification.class, transactionId);

        if (transaction != null) {
            try {
                beginTx();
                em.remove(transaction);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}

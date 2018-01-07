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

    public Notification getNotification(String notificationId) {

        Notification notification = em.find(Notification.class, notificationId);

        if (notification == null) {
            throw new NotFoundException();
        }

        return notification;
    }

    public Notification createNotification(Notification notification) {

        try {
            beginTx();
            em.persist(notification);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return notification;
    }

    public Notification putNotification(String notificationId, Notification notification) {

        Notification c = em.find(Notification.class, notificationId);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            notification.setId(c.getId());
            notification = em.merge(notification);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return notification;
    }

    public boolean deleteNotification(String notificationId) {

        Notification notification = em.find(Notification.class, notificationId);

        if (notification != null) {
            try {
                beginTx();
                em.remove(notification);
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

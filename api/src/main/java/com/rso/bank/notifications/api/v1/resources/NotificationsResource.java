package com.rso.bank.notifications.api.v1.resources;

import com.rso.bank.notifications.Notification;
import com.rso.bank.notifications.api.v1.configuration.RestProperties;
import com.rso.bank.notifications.services.NotificationsBean;

import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Metered;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class NotificationsResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private RestProperties restProperties;


    @Inject
    private NotificationsBean notificationsBean;

    @GET
    @Metered
    public Response getNotifications() {

        List<Notification> notifications = notificationsBean.getNotifications(uriInfo);

        return Response.ok(notifications).build();
    }

    @GET
    @Path("/{notificationId}")
    public Response getNotification(@PathParam("notificationId") String notificationId) {

        Notification notification = notificationsBean.getNotification(notificationId);

        if (notification == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(notification).build();
    }

    @POST
    public Response createNotification(Notification notification) {

        if (notification.getTitle() == null || notification.getTitle().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            notification = notificationsBean.createNotification(notification);
        }

        if (notification.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(notification).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(notification).build();
        }
    }

    @PUT
    @Path("{notificationId}")
    public Response putNotification(@PathParam("notificationId") String notificationId, Notification notification) {

        notification = notificationsBean.putNotification(notificationId, notification);

        if (notification == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (notification.getId() != null)
                return Response.status(Response.Status.OK).entity(notification).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("{notificationId}")
    public Response deleteNotification(@PathParam("notificationId") String notificationId) {

        boolean deleted = notificationsBean.deleteNotification(notificationId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("healthy")
    public Response setHealth(Boolean healthy) {
        restProperties.setHealthy(healthy);
        return Response.ok().build();
    }

    @GET
    @Path("healthy")
    public Response getHealth() {
        restProperties.isHealthy();
        return Response.ok().entity(restProperties.isHealthy()).build();
    }
}

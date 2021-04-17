package org.labmonkeys.cajun_navy.responder.api;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.labmonkeys.cajun_navy.responder.dto.ResponderDTO;
import org.labmonkeys.cajun_navy.responder.dto.ResponderQueryDTO;
import org.labmonkeys.cajun_navy.responder.dto.ResponderStatsDTO;
import org.labmonkeys.cajun_navy.responder.service.ResponderService;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;

@Path("/")
public class ResponderApi {

    @Inject
    EventBus bus;

    @Inject
    ResponderService service;

    @GET
    @Path("/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> stats() {
        return bus.<ResponderStatsDTO>request("stats", null).onItem().transform(msg -> Response.ok(msg.body()).build());
    }

    @GET
    @Path("/responder/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> responder(@PathParam("id") String responderId) {
        return bus.<ResponderDTO>request("responderById", responderId).onItem().transform(msg -> {
            ResponderDTO dto = msg.body();
            if (dto == null) {
                return Response.ok(Status.NOT_FOUND).build();
            } else {
                return Response.ok(dto).build();
            }
        });
    }

    @GET
    @Path("/responder/byname/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> responderByName(@PathParam("name") String name) {
        return bus.<List<ResponderDTO>>request("responderByName", name).onItem().transform(msg -> {
            List<ResponderDTO> dtos = msg.body();
            if (dtos.size() == 0) {
                return Response.ok(Status.NOT_FOUND).build();
            } else {
                return Response.ok(dtos).build();
            }
        });
    }

    @GET
    @Path("/responders/available")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> availableResponders(@QueryParam("disasterId") Optional<String> disasterId, @QueryParam("limit") Optional<Integer> limit, @QueryParam("offset") Optional<Integer> offset) {
        ResponderQueryDTO message = new ResponderQueryDTO();
        if (disasterId.isPresent()) {
            message.setDisasterId(disasterId.get());
        } else {
            message.setDisasterId("");
        }
        if (limit.isPresent()) {
            message.setLimit(limit.get());
        } else {
            message.setLimit(0);
        }
        if (offset.isPresent()) {
            message.setOffset(offset.get());
        } else {
            message.setOffset(0);
        }
        return bus.<List<ResponderDTO>>request("availableResponders", message).onItem().transform(msg -> Response.ok(msg.body()).build());
    }

    @GET
    @Path("/responders")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> responders(@QueryParam("disasterId") Optional<String> disasterId, @QueryParam("limit") Optional<Integer> limit, @QueryParam("offset") Optional<Integer> offset) {
        ResponderQueryDTO message = new ResponderQueryDTO();
        if (disasterId.isPresent()) {
            message.setDisasterId(disasterId.get());
        } else {
            message.setDisasterId("");
        }
        if (limit.isPresent()) {
            message.setLimit(limit.get());
        } else {
            message.setLimit(0);
        }
        if (offset.isPresent()) {
            message.setOffset(offset.get());
        } else {
            message.setOffset(0);
        }
        return bus.<List<ResponderDTO>>request("responders", message).onItem().transform(msg -> Response.ok(msg.body()).build());
    }

    @POST
    @Path("/responder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createResponder(ResponderDTO dto) {
        return bus.<ResponderDTO>request("createResponder", dto).onItem().transform(msg -> Response.ok(msg.body()).build());
    }

    @PUT
    @Path("/responder/info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateResponderInfo(ResponderDTO dto) {
        return bus.<ResponderDTO>request("updateResponderInfo", dto).onItem().transform(msg -> Response.ok(msg.body()).build());
    }

    @PUT
    @Path("/responder/available")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateResponderAvailable(ResponderDTO dto) {
        return bus.<ResponderDTO>request("updateResponderAvailable", dto).onItem().transform(msg -> Response.ok(msg.body()).build());
    }

    @PUT
    @Path("/responder/location")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateResponderLocation(ResponderDTO dto) {
        return bus.<ResponderDTO>request("updateResponderLocation", dto).onItem().transform(msg -> Response.ok(msg.body()).build());
    }

    @POST
    @Path("/responders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createResponders(List<ResponderDTO> dtos) {
        return bus.<List<ResponderDTO>>request("createResponders", dtos).onItem().transform(msg -> Response.ok(msg.body()).build());
    }

    @POST
    @Path("/responders/reset")
    public Response reset() {
        service.reset();
        return Response.ok().build();
    }

    @POST
    @Path("/responders/clear")
    public Response clear(@QueryParam("deleteBots") Optional<Boolean> delete) {

        service.clear(false, false);
        return Response.ok().build();
    }

    @POST
    @Path("/responders/clear/delete-bots")
    public Response clearAndDeleteBots() {
        service.clear(true, false);
        return Response.ok().build();
    }

    @POST
    @Path("/responders/clear/delete-all")
    public Response clearAndDeleteAll() {
        service.clear(false, true);
        return Response.ok().build();
    }
}

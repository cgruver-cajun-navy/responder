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
import org.labmonkeys.cajun_navy.responder.dto.ResponderStatsDTO;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.eventbus.EventBus;

@Path("/")
public class ResponderApi {

    @Inject
    EventBus bus;

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
    public Uni<Response> availableResponders(@QueryParam("limit") Optional<Integer> limit, @QueryParam("offset") Optional<Integer> offset) {
        JsonObject message = new JsonObject();
        if (limit.isPresent()) {
            message.put("limit", limit.get());
        } else {
            message.put("limit", 0);
        }
        if (offset.isPresent()) {
            message.put("offset", offset.get());
        } else {
            message.put("offset", 0);
        }
        return bus.<List<ResponderDTO>>request("avalableResponders", message).onItem().transform(msg -> Response.ok(msg.body()).build());
    }

    @GET
    @Path("/responders")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> allResponders(@QueryParam("limit") Optional<Integer> limit, @QueryParam("offset") Optional<Integer> offset) {
        JsonObject message = new JsonObject();
        if (limit.isPresent()) {
            message.put("limit", limit.get());
        }
        if (offset.isPresent()) {
            message.put("offset", offset.get());
        }
        return bus.<List<ResponderDTO>>request("allResponders", message).onItem().transform(msg -> Response.ok(msg.body()).build());
    }

    @POST
    @Path("/responder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createResponder(ResponderDTO dto) {
        return bus.<ResponderDTO>request("createResponder", dto).onItem().transform(msg -> Response.ok(msg.body()).build());
    }

    @PUT
    @Path("/responder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateResponder(ResponderDTO dto) {
        return bus.<ResponderDTO>request("updateResponder", dto).onItem().transform(msg -> Response.ok(msg.body()).build());
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
        responderService.reset();
        return Response.ok().build();
    }

    @POST
    @Path("/responders/clear")
    public Response clear(@QueryParam("delete") Optional<String> delete) {

        if (delete.orElse("").equals("all")) {
            responderService.deleteAll();
        } else if (delete.orElse("").equals("bots")) {
            responderService.clear(true);
        } else {
            responderService.clear(false);
        }
        return Response.ok().build();
    }
}

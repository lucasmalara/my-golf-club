package com.mygolfclub.controller.rest;

import com.mygolfclub.assembler.GolfClubMemberModelAssembler;
import com.mygolfclub.entity.member.GolfClubMember;
import com.mygolfclub.exception.GolfClubMemberNotFoundException;
import com.mygolfclub.service.member.GolfClubMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Stream;

import static com.mygolfclub.utils.constants.ConstantsProvider.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(API)
@Tag(name = "GolfClubMembers")
public class GolfClubMemberController {


    private final GolfClubMemberService memberService;
    private final GolfClubMemberModelAssembler assembler;

    @Autowired
    public GolfClubMemberController(GolfClubMemberService memberService,
                                    GolfClubMemberModelAssembler assembler) {
        this.memberService = memberService;
        this.assembler = assembler;
    }

    @Operation(
            description = "Get members - all, active or inactive",
            summary = "Get members",
            responses = {
                    @ApiResponse(ref = "get", responseCode = "200"),
                    @ApiResponse(ref = "forbidden", responseCode = "403")
            }
    )
    @GetMapping("/members")
    public CollectionModel<EntityModel<GolfClubMember>> getMembers(
            @RequestParam(value = "active", required = false) Boolean isActiveMember) {
        Stream<GolfClubMember> memberStream = memberService.findAll().stream();
        if (isActiveMember != null) {
            memberStream = memberStream
                    .filter(clubMember ->
                            clubMember.isActiveMember() == isActiveMember);
        }
        return CollectionModel
                .of(memberStream.map(assembler::toModel).toList(),
                        linkTo(methodOn(GolfClubMemberController.class).getMembers(isActiveMember))
                                .withSelfRel()
                );
    }


    @Operation(
            description = "Get member by id",
            summary = "Get member",
            responses = {
                    @ApiResponse(ref = "getById", responseCode = "200"),
                    @ApiResponse(ref = "notFoundById", responseCode = "404"),
                    @ApiResponse(ref = "forbiddenById", responseCode = "403")
            }
    )
    @GetMapping("/members/{memberId}")
    public EntityModel<GolfClubMember> getMember(@PathVariable(value = "memberId") int id) {
        return assembler.toModel(memberService.findById(id));
    }

    @Operation(
            description = "Create a member",
            summary = "Create a member",
            responses = {
                    @ApiResponse(ref = "post", responseCode = "201"),
                    @ApiResponse(ref = "postNotValid", responseCode = "400"),
                    @ApiResponse(ref = "forbidden", responseCode = "403")
            }

    )
    @PostMapping("/members")
    public ResponseEntity<EntityModel<GolfClubMember>> addMember(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Member to add",
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = VALID_POST_REQUEST_BODY
                            )
                    )
            )
            @RequestBody GolfClubMember member) {
        EntityModel<GolfClubMember> entityModel = assembler.toModel(memberService.save(member));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(
            description = "Update a updatedMember by id",
            summary = "Update a member",
            responses = {
                    @ApiResponse(ref = "putById", responseCode = "201"),
                    @ApiResponse(ref = "putNotValid", responseCode = "400"),
                    @ApiResponse(ref = "notFoundById", responseCode = "404"),
                    @ApiResponse(ref = "forbiddenById", responseCode = "403")
            }
    )
    @PutMapping("/members/{memberId}")
    public ResponseEntity<EntityModel<GolfClubMember>> updateMember(
            @PathVariable(value = "memberId") int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updates on a member by id",
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = VALID_PUT_REQUEST_BODY
                            )
                    )
            )
            @RequestBody GolfClubMember updatedMember) {
        EntityModel<GolfClubMember> entityModel = assembler.toModel(
                Optional.of(memberService.findById(id))
                        .map(memberById -> {
                            memberById.setFirstName(updatedMember.getFirstName());
                            memberById.setLastName(updatedMember.getLastName());
                            memberById.setEmail(updatedMember.getEmail());
                            memberById.setActiveMember(updatedMember.isActiveMember());
                            return memberService.save(memberById);
                        })
                        .orElseThrow(() ->
                                new GolfClubMemberNotFoundException(String.valueOf(id))
                        )
        );
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(
            description = "Delete a member by id",
            summary = "Delete a member",
            responses = {
                    @ApiResponse(ref = "deleted", responseCode = "204"),
                    @ApiResponse(ref = "notFoundById", responseCode = "404"),
                    @ApiResponse(ref = "forbiddenById", responseCode = "403")
            }
    )
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable(value = "memberId") int id) {
        memberService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

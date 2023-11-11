package com.mygolfclub.assembler;

import com.mygolfclub.controller.rest.GolfClubMemberController;
import com.mygolfclub.entity.member.GolfClubMember;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GolfClubMemberModelAssembler
        implements RepresentationModelAssembler<GolfClubMember, EntityModel<GolfClubMember>> {

    @Override
    @NonNull
    public EntityModel<GolfClubMember> toModel(@NonNull GolfClubMember member) {
        return EntityModel.of(member,
                linkTo(methodOn(GolfClubMemberController.class)
                        .getMember(member.getId())).withSelfRel(),
                linkTo(methodOn(GolfClubMemberController.class)
                        .getMembers(member.isActiveMember())).withRel("active"));
    }
}

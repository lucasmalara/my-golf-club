package com.mygolfclub.utils;

import com.mygolfclub.entity.member.GolfClubMember;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.Optional;

public class GolfClubMemberExtension implements ParameterResolver {

    private static GolfClubMember createExample() {
        return GolfClubMember.builder()
                .firstName("Lorem")
                .lastName("Ipsum")
                .email("dolor@sit.amet")
                .activeMember(true)
                .build();
    }

    @Override
    public boolean supportsParameter(ParameterContext paramContext, ExtensionContext extContext)
            throws ParameterResolutionException {
        Parameter param = paramContext.getParameter();
        return param.isAnnotationPresent(InjectMember.class) && param.getType().equals(GolfClubMember.class);
    }

    @Override
    public Object resolveParameter(ParameterContext paramContext, ExtensionContext extContext)
            throws ParameterResolutionException {
        Optional<InjectMember> injectMemberOptional = paramContext.findAnnotation(InjectMember.class);
        assert injectMemberOptional.isPresent();

        InjectMember injectMember = injectMemberOptional.get();
        return GolfClubMember.builder()
                .firstName(injectMember.firstName())
                .lastName(injectMember.lastName())
                .email(injectMember.email())
                .build();
    }
}

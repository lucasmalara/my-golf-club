package com.mygolfclub.utils;

import com.mygolfclub.entity.member.GolfClubMember;

public class GolfClubMemberTestsUtils {

    public static GolfClubMember memberExample() {
        return GolfClubMember.builder()
                .firstName("Lorem")
                .lastName("Ipsum")
                .email("dolor@sit.amet")
                .activeMember(true)
                .build();

    }
}

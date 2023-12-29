package com.mygolfclub.utils;

import com.mygolfclub.entity.member.GolfClubMember;

public class GolfClubMemberTestsUtils {


    public static GolfClubMember memberExample(String firstName,
                                                String lastName,
                                                String email,
                                                boolean isActiveMember) {
        boolean anyNullProperty =
                firstName == null || lastName == null || email == null;
        if (anyNullProperty)
            return memberExample();

        GolfClubMember memberExample = new GolfClubMember();
        memberExample.setFirstName(firstName);
        memberExample.setLastName(lastName);
        memberExample.setEmail(email);
        memberExample.setActiveMember(isActiveMember);
        return memberExample;
    }

    public static GolfClubMember memberExample() {
        return memberExample("Lorem",
                "Ipsum",
                "dolor@sit.amet",
                true);
    }
}

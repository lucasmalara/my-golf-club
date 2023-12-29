package com.mygolfclub.service.member;

import com.mygolfclub.entity.member.GolfClubMember;

import java.util.Collection;
import java.util.List;

public interface GolfClubMemberService {
    Collection<GolfClubMember> findAll();

    Collection<GolfClubMember> findAllByActivity(boolean isActive);

    List<GolfClubMember> findAllByFirstAndLastName(String firstName, String lastName);

    GolfClubMember findById(int id);

    GolfClubMember save(GolfClubMember member);

    void deleteById(int id);
}

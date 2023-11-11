package com.mygolfclub.service.member;

import com.mygolfclub.entity.member.GolfClubMember;

import java.util.Collection;

public interface GolfClubMemberService {
    Collection<GolfClubMember> findAll();

    Collection<GolfClubMember> findAllByActivity(boolean isActive);

    GolfClubMember findById(int id);

    GolfClubMember save(GolfClubMember member);

    void deleteById(int id);
}

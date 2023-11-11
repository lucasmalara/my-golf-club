package com.mygolfclub.persistence;

import com.mygolfclub.entity.member.GolfClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GolfClubMemberRepository extends JpaRepository<GolfClubMember, Integer> {
    List<GolfClubMember> findAllByActiveMember(boolean isActiveMember);
}

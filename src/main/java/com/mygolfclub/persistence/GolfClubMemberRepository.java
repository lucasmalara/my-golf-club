package com.mygolfclub.persistence;

import com.mygolfclub.entity.member.GolfClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GolfClubMemberRepository extends JpaRepository<GolfClubMember, Integer> {

    List<GolfClubMember> findAllByActiveMember(boolean isActiveMember);

    @Query("select m from GolfClubMember m where m.firstName=:firstName and m.lastName=:lastName")
    List<GolfClubMember> findAllByFirstAndLastName(@Param("firstName") String firstName,
                                                   @Param("lastName") String lastName);
}

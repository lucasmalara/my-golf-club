package com.mygolfclub.service.member;

import com.mygolfclub.entity.member.GolfClubMember;
import com.mygolfclub.exception.GolfClubMemberNotFoundException;
import com.mygolfclub.persistence.GolfClubMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class GolfClubMemberServiceImpl implements GolfClubMemberService {

    private final GolfClubMemberRepository memberRepository;

    @Autowired
    public GolfClubMemberServiceImpl(GolfClubMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Collection<GolfClubMember> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Collection<GolfClubMember> findAllByActivity(boolean isActive) {
        return memberRepository.findAllByActiveMember(isActive);
    }

    @Override
    public GolfClubMember findById(int id) {
        return memberRepository
                .findById(id)
                .orElseThrow(() ->
                        new GolfClubMemberNotFoundException(String.valueOf(id)));
    }

    @Override
    public GolfClubMember save(GolfClubMember member) {
        return memberRepository.save(member);
    }

    @Override
    public void deleteById(int id) {
        memberRepository.deleteById(id);
    }
}

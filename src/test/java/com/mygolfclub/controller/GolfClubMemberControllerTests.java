package com.mygolfclub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygolfclub.assembler.GolfClubMemberModelAssembler;
import com.mygolfclub.controller.rest.GolfClubMemberController;
import com.mygolfclub.entity.member.GolfClubMember;
import com.mygolfclub.service.member.GolfClubMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.mygolfclub.utils.GolfClubMemberTestsUtils.memberExample;
import static com.mygolfclub.utils.constants.ConstantProvider.API;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GolfClubMemberController.class)
class GolfClubMemberControllerTests {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    @MockBean
    private GolfClubMemberService memberServiceMock;
    @MockBean
    private GolfClubMemberModelAssembler modelAssemblerMock;

    @Autowired
    GolfClubMemberControllerTests(MockMvc mvc, ObjectMapper mapper) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @Test
    @DisplayName("Testing member GET request. - All, not authenticated.")
    void givenMembers_whenGetMembers_thenStatusIsUnauthorized() throws Exception {

        // given
        GolfClubMember member1 = memberExample();
        GolfClubMember member2 = memberExample("Jessy", "Pinkman", "pink@man.com", true);
        GolfClubMember member3 = memberExample("Jimmy", "McGill", "betterc@llsaul.com", false);
        given(memberServiceMock.findAll())
                .willReturn(List.of(member1, member2, member3));

        // when
        ResultActions action = mvc.perform(get(API + "/members"));

        // then
        action.andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("Testing member GET request. - One by id, not authenticated.")
    void givenId_whenGetMember_thenStatusIsUnauthorized() throws Exception {

        // given
        GolfClubMember memberExample = memberExample();
        given(memberServiceMock.findById(memberExample.getId()))
                .willReturn(memberExample);

        // when
        ResultActions action =
                mvc.perform(get(API + "/members/{memberId}", memberExample.getId()));

        // then
        action.andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("Testing member POST request. - Not authenticated.")
    void givenMember_whenPostMember_thenStatusIsForbidden() throws Exception {

        // given
        GolfClubMember memberExample = memberExample();
        given(memberServiceMock.save(any(GolfClubMember.class)))
                .willAnswer(invocation -> invocation.getArgument(0, GolfClubMember.class));

        // when
        ResultActions action = mvc.perform(post(API + "/members")
                .accept(APPLICATION_JSON)
                .content(mapper.writeValueAsString(memberExample)));

        // then
        action.andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("Testing member PUT request. - One by id, not authenticated.")
    void givenIdAndMember_whenPutMember_thenStatusIsForbidden() throws Exception {

        // given
        GolfClubMember memberExample = memberExample();
        given(memberServiceMock.findById(memberExample.getId()))
                .willReturn(memberExample);
        given(memberServiceMock.save(any(GolfClubMember.class)))
                .willAnswer(invocation -> invocation.getArgument(0, GolfClubMember.class));

        memberExample.setFirstName("Nacho");
        memberExample.setLastName("Varga");
        memberExample.setEmail("n@v.mx");
        memberExample.setActiveMember(true);

        // when
        ResultActions action = mvc.perform(put(API + "/members/{memberId}", memberExample.getId())
                .accept(APPLICATION_JSON)
                .content(mapper.writeValueAsString(memberExample)));

        // then
        action.andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("Testing member DELETE request. - One by id, not authenticated.")
    void givenId_whenDeleteMember_thenStatusIsForbidden() throws Exception {

        // given
        int id = 1;
        willDoNothing().given(memberServiceMock).deleteById(id);

        // when
        ResultActions action = mvc.perform(delete(API + "/members/{memberId}", id));

        // then
        action.andExpect(status().isForbidden())
                .andDo(print());
    }
}

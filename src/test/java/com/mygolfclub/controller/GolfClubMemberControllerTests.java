package com.mygolfclub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygolfclub.assembler.GolfClubMemberModelAssembler;
import com.mygolfclub.controller.rest.GolfClubMemberController;
import com.mygolfclub.entity.member.GolfClubMember;
import com.mygolfclub.service.member.GolfClubMemberService;
import com.mygolfclub.utils.GolfClubMemberExtension;
import com.mygolfclub.utils.InjectMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.mygolfclub.utils.constants.ConstantProvider.API;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(GolfClubMemberExtension.class)
@WebMvcTest(GolfClubMemberController.class)
class GolfClubMemberControllerTests {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    @MockBean
    private GolfClubMemberService memberServiceMock;
    @MockBean
    private GolfClubMemberModelAssembler modelAssemblerMock;
    private final GolfClubMember memberExample;
    
    GolfClubMemberControllerTests(@Autowired MockMvc mvc,
                                  @Autowired ObjectMapper mapper,
                                  @InjectMember GolfClubMember memberExample) {
        this.mvc = mvc;
        this.mapper = mapper;
        this.memberExample = memberExample;
    }

    @Test
    @DisplayName("Testing member GET request. - All, not authenticated.")
    void givenMembers_whenGetMembers_thenStatusIsUnauthorized() throws Exception {

        // given
        GolfClubMember member1 = memberExample.deepCopy();
        GolfClubMember member2 = GolfClubMember.builder()
                .firstName("Jessy")
                .lastName("Pinkman")
                .email("pink@man.com")
                .activeMember(true)
                .build();
        GolfClubMember member3 = GolfClubMember.builder()
                .firstName("Jimmy")
                .lastName("McGill")
                .email("betterc@llsaul.com")
                .activeMember(false)
                .build();
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
        GolfClubMember example = memberExample.deepCopy();
        given(memberServiceMock.findById(example.getId()))
                .willReturn(example);

        // when
        ResultActions action =
                mvc.perform(get(API + "/members/{memberId}", example.getId()));

        // then
        action.andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("Testing member POST request. - Not authenticated.")
    void givenMember_whenPostMember_thenStatusIsForbidden() throws Exception {

        // given
        GolfClubMember example = memberExample.deepCopy();
        given(memberServiceMock.save(any(GolfClubMember.class)))
                .willAnswer(invocation -> invocation.getArgument(0, GolfClubMember.class));

        // when
        ResultActions action = mvc.perform(post(API + "/members")
                .accept(APPLICATION_JSON)
                .content(mapper.writeValueAsString(example)));

        // then
        action.andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("Testing member PUT request. - One by id, not authenticated.")
    void givenIdAndMember_whenPutMember_thenStatusIsForbidden() throws Exception {

        // given
        GolfClubMember example = memberExample.deepCopy();
        given(memberServiceMock.findById(example.getId()))
                .willReturn(example);
        given(memberServiceMock.save(any(GolfClubMember.class)))
                .willAnswer(invocation -> invocation.getArgument(0, GolfClubMember.class));

        example.setFirstName("Nacho");
        example.setLastName("Varga");
        example.setEmail("n@v.mx");
        example.setActiveMember(true);

        // when
        ResultActions action = mvc.perform(put(API + "/members/{memberId}", example.getId())
                .accept(APPLICATION_JSON)
                .content(mapper.writeValueAsString(example)));

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

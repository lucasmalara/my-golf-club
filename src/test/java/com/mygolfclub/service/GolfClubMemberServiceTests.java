package com.mygolfclub.service;

import com.mygolfclub.entity.member.GolfClubMember;
import com.mygolfclub.exception.GolfClubMemberNotFoundException;
import com.mygolfclub.persistence.GolfClubMemberRepository;
import com.mygolfclub.service.member.GolfClubMemberServiceImpl;
import com.mygolfclub.utils.GolfClubMemberExtension;
import com.mygolfclub.utils.InjectMember;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.mygolfclub.utils.GolfClubMemberTestsUtils.memberExample;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(GolfClubMemberExtension.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GolfClubMemberServiceTests {

    @Mock
    private GolfClubMemberRepository memberRepositoryMock;

    @InjectMocks
    private GolfClubMemberServiceImpl memberService;

    private final GolfClubMember memberExample;

    GolfClubMemberServiceTests(@InjectMember GolfClubMember memberExample) {
        this.memberExample = memberExample;
    }

    @Order(1)
    @Test
    @DisplayName("Testing member save service method.")
    void givenMember_whenSaveMember_thenReturnSameMember() {

        // given
        GolfClubMember toSave = memberExample.deepCopy();

        // when
        when(memberRepositoryMock.save(toSave))
                .thenReturn(toSave);
        GolfClubMember saved = memberService.save(toSave);

        // then
        then(memberRepositoryMock)
                .should()
                .save(toSave);

        assertThat(saved)
                .isNotNull()
                .isEqualTo(toSave);
    }

    @Order(2)
    @ParameterizedTest
    @NullSource
    @DisplayName("Testing member save service method. - Null entity.")
    void givenNullMember_whenSaved_thenThrowInvalidDataAccessApiUsageException(GolfClubMember nullMember) {

        // when
        when(memberRepositoryMock.save(nullMember))
                .thenThrow(InvalidDataAccessApiUsageException.class);

        // then
        then(memberRepositoryMock)
                .should(never())
                .save(any(GolfClubMember.class));

        assertThatThrownBy(() -> memberService.save(nullMember))
                .isExactlyInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Order(3)
    @Test
    @DisplayName("Testing member find service method. - All found.")
    void givenMembers_whenFindAll_thenReturnTheseMembers() {

        // given
        GolfClubMember member1 = memberExample.deepCopy();
        GolfClubMember member2 = GolfClubMember.builder()
                .firstName("Mockito")
                .lastName("Poquito")
                .email("inject@mock.es")
                .activeMember(true)
                .build();
        List<GolfClubMember> expected = List.of(member1, member2);

        // when
        when(memberRepositoryMock.findAll())
                .thenReturn(expected);
        Collection<GolfClubMember> allFound = memberService.findAll();

        // then
        then(memberRepositoryMock)
                .should()
                .findAll();

        assertThat(allFound)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Order(4)
    @ParameterizedTest
    @EmptySource
    @DisplayName("Testing member find service method. - All empty.")
    void givenNoMembers_whenFindAll_thenReturnEmptyList(List<GolfClubMember> noMembers) {

        // when
        when(memberRepositoryMock.findAll())
                .thenReturn(noMembers);
        Collection<GolfClubMember> allFound = memberService.findAll();

        // then
        then(memberRepositoryMock)
                .should()
                .findAll();

        assertThat(allFound)
                .isNotNull()
                .isEmpty();
    }

    @Order(5)
    @ParameterizedTest
    @ValueSource(ints = {0, 1, Integer.MAX_VALUE})
    @DisplayName("Testing member find by id service method. - Not found by id.")
    void givenId_whenFindById_thenReturnMemberWithExactId(int id) {

        // given
        GolfClubMember toFindById = memberExample.deepCopy();
        toFindById.setId(id);

        // when
        when(memberRepositoryMock.findById(id))
                .thenReturn(Optional.of(toFindById));
        GolfClubMember byId = memberService.findById(id);

        // then
        then(memberRepositoryMock)
                .should()
                .findById(id);

        Condition<GolfClubMember> havingExpectedId =
                new Condition<>(member -> member.getId() == id,
                        "id equals expected");
        assertThat(byId)
                .isNotNull()
                .is(havingExpectedId)
                .isEqualTo(toFindById);
    }

    @Order(6)
    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE})
    @DisplayName("Testing member find by id service method. - One found by id.")
    void givenId_whenFindById_thenThrowGolfClubMemberNotFoundException(int id) {

        // when
        when(memberRepositoryMock.findById(anyInt()))
                .thenReturn(Optional.empty());

        // then
        then(memberRepositoryMock)
                .should(never())
                .findById(id);

        assertThatThrownBy(() -> memberService.findById(id))
                .isExactlyInstanceOf(GolfClubMemberNotFoundException.class);
    }

    @Order(7)
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Testing member find all by is active member service method. - All found by their property value.")
    void givenBoolean_whenFindAllByActiveMember_thenReturnExpectedMemberList(boolean b) {

        // given
        GolfClubMember inactiveMember1 = memberExample.deepCopy();
        inactiveMember1.setActiveMember(false);
        GolfClubMember activeMember = GolfClubMember.builder()
                .firstName("Other")
                .lastName("Member")
                .email("other@gmail.com")
                .activeMember(true)
                .build();
        GolfClubMember inactiveMember2 = GolfClubMember.builder()
                .firstName("Foo")
                .lastName("Bar")
                .email("foo@bar.baz")
                .activeMember(false)
                .build();
        List<GolfClubMember> expected =
                b ? List.of(activeMember) : List.of(inactiveMember1, inactiveMember2);

        // when
        when(memberRepositoryMock.findAllByActiveMember(b)).
                thenReturn(expected);
        Collection<GolfClubMember> foundByActivity = memberService.findAllByActivity(b);

        // then
        then(memberRepositoryMock)
                .should()
                .findAllByActiveMember(b);

        assertThat(foundByActivity)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Order(8)
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    @DisplayName("Testing member find all by first and last name service method. - All found by first name and last name.")
    void givenFirstAndLastName_whenFindAllByFirstAndLastName_thenReturnExpectedMemberList(int bound) {

        // given
        GolfClubMember example = memberExample.deepCopy();
        final String firstName = example.getFirstName();
        final String lastName = example.getLastName();

        List<GolfClubMember> expected = new ArrayList<>();
        IntStream.rangeClosed(1, bound).forEach(i -> {
            GolfClubMember member = new GolfClubMember();
            member.setId(i);
            member.setFirstName(firstName);
            member.setLastName(lastName);
            member.setEmail(member.getEmail());
            expected.add(member);
        });

        // when
        when(memberRepositoryMock.findAllByFirstAndLastName(firstName, lastName))
                .thenReturn(expected);
        List<GolfClubMember> byFirstAndLastName =
                memberService.findAllByFirstAndLastName(firstName, lastName);

        // then
        then(memberRepositoryMock)
                .should()
                .findAllByFirstAndLastName(firstName, lastName);

        assertThat(byFirstAndLastName)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Order(9)
    @Test
    @DisplayName("Testing member update service method.")
    void givenSavedMember_whenUpdate_thenMemberReplaced() {

        // given
        GolfClubMember example = memberExample.deepCopy();
        given(memberRepositoryMock.save(example))
                .willReturn(example);
        GolfClubMember toUpdate = memberService.save(example);

        // when
        toUpdate.setFirstName("UpFirstName");
        toUpdate.setLastName("UpLastName");
        toUpdate.setEmail("updated@email.com");
        when(memberRepositoryMock.save(toUpdate))
                .thenReturn(toUpdate);
        when(memberRepositoryMock.findById(example.getId()))
                .thenReturn(Optional.of(toUpdate));
        memberService.save(toUpdate);

        // then
        then(memberRepositoryMock)
                .should(times(2))
                .save(argThat(toSave -> toSave.getId() == example.getId()));

        GolfClubMember byId = memberService.findById(example.getId());
        then(memberRepositoryMock)
                .should()
                .findById(memberExample().getId());

        Condition<GolfClubMember> havingExpectedId =
                new Condition<>(member -> member.getId() == example.getId(),
                        "id equals expected");
        assertThat(byId)
                .isNotNull()
                .is(havingExpectedId)
                .isEqualTo(toUpdate);
    }

    @Order(10)
    @Test
    @DisplayName("Testing member delete service method. - One by id.")
    void givenId_whenDeleteById_thenMemberRemoved() {

        // given
        GolfClubMember example = memberExample.deepCopy();
        given(memberRepositoryMock.save(example))
                .willReturn(example);
        GolfClubMember saved = memberService.save(example);
        int givenId = saved.getId();

        // when
        willDoNothing()
                .given(memberRepositoryMock)
                .deleteById(givenId);
        memberService.deleteById(givenId);

        // then
        then(memberRepositoryMock)
                .should()
                .deleteById(example.getId());

        assertThatThrownBy(() -> memberService.findById(givenId))
                .isExactlyInstanceOf(GolfClubMemberNotFoundException.class);
    }
}

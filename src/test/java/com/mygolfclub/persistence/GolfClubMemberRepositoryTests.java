package com.mygolfclub.persistence;

import com.mygolfclub.entity.member.GolfClubMember;
import com.mygolfclub.utils.GolfClubMemberExtension;
import com.mygolfclub.utils.InjectMember;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(GolfClubMemberExtension.class)
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
@Transactional
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GolfClubMemberRepositoryTests {

    private final GolfClubMemberRepository memberRepository;
    private final GolfClubMember memberExample;

    GolfClubMemberRepositoryTests(@Autowired GolfClubMemberRepository memberRepository,
                                  @InjectMember GolfClubMember memberExample) {
        this.memberRepository = memberRepository;
        this.memberExample = memberExample;
    }

    @Order(1)
    @Test
    @DisplayName("Testing member CREATE operation.")
    void givenMember_whenSaveMember_thenReturnSameMember() {

        // given
        GolfClubMember toSave = memberExample.deepCopy();

        // when
        GolfClubMember savedMember = memberRepository.save(toSave);

        // then
        assertThat(savedMember)
                .isNotNull()
                .isEqualTo(toSave);
    }

    @Order(2)
    @ParameterizedTest
    @NullSource
    @DisplayName("Testing member CREATE operation. - Null entity.")
    void givenNullMember_whenSavedMember_thenThrowInvalidDataAccessApiUsageExceptionAndFindAllDoesNotContainNullMember(
            GolfClubMember toSave) {

        // when
        var throwableAssert = assertThatThrownBy(() -> memberRepository.save(toSave));

        // then
        throwableAssert.isExactlyInstanceOf(InvalidDataAccessApiUsageException.class);

        List<GolfClubMember> all = memberRepository.findAll();

        assumeThat(all)
                .isNotEmpty();
        assertThat(all)
                .doesNotContain(toSave);
    }

    @Order(3)
    @Test
    @DisplayName("Testing member READ operation. - All.")
    void givenSavedMembers_whenFindAll_thenReturnMemberListContainsTheseMembersInExactOrder() {

        // given
        GolfClubMember toFind = GolfClubMember.builder()
                .firstName("Carol")
                .lastName("Greatest")
                .email("carol@the.greatest")
                .activeMember(false)
                .build();
        memberRepository.save(toFind);

        GolfClubMember otherToFind = GolfClubMember.builder()
                .firstName("Nicole")
                .lastName("Enigma")
                .email("nenigma@mydomain.xyz")
                .activeMember(true)
                .build();
        memberRepository.save(otherToFind);

        // when
        List<GolfClubMember> all = memberRepository.findAll();

        // then
        assertThat(all)
                .isNotNull()
                .containsSubsequence(toFind, otherToFind);
    }

    @Order(4)
    @Test
    @DisplayName("Testing member READ operation - One by id.")
    void givenSavedMember_whenFindById_thenReturnSameMember() {

        // given
        GolfClubMember toFindById = memberExample.deepCopy();
        memberRepository.save(toFindById);

        // when
        Optional<GolfClubMember> byIdOptional = memberRepository.findById(toFindById.getId());

        // then
        assertThat(byIdOptional)
                .isPresent()
                .containsSame(toFindById);
    }

    @Order(5)
    @ParameterizedTest
    @NullSource
    @DisplayName("Testing member READ operation. - One by null id.")
    void givenNullId_whenFindById_thenThrowInvalidDataAccessApiUsageException(Integer id) {

        // when
        var throwableAssert = assertThatThrownBy(() -> memberRepository.findById(id));

        // then
        throwableAssert.isExactlyInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Order(6)
    @ParameterizedTest
    @DisplayName("Testing member READ operation - All by their property value.")
    @ValueSource(booleans = {true, false})
    void givenSavedMembers_whenFindByActivity_thenReturnMemberListContainsTheseMembersInExactOrder(boolean b) {

        // given
        GolfClubMember toFindByActivity1 = GolfClubMember.builder()
                .firstName("Chris")
                .lastName("Ramzes")
                .email("ramzes@mail.eg")
                .activeMember(true)
                .build();
        memberRepository.save(toFindByActivity1);

        GolfClubMember toFindByActivity2 = GolfClubMember.builder()
                .firstName("Victoria")
                .lastName("Winner")
                .email("victoria@peace.roma")
                .activeMember(false)
                .build();
        memberRepository.save(toFindByActivity2);

        GolfClubMember toFindByActivity3 = GolfClubMember.builder()
                        .firstName("Micael")
                        .lastName("Stanley")
                        .email("mstanley@hotmail.com")
                        .activeMember(true)
                        .build();
        memberRepository.save(toFindByActivity3);

        var expected = b ? List.of(toFindByActivity1, toFindByActivity3) : List.of(toFindByActivity2);

        // when
        List<GolfClubMember> activeMembers = memberRepository.findAllByActiveMember(b);

        // then
        assertThat(activeMembers)
                .isNotNull()
                .containsSubsequence(expected);
    }

    @Order(7)
    @ParameterizedTest
    @CsvSource(value = {"Consectetur;Adipiscing", "Elit;Sed"}, delimiter = ';')
    @DisplayName("Testing member READ operation. - Custom query using JPQL with the named params; All by first name and last name.")
    void givenFirstAndLastName_whenFindByFirstAndLastName_thenReturnExactlyExpectedMembersList(String firstName,
                                                                                               String lastName) {

        // given
        String email =
                Character.toLowerCase(firstName.charAt(0)) + lastName.toLowerCase() + "@my.domain.com";

        GolfClubMember toFindByFirstAndLastName = GolfClubMember.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .activeMember(false)
                        .build();
        memberRepository.save(toFindByFirstAndLastName);

        // when
        List<GolfClubMember> byFirstAndLastName =
                memberRepository.findAllByFirstAndLastName(firstName, lastName);

        // then
        assertThat(byFirstAndLastName)
                .isNotNull();

        byFirstAndLastName.forEach(each -> {
            assertThat(each)
                    .isNotNull();

            assertThat(each.getFirstName())
                    .isEqualTo(firstName);

            assertThat(each.getLastName())
                    .isEqualTo(lastName);
        });
    }

    @Order(8)
    @ParameterizedTest
    @NullSource
    @DisplayName("Testing member READ operation. - Custom query using JPQL with named params; All by null first name and null last name.")
    void givenNullFirstAndLastName_whenFindByFirstAndLastName_thenReturnEmptyMembersList(String nullValue) {

        // when
        List<GolfClubMember> byFirstAndLastName =
                memberRepository.findAllByFirstAndLastName(nullValue, nullValue);

        // then
        assertThat(byFirstAndLastName)
                .isNotNull()
                .isEmpty();
    }

    @Order(9)
    @Test
    @DisplayName("Testing member UPDATE operation.")
    void givenSavedMember_whenUpdate_thenReturnUpdatedMember() {

        // given
        GolfClubMember toSave = GolfClubMember.builder()
                .firstName("Gallus")
                .lastName("Known")
                .email("gallus@known.net")
                .activeMember(true)
                .build();
        GolfClubMember saved = memberRepository.save(toSave);

        // when
        String updatedLastName = "Anonymous";
        saved.setLastName(updatedLastName);
        String updatedEmail = "x@anonymous.org";
        saved.setEmail(updatedEmail);
        GolfClubMember updated = memberRepository.save(saved);

        // then
        Optional<GolfClubMember> byIdOptional = memberRepository.findById(toSave.getId());

        assertThat(byIdOptional)
                .isPresent()
                .containsSame(updated);

        GolfClubMember byId = byIdOptional.get();

        Condition<GolfClubMember> havingUpdatedFields =
                new Condition<>(member ->
                        member.getLastName().equals(updatedLastName) && member.getEmail().equals(updatedEmail),
                        "member's updated fields");
        assertThat(byId)
                .is(havingUpdatedFields);
    }

    @Order(10)
    @Test
    @DisplayName("Testing member DELETE operation. - One by self.")
    void givenSavedMember_whenDeleteMember_thenMemberIsRemoved() {

        // given
        GolfClubMember toDelete = memberExample.deepCopy();
        memberRepository.save(toDelete);

        // when
        memberRepository.delete(toDelete);

        // then
        Optional<GolfClubMember> byIdOptional = memberRepository.findById(toDelete.getId());

        assertThat(byIdOptional)
                .isEmpty();

        List<GolfClubMember> all = memberRepository.findAll();

        assumeThat(all)
                .isNotEmpty();
        assertThat(all)
                .doesNotContain(toDelete);
    }

    @Order(11)
    @ParameterizedTest
    @NullSource
    @DisplayName("Testing member DELETE operation. - One by null self.")
    void givenNullMemberAndAllMembersInList_whenDeleteNullMember_thenThrowInvalidDataAccessApiUsageExceptionAndFindAllReturnSameList(
            GolfClubMember toDelete) {

        // given
        List<GolfClubMember> all = memberRepository.findAll();

        // when
        var throwableAssert = assertThatThrownBy(() -> memberRepository.delete(toDelete));

        // then
        throwableAssert.isExactlyInstanceOf(InvalidDataAccessApiUsageException.class);

        assertThat(all)
                .isNotNull()
                .isEqualTo(memberRepository.findAll());
    }

    @Order(12)
    @Test
    @DisplayName("Testing member DELETE operation. - One by id.")
    void givenSavedMember_whenDeleteMemberById_thenMemberIsRemoved() {

        // given
        GolfClubMember toDeleteById = memberExample.deepCopy();
        memberRepository.save(toDeleteById);

        // when
        memberRepository.deleteById(toDeleteById.getId());

        // then
        Optional<GolfClubMember> byIdOptional = memberRepository.findById(toDeleteById.getId());

        assertThat(byIdOptional)
                .isEmpty();

        List<GolfClubMember> all = memberRepository.findAll();

        assumeThat(all)
                .isNotEmpty();
        assertThat(all)
                .doesNotContain(toDeleteById);
    }

    @Order(13)
    @ParameterizedTest
    @NullSource
    @DisplayName("Testing member DELETE operation. - One by null id.")
    void givenNullIdAndAllMembersInList_whenDeleteMemberById_thenThrowInvalidDataAccessApiUsageExceptionAndFindAllReturnSameList(
            Integer id) {

        // given
        List<GolfClubMember> all = memberRepository.findAll();

        // when
        var throwableAssert = assertThatThrownBy(() -> memberRepository.deleteById(id));

        // then
        throwableAssert.isExactlyInstanceOf(InvalidDataAccessApiUsageException.class);

        assertThat(all)
                .isNotNull()
                .isEqualTo(memberRepository.findAll());
    }
}

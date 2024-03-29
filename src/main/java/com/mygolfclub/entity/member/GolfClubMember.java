package com.mygolfclub.entity.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "golf_club_member")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GolfClubMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotNull(message = "First name is required.")
    @Pattern(
            regexp = "^[A-Z][a-zA-Z]{1,45}",
            message = "First name is not valid."
    )
    @Length(
            min = 2,
            max = 45,
            message = "First name must contain between 2 and 45 alphabetic characters."
    )
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "Last name is required.")
    @Pattern(
            regexp = "^[A-Z][a-zA-Z]{1,45}",
            message = "Last name is not valid."
    )
    @Length(
            min = 2,
            max = 45,
            message = "Last name must contain between 2 and 45 alphabetic characters."
    )
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "Email is required.")
    @Pattern(
            regexp = "^(?=.{1,40}@)[a-z0-9._\\-]+@[a-z]+[a-z.]*(\\.[a-z]{2,})$",
            message = "Email is not valid."
    )
    @Size(
            min = 5,
            max = 45,
            message = "Email must contain between 5 and 45 characters."
    )
    @Column
    private String email;

    @NotNull(message = "is required.")
    @Column(name = "active_member")
    private boolean activeMember;

    public GolfClubMember() {
        // Added no args constructor after providing a builder for this class.
        // It is used for model creation.
    }

    public GolfClubMember deepCopy() {
        return SerializationUtils.clone(this);
    }

}

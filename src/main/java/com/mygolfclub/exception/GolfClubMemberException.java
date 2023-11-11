package com.mygolfclub.exception;

public abstract sealed class GolfClubMemberException extends RuntimeException permits GolfClubMemberNotFoundException {
    protected GolfClubMemberException(String message) {
        super(message);
    }
}

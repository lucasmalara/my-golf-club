package com.mygolfclub.exception;

public non-sealed class GolfClubMemberNotFoundException extends GolfClubMemberException {

    public GolfClubMemberNotFoundException(String id) {
        super("MyGolfClub member not found by id: " + id);
    }
}

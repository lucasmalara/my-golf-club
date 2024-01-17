package com.mygolfclub.utils.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstantProvider {
    public static final String HOME = "/my-golf-club";
    public static final String API = HOME + "/api";
    public static final String PREFIX_DIR_MEMBERS = "members/";
    public static final String PREFIX_DIR_USERS = "users/";
    public static final String SAVE_MEMBER_FILE = "save-member";

    public static final String VALID_POST_REQUEST_BODY = """
            {
              "firstName": "Walter",
              "lastName": "White",
              "email": "contact@heisenberg.com",
              "activeMember": true
            }""";
    public static final String VALID_PUT_REQUEST_BODY = """
            {
              "firstName": "Gustavo",
              "lastName": "Fring",
              "email": "contact@lospolloshermanos.com",
              "activeMember": false
            }""";
}

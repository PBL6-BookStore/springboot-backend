package com.pbl6.bookstore.common.constant;

import java.util.List;

/**
 * @author lkadai0801
 * @since 30/10/2022
 */
public class Constant {
    public final static String staticUri = "src/main/resources/static";
    public final static String staticImageUri = "/images/books/";

    // authentication
    public final static String PREFIX_TOKEN = "Bearer ";
    public final static String BYTE_CODE = "pbl6bookstore123";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String LOGIN_PATH = "/login";
    public static final String REFRESH_TOKEN_PATH = "/token/refresh";

    public static final String REGISTER_PATH = "/accounts";

    public static final List<String> UN_AUTHENTICATION_PATH = List.of(LOGIN_PATH, REFRESH_TOKEN_PATH, REGISTER_PATH);

}

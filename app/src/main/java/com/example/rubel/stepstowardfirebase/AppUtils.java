package com.example.rubel.stepstowardfirebase;

import java.util.regex.Pattern;

/**
 * Created by rubel on 5/8/2017.
 */

public class AppUtils {

    static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /*
     * validate email using email pattern
     */
    public static boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return  pattern.matcher(email).matches();
    }

    public static String getEmailFromPhone(String phone){
        String email;

        if(phone.charAt(0) == '+')
            email = phone.substring(1);
        else email = phone;

        email = new StringBuilder()
            .append(email)
            .append("@example.com")
            .toString();

        return email;
    }

    public static String getPasswordFromEmail(String phone){
        return phone;
    }
}

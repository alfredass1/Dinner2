package com.dinner.dinner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    private static final String CREDENTIALS_PATTERN = "^[a-zA-Z]{3,20}$";

    /*
    * Validuoja ivestus vartotojo duomenis, vartotojo vardas ir slaptazodis
    * @param perduodamas ivestas vartotojo vardas is slaptazodis
    * @return true-jei duomenys yra teisingi, false-jei duomenys yra neteisingi
    * */
    public static boolean isCredentialsValid(String credentials){
        Pattern pattern=Pattern.compile(CREDENTIALS_PATTERN);
        Matcher matcher=pattern.matcher(credentials);
        return matcher.matches();
    }
}
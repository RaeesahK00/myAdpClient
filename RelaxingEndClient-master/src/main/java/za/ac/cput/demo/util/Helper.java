package za.ac.cput.demo.util;

import org.apache.commons.validator.routines.EmailValidator;

public class Helper {

    public static boolean isNullOrEmpty(String string){
        if(string.isEmpty()) return false;
        else return true;
    }

    public static boolean isNullOrEmpty2(String s){
        if (s==null || s.trim().isEmpty()) {
            return true;
        }
            return false;
    }

    public static boolean emailValidator(String email){
        boolean valid = EmailValidator.getInstance().isValid(email);
        return valid;
    }


    public static boolean phoneNumValidator(String phoneNum){
        return phoneNum != null && phoneNum.matches("\\d{10}");
    }

}

package com.example.lab4.Domain.Validators;

import com.example.lab4.Domain.User;

public class UserValidator implements Validator<User>{
    @Override
    public void validate(User entity) throws ValidationException {
        String errMsg = "";
        if(entity.getId() == null)
            errMsg+= "Id error ";
        if(entity.getFirstName() == null || "".equals(entity.getFirstName()))
            errMsg+="first name error ";
        if(entity.getLastName() == null || "".equals(entity.getLastName()))
            errMsg+="last name error ";
        if(entity.getUserName() == null || "".equals(entity.getUserName()))
            errMsg+="user name error";
        if(entity.getPassword() == null|| "".equals(entity.getPassword()))
            errMsg+="password error";
        if(!errMsg.isEmpty())
            throw new ValidationException(errMsg);
    }
}

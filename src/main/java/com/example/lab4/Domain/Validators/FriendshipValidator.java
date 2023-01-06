package com.example.lab4.Domain.Validators;

import com.example.lab4.Domain.Friendship;
import com.example.lab4.Domain.FriendshipForDB;
import com.example.lab4.Repository.Repository;

public class FriendshipValidator implements Validator<FriendshipForDB> {

    @Override
    public void validate(FriendshipForDB entity) throws ValidationException {
        String errMsg = "";
        if(entity.getId() == null)
            errMsg+= "Id error ";
        if(!errMsg.isEmpty())
            throw new ValidationException(errMsg);

    }
}

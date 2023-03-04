package service;

import dao.*;
import model.Authtoken;
import model.User;
import request.LoginRequest;
import result.ClearResult;
import result.LoginResult;

import java.sql.Connection;
import java.util.UUID;


/**
 * The class that performs the login operation
 */

public class LoginService {

    public LoginResult login(LoginRequest r) {

        AuthtokenDao authtokenDao;
        UserDao userDao;
        Database db;


        try {
            db = new Database();

            Connection conn = db.getConnection();
            userDao = new UserDao(conn);
            authtokenDao = new AuthtokenDao(conn);
            String tokenID;

            if(userDao.findUser(r.getUsername())==null) {
                db.closeConnection(false);
                return new LoginResult(null,r.getUsername(),null);
            }
            User user = userDao.findUser(r.getUsername());

            if(!user.getPassword().equals(r.getPassword())) {
                db.closeConnection(false);
                return new LoginResult(null,r.getUsername(),null);
            }
            else {

                tokenID = UUID.randomUUID().toString();
                while(authtokenDao.find_token(tokenID)!=null) {
                    tokenID = UUID.randomUUID().toString();
                }
                Authtoken authtoken = new Authtoken(tokenID,r.getUsername());
                authtokenDao.insert_token(authtoken);

            }



            db.closeConnection(true);

            return new LoginResult(tokenID,r.getUsername(),user.getPersonID());
        }
        catch(DataAccessException d){

            return new LoginResult(null,r.getUsername(),null);
        }
    }


}

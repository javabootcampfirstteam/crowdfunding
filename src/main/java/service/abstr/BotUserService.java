package service.abstr;

import model.BotUser;

public interface BotUserService {

    void addUser(Integer id, BotUser botUser);
    BotUser getUser(Integer id);
    boolean isUserExistById(Integer id);
}

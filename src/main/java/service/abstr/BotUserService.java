package service.abstr;

import model.BotUser;

public interface BotUserService {
    void addUser (BotUser botUser, Integer id);
    BotUser getUser (Integer id);
    boolean isUserExistById (Integer id);
    Integer getUserContextById(BotUser botUser);
}

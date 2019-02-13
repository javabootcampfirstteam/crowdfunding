package dao.abstr;

import model.BotUser;

public interface BotUserDao {

    BotUser getBotUserById(int id);

    void addBotUser(int id, BotUser botUser);

    BotUser[] getAllUsers();
}

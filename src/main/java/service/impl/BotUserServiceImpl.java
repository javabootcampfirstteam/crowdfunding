package service.impl;

import dao.abstr.BotUserDao;
import dao.impl.BotUserDaoImpl;
import model.BotUser;
import service.abstr.BotUserService;
import storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class BotUserServiceImpl implements BotUserService {

    private static BotUserServiceImpl instance;



    private BotUserServiceImpl() {
    }

    public static BotUserServiceImpl getInstance(){
        if(instance==null){
            instance = new BotUserServiceImpl();
        }
        return instance;
    }

    private BotUserDao botUserDao = BotUserDaoImpl.getInstance();

    @Override
    public void addUser(Integer id, BotUser botUser) {
        botUserDao.addBotUser(id,botUser);

    }

    @Override
    public BotUser getUser(Integer id) {
        return botUserDao.getBotUserById(id);
    }

    @Override
    public boolean isUserExistById(Integer id){
    return botUserDao.getBotUserById(id) != null;
    }


}

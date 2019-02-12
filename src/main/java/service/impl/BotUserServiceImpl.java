package service.impl;

import dao.abstr.BotUserDao;
import dao.impl.BotUserDaoImpl;
import model.BotUser;
import service.abstr.BotUserService;

public class BotUserServiceImpl implements BotUserService {


	private BotUserServiceImpl() {}
	private static BotUserServiceImpl instance;
	public static BotUserServiceImpl getInstance(){
		if (instance == null) {
			instance = new BotUserServiceImpl();
		}
		return instance;
	}

	private BotUserDao botUserDao = BotUserDaoImpl.getInstance();


	@Override
	public void addUser(BotUser botUser, Integer id) {
	  	botUserDao.addUser(botUser, id);
	}

	@Override
	public BotUser getUser(Integer id) {
		return botUserDao.getBotUserById(id);
	}


	@Override
	public boolean isUserExistById(Integer id) {
		if(botUserDao.getBotUserById(id)!= null ){
			return true;
		}
		return false;
	}

	@Override
	public Integer getUserContextById(BotUser botUser) {
		return botUser.getUserId();
	}

}

package storage;

import model.BotUser;
import model.Project;

import java.util.*;

public class Storage {

	public static Map<Integer, BotUser> USERS_TABLE = new HashMap<Integer, BotUser>();
	public static Map<Integer, Project> PROJECTS_TABLE = new HashMap<Integer, Project>();
	public static int PROJECT_ID = 1;


}

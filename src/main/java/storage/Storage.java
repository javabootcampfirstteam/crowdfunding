package storage;

import model.BotUser;
import model.Project;

import java.util.*;

public class Storage {

	public static final Map<Integer, BotUser> USERS_TABLE = new HashMap<Integer, BotUser>();
	public static final Map<Integer, Project> PROJECTS_TABLE = new HashMap<Integer, Project>();
	public static final ArrayList<String> COMMAND_LIST = new ArrayList<String>(Arrays.asList("/help", "/projects", "/menu", "/pay", "addProject"));
	public List<String> USER_CONTEXT = new ArrayList<>();

}

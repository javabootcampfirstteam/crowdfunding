package storage;

import model.BotUser;

import java.util.*;

public class Storage {

    public static Map<Integer, BotUser> USERS_TABLE = new HashMap<>();
    public static final ArrayList<String> COMMAND_LIST = new ArrayList<>(Arrays.asList("/help", "/projects",
            "/menu", "/pay", "addProject", "/reg", "/regCity", "/regPhone", "/final"));
    public List<String> USER_CONTEXT = new ArrayList<>();
}
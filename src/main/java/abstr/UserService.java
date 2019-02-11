package abstr;

public interface UserService {

	void addUser(String name, Integer id);
	void getUser(Integer id);
	void isUserExistById (Integer id);
}

package dao.impl;
//а все вместе это такой способ проектирования приложений: Паттерн Синглтон.
public class UserDaoImpl {
        private UserDaoImpl() {}
        private static UserDaoImpl instance;
        public static UserDaoImpl getInstance(){
            if (instance == null) {
                instance = new UserDaoImpl();
            }
            return instance;
        }
}

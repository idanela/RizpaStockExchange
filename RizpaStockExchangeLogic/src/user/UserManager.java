package user;

import stockExchangeEngine.StockExchangeEngine;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {

        private final Set<User> usersSet;

        public UserManager() {
            usersSet = new HashSet<>();
        }

        public synchronized void addUser(String username, boolean isAdmin, StockExchangeEngine engine) {
           User user = new User(username,isAdmin);
            usersSet.add(user);
            engine.addUser(user);
        }

        public synchronized void removeUser(String username) {
            usersSet.remove(username);
        }

        public synchronized Set<User> getUsers() {
            return Collections.unmodifiableSet(usersSet);
        }

        public boolean isUserExists(String username) {
            User userToCheck = new User(username,false);
            return usersSet.contains(userToCheck);
            //todo: check if contains works on complex object
        }
}


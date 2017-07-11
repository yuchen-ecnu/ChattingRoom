package server;

import service.UserService;

public interface BaseService {
	UserService userService = new UserService();
}

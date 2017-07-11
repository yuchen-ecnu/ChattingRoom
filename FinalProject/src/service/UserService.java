package service;

import java.sql.ResultSet;
import java.sql.SQLException;

import dao.DBHelper;

public class UserService {
	private static DBHelper db1;

	/**
	 * 登录检测
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkUser(String username, String password) {
		String sql = "select *from user where username='" + username + "';";
		db1 = new DBHelper(sql);

		try {
			ResultSet ret = db1.pst.executeQuery();

			// 计算resultset的size
			ret.last();
			int rowCount = ret.getRow();
			ret.beforeFirst();
			if (rowCount > 0) {
				while (ret.next()) {
					if (ret.getString("password").equals(password)) {
						return true;
					}
				}
			}

			ret.close();
			db1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean register(String username, String password) {
		String sql = "select *from user where username='" + username + "';";
		db1 = new DBHelper(sql);
		try {
			ResultSet ret = db1.pst.executeQuery();

			// 计算resultset的size
			ret.last();
			int rowCount = ret.getRow();
			ret.beforeFirst();
			if (rowCount == 0) {
				sql="INSERT INTO user (username,password) VALUES ('"+username+"','"+password+"');";
				System.out.println(sql);
				db1 = new DBHelper(sql);
				db1.pst.executeUpdate();
				return true;
			}

			ret.close();
			db1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}

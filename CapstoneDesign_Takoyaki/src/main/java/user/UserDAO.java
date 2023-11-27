package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public UserDAO() {
		try {
			String dbURL = "AWS_RDS_ADDRESS";
			String dbID = "DBID";
			String dbPW = "DBPW";
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection(dbURL, dbID, dbPW);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int login(String id, String pw) {
		String SQL = "SELECT pw FROM user_tb WHERE ID = ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals(pw)) {
					return 1; // �α��� ����
				} else
					return 0; // password ����ġ
			}
			return -1; // ���̵� ����.
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2; // DB ����
	}

	public int join(UserDTO user) {
		String SQL = "INSERT INTO user_tb VALUES(?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getId());
			pstmt.setString(2, user.getPw());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getBirth());
			pstmt.setString(5, user.getMobile());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int registerCheck(String id) {
		pstmt = null;
		rs = null;
		String SQL = "SELECT * FROM user_tb WHERE ID = ?";
		
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next() || id.equals("")) {
				return 0; 	// �̹� �����ϴ� ȸ��
			} else {
				return 1;	// ���� ������ ���̵�
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1;	//DB ����
	}

}
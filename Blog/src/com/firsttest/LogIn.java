package com.firsttest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static PreparedStatement psmt;
    private static Connection connection;
    private static Statement statement;
//    private static ResultSet rs;
    String password="";
    static final String DB_URL=
//    		"jdbc:mysql://localhost:3306/boke?characterEncoding=gbk && serverTimezone=UTC";
    		"jdbc:mysql://192.168.121.134:3306/boke?useSSL=false && characterEncoding=utf8 && serverTimezone=UTC";
    static final String USER="root";
    static final String PASS="123456";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogIn() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @throws ClassNotFoundException
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
//		response.setCharacterEncoding("UTF-8");
		String id=request.getParameter("id");
//		System.out.println("id="+id);
		if(id.equals("1")) {
			try {
				logIn(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(id.equals("2")) {
			try {
				insertUser(request, response);
			} catch (ClassNotFoundException | ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(id.equals("3")) {
			try {
				updateUser(request, response);
			} catch (ClassNotFoundException | ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

//    ////////////////////////////
//  登录
  protected void logIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // TODO Auto-generated method stub
      request.setCharacterEncoding("utf-8");
      String username = request.getParameter("username");
      String password = request.getParameter("password");
      try {
          //
          Class.forName("com.mysql.jdbc.Driver");
          //
          connection = DriverManager.getConnection
                  (DB_URL, USER, PASS);
          //
          statement = connection.createStatement();
          String sql="select * from users where user_id='"+username+"'";
          ResultSet resultSet = statement.executeQuery(sql);
          String n;
          if(resultSet.next()) {
              if((resultSet.getString("user_id")).equals(username)&&(resultSet.getString("user_password")).equals(String.valueOf(password))) {
//                  System.out.println("写入："+username);

//            	  USER_ID.setUSER_ID("");
                  LogID.setUSER_ID(username);
//            	  System.out.println("输入："+LogID.getUSER_ID());
                  n="0";
                  response.getWriter().write(n);
              }
              else {
                  n="1";
                  response.getWriter().write(n);
              }
          }
          else {
              n="2";
              response.getWriter().write(n);
          }
          resultSet.close();
          statement.close();
          connection.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
//
    public static boolean selectUserID(String s) throws ClassNotFoundException  {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            //
            connection = DriverManager.getConnection
                    (DB_URL, USER, PASS);
            //
            statement = connection.createStatement();//
            String sql="select user_id,user_password from users";
            ResultSet rs = statement.executeQuery(sql);//
            while(rs.next()) {
                if(rs.getString(1).equals(s)){
                    closeMySQl(connection, psmt);
                    return true;
                }
            }
            closeMySQl(connection, psmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //注册
    public static void  insertUser(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException, ClassNotFoundException{
		
		String userID=request.getParameter("userID");
		if(!LogIn.selectUserID(userID)) {
			UserClass user=new UserClass();
			user.setUser_id(request.getParameter("userID"));
			user.setUer_name(request.getParameter("set_username"));
			user.setUser_password(request.getParameter("set_passwd"));
			user.setUser_email(request.getParameter("email"));
			user.setUser_telephone_number(request.getParameter("telephone"));
			LogIn.insetLine(user);
			response.getWriter().write("1");
		}else {
			response.getWriter().write("0");
		}
	}
    public static boolean insetLine(UserClass user) throws ClassNotFoundException  {
        int m = -1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection
                    (DB_URL, USER, PASS);
            statement = connection.createStatement();
            String sql="insert into users(user_id,user_name,user_password,user_email,user_registration_time,user_telephone_number) values(?,?,?,?,?,?)";
            psmt=connection.prepareStatement(sql);
            
            psmt.setString(1, user.getUser_id());
            psmt.setString(2, user.getUer_name());
            psmt.setString(3, user.getUser_password());
            psmt.setString(4, user.getUser_email());
            psmt.setTimestamp(5, user.getUser_registration_time());
            psmt.setString(6, user.getUser_telephone_number());
            m= psmt.executeUpdate();
            closeMySQl(connection, psmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(m!=0)
            return true;
        else
            return false;
    }
    // 更新
	private static void  updateUser (HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException, ClassNotFoundException{
//		System.out.println("111:"+LogID.getUSER_ID());
		String userID=request.getParameter("userID");
		UserClass user=new UserClass();
		user.setUser_id(request.getParameter("userID"));
		user.setUer_name(request.getParameter("set_username"));
		user.setUser_password(request.getParameter("set_passwd"));
		user.setUser_email(request.getParameter("email"));
		user.setUser_telephone_number(request.getParameter("telephone"));
//		System.out.println(user.toString());
		if(LogIn.selectUserID(userID)) {
			LogIn.updateLine(user);
			response.getWriter().write("1");
		}else {
			response.getWriter().write("0");
		}
	}
    public static void updateLine(UserClass user) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection
                    (DB_URL, USER, PASS);
            statement = connection.createStatement();
            String sql="update users set user_name=?,user_password=?,user_email=?,user_registration_time=?,user_telephone_number=? where user_ID='"+user.getUser_id()+"'";
//            System.out.println(sql);
            psmt=connection.prepareStatement(sql);
            psmt.setString(1, user.getUer_name());
            psmt.setString(2, user.getUser_password());
            psmt.setString(3, user.getUser_email());
            psmt.setTimestamp(4, user.getUser_registration_time());
            psmt.setString(5, user.getUser_telephone_number());
            psmt.executeUpdate();
            closeMySQl(connection, psmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void closeMySQl(Connection c,PreparedStatement s)  {
        try {
            if(c!=null)
                c.close();
            if(s!=null)
                s.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}

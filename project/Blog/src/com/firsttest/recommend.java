package com.firsttest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

//@WebServlet("/recommend")
public class recommend extends HttpServlet {
    private static final long serialVersionUID = 1L;
//    private static PreparedStatement psmt;
    private static Connection connection;
    private static Statement statement;
//    private static ResultSet rs;
    static final String DB_URL=
//    		"jdbc:mysql://localhost:3306/boke?characterEncoding=gbk && serverTimezone=UTC";
    		"jdbc:mysql://192.168.121.134:3306/boke?useSSL=false && characterEncoding=utf8 && serverTimezone=UTC";
    static final String USER="root";
    static final String PASS="123456";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public recommend() {
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
		String id=request.getParameter("id");
//		System.out.println("id="+id);
		if(id.equals("1")) {
			try {
				Header(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(id.equals("2")) {
			try {
				Hot(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(id.equals("3")) {
			try {
				isLogin(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    protected void isLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
//        // TODO Auto-generated method stub
////    	System.out.println(LogID.getUSER_ID());
        response.setCharacterEncoding("UTF-8");
        String data="";
        if(LogID.getUSER_ID().equals("")) {
        	data="1";
        }else {
        	data="2";
        }
    	response.setCharacterEncoding("UTF-8");
    	response.getWriter().write(data); 
    }
    protected void Header(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("utf-8");
//        System.out.println("aaaaaaaaaaaaaaaaaaaa");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection
                    (DB_URL, USER, PASS);
            statement = connection.createStatement();
            String text_class = request.getParameter("text_class1");
            
            //首页
            if (text_class == null){
            	String sql="select text_id,text_title,writer_id,text_views,text_date,text_content,text_good,text_save,text_comment,text_class from text ORDER BY text_date DESC limit 10";
//            	System.out.println(sql);
            	ResultSet resultSet = statement.executeQuery(sql);
            	String n="";
            	while (resultSet.next()) {
            		n+=resultSet.getString("text_id")+"$$$"+resultSet.getString("text_title")+"$$$"+
              			resultSet.getString("writer_id")+"$$$"+resultSet.getString("text_date")+"$$$"+
              			resultSet.getString("text_class")+"$$$"+resultSet.getString("text_content")+"$$$"+
              			resultSet.getString("text_views")+"$$$"+resultSet.getString("text_good")+"$$$"+
              			resultSet.getString("text_comment")+"$$$"+resultSet.getString("text_save")+"$$$$";
                    //跳转网页                
            	}
            	n+=LogID.getUSER_ID();
//            System.out.println(n);
            	response.setCharacterEncoding("UTF-8");
            	response.getWriter().write(n);  
            	resultSet.close();
            	statement.close();
            	connection.close();
            }
            //分类
            else {
//            	System.out.println("class:"+text_class);
            	String sql="select text_id,text_title,writer_id,text_views,text_date,text_content,text_good,text_save,text_comment,text_class,0.01*text_views+0.2*text_good+0.5*text_comment+0.7*text_save score from text where text_class=\""+text_class+"\" ORDER BY score desc limit 5";
//            System.out.println(sql);
            	ResultSet resultSet = statement.executeQuery(sql);
            	String n="";
            	while (resultSet.next()) {
            		n+=resultSet.getString("text_id")+"$$$"+resultSet.getString("text_title")+"$$$"+
              			resultSet.getString("writer_id")+"$$$"+resultSet.getString("text_date")+"$$$"+
              			resultSet.getString("text_class")+"$$$"+resultSet.getString("text_content")+"$$$"+
              			resultSet.getString("text_views")+"$$$"+resultSet.getString("text_good")+"$$$"+
              			resultSet.getString("text_comment")+"$$$"+resultSet.getString("text_save")+"$$$$";
                    //跳转网页              
            	}
            	response.setCharacterEncoding("UTF-8");
            	response.getWriter().write(n);  
            	resultSet.close();
            	statement.close();
            	connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
          }
    }

    protected void Hot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("utf-8");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection
                    (DB_URL, USER, PASS);
            statement = connection.createStatement();
            String text_id = request.getParameter("text_id");
            
            //热度
            if (text_id == null){
            	String sql="select text_id,text_title,writer_id,text_views,text_date,text_content,text_good,text_save,text_comment,text_class,0.01*text_views+0.2*text_good+0.5*text_comment+0.7*text_save score from text ORDER BY score desc limit 10";
//            	System.out.println(sql);
            	ResultSet resultSet = statement.executeQuery(sql);
            	String n="";
            	while (resultSet.next()) {
            		n+=resultSet.getString("text_id")+"$$$"+resultSet.getString("text_title")+"$$$"+
              			resultSet.getString("writer_id")+"$$$"+resultSet.getString("text_date")+"$$$"+
              			resultSet.getString("text_class")+"$$$"+resultSet.getString("text_content")+"$$$"+
              			resultSet.getString("text_views")+"$$$"+resultSet.getString("text_good")+"$$$"+
              			resultSet.getString("text_comment")+"$$$"+resultSet.getString("text_save")+"$$$$";
                    //跳转网页                
            	}
//            	System.out.println(n);
            	response.setCharacterEncoding("UTF-8");
            	response.getWriter().write(n);  
            	resultSet.close();
            	statement.close();
            	connection.close();
            }
            //展示博客全文
            else {
            System.out.println(text_id);
                
                //浏览量+1
                String sql1="update text set text_views=text_views+1 where text_id="+text_id;
                statement.execute(sql1);
                
            	String sql="select text_id,text_title,writer_id,text_views,text_date,text_content,text_good,text_save,text_comment,text_class from text where text_id=\""+text_id+"\"";
//            System.out.println(sql);
            	ResultSet resultSet = statement.executeQuery(sql);
            	String n="";
            	resultSet.next();
            	n+=resultSet.getString("text_id")+"$$$"+resultSet.getString("text_title")+"$$$"+
              			resultSet.getString("writer_id")+"$$$"+resultSet.getString("text_date")+"$$$"+
              			resultSet.getString("text_class")+"$$$"+resultSet.getString("text_content")+"$$$"+
              			resultSet.getString("text_views")+"$$$"+resultSet.getString("text_good")+"$$$"+
              			resultSet.getString("text_comment")+"$$$"+resultSet.getString("text_save")+"$$$$";
                    //跳转网页              
//            System.out.println(n);
            	response.setCharacterEncoding("UTF-8");
            	response.getWriter().write(n);  
            	resultSet.close();
            	statement.close();
            	connection.close();
            }
        } catch (Exception e) {
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

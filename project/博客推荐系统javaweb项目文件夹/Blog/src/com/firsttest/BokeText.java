package com.firsttest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/BokeText")
public class BokeText extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection connection;
	private static Statement statement;
    private static PreparedStatement psmt;
    static final String DB_URL=
//    		"jdbc:mysql://localhost:3306/boke?characterEncoding=gbk && serverTimezone=UTC";
    		"jdbc:mysql://192.168.121.134:3306/boke?useSSL=false && characterEncoding=utf8 && serverTimezone=UTC";
    static final String USER="root";
    static final String PASS="123456";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BokeText() {
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
		if(id.equals("4")) {
			try {
				bokeText(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(id.equals("5")) {
			try {
				saveText(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(id.equals("6")) {
			try {
				allText(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(id.equals("7")) {
			try {
				writeText(request, response);
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(id.equals("8")) {
			try {
				deleteText(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(id.equals("9")) {
			try {
				recommendText(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(id.equals("10")) {
			try {
				goodSave(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(id.equals("11")) {
			try {
				deleteSave(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
//  删除收藏的博文
  protected void deleteSave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // TODO Auto-generated method stub
	  String text_ID = request.getParameter("text_ID");
      request.setCharacterEncoding("utf-8");
      try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          connection = DriverManager.getConnection
          		(DB_URL, USER, PASS);
          statement = connection.createStatement();
      	//收藏-1
          String sql1="update text set text_save=text_save-1 where text_id="+text_ID;
          statement.execute(sql1);
////          System.out.println(LogID.getUSER_ID());
          String sql="delete from behavior where user_id="+LogID.getUSER_ID()+" and text_id="+text_ID;
////          System.out.println(sql);
          int resultSet = statement.executeUpdate(sql);
          response.getWriter().write(Integer.toString(resultSet));
          statement.close();
          connection.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
    //点赞、收藏
    protected void goodSave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("utf-8");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection
            		(DB_URL, USER, PASS);
            statement = connection.createStatement();
            String text_id=request.getParameter("text_id");
            String act=request.getParameter("act");

//        	System.out.println(act.equals("2"));
            //act为"1"，点赞；act为"2"，收藏。
            if(act.equals("1")) {
                //点赞+1
                String sql1="update text set text_good=text_good+1 where text_id="+text_id;
                statement.execute(sql1);
                if(!LogID.getUSER_ID().equals("")) {
                	String sql2="select * from behavior where user_id="+LogID.getUSER_ID()+" and text_id="+text_id;
                    ResultSet rs = statement.executeQuery(sql2);
                    if(rs.next()) {
                        String sql3="update behavior set text_good=1 where user_id="+LogID.getUSER_ID()+" and text_id="+text_id;
                        statement.execute(sql3);
                    }else {
                        String sql3="insert into behavior(user_id,text_id,text_good) values(?,?,?)";
            	        psmt=connection.prepareStatement(sql3);
            			psmt.setString(1, LogID.getUSER_ID());
            			psmt.setString(2, text_id);
            			psmt.setString(3, "1");
            			psmt.executeUpdate();
                    }
                } 
                response.getWriter().write("1");
            }else if(act.equals("2")) {
                if(!LogID.getUSER_ID().equals("")) {
                	
                	//收藏+1
                    String sql1="update text set text_save=text_save+1 where text_id="+text_id;
                    statement.execute(sql1);
                    
                	String sql2="select * from behavior where user_id="+LogID.getUSER_ID()+" and text_id="+text_id;
//                	System.out.println("用户："+sql2);
                    ResultSet rs = statement.executeQuery(sql2);
                    if(rs.next()) {
                        String sql3="update behavior set text_save=1 where user_id="+LogID.getUSER_ID()+" and text_id="+text_id;
                        statement.execute(sql3);
                    }else {
//                    	System.out.println("到这了："+act);
                        String sql3="insert into behavior(user_id,text_id,text_save) values(?,?,?)";
            	        psmt=connection.prepareStatement(sql3);
            			psmt.setString(1, LogID.getUSER_ID());
            			psmt.setString(2, text_id);
            			psmt.setString(3, "1");
            			psmt.executeUpdate();
                    }
                    response.getWriter().write("2");
                }else {
                	response.getWriter().write("3");
                }
                
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  //推荐
    protected void recommendText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("utf-8");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection
            		(DB_URL, USER, PASS);
            statement = connection.createStatement();
//            System.out.println(LogID.getUSER_ID());
            String sql="select * from text where text.text_id in (select item_cf.text_id from item_cf where item_cf.user_id="+LogID.getUSER_ID()+") and " + 
            		"text.text_id not in (select text_id from behavior where user_id="+LogID.getUSER_ID()+") ORDER BY text_views DESC";
//            System.out.println(sql);
            ResultSet resultSet = statement.executeQuery(sql);
            String data = "";
            while (resultSet.next()) {
              	data+=resultSet.getString("text_id")+"$$$"+resultSet.getString("text_title")+"$$$"+resultSet.getString("writer_id")+"$$$"+resultSet.getString("text_date")+"$$$"+resultSet.getString("text_class")+"$$$"+resultSet.getString("text_content")+"$$$"+resultSet.getString("text_views")+"$$$"+resultSet.getString("text_good")+"$$$"+resultSet.getString("text_comment")+"$$$"+resultSet.getString("text_save")+"$$$$";
             }
//            System.out.println(data);
    		response.setCharacterEncoding("UTF-8");
            response.getWriter().write(data);
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  删除博文
  protected void deleteText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // TODO Auto-generated method stub
	  String text_ID = request.getParameter("text_ID");
      request.setCharacterEncoding("utf-8");
      try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          connection = DriverManager.getConnection
          		(DB_URL, USER, PASS);
          statement = connection.createStatement();
////          System.out.println(LogID.getUSER_ID());
          String sql="delete from text where text_id="+text_ID;
////          System.out.println(sql);
          int resultSet = statement.executeUpdate(sql);
          response.getWriter().write(Integer.toString(resultSet));
          statement.close();
          connection.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

    //发表文章
    public static void  writeText(HttpServletRequest request,HttpServletResponse response)throws SQLException, ClassNotFoundException, IOException{
		
		String boketitle = request.getParameter("boketitle");
		String bokeclass = request.getParameter("bokeclass");
		String text_content = request.getParameter("boketext");
		if(boketitle!=""&&text_content!="") {
			Date date = new Date();//获得系统时间.
			SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
			String nowTime = sdf.format(date);
			Timestamp text_date =Timestamp.valueOf(nowTime);//把时间转换
			
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection
	                (DB_URL, USER, PASS);
	        statement = connection.createStatement();//
	        //计算现有的文章数量
	        String sql1="select count(text_id) from text";
	        ResultSet resultSet1 = statement.executeQuery(sql1);
	        String text_ID = "";
	        while(resultSet1.next()) {
	        	int num = Integer.parseInt(resultSet1.getString(1))+1;
	        	text_ID = Integer.toString(num);
//	        	System.out.println(text_ID);
	        }
	        ///////
//	        System.out.println("class:::::"+bokeclass);
	        ///////
	        String sql="insert into text(text_id,text_title,writer_id,text_date,text_class,text_content) values(?,?,?,?,?,?)";
	        psmt=connection.prepareStatement(sql);
			psmt.setString(1, text_ID);
			psmt.setString(2, boketitle);
//			System.out.println("作者ID:"+LogID.getUSER_ID());
			psmt.setString(3, LogID.getUSER_ID());
			psmt.setTimestamp(4, text_date);
			psmt.setString(5, bokeclass);
			psmt.setString(6, text_content);
			psmt.executeUpdate();
			response.getWriter().write("1");
		}
		else {
			response.getWriter().write("0");
		}

	}

//  返回博客ID和title
  protected void bokeText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // TODO Auto-generated method stub
      request.setCharacterEncoding("utf-8");
      try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          connection = DriverManager.getConnection
          		(DB_URL, USER, PASS);
          statement = connection.createStatement();       
          String sql="select * from text where writer_id="+LogID.getUSER_ID();
          ResultSet resultSet = statement.executeQuery(sql);
          String n = "";
          while (resultSet.next()) {
          	n+=resultSet.getString("text_id")+"$$$"+resultSet.getString("text_title")+"$$$"+resultSet.getString("writer_id")+"$$$"+resultSet.getString("text_date")+"$$$"+resultSet.getString("text_class")+"$$$"+resultSet.getString("text_content")+"$$$"+resultSet.getString("text_views")+"$$$"+resultSet.getString("text_good")+"$$$"+resultSet.getString("text_comment")+"$$$"+resultSet.getString("text_save")+"$$$$";
          }
          
//          System.out.println(n);
  		  response.setCharacterEncoding("UTF-8");
          response.getWriter().write(n);
          resultSet.close();
          statement.close();
          connection.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
  
//收藏
protected void saveText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    request.setCharacterEncoding("utf-8");
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection
        		(DB_URL, USER, PASS);
        statement = connection.createStatement();
//        System.out.println(LogID.getUSER_ID());
        String sql="select * from text where text.text_id in (select text_id from behavior where text_save=1 and user_id="+LogID.getUSER_ID()+")";
//        System.out.println(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        String data = "";
        while (resultSet.next()) {
          	data+=resultSet.getString("text_id")+"$$$"+resultSet.getString("text_title")+"$$$"+
          			resultSet.getString("writer_id")+"$$$"+resultSet.getString("text_date")+"$$$"+
          			resultSet.getString("text_class")+"$$$"+resultSet.getString("text_content")+"$$$"+
          			resultSet.getString("text_views")+"$$$"+resultSet.getString("text_good")+"$$$"+
          			resultSet.getString("text_comment")+"$$$"+resultSet.getString("text_save")+"$$$$";
         }
//        System.out.println(data);
		response.setCharacterEncoding("UTF-8");
        response.getWriter().write(data);
        resultSet.close();
        statement.close();
        connection.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
  //完整文章
protected void allText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    request.setCharacterEncoding("utf-8");
    String text_id=request.getParameter("text_id");
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection
        		(DB_URL, USER, PASS);
        statement = connection.createStatement();
        
        //浏览量+1
        String sql1="update text set text_views=text_views+1 where text_id="+text_id;
        statement.execute(sql1);
        
        String sql="select * from text where text_id="+text_id;
        ResultSet resultSet = statement.executeQuery(sql);
        String text = "";
        while (resultSet.next()) {
        	text=resultSet.getString("text_id")+"$$$"+resultSet.getString("text_title")+"$$$"+resultSet.getString("writer_id")+"$$$"+resultSet.getString("text_date")+"$$$"+resultSet.getString("text_class")+"$$$"+resultSet.getString("text_content")+"$$$"+resultSet.getString("text_views")+"$$$"+resultSet.getString("text_good")+"$$$"+resultSet.getString("text_comment")+"$$$"+resultSet.getString("text_save");
        }
//        System.out.println(text);
		response.setCharacterEncoding("UTF-8");
        response.getWriter().write(text);
        resultSet.close();
        statement.close();
        connection.close();
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

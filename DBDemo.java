import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.lang.*;

public class DBDemo {
	
	

	public static void ReportDemo () throws ClassNotFoundException, IOException {
		Class.forName ("com.mysql.jdbc.Driver");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		File file = new File("D:\\DatabaseProject\\JDBC\\Report.txt");
		FileWriter fstream = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fstream);
        // out.write("CRN" + "\t" + "Sub" + "\t" + "Num" + "\t" + "Sec" + "\t" + "Title" + "\t" + "NumStu" + "\t" + "N" + "\t" + "Note");out.newLine();
        int count = 0;
        try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Registration", "root", "root");
			
			stmt = conn.prepareStatement("SELECT CRN, Sub, Num, Sec, Title, NumStu, N, Note FROM Sections NATURAL LEFT OUTER JOIN Sectionof NATURAL LEFT OUTER JOIN SelectedCRN NATURAL LEFT OUTER JOIN Notes WHERE NumStu IS NOT NULL");
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				/*
				int a = rs.getInt(1);
				String b = rs.getString(2);
				int c = rs.getInt(3);
				int d = rs.getInt(4);
				String e = rs.getString(5);
				int f = rs.getInt(6);
				*/
				// String h = rs.getString(8);
				/*
				if(h == null){
					String g = rs.getString(7);
				} else {
					int g = rs.getInt(7);
				}
				*/
				
				int g = rs.getInt(7);
				
				// System.out.println (a + "\t" + b + "\t" + c + "\t" + d + "\t" + e + "\t" + f + "\t" + g + "\t" + h);
				
				
				
				if(count % 15 == 0){
					
					out.write(String.format("%1$-10s %2$d", "Page Number: ", count / 15 + 1));out.newLine();out.newLine();
					out.write(String.format("%1$-8s %2$-8s %3$-8s %4$-5s %5$-35s %6$-12s %7$-70s", "CRN", "Dept", "Course#", "Sec", "CourseTitle", "#ofStudents", "Notes"));out.newLine();
					if(g == 1 || g == 0){
		            	out.write(String.format("%1$-8s %2$-8s %3$-8s %4$-5s %5$-35s %6$-12s %7$-70s", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(8)));
			            /*
			            out.write(rs.getString(3));out.write("\t");
			            out.write(rs.getString(4));out.write("\t");
			            out.write(rs.getString(5));out.write("\t");
			            out.write(rs.getString(6));out.write("\t");
			            out.write(rs.getString(7));out.write("\t");
			            out.write(rs.getString(8));out.write("\t");
			            */
			            out.newLine();
		            } else{
		            	/*
		            	out.write("\t");
			            out.write("\t");
			            out.write("\t");
			            out.write("\t");
			            out.write("\t");
			            out.write("\t");
			            out.write(String.format("%1$-10s", rs.getString(8)));
			            */
		            	out.write(String.format("%1$-8s %2$-8s %3$-8s %4$-5s %5$-35s %6$-12s %7$-70s", " ", " ", " ", " ", " ", " ", rs.getString(8)));
			            out.newLine();
			            // System.out.println(g);
		            }
				} else {
					
		            if(g == 1|| g == 0){
		            	out.write(String.format("%1$-8s %2$-8s %3$-8s %4$-5s %5$-35s %6$-12s %7$-70s", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(8)));
			            /*
			            out.write(rs.getString(3));out.write("\t");
			            out.write(rs.getString(4));out.write("\t");
			            out.write(rs.getString(5));out.write("\t");
			            out.write(rs.getString(6));out.write("\t");
			            out.write(rs.getString(7));out.write("\t");
			            out.write(rs.getString(8));out.write("\t");
			            */
			            out.newLine();
		            } else {
		            	/*
		            	out.write("\t");
			            out.write("\t");
			            out.write("\t");
			            out.write("\t");
			            out.write("\t");
			            out.write("\t");
			            out.write(String.format("%1$-10s", rs.getString(8)));
			            */
		            	out.write(String.format("%1$-8s %2$-8s %3$-8s %4$-5s %5$-35s %6$-12s %7$-70s", " ", " ", " ", " ", " ", " ", rs.getString(8)));
			            out.newLine();
		            }
		            if(count % 15 == 14){
		            	out.newLine();out.write("\f");
		            }
				}
				
				
				count = count+1;
	            
	            
	            
			}
			out.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			int errcode = e.getErrorCode(); // possibly handle?
			String state = e.getSQLState();
			String errmsg = e.getMessage();
			System.out.println ("Error code: " + errcode);
			System.out.println ("State:      " + state);
			System.out.println ("Message:    " + errmsg);
		}
		finally {
			if (rs != null) {
				try {
					rs.close ();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static void db_init () throws SQLException, ClassNotFoundException {
		Class.forName ("com.mysql.jdbc.Driver");
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/Registration", "root", "root");
		
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("DROP TABLE IF EXISTS SelectedCRN");
		stmt.executeUpdate("CREATE TABLE SelectedCRN (CRN INTEGER, NumStu INTEGER, PRIMARY KEY (CRN))");
		stmt.executeUpdate("INSERT INTO SelectedCRN(CRN, NumStu) SELECT CRN, COUNT(CRN) FROM Enroll GROUP BY CRN HAVING COUNT(*) < 10");
		
		conn.close();
	}

	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception, IOException {
		// TODO Auto-generated method stub
		
		db_init ();
		
		System.out.println ("ReportDemo");
		ReportDemo ();
	}
}

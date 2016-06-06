package uwyo.cs.uwreg.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import uwyo.cs.uwreg.dao.model.Course;
import uwyo.cs.uwreg.dao.model.Student;



public class UWregDAOImpl implements UWregDAO {
	
	private JdbcTemplate jdbcTemplate = null;
    
    public UWregDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    
        
	@Override
	public Student getStudentByWnumber(String wnumber) {
    	// TODO 3 (10 pts) - Get the record of student with given wnumber
		
		List<Student> Students = jdbcTemplate.query("SELECT * FROM registration.students WHERE WNum = ?", new Object[] {wnumber}, new RowMapper<Student>(){
			
			public Student mapRow(ResultSet RS, int rowNum) throws SQLException{
				String studentWNum = RS.getString("WNum");
				String studentLastName = RS.getString("Last_Name");
				String studentFirstName = RS.getString("First_Name");
				String studentGender = RS.getString("Gender");
				
				Student student = new Student(studentWNum, studentFirstName, studentLastName, studentGender);	 
				return student;
				
			}
		});
		return Students.get(0);
	
    }

	@Override
	public List<Course> getCoursesRegistered(String wnumber) {
    	// TODO 4 (10 pts) - Get the courses the given student is registered for
	    List<Course> Courses = jdbcTemplate.query("SELECT * FROM registration.enroll NATURAL LEFT OUTER JOIN registration.sections NATURAL LEFT OUTER JOIN registration.sectionof NATURAL LEFT OUTER JOIN registration.teach NATURAL LEFT OUTER JOIN registration.notes NATURAL LEFT OUTER JOIN registration.schedules WHERE WNum = ?", new Object[] {wnumber}, new RowMapper<Course>(){
			
			public Course mapRow(ResultSet RS, int rowNum) throws SQLException{
				
				String coursecrn = RS.getString("CRN");
				String courseusp = RS.getString("USP");
				String coursesubject = RS.getString("Sub");
				String coursecnumber = RS.getString("Num");
				String coursesection = RS.getString("Sec");
				String coursetitle = RS.getString("Title");
				int coursecredits = RS.getInt("Credits");
				String coursedays = RS.getString("Day");
				String coursestart = RS.getString("Start");
				String coursestop = RS.getString("Stop");
				String coursebuilding = RS.getString("Bldg");
				String courseroom = RS.getString("Room");
				String courseinstructor = RS.getString("FName") + ", " + RS.getString("LName");
				String[] coursenotes = {RS.getString("Note")};
				
				Course course = new Course(coursecrn, courseusp, coursesubject, coursecnumber, coursesection, coursetitle, coursecredits, coursedays, coursestart, coursestop, coursebuilding, courseroom, courseinstructor, coursenotes);	 
				return course;
				
			}
		});
	    
	    
	    List<Course> CoursesKeep = new ArrayList<Course>();
	    
	    ArrayList<String> allCrns = new ArrayList<String>();
	    for (Course element : Courses) {
	    	String coursecrn = element.getCrn();
	    	allCrns.add(coursecrn);
	    }
	    
	    Set<String> unique = new HashSet<String>(allCrns);
	    
	    for (String element : unique) {
	    	CoursesKeep.add(getCoursesByCrn(element).get(0));
	    }
	    
	    return CoursesKeep;
	}

	@Override
	public List<Course> getCoursesByCrn(String crn) {
    	// TODO 5 (10 pts) - Get the course with the given crn, or an empty list if no such course exists
	    List<Course> Courses = jdbcTemplate.query("SELECT * FROM registration.sections NATURAL LEFT OUTER JOIN registration.sectionof NATURAL LEFT OUTER JOIN registration.teach NATURAL LEFT OUTER JOIN registration.notes NATURAL LEFT OUTER JOIN registration.schedules WHERE CRN = ?", new Object[] {crn}, new RowMapper<Course>(){
			
			public Course mapRow(ResultSet RS, int rowNum) throws SQLException{
				
				String coursecrn = RS.getString("CRN");
				String courseusp = RS.getString("USP");
				String coursesubject = RS.getString("Sub");
				String coursecnumber = RS.getString("Num");
				String coursesection = RS.getString("Sec");
				String coursetitle = RS.getString("Title");
				int coursecredits = RS.getInt("Credits");
				String coursedays = RS.getString("Day");
				String coursestart = RS.getString("Start");
				String coursestop = RS.getString("Stop");
				String coursebuilding = RS.getString("Bldg");
				String courseroom = RS.getString("Room");
				String courseinstructor = RS.getString("FName") + ", " + RS.getString("LName");
				String[] coursenotes = {RS.getString("Note")};
				
				Course course = new Course(coursecrn, courseusp, coursesubject, coursecnumber, coursesection, coursetitle, coursecredits, coursedays, coursestart, coursestop, coursebuilding, courseroom, courseinstructor, coursenotes);	 
				return course;
				
			}
			
			
		});
	    

	    String coursenotestemp = "";
	    List<Course> CoursesKeep = new ArrayList<Course>();
	    
	    for (Course element : Courses) {
	    	
	    	String coursecrn = element.getCrn();
			String courseusp = element.getUsp();
			String coursesubject = element.getSubject();
			String coursecnumber = element.getCnumber();
			String coursesection = element.getSection();
			String coursetitle = element.getTitle();
			int coursecredits = element.getCredits();
			String coursedays = element.getDays();
			String coursestart = element.getStart();
			String coursestop = element.getStop();
			String coursebuilding = element.getBuilding();
			String courseroom = element.getRoom();
			String courseinstructor = element.getInstructor();
			
			
			String[] coursenotes = element.getNotes();
			
			//System.out.print(coursenotes[0]);
			//System.out.print(coursenotes[0].toString());
			
			coursenotestemp = coursenotestemp + " " + coursenotes[0];
	    	
			
			
			String[] strArray = new String[] {coursenotestemp};
			//System.out.print(strArray);
			Course coursetemp = new Course(coursecrn, courseusp, coursesubject, coursecnumber, coursesection, coursetitle, coursecredits, coursedays, coursestart, coursestop, coursebuilding, courseroom, courseinstructor, strArray);
			
			
			CoursesKeep.add(coursetemp);
	    	
	    }
	    /*
	    Course CourseTemp = CoursesKeep.get(CoursesKeep.size() - 1);
	    CoursesKeep.clear();
	    CoursesKeep.add(CourseTemp);
	    */
	    //System.out.print(CoursesKeep.size());
	    CoursesKeep.subList(0,CoursesKeep.size()-1).clear();
	    
		return CoursesKeep;
	}
	
	@Override
	public List<Course> getCoursesByCnumber(String dept, String cnumber) {
    	// TODO 6 (10 pts) - Get the course with the given department and course number (e.g., COSC 4820)
		
		List<Course> Courses = null;

		
		//System.out.printf("%s,%s,%s\n", dept, cnumber, "first");
		
		
		if(!dept.isEmpty() &&  !cnumber.isEmpty()){
			//System.out.printf("%s,%s,%s\n", dept, cnumber, "second");
			Courses = jdbcTemplate.query("SELECT * FROM registration.sections NATURAL LEFT OUTER JOIN registration.sectionof NATURAL LEFT OUTER JOIN registration.teach NATURAL LEFT OUTER JOIN registration.notes NATURAL LEFT OUTER JOIN registration.schedules WHERE Sub = ? AND Num = ?", new Object[] {dept, cnumber}, new RowMapper<Course>(){
				
				public Course mapRow(ResultSet RS, int rowNum) throws SQLException{
					
					String coursecrn = RS.getString("CRN");
					String courseusp = RS.getString("USP");
					String coursesubject = RS.getString("Sub");
					String coursecnumber = RS.getString("Num");
					String coursesection = RS.getString("Sec");
					String coursetitle = RS.getString("Title");
					int coursecredits = RS.getInt("Credits");
					String coursedays = RS.getString("Day");
					String coursestart = RS.getString("Start");
					String coursestop = RS.getString("Stop");
					String coursebuilding = RS.getString("Bldg");
					String courseroom = RS.getString("Room");
					String courseinstructor = RS.getString("FName") + ", " + RS.getString("LName");
					String[] coursenotes = {RS.getString("Note")};
					
					Course course = new Course(coursecrn, courseusp, coursesubject, coursecnumber, coursesection, coursetitle, coursecredits, coursedays, coursestart, coursestop, coursebuilding, courseroom, courseinstructor, coursenotes);	 
					return course;
				}
			});
			
		} else if (!dept.isEmpty()){
			//System.out.printf("%s,%s,%s\n", dept, cnumber, "third");
			Courses = jdbcTemplate.query("SELECT * FROM registration.sections NATURAL LEFT OUTER JOIN registration.sectionof NATURAL LEFT OUTER JOIN registration.teach NATURAL LEFT OUTER JOIN registration.notes NATURAL LEFT OUTER JOIN registration.schedules WHERE Sub = ?", new Object[] {dept}, new RowMapper<Course>(){
				
				public Course mapRow(ResultSet RS, int rowNum) throws SQLException{
					
					String coursecrn = RS.getString("CRN");
					String courseusp = RS.getString("USP");
					String coursesubject = RS.getString("Sub");
					String coursecnumber = RS.getString("Num");
					String coursesection = RS.getString("Sec");
					String coursetitle = RS.getString("Title");
					int coursecredits = RS.getInt("Credits");
					String coursedays = RS.getString("Day");
					String coursestart = RS.getString("Start");
					String coursestop = RS.getString("Stop");
					String coursebuilding = RS.getString("Bldg");
					String courseroom = RS.getString("Room");
					String courseinstructor = RS.getString("FName") + ", " + RS.getString("LName");
					String[] coursenotes = {RS.getString("Note")};
					
					Course course = new Course(coursecrn, courseusp, coursesubject, coursecnumber, coursesection, coursetitle, coursecredits, coursedays, coursestart, coursestop, coursebuilding, courseroom, courseinstructor, coursenotes);	 
					return course;
				}
			});
		}else if (!cnumber.isEmpty()){
			//System.out.printf("%s,%s,%s\n", dept, cnumber, "fourth");
			Courses = jdbcTemplate.query("SELECT * FROM registration.sections NATURAL LEFT OUTER JOIN registration.sectionof NATURAL LEFT OUTER JOIN registration.teach NATURAL LEFT OUTER JOIN registration.notes NATURAL LEFT OUTER JOIN registration.schedules WHERE Num = ?", new Object[] {cnumber}, new RowMapper<Course>(){
				
				public Course mapRow(ResultSet RS, int rowNum) throws SQLException{
					
					String coursecrn = RS.getString("CRN");
					String courseusp = RS.getString("USP");
					String coursesubject = RS.getString("Sub");
					String coursecnumber = RS.getString("Num");
					String coursesection = RS.getString("Sec");
					String coursetitle = RS.getString("Title");
					int coursecredits = RS.getInt("Credits");
					String coursedays = RS.getString("Day");
					String coursestart = RS.getString("Start");
					String coursestop = RS.getString("Stop");
					String coursebuilding = RS.getString("Bldg");
					String courseroom = RS.getString("Room");
					String courseinstructor = RS.getString("FName") + ", " + RS.getString("LName");
					String[] coursenotes = {RS.getString("Note")};
					
					Course course = new Course(coursecrn, courseusp, coursesubject, coursecnumber, coursesection, coursetitle, coursecredits, coursedays, coursestart, coursestop, coursebuilding, courseroom, courseinstructor, coursenotes);	 
					return course;
				}
			});
		}
		
		List<Course> CoursesKeep = new ArrayList<Course>();
	    
	    ArrayList<String> allCrns = new ArrayList<String>();
	    for (Course element : Courses) {
	    	String coursecrn = element.getCrn();
	    	allCrns.add(coursecrn);
	    }
	    
	    Set<String> unique = new HashSet<String>(allCrns);
	    
	    for (String element : unique) {
	    	CoursesKeep.add(getCoursesByCrn(element).get(0));
	    }
	    
	    return CoursesKeep;
	    
	}
	
	@Override
	public List<Course> getCoursesByTitle(String title) {
    	// TODO 7 (10 pts) - Get the courses that partially match the given title
		//System.out.printf("%s", title);
		String sql = "SELECT * FROM registration.sections NATURAL LEFT OUTER JOIN registration.sectionof NATURAL LEFT OUTER JOIN registration.teach NATURAL LEFT OUTER JOIN registration.notes NATURAL LEFT OUTER JOIN registration.schedules WHERE Title LIKE ?";
		String TT = "%" + title.trim() + "%";
		
		List<Course> Courses = jdbcTemplate.query(sql, new Object[] {TT}, new RowMapper<Course>(){
			
			public Course mapRow(ResultSet RS, int rowNum) throws SQLException{
				
				String coursecrn = RS.getString("CRN");
				String courseusp = RS.getString("USP");
				String coursesubject = RS.getString("Sub");
				String coursecnumber = RS.getString("Num");
				String coursesection = RS.getString("Sec");
				String coursetitle = RS.getString("Title");
				int coursecredits = RS.getInt("Credits");
				String coursedays = RS.getString("Day");
				String coursestart = RS.getString("Start");
				String coursestop = RS.getString("Stop");
				String coursebuilding = RS.getString("Bldg");
				String courseroom = RS.getString("Room");
				String courseinstructor = RS.getString("FName") + ", " + RS.getString("LName");
				String[] coursenotes = {RS.getString("Note")};
				
				//System.out.printf("%s", coursecrn);
				Course course = new Course(coursecrn, courseusp, coursesubject, coursecnumber, coursesection, coursetitle, coursecredits, coursedays, coursestart, coursestop, coursebuilding, courseroom, courseinstructor, coursenotes);	 
				return course;
			}
		});
		List<Course> CoursesKeep = new ArrayList<Course>();
	    
	    ArrayList<String> allCrns = new ArrayList<String>();
	    for (Course element : Courses) {
	    	String coursecrn = element.getCrn();
	    	allCrns.add(coursecrn);
	    }
	    
	    Set<String> unique = new HashSet<String>(allCrns);
	    
	    for (String element : unique) {
	    	CoursesKeep.add(getCoursesByCrn(element).get(0));
	    }
	    
	    return CoursesKeep;
	}

	@Override
	public void addDropCourses(String wnumber, List<String> adds, List<String> drops) throws UWregDAOException {
    	// TODO 8 (20 pts) - add & drop the given courses (CRNs) from the user's schedule
		// 10 points: Add and drop the right courses 
    	// 10 points: Check that (new) schedule has at least 12 and at most 18 credit
    	//            hours, and that there are no time conflicts
		// If something goes wrong (e.g., schedule fails sanity checks), throw a UWregDAOException
		
		//System.out.print(adds);
		//System.out.printf("%s", drops);
		/*
		System.out.printf("%s", adds.isEmpty());
		System.out.println(adds.size());
		System.out.println(temp.isEmpty());
		System.out.println(temp.length());
		System.out.printf("%s", drops.isEmpty());
		*/
		String temp1 = adds.get(0);
		String temp2 = drops.get(0);
		
		List<Course> Courses = getCoursesRegistered(wnumber);
		
		int totalCredits = 0;
		List<String> Time = new ArrayList<String>();
		
		for (Course element : Courses) {
			int coursecredits = element.getCredits();
			totalCredits = totalCredits +  coursecredits;
			
			String coursedays = element.getDays();
			String coursestart = element.getStart();
			String coursestop = element.getStop();
			String schedule = coursedays+coursestart+coursestop;
			Time.add(schedule);
		}
		//System.out.println(totalCredits);
		//System.out.println(Time);
		
		if(!temp1.isEmpty()){
			for (String element : adds) {
			    Course tempcourse = getCoursesByCrn(element).get(0);
			    int coursecredits = tempcourse.getCredits();
			    totalCredits = totalCredits +  coursecredits;
			    
			    String coursedays = tempcourse.getDays();
				String coursestart = tempcourse.getStart();
				String coursestop = tempcourse.getStop();
				String schedule = coursedays+coursestart+coursestop;
				Time.add(schedule);
			}
		}
		
		//System.out.println(totalCredits);
		//System.out.println(Time);
		
		
		if(!temp2.isEmpty()){
			for (String element : drops) {
			    Course tempcourse = getCoursesByCrn(element).get(0);
			    int coursecredits = tempcourse.getCredits();
			    totalCredits = totalCredits -  coursecredits;
			    
			    String coursedays = tempcourse.getDays();
				String coursestart = tempcourse.getStart();
				String coursestop = tempcourse.getStop();
				String schedule = coursedays+coursestart+coursestop;
				for (Iterator<String> iter = Time.listIterator(); iter.hasNext(); ) {
				    String a = iter.next();
				    //System.out.println(a);
				    if (schedule.equals(a)) {
				        iter.remove();
				        //System.out.println(schedule);
				    }
				}
			}
		}
		
		//System.out.println(totalCredits);
		//System.out.println(Time);
		
		for (Iterator<String> iter = Time.listIterator(); iter.hasNext(); ) {
		    String a = iter.next();
		    //System.out.println(a);
		    if (a.equals("nullnullnull")) {
		        iter.remove();
		    }
		}
		
		Set<String> set = new HashSet<String>(Time);
		
		
		
		if(totalCredits >= 12 && totalCredits <= 18 && set.size() == Time.size())  {
			String sql1 = "INSERT INTO registration.enroll(WNum, CRN) VALUES (?,?)";
			String sql2 = "DELETE FROM registration.enroll WHERE WNum = ? AND CRN = ?";
			
			if(!temp1.isEmpty() && !temp2.isEmpty()){
				for (String element : adds) {
					jdbcTemplate.update(sql1, wnumber, element);
				}
				for (String element : drops) {
					jdbcTemplate.update(sql2, wnumber, element);
				}
			}else if(!temp1.isEmpty()){
				for (String element : adds) {
					jdbcTemplate.update(sql1, wnumber, element);
				}
			}else if(!temp2.isEmpty()){
				for (String element : drops) {
					jdbcTemplate.update(sql2, wnumber, element);
				}
			}
		} else throw new UWregDAOException("Not implemented");
		
		
		
		
	}

}

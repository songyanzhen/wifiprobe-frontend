package DAOinsert;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
		public static SessionFactory factory; 
		static { 
	        try {    
	            factory = new Configuration().configure()  
	  
	                    .buildSessionFactory();  
	  
	        } catch (Exception e) {  
	  
	            e.printStackTrace();  
	  
	        }  
	  
	    }  
	  
	    public static Session openSession(){  
	  
	        Session session=factory.openSession();  
	        return session;  
	  
	    }  
}

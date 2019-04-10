package frontend;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import DAOinsert.HibernateUtil;
import DAOinsert.Otherinfo;

public class hiberTest {
	public static void main(String []args)
	{
		Session session = null;
		
		try{
			session = HibernateUtil.factory.openSession();
			session.beginTransaction();
			String hql = "from Otherinfo oi  ORDER BY oi.time desc";
			Query query = session.createQuery(hql);
			query.setFirstResult(0);//开始从第几条开始取数据
	        query.setMaxResults(10);//取多少条数据
			
			List<Otherinfo> results = query.list();
			for(Otherinfo oi:results)
			{
				System.out.println(oi.getTime());
			}
			
			
			session.getTransaction().commit();
		}catch(Exception e)
		{
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
		if(session!=null){
			if(session.isOpen()){
				session.close();
			}
		}
		}
	}
}

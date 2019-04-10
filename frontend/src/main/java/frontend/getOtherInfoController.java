package frontend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class getOtherInfoController {
	static Connection conn;
	Queue<JSONObject> queue1 = new LinkedList<JSONObject>();
	Queue<JSONObject> queue2 = new LinkedList<JSONObject>();
	
	JSONObject gps = new JSONObject();
	
	@RequestMapping(value="/getgps", method=RequestMethod.GET)
	public @ResponseBody String getgps(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		
		String probeID=request.getParameter("probeID");
		String pos = null;
		if(probeID.equals("0"))
		{
			pos = gps.getString("1");
		}
		else if(probeID.equals("1"))
		{
			pos = gps.getString("2");
		}
		
		return pos;
	}
	
	@RequestMapping(value="/otherInfo", method=RequestMethod.POST)
	public String getOtherInfo(@RequestBody byte[]in,HttpServletRequest request,
			HttpServletResponse response)
	{
		String otherInfo = new String(in);
		JSONObject otherInfoJson = JSONObject.fromObject(otherInfo);
		
		if(otherInfoJson.containsKey("lat"))
		{
			JSONObject pos = new JSONObject();
			pos.put("lat", otherInfoJson.getString("lat"));
			pos.put("lon", otherInfoJson.getString("lon"));
			gps.put(otherInfoJson.getString("probeId"), pos.toString());
		}
		else
		{
			
			JSONObject pos = new JSONObject();
			pos.put("lat", "39.983499");
			pos.put("lon", "116.351208");
			gps.put(otherInfoJson.getString("probeId"), pos.toString());
		}
//		request.getSession().setAttribute("pos"+otherInfoJson.getString("probeId"), otherInfoJson.getString("lon")+","+otherInfoJson.getString("lat"));
//		request.getSession().setAttribute("lat", otherInfoJson.getString("lat"));
		
		
		
		
//		System.out.println(otherInfoJson);
		JSONObject frontsize = new JSONObject();
		double inrate = Double.parseDouble(otherInfoJson.get("insize").toString())/Double.parseDouble(otherInfoJson.get("size").toString());
		frontsize.put("size", otherInfoJson.get("size"));
		frontsize.put("insize", otherInfoJson.get("insize"));
		frontsize.put("inrate", inrate);
		frontsize.put("time", otherInfoJson.get("time"));
//		System.out.println(inrate);
		//浠ュ墠鐗堟湰鏄啓鍏ユ枃浠�
//		writeLog(frontsize.toString());
		
		//鐜板湪鐗堟湰鏄彃鍏ュ埌鏁版嵁搴�
//		otherInfoJson.put("inrate", inrate);
//		insertOtherInfo(otherInfoJson);
		
		System.out.println(frontsize.toString());
		Queue<JSONObject> queue = null;
		if((otherInfoJson.getString("probeId")).equals("1"))
		{
			queue = queue1;
		}
		else if((otherInfoJson.getString("probeId")).equals("2"))
		{
			queue = queue2;
		}
		
		
		if(queue.size()<10)
		{
			queue.add(frontsize);
		}
		else if(queue.size()==10)
		{
			queue.poll();
			queue.add(frontsize);
		}
		
		for(JSONObject i:queue){
			System.out.println(i.toString());
			
		}
		System.out.println("\n");
		return null;
	}
	
	@RequestMapping(value="/getFlow", method=RequestMethod.GET)
	public @ResponseBody String getFlow(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		
		String probeID=request.getParameter("probeID");
//		System.out.println(probeID);
//		ArrayList<String> json = readFile("/home/gyc3/otherInfo.json");
//		System.out.println(json.size());
//		if(json.size()<10)
//			return null;
//		int len = json.size();
//		ArrayList<String> subjson = new ArrayList<String>();
//		for(int i = len -10;i<len;i++)
//			subjson.add(json.get(i));
//		
//		JSONArray res = JSONArray.fromObject(subjson);
//		response.getWriter().print(res.toString());
		
		
//		JSONArray res = getFlow2();
//		System.out.println(res);
		Queue<JSONObject> queue = null;
		if(probeID.equals("0"))
		{
			queue = queue1;
		}
		else if(probeID.equals("1"))
		{
			queue = queue2;
		}
		JSONObject res1 = new JSONObject();//used to get data through sorted key(time)
		JSONArray res = new JSONArray();//result to return
		List<String> timeList = new ArrayList<String>();
		System.out.println(queue.toString());
		if(queue.size()<10)
		{
			return "";
		}
		else
		{
			for(JSONObject jo:queue)
			{
//				res.add(jo);
				res1.put(jo.getString("time"), jo.toString());
				timeList.add(jo.getString("time"));
			}
			Collections.sort(timeList);
			for(int i = 0;i<timeList.size();i++)
			{
				res.add(res1.getJSONObject(timeList.get(i)));
			}
			return res.toString();
		}
		
	}
	
//	public JSONArray getFlow2()
//	{
//		JSONArray ja = new JSONArray();
//		JSONObject jo = new JSONObject();
//		Session session = null;
//		try{
//			session = HibernateUtil.factory.openSession();
//			session.beginTransaction();
//			String hql = "from Otherinfo oi  ORDER BY oi.time desc";
//			Query query = session.createQuery(hql);
//			query.setFirstResult(0);//寮�濮嬩粠绗嚑鏉″紑濮嬪彇鏁版嵁
//	        query.setMaxResults(10);//鍙栧灏戞潯鏁版嵁
//			
//			List<Otherinfo> results = query.list();
//			for(Otherinfo oi:results)
//			{
//				jo.put("size", oi.getSize());
//				jo.put("insize", oi.getInsize());
//				jo.put("inrate", oi.getInrate());
//				jo.put("time", oi.getTime());
//				System.out.println(jo);
//				ja.add(jo);
//			}
//			
//			
//			session.getTransaction().commit();
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//			session.getTransaction().rollback();
//		}finally{
//		if(session!=null){
//			if(session.isOpen()){
//				session.close();
//			}
//		}
//		}
//		return ja;
//	}
	
//	public static void insertOtherInfo(JSONObject jo) {
//		//璇诲彇閰嶇疆鏂囦欢
//		Configuration cfg = new Configuration().configure();
//		
//		SessionFactory factory = cfg.buildSessionFactory();
//		
//		Session session = null;
//		try{
//			session = factory.openSession();
//			//锟�?鍚簨锟�?
//			session.beginTransaction();
//			
//			Otherinfo otherinfo = new Otherinfo();
//			otherinfo.setId(jo.getString("id"));
//			otherinfo.setInrate(jo.getDouble("inrate"));
//			System.out.println(jo.getDouble("inrate"));
//			
//			otherinfo.setInsize(jo.getInt("insize"));
//			otherinfo.setLat(jo.getString("lat"));
//			otherinfo.setLon(jo.getString("lon"));
//			otherinfo.setMmac(jo.getString("mmac"));
//			otherinfo.setSize(jo.getInt("size"));
//			otherinfo.setTime(jo.getString("time"));
//			
//			
//			session.save(otherinfo);
//			//鎻愪氦浜嬪姟
//			session.getTransaction().commit();
//			
//		}catch(Exception e){
//			e.printStackTrace();
//			//鍥炴粴浜嬪姟
//			session.getTransaction().rollback();
//		}finally{
//			if(session != null){
//				if(session.isOpen()){
//					//鍏抽棴session
//					session.close();
//				}
//			}
//		}
//	}
	
	 public static void writeLog(String str)
	    {
	        try
	        {
	        String path="/home/gyc3/otherInfo.json";
	        File file=new File(path);
	        if(!file.exists())
	            file.createNewFile();
	        
	        FileOutputStream out=new FileOutputStream(file,true); //锟斤拷锟阶凤拷臃锟绞斤拷锟絫rue        
	        StringBuffer sb=new StringBuffer();
	        sb.append(str+"\r\n");
	        out.write(sb.toString().getBytes("utf-8"));//注锟斤拷锟斤拷要转锟斤拷锟斤拷应锟斤拷锟街凤拷锟斤拷
	        out.close();
	        }
	        catch(IOException ex)
	        {
	            System.out.println(ex.getStackTrace());
	        }
	    }    
	
	public  ArrayList<String> readFile(String filePath){
        try {
                String encoding="UTF-8";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //锟叫讹拷锟侥硷拷锟角凤拷锟斤拷锟�
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//锟斤拷锟角碉拷锟斤拷锟斤拷锟绞�
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    ArrayList<String> list = new ArrayList<String>();
                    while((lineTxt = bufferedReader.readLine()) != null){
//                        System.out.println(lineTxt);
                    	list.add(lineTxt);
                    }
                    read.close();
                    return list;
        }else{
            System.out.println("锟揭诧拷锟斤拷指锟斤拷锟斤拷锟侥硷拷");
            return null;
        }
        } catch (Exception e) {
            System.out.println("锟斤拷取锟侥硷拷锟斤拷锟捷筹拷锟斤拷");
            e.printStackTrace();
            return null;
        }
     
    }
	
	
	
}

package frontend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class testCollectionSort {
	public static void main(String[]args)
	{
//		List<String> list = new ArrayList<String>();
//		list.add("2017/04/10 13:50:40");
//		list.add("2017/04/10 13:50:36");
//		list.add("2017/04/10 13:50:39");
//		Collections.sort(list);
//		for(int i = 0;i<list.size();i++)
//		{
//			System.out.println(list.get(i));
//		}
//		
//		JSONArray ja = new JSONArray();
//		ja.add("{'y':'1'}");
//		ja.add("{'b':'2'}");
//		ja.add("{'c':'3'}");
//		ja.add("{'b':'2'}");
//		for(int i = 0;i<ja.size();i++)
//		{
//			System.out.println(ja.get(i));
//		}
//		
		JSONObject jo = new JSONObject();
		jo.put("c2", 3);
		String str = jo.toString();
		System.out.println(str);
		System.out.println(str.replaceAll("c", "t"));
		
		String res = "{'c1':'1'}";
		System.out.println(res.replaceAll("c", "t"));
	}
}

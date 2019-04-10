package frontend;

import java.util.Arrays;

import net.sf.json.JSONObject;

public class testJOshuzu {
	public static void main(String[]args)
	{
//		int []time = new int[24];
//		for(int i =0;i<24;i++)
//		{
//			time[i] = i+5;
//		}
//		System.out.println(Arrays.toString(time));
		String str = "{'a':'1'}";
		JSONObject jo = JSONObject.fromObject(str);
		jo.containsKey("a");
		System.out.println(jo.getString("b"));
	}
}

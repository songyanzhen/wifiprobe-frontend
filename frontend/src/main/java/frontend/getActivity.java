package frontend;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
@Controller
public class getActivity {
	//鏉ヨ鍛ㄦ湡鍜岄珮涓綆娲昏穬搴�
	@RequestMapping(value="/getactivity", method=RequestMethod.GET)
	public @ResponseBody String getActivityFunc(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//
		int high = 3;
		int medium = 6;
		int low = 9;
		
		
		String probeID=request.getParameter("probeID");
		String tableName = null;
		if(probeID.equals("0"))
			tableName = "cycle20171";
		else if(probeID.equals("1"))
			tableName = "cycle20172";
		JSONObject re = HBaseTest.getOneRecordActivity(tableName, "new", high,medium, low);
		
		String res = re.toString();
		res.replaceAll("c", "t");
		System.out.println(res.replaceAll("c", "t"));
		return res.replaceAll("c", "t");
	}
}

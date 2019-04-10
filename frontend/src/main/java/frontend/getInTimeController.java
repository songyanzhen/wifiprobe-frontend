package frontend;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class getInTimeController {
	
	//驻店时长
	@RequestMapping(value="/getintime", method=RequestMethod.GET)
	public @ResponseBody String getInTime(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String probeID=request.getParameter("probeID");
		
		String tableName = null;
		if(probeID.equals("0"))
			tableName = "inTime1";
		else if(probeID.equals("1"))
			tableName = "inTime2";
		JSONObject re = HBaseTest.getOneRecordInTime(tableName, "new",0, 12);
//		System.out.println(re);
		return re.toString();
	}
}

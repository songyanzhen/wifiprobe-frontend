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
public class getOldActivityController {
	
	String date;//从前端获取日期时间,生成一个yyyy/MM的string
	//驻店时长
	@RequestMapping(value="/getoldactivity", method=RequestMethod.GET)
	public @ResponseBody String getOldActivity(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String tableName = "cycle";
		JSONObject re = HBaseTest.getOneRecordInTime(tableName, "");
		System.out.println(re);
		return re.toString();
	}
}

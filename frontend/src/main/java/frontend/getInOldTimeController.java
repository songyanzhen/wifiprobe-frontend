package frontend;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/details")
public class getInOldTimeController {
	
	String date,hour;
	@RequestMapping(value="/intime", method=RequestMethod.GET)
	public @ResponseBody String getInOldTime(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String probeID=request.getParameter("probeID");
		Date date = new Date(request.getParameter("date"));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
		
		
		System.out.println(probeID);
		System.out.println(sdf.format(date));
		String tableName = null;
		if(probeID.equals("0"))
			tableName = "inTime1";
		else if(probeID.equals("1"))
			tableName = "inTime2";

		return HbaseOld.scanByPrefixFilterInOldTime(tableName, sdf.format(date), 2, 3, 4, 5);
	}
}

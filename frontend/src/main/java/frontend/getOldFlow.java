package frontend;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/details")
public class getOldFlow {
	@RequestMapping(value="/traffic", method=RequestMethod.GET)
	public @ResponseBody String getFlow(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		JSONObject res = new JSONObject();
		String probeID=request.getParameter("probeID");
		Date date = new Date(request.getParameter("date"));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");

		Calendar c = Calendar.getInstance();   
		
		System.out.println(probeID);
		System.out.println(sdf.format(date));
		String tableName = null;
		if(probeID.equals("0"))
			tableName = "custom1";
		else if(probeID.equals("1"))
			tableName = "custom2";
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -6);
		res.put("d1", HbaseOld.scanOldFlow(tableName, sdf.format(c.getTime())));
		
		//System.out.println(lastDate);
		for(int i = 1; i < 7; i++) {
			c.add(Calendar.DAY_OF_MONTH, +1);
			int h = i+1;
			res.put("d"+h, HbaseOld.scanOldFlow(tableName, sdf.format(c.getTime())));
			
		}
		
		return res.toString();
		//return HbaseOld.scanByPrefixFilternewOld(tableName, sdf.format(date));
	}
}

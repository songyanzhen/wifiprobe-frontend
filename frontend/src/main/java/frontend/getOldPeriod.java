package frontend;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
public class getOldPeriod {
	@RequestMapping(value="/period", method=RequestMethod.GET)
	public @ResponseBody String getPeriod(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		int high = 3;
		int medium = 6;
		int low = 9;
		
		
		String probeID=request.getParameter("probeID");
		Date date = new Date(request.getParameter("date"));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM");

	
		System.out.println(probeID);
		System.out.println(sdf.format(date));
		String tableName = null;
		if(probeID.equals("0"))
			tableName = "cycle20171";
		else if(probeID.equals("1"))
			tableName = "cycle20172";
		
		
		return HbaseOld.scanByPrefixFilterOldPeriod(tableName, sdf.format(date), high, medium, low);
	}
}

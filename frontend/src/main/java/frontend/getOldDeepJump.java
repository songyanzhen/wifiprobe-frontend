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

	public class getOldDeepJump {
		@RequestMapping(value="/deep", method=RequestMethod.GET)
		public @ResponseBody String getDeepJump(HttpServletRequest request,HttpServletResponse response) throws IOException
		{
			String probeID=request.getParameter("probeID");
			Date date = new Date(request.getParameter("date"));
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
			double []result = new double[2];
			double []deep = new double[7];
			double []jump = new double[7];
			
			Calendar c = Calendar.getInstance();   
		
			System.out.println(probeID);
			System.out.println(sdf.format(date));
			String tableName = null;
			if(probeID.equals("0"))
				tableName = "inDay1";
			else if(probeID.equals("1"))
				tableName = "inDay2";
			c.setTime(date);
			c.add(Calendar.DAY_OF_MONTH, -6);
			result = HbaseOld.scanDeepJump(tableName, sdf.format(c.getTime()));
			deep[0] = result[0];
			jump[0] = result[1];
			//System.out.println(lastDate);
			for(int i = 1; i < 7; i++) {
				c.add(Calendar.DAY_OF_MONTH, +1);
				result = HbaseOld.scanDeepJump(tableName, sdf.format(c.getTime()));
				deep[i] = result[0];
				jump[i] = result[1];
			}
			
		   JSONObject jo = new JSONObject();
		   jo.put("deep", Arrays.toString(deep));
		   jo.put("jump", Arrays.toString(jump));
			return jo.toString();
			
			//return HbaseOld.scanByPrefixFilternewOld(tableName, sdf.format(date));
		}
	}


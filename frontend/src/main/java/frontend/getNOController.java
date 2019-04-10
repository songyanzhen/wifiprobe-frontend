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
public class getNOController {
	//鏂拌�侀【瀹�
	@RequestMapping(value="/getNo", method=RequestMethod.GET)
	public @ResponseBody String getNo(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String probeID=request.getParameter("probeID");
		String tableName = null;
		if(probeID.equals("0"))
			tableName = "new1";
		else if(probeID.equals("1"))
			tableName = "new2";
		JSONObject re = HBaseTest.getOneRecordNo(tableName, "new");
		JSONObject res = new JSONObject();
		res.put("newVisitor", re.getString("new"));
		res.put("oldVisitor", re.getString("old"));
		return res.toString();
	}
}

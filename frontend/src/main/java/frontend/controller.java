package frontend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class controller {
	
	@RequestMapping(value="/index")
	public String index(Model model)
	{
		return null;
	}
	
	@RequestMapping(value="/period")
	public String period(Model model)
	{
		return null;
	}
	@RequestMapping(value="/deep")
	public String deep(Model model)
	{
		return null;
	}
	@RequestMapping(value="/intime")
	public String intime(Model model)
	{
		return null;
	}
	@RequestMapping(value="/traffic")
	public String traffic(Model model)
	{
		return null;
	}
	@RequestMapping(value="/type")
	public String type(Model model)
	{
		return null;
	}
	@RequestMapping(value="/activity")
	public String activity(Model model)
	{
		return null;
	}
}

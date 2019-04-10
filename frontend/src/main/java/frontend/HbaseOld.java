package frontend;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HbaseOld {
	private static Configuration conf=null;
	static{
		conf=HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "192.168.116.46,192.168.116.47,192.168.116.48");
	}
	
	public static String scanOldFlow(String tableName, String rowKey) throws IOException
	{
		//rowkey is yyyy/MM/dd
		int []all = new int[24];
		int []in = new int[24];
		double []rate = new double[24];
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		JSONObject jo3 = new JSONObject();
		JSONArray res = new JSONArray();
		HTable table=new HTable(conf, tableName);
		Get get=new Get(rowKey.getBytes());
		Result rs=table.get(get);
		for(KeyValue kv:rs.raw()){
      	    int hour = Integer.parseInt(new String(kv.getQualifier()));
      	    
      	    String str = new String(kv.getValue());
      	    int allsize = Integer.parseInt(str.split(",")[0]);
      	    int insize = Integer.parseInt(str.split(",")[1]);
      	    double inrate = Double.parseDouble(str.split(",")[2]);
      	    
      	    all[hour] = allsize;
      	    in[hour] = insize;
      	    rate[hour] = inrate;
		}
		jo1.put("data", Arrays.toString(all));
		jo2.put("data", Arrays.toString(in));
		jo3.put("data", Arrays.toString(rate));
		res.add(jo1);res.add(jo2);res.add(jo3);
		return res.toString();
	}
	
	public static String scanByPrefixFilterOldPeriod(String tablename, String rowKey, int highIndex, int mediumIndex, int lowIndex) throws IOException {
		int []period = new int[11];
		int high = 0;
		int medium = 0;
		int low = 0;
		int sleep = 0;
		JSONObject re = new JSONObject();
		
		HTable table=new HTable(conf, tablename);
		Get get=new Get(rowKey.getBytes());
		Result rs=table.get(get);
		for(KeyValue kv:rs.raw()){
			String col = new String(kv.getQualifier());
      	    int colIndex = Integer.parseInt(col.substring(1, col.length()));
      	    period[colIndex-1] = Integer.parseInt(new String(kv.getValue()));
      	    
      	    if(colIndex<highIndex)
				high+=Integer.parseInt(new String(kv.getValue()));
			else if(colIndex<mediumIndex&&colIndex>=highIndex)
			{
				medium+=Integer.parseInt(new String(kv.getValue()));
			}
			else if(colIndex<lowIndex&&colIndex>=mediumIndex)
			{
				low+=Integer.parseInt(new String(kv.getValue()));
			}
			else if(colIndex>=lowIndex)
			{
				sleep+=Integer.parseInt(new String(kv.getValue()));
			}
			System.out.print(new String(kv.getRow()) + " " );       
            System.out.print(new String(kv.getFamily()) + ":" );       
            System.out.print(new String(kv.getQualifier()) + " " );       
            System.out.print(kv.getTimestamp() + " " );       
            System.out.println(new String(kv.getValue()));      
		}
		re.put("high", high);
		re.put("medium", medium);
		re.put("low", low);
		re.put("sleep", sleep);	
		re.put("period", Arrays.toString(period));
		return re.toString();
	}
	
	//פ��ʱ��details
	public static String scanByPrefixFilterInOldTime(String tablename, String rowPrifix, int f1i, int f2i, int f3i, int f4i) {
		int []f1 = new int[24];
		int []f2 = new int[24];
		int []f3 = new int[24];
		int []f4 = new int[24];
		int []f5 = new int[24];
		   try {
		       HTable table = new HTable(conf, tablename);
		       Scan s = new Scan();
		       s.setFilter(new PrefixFilter(rowPrifix.getBytes()));
		       ResultScanner rs = table.getScanner(s);
		       for (Result r : rs) {
		          KeyValue[] kv = r.raw();
		          int hour = 0;//����ͬһ��kv��hour�ǲ����
		          
		          for (int i = 0; i < kv.length; i++) {
		        	  String strHour = new String(kv[i].getRow());
		        	  hour = Integer.parseInt(strHour.split(",")[1]);
		        	  
		        	  String col = new String(kv[i].getQualifier());
		        	  int colIndex = Integer.parseInt(col.substring(1, col.length()));
		        	  if(colIndex<f1i)
		        		  f1[hour]+=Integer.parseInt(new String(kv[i].getValue()));
		        	  else if(colIndex<f2i&&colIndex>=f1i)
		        		  f2[hour]+=Integer.parseInt(new String(kv[i].getValue()));
		        	  else if(colIndex<f3i&&colIndex>=f2i)
		        		  f3[hour]+=Integer.parseInt(new String(kv[i].getValue()));
		        	  else if(colIndex<f4i&&colIndex>=f3i)
		        		  f4[hour]+=Integer.parseInt(new String(kv[i].getValue()));
		        	  else if(colIndex>=f4i)
		        		  f5[hour]+=Integer.parseInt(new String(kv[i].getValue()));
//		              System.out.print(new String(kv[i].getRow()) + "  ");
//		              System.out.print(new String(kv[i].getFamily()) + ":");
//		              System.out.print(new String(kv[i].getQualifier()) + "  ");
//		              System.out.print(kv[i].getTimestamp() + "  ");
//		              System.out.println(new String(kv[i].getValue()));
		          }
		      }
		   } catch (IOException e) {
		         e.printStackTrace();
		   }
		   JSONObject jo = new JSONObject();
		   jo.put("f1", Arrays.toString(f1));
		   jo.put("f2", Arrays.toString(f2));
		   jo.put("f3", Arrays.toString(f3));
		   jo.put("f4", Arrays.toString(f4));
		   jo.put("f5", Arrays.toString(f5));
		   
		   System.out.println(jo.toString());
		return jo.toString();
		}
	//���Ϲ˿� details
	public static String scanByPrefixFilternewOld(String tablename, String rowPrifix) {
		int []newVisitor = new int[12];
		int []oldVisitor = new int[12];
		   try {
		       HTable table = new HTable(conf, tablename);
		       Scan s = new Scan();
		       s.setFilter(new PrefixFilter(rowPrifix.getBytes()));
		       ResultScanner rs = table.getScanner(s);
		       for (Result r : rs) {
		          KeyValue[] kv = r.raw();
		          int month = 0;
		          
		          for (int i = 0; i < kv.length; i++) {
		        	  String strMonth = new String(kv[i].getRow());
		        	  month = Integer.parseInt(strMonth.substring(5, 7));
		        	  
		        	  String col = new String(kv[i].getQualifier());
		        	  if(col.equals("new"))
		        		  newVisitor[month-1]+=Integer.parseInt(new String(kv[i].getValue()));
		        	  if(col.equals("old"))
		        		  oldVisitor[month-1]+=Integer.parseInt(new String(kv[i].getValue()));
//		              System.out.print(new String(kv[i].getRow()) + "  ");
//		              System.out.print(new String(kv[i].getFamily()) + ":");
//		              System.out.print(new String(kv[i].getQualifier()) + "  ");
//		              System.out.print(kv[i].getTimestamp() + "  ");
//		              System.out.println(new String(kv[i].getValue()));
		          }
		      }
		   } catch (IOException e) {
		         e.printStackTrace();
		   }
		   JSONObject jo = new JSONObject();
		   jo.put("newVisitor", Arrays.toString(newVisitor));
		   jo.put("oldVisitor", Arrays.toString(oldVisitor));
		return jo.toString();
		}
	//����� details
	public static double[] scanDeepJump(String tablename, String rowKey) {
		double []dj = new double[2];
		   try {
		       HTable table = new HTable(conf, tablename);
		       Get get=new Get(rowKey.getBytes());
		       Result rs=table.get(get);
		       
	          KeyValue[] kv = rs.raw();
	          
	          for (int i = 0; i < kv.length; i++) {

	        	  String col = new String(kv[i].getQualifier());
	        	  if(col.equals("deep"))  		  
	        		  dj[0] = Double.valueOf(new String(kv[i].getValue())).doubleValue();
	        	  if(col.equals("jump"))
	        		  dj[1] = Double.valueOf(new String(kv[i].getValue())).doubleValue();
	          }
		      
		       System.out.println(dj[0]);
		       System.out.println(dj[1]);
		   } catch (IOException e) {
		         e.printStackTrace();
		   }
//		   JSONObject jo = new JSONObject();
//		   jo.put("newVisitor", Arrays.toString(newVisitor));
//		   jo.put("oldVisitor", Arrays.toString(oldVisitor));
		//return jo.toString();
		   return dj;
	}

	
}

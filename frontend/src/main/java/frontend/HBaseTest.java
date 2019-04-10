package frontend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import net.sf.json.JSONObject;

public class HBaseTest {
	private static Configuration conf=null;
	static{
		conf=HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "192.168.116.46,192.168.116.47,192.168.116.48");
	}
	
	
	
	
	
	/**
	 * 鍒涘缓涓�涓〃
	 * @date 2015-07-23 21:44:32
	 * @author sgl
	 * @param tableName 琛ㄥ悕
	 * @param columnFamilys 鍒楁棌
	 * @throws IOException
	 */
	public static void createTable(String tableName,String[]columnFamilys) throws IOException{
		HBaseAdmin admin =new HBaseAdmin(conf);
		if (admin.tableExists(tableName)) {
			System.out.println("table already exists!");
		}else{
			HTableDescriptor tableDesc=new HTableDescriptor(tableName);
			for(int i=0;i<columnFamilys.length;i++){
				tableDesc.addFamily(new HColumnDescriptor(columnFamilys[i]));
			}
			admin.createTable(tableDesc);
			System.out.println("create table "+tableName+" success!");
		}
		
	}
	/**
	 * 娣诲姞涓�鏉¤褰�
	 * @date 2015-07-23 22:16:30
	 * @author sgl
	 * @param tableName 琛ㄥ悕
	 * @param rowKey 琛屽仴
	 * @param family 鍒楁棌
	 * @param qualifier 闄愬畾绗�
	 * @param value 鍊�
	 * @throws IOException
	 */
	public static void addRecord(String tableName,String rowKey,String family,String qualifier,String value) throws IOException{
		HTable table =new HTable(conf, tableName);
		Put put=new Put(Bytes.toBytes(rowKey));
		put.add(Bytes.toBytes(family),Bytes.toBytes(qualifier),Bytes.toBytes(value));
		table.put(put);
		System.out.println("insert record "+rowKey+" to table "+tableName+" success");
		
	}
	/**
	 * 鍒犻櫎涓�琛岃褰�
	 * @date 2015-07-23 22:17:53
	 * @author sgl
	 * @param tableName 琛ㄥ悕
	 * @param rowKey 琛屽仴
	 * @throws IOException
	 */
	public static void deleteRecord(String tableName,String rowKey) throws IOException{
		HTable table=new HTable(conf, tableName);
		List<Delete>list=new ArrayList<Delete>();
		Delete delete=new Delete(rowKey.getBytes());
		list.add(delete);
		table.delete(list);
		System.out.println("delete record "+ rowKey+" success!");
		
	}
	/**
	 * 鑾峰彇涓�琛岃褰�
	 * @date 2015-07-23 22:21:33
	 * @author sgl
	 * @param tableName 琛ㄥ悕
	 * @param rowKey 琛屽仴
	 * @return 
	 * @throws IOException 
	 */
	public static JSONObject getOneRecord(String tableName,String rowKey) throws IOException{
		HTable table=new HTable(conf, tableName);
		Get get=new Get(rowKey.getBytes());
		Result rs=table.get(get);
		for(KeyValue kv:rs.raw()){
			System.out.print(new String(kv.getRow()) + " " );       
            System.out.print(new String(kv.getFamily()) + ":" );       
            System.out.print(new String(kv.getQualifier()) + " " );       
            System.out.print(kv.getTimestamp() + " " );       
            System.out.println(new String(kv.getValue()));      
		}
		return null;
		
	}
	
	public static JSONObject getOneRecordInTime(String tableName,String rowKey, int jumpIndex, int deepIndex) throws IOException{
		JSONObject re = new JSONObject();
		HTable table=new HTable(conf, tableName);
		Get get=new Get(rowKey.getBytes());
		Result rs=table.get(get);
		
		int count = 0;
		int jump = 0;
		int deep = 0;
		for(KeyValue kv:rs.raw()){
			String col = new String(kv.getQualifier());
//			System.out.println(col);
			
			if(col.equals(""))
			{
				re.put("time", new String(kv.getValue()));
			}
			else
			{
				int colIndex = Integer.parseInt(col.substring(1, col.length()));
				re.put(col, new String(kv.getValue()));
				count+=Integer.parseInt(new String(kv.getValue()));
				if(colIndex<=jumpIndex)
					jump+=Integer.parseInt(new String(kv.getValue()));
				if(colIndex>deepIndex)
					deep+=Integer.parseInt(new String(kv.getValue()));
			}
//			System.out.print(new String(kv.getRow()) + " " );       
//            System.out.print(new String(kv.getFamily()) + ":" );       
//            System.out.print(new String(kv.getQualifier()) + " " );       
//            System.out.print(kv.getTimestamp() + " " );       
//            System.out.println(new String(kv.getValue()));      
		}
		
		re.put("sum", count);
		if(count==0)
		{
			re.put("jump", 0);
			re.put("deep", 0);
		}else
		{
			re.put("jump", (double)jump/(double)count);
			re.put("deep", (double)deep/(double)count);
		}
		
		return re;
		
	}
	
	public static JSONObject getOneRecordActivity(String tableName,String rowKey, int highIndex, int mediumIndex, int lowIndex) throws IOException{
		JSONObject re = new JSONObject();
		HTable table=new HTable(conf, tableName);
		Get get=new Get(rowKey.getBytes());
		Result rs=table.get(get);
		int count = 0;
		int high = 0;
		int medium = 0;
		int low = 0;
		int sleep = 0;
		for(KeyValue kv:rs.raw()){
			String col = new String(kv.getQualifier());
			if((new String(kv.getQualifier())).equals(""))
			{
				re.put("time", new String(kv.getValue()));
			}
			else
			{
				int colIndex = Integer.parseInt(col.substring(1, col.length()));

				re.put(new String(kv.getQualifier()), new String(kv.getValue()));
				count+=Integer.parseInt(new String(kv.getValue()));
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
			}
//			System.out.print(new String(kv.getRow()) + " " );       
//            System.out.print(new String(kv.getFamily()) + ":" );       
//            System.out.print(new String(kv.getQualifier()) + " " );       
//            System.out.print(kv.getTimestamp() + " " );       
//            System.out.println(new String(kv.getValue()));      
		}
		re.put("sum", count);
		re.put("high", high);
		re.put("medium", medium);
		re.put("low", low);
		re.put("sleep", sleep);	
		return re;
		
	}
	
	public static JSONObject getOneRecordNo(String tableName,String rowKey) throws IOException{
		JSONObject re = new JSONObject();
		HTable table=new HTable(conf, tableName);
		Get get=new Get(rowKey.getBytes());
		Result rs=table.get(get);
		for(KeyValue kv:rs.raw()){
			String col = new String(kv.getQualifier());
//			System.out.println(col);
			
			if(col.equals(""))
			{
				re.put("time", new String(kv.getValue()));
			}
			else
			{
				re.put(col, new String(kv.getValue()));
			}
//			System.out.print(new String(kv.getRow()) + " " );       
//            System.out.print(new String(kv.getFamily()) + ":" );       
//            System.out.print(new String(kv.getQualifier()) + " " );       
//            System.out.print(kv.getTimestamp() + " " );       
//            System.out.println(new String(kv.getValue()));      
		}
		return re;
	}
	
	/**
	 * 鑾峰彇鎵�鏈夋暟鎹�
	 * @date 2015-07-23 22:26:19
	 * @author sgl
	 * @param tableName 琛ㄥ悕
	 * @throws IOException 
	 */
	public static void getAllRecord(String tableName) throws IOException{
		HTable table=new HTable(conf, tableName);
		Scan scan=new Scan();
		ResultScanner scanner=table.getScanner(scan);
		for(Result result:scanner){
			for(KeyValue kv:result.raw()){
				System.out.print(new String(kv.getRow()) + " ");       
                System.out.print(new String(kv.getFamily()) + ":");       
                System.out.print(new String(kv.getQualifier()) + " ");       
                System.out.print(kv.getTimestamp() + " ");       
                System.out.println(new String(kv.getValue()));   
			}
		}
		
	}
	/**
	 * 鍒犻櫎涓�涓〃
	 * @date 2015-07-23 22:29:35
	 * @author sgl
	 * @param tableName 琛ㄥ悕
	 * @throws IOException 
	 */
	public static void deleteTable(String tableName) throws IOException{
		HBaseAdmin admin=new HBaseAdmin(conf);
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
		System.out.println("delete table "+tableName+" success!");
		
	}
	
	public static void main(String[] args) {
		try {
			String tableName="timeslot";
			String[]columnFamilys={"minDistribution"};
			HBaseTest.createTable(tableName, columnFamilys);
			
			HBaseTest.addRecord(tableName, "1e:2o,20170330", "min", "17:28", "11111111110000000000");
			HBaseTest.addRecord(tableName, "1e:3c,20170330", "min", "17:28", "00001111110000000000");
			HBaseTest.addRecord(tableName, "1e:3c,20170330", "min", "17:40", "00001111111111111111");
			HBaseTest.addRecord(tableName, "1e:3c,20170330", "min", "17:41", "11111111110000000000");
			HBaseTest.addRecord(tableName, "1e:5y,20170330", "min", "17:41", "00111111110000000000");
//			HBaseTest.addRecord(tableName, "sgl", "grade", "", "5");
//			HBaseTest.addRecord(tableName, "sgl", "course", "", "90");
//			HBaseTest.addRecord(tableName, "sgl", "course", "math", "97");
//			HBaseTest.addRecord(tableName, "sgl", "course", "art", "87");
//			HBaseTest.addRecord(tableName, "guoguo", "grade", "", "4");
//			HBaseTest.addRecord(tableName, "guoguo", "course", "math", "89");
//			
//			System.out.println("********get one record*********");
//			HBaseTest.getOneRecord(tableName, "sgl");
			
			System.out.println("********get all record*********");
			HBaseTest.getAllRecord(tableName);
			
//			System.out.println("********delete one record*********");
//			HBaseTest.deleteRecord(tableName, "guoguo");
//			HBaseTest.getAllRecord(tableName);
			
//			System.out.println("********delete table*********");
//			HBaseTest.deleteTable(tableName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

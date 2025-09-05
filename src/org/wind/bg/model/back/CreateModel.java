package org.wind.bg.model.back;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.wind.bg.model.User;
import org.wind.orm.Table;
import org.wind.orm.annotation.Column;
import org.wind.orm.annotation.DateTime;
import org.wind.orm.annotation.Id;
import org.wind.orm.annotation.Tables;
import org.wind.orm.bean.Config;
import org.wind.orm.util.TableUtil;

/**
 * @描述 : 生成Model实体类
 * @版权 : 胡璐璐
 * @时间 : 2017年10月25日 14:50:36
 */
public class CreateModel {

	/**生成Model所需要的信息**/
	public static final String table="config";			//当前生成Model的实体表
	public static final String dataSource="";			//当前数据源ID
	public static final String table_separator="_";			//表的分隔符（如：nh_cp_product_info的分隔符号为“_”）,用来生成正规的Model名
	public static final String col_separator="_";			//列的分隔符（如：USER_ID的分隔符号为“_”）,用来生成正规的成员属性（变量）名
	//要放在哪个Model的Package下
	public static final Class<? extends Table> tableClass=User.class;	//生成Model存放的Package目录（以指定的tableClass类为主）
	public static final Class<?> tableParentClass=tableClass.getSuperclass();
	
	public static void main(String[] args) throws Exception {
		Config config=TableUtil.getConfig(tableClass);
		Connection con=null;
		try{
			con=DriverManager.getConnection(config.getUrl(),config.getUserName(),config.getPassWord());
//			service(con);		//所有表生成
			service(con,table);		//单表生成
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(con!=null){con.close();}
		}
	}
	
	/**生成所有数据表的Model**/
	public static void service(Connection con) throws Exception{
		List<String> t_tableList=new ArrayList<String>();
		/*生成所有数据表的Model*/
		DatabaseMetaData dbmd = con.getMetaData();  
		String dbName=con.getCatalog();		//当前目录名称（数据库名）
	    // 表名列表  
		ResultSet rest = dbmd.getTables(dbName, null, null,new String[] { "TABLE" });
		// 输出 table_name
		while (rest.next()) {
			String t_table=rest.getString("TABLE_NAME");
			//_copy表不需要
			if(t_table!=null && t_table.length()>0 && !t_table.equalsIgnoreCase("_copy")){
				t_tableList.add(t_table);
			}
		}
		rest.close();
		for(String t_table:t_tableList){
			service(con,t_table);		//单表生成
		}
	}
	
	//处理
	public static void service(Connection con,String table) throws Exception {
		Package t_pack=tableClass.getPackage();
		String packagePath=t_pack.getName();		//Model存放的src目录
		
		/**获取Model路径**/
		String t_targetPath=System.getProperty("user.dir")+"/src/"+packagePath;		//保存的Model的硬盘路径
		t_targetPath=t_targetPath.replaceAll("\\.", "/");		//点号改为正斜杠
		t_targetPath=t_targetPath.replaceAll("\\\\", "/");		//反斜杠改为正斜杠
		t_targetPath+="/";
		
		/**Model名**/
		String t_model=getModeName(table);		//Model名
		File file_model=new File(t_targetPath,t_model+".java");		//Model文件
		//是否不存在
		if(!file_model.exists()){
			String t_line="\r\n";		//换行符
		    String userName = System.getenv().get("USERNAME");	// 获取当前系统登录的用户名
		    String current_dateTime=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date());		//当前操作时间
		    
		    /*******************Model内容*******************/
			StringBuffer t_model_content=new StringBuffer("package "+packagePath+";"+t_line+t_line);		//Model内容
			
			/**需要导入的包/类**/
			StringBuffer importStr=new StringBuffer("import "+tableParentClass.getName()+";"+t_line);		//继承Model
			/*导入包（检需要的）*/
			String an_Column="import "+Column.class.getName()+";"+t_line;		//Column注解
			String an_Id="import "+Id.class.getName()+";"+t_line;		//Id注解
//			String an_InsertTime="import "+InsertTime.class.getName()+";"+t_line;	//InsertTime注解
//			String an_UpdateTime="import "+UpdateTime.class.getName()+";"+t_line;	//UpdateTime注解
			String an_Tables="import "+Tables.class.getName()+";"+t_line;	//Tables注解
//			String an_ForeignKey="import "+ForeignKey.class.getName()+";"+t_line;	//ForeignKey注解
			String an_DateTime="import "+DateTime.class.getName()+";"+t_line;	//DateTime注解
//			String an_DataSource="import "+DataSource.class.getName()+";"+t_line;	//DataSource注解
			
			/*是否导入了注解格式包*/
			boolean isImport_DateTime=false;
			
			/**初始化信息**/
			String t_primaryKey=null;
			String table_remark=null;
			StringBuffer colStr=new StringBuffer();
			String tableArr[]=table.split("\\.");
			Config config=TableUtil.getConfig(tableClass);
			/**表**/
			ResultSet rs_table=con.getMetaData().getTables(null, null, tableArr[tableArr.length-1], new String[] {"TABLE"});
			if(rs_table.next()){
				table_remark = rs_table.getString("REMARKS");		//表注释
			}
			rs_table.close();
			//若正规的防范获取不到表注释
			if(table_remark==null || table_remark.trim().length()<=0){
				/*mysql*/
				//table_schema=数据库名
				String sql_table="Select TABLE_COMMENT from INFORMATION_SCHEMA.TABLES Where table_schema = '"+config.getDataBase()+"' AND table_name LIKE '"+tableArr[tableArr.length-1]+"' ";
				Statement stmt=con.createStatement();
				rs_table=stmt.executeQuery(sql_table);
				if(rs_table.next()){
					table_remark=rs_table.getString(1);
				}
				rs_table.close();
				stmt.close();
			}
			
			/**列**/
			ResultSet rs = con.getMetaData().getPrimaryKeys(null, null, tableArr[tableArr.length-1]);
			/*主键*/
			if(rs.next()){
				t_primaryKey=rs.getString("COLUMN_NAME");	//主键列名
				
				rs.close();
				rs = con.getMetaData().getColumns(null,null,tableArr[tableArr.length-1],t_primaryKey);		//
				//拿到
				if(rs.next()){
					int t_type=rs.getInt("DATA_TYPE");		//数据库类型：java.sql.Types
					
					String t_col_type=getSqlToJava_type(t_type,rs);		//java类型名
					String t_col_field=getColName(t_primaryKey);		//列字段名称
					//
					colStr.append("	@Id@Column(\""+t_primaryKey+"\")"+t_line);
					colStr.append("	private "+t_col_type+" "+t_col_field+";		//主键"+t_line);
				}
			}
			rs.close();
		
			/*其他列*/
			rs = con.getMetaData().getColumns(null, null, tableArr[tableArr.length-1],null);
			while(rs.next()){
				String t_col=rs.getString("COLUMN_NAME");	//列名
				//排除主键
				if(t_primaryKey==null || !t_primaryKey.equalsIgnoreCase(t_col)){
					int t_type=rs.getInt("DATA_TYPE");		//数据库类型：java.sql.Types
					String t_remaks=rs.getString("REMARKS");	//注释
					if(t_remaks==null || t_remaks.trim().length()<=0){
						t_remaks="";
					}else{
						t_remaks="		//"+t_remaks.trim();
					}
					//
					String t_col_type=getSqlToJava_type(t_type,rs);		//java类型名
					String t_col_field=getColName(t_col);		//列字段名称
					/*时间注解，及格式设置*/
					String an_DateTimeStr="";			//要加入的时间注解格式
					switch(t_type){
						case Types.DATE:{						//DATE（日期）
							isImport_DateTime=true;
							an_DateTimeStr="@DateTime(\"yyyy-MM-dd\")";
							break;		
						}
						case Types.TIME:{						//TIME（时间型）
							isImport_DateTime=true;
							an_DateTimeStr="@DateTime(\"HH:mm:ss\")";
							break;		
						}
						case Types.TIMESTAMP:{			//TIMESTAMP（时间戳）
							isImport_DateTime=true;
							an_DateTimeStr="@DateTime(\"yyyy-MM-dd HH:mm:ss\")";
							break;	
						}
					}
					
					/*列注解*/
					StringBuffer col_anStr=new StringBuffer();		//列注解信息
					String t_colArr[]=t_col.split(col_separator);		//列的段数
					String t_Column_str="";		//列名注解
					//两段以上（含两段）
					if(t_colArr.length>1){
						t_Column_str="	@Column(\""+t_col+"\")";
					}
					col_anStr.append(t_Column_str);
					col_anStr.append(an_DateTimeStr);
					//有内容
					if(col_anStr!=null && col_anStr.toString().trim().length()>0){
						col_anStr.append(t_line);
					}
					colStr.append(col_anStr);
					colStr.append("	private "+t_col_type+" "+t_col_field+";"+t_remaks+t_line);
					//
//					
				}
			}
			rs.close();
			/*导入包*/
			importStr.append(an_Tables);
			importStr.append(an_Column);
			importStr.append(an_Id);
//			importStr.append(an_DataSource);
			//日期时间格式化
			if(isImport_DateTime){
				importStr.append(an_DateTime);
			}
			importStr.append(t_line);
			
			table_remark=table_remark!=null?table_remark:"";
			/**文档注释——版权信息**/
			StringBuffer copyrightStr=new StringBuffer();
			copyrightStr.append("/**"+t_line);
			copyrightStr.append(" * @描述 : "+table_remark+t_line);
			copyrightStr.append(" * @作者 : "+userName+t_line);
			copyrightStr.append(" * @时间 : "+current_dateTime+t_line);
			copyrightStr.append(" */"+t_line);
			
			/**文档注释——常规（含：@Table注解，public class，序列化serialVersionUID）**/
			StringBuffer conventionalStr=new StringBuffer();
			if(dataSource!=null && dataSource.length()>0){
				conventionalStr.append("@DataSource(\""+dataSource+"\")"+t_line);
			}
			conventionalStr.append("@Tables(\""+table+"\")"+t_line);
			conventionalStr.append("public class "+t_model+" extends "+tableParentClass.getSimpleName()+"{"+t_line+t_line);
//			conventionalStr.append("	private static final long serialVersionUID = "+System.currentTimeMillis()+"L;"+t_line+t_line);
			
			/**全部写入Model的内容**/
			t_model_content.append(importStr);
			t_model_content.append(copyrightStr);
			t_model_content.append(conventionalStr);
			t_model_content.append(colStr);
			t_model_content.append(t_line+"}");
			
			/**写入文件**/
			FileOutputStream fos=new FileOutputStream(file_model);
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fos));		//字节流转为字符流
			bw.write(t_model_content.toString());
			
			bw.flush();
			bw.close();
			fos.close();
			System.out.println("写入完毕——"+table);
		}else{
			System.err.println("该Model已存在，不需要写入——"+table);
		}
	}
	
	/**获取 : 表映射的实体类（Model）名称——正规化**/
	public static String getModeName(String table){
		String t_tableArr[]=table.split(table_separator);
		String t_model=t_tableArr[0];		//Model名——第1段，首字母大写
		t_model=t_model.substring(0,1).toUpperCase()+t_model.substring(1).toLowerCase();	//首字母大小,其他小写，暂时需表名的长度大于1
		//多个分隔符
		if(t_tableArr.length>1){
			String t_model_other="";		//model其他段名
			for(int i=1;i<t_tableArr.length;i++){
				String t=t_tableArr[i];
				String t_new=t.substring(0,1).toUpperCase();		//当前段Model名——首字母
				if(t.length()>1){
					t_new+=t.substring(1).toLowerCase();		//其他全小写
				}
				t_model_other+=t_new;
			}
			t_model+=t_model_other;
		}
		return t_model;
	}
	/**获取 : 列映射的成员属性（变量、字段）名称**/
	public static String getColName(String col){
		String t_colArr[]=col.split(col_separator);
		String t_col=t_colArr[0].toLowerCase();		//返回的字段名称，第1段全小写
		//多个分隔符
		if(t_colArr.length>1){
			String t_col_other="";		//model其他段名
			for(int i=1;i<t_colArr.length;i++){
				String t=t_colArr[i];
				String t_new=t.substring(0,1).toUpperCase();		//当前段列名——首字母
				if(t.length()>1){
					t_new+=t.substring(1).toLowerCase();		//其他全小写
				}
				t_col_other+=t_new;
			}
			t_col+=t_col_other;
		}
		return t_col;
	}
	/**获取：sql类型对应的java类型的名称（有数组的带“[]”）**/
	public static String getSqlToJava_type(int sqlType,ResultSet rs) throws Exception{
		String t_col_type=null;
		switch(sqlType){
	//		case Types.ARRAY:t_col_type="String";break;		//ARRAY（数组型）
			case Types.BIGINT:t_col_type="Long";break;		//BIGINT（长整型数字）
			case Types.BINARY:t_col_type="byte[]";break;		//BINARY（二进制型）
			case Types.BIT :t_col_type="Boolean";break;		//BIT（字节型；位；0和1）
			case Types.BLOB:t_col_type="byte[]";break;		//BLOB（二进制大队向）
			case Types.BOOLEAN:t_col_type="Boolean";break;		//BOOLEAN（布尔型）
			case Types.CHAR:t_col_type="String";break;		//CHAR（字符型）
			case Types.CLOB:t_col_type="String";break;		//CLOB（大字符串对象）
			case Types.DATALINK:t_col_type="String";break;		//DATALINK（数据链接）
			case Types.DATE:t_col_type="String";break;		//DATE（日期）
			//DECIMAL（大数字型，一般是实数型——float、double）
			case Types.DECIMAL:{
				t_col_type="Double";
				int baoliu=rs.getInt("DECIMAL_DIGITS");
				if(baoliu==0){
					t_col_type="Long";
				}
				break;	
			}
	//		case Types.DISTINCT:t_col_type="String";break;		//DISTINCT（截然不同的？）
			case Types.DOUBLE:t_col_type="Double";break;		//DOUBLE（双精度实数型）
			case Types.FLOAT:t_col_type="Float";break;		//FLOAT（单精度是属性）
			case Types.INTEGER:t_col_type="Integer";break;		//INTEGER（有符号整数型）
	//		case Types.JAVA_OBJECT:t_col_type="String";break;		//JAVA_OBJECT
			case Types.LONGNVARCHAR:t_col_type="String";break;		//LONGNVARCHAR（长字符串型，Unicode数据）
			case Types.LONGVARBINARY:t_col_type="byte[]";break;		//LONGVARBINARY（长二进制、字节型）
			case Types.LONGVARCHAR:t_col_type="String";break;		//LONGVARCHAR（长字符串型，非Unicode数据）
			case Types.NCHAR:t_col_type="String";break;		//NCHAR（Unicode字符型）
			case Types.NCLOB:t_col_type="String";break;		//NCLOB（Unicode字符串大对象型）
	//		case Types.NULL:t_col_type="String";break;		//NULL
			//数字型（可整型，可小数【默认】）
			case Types.NUMERIC:{
				t_col_type="Double";
				int baoliu=rs.getInt("DECIMAL_DIGITS");
				if(baoliu==0){
					t_col_type="Long";
				}
				break;		
			}
			case Types.NVARCHAR:t_col_type="String";break;		//NVARCHAR（Unicode字符串型）
	//		case Types.OTHER:t_col_type="String";break;		//OTHER
	//		case Types.REAL:t_col_type="Double";break;		//REAL
	//		case Types.REF:t_col_type="String";break;		//REF
	//		case Types.ROWID:t_col_type="String";break;		//ROWID
			case Types.SMALLINT:t_col_type="Integer";break;		//SMALLINT（短整型）
	//		case Types.SQLXML:t_col_type="String";break;		//XML
	//		case Types.STRUCT:t_col_type="String";break;		//STRUCT（结构体）
			case Types.TIME:t_col_type="String";break;		//TIME（时间型）
			case Types.TIMESTAMP:t_col_type="String";break;		//TIMESTAMP（时间戳）
			case Types.TINYINT:t_col_type="Integer";break;		//TINYINT（非常小的整数）
			case Types.VARBINARY:t_col_type="byte[]";break;		//VARBINARY（？？二进制）
			case Types.VARCHAR:t_col_type="String";break;		//VARCHAR（字符串型）
			default:t_col_type="String";break;		//默认：字符串
		}
		return t_col_type;
	}
	
}
package com.auto.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.auto.util.code.CodeTypeName;
import com.auto.util.code.ICodeType;

public class Util {

	private static final Log log = LogFactory.getLog(Util.class);
	private static SimpleDateFormat YMD=new SimpleDateFormat("yyyy-MM-dd");


	/**
	 * 删除最后一个“逗号字符”
	 */
	public static StringBuilder delLastChar(StringBuilder sb){
		if (sb.length() > 0 && sb.lastIndexOf(",")==sb.length()-1) {
			sb.replace(sb.length() - 1, sb.length(), "");
		}
		return sb;
	}

	/**
	 * 代码值转换
	 * @deprecated {@link com.sohu.auto.userCenter.admin.constant.code.Code#code2str(Class, Integer) reference Code }
	 * @param code
	 * @return
	 */
	public static String code2str(Class<? extends ICodeType> codeType , Integer code){
		ICodeType[] codes=codeType.getEnumConstants();
		if(codes.length>code){
			return codeType.getEnumConstants()[code].getName();//TODO 不用枚举的序列定义代码值，而是用遍历的方法
		}else{
			CodeTypeName codeTypeName=codeType.getAnnotation(CodeTypeName.class);
			if(codeTypeName!=null){
				log.warn("没有找到【"+codeTypeName.name()+"】相应的代码值["+code+"]！");
			}
			return "[code:"+code+"is null]";
		}
	}

	/**
	 *获取一个double类型数值的百分比
	 *example: FormatPercent(0.33333333,2) = 33%
	 * @param number 小数
	 * @param newValue 精确位数
	 * @return
	 */
	public static String formatPercent(Object number, int newValue) {
	    if(number instanceof Double){
            if(Double.isNaN((Double)number) ){
                return "0.00%";//除零后的无理数
            }else if( Double.isInfinite((Double)number)){
                //无穷大，不做处理。一般属于业务数据错误。本程序不能处理
            }
	    }else if(number instanceof BigDecimal){
	        //TODO BigDecimal的无理数处理
	    }else if(number instanceof Float){
            if(Float.isNaN((Float)number) ){
                return "0.00%";//除零后的无理数
            }else if( Float.isInfinite((Float)number)){
                //无穷大，不做处理。一般属于业务数据错误。本程序不能处理
            }	    
        }
		java.text.NumberFormat nf = java.text.NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(newValue);
		return nf.format(number);
	}

	/**
	 * 前x天的日期
	 * 说明：今天的任何时刻，前一天指的是，昨天的凌晨到午夜
	 * @param count 前x天
	 * @return
	 */
	public static Calendar getCalendarByCount(int count){
		final long DAY_SECOND = 86400;//一天一共有86400秒
		final long startDay_SECOND = 28800;//Calendar第一天的开始时间8*60*60
		Calendar today=Calendar.getInstance();
		Calendar nToday=Calendar.getInstance();
		long todayTimeInMillis=today.getTimeInMillis()%(DAY_SECOND*1000);
		long nDayTimeInMillis=today.getTimeInMillis()-(todayTimeInMillis+DAY_SECOND*1000*count);
		nToday.setTimeInMillis(nDayTimeInMillis-startDay_SECOND*1000);
		return nToday;
	}

	/**
	 * 格式化BigDecimal类型数据 //TODO 有问题，需要改一下，有些数字精确多一位
	 * Fortmat BigDecimal while the number is too long.
	 * for example: the param value = 1222222.222222, 
	 * and then the return will be 1,222,222.22 . It depends on NumberFormat.getInstance() .
	 * @param value
	 * @param bit 精确度
	 * @return
	 */
	public static String formatBigDecimal(Object value,int bit){
		String content = null;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(bit);
        nf.setMinimumFractionDigits(bit);
		
		if (value == null) {
			content = "";
		} else {
			if(value instanceof BigDecimal){
			    //
			}else if(value instanceof Double){
			    if(Double.isNaN((Double)value)){
			        return "0";
			    }
			    //
            }else{
                //
			}
			content = nf.format(value);
		}
		return content;
	}

	/**
	 * 日期相减得到天数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long DateSub(Date d1,Date d2){
		long day=0;
		try {
			Date date1 = YMD.parse(YMD.format(d1));
			Date date2 =YMD.parse(YMD.format(d2));

			/*long l = date1.getTime()-date2.getTime()>0 ? date1.getTime()-date2.getTime(): 
				date2.getTime()-date1.getTime(); 
			 */
			//日期相减得到相差的日期 
			day = (date1.getTime()-date2.getTime())/(24*60*60*1000)>0 ? 
										(date1.getTime()-date2.getTime())/(24*60*60*1000): 
										(date2.getTime()-date1.getTime())/(24*60*60*1000); 
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return day;
	}
	
    /**
     * 按月统计转换
     * @param date
     * @return
     */
    public static String formatDateByMonth(Date date){
        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM");
        return dateformat.format(date);
    }
    /**
     * 按日统计转换
     * @param date
     * @return
     */
    public static String formatDateByDay(Date date){
        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
        return dateformat.format(date);
    }

    /**
     * 时间段格式化（如：x小时x分钟x秒）
     * @param timeInSeconds
     * @return
     */
    public static String calcHMS(double timeInSecondsD) {
        int hours=0, minutes=0, seconds=0;
        float timeInSeconds=Math.round(timeInSecondsD*100)/100.00f;
        hours = (int)(timeInSeconds / 3600);
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = (int)(timeInSeconds / 60);
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = (int)timeInSeconds;
        
        StringBuilder sb=new StringBuilder();
        if(hours!=0){
            sb.append(hours).append("小时");
        }
        if(!(hours==0 && minutes==0)){
            sb.append(minutes).append("分钟");
        }
            sb.append(seconds).append("秒");
        return sb.toString();
    }
    /**
	 * 防SQL注入
	 * @param str
	 * @author zhanzhu
	 */
	public static boolean sql_inj(String str) {
		String[] inj_stra = { "*", "script", "mid", "master", "truncate", "char", "insert",
				"select", "delete", "update", "declare", "iframe", "'" };
		for (int i = 0; i < inj_stra.length; i++) {
			if (str.toLowerCase().indexOf(inj_stra[i]) >= 0) {//sql不区分大小写
				log.info("特殊字符，传入str=" + str + ",特殊字符：" + inj_stra[i]);
				return true;
			}
		}
		return false;
	}
	
//	/**
//	 * get Connection
//	 * @return
//	 */
//	public static Connection getConnection() {
//		Connection conn = null;
//		String property = "user" ;//TODO 生产环境改成 application
//		String className = ConfigManager.instance().getProperty(property, "dataSource.driverClass");
//		String url = ConfigManager.instance().getProperty(property, "dataSource.jdbcUrl");
//		String userName = ConfigManager.instance().getProperty(property, "dataSource.user");
//		String password = ConfigManager.instance().getProperty(property, "dataSource.password");
//		try {
//			Class.forName(className);
//			conn = DriverManager.getConnection(url, userName, password);
//
//		} catch (ClassNotFoundException e) {
//			log.error("getConnection", e);
//		} catch (SQLException e) {
//			log.error("getConnection", e);
//		}
//		return conn;
//	}
	
//	/**
//	 * get Connection
//	 * @return
//	 */
//	public static Session getHibernateSession() {
//		String[] configLocations = { "applicationContext.xml" };
//		Connection conn = null;
//		ApplicationContext ctx = new ClassPathXmlApplicationContext(
//				configLocations);
//		SessionFactory sf = (SessionFactory) ctx.getBean("sessionFactory");
//		Session s = sf.openSession();
//		
//		return s;
//	}
	/**
	 * 释放数据库资源对象 conn stmt rs
	 * 
	 * @param conn
	 * @param stmt
	 * @param rs
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.error("close", e);
		}
	}
	/**
	 * 取ip
	 * @param request
	 * @return
	 */
	 public static String getRequestIP(HttpServletRequest request) {
			String ip = request.getHeader("X-Forwarded-For");

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			// 解析IP, 第一个为真ip
			String[] ips = null;
			if (ip != null) {
				ips = ip.split(",");
				ip = ips[0];
			}
			return ip;

		}
	 
	    //格式化日期
	    public static String data2Str(Date date){
//	        SimpleDateFormat dateformat2=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ");  
	        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy/MM/dd");  
	        return dateformat.format(date);
	    }
	    
	    /**
	     * 根据年龄和系统当前时间，计算出最小出生日期
	     * @param age
	     * @return
	     */
	    public static String calcMinBirthdayByAge(Integer age){
	        Calendar currDate=Calendar.getInstance();
	        currDate.add(Calendar.YEAR, -age);
	        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy/MM/dd");
	        return myFormatter.format(currDate.getTime());
	    }
	    
	    /**
	     * 根据出生日期计算年龄
	     * @param birthday
	     * @return
	     */
	    public static String calcAge(String birthday){
	        Date birthDay=new Date();
	        try {
	            birthDay = (new SimpleDateFormat("yyyy-MM-dd")).parse(birthday);
	        } catch (ParseException e) {
	            log.error(e);
	        }
	        Calendar cal = Calendar.getInstance();
	        if (cal.before(birthDay)) {
	            throw new IllegalArgumentException(
	                "The birthDay is before Now.It's unbelievable!");
	        }
	        int yearNow = cal.get(Calendar.YEAR);
	        int monthNow = cal.get(Calendar.MONTH);
	        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
	        cal.setTime(birthDay);

	        int yearBirth = cal.get(Calendar.YEAR);
	        int monthBirth = cal.get(Calendar.MONTH);
	        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

	        int age = yearNow - yearBirth;

	        if (monthNow <= monthBirth) {
	            if (monthNow == monthBirth) {
	                //monthNow==monthBirth
	                if (dayOfMonthNow < dayOfMonthBirth) {
	                    age--;
	                } else {
	                    //do nothing
	                }
	            } else {
	                //monthNow>monthBirth
	                age--;
	            }
	        } else {
	            //monthNow<monthBirth
	            //donothing
	        }
	        return Integer.valueOf(age).toString();
	    }
}

package com.auto.util.code;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 单值代码
 * @author gangtaoyu
 * @version 1.0
 *
 */
public class Code {
    
    private static final Log log = LogFactory.getLog(Code.class);
    
    /**
     * 代码值转换成名称
     * @param code
     * @return
     */
    public static String code2str(Class<? extends ICodeType> codeType , Integer code){
        ICodeType[] codes = codeType.getEnumConstants();
        for (ICodeType c : codes) {
            if (c.getValue().compareTo(code) == 0) {
                return c.getName();
            }
        }

        CodeTypeName codeTypeName = codeType.getAnnotation(CodeTypeName.class);
        if (codeTypeName != null) {
            log.warn("没有找到【" + codeTypeName.name() + "】相应的代码值[" + code + "]！");
        }
        return "[code:" + code + "is null]";
    }
    
    /**
     * 根据代码值，得到代码的枚举类型
     * @param type
     * @return
     */
    public static ICodeType code2type(Class<? extends ICodeType> codeType , Integer code){
        ICodeType[] codes = codeType.getEnumConstants();
        for (ICodeType c : codes) {
            if (c.getValue().compareTo(code) == 0) {
                return c;
            }
        }
        
        CodeTypeName codeTypeName = codeType.getAnnotation(CodeTypeName.class);
        if (codeTypeName != null) {
            log.warn("没有找到【" + codeTypeName.name() + "】相应的代码值[" + code + "]！");
        }
        return null;
    }
    
    /**
     * 根据代码值，得到代码的枚举类型
     * @param name
     * @return
     */
    public static ICodeType name2Type(Class<? extends ICodeType> codeType , String name){
        ICodeType[] codes = codeType.getEnumConstants();
        for (ICodeType c : codes) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        
        CodeTypeName codeTypeName = codeType.getAnnotation(CodeTypeName.class);
        if (codeTypeName != null) {
            log.warn("没有找到【" + codeTypeName.name() + "】相应的代码名称[" + name + "]！");
        }
        return null;
    }
    
    /**
     * 把某种类型的代码封装成list
     * @param codeType
     * @return
     */
    public static List<Map<String,String>> codes2List(Class<? extends Enum<?>> codeType){
        List<Map<String,String>> lst=new ArrayList<Map<String,String>>();
        Enum<?>[] codes = codeType.getEnumConstants();
        Field[] fields =codeType.getDeclaredFields();
        for (Enum<?> c : codes) {
            c.getDeclaringClass();
            Map<String,String> map=new HashMap<String,String>();
            for(Field f :fields){
                if(!f.isEnumConstant()){
                    String fieldValue="";
                    try {
                        f.setAccessible(true);
                        fieldValue=f.get(c).toString();
                    } catch (IllegalArgumentException e) {
                        log.warn("代码【" + c.name() + "】取值异常！");
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        log.warn("代码【" + c.name() + "】取值异常！");
                        e.printStackTrace();
                    }
                    map.put(f.getName(), fieldValue);
                }
            }
            lst.add(map);
        }
        return lst;
    }
    
    /**
     * 产品代码
     * 0：SAA, 1：车商宝,2：违章查询,3：中石化,4：手机客户端,5:批量导入，6：车展短信，100所有产品
     */
    @CodeTypeName(name="产品代码")
    @SuppressWarnings("deprecation")
    public enum ProductType implements ICodeType{
    	ALL("全站会员",100,new Date(2003-1900,0,1)),
        SAA("论坛",0,new Date(2003-1900,0,14)),              
        CSB("车商宝",1,new Date(2009-1900,5,5)), //2009-06-05 17:03:13 2008-03-31 16:16:19
        WGCX("违章查询web",2,new Date(2011-1900,10,1)),
        ZSH("中石化服务",3,new Date(2011-1900,10,3)),
        SJKHD("手机客户端",4,new Date(2011-1900,10,8)),
        PLDR("批量导入",5,new Date(2011-1900,11,22)),
        CZDX("车展短信",6,new Date(2012-1900,3,13)),
        MCB("买车宝",12,new Date(2012-1900,5,1)),
        ZT("专题",13,new Date(2012-1900,7,6)),
    	GRZX("个人中心",15,new Date(2012-1900,7,6)),
    	WZCX_ANDROID("违章查询android",4001,new Date(2012-1900,7,6)),
    	WZCX_IPHONE("违章查询iphone",4002,new Date(2012-1900,7,6)),
    	JYEZ_ANDROID("加油e站android",4003,new Date(2012-1900,7,6)),
    	JYEZ_IPHONE("加油e站iphone",4004,new Date(2012-1900,7,6)),
    	MCB_ANDROID("买车宝android",4005,new Date(2012-1900,7,6)),
    	MCB_IPHONE("买车宝iphone",4006,new Date(2012-1900,7,6)),
        
    	SHQC_ANDROID("搜狐汽车android",4007,new Date(2012-1900,7,6)),
    	SHQC_IPHONE("搜狐汽车iphone",4008,new Date(2012-1900,7,6)),
    	ESC_ANDROID("二手车android",4009,new Date(2012-1900,7,6)),
    	ESC_IPHONE("二手车iphone",4010,new Date(2012-1900,7,6));
    	
        private String name;//代码对应的名称
        private Integer value;//代码对应的值
        private Date onlineTime;//产品上线日期
        
        ProductType(String name,Integer value,Date onlineTime){
            this.name=name;
            this.value=value;
            this.onlineTime=onlineTime;
        }
        public String getName(){
            return this.name;
        }
        public Date getOnlineTime() {
            return onlineTime;
        }
        public Integer getValue() {
            return value;
        }        
        
        /**
         * 根据代码值，得到代码的枚举类型
         * @deprecated {@link Code#code2type(Class, Integer) reference Code }
         * @param type
         * @return
         */
        public static ProductType parseType(String type){
            if(StringUtils.isNotBlank(type)){
                int value=Integer.valueOf(type).intValue();
                ProductType[] codes = ProductType.values();
                for (ProductType c : codes) {
                    if (c.getValue().compareTo(value) == 0) {
                        return c;
                    }
                }
            }
            return ProductType.ALL;
        }
        
        /**
         * 获取代码值字符串
         * @deprecated {@link #getValue() reference getValue()}
         */
        public String toString(){
            return this.getValue().toString();
        }
    }
    
    /**
     * 性别代码
     * 0：男, 1：女
     */
    @CodeTypeName(name="性别代码")
    public enum Sex implements ICodeType{
        MALE("男",0),
        FEMALE("女",1);

        private String name;
        private Integer value;
        
        Sex(String name,Integer value){
            this.name=name;
            this.value=value;
        }        
        @Override
        public String getName(){
            return this.name;
        }
        @Override
        public Integer getValue() {
            return value;
        }
    }
    
    /**
     * 手机认证
     * 0：手机认证会员, 1：非手机认证会员
     */
    @CodeTypeName(name="手机认证代码")
    public enum Phoneauth implements ICodeType{
        YES("手机认证会员",0),
        NO("非手机认证会员",1);

        private String name;
        private Integer value;
        
        Phoneauth(String name,Integer value){
            this.name=name;
            this.value=value;
        }        
        @Override
        public String getName(){
            return this.name;
        }
        @Override
        public Integer getValue() {
            return value;
        }
    }
    
    
    /**
     * 是否代码
     * 1：是, 2：否
     */
    @CodeTypeName(name="是否代码")
    public enum yesOrNo implements ICodeType{
        yes("是",1),
        no("否",2);

        private String name;
        private Integer value;
        
        yesOrNo(String name,Integer value){
            this.name=name;
            this.value=value;
        }        
        @Override
        public String getName(){
            return this.name;
        }
        @Override
        public Integer getValue() {
            return value;
        }
    }
    
    /**
     * 是否已处理
     * 1：已处理, 2：未处理
     */
    @CodeTypeName(name="是否已处理代码")
    public enum DealState implements ICodeType{
        already("已处理",1),
        unready("未处理",2),
        ALL("全部",100);

        private String name;
        private Integer value;
        
        DealState(String name,Integer value){
            this.name=name;
            this.value=value;
        }        
        @Override
        public String getName(){
            return this.name;
        }
        @Override
        public Integer getValue() {
            return value;
        }
    }
    
    
}

package com.auto.util.code;

/**
 * 所有的代码都实现此接口
 * ICode
 * @author gangtaoyu
 * @version 1.0
 *
 */
public interface ICodeType {

    /**
     * 代码名称
     * @return
     */
    public String getName();
    
    /**
     * 代码值
     * @return
     */
    public Integer getValue();

}

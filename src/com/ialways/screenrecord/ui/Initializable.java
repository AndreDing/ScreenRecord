package com.ialways.screenrecord.ui;

import android.content.Context;

/**
 * 项目名称：ScreenRecord    
 * 类名称：Initializable    
 * 类描述： 需要通过MainApp初始化的类均需继承此方法
 * 创建人：dingchao    
 * 创建时间：2015年10月22日 下午1:39:03    
 * 修改人：dingchao    
 * 修改时间：2015年10月22日 下午1:39:03    
 * 修改备注：    
 * @version 1.0 
 *
 */
public interface Initializable {

    public void init(Context ctx);
}

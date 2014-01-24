package com.kongur.monolith.socket.mina;




/**
 * 
 * 报文请求
 * 
 * @author zhengwei
 *
 */
public interface Request {
    
    String getTransCode();

	/**
	 * 设置交易代码
	 * 
	 * @return
	 */
	void setTransCode(String transCode);
}

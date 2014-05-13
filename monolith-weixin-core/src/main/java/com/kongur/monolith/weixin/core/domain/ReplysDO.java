package com.kongur.monolith.weixin.core.domain;

import java.util.List;

/**
 * 多条回复记录
 * 
 * @author zhengwei
 * @date 2014-2-20
 */
public class ReplysDO {

	private List<ReplyDO> replys;

	public ReplysDO() {

	}

	public ReplysDO(List<ReplyDO> replyList) {
		this.replys = replyList;
	}

	public int count() {
		return this.replys != null ? this.replys.size() : 0;
	}

	public List<ReplyDO> getReplys() {
		return replys;
	}

	public void setReplys(List<ReplyDO> replys) {
		this.replys = replys;
	}

	public void addReply(ReplyDO repaly) {
		this.replys.add(repaly);
	}

}

package com.framework.dao;

import java.util.List;
import java.util.Map;

import com.framework.entity.TCarLeaseEntity;

/**
 * 
 * 
 * @author R & D
 * @email 908350381@qq.com
 * @date 2018-05-22 21:25:48
 */
public interface TCarLeaseDao extends BaseDao<TCarLeaseEntity> {
	
	public List<TCarLeaseEntity> queryMobileTerminalList(Map<String, Object> map);
	
	public List<TCarLeaseEntity> queryPCTerminalList(Map<String, Object> map);
	
	int queryLeaseCarTotal(Map<String, Object> map);
	
	public List<TCarLeaseEntity> queryPCTerminalByTypeList(Map<String, Object> map);
	
	int queryLeaseCarByTypeTotal(Map<String, Object> map);
}

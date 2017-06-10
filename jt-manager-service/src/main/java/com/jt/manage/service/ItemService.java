package com.jt.manage.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.vo.SysResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;

@Service
public class ItemService extends BaseService<Item>{
@Autowired
private ItemMapper itemMapper;
@Autowired
private ItemDescMapper itemDescMapper;

public List<Item> findItemByPageInfo(int page, int rows) {
	Map<String,Integer> pageInfo=new HashMap<>();
	pageInfo.put("startNum", (page-1)*rows);
	pageInfo.put("num", rows);
	return itemMapper.findItemByPageInfo(pageInfo);
}

public int selectCountItem() {
	// TODO Auto-generated method stub
	return itemMapper.selectCountItem();
}
//后台商品新增
public SysResult savaItem(Item item,String desc){
	try {
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		itemMapper.insertSelective(item);
		ItemDesc itemDesc=new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(itemDesc.getCreated());
		itemDescMapper.insertSelective(itemDesc);
		return SysResult.ok(null);
	} catch (Exception e) {
		return SysResult.build(201, "商品新增失败");
	}
	
}

public SysResult updateItem(Item item) {
	// TODO Auto-generated method stub
	item.setUpdated(new Date());
	itemMapper.updateByPrimaryKeySelective(item);
	return SysResult.ok(null);
}

public SysResult deleteItem(String[] id) {
	try {
		 itemMapper.deleteByIDS(id);
			return SysResult.ok();
	} catch (Exception e) {
	return 	SysResult.build(2011, "删除商品失败");
	}
	

}

public SysResult itemDesc(Long itemId) {
	// TODO Auto-generated method stub
ItemDesc itemDesc = itemDescMapper.selectById(itemId);
	return SysResult.ok(itemDesc);
}

public Item findItemById(long itemId) {
	// TODO Auto-generated method stub
	return itemMapper.selectByPrimaryKey(itemId);
}

public ItemDesc getItemDesc(long itemId) {
 return	itemDescMapper.selectById(itemId);
	
}
}

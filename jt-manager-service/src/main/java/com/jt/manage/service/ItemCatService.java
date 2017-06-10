package com.jt.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.ItemCat;
@Service
public class ItemCatService extends BaseService<ItemCat>{
@Autowired
ItemCatMapper itemCatMapper;
	public List<ItemCat> findItemCatList(long id) {
		// TODO Auto-generated method stub
		return itemCatMapper.findItemCatList(id);
	}
	
	public ItemCatResult itemCatJsonp() {
		List<ItemCatData> icd_v1_list=new ArrayList<>();
		//①封装一级商品分类的json，一级分类的特点是，parent_id=0
		//查询所有商品分类
		
		List<ItemCat> itemCatList=itemCatMapper.select(null);
		
		for(ItemCat itemCat_v1:itemCatList){
			//如果parent_id=0，证明是一级商品分离
			if(itemCat_v1.getParentId()==0){
				ItemCatData icd_v1=new ItemCatData();
				icd_v1.setUrl("/products/"+itemCat_v1.getId()+".html");
				icd_v1.setName("<a href='/products/"+itemCat_v1.getId()+".html'>"+itemCat_v1.getName()+"</a>");
				//这个集合用于封装二级商品分类的json对象的集合
				List<ItemCatData> icd_v2_list=new ArrayList<>();
				icd_v1.setItems(icd_v2_list);
				icd_v1_list.add(icd_v1);
				
				List<ItemCat> itemCat_v2_list=
						itemCatMapper.findItemCatByParentid(itemCat_v1.getId());
				
				for(ItemCat itemCat_v2:itemCat_v2_list){
					//{"u":"/products/2.html",n:"电子书刊"
					ItemCatData icd_v2=new ItemCatData();
					icd_v2.setUrl("/products/"+itemCat_v2.getId()+".html");
					icd_v2.setName(itemCat_v2.getName());
					List<String> icd_v3_list=new ArrayList<>();
					icd_v2.setItems(icd_v3_list);
					icd_v2_list.add(icd_v2);
					
					List<ItemCat> itemCat_v3_list=
							itemCatMapper.findItemCatByParentid(itemCat_v2.getId());
					//"/products/4.html|网络原创","/products/5.html|数字杂志"
					for(ItemCat itemCat_v3:itemCat_v3_list){
						icd_v3_list.add("/products/"+itemCat_v3.getId()+".html|"+itemCat_v3.getName());
					}
					
				}
				
			}
		}
		
		ItemCatResult result=new ItemCatResult();
		result.setItemCats(icd_v1_list);
		return result;
	}
	/*public ItemCatResult getWebItemCatAll(){  	
	        ItemCatResult result = new ItemCatResult();
	        // 全部查出，并且在内存中生成树形结构
	        List<ItemCat> cats = super.queryAll();

	        // 转为map存储，key为父节点ID，value为数据集合
	        Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
	        for (ItemCat itemCat : cats) {
	            if (!itemCatMap.containsKey(itemCat.getParentId())) {
	                itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
	            }
	            itemCatMap.get(itemCat.getParentId()).add(itemCat);
	        }

	        // 封装一级对象
	        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
	        for (ItemCat itemCat : itemCatList1) {
	            ItemCatData itemCatData = new ItemCatData();
	            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
	            itemCatData.setName("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");
	            result.getItemCats().add(itemCatData);
	            if (!itemCat.getIsParent()) {
	                continue;
	            }

	            // 封装二级对象
	            List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
	            List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
	            itemCatData.setItems(itemCatData2);
	            for (ItemCat itemCat2 : itemCatList2) {
	                ItemCatData id2 = new ItemCatData();
	                id2.setName(itemCat2.getName());
	                id2.setUrl("/products/" + itemCat2.getId() + ".html");
	                itemCatData2.add(id2);
	                if (itemCat2.getIsParent()) {
	                    // 封装三级对象
	                    List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
	                    List<String> itemCatData3 = new ArrayList<String>();
	                    id2.setItems(itemCatData3);
	                    for (ItemCat itemCat3 : itemCatList3) {
	                        itemCatData3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
	                    }
	                }
	            }
	            if (result.getItemCats().size() >= 14) {
	                break;
	            }
	        }

	        return result;
	    
		
	}
*/
}

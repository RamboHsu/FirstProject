package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 */
public interface BrandService {
    /**
     * 查询所有品牌
     * @return
     */
    List<TbBrand> findAll();

    /**
     * 品牌分页
     * @param pageNum 当前页面
     * @param pageSize 每页记录数
     * @return
     */
/*
    public PageResult findPage(Integer pageNum,Integer pageSize);
*/

    /**
     * 添加品牌
     * @param brand
     */
    public void add(TbBrand brand);

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    public TbBrand findOne(Long id);

    /**
     * 更新品牌
     * @param brand
     */
    public void update(TbBrand brand);

    /**
     * 删除
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     *
     * @param brand
     * @param pageNum
     * @param pageSize
     * @return
     */
   public PageResult findPage(TbBrand brand,Integer pageNum,Integer pageSize);

    /**
     * 返回下拉列表数据
     * @return
     */
   public List<Map> selectOptionList();

}

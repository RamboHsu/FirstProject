package com.pinyougou.sellergoods.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbBrandMapper brandMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbSellerMapper sellerMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        goods.getGoods().setAuditStatus("0");//设置未申请状态
        goodsMapper.insert(goods.getGoods());//插入商品基本信息
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());//设置id
        goodsDescMapper.insert(goods.getGoodsDesc());//插入商品扩展信息

        saveItemList(goods);//插入商品 SKU 列表数据
    }


    /**
     * 修改
     */
    @Override
    public void update(Goods goods) {
        goods.getGoods().setAuditStatus("0");//设置未申请状态：如果是经过修改的商品，需要重新设置状态
        goodsMapper.updateByPrimaryKey(goods.getGoods());//更新商品表
        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());//更新商品扩展表

        //删除原有SKU列表
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goods.getGoods().getId());
        itemMapper.deleteByExample(example);

        //添加新的SKU列表数据
        saveItemList(goods);//插入商品 SKU 列表数据
    }



    //增加功能中提取重复代码创建的私有方法****************************************************
    private void setItemValues(TbItem item,Goods goods){
        //商品分类
        item.setGoodsId(goods.getGoods().getId());//商品SPU编号
        item.setSellerId(goods.getGoods().getSellerId());//商家id
        item.setCategoryid(goods.getGoods().getCategory3Id());//商品分类编号3级
        item.setCreateTime(new Date());//创建日期
        item.setUpdateTime(new Date());//修改日期
        //品牌名称
        TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        item.setBrand(brand.getName());
        //分类名称
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());
        //商家名称
        TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        item.setSeller(seller.getNickName());
        //图片地址
        List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (imageList.size() > 0) {
            item.setImage((String) imageList.get(0).get("url"));
        }
    }

    //将 SKU 列表插入的代码提取出来，封装到私有方法中**********************************************
    private void saveItemList(Goods goods){
        if("1".equals(goods.getGoods().getIsEnableSpec())){
            for (TbItem item : goods.getItemList()) {
                String title = goods.getGoods().getGoodsName();
                Map<String, Object> specMap = JSON.parseObject(item.getSpec());
                for (String key : specMap.keySet()) {
                    title += " " + specMap.get(key);
                }
                item.setTitle(title);
                //调用重复代码部分的私有方法
                setItemValues(item,goods);
                itemMapper.insert(item);
            }
        }else {//没有启用规格
            TbItem item = new TbItem();
            item.setTitle(goods.getGoods().getGoodsName());//标题
            item.setPrice(goods.getGoods().getPrice());//价格
            item.setNum(9999);//库存数量
            item.setStatus("1");//状态
            item.setIsDefault("1");//是否设置为默认
            item.setSpec("{}");//规格
            //调用重复代码部分的私有方法
            setItemValues(item,goods);
            itemMapper.insert(item);
        }
    }


    /**
     * 根据ID获取实体
     * @param id
     * @return
     */
    @Override
    public Goods findOne(Long id) {
        Goods goods = new Goods();
        //商品基本列表
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        goods.setGoods(tbGoods);
        //商品扩展列表
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setGoodsDesc(tbGoodsDesc);
        //读取SKU列表
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);//查询条件:商品id
        List<TbItem> itemList = itemMapper.selectByExample(example);
        goods.setItemList(itemList);

        return goods;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            TbGoods goods = goodsMapper.selectByPrimaryKey(id);
            goods.setIsDelete("1");
            goodsMapper.updateByPrimaryKey(goods);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeleteIsNull();//非删除状态

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
//                criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }
        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改状态
     * @param ids
     * @param status
     */
    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id : ids) {
            TbGoods goods = goodsMapper.selectByPrimaryKey(id);
            goods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(goods);
        }
    }

    /**
     *  根据商品id和状态查询Item信息表
     * @param goodsId
     * @param status
     * @return
     */
    @Override
    public List<TbItem> findItemListByGoodsIdSAndStatus(Long[] goodsId, String status) {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdIn(Arrays.asList(goodsId));
        criteria.andStatusEqualTo(status);

        return itemMapper.selectByExample(example);
    }


}

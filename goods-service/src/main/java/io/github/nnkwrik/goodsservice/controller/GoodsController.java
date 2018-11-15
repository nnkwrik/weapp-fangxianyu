package io.github.nnkwrik.goodsservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.nnkwrik.goodsservice.dao.GoodsMapper;
import io.github.nnkwrik.goodsservice.model.po.Category;
import io.github.nnkwrik.goodsservice.model.po.Channel;
import io.github.nnkwrik.goodsservice.model.vo.CatalogVo;
import io.github.nnkwrik.goodsservice.model.vo.IndexVO;
import io.github.nnkwrik.goodsservice.model.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nnkwrik
 * @date 18/11/14 18:42
 */
@RestController
public class GoodsController {

    @Autowired
    private GoodsMapper goodsMapper;

    //    @GetMapping("/index/index")
    public String test() throws JsonProcessingException {

//          newGoods: res.data.newGoodsList,
//          hotGoods: res.data.hotGoodsList,
//          topics: res.data.topicList,
//          brand: res.data.brandList,
//          floorGoods: res.data.categoryList,
//          banner: res.data.banner,
//          channel: res.data.channel

        Map<String, Object> json = new HashMap<>();

        Map<String, Object> data = new HashMap<>();

        String img = "https://nnkwrik.github.io/2018/11/12/20181112/1541980429999.png";

        List<Map> newGoodsList = new ArrayList<>();
        Map<String, Object> newGoodsListIn = new HashMap<>();
        newGoodsList.add(newGoodsListIn);

        newGoodsListIn.put("id", 1);
        newGoodsListIn.put("list_pic_url", img);
        newGoodsListIn.put("name", "newGoods");
        newGoodsListIn.put("retail_price", 100);


        Map<String, Object> hotGoodsList = new HashMap<>();
        hotGoodsList.put("id", 1);
        hotGoodsList.put("list_pic_url", img);
        hotGoodsList.put("name", "hotGoodsList");
        hotGoodsList.put("retail_price", 100);
        hotGoodsList.put("goods_brief", "热门铲平");

        Map<String, Object> categoryList = new HashMap<>();
        Map<String, Object> categoryListIn = new HashMap<>();
        categoryList.put("categoryList", categoryListIn);

        categoryListIn.put("id", 1);
        categoryListIn.put("list_pic_url", img);
        categoryListIn.put("name", "categoryListIn");
        categoryListIn.put("retail_price", 100);

        data.put("newGoodsList", newGoodsList);
        data.put("hotGoodsList", hotGoodsList);
        data.put("categoryList", categoryList);

        json.put("data", data);
        String res = new ObjectMapper().writeValueAsString(json);
//        System.out.println(res);

//        "{\"errno\":0," +
//                "\"errmsg\":\"\"," +
//                "\"data\":{" +
//                "\"banner\":[{\"id\":1,\"ad_position_id\":1,\"media_type\":1,\"name\":\"合作 谁是你的菜\",\"link\":\"\",\"image_url\":\"http://yanxuan.nosdn.127.net/65091eebc48899298171c2eb6696fe27.jpg\",\"content\":\"合作 谁是你的菜\",\"end_time\":0,\"enabled\":1}," +
//                "{\"id\":2,\"ad_position_id\":1,\"media_type\":1,\"name\":\"活动 美食节\",\"link\":\"\",\"image_url\":\"http://yanxuan.nosdn.127.net/bff2e49136fcef1fd829f5036e07f116.jpg\",\"content\":\"活动 美食节\",\"end_time\":0,\"enabled\":1}," +
//                "{\"id\":3,\"ad_position_id\":1,\"media_type\":1,\"name\":\"活动 母亲节\",\"link\":\"\",\"image_url\":\"http://yanxuan.nosdn.127.net/8e50c65fda145e6dd1bf4fb7ee0fcecc.jpg\",\"content\":\"活动 母亲节\",\"end_time\":0,\"enabled\":1}]," +
//                "\"channel\":[{\"id\":1,\"name\":\"居家\",\"url\":\"/pages/category/category?id=1005000\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/c031ea3cf575f885cd1c.png\",\"sort_order\":1}," +
//                "{\"id\":2,\"name\":\"餐厨\",\"url\":\"/pages/category/category?id=1005001\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/4fbe8913819b017ebe1b.png\",\"sort_order\":2}," +
//                "{\"id\":3,\"name\":\"配件\",\"url\":\"/pages/category/category?id=1008000\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/e8070853e6c6f5627713.png\",\"sort_order\":3}," +
//                "{\"id\":4,\"name\":\"服装\",\"url\":\"/pages/category/category?id=1005002\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/4fa3c0c72964901c5a45.png\",\"sort_order\":4}," +
//                "{\"id\":5,\"name\":\"志趣\",\"url\":\"/pages/category/category?id=1019000\",\"icon_url\":\"http://ac-"

        String a = "{\"errno\":0," +
                "\"errmsg\":\"\"," +
                "\"data\":{" +
                "\"newGoodsList\":[{\"id\":1,\"name\":\"居家\",\"list_pic_url\":\"" + img + "\",\"retail_price\":100}," +
                "{\"id\":1,\"name\":\"居家\",\"list_pic_url\":\"" + img + "\",\"retail_price\":100}]," +
                "\"hotGoodsList\":[{\"id\":1,\"name\":\"居家\",\"list_pic_url\":\"" + img + "\",\"retail_price\":100,\"goods_brief\":\"热门铲平\"}," +
                "{\"id\":1,\"name\":\"居家\",\"list_pic_url\":\"" + img + "\",\"retail_price\":100,\"goods_brief\":\"热门铲平\"}]," +
                "\"categoryList\":[{\"id\":1,\"name\":\"居家\",\"list_pic_url\":\"" + img + "\",\"retail_price\":100}," +
                "{\"id\":2,\"name\":\"居家\",\"list_pic_url\":\"" + img + "\",\"retail_price\":100}]," +
                "\"topicList\":[{\"id\":1,\"title\":\"居家\",\"scene_pic_url\":\"" + img + "\",\"price_info\":100,\"subtitle\":\"热门铲平\"}," +
                "{\"id\":1,\"title\":\"居家\",\"scene_pic_url\":\"" + img + "\",\"price_info\":100,\"subtitle\":\"热门铲平\"}]," +
                "\"brandList\":[{\"id\":1,\"name\":\"居家\",\"new_pic_url\":\"" + img + "\",\"floor_price\":100}," +
                "{\"id\":1,\"name\":\"居家\",\"new_pic_url\":\"" + img + "\",\"floor_price\":100}]," +
                "\"channel\":[{\"id\":1,\"name\":\"居家\",\"url\":\"/pages/category/category?id=1005000\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/c031ea3cf575f885cd1c.png\",\"sort_order\":1}," +
                "{\"id\":2,\"name\":\"餐厨\",\"url\":\"/pages/category/category?id=1005001\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/4fbe8913819b017ebe1b.png\",\"sort_order\":2}," +
                "{\"id\":3,\"name\":\"配件\",\"url\":\"/pages/category/category?id=1008000\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/e8070853e6c6f5627713.png\",\"sort_order\":3}," +
                "{\"id\":2,\"name\":\"餐厨\",\"url\":\"/pages/category/category?id=1005001\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/4fbe8913819b017ebe1b.png\",\"sort_order\":2}," +
                "{\"id\":3,\"name\":\"配件\",\"url\":\"/pages/category/category?id=1008000\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/e8070853e6c6f5627713.png\",\"sort_order\":3}," +
                "{\"id\":2,\"name\":\"餐厨\",\"url\":\"/pages/category/category?id=1005001\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/4fbe8913819b017ebe1b.png\",\"sort_order\":2}," +
                "{\"id\":3,\"name\":\"配件\",\"url\":\"/pages/category/category?id=1008000\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/e8070853e6c6f5627713.png\",\"sort_order\":3}," +
                "{\"id\":4,\"name\":\"服装\",\"url\":\"/pages/category/category?id=1005002\",\"icon_url\":\"http://ac-3yr0g9cz.clouddn.com/4fa3c0c72964901c5a45.png\",\"sort_order\":4}]," +
                "\"banner\":[{\"id\":1,\"ad_position_id\":1,\"media_type\":1,\"name\":\"合作 谁是你的菜\",\"link\":\"\",\"image_url\":\"" + img + "\",\"content\":\"合作 谁是你的菜\",\"end_time\":0,\"enabled\":1}," +
                "{\"id\":1,\"ad_position_id\":1,\"media_type\":1,\"name\":\"合作 谁是你的菜\",\"link\":\"\",\"image_url\":\"" + img + "\",\"content\":\"合作 谁是你的菜\",\"end_time\":0,\"enabled\":1}]" +
                "}}";


        return a;

//        Map<String,Object>  channel = new HashMap<>();
//
//
//
//        Map<String,Object>  topicList = new HashMap<>();
//        Map<String,Object>  brandList = new HashMap<>();
//        Map<String,Object>  banner = new HashMap<>();

    }

    @GetMapping("/index/index")
    public ResponseVO<IndexVO> index() {

        List<Channel> channelList = goodsMapper.findChannel();
        IndexVO indexVO = IndexVO.createFromPo(channelList);
//        indexVO.setChannel(channelList);

        System.out.println(indexVO);

        return ResponseVO.ok(indexVO);
    }

    @GetMapping("/catalog/index")
    public ResponseVO<CatalogVo> catalog() {

//        navList: [], res.data.categoryList, List[id,name]
//        categoryList: [],
//        currentCategory: {}, wap_banner_url,front_name,name,subCategoryList[id,name,wap_banner_url,cat_id]
//
//        navList:
//                currentCategory:
//goodsList id list_pic_url name retail_price

        List<Category> mainCategory = goodsMapper.findMainCategory();
        List<Category> subCategory = goodsMapper.findSubCategory(mainCategory.get(0).getId());

        CatalogVo vo = CatalogVo.createFromPo(mainCategory,subCategory);


//        Category currentCategory = new Category();
//        currentCategory.setId(mainCategory.get(0).getId());
//        currentCategory.setName(mainCategory.get(0).getName());
//
//
//        List<CategoryVo> categoryList = po2voList(mainCategory);
//        List<CategoryVo> subCategoryVo = po2voList(subCategory);
//
//        CategoryVo currentCategory = new CategoryVo();
//        currentCategory.setId(categoryList.get(0).getId());
//        currentCategory.setName(categoryList.get(0).getName());
//        currentCategory.setSubCategoryList(subCategory);
//
//        CatalogVo vo = new CatalogVo();
//        vo.setCategoryList(categoryList);
//        vo.setCurrentCategory(currentCategory);


        System.out.println(vo);

        return ResponseVO.ok(vo);
    }

    @GetMapping("/catalog/{id}")
    public ResponseVO<CatalogVo> subCatalog(@PathVariable("id") int id){
        List<Category> subCategory = goodsMapper.findSubCategory(id);
//        List<CategoryVo> subCategoryVo = po2voList(subCategory);
//        Category currentCategory = goodsMapper.findCategoryById(id);
//        CategoryVo currentCategoryVo = PO2VO.Category.apply(currentCategory);
//
//
//        currentCategoryVo.setSubCategoryList(subCategoryVo);
//        vo.setCurrentCategory(currentCategoryVo);
        CatalogVo vo = CatalogVo.createFromPo(subCategory);

        return ResponseVO.ok(vo);
    }

//    private List<CategoryVo> po2voList(List<Category> poList){
//        return poList.stream()
//                .map(PO2VO.Category).collect(Collectors.toList());
//    }




}

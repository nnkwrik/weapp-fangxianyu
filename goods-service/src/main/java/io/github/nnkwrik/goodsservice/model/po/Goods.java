package io.github.nnkwrik.goodsservice.model.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nnkwrik
 * @date 18/11/16 19:07
 */
@Data
public class Goods {
    private Integer id;
    private Integer categoryId;
    private String sellerId;
    private String name;
    private Double price;
    private Double marketPrice;
    private String primaryPicUrl;
    private String desc;
    private Integer wantCount;
    private Integer browseCount;
    private Boolean isSelling;
    private Boolean isDelete;
    private Date lastEdit;

    private Integer regionId;
    private String region;

    private Double postage;

    private Boolean ableSelfTake;
    private Boolean ableMeet;
    private Boolean ableExpress;

    private String buyerId;
    private Date soldTime;
}

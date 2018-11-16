package io.github.nnkwrik.goodsservice.model.vo.inner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/16 15:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVo {
    private Integer id;
    private String name;
    private String wap_banner_url;
    private List<CategoryVo> subCategoryList;
}

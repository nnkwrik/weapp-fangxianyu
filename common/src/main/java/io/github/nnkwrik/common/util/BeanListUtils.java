package io.github.nnkwrik.common.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nnkwrik
 * @date 18/11/30 16:28
 */
public class BeanListUtils {

    /**
     * BeanUtils.copyProperties()的List增强版
     * 对List的每个元素进行拷贝,生成target类型的List
     * @param sourceList
     * @param targetClazz 要生成的target List<T>的Class对象
     * @param <T>
     * @return
     */
    public static <T,R> List<R> copyListProperties(List<T> sourceList, Class<R> targetClazz) {
        List<R> result = new ArrayList<>();
        sourceList.stream().forEach(source -> {
            try {
                R target = targetClazz.newInstance();
                BeanUtils.copyProperties(source, target);
                result.add(target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }

}

package io.github.nnkwrik.goodsservice.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * po转vo
 * @author nnkwrik
 * @date 18/11/15 18:00
 */
public class PO2VO {

    /**
     * po转vo
     * @param po
     * @param voClass vo的类型
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R convert(T po, Class<R> voClass) {
        R vo = null;
        try {
            vo = voClass.newInstance();
            BeanUtils.copyProperties(po, vo);
            PO2VO.checkDate(po, vo);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return vo;
    }

    /**
     * poList转voList
     * @param poList
     * @param voClass vo的类型
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> convertList(List<T> poList, Class<R> voClass) {
        return poList.stream()
                .map(po -> PO2VO.convert(po, voClass)).collect(Collectors.toList());
    }


    /**
     * 把po中的Date对象转换成String,并对vo进行赋值
     *
     * @param po
     * @param vo
     * @param <T>
     * @param <R>
     */
    private static <T, R> void checkDate(T po, R vo) {
        BeanWrapper poWrapper = new BeanWrapperImpl(po);
        BeanWrapper voWrapper = new BeanWrapperImpl(vo);


        for (PropertyDescriptor pd : poWrapper.getPropertyDescriptors()) {
            Object date;
            if (pd.getPropertyType() == Date.class
                    && (date = poWrapper.getPropertyValue(pd.getDisplayName())) != null) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String formatted = formatter.format(date);

                voWrapper.setPropertyValue(pd.getDisplayName(), formatted);

            }
        }
    }

}

package com.haizhi.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @author mtl
 * @Description:
 * @date 2020/4/28 17:16
 */
@Data
@ToString
public class TbItem {
    private String id;
    private String title;
    private String sellPoint;
    private String price;
    private String num;
    private String barcode;
    private String image;
    private String cid;
    private String status;
    private String created;
    private String updated;
}

package org.example.mq;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hujun
 * @version 1.0.0
 * @ClassName OrderStatusReq.java
 * @Description
 * @createTime 2021-01-06 15:13:00
 *         String msg = "{\"orderNo\":\"2101063633900111912\",\"productType\":10,\"status\":1015,\"subOrderNo\":\"2101063633900111912-00\",\"updateTime\":1609898743827}";
 */
@Data
@NoArgsConstructor
public class OrderStatusReq {
    private String subOrderNo;
    private String orderNo;
    private Integer status;
    private Long updateTime;
    private Integer productType;
    private Long activityId;
    private String statusDesc;
}

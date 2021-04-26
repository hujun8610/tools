package org.example.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author hujun
 * @version 1.0.0
 * @ClassName RightsJumpReq.java
 * @Description
 * @createTime 2021-01-27 10:39:00
 */
@Data
public class RightsJumpReq implements Serializable {
    private String url;
    private Map<String, String> headMap;
}

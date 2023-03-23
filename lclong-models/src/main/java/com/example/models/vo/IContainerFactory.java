
package com.example.models.vo;

import java.util.HashMap;
import java.util.Map;

public interface IContainerFactory {
    IContainerFactory defaultContainerFactory = new IContainerFactory() {
        public Map<String, Object> getAttrsMap() {
            return new HashMap();
        }
    };
    Map getAttrsMap();
}

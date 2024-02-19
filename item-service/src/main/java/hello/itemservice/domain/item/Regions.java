package hello.itemservice.domain.item;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Regions {
    private static final Map<String, String> regions = new LinkedHashMap<>();
    
    static {
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
    }

    public static Map<String, String> getRegions() {
        return regions;
    }
}
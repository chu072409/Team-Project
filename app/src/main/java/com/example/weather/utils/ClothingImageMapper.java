package com.example.weather.utils;

import com.example.weather.R;
import java.util.HashMap;
import java.util.Map;

public class ClothingImageMapper {

    private static final Map<String, Integer> subCategoryImageMap = new HashMap<>();

    static {
        // 🧥 아우터
        subCategoryImageMap.put("바람막이", R.drawable.a);
        subCategoryImageMap.put("레인자켓", R.drawable.b);
        subCategoryImageMap.put("두꺼운 패딩", R.drawable.c);
        subCategoryImageMap.put("롱패딩", R.drawable.d);
        subCategoryImageMap.put("얇은 자켓", R.drawable.e);
        subCategoryImageMap.put("얇은 방수 점퍼", R.drawable.b);//1
        subCategoryImageMap.put("레인코드", R.drawable.f);
        subCategoryImageMap.put("울코트", R.drawable.aa);//1
        subCategoryImageMap.put("야상자켓", R.drawable.g);

        // 👕 상의
        subCategoryImageMap.put("린넨 셔츠", R.drawable.a1);
        subCategoryImageMap.put("반팔 티셔츠", R.drawable.a2);
        subCategoryImageMap.put("민소매", R.drawable.a3);
        subCategoryImageMap.put("니트", R.drawable.a4);
        subCategoryImageMap.put("블라우스", R.drawable.a5);
        subCategoryImageMap.put("맨투맨", R.drawable.a6);
        subCategoryImageMap.put("히트텍", R.drawable.a7);
        subCategoryImageMap.put("긴팔셔츠", R.drawable.a8);
        subCategoryImageMap.put("얇은셔츠", R.drawable.a9);
        subCategoryImageMap.put("기모셔츠", R.drawable.a10);

        // 👖 하의
        subCategoryImageMap.put("반바지", R.drawable.b1);
        subCategoryImageMap.put("슬랙스", R.drawable.b2);
        subCategoryImageMap.put("청바지", R.drawable.b3);
        subCategoryImageMap.put("면바지", R.drawable.b4);
        subCategoryImageMap.put("롱스커트", R.drawable.b5);
        subCategoryImageMap.put("치마", R.drawable.b6);
        subCategoryImageMap.put("기모바지", R.drawable.b7);
        subCategoryImageMap.put("두꺼운레깅스", R.drawable.b8);
        subCategoryImageMap.put("긴바지", R.drawable.b9);
        subCategoryImageMap.put("트레이닝팬츠", R.drawable.b10);

        // 👟 신발
        subCategoryImageMap.put("샌들", R.drawable.c1);
        subCategoryImageMap.put("슬리퍼", R.drawable.c2);
        subCategoryImageMap.put("방수 운동화", R.drawable.c3);
        subCategoryImageMap.put("레인부츠", R.drawable.c4);
        subCategoryImageMap.put("부츠", R.drawable.c5);
        subCategoryImageMap.put("방한부츠", R.drawable.c6);
        subCategoryImageMap.put("밝은색운동화", R.drawable.c7);
        subCategoryImageMap.put("크록스", R.drawable.c8);
        subCategoryImageMap.put("통굽샌들", R.drawable.c9);
    }

    public static int getImageResId(String subCategory) {
        Integer resId = subCategoryImageMap.get(subCategory);
        return resId != null ? resId : R.drawable.placeholder;
    }
}

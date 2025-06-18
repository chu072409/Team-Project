package com.example.weather.recommend;

import com.example.weather.model.ClothingItem;
import com.example.weather.utils.ClothingImageMapper;

import java.util.List;
import java.util.Set;

public class OutfitRecommender {

    private static final Set<String> allowedSubCategories = Set.of(
            // 아우터
            "바람막이", "레인자켓", "두꺼운 패딩", "롱패딩", "얇은 자켓", "얇은 방수 점퍼",
            "레인코드", "울코트", "야상자켓",

            // 상의
            "린넨 셔츠", "반팔 티셔츠", "민소매", "니트", "블라우스", "맨투맨", "히트텍",
            "긴팔셔츠", "얇은셔츠", "기모셔츠",

            // 하의
            "반바지", "슬랙스", "청바지", "면바지", "롱스커트", "치마",
            "기모바지", "두꺼운레깅스", "긴바지", "트레이닝팬츠",

            // 신발
            "샌들", "슬리퍼", "방수 운동화", "레인부츠", "부츠",
            "방한부츠", "밝은색운동화", "크록스", "통굽샌들"
    );

    public static ClothingItem recommend(String category, int temperature, String weatherDesc, List<ClothingItem> userItems) {
        for (ClothingItem item : userItems) {
            if (item.getCategory().equals(category)
                    && allowedSubCategories.contains(item.getSubCategory())
                    && isSuitable(item.getSubCategory(), temperature, weatherDesc)) {
                return item;
            }
        }
        return getDefaultItem(category, temperature, weatherDesc);
    }

    private static boolean isSuitable(String sub, int temp, String weather) {
        sub = sub.trim();
        if (!allowedSubCategories.contains(sub)) return false;

        switch (sub) {
            // 아우터
            case "롱패딩": case "두꺼운 패딩": case "방한부츠": return temp <= 5;
            case "울코트": case "레인코드": case "야상자켓": return temp > 5 && temp <= 15;
            case "바람막이": case "얇은 방수 점퍼": case "레인자켓": return temp >= 10 && temp <= 20 && weather.contains("비");

            case "반팔 티셔츠": return temp >= 24;
            case "긴팔셔츠": case "맨투맨":  return temp >= 16 && temp < 24;
            case "히트텍": return temp < 5;
            case "니트": case "블라우스": case "린넨 셔츠": return temp >= 13 && temp <= 22;
            case "민소매": return temp >= 28;
            // 상의

            // 하의
            case "반바지": return temp >= 26;
            case "슬랙스": case "청바지": case "면바지": case "긴바지": case "트레이닝팬츠": return temp >= 15 && temp <= 25;
            case "기모바지": return  temp < 5;
                case "두꺼운레깅스": return temp <= 10;
            case "롱스커트": case "치마": return temp >= 20;

            // 신발
            case "방수 운동화": return temp > 5;
                case "밝은색운동화": return weather.contains("비") || temp >= 10;
            case "샌들": case "슬리퍼": case "통굽샌들": return temp >= 25 && weather.contains("맑음");
            case "레인부츠": return weather.contains("비") || weather.contains("소나기");
            case "부츠": return weather.contains("눈") || temp <= 5;
            case "크록스": return temp >= 20;

            default: return false;
        }
    }

    private static ClothingItem getDefaultItem(String category, int temp, String weather) {
        String subCategory;
        switch (category) {
            case "아우터":
                if (temp > 5) subCategory = "야상자켓";// 5<=
                else if (temp <= 15) subCategory = "울코트";
                else subCategory = weather.contains("비") ? "레인자켓" : "바람막이";
                break;
            case "상의":
                if (temp >= 28) subCategory = "민소매";
                else if (temp >= 24) subCategory = "반팔 티셔츠";
                else if (temp >= 16) subCategory = "맨투맨";
                else subCategory = "블라우스";
                break;
            case "하의":
                if (temp >= 26) subCategory = "반바지";
                else if (temp >= 20) subCategory = "슬랙스";
                else subCategory = "치마";
                break;
            case "신발":
                if (weather.contains("눈")) subCategory = "부츠";
                else if (weather.contains("비")) subCategory = "방수 운동화";
                else if (temp >= 25) subCategory = "샌들";
                else subCategory = "샌들";
                break;
            default:
                subCategory = "기본";
        }

        int imageResId = ClothingImageMapper.getImageResId(subCategory);
        return new ClothingItem(category, subCategory, imageResId, true);
    }
}

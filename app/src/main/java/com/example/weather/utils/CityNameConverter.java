
package com.example.weather.utils;

import java.util.HashMap;
import java.util.Map;
//test
public class CityNameConverter {

    private static final Map<String, String> cityMap = new HashMap<>();

    static {

        cityMap.put("Seoul", "서울");

        // 광역시
        cityMap.put("Busan", "부산"); cityMap.put("Incheon", "인천"); cityMap.put("Daegu", "대구");
        cityMap.put("Daejeon", "대전"); cityMap.put("Gwangju", "광주"); cityMap.put("Ulsan", "울산");

        // 특별자치시/도
        cityMap.put("Sejong", "세종"); cityMap.put("Jeju", "제주");

        // 경기도 주요 도시
        cityMap.put("Suwon", "수원"); cityMap.put("Goyang", "고양"); cityMap.put("Yongin", "용인");
        cityMap.put("Seongnam", "성남"); cityMap.put("Hwaseong", "화성"); cityMap.put("Bucheon", "부천");
        cityMap.put("Ansan", "안산"); cityMap.put("Namyangju", "남양주"); cityMap.put("Pyeongtaek", "평택");
        cityMap.put("Uijeongbu", "의정부"); cityMap.put("Siheung", "시흥"); cityMap.put("Gimpo", "김포");
        cityMap.put("Gwangmyeong", "광명"); cityMap.put("Icheon", "이천"); cityMap.put("Paju", "파주");
        cityMap.put("Anseong", "안성"); cityMap.put("Yangju", "양주"); cityMap.put("Osan", "오산");
        cityMap.put("Gunpo", "군포"); cityMap.put("Uiwang", "의왕"); cityMap.put("Hanam", "하남");
        cityMap.put("Guri", "구리"); cityMap.put("Pocheon", "포천"); cityMap.put("Dongducheon", "동두천");

        // 경상북도 주요 도시
        cityMap.put("Pohang", "포항"); cityMap.put("Gumi", "구미"); cityMap.put("Gyeongju", "경주");
        cityMap.put("Andong", "안동"); cityMap.put("Yeongju", "영주"); cityMap.put("Mungyeong", "문경");

        // 경상남도 주요 도시
        cityMap.put("Changwon", "창원"); cityMap.put("Gimhae", "김해"); cityMap.put("Jinju", "진주");
        cityMap.put("Yangsan", "양산"); cityMap.put("Tongyeong", "통영"); cityMap.put("Sacheon", "사천");

        // 충청북도 주요 도시
        cityMap.put("Cheongju", "청주"); cityMap.put("Chungju", "충주");

        // 충청남도 주요 도시
        cityMap.put("Cheonan", "천안"); cityMap.put("Asan", "아산"); cityMap.put("Seosan", "서산");

        // 전라북도 주요 도시
        cityMap.put("Jeonju", "전주"); cityMap.put("Gunsan", "군산"); cityMap.put("Iksan", "익산");
        cityMap.put("Wanju", "완주");

        // 전라남도 주요 도시
        cityMap.put("Mokpo", "목포"); cityMap.put("Yeosu", "여수"); cityMap.put("Suncheon", "순천");

        // 강원도 주요 도시
        cityMap.put("Gangneung", "강릉"); cityMap.put("Wonju", "원주"); cityMap.put("Chuncheon", "춘천");

        // 기타 주요 도시
        cityMap.put("Anyang", "안양"); cityMap.put("Jecheon", "제천"); cityMap.put("Gwangyang", "광양");
        cityMap.put("Miryang", "밀양"); cityMap.put("Yeongcheon", "영천");
    }

    public static String convert(String englishName) {
        return cityMap.getOrDefault(englishName, englishName);
    }
}

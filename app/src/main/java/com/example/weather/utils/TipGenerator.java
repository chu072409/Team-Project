package com.example.weather.utils;

public class TipGenerator {
    public static String generateTip(int temp, String desc) {
        if (desc.contains("맑음") && temp >= 20) return "자외선이 강하니 썬크림을 꼭 챙기세요!";
        else if (desc.contains("비") || desc.contains("소나기")) return "비가 오니 우산을 꼭 챙기세요.";
        else if (desc.contains("천둥") || desc.contains("번개")) return "천둥 번개 주의! 실외 활동 자제하세요.";
        else if (desc.contains("눈")) return "눈길 조심! 따뜻하게 입으세요.";
        else return "쾌적한 날씨입니다. 외출을 즐기세요!";
    }
}

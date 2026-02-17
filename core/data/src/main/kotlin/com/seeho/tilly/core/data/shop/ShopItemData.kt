package com.seeho.tilly.core.data.shop

import com.seeho.tilly.core.model.ItemCategory
import com.seeho.tilly.core.model.ShopItem

/**
 * 상점 아이템 로컬 데이터 (서버 없이 하드코딩)
 * imageResName은 core/designsystem 모듈의 drawable-nodpi 파일명과 일치해야 함
 */
object ShopItemData {

    /** 전체 상점 아이템 목록 */
    val allItems: List<ShopItem> = buildList {
        // ======== 기본 제공 (무료) ========
        // imageResName → drawable-nodpi에 실제 존재하는 파일명
        add(ShopItem("monitor_retro", "레트로 모니터", "화면보다 뒤통수가 더 튀어나온 감성.", ItemCategory.EQUIPMENT, 0, "gif_obj_basic_com", 1, isAnimated = true, isDefault = true))
        add(ShopItem("keyboard_basic", "보급형 멤브레인", "소음은 적지만 내 손가락은 고통받는다.", ItemCategory.KEYBOARD, 0, "obj_keyboard_basic", 1, isDefault = true))
        add(ShopItem("desk_basic", "흔들리는 기본 책상", "흔들리는 편안함.\n수평 맞추기용 종이 필수.", ItemCategory.DESK, 0, "bg_desk_basic", 1, isDefault = true))
        add(ShopItem("chair_basic", "딱딱한 기본 의자", "30분만 앉아도 엉덩이가 비명을 지른다.", ItemCategory.CHAIR, 0, "bg_chair_basic", 1, isDefault = true))

        // ======== 장비 (Tier 1~3) ========
        add(ShopItem("monitor_modern", "현대식 모니터", "코드 한줄이 더 보인다.\n시력 보호는 덤.", ItemCategory.EQUIPMENT, 500, "obj_com_modern", 3))

        // ======== 책상 (Tier 2~3) ========
        add(ShopItem("desk_modern", "오피스 책상", "깔끔한 화이트 책상.\n서랍엔 츄르가 가득하다.", ItemCategory.DESK, 600, "bg_desk_modern", 3))

        // ======== 의자 (Tier 2) ========
        add(ShopItem("chair_modern", "모던 체어", "드디어 앉을 만하다.\n깔끔함 그 자체.", ItemCategory.CHAIR, 300, "bg_chair_modern", 2))
        add(ShopItem("chair_gaming", "게이밍 체어", "나를 감싸는 포근함.\n하지만 게임이 하고싶어진다.", ItemCategory.CHAIR, 500, "bg_chair_gaming", 2))

        // ======== 장식 (Tier 1) ========
        add(ShopItem("deco_coffee", "뜨거운 커피", "개발자에게 커피는 필수.\n앗 뜨거워!", ItemCategory.DECORATION, 100, "item_coffee", 1))
        add(ShopItem("deco_duck", "러버덕", "내 코드를 가장 잘 이해하는 유일한 존재.\n오늘도 잘 부탁해.", ItemCategory.DECORATION, 150, "item_duck", 1))

        // ======== 테마 (Tier 4) ========
        add(ShopItem("theme_tilly_green", "Tilly Green", "검은건 배경이요, 초록은 글자로다.", ItemCategory.THEME, 0, "theme_tilly_green", 4, isDefault = true))
        add(ShopItem("theme_dracula", "Dracula", "눈의 피로를 줄여주는 드라큘라 테마", ItemCategory.THEME, 500, "theme_dracula", 4))
        add(ShopItem("theme_monokai", "Monokai", "클래식 이즈 베스트.", ItemCategory.THEME, 500, "theme_monokai", 4))
        add(ShopItem("theme_one_dark", "One Dark", "국룰 다크 모드.\n10명 중 8명이 쓴다는 그 색조.", ItemCategory.THEME, 500, "theme_one_dark", 4))
        add(ShopItem("theme_nord", "Nord", "내 본체는 뜨거워도 테마만은 차갑게.", ItemCategory.THEME, 800, "theme_nord", 4))
        add(ShopItem("theme_gruvbox", "Gruvbox", "눈이 편한하고 어딘가 힙한 레트로 감성.", ItemCategory.THEME, 800, "theme_gruvbox", 4))
    }
}

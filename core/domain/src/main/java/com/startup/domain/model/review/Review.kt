package com.startup.domain.model.review
data class Review(
    val reviewId: Int,
    val spotId: Int,
    val distance: Double,
    val step: Int,
    /** 걸은 날짜 : yyyy-MM-dd */
    val date: String,
    /** hh:mm:ss */
    val startTime: String,
    /** hh:mm:ss */
    val endTime: String,
    /** 같이 걸은 캐릭터 Id */
    val characterId: Long,
    val characterType : Int,
    val characterClass : Int,
    /** 스팟 타입 아이콘 **/
    val spotType:String,
    val rank : Int,
    /** 리뷰 사진 url */
    val pic: String,
    /** 리뷰 작성 여부 */
    val reviewCd: Boolean,
    val review: String,
    /** 평점 */
    val rating: Int,
)
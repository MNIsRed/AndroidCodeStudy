package com.mole.net.response

data class PetResponse (
    /**
     * 状态码
     */
    val code: Long,

    /**
     * 宠物信息
     */
    val data: Pet
)

/**
 * 宠物信息
 *
 * Pet
 */
data class Pet (
    /**
     * 分组
     */
    val category: Category,

    /**
     * 宠物ID编号
     */
    val id: Long,

    /**
     * 名称
     */
    val name: String,

    /**
     * 照片URL
     */
    val photoUrls: List<String>,

    /**
     * 宠物销售状态
     */
    val status: String,

    /**
     * 标签
     */
    val tags: List<Tag>
)

/**
 * 分组
 *
 * Category
 */
data class Category (
    /**
     * 分组ID编号
     */
    val id: Long? = null,

    /**
     * 分组名称
     */
    val name: String? = null
)

/**
 * 宠物销售状态
 */
enum class Status(val value: String) {
    Available("available"),
    Pending("pending"),
    Sold("sold");

    companion object {
        public fun fromValue(value: String): Status = when (value) {
            "available" -> Available
            "pending"   -> Pending
            "sold"      -> Sold
            else        -> throw IllegalArgumentException()
        }
    }
}

/**
 * Tag
 */
data class Tag (
    /**
     * 标签ID编号
     */
    val id: Long? = null,

    /**
     * 标签名称
     */
    val name: String? = null
)
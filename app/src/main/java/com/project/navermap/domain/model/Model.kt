package com.project.navermap.domain.model

import androidx.recyclerview.widget.DiffUtil

abstract class Model(
    open val id : Long,
    open val type : CellType
) {

    open fun isTheSame(item: Model) =
        this.id == item.id && this.type == item.type

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Model>() {
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.isTheSame(newItem)
            }

            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem == newItem
            }
        }
    }


    /**
     * [DIFF_CALLBACK]의 areContentsTheSame에서 사용할 equals
     * equals가 없으면 areContentsTheSame에 @SuppressLint("DiffUtilEquals")를 붙여야한다.
     * Model 클래스를 비교할 일이 없으므로 Annotation을 붙여도 상관없다.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Model

        if (id != other.id) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

}
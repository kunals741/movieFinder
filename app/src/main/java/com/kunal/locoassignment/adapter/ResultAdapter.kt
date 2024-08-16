package com.kunal.locoassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kunal.locoassignment.databinding.ItemGridResultBinding
import com.kunal.locoassignment.databinding.ItemListResultBinding
import com.kunal.locoassignment.model.ResultDetail

class ResultAdapter(
    private val layoutManager: GridLayoutManager? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType {
        LIST,
        GRID
    }

    private var resultList = mutableListOf<ResultDetail>()

    class MyDiffUtilCallback(
        private val oldList: List<ResultDetail>,
        private val newList: List<ResultDetail>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].imdbID == newList[newItemPosition].imdbID
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    fun setData(newList: List<ResultDetail>) {
        val diffCallback = MyDiffUtilCallback(resultList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        resultList.clear()
        resultList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    //List View Holder
    inner class ResultListViewHolder(private val binding: ItemListResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resultDetail: ResultDetail) = with(binding) {
            Glide.with(ivPoster).load(resultDetail.poster).into(ivPoster)
            tvName.text = resultDetail.title
            tvRating.text = resultDetail.rating.toString()
            tvTypeValue.text = resultDetail.type
            tvReleaseValue.text = resultDetail.year
        }
    }

    //Grid View Holder
    inner class ResultGridViewHolder(private val binding: ItemGridResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resultDetail: ResultDetail) = with(binding) {
            Glide.with(ivPoster).load(resultDetail.poster).into(ivPoster)
            tvName.text = resultDetail.title
            tvRating.text = resultDetail.rating.toString()
            tvTypeValue.text = resultDetail.type
            tvReleaseValue.text = resultDetail.year
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.LIST.ordinal -> {
                val binding =
                    ItemListResultBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ResultListViewHolder(binding)
            }

            else -> {
                val binding =
                    ItemGridResultBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ResultGridViewHolder(binding)
            }
        }
    }

    override fun getItemCount() = resultList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ViewType.LIST.ordinal -> (holder as ResultListViewHolder).bind(resultList[position])
            else -> (holder as ResultGridViewHolder).bind(resultList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (layoutManager?.spanCount == 1) ViewType.LIST.ordinal
        else ViewType.GRID.ordinal
    }

}
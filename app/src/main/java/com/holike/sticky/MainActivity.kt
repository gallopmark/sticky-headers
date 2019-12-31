package com.holike.sticky

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import pony.xcode.sticky.StickyHeaders
import pony.xcode.sticky.StickyHeadersLinearLayoutManager


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val stickyLayoutManager = StickyHeadersLinearLayoutManager<MyAdapter>(this)
        recycler_view.layoutManager = stickyLayoutManager
        recycler_view.adapter = MyAdapter()
    }

    interface MultipleItem {
        fun getItemType(): Int
    }

    class HeadItem(var title: String?) : MultipleItem {

        override fun getItemType(): Int = 0
    }

    class BeanItem(var text: String?) : MultipleItem {
        override fun getItemType(): Int = 1
    }

    private class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>(), StickyHeaders,
        StickyHeaders.ViewSetup {
        val datas = ArrayList<MultipleItem>()

        init {
            for (i in 0 until 100) {
                datas.add(HeadItem("head${(i + 1)}"))
                for (j in 0 until 20) {
                    datas.add(BeanItem("bean${(j + 1)}"))
                }
            }
        }

        override fun isStickyHeader(position: Int): Boolean = datas[position].getItemType() == 0

        override fun teardownStickyHeaderView(stickyHeader: View) {
            ViewCompat.setElevation(stickyHeader, 0f)
        }

        override fun setupStickyHeaderView(stickyHeader: View) {
            ViewCompat.setElevation(stickyHeader, 10f)
        }

        override fun getItemViewType(position: Int): Int = datas[position].getItemType()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return if (viewType == 0) {
                MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header,
                    parent,
                    false))
            } else {
                MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bean,
                    parent,
                    false))
            }
        }

        override fun getItemCount(): Int = datas.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val itemType = holder.itemViewType
            val item = datas[position]
            if (itemType == 0) {
                val headItem: HeadItem = item as HeadItem
                holder.itemView.findViewById<TextView>(R.id.tv_header).text = headItem.title
            } else {
                val beanItem: BeanItem = item as BeanItem
                holder.itemView.findViewById<TextView>(R.id.tv_bean).text = beanItem.text
            }
        }

        open inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}

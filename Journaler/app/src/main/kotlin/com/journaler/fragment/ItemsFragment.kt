package com.journaler.fragment

import com.journaler.R


class ItemsFragment : BaseFragment() {

    override val logTag = "Items fragment"

    override fun getLayout(): Int {
        return R.layout.fragment_items
    }

}
package com.qianhua.market.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.qianhua.market.R
import com.qianhua.market.utils.CommonUtils


abstract class BaseTabFragment(id: Int) : BaseFragment(id) {

    private var fakedStatusBar: View? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_base_tab, container, false)
        fakedStatusBar = view.findViewById(R.id.faked_status_bar)
        val rootView = view.findViewById<View>(R.id.root_view) as ViewGroup
        inflater.inflate(LAYOUT_RES_ID, rootView, true)
        if (needFakeStatusBar()) {
            fakeStatusBar()
        }
        return view
    }

    override fun onDestroyView() {
        fakedStatusBar = null
        super.onDestroyView()
    }

    /**
     * whether BTF should render FakeStatusBar
     *
     * @return true if should, subImplements should overwrite this to determine custom action
     */
    protected fun needFakeStatusBar(): Boolean {
        return true
    }

    internal fun fakeStatusBar() {
        val statusHeight = CommonUtils.getStatusBarHeight(activity)
        val params = fakedStatusBar!!.layoutParams
        params.height = statusHeight
        fakedStatusBar!!.layoutParams = params
    }
}

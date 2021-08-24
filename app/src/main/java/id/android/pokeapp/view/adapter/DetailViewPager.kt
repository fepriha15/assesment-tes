package id.android.pokeapp.view.adapter

import android.content.Context
import android.system.Os.stat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.android.pokeapp.R
import id.android.pokeapp.view.fragment.EvolutionFragment
import id.android.pokeapp.view.fragment.StatFragment
import java.util.ArrayList

class DetailViewPager(private val mContext: Context?, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragments = ArrayList<Fragment>()

    init {
        fragments.add(StatFragment())
        fragments.add(EvolutionFragment())
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return mContext?.getString(R.string.stat)
            1 -> return mContext?.getString(R.string.evolution)
        }
        return null
    }
}
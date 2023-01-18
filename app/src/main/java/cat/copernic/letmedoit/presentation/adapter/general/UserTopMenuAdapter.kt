package cat.copernic.letmedoit.presentation.adapter.general

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.collections.ArrayList
/**
 * Adapter menu superior usuario
 */

class UserTopMenuAdapter(fragmentManager: FragmentManager, val items: ArrayList<Fragment>, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }
}
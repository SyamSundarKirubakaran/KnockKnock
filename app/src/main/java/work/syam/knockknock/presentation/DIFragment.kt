package work.syam.knockknock.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import work.syam.knockknock.R

@AndroidEntryPoint
class DIFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_d_i, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DIFragment()
    }
}
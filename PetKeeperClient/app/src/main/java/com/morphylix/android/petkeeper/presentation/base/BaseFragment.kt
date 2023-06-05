package com.morphylix.android.petkeeper.presentation.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

private const val TAG = "BaseFragment"

open class BaseFragment<BINDING: ViewBinding>(
    val inflateFun: (inflater: LayoutInflater, container: ViewGroup?) -> BINDING
) : Fragment() {

    private var _binding: BINDING? = null
    val binding: BINDING
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateFun(inflater, container)
        Log.i(TAG, "Initialized")
        return binding.root
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}
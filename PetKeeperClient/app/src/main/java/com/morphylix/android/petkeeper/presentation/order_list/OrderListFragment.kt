package com.morphylix.android.petkeeper.presentation.order_list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.morphylix.android.petkeeper.R
import com.morphylix.android.petkeeper.databinding.FragmentOrderListBinding
import com.morphylix.android.petkeeper.domain.model.domain.Order
import com.morphylix.android.petkeeper.domain.model.domain.User
import com.morphylix.android.petkeeper.domain.model.domain.settings.SettingsConfig
import com.morphylix.android.petkeeper.presentation.MainActivity
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val TAG = "OrderListFragment"

class OrderListFragment : BaseFragment<FragmentOrderListBinding>({ inflater, container ->
    FragmentOrderListBinding.inflate(inflater, container, false)
}) {
    private var orderListAdapter: OrderListAdapter = OrderListAdapter()
    private val orderListViewModel: OrderListViewModel by activityViewModels()
    private lateinit var settingsConfig: SettingsConfig


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        orderListViewModel.getSettingsConfig()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.filter_button -> {
                        Log.i(TAG, "Clicked on filter button yo")
                        findNavController().navigate(R.id.filterSettingsFragment)
                    }
                }
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        Log.i(TAG, "adapter is ${orderListAdapter.currentList}")
        binding.orderListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.orderListRecyclerView.adapter = orderListAdapter
        Log.i(TAG, "cur list size is ${orderListAdapter.currentList.size}")
        binding.newOrderFab.setOnClickListener {
            val action = OrderListFragmentDirections.actionOrderListFragmentToChooseOrderTypeFragment()
            view.findNavController().navigate(action)
        }

        binding.orderListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0 && binding.newOrderFab.isShown) {
                    binding.newOrderFab.hide()
                    Log.i(TAG, "hide fab")
                }
                if (dy < 0 && !binding.newOrderFab.isShown) {
                    binding.newOrderFab.show()
                    Log.i(TAG, "show fab")
                }
            }
        })


        lifecycleScope.launchWhenStarted {
            orderListViewModel.state.collect { state ->
                when (state) {
                    is OrderListState.Loading -> {
                    }
                    is OrderListState.Success<*> -> {
                        if (state.data is SettingsConfig) {
                            Log.i(TAG, "Got settings config sort by ${state.data.sortBy} filter by ${state.data.filterBy}")
                            orderListViewModel.loadInfo()
                        }
                        if (state.data is User) {
                            Log.i(TAG, "${state.data.name} ${state.data.surname}")
                        } else if (state.data is List<*> && state.data.firstOrNull() is Order) {
                            //Log.i(TAG, "orderlist is ${state.data.forEach { ((it as Order).pet?.picture != null) }}")
                            state.data.forEach { Log.i(TAG, "${((it as Order).pet?.picture != null)}") }
                            Log.i(TAG, "submitting list with size ${state.data.size}")
//                            orderListAdapter.submitList(emptyList())
//                            delay(200)
                            orderListAdapter.submitList(state.data as List<Order>)
                        }
                        orderListViewModel.setLoadingState()
                    }
                    is OrderListState.Error -> {

                    }
                }
            }
        }
    }


}
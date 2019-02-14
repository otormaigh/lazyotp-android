package ie.elliot.lazysms.feature.addprovider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ie.elliot.lazysms.app
import ie.elliot.lazysms.feature.addprovider.AddSmsProviderState.*
import kotlinx.android.synthetic.main.bs_add_provider.*

class AddSmsProviderBottomSheetFragment : BottomSheetDialogFragment() {
  private val viewModel by lazy { AddSmsProviderViewModel(requireContext().app.database) }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
    inflater.inflate(ie.elliot.lazysms.R.layout.bs_add_provider, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.stateMachine.observe(viewLifecycleOwner, Observer { state -> processState(state) })

    tilProvider.requestFocus()

    btnAdd.setOnClickListener {
      processState(Success)

//      viewModel.addProvider(etProvider.text, etDigitCount.text)
    }
    etDigitCount.setOnEditorActionListener { v, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        btnAdd.performClick()
      }
      false
    }
  }

  private fun processState(state: AddSmsProviderState) {
    when (state) {
      is Fail.Sender -> tilProvider.error = state.reason
      is Fail.DigitCount -> tilDigitCount.error = state.reason
      is Loading -> btnAdd.isLoading = false
      is Success -> btnAdd.isLoading = true
    }
  }

  companion object {
    fun show(fragmentManager: FragmentManager) = AddSmsProviderBottomSheetFragment().apply {
      show(fragmentManager, AddSmsProviderBottomSheetFragment::class.java.canonicalName)
    }
  }
}
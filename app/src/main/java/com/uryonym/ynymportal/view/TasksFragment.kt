package com.uryonym.ynymportal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.uryonym.ynymportal.R
import com.uryonym.ynymportal.databinding.TasksFragmentBinding
import com.uryonym.ynymportal.view.adapter.TasksAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private val taskViewModel by viewModels<TasksViewModel>()
    private lateinit var viewDataBinding: TasksFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.tasks_fragment, container, false)
        viewDataBinding = TasksFragmentBinding.bind(root).apply {
            viewModel = taskViewModel
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        val adapter = TasksAdapter(taskViewModel)
        viewDataBinding.taskList.adapter = adapter

        taskViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        taskViewModel.navigateAddTask.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(TasksFragmentDirections.actionTasksFragmentToAddTaskBottomSheetDialogFragment())
                taskViewModel.doneNavigateAddTask()
            }
        })

        taskViewModel.taskId.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(TasksFragmentDirections.actionTasksFragmentToEditTaskFragment(it))
                taskViewModel.doneOpenTask()
            }
        })

        return viewDataBinding.root
    }

}
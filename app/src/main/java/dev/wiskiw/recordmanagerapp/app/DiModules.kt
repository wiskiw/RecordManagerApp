package dev.wiskiw.recordmanagerapp.app

import dev.wiskiw.recordmanagerapp.presentation.screen.record.RecordViewModel
import dev.wiskiw.recordmanagerapp.presentation.screen.recordlist.RecordListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RecordViewModel(get()) }
    viewModel { RecordListViewModel(get()) }
}

val appModule = module {
}

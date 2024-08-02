package dev.wiskiw.recordmanagerapp.app

import dev.wiskiw.recordmanagerapp.data.repository.MockedRecordRepository
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRepository
import dev.wiskiw.recordmanagerapp.domain.usecase.RecordUseCase
import dev.wiskiw.recordmanagerapp.domain.usecase.SearchRecordUseCase
import dev.wiskiw.recordmanagerapp.presentation.screen.record.RecordViewModel
import dev.wiskiw.recordmanagerapp.presentation.screen.recordlist.RecordListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RecordViewModel(get(), get()) }
    viewModel { RecordListViewModel(get(), get()) }
}

val appModule = module {
    // creating Repositories
    // TODO replace with real implementation
    single<RecordRepository> { MockedRecordRepository() }

    // creating UseCases
    single { RecordUseCase(get(), get()) }
    single { SearchRecordUseCase(get()) }
}

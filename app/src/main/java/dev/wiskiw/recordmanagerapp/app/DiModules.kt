package dev.wiskiw.recordmanagerapp.app

import dev.wiskiw.recordmanagerapp.app.logger.AndroidLogger
import dev.wiskiw.recordmanagerapp.app.logger.AppLogger
import dev.wiskiw.recordmanagerapp.data.repository.MockedRecordRelationsRepository
import dev.wiskiw.recordmanagerapp.data.repository.MockedRecordRepository
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRelationsRepository
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRepository
import dev.wiskiw.recordmanagerapp.domain.usecase.RecordUseCase
import dev.wiskiw.recordmanagerapp.domain.usecase.SearchRecordUseCase
import dev.wiskiw.recordmanagerapp.presentation.screen.record.RecordViewModel
import dev.wiskiw.recordmanagerapp.presentation.screen.recordlist.RecordListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RecordViewModel(get(), get(), get()) }
    viewModel { RecordListViewModel(get(), get()) }
}

val appModule = module {
    single<AppLogger> { AndroidLogger() }

    // creating Repositories
    // TODO replace with real implementation
    single<RecordRepository> { MockedRecordRepository() }
    single<RecordRelationsRepository> { MockedRecordRelationsRepository() }

    // creating UseCases
    single { RecordUseCase(get(), get()) }
    single { SearchRecordUseCase(get()) }
}

package dev.wiskiw.recordmanagerapp.app

import androidx.room.Room
import dev.wiskiw.recordmanagerapp.app.logger.AndroidLogger
import dev.wiskiw.recordmanagerapp.app.logger.AppLogger
import dev.wiskiw.recordmanagerapp.data.repository.RoomRecordRelationsRepository
import dev.wiskiw.recordmanagerapp.data.repository.RoomRecordRepository
import dev.wiskiw.recordmanagerapp.data.room.AppRoomDatabase
import dev.wiskiw.recordmanagerapp.data.room.mapper.RecordIdMapper
import dev.wiskiw.recordmanagerapp.data.room.mapper.RecordMapper
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRelationsRepository
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRepository
import dev.wiskiw.recordmanagerapp.domain.usecase.RecordRelationsUseCase
import dev.wiskiw.recordmanagerapp.domain.usecase.RecordUseCase
import dev.wiskiw.recordmanagerapp.domain.usecase.SearchRecordUseCase
import dev.wiskiw.recordmanagerapp.presentation.screen.editrecord.EditRecordViewModel
import dev.wiskiw.recordmanagerapp.presentation.screen.record.RecordViewModel
import dev.wiskiw.recordmanagerapp.presentation.screen.recordlist.RecordListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RecordViewModel(get(), get(), get(), get()) }
    viewModel { RecordListViewModel(get(), get(), get(), get()) }
    viewModel { EditRecordViewModel(get(), get(), get()) }
}

val dataModule = module {
    single<AppRoomDatabase> {
        Room.databaseBuilder(androidContext(), AppRoomDatabase::class.java, RecordManagerApp.ROOM_DB_NAME).build()
    }
}

val appModule = module {
    single<AppLogger> { AndroidLogger() }

    // creating Repositories
    single<RecordRepository> {
        val roomDatabase = get<AppRoomDatabase>()
        val idMapper = RecordIdMapper()
        val recordMapper = RecordMapper(idMapper)

        RoomRecordRepository(
            recordEntityDao = roomDatabase.recordEntityDao(),
            recordIdMapper = idMapper,
            recordMapper = recordMapper,
        )
    }
    single<RecordRelationsRepository> {
        val roomDatabase = get<AppRoomDatabase>()
        val idMapper = RecordIdMapper()
        val recordMapper = RecordMapper(idMapper)

        RoomRecordRelationsRepository(
            recordEntityDao = roomDatabase.recordEntityDao(),
            recordIdMapper = idMapper,
            recordMapper = recordMapper,
        )
    }

    // creating UseCases
    single { RecordUseCase(get()) }
    single { RecordRelationsUseCase(get(), get()) }
    single { SearchRecordUseCase(get()) }
}

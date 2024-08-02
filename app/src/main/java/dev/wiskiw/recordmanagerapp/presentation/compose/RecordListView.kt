package dev.wiskiw.recordmanagerapp.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.model.RecordType
import dev.wiskiw.recordmanagerapp.presentation.theme.RecordManagerTheme
import dev.wiskiw.recordmanagerapp.presentation.theme.size


@Composable
fun RecordListView(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(MaterialTheme.size.one),
    records: List<Record>,
    onClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier, verticalArrangement = verticalArrangement, contentPadding = contentPadding
    ) {
        items(records) { record ->
            RecordItem(
                modifier = Modifier.fillMaxWidth(),
                record = record,
                onClick = { onClick(record.id) }
            )
        }
    }
}

@Composable
private fun RecordItem(
    modifier: Modifier = Modifier,
    record: Record,
    onClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(MaterialTheme.size.one)
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = shape,
            )
            .clip(shape)
            .clickable { onClick.invoke() }
            .padding(MaterialTheme.size.one),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.size.one),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.size.half),
        ) {
            Text(
                text = record.id,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = record.name,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun PreviewLight() {
    val records = listOf(
        Record(
            id = "r-1",
            type = RecordType.Desk,
            name = "name-1",
            description = "description-1",
        ),
        Record(
            id = "r-2",
            type = RecordType.Server,
            name = "name-2",
            description = "description-2",
        ),
        Record(
            id = "r-3",
            type = RecordType.Employee,
            name = "name-3",
            description = "description-3",
        ),
    )

    RecordManagerTheme(
        darkTheme = false,
    ) {
        RecordListView(records = records, onClick = {})
    }
}

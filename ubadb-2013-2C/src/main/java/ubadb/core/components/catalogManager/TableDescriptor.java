package ubadb.core.components.catalogManager;

import ubadb.core.common.TableId;

public class TableDescriptor
{
	private TableId tableId;
	private String tableName;

	public TableDescriptor(TableId tableId, String tableName)
	{
		this.tableId = tableId;
		this.tableName = tableName;
	}

	public TableId getTableId()
	{
		return tableId;
	}

	public String getTableName()
	{
		return tableName;
	}
}

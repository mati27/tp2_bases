package ubadb.core.components.catalogManager;

import ubadb.core.common.TableId;

public interface CatalogManager
{
	TableDescriptor getTableDescriptorByTableId(TableId tableId);
}
package de.hybris.merchandise.initialdata.dataimport.impl;

import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;


public class MerchandiseDataImportService extends SampleDataImportService
{
	@Override
	protected void importProductCatalog(final String extensionName, final String productCatalogName)
	{
		super.importProductCatalog(extensionName, productCatalogName);
		//Load User Price Groups
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/pricegroups.impex",
						extensionName, productCatalogName), false);
	}
}
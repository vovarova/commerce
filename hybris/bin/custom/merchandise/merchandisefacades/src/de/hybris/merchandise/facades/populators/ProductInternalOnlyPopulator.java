/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.merchandise.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class ProductInternalOnlyPopulator implements Populator<ProductModel, ProductData>
{
	@Override
	public void populate(final ProductModel source, final ProductData target) throws ConversionException
	{
		target.setInternalOnly(source.getInternalOnly() == null ? false : source.getInternalOnly().booleanValue());
	}
}
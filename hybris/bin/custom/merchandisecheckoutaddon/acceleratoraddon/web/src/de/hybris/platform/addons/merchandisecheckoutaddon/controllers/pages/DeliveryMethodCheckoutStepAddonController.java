/**
 *
 */
package de.hybris.platform.addons.merchandisecheckoutaddon.controllers.pages;

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
import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.addons.merchandisecheckoutaddon.controllers.MerchandisecheckoutaddonControllerConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.storefront.controllers.pages.checkout.steps.AbstractCheckoutStepController;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "/checkout/multi/addon/delivery-method")
public class DeliveryMethodCheckoutStepAddonController extends AbstractCheckoutStepController
{
	private final static String DELIVERY_METHOD = "delivery-method";

	@RequestMapping(value = "/choose", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	@PreValidateCheckoutStep(checkoutStep = DELIVERY_METHOD)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		// Try to set default delivery mode
		getCheckoutFacade().setDeliveryModeIfAvailable();
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		model.addAttribute("cartData", cartData);
		model.addAttribute("deliveryMethods", getCheckoutFacade().getSupportedDeliveryModes());
		this.prepareDataForPage(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.deliveryMethod.breadcrumb"));
		model.addAttribute("metaRobots", "noindex,nofollow");
		setCheckoutStepLinksForModel(model, getCheckoutStep());
		return MerchandisecheckoutaddonControllerConstants.ChooseDeliveryMethodPage;
	}

	/**
	 * This method gets called when the "Use Selected Delivery Method" button is clicked. It sets the selected delivery
	 * mode on the checkout facade and reloads the page highlighting the selected delivery Mode.
	 * 
	 * @param selectedDeliveryMethod
	 *           - the id of the delivery mode.
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@RequireHardLogIn
	public String doSelectDeliveryMode(@RequestParam("delivery_method") final String selectedDeliveryMethod,
			@RequestParam("gift_wrap") final String selectGiftWrap)
	{
		if (!getCheckoutFlowFacade().hasValidCart())
		{
			LOG.info("Missing, empty or unsupported cart");
			return REDIRECT_URL_CART;
		}
		if (StringUtils.isNotEmpty(selectedDeliveryMethod))
		{
			getCheckoutFacade().setDeliveryMode(selectedDeliveryMethod);
		}
		if (StringUtils.isNotEmpty(selectGiftWrap))
		{
			getSessionService().setAttribute("GIFT_WRAP", selectGiftWrap.toUpperCase());
		}
		return getCheckoutStep().nextStep();
	}

	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(DELIVERY_METHOD);
	}
}
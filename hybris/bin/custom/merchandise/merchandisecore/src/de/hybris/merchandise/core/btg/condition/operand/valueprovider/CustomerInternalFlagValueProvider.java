package de.hybris.merchandise.core.btg.condition.operand.valueprovider;

import de.hybris.merchandise.core.model.BTGCustomerInternalFlagOperandModel;
import de.hybris.platform.btg.condition.operand.OperandValueProvider;
import de.hybris.platform.btg.enums.BTGConditionEvaluationScope;
import de.hybris.platform.btg.jalo.BTGException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;


/**
 * @author KKW
 *
 */
public class CustomerInternalFlagValueProvider implements OperandValueProvider<BTGCustomerInternalFlagOperandModel>
{

	@Override
	public Object getValue(final BTGCustomerInternalFlagOperandModel operand, final UserModel user,
			final BTGConditionEvaluationScope scope)
	{
		if (user instanceof CustomerModel)
		{
			return ((CustomerModel) user).getIsInternal();
		}
		throw new BTGException("user must be of type [Customer]");

	}

	@Override
	public Class getValueType(final BTGCustomerInternalFlagOperandModel arg0)
	{
		return Boolean.class;
	}

}
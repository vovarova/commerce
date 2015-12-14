package de.hybris.merchandise.fulfilmentprocess.actions.order;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.dao.OrderCancelDao;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;

import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;


public class WaitForImmediateCancel extends AbstractAction
{

	private static final String STATUS_NOK = "NOK";
	private static final String STATUS_OK = "OK";
	private static final String STATUS_CANCELLED = "CANCELLED";

	private static Set<String> transitions = createTransitions(STATUS_OK, STATUS_NOK);
	private final static Logger LOG = Logger.getLogger(WaitForImmediateCancel.class);


	private OrderCancelDao orderCancelDao;


	@Override
	public String execute(final BusinessProcessModel process) throws RetryLaterException, Exception
	{
		if (process instanceof OrderProcessModel)
		{
			final OrderProcessModel orderProcess = (OrderProcessModel) process;
			final OrderModel order = orderProcess.getOrder();

			getModelService().refresh(order);
			if (OrderStatus.CANCELLED.equals(order.getStatus()) || OrderStatus.CANCELLING.equals(order.getStatus()))
			{
				LOG.debug("Process [" + process.getCode() + "] : Order [" + order.getCode() + "] was cancelled");
				return STATUS_CANCELLED;
			}

			if (stillWait(order))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Process [" + process.getCode() + "] : Order [" + order.getCode() + "] in a wait node");
				}
				updateStatus(order);
				try
				{
					Thread.sleep(2000);
				}
				catch (final InterruptedException e)
				{
					LOG.error("Sleep interrupted", e);
				}
				return STATUS_NOK;
			}
			return STATUS_OK;
		}
		throw new IllegalStateException("Process must be of type " + OrderProcessModel.class.getName());
	}


	private void updateStatus(final OrderModel order)
	{
		getModelService().refresh(order);
		if (!OrderStatus.WAITING_FOR_IMMEDIATE_CANCEL.equals(order.getStatus()))
		{
			order.setStatus(OrderStatus.WAITING_FOR_IMMEDIATE_CANCEL);
			getModelService().save(order);
		}
	}

	@Override
	public Set getTransitions()
	{
		return transitions;
	}

	private boolean stillWait(final OrderModel order)
	{
		final long orderPlacedMilis = order.getCreationtime().getTime();
		final long timetowait = getImmediateCancelWaitingTime() * 1000;

		return (new Date().getTime() < (orderPlacedMilis + timetowait));
	}


	private int getImmediateCancelWaitingTime()
	{
		return getCancelConfiguration().getQueuedOrderWaitingTime();
	}

	private OrderCancelConfigModel getCancelConfiguration()
	{
		return getOrderCancelDao().getOrderCancelConfiguration();
	}

	@Resource
	public void setOrderCancelDao(final OrderCancelDao orderCancelDao)
	{
		this.orderCancelDao = orderCancelDao;
	}

	/**
	 * @return the orderCancelDao
	 */
	protected OrderCancelDao getOrderCancelDao()
	{
		return orderCancelDao;
	}
}
package de.hybris.platform.addons.randomimageaddon.cmscockpit.editor;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.DefaultIntegerUIEditor;

import java.util.Map;

import org.zkoss.zk.ui.HtmlBasedComponent;


public class PixelNumberEditor extends DefaultIntegerUIEditor
{

	@Override
	public HtmlBasedComponent createViewComponent(final Object initialValue, final Map<String, ? extends Object> parameters,
			final EditorListener listener)
	{

		final HtmlBasedComponent superEditor = super.createViewComponent(initialValue, parameters, listener);
		superEditor.setTooltiptext("Set value in pixels,mister!");
		return superEditor;
	}
}
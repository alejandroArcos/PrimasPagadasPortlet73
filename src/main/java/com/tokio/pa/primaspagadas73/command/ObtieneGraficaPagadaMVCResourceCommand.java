package com.tokio.pa.primaspagadas73.command;

import com.google.gson.Gson;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.cotizador.CotizadorService;
import com.tokio.cotizador.Bean.PrimaResponse;
import com.tokio.pa.primaspagadas73.constants.PrimasPagadasPortlet73PortletKeys;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true, 
		property = { "javax.portlet.name=" + PrimasPagadasPortlet73PortletKeys.PRIMASPAGADASPORTLET73,
					 "mvc.command.name=/analitica/obtieneGraficaPrimaPagadaPesos" },
		service = MVCResourceCommand.class
)

public class ObtieneGraficaPagadaMVCResourceCommand extends BaseMVCResourceCommand{
	private static Log _log = LogFactoryUtil.getLog(ObtieneGraficaPagadaMVCResourceCommand.class);
	
	@Reference
	CotizadorService _CotizadorService;

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		_log.info("ObtieneGraficaPrimaEmitidaMVCResourceCommand....");
		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String usuario = themeDisplay.getUser().getFullName();
		String pantalla = "LOGIN";
//		String agente = "5896";
		String agente = ParamUtil.getString(resourceRequest, "agente");
		
		int year = ParamUtil.getInteger(resourceRequest, "anoBusqueda");
		int month = ParamUtil.getInteger(resourceRequest, "mesBusqueda");
		_log.info("año: "+ year +" mes:"+month);
		
		PrimaResponse primaResp = null;
		
		try {
			primaResp = _CotizadorService.getPrimaPagada(year, month, agente, usuario, pantalla);
			Gson gson = new Gson();
			String stringJsonDatos = gson.toJson(primaResp.getDatosKPI());
			resourceResponse.getWriter().write(stringJsonDatos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}

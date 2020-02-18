/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.provvisoriocassa;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.provvisoriocassa.RicercaSinteticaProvvisorioDiCassaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;

/**
 * Classe di action per le ricerche sintetiche del provvisorio di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/03/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaSinteticaProvvisorioDiCassaAction extends GenericBilancioAction<RicercaSinteticaProvvisorioDiCassaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8338470732256037965L;
	
	@Autowired private transient ProvvisorioService provvisorioService;
	@Autowired private transient PreDocumentoSpesaService preDocumentoSpesaService;
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Imposto l'annotazione
		model.setDaRegolarizzare("S");
		model.setDaAnnullare("N");
		return super.execute();
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento liste
		try {
			caricaListaContoTesoreria();
		} catch(WebServiceInvocationFailureException e) {
			log.error("prepare", "Errore nell'invocazione del caricamento di una lista: " + e.getMessage(), e);
		} finally {
			checkMetodoConclusoSenzaErrori();
		}
	}
	
	/**
	 * Ricerca del provvisorio.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		
		RicercaProvvisoriDiCassa request = model.creaRequestRicercaProvvisoriDiCassa();
		RicercaProvvisoriDiCassaResponse response = provvisorioService.ricercaProvvisoriDiCassa(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nella ricerca dei provvisori di cassa");
			addErrori(response);
			return INPUT;
		}
		if(response.getNumRisultati() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Totale: "+response.getNumRisultati());
		log.debug(methodName, "Ricerca effettuata con successo");
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PROVVISORIO_DI_CASSA, request);
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_PROVVISORIO_DI_CASSA, response.getElencoProvvisoriDiCassa());
		// Imposto in sessione i dati necessari a causa della NON gestione corretta dei parametri di paginaziones
		sessionHandler.setParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN, response.getNumPagina());
		sessionHandler.setParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN, response.getNumRisultati());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.TIPO_PROVVISORIO, model.getProvvisorioDiCassa().getTipoProvvisorioDiCassa());		
		sessionHandler.setParametro(BilSessionParameter.LISTA_CONTO_TESORERIA, model.getListaContoTesoreria());

		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ricerca()}.
	 */
	public void validateRicerca() {
		boolean ricercaValida = model.getDataEmissioneDa() != null	
			|| model.getDataEmissioneA() != null
			|| model.getDataInizioTrasmissione() != null
			|| model.getDataFineTrasmissione() != null
			|| model.getImportoDa() != null
			|| model.getImportoA() != null
			|| checkPresenzaIdEntita(model.getContoTesoreria())
			|| StringUtils.isNotBlank(model.getDaAnnullare())
			|| StringUtils.isNotBlank(model.getDaRegolarizzare())
			|| (
				model.getProvvisorioDiCassa() != null
				&& (
					model.getProvvisorioDiCassa().getTipoProvvisorioDiCassa() != null
					|| model.getProvvisorioDiCassa().getNumero() != null
					|| StringUtils.isNotBlank(model.getProvvisorioDiCassa().getCausale())
					|| StringUtils.isNotBlank(model.getProvvisorioDiCassa().getSubCausale())
					|| StringUtils.isNotBlank(model.getProvvisorioDiCassa().getDenominazioneSoggetto())
				)
			);
		
		checkCondition(ricercaValida, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		checkCondition(model.getDataEmissioneDa() == null || model.getDataEmissioneA() == null || !model.getDataEmissioneDa().after(model.getDataEmissioneA()),
			ErroreCore.VALORE_NON_VALIDO.getErrore("Data emissione Inizio/Fine", "L'inizio della data di emissione non puo' essere successiva alla data di fine"));
		
		checkCondition(model.getDataInizioTrasmissione() == null || model.getDataFineTrasmissione() == null || !model.getDataInizioTrasmissione().after(model.getDataFineTrasmissione()),
				ErroreCore.VALORE_NON_VALIDO.getErrore("Data Trasmissione Inizio/Fine", "L'inizio della data di trasmissione non puo' essere successiva alla data di fine"));
		
		checkCondition(model.getImportoDa() == null || model.getImportoA() == null || model.getImportoA().subtract(model.getImportoDa()).signum() >= 0,
			ErroreCore.VALORE_NON_VALIDO.getErrore("Importo Da/A", "L'importo A non puo' essere inferiore all'importo Da"));
	
		validateContoEvidenza();
	}
	
	private void validateContoEvidenza() {
		if(!checkPresenzaIdEntita(model.getContoTesoreria())) {
			return;
		}
		ContoTesoreria contoTesoreria = ComparatorUtils.searchByUid(model.getListaContoTesoreria(), model.getContoTesoreria());
		checkNotNullNorInvalidUid(contoTesoreria, "Conto tesoreria");
		model.setContoTesoreria(contoTesoreria);
	}

	/**
	 * Carica la lista del Conto Tesoreria.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaContoTesoreria() throws WebServiceInvocationFailureException {
		List<ContoTesoreria> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CONTO_TESORERIA);
		if(listaInSessione == null) {
			LeggiContiTesoreria request = model.creaRequestLeggiContiTesoreria();
			logServiceRequest(request);
			LeggiContiTesoreriaResponse response = preDocumentoSpesaService.leggiContiTesoreria(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaContoTesoreria");
			}
			
			listaInSessione = response.getContiTesoreria();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CONTO_TESORERIA, listaInSessione);
		}
		
		model.setListaContoTesoreria(listaInSessione);
	}
}

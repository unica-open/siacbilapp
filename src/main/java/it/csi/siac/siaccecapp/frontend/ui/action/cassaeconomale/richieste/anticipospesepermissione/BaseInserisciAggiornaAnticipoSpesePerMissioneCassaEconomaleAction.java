/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospesepermissione;

import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseInserisciAggiornaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione.BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaMezziDiTrasporto;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaMezziDiTrasportoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.model.DatiTrasfertaMissione;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.MezziDiTrasporto;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValutaResponse;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe base di action per l'inserimento dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/01/2015
 * 
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleAction<M extends BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleModel> extends BaseInserisciAggiornaRichiestaEconomaleAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4001090349320981284L;

	/** Nome del model dell'aggiornamento per la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNAMENTO = "AggiornaAnticipoSpesePerMissioneCassaEconomaleModel";
	/** Nome del model dell'inserimento per la sessione */
	public static final String MODEL_SESSION_NAME_INSERIMENTO = "InserisciAnticipoSpesePerMissioneCassaEconomaleModel";
	/** Nome del model dell'aggiornamento rendiconto per la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNAMENTO_RENDICONTO = "AggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel";
	/** Nome del model dell'inserimento rendiconto per la sessione */
	public static final String MODEL_SESSION_NAME_INSERIMENTO_RENDICONTO = "InserisciRendicontoAnticipoSpesePerMissioneCassaEconomaleModel";
	
	@Autowired private transient DocumentoIvaService documentoIvaService;
	
	@Override
	protected void caricaListe() throws WebServiceInvocationFailureException {
		caricaListaMezziTrasporto();
		caricaListeClassificatoriGenerici();
		caricaListaTipoGiustificativo();
		caricaListaValuta();
	}
	
	/**
	 * Caricamento della lista dei mezzi di trasporto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListaMezziTrasporto() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaMezziTrasporto";
		List<MezziDiTrasporto> listaMezziDiTrasporto = sessionHandler.getParametro(BilSessionParameter.LISTA_MEZZI_DI_TRASPORTO);
		if(listaMezziDiTrasporto == null) {
			log.debug(methodName, "Lista MezziDiTrasporto non presente in sessione. Caricamento da servizio");
			// Carico la lista da servizio
			RicercaMezziDiTrasporto request = model.creaRequestRicercaMezziDiTrasporto();
			logServiceRequest(request);
			RicercaMezziDiTrasportoResponse response = richiestaEconomaleService.ricercaMezziDiTrasporto(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String errorMsg = createErrorInServiceInvocationString(RicercaMezziDiTrasporto.class, response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			listaMezziDiTrasporto = response.getMezziDiTrasporto();
			sessionHandler.setParametro(BilSessionParameter.LISTA_MEZZI_DI_TRASPORTO, listaMezziDiTrasporto);
		}
		model.setListaMezziDiTrasporto(listaMezziDiTrasporto);
	}

	/**
	 * Caricamento della lista dei tipi giustificativo.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListaTipoGiustificativo() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTipoGiustificativo";
		List<TipoGiustificativo> listaTipoGiustificativo = sessionHandler.getParametro(getBilSessionParameterListaTipoGiustificativo());
		if(listaTipoGiustificativo == null) {
			log.debug(methodName, "Lista di TipoGiustificativo non presente in sessione. Caricamento da servizio");
			// Carico la lista da servizio
			RicercaTipoGiustificativo request = model.creaRequestRicercaTipoGiustificativoAnticipoMissione();
			logServiceRequest(request);
			RicercaTipoGiustificativoResponse response = richiestaEconomaleService.ricercaTipoGiustificativo(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String errorMsg = createErrorInServiceInvocationString(RicercaTipoGiustificativo.class, response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			listaTipoGiustificativo = response.getTipiGiustificativi();
			sessionHandler.setParametro(getBilSessionParameterListaTipoGiustificativo(), listaTipoGiustificativo);
		}
		model.setListaTipoGiustificativo(listaTipoGiustificativo);
	}
	
	/**
	 * Ottiene il parametro di sessione per la lista dei tipi giustificativo-
	 * 
	 * @return il parametro di sessione per la lista
	 */
	protected BilSessionParameter getBilSessionParameterListaTipoGiustificativo() {
		return BilSessionParameter.LISTA_TIPO_GIUSTIFICATIVO_ANTICIPO_MISSIONE;
	}

	/**
	 * Caricamento della lista delle valute
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListaValuta() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaValuta";
		List<Valuta> listaValuta = sessionHandler.getParametro(BilSessionParameter.LISTA_VALUTA);
		if(listaValuta == null) {
			log.debug(methodName, "Lista di Valuta non presente in sessione. Caricamento da servizio");
			// Carico la lista da servizio
			RicercaValuta request = model.creaRequestRicercaValuta();
			logServiceRequest(request);
			RicercaValutaResponse response = documentoIvaService.ricercaValuta(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String errorMsg = createErrorInServiceInvocationString(RicercaValuta.class, response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			listaValuta = response.getListaValuta();
			sessionHandler.setParametro(BilSessionParameter.LISTA_VALUTA, listaValuta);
		}
		model.setListaValuta(listaValuta);
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		final String methodName = "validateCompleteStep1";
		
		checkNotNull(model.getRichiestaEconomale(), "Richiesta", true);
		checkNotNull(model.getRichiestaEconomale().getDatiTrasfertaMissione(), "Dati della richiesta", true);
		// Ho la richiesta
		RichiestaEconomale re = model.getRichiestaEconomale();
		DatiTrasfertaMissione dtm = model.getRichiestaEconomale().getDatiTrasfertaMissione();
		try {
			checkMatricola(re.getSoggetto());
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + pve.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		} catch(WebServiceInvocationFailureException wsife) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + wsife.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		}
		
		checkNotNullNorEmpty(dtm.getMotivo(), "Motivo trasferta");
		checkNotNull(dtm.getFlagEstero(), "Estero");
		checkNotNullNorEmpty(dtm.getLuogo(), "Luogo");
		
		checkValidDate(dtm.getDataInizio(), "Data inizio");
		checkValidDate(dtm.getDataFine(), "Data fine");
		checkCondition(dtm.getDataInizio() == null || dtm.getDataFine() == null || dtm.getDataInizio().compareTo(dtm.getDataFine()) <= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Data fine", ": non deve essere precedente alla data inizio"));
		checkCondition(!model.getListaGiustificativo().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Giustificativi"));
	}
	

	
	@Override
	protected boolean isValidTipoRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		return richiestaEconomale != null
			&& richiestaEconomale.getTipoRichiestaEconomale() != null
			&& BilConstants.CODICE_TIPO_RICHIESTA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE.getConstant().equals(richiestaEconomale.getTipoRichiestaEconomale().getCodice());
	}
	
	@Override
	protected void impostaDatiCopia(RichiestaEconomale richiestaEconomale) {
		super.impostaDatiCopia(richiestaEconomale);

		if (richiestaEconomale.getDatiTrasfertaMissione() != null ) {
			model.getRichiestaEconomale().setDatiTrasfertaMissione(richiestaEconomale.getDatiTrasfertaMissione());
			model.setMezziDiTrasportoSelezionati(richiestaEconomale.getDatiTrasfertaMissione().getMezziDiTrasporto());
		}

	}
	
	@Override
	protected void impostaDatiAggiornamento(RichiestaEconomale richiestaEconomale) {
		super.impostaDatiAggiornamento(richiestaEconomale);

		if (richiestaEconomale.getDatiTrasfertaMissione() != null ) {
			model.getRichiestaEconomale().setDatiTrasfertaMissione(richiestaEconomale.getDatiTrasfertaMissione());
			model.setMezziDiTrasportoSelezionati(richiestaEconomale.getDatiTrasfertaMissione().getMezziDiTrasporto());
		}
		
		model.setListaGiustificativo(richiestaEconomale.getGiustificativi());
	}
	
	@Override
	protected void caricaClassificatoriDaLista() {
		super.caricaClassificatoriDaLista();
		
		// Mezzi di trasporto
		List<MezziDiTrasporto> listaMezziDiTrasporto = model.getRichiestaEconomale().getDatiTrasfertaMissione().getMezziDiTrasporto();
		if(listaMezziDiTrasporto != null) {
			for(ListIterator<MezziDiTrasporto> li = listaMezziDiTrasporto.listIterator(); li.hasNext();) {
				MezziDiTrasporto mdt = li.next();
				MezziDiTrasporto mezziDiTrasportoFound = ComparatorUtils.searchByUidEventuallyNull(model.getListaMezziDiTrasporto(), mdt);
				li.set(mezziDiTrasportoFound);
			}
		}
		
		// Valuta del giustificativo
		for(Giustificativo g : model.getRichiestaEconomale().getGiustificativi()) {
			Valuta v = ComparatorUtils.searchByUidEventuallyNull(model.getListaValuta(), g.getValuta());
			g.setValuta(v);
		}
	}
	
	@Override
	protected BigDecimal ottieniImporto() {
		return model.getTotaleImportiGiustificativi();
	}
	
}

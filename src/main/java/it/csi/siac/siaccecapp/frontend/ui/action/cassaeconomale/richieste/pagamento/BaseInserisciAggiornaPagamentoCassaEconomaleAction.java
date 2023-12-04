/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.pagamento;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseInserisciAggiornaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamento.BaseInserisciAggiornaPagamentoCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValutaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe base di action per l'inserimento del pagamento.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 01/02/2016
 * 
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaPagamentoCassaEconomaleAction<M extends BaseInserisciAggiornaPagamentoCassaEconomaleModel> extends BaseInserisciAggiornaRichiestaEconomaleAction<M> {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = 7156823067352065696L;

	/** Nome del model dell'inserimento per la sessione */
	public static final String MODEL_SESSION_NAME_INSERIMENTO = "InserisciPagamentoCassaEconomaleModel";
	/** Nome del model dell'aggiornamento per la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNAMENTO= "AggiornaPagamentoCassaEconomaleModel";
	
	@Autowired private transient DocumentoIvaService documentoIvaService;
	
	@Override
	protected void caricaListe() throws WebServiceInvocationFailureException {

		caricaListeClassificatoriGenerici();
		caricaListaValuta();
		caricaListaTipoGiustificativo();
		caricaListaClasseSoggetto();
	}
	
	/**
	 * Carica la liste delle classi Soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaClasseSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClassiSoggetto == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			logServiceRequest(request);
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			// Controllo gli errori
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(ListeGestioneSoggetto.class, response));
			}
			listaClassiSoggetto = response.getListaClasseSoggetto();
			ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
		}
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}

	/**
	 * Caricamento della lista dei tipi giustificativo.
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
			RicercaTipoGiustificativo request = model.creaRequestRicercaTipoGiustificativo();
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
		return BilSessionParameter.LISTA_TIPO_GIUSTIFICATIVO_PAGAMENTO;
	}
	
	/**
	 * Carica il tipo di giustificativo.
	 * 
	 * @param giustificativo il giustificativo da caricare
	 */
	protected void caricaTipoGiustificativo(Giustificativo giustificativo) {
		List<TipoGiustificativo> listaTipoGiustificativo = model.getListaTipoGiustificativo();
		TipoGiustificativo foundTipoGiustificativo = ComparatorUtils.searchByUidEventuallyNull(listaTipoGiustificativo, giustificativo.getTipoGiustificativo());
		giustificativo.setTipoGiustificativo(foundTipoGiustificativo);
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		final String methodName = "validateCompleteStep1";
		
		checkNotNull(model.getRichiestaEconomale(), "Richiesta", true);
		
		// Ho la richiesta
		RichiestaEconomale re = model.getRichiestaEconomale();
		
		try {
			checkSoggettoFattura(re.getSoggetto());
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + pve.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		} catch(WebServiceInvocationFailureException wsife) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + wsife.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		}
		
		checkNotNullNorEmpty(re.getDescrizioneDellaRichiesta(), "Descrizione della spesa");
	}
	
	/**
	 * Impostazione dei dati del soggetto nella richiesta economale.
	 * 
	 * @param richiestaEconomale la richiesta da popolare
	 * @param soggetto           il soggetto tramite cui popolare la richiesta
	 */
	@Override
	protected void impostazioneDatiSoggetto(RichiestaEconomale richiestaEconomale, Soggetto soggetto) {
		richiestaEconomale.setSoggetto(soggetto);
		
		// Spiattello i dati sulla richiesta economale
		//richiestaEconomale.setMatricola(soggetto.getMatricola());
		richiestaEconomale.setCodiceBeneficiario(soggetto.getCodiceSoggetto());
		richiestaEconomale.setNome(soggetto.getNome());
		// SIAC-4792: se il soggetto non ha cognome (persona giuridica) prendo la denominazione
		richiestaEconomale.setCognome(StringUtils.isNotBlank(soggetto.getCognome()) ? soggetto.getCognome() : soggetto.getDenominazione());
		richiestaEconomale.setCodiceFiscale(soggetto.getCodiceFiscale());
	}
	
	@Override
	protected boolean isValidTipoRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		return richiestaEconomale != null
			&& richiestaEconomale.getTipoRichiestaEconomale() != null
			&& BilConstants.CODICE_TIPO_RICHIESTA_ECONOMALE_PAGAMENTO.getConstant().equals(richiestaEconomale.getTipoRichiestaEconomale().getCodice());
	}
	
	@Override
	protected void impostaDatiAggiornamento(RichiestaEconomale richiestaEconomale) {
		super.impostaDatiAggiornamento(richiestaEconomale);
		
		model.setListaGiustificativo(richiestaEconomale.getGiustificativi());
		if (richiestaEconomale.getSubdocumenti()!=null){
			model.setListaSubdocumentoSpesa(richiestaEconomale.getSubdocumenti());
			DocumentoSpesa documentoSpesa = null;
			for(SubdocumentoSpesa ss : richiestaEconomale.getSubdocumenti()) {
				if(ss != null && ss.getDocumento() != null && ss.getDocumento().getUid() != 0) {
					documentoSpesa = ss.getDocumento();
					break;
				}
			}
			model.setDocumentoSpesa(documentoSpesa);
		}
	}
	
	@Override
	protected void caricaClassificatoriDaLista() {
		super.caricaClassificatoriDaLista();
		
		
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

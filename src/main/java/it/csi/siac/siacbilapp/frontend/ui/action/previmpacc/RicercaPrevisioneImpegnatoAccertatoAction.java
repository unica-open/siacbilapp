/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilapp.frontend.ui.action.previmpacc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.previmpacc.RicercaPrevisioneImpegnatoAccertatoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * The Class RicercaPrevisioneImpegnatoAccertatoAction.
 *
 * @author elisa
 * @version 1.0.0 15 ott 2021
 */
public class RicercaPrevisioneImpegnatoAccertatoAction extends GenericBilancioAction<RicercaPrevisioneImpegnatoAccertatoModel> {

	/** per la serializzazione */
	private static final long serialVersionUID = 1L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;

	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	@Autowired private ClassificatoreService classificatoreService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		cleanErroriMessaggiInformazioni();
		caricaAzionePerSAC();
	}
	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		gestioneRientroDaConsulta();
		return SUCCESS;
	}
	
	private void gestioneRientroDaConsulta() {
		if(model.isDaConsultaEntrata()) {
			model.setCapitolo(null);
			RicercaSinteticaCapitoloEntrataGestione req = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO, RicercaSinteticaCapitoloEntrataGestione.class);
			CapitoloEntrataGestione cap = new CapitoloEntrataGestione();
			cap.setNumeroCapitolo(req.getRicercaSinteticaCapitoloEntrata().getNumeroCapitolo());
			cap.setNumeroArticolo(req.getRicercaSinteticaCapitoloEntrata().getNumeroArticolo());
			impostaSacDaRicercaSintetica(req.getRicercaSinteticaCapitoloEntrata().getCodiceStrutturaAmmCont());
			model.setCapitolo(cap);
			impostaStartPosition();
			return;
		}
		if(model.isDaConsultaSpesa()) {
			model.setCapitolo(null);
			RicercaSinteticaCapitoloUscitaGestione req = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO, RicercaSinteticaCapitoloUscitaGestione.class);
			CapitoloUscitaGestione cap = new CapitoloUscitaGestione();
			cap.setNumeroCapitolo(req.getRicercaSinteticaCapitoloUGest().getNumeroCapitolo());
			cap.setNumeroArticolo(req.getRicercaSinteticaCapitoloUGest().getNumeroArticolo());
			impostaSacDaRicercaSintetica(req.getRicercaSinteticaCapitoloUGest().getCodiceStrutturaAmmCont());
			model.setCapitolo(cap);
			impostaStartPosition();
			return;
		}
	}

	
	private void impostaStartPosition() {
		final String methodName = "execute";
		int startPosition = 0;
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
			startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName,"startPosition = "+startPosition);
	}

	private void impostaSacDaRicercaSintetica(String codiceStrutturaAmmCont) {
		if(StringUtils.isBlank(codiceStrutturaAmmCont)) {
			return;
		}
		List<StrutturaAmministrativoContabile> lista = ottieniListaSac();
		StrutturaAmministrativoContabile utility = new StrutturaAmministrativoContabile();
		utility.setCodice(codiceStrutturaAmmCont);
		StrutturaAmministrativoContabile sac =  ComparatorUtils.findByCodiceWithChildren(lista,utility);
		model.setStrutturaAmministrativoContabile(sac);
		
	}
	private void impostaValoreSAC() {
		if(!idEntitaPresente(model.getStrutturaAmministrativoContabile())) {
			return;
		}
		List<StrutturaAmministrativoContabile> lista = ottieniListaSac();
		StrutturaAmministrativoContabile foundSAC = ComparatorUtils.searchByUidWithChildren(lista, model.getStrutturaAmministrativoContabile());
		model.setStrutturaAmministrativoContabile(foundSAC);
		String codiceTipoSAC = foundSAC.getTipoClassificatore() != null && StringUtils.isNotBlank(foundSAC.getTipoClassificatore().getCodice())? 
				foundSAC.getTipoClassificatore().getCodice() 
					: StringUtils.defaultIfBlank(model.getCodiceTipoClassificatoreStrutturaAmministrativoContabile(), "");
		model.setCodiceTipoClassificatoreStrutturaAmministrativoContabile(codiceTipoSAC);
		
		
	}


	protected List<StrutturaAmministrativoContabile> ottieniListaSac() {
		List<StrutturaAmministrativoContabile> lista = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		if(lista != null) {
			return lista;
		}
		LeggiStrutturaAmminstrativoContabile request = model.creaRequestLeggiStrutturaAmminstrativoContabile();
		LeggiStrutturaAmminstrativoContabileResponse response = classificatoreService.leggiStrutturaAmminstrativoContabile(request);
		if(response.hasErrori()) {
			addErrori(response);
			return new ArrayList<StrutturaAmministrativoContabile>();
		}
		sessionHandler.setParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE, response.getListaStrutturaAmmContabile());
		return response.getListaStrutturaAmmContabile();
	}
	
	
	/**
	 * Caricamento dell'azione per la gestione della SAC
	 */
	private void caricaAzionePerSAC() {
		model.setNomeAzioneSAC(AzioneConsentitaEnum.PREVISIONE_IMPEGNATO_ACCERTATO.getNomeAzione());
	}
	
	
	public void validateRicercaCapitoliSpesa() {
		validaRicerca();
	}


	protected void validaRicerca() {
		Capitolo<?, ?> capitoloDaValidare = model.getCapitolo();
		if(capitoloDaValidare == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo"));
			return;
		}
		checkNotNull(capitoloDaValidare.getTipoCapitolo(), "Tipo");
	}
	public void validateRicercaCapitoliEntrata() {
		validaRicerca();
	}
	
	/**
	 * Metodo di ricerca di dettaglio per il Capitolo di Uscita Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String ricercaCapitoliEntrata() {
		final String methodName = "ricercaDettaglio";
		model.setDaConsultaEntrata(false);
		model.setDaConsultaSpesa(false);
		impostaValoreSAC();
		
		RicercaSinteticaCapitoloEntrataGestione request = model.creaRequestRicercaSinteticaCapitoloEntrataGestione();
		logServiceRequest(request);
		
		RicercaSinteticaCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(request);
		logServiceResponse(response);
		 
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		// Ricordare pulire correttamente la sessione 
		sessionHandler.cleanAllSafely();
		
		/*
		 * Mette in sessione:
		 * 		1.	La lista dei risultati : per poter visualizzare i dati trovati nella action di risultatiRicerca
		 * 		2. 	La request usata per chiamare il servizio di ricerca : per poterla riusare allo scopo di 
		 * 				reperire un nuovo blocco di risultati
		 */
		// Workaround per sopperire al fatto che le requests non siano serializzabili
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO, request);
		
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE, response.getCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE_IMPORTI, response.getTotaleImporti());
		sessionHandler.setParametro(BilSessionParameter.TIPO_CAPITOLO, TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE);
		
//		return  SUCCESS + "_entrata";
		return  SUCCESS;
	}
	
	/**
	 * Metodo di ricerca di dettaglio per il Capitolo di Uscita Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String ricercaCapitoliSpesa() {
		final String methodName = "ricercaDettaglio";
		
		impostaValoreSAC();
		model.setDaConsultaEntrata(false);
		model.setDaConsultaSpesa(false);
		
		RicercaSinteticaCapitoloUscitaGestione request = model.creaRequestRicercaSinteticaCapitoloUscitaGestione();
		logServiceRequest(request);
			
		RicercaSinteticaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		// Ricordare pulire correttamente la sessione 
		sessionHandler.cleanAllSafely();

		/*
		 * Mette in sessione:
		 * 		1.	La lista dei risultati : per poter visualizzare i dati trovati nella action di risultatiRicerca
		 * 		2. 	La request usata per chiamare il servizio di ricerca : per poterla riusare allo scopo di 
		 * 				reperire un nuovo blocco di risultati
		 */
		// Workaround per sopperire al fatto che le requests non siano serializzabili
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO, request);
		
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE, response.getCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE_IMPORTI, response.getTotaleImporti());
		sessionHandler.setParametro(BilSessionParameter.TIPO_CAPITOLO, TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
		
		return SUCCESS; // + "_spesa";
	}
	
}

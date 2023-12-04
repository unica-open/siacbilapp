/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.provvedimento;

import java.util.Arrays;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.provvedimento.RicercaProvvedimentoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di action per la ricerca del provvedimento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 26/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaProvvedimentoAction extends GenericBilancioAction<RicercaProvvedimentoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4469535316703691322L;
	
	@Autowired private transient ProvvedimentoService provvedimentoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaClassificatori();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Ricerca con operazioni il provvedimento.
	 * 
	 * @return la stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaConOperazioniCDU() {
		final String methodName = "ricercaConOperazioniCDU";
		
		// Popolo le codifiche
		popolaCodificheDaSessione();
		
		log.debug(methodName, "Creazione della request");
		RicercaProvvedimento req = model.creaRequestRicercaProvvedimento();
		logServiceRequest(req);
		RicercaProvvedimentoResponse res = provvedimentoService.ricercaProvvedimentoConParametri(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio RicercaProvvedimento");
			addErrori(res);
			return INPUT;
		}
		
		if(res.getListaAttiAmministrativi().isEmpty()) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PROVVEDIMENTO, req);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_PROVVEDIMENTO, res.getListaAttiAmministrativi());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	/**
	 * Ricerca senza operazioni il provvedimento.
	 * 
	 * @return la stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaSenzaOperazioniCDU() {
		final String methodName = "ricercaSenzaOperazioniCDU";
		log.info(methodName, "begin");
				
		
		// Popolo le codifiche
		popolaCodificheDaSessione();
		
		log.debug(methodName, "Creazione della request");
		RicercaProvvedimento req = model.creaRequestRicercaProvvedimento();
		logServiceRequest(req);
		RicercaProvvedimentoResponse res = provvedimentoService.ricercaProvvedimento(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio di RicercaProvvedimento");
			addErrori(methodName, res);
			// Restituisco comunque il successo
			return INPUT;
		}
		
		if(res.getListaAttiAmministrativi().isEmpty()) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore(""));
			// Restituisco comunque il successo
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		
		// Imposto i provvedimenti nel model
		log.debug(methodName, "Creo i wrapper da injettare nel JSON");
		model.impostaListaElementoProvvedimento(res.getListaAttiAmministrativi());
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_PROVVEDIMENTO, res.getListaAttiAmministrativi());
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link RicercaProvvedimentoAction#ricercaConOperazioniCDU()}.
	 */
	public void validateRicercaConOperazioniCDU() {
		AttoAmministrativo aa = model.getAttoAmministrativo();
		try {
			checkCondition(aa != null, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(), true);
		} catch(ParamValidationException e) {
			return;
		}
		
		checkCondition(aa.getAnno() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno"));
		checkCondition(aa.getNumero() != 0 || (model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("necessario inserire il numero atto oppure il tipo atto"));
	}
	
	/**
	 * Validazione per il metodo {@link RicercaProvvedimentoAction#ricercaSenzaOperazioniCDU()}.
	 */
	public void validateRicercaSenzaOperazioniCDU() {
		AttoAmministrativo aa = model.getAttoAmministrativo();
		checkCondition(aa != null, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(), true);
		
		log.info("validateRicercaSenzaOperazioniCDU", "condizione " + (aa.getNumero() != 0 || (model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0)));

		checkCondition(aa.getNumero() != 0 || (model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("necessario inserire il numero atto oppure il tipo atto"));
	
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle liste dei classificatori.
	 */
	private void caricaClassificatori() {
		// Carico i tipi di provvedimento
		TipiProvvedimento req = model.creaRequestTipiProvvedimento();
		TipiProvvedimentoResponse res = provvedimentoService.getTipiProvvedimento(req);
		model.setListaTipoAtto(res.getElencoTipi());
		ComparatorUtils.sortByCodice(model.getListaTipoAtto());
		// Carico gli stati operativi
		model.setListaStatoOperativo(Arrays.asList(StatoOperativoAtti.values()));
	}
	
	/**
	 * Popola le codifiche a partire dalla lista caricata in sessione.
	 */
	private void popolaCodificheDaSessione() {
		List<TipoAtto> listaTipiAttoInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		TipoAtto tipoAtto = ComparatorUtils.searchByUid(listaTipiAttoInSessione, model.getTipoAtto());
		// Imposto il tipoAtto nel model
		model.setTipoAtto(tipoAtto);
		
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabileInSessione =
				sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		StrutturaAmministrativoContabile strutturaAmministrativoContabile =
				ComparatorUtils.searchByUidWithChildren(listaStrutturaAmministrativoContabileInSessione, model.getStrutturaAmministrativoContabile());
		model.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
	}

}

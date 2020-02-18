/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.AttoDiLeggeService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLeggeResponse;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaClassificatoriERestituisciSeSingolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaClassificatoriERestituisciSeSingoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiopeResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCategoriaCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCategoriaCapitoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Superclasse per la action del Capitolo.
 * <br>
 * Si occupa di definire i metodi comuni per le actions del Capitolo, con attenzione a:
 * <ul>
 *     <li>aggiornamento</li>
 *     <li>inserimento</li>
 *     <li>ricerca</li>
 * </ul>
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 20/08/2013
 * 
 * @param <M> la parametrizzazione del Model
 */
public abstract class CapitoloAction<M extends CapitoloModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8895560114406942698L;

	/** Serviz&icirc; deli classificatori bil */
	@Autowired /* @CachedService */protected transient ClassificatoreBilService classificatoreBilService;
	/** Serviz&icirc; del capitolo */
	@Autowired protected transient CapitoloService capitoloService;
	/** Serviz&icirc; dell'atto di legge */
	@Autowired /* @CachedService */protected transient AttoDiLeggeService attoDiLeggeService;
	/** Serviz&icirc; della variazione di bilancio */
	@Autowired protected transient VariazioneDiBilancioService variazioneDiBilancioService;

	/* Metodi di utilita' */

	/**
	 * Inizializzazione delle liste. Tale inizializzazione avviene in due step:
	 * <ul>
	 *     <li>si ottengono le liste dalla sessione;</li>
	 *     <li>se le liste fornite dalla sessione sono nulle, allora le si carica con chiamata al WebService.</li>
	 * </ul>
	 * 
	 * @param codiceTipoElementoBilancio il codice dell'elemento di bilancio rispetto cui caricare i classificatori
	 */
	protected void caricaListaCodifiche(BilConstants codiceTipoElementoBilancio) {
		caricaListaCodificheDiBase(codiceTipoElementoBilancio);
	}

	/**
	 * Carica la lista delle codifiche di base
	 * 
	 * @param codiceTipoElementoBilancio il codice dell'elemento di bilancio rispetto cui caricare i classificatori
	 */
	protected void caricaListaCodificheDiBase(BilConstants codiceTipoElementoBilancio) {
		final String methodName = "caricaListaCodificheDiBase";
		
		/* Lista Tipo Atto - Serve per la Gestione degli Atti */
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_DI_LEGGE);

		if (listaTipoAtto == null) {
			log.debug(methodName, "Caricamento lista tipi di atto");
			RicercaTipiAttoDiLeggeResponse responseAL = getListaTipoAttoDiLegge();
			listaTipoAtto = responseAL.getElencoTipi();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_DI_LEGGE, responseAL.getElencoTipi());
		}
		model.setListaTipoAtto(listaTipoAtto);
		
		/* Categoria capitolo */
		List<CategoriaCapitolo> listaCategoriaCapitolo = ottieniListaCategoriaCapitolo(codiceTipoElementoBilancio);
		model.setListaCategoriaCapitolo(listaCategoriaCapitolo);
		
		/* Liste derivate */
		/* Piano dei Conti */
		List<ElementoPianoDeiConti> listaElementoPianoDeiConti = sessionHandler.getParametro(BilSessionParameter.LISTA_ELEMENTO_PIANO_DEI_CONTI);
		if (listaElementoPianoDeiConti != null && !listaElementoPianoDeiConti.isEmpty()) {
			model.setListaElementoPianoDeiConti(listaElementoPianoDeiConti);
		}
		/* Struttura Amministrativo Contabile */
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile =
				sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		if (listaStrutturaAmministrativoContabile != null && !listaStrutturaAmministrativoContabile.isEmpty()) {
			model.setListaStrutturaAmministrativoContabile(listaStrutturaAmministrativoContabile);
		}
	}
	
	/**
	 * Ottiene la lista dela categoria capitolo.
	 * @param codiceTipoElementoBilancio il codice dell'elemento di bilancio rispetto cui caricare i classificatori
	 */
	private List<CategoriaCapitolo> ottieniListaCategoriaCapitolo(BilConstants codiceTipoElementoBilancio) {
		final String methodName = "ottieniListaCategoriaCapitolo";
		String nomeTipo = codiceTipoElementoBilancio.name().substring(7);
		String nomeCostante = "LISTA_CATEGORIA_CAPITOLO_" + nomeTipo;
		BilSessionParameter costanteListaCategoriaCapitolo;
		TipoCapitolo tipoCapitolo;
		try {
			costanteListaCategoriaCapitolo = BilSessionParameter.valueOf(nomeCostante);
			tipoCapitolo = TipoCapitolo.valueOf(nomeTipo);
		} catch(IllegalArgumentException e) {
			log.error(methodName, "Impossibile derivare il campo in cui possa essere salvata la categoria capitolo per il tipo di elemento di bilancio " + codiceTipoElementoBilancio, e);
			return new ArrayList<CategoriaCapitolo>();
		}
		
		List <CategoriaCapitolo> listaCategoriaCapitolo = sessionHandler.getParametro(costanteListaCategoriaCapitolo);
		if(listaCategoriaCapitolo != null) {
			return listaCategoriaCapitolo;
		}
		log.debug(methodName, "Caricamento di categoria capitolo da servizio");
		
		RicercaCategoriaCapitolo request = model.creaRequestRicercaCategoriaCapitolo(tipoCapitolo);
		logServiceRequest(request);
		RicercaCategoriaCapitoloResponse response = capitoloService.ricercaCategoriaCapitolo(request);
		logServiceResponse(response);
		
		/* Imposto le liste nella variabile locale */
		listaCategoriaCapitolo = response.getElencoCategoriaCapitolo();
		if (listaCategoriaCapitolo == null) {
			listaCategoriaCapitolo = new ArrayList<CategoriaCapitolo>();
		}
		
		/* Imposto in sessione le liste */
		sessionHandler.setParametro(costanteListaCategoriaCapitolo, listaCategoriaCapitolo);
		
		return listaCategoriaCapitolo;
	}
	
	/**
	 * Estrae il label, ovvero la descrizione, dalla lista di Classificatori Generici fornita come parametro.
	 * 
	 * @param lista la lista di classificatori generici
	 * @param numeroClassificatore il numero del classificatore generico
	 * @return il label estratto
	 */
	protected String estraiLabelDaListaClassificatoreGenerico(List<ClassificatoreGenerico> lista, int numeroClassificatore) {
		if (lista.isEmpty()) {
			log.debug("estraiLabelDaListaClassificatoreGenerico", "La lista dei classificatori generici numero " + numeroClassificatore + " risulta vuota");
			return null;
		}
		return lista.get(0).getTipoClassificatore().getDescrizione();
	}

	/* Response */
	/**
	 * @method RicercaTipiAttoDiLeggeResponse
	 * @return la response corrispondente
	 */
	protected RicercaTipiAttoDiLeggeResponse getListaTipoAttoDiLegge() {
		RicercaTipiAttoDiLegge request = model.creaRequestRicercaTipiAttoDiLegge();
		logServiceRequest(request);
		RicercaTipiAttoDiLeggeResponse response = attoDiLeggeService.getTipiAttoLegge(request);
		logServiceResponse(response);
		return response;
	}

	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) {
		LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(request);
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
		logServiceResponse(response);
		return response;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriGenericiByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriGenericiByTipoElementoBilResponse ottieniResponseLeggiClassificatoriGenericiByTipoElementoBil(String codice) {
		LeggiClassificatoriGenericiByTipoElementoBil request = model.creaRequestLeggiClassificatoriGenericiByTipoElementoBil(codice);
		return classificatoreBilService.leggiClassificatoriGenericiByTipoElementoBil(request);
	}

	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di {@link LeggiClassificatoriBilByIdPadreResponse}.
	 * 
	 * @param idPadre l'uid del classificatore padre
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriBilByIdPadreResponse ottieniResponseLeggiClassificatoriBilByIdPadre(int idPadre) {
		LeggiClassificatoriBilByIdPadre request = model.creaRequestLeggiClassificatoriBilByIdPadre(idPadre);
		return classificatoreBilService.leggiClassificatoriByIdPadre(request);
	}

	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di {@link LeggiTreePianoDeiContiResponse}.
	 * 
	 * @param idPadre l'uid del classificatore padre
	 * @return la response corrispondente
	 */
	protected LeggiTreePianoDeiContiResponse ottieniResponseLeggiTreePianoDeiConti(int idPadre) {
		LeggiTreePianoDeiConti request = model.creaRequestLeggiTreePianoDeiConti(idPadre);
		return classificatoreBilService.leggiTreePianoDeiConti(request);
	}

	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di {@link LeggiTreeSiopeResponse} per il Siope di Entrata.
	 * 
	 * @param idPadre l'uid del classificatore padre
	 * @return la response corrispondente
	 */
	protected LeggiTreeSiopeResponse ottieniResponseLeggiTreeSiopeEntrata(int idPadre) {
		LeggiTreeSiope request = model.creaRequestLeggiTreeSiope(idPadre);
		return classificatoreBilService.leggiTreeSiopeEntrata(request);
	}

	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di {@link LeggiTreeSiopeResponse} per il Siope di Spesa.
	 * 
	 * @param idPadre l'uid del classificatore padre
	 * @return la response corrispondente
	 */
	protected LeggiTreeSiopeResponse ottieniResponseLeggiTreeSiopeSpesa(int idPadre) {
		LeggiTreeSiope request = model.creaRequestLeggiTreeSiope(idPadre);
		return classificatoreBilService.leggiTreeSiopeSpesa(request);
	}

	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di {@link ControllaClassificatoriModificabiliCapitolo}.
	 * 
	 * @param capitolo il capitolo di cui cercare i classificatori editabili
	 * @param tipo il tipo del capitolo
	 * 
	 * @return la response corrispondente
	 */
	protected ControllaClassificatoriModificabiliCapitoloResponse ottieniResponseControllaClassificatoriModificabiliCapitolo(Capitolo<?, ?> capitolo, TipoCapitolo tipo) {
		final String methodName = "ottieniResponseControllaClassificatoriModificabiliCapitolo";
		log.debug(methodName, "Controllo quali classificatori siano modificabili");
		ControllaClassificatoriModificabiliCapitolo request = model.creaRequestControllaClassificatoriModificabiliCapitolo(capitolo, tipo);
		logServiceRequest(request);

		log.debug(methodName, "Richiamo il WebService di controllo dei classificatori");
		ControllaClassificatoriModificabiliCapitoloResponse response = capitoloService.controllaClassificatoriModificabiliCapitolo(request);
		logServiceResponse(response);
		return response;
	}

	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di {@link ControllaAttributiModificabiliCapitoloResponse}.
	 * 
	 * @param capitolo il capitolo di cui cercare i classificatori editabili
	 * @param tipo il tipo del capitolo
	 * 
	 * @return la response corrispondente
	 */
	protected ControllaAttributiModificabiliCapitoloResponse ottieniResponseControllaAttributiModificabiliCapitolo(Capitolo<?, ?> capitolo, TipoCapitolo tipo) {
		final String methodName = "ottieniResponseControllaAttributiModificabiliCapitolo";
		log.debug(methodName, "Controllo quali attributi siano modificabili");
		ControllaAttributiModificabiliCapitolo request = model.creaRequestControllaAttributiModificabiliCapitolo(capitolo, tipo);
		logServiceRequest(request);

		log.debug(methodName, "Richiamo il WebService di controllo degli attributi");
		ControllaAttributiModificabiliCapitoloResponse response = capitoloService.controllaAttributiModificabiliCapitolo(request);
		logServiceResponse(response);
		return response;
	}

	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di {@link ControllaClassificatoriModificabiliCapitolo}.
	 * 
	 * @param capitolo il capitolo di cui cercare i classificatori editabili
	 * 
	 * @return la response corrispondente
	 */
	protected ControllaClassificatoriModificabiliCapitoloResponse ottieniResponseControllaClassificatoriModificabiliCapitolo(Capitolo<?, ?> capitolo) {
		return ottieniResponseControllaClassificatoriModificabiliCapitolo(capitolo, capitolo.getTipoCapitolo());
	}

	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di {@link ControllaAttributiModificabiliCapitolo}.
	 * 
	 * @param capitolo il capitolo di cui cercare i classificatori editabili
	 * 
	 * @return la response corrispondente
	 */
	protected ControllaAttributiModificabiliCapitoloResponse ottieniResponseControllaAttributiModificabiliCapitolo(Capitolo<?, ?> capitolo) {
		return ottieniResponseControllaAttributiModificabiliCapitolo(capitolo, capitolo.getTipoCapitolo());
	}

	/**
	 * Imposta i dati del capitolo creato per la variazione.
	 * @param <C> la tipizzazione del capitolo
	 * 
	 * @param capitoloDaImpostare il capitolo che deve essere impostato per la variazione
	 */
	protected <C extends Capitolo<?, ?>> void impostaDatiPerVariazioni(C capitoloDaImpostare) {
		final String methodName = "impostaDatiPerVariazioni";

		log.debug(methodName, "Imposto nella lista delle variazioni presente in sessione il capitolo appena inserito");
		boolean daAggiorna = sessionHandler.getParametro(BilSessionParameter.INSERISCI_NUOVO_DA_AGGIORNAMENTO) != null;
		BilSessionParameter modelDaConsiderare = ottieniModelDaConsiderare(daAggiorna);

		Object modelInSessione = sessionHandler.getParametro(modelDaConsiderare);

		Integer uidVariazione = (Integer) ReflectionUtil.getField(modelInSessione, "uidVariazione");
		model.setUidVariazioneCapitolo(uidVariazione.intValue());
		
		InserisciDettaglioVariazioneImportoCapitolo req = model.creaRequestInserisciDettaglioVariazioneImportoCapitolo(capitoloDaImpostare.getUid());
		log.debug(methodName, "richiamo il servizio di inserimento");
		//*
		InserisciDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.inserisciDettaglioVariazioneImportoCapitolo(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "invocazione terminata con esito fallimento");
			addErrori(res);
			
		}

		// Reimposto la action aggiornata in sessione
		sessionHandler.setParametro(modelDaConsiderare, modelInSessione);
	}

	private BilSessionParameter ottieniModelDaConsiderare(boolean daAggiorna) {
		if(daAggiorna){
			return model.isGestioneUEB()? BilSessionParameter.MODEL_AGGIORNA_VARIAZIONE_IMPORTI_UEB : BilSessionParameter.MODEL_AGGIORNA_VARIAZIONE_IMPORTI;
		}
		
		return model.isGestioneUEB()? BilSessionParameter.MODEL_INSERISCI_VARIAZIONE_IMPORTI_UEB : BilSessionParameter.MODEL_INSERISCI_VARIAZIONE_IMPORTI;
	}

	/**
	 * Controlla la presenza dell'atto di legge.
	 * @param attoDiLegge l'atto da validare
	 * @return <code>true</code> se la ricerca per atto di legge &eacute; valida; <code>false</code> altrimenti
	 */
	protected boolean checkAttoDiLegge(AttoDiLegge attoDiLegge) {
		final String methodName = "checkAttoDiLegge";
		Integer annoAttoDiLegge = attoDiLegge.getAnno();
		Integer numeroAttoDiLegge = attoDiLegge.getNumero();
		String codiceTipoAttoDiLegge = attoDiLegge.getTipoAtto().getCodice();
		if (annoAttoDiLegge == null && numeroAttoDiLegge == null && codiceTipoAttoDiLegge.isEmpty()) {
			log.debug(methodName, "Non c'è nessun criterio di ricerca corrispondente, dunque ignoro il tutto");
		} else if (annoAttoDiLegge == null) {
			addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno Atto di Legge"));
		} else if (numeroAttoDiLegge == null && codiceTipoAttoDiLegge.isEmpty()) {
			addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero o Tipo atto"));
		} else {
			log.debug(methodName, "Atto di legge valido");
			return true;
		}
		return false;
	}

	/**
	 * Controlla la presenza del capitolo.
	 * 
	 * @param capitolo il capitolo da validare
	 * 
	 * @return <code>true</code> se la ricerca per capitolo &eacute; valida; <code>false</code> altrimenti
	 */
	protected boolean checkCapitolo(Capitolo<?, ?> capitolo) {
		return capitolo.getNumeroCapitolo() != null
				|| capitolo.getNumeroArticolo() != null
				|| capitolo.getNumeroUEB() != null
				|| StringUtils.isNotBlank(capitolo.getDescrizione())
				|| StringUtils.isNotBlank(capitolo.getDescrizioneArticolo())
				|| capitolo.getExAnnoCapitolo() != null
				|| capitolo.getExCapitolo() != null
				|| capitolo.getExArticolo() != null
				|| capitolo.getExUEB() != null;
	}

	/**
	 * Controlla se la categoria del capitolo sia <code>STANDARD</code>
	 * 
	 * @param categoriaCapitolo la categoria da controllare
	 * @return <code>true</code> se la categoria &eacute; <code>STANDARD</code>; <code>false</code> altrimenti
	 */
	protected boolean isCategoriaCapitoloStandard(CategoriaCapitolo categoriaCapitolo) {
		CategoriaCapitolo categoriaCapitoloStandard = ComparatorUtils.findByCodice(model.getListaCategoriaCapitolo(), BilConstants.CODICE_CATEGORIA_CAPITOLO_STANDARD.getConstant());
		return categoriaCapitolo != null && categoriaCapitoloStandard != null && categoriaCapitolo.getUid() == categoriaCapitoloStandard.getUid();
	}
	
	/**
	 * Controlla se la categoria del capitolo sia di tipo <code>FONDO PLURIENNALE VINCOLATO</code>
	 * 
	 * @param categoriaCapitolo la categoria da controllare
	 * @return <code>true</code> se la categoria &eacute; <code>FONDO PLURIENNALE VINCOLATO</code>; <code>false</code> altrimenti
	 */
	protected boolean isCategoriaCapitoloFondoPluriennaleVincolato(CategoriaCapitolo categoriaCapitolo) {
		CategoriaCapitolo categoriaCapitoloFondoPluriVincolato = ComparatorUtils.findByCodice(model.getListaCategoriaCapitolo(), BilConstants.CODICE_CATEGORIA_CAPITOLO_FONDO_PLURIENNALE_VINCOLATO.getConstant());
		return categoriaCapitolo != null && categoriaCapitoloFondoPluriVincolato != null && categoriaCapitolo.getUid() == categoriaCapitoloFondoPluriVincolato.getUid();
	}

	/**
	 * Metodo di utilit&agrave; per la validazione degli importi per il Capitolo.
	 * 
	 * @param importi                l'Importo da validare
	 * @param annoRispettoAEsercizio lo scostamento dell'anno dell'Importo rispetto all'anno di esercizio
	 * @param controlloGlobale       variabile che definisce se effettuare un controllo su tutti i parametri o solo su alcuni
	 * @param forceCassaCoherence    forza la coerenza dell'importo di cassa: <code>cassa &le; competenza + residuo</code>
	 */
	protected void validaImportoCapitolo(ImportiCapitolo importi, int annoRispettoAEsercizio, boolean controlloGlobale, boolean forceCassaCoherence) {
		int anno = model.getAnnoEsercizioInt() + annoRispettoAEsercizio;
		if(importi == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Competenza per anno " + anno));
			if(controlloGlobale) {
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Residui per anno " + anno));
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Cassa per anno " + anno));
			}
			return;
		}
		
		// Gli importi sono valorizzati
		checkImporto(importi.getStanziamento(), "Competenza", anno);
		if(controlloGlobale) {
			checkImporto(importi.getStanziamentoResiduo(), "Residui", anno);
			checkImporto(importi.getStanziamentoCassa(), "Cassa", anno);
			if(forceCassaCoherence && importi.getStanziamento() != null && importi.getStanziamentoResiduo() != null && importi.getStanziamentoCassa() != null) {
				checkCondition(importi.getStanziamentoCassa().subtract(importi.getStanziamento()).subtract(importi.getStanziamentoResiduo()).signum() <= 0,
						ErroreCore.VALORE_NON_VALIDO.getErrore("Cassa per anno " + anno, ": deve essere inferiore o uguale alla somma di competenza e residuo ("
							+ FormatUtils.formatCurrency(importi.getStanziamento().add(importi.getStanziamentoResiduo())) + ")"));
			}
		}
	}

	/**
	 * Controlla che l'importo sia valido, ovvero &ge; 0;
	 * 
	 * @param importo l'importo da controllare
	 * @param name    il nome del campo
	 * @param anno    l'anno
	 */
	protected void checkImporto(BigDecimal importo, String name, int anno) {
		checkCondition(importo != null && importo.signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore(name + " per anno " + anno, ": minore di zero"));
	}
	
	/**
	 * CR-2559.
	 * Ottiene il Siope nel caso in cui sia unico
	 * @param <T> la tipizzazione della codifica
	 * @param tipologiaClassificatore la tipologia del SIOPE
	 * @return il SIOPE nel caso in cui sia univoco
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Codifica> T ottieniSiopeSeUnico(TipologiaClassificatore tipologiaClassificatore) {
		final String methodName = "ottieniSiopeSeUnico";
		ContaClassificatoriERestituisciSeSingolo request = model.creaRequestContaClassificatoriERestituisciSeSingolo(tipologiaClassificatore);
		logServiceRequest(request);
		ContaClassificatoriERestituisciSeSingoloResponse response = classificatoreBilService.contaClassificatoriERestituisciSeSingolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			// Ignoro e proseguo
			log.debug(methodName, createErrorInServiceInvocationString(request, response));
			return null;
		}
		log.debug(methodName, "Numero di classificatori trovati: " + response.getCount());
		return (T) response.getCodifica();
	}
	
	/**
	 * Ottiene la stringa di errore per le variazioni non definitive collegate
	 * @param variazioniImporti le variazioni di importi
	 * @param variazioniCodifiche le variazioni di codifiche
	 * @return la stringa di errore
	 */
	protected String getStringaErroreVariazioniNonDefinitiveCollegate(List<Integer> variazioniImporti, List<Integer> variazioniCodifiche) {
		List<String> components = new ArrayList<String>();
		
		if(!variazioniImporti.isEmpty()){
			List<String> tmp = new ArrayList<String>();
			for(Integer num : variazioniImporti) {
				tmp.add("n. " + num.toString());
			}
			components.add("importi " + StringUtils.join(tmp,  ", "));
		}
		
		if(!variazioniCodifiche.isEmpty()){
			List<String> tmp = new ArrayList<String>();
			for(Integer num : variazioniCodifiche) {
				tmp.add("n. " + num.toString());
			}
			components.add("codifiche " + StringUtils.join(tmp,  ", "));
		}
		
		return new StringBuilder()
			.append(": ")
			.append(StringUtils.join(components, ", "))
			.toString();
	}
	
	/**
	 * Trova il classificatore per il codice fornito
	 * @param <C> la tipizzazione del classificatore gerarchico
	 * @param classificatore il classificatore da ricercare
	 * @param tipologiaClassificatore la tipologia del classificatore
	 * @param tipoClassificatore il tipo di classificatore
	 * @return il classificatore ricercato
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	@SuppressWarnings("unchecked")
	protected <C extends ClassificatoreGerarchico> C findClassificatoreByCodice(C classificatore, TipologiaClassificatore tipologiaClassificatore, String tipoClassificatore) throws WebServiceInvocationFailureException {
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno req = model.creaRequestLeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(classificatore, tipologiaClassificatore);
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse res = classificatoreBilService.leggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		if(res.getClassificatore() == null) {
			Errore errore = ErroreCore.ENTITA_NON_TROVATA.getErrore(tipoClassificatore, classificatore.getCodice());
			addErrore(errore);
			throw new WebServiceInvocationFailureException(errore.getTesto());
		}
		return (C) res.getClassificatore();
	}
	
	/**
	 * Estrazione del label del classificatore by tipo
	 * @param tipoClassificatore il tipo
	 * @return il label
	 */
	protected String estraiLabelByTipo(TipoClassificatore tipoClassificatore) {
		return tipoClassificatore != null ? tipoClassificatore.getDescrizione() : null;
	}
	
	/**
	 * SIAC-6884
	 * Estrazione di tutti i settori dato in input una direzione
	 * 
	 * 
	 * */
	protected List<Map<String, String>> getListaSettoriDirezione(StrutturaAmministrativoContabile direzioneProponenteVariazione, List<Map<String, String>> listaSettori) {
		List<StrutturaAmministrativoContabile> figlioSub = direzioneProponenteVariazione.getSubStrutture();
		
		if(figlioSub != null && figlioSub.isEmpty()){
			Map<String, String> mMap = new HashMap<String, String>();
			mMap.put(direzioneProponenteVariazione.getTipoClassificatore().getCodice(), direzioneProponenteVariazione.getCodice());
			listaSettori.add(mMap);
		}else{
			for(int i=0; i<figlioSub.size(); i++){
				getListaSettoriDirezione(figlioSub.get(i), listaSettori);
			}
			
			
		}
		return listaSettori;
	}
	
	/**SIAC-6884
	 * Dato in input un codice di direzione, permette di ottenere la SAC corrispondente con tutte le substrutture*/
	protected StrutturaAmministrativoContabile getSacDirezioneProponente(String direzioneProponenteVarSel, List<StrutturaAmministrativoContabile> listaSac) {		
		for(StrutturaAmministrativoContabile sacDaOttenere : listaSac){
			if(sacDaOttenere.getCodice().equals(direzioneProponenteVarSel)){
				return sacDaOttenere;
			}
		}
		return null;
	}
	
	/* SIAC-6884
	 Restituisce true/false se substruttura **/
	//Controlla se la sac si trova nella sottostruttura della proponente
		protected boolean isSubStruttura(StrutturaAmministrativoContabile sac,
				StrutturaAmministrativoContabile direzioneProponenteVariazione) {
			
			List<Map<String, String>> listaSettori = new ArrayList<Map<String, String>>();
			List<Map<String, String>> listaSettoriDaDirezione = getListaSettoriDirezione(direzioneProponenteVariazione, listaSettori);
			
			
			String tipoClassificatoreDirezione = sac.getTipoClassificatore() != null ? sac.getTipoClassificatore().getCodice() : null;
			String tipoDirezione = direzioneProponenteVariazione.getTipoClassificatore() != null ? direzioneProponenteVariazione.getTipoClassificatore().getCodice() : null;
			if(direzioneProponenteVariazione != null && sac.getCodice().equals(direzioneProponenteVariazione.getCodice()) && tipoClassificatoreDirezione.equals(tipoDirezione)){
				return true;
			}
			
			for(Map<String, String> item: listaSettoriDaDirezione){
				for(Map.Entry<String, String> entry: item.entrySet()){
					if(sac.getCodice().equals(entry.getValue()) && sac.getTipoClassificatore().getCodice().equals(entry.getKey())){
						return true;
					}
				}
		
			} 		
			return false;
		}
	
		/**
		 * 
		 * SIAC-6884
		 * Controlla se SAC Afferente ad utente e a settore variazione */
		protected boolean sacAfferente(StrutturaAmministrativoContabile sac, List<StrutturaAmministrativoContabile> struttureAccount, String direzioneProponenteVarSel) {		
			boolean isAssociato = false;		
			for(StrutturaAmministrativoContabile sacUser : struttureAccount){
				if(sac.getCodice().equals(sacUser.getCodice())){
					isAssociato = true;
					break;
				}	
			}
			
			
			//SECONDA PARTE
			
			List<StrutturaAmministrativoContabile> listSac = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
			StrutturaAmministrativoContabile direzioneProponenteVariazione = getSacDirezioneProponente(direzioneProponenteVarSel, listSac);
			
			
			
			boolean isFiglio = isSubStruttura(sac, direzioneProponenteVariazione);

			if(isAssociato && isFiglio){
				return true;
			}
			return false;
		}
}

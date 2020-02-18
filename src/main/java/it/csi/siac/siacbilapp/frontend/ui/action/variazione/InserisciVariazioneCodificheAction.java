/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneCodifiche;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCategoriaCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCategoriaCapitoloResponse;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di action per la variazione delle codifiche.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 18/10/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciVariazioneCodificheAction extends InserisciVariazioneBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6031986078368879771L;
	
	@Autowired private transient ClassificatoreService classificatoreService;
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	@Override
	public void prepareEnterStep2() {
		// Inizializzazione del model
		try {
			super.prepareExecute();
		} catch(Exception e) {
			throw new IllegalArgumentException("Errore nel Prepare", e);
		}
		model.getScelta().setSceltaSt("CODIFICHE");
		super.prepareEnterStep2();
		
		// Imposto il valore di default
		model.getDefinisci().setTipoVariazione(TipoVariazione.VARIAZIONE_CODIFICA);
	}

	@Override
	@AnchorAnnotation(value = "%{cdu}", name="Variazioni STEP 2", afterAction = true)
	public String enterStep2() {
		model.getDefinisci().setAzioneCuiRedirigere("esecuzioneStep2InserimentoVariazioniCodifiche");
		//SIAC-6883
//		model.getDefinisci().setAnnoVariazioneAbilitato(Boolean.FALSE);
		return SUCCESS;
	}

	@Override
	public String enterStep3() {
		//SIAC-6883
//		model.getSpecificaCodifiche().setAnnoCapitolo(model.getDefinisci().getAnnoVariazione());
		return SUCCESS;
	}

	@Override
	public String executeStep3() {
		final String methodName = "executeStep3";
		log.debug(methodName, "Richiamo il servizio per l'inserimento della variazione");
		
		// Creazione della request
		InserisceVariazioneCodifiche request;
		try {
			request = model.creaRequestInserisceVariazioneCodifiche();
		} catch (Exception e) {
			log.error(methodName, "Errore nella creazione della request", e);
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore(e.getMessage()));
			return INPUT;
		}
		
		log.debug(methodName, "Request creata");
		logServiceRequest(request);
		
		InserisceVariazioneCodificheResponse response = variazioneDiBilancioService.inserisceVariazioneCodifiche(request);
		log.debug(methodName, "Servizio richiamato");
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La request si è risolta in un fallimento");
			addErrori(response);
			return INPUT;
		}
		
		// L'invocazione è stata un successo
		log.debug(methodName, "L'invocazione ha avuto esito positivo");
		
		// Imposto il numero della variazione
		model.getSpecificaCodifiche().setNumeroVariazione(response.getVariazioneCodificaCapitolo().getNumero());
		
		if(!Boolean.TRUE.equals(response.getIsProvvedimentoPresente())) {
			addMessaggio(ErroreBil.PROVVEDIMENTO_VARIAZIONE_NON_PRESENTE.getErrore());
		}
		
		if(hasMessaggi()) {
			setMessaggiInSessionePerActionSuccessiva();
		}
		
		return SUCCESS;
	}

	@Override
	public String enterStep4() {
		final String methodName = "enterStep4";
		SpecificaVariazioneCodifiche model3 = model.getSpecificaCodifiche();
		log.debug(methodName, "Impostazione dei dati tra i vari model");
		model.impostaDatiStep4(model3);
		leggiEventualiMessaggiAzionePrecedente();
		return SUCCESS;
	}
	
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* ************ Comunicazioni AJAX ************ */
	
	/**
	 * Legge i capitoli ora presenti nella variazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String leggiCapitoliNellaVariazioneCodifica() {
		return SUCCESS;
	}
	
	/**
	 * Effettua una ricerca per trovare:
	 * <ul>
	 *     <li>l'elenco dei classificatori modificabili</li>
	 *     <li>le liste dei classificatori</li>
	 * </ul>
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String controllaModificabilitaECaricaClassificatori() {
		// Imposto nel campo old il precedente wrapper
		ElementoCapitoloCodifiche elementoCapitoloCodifiche = model.getSpecificaCodifiche().getElementoCapitoloCodifiche();
		// Effettuo le ricerche
		ricercaClassificatoriModificabili(elementoCapitoloCodifiche);
		ricercaAttributiModificabili(elementoCapitoloCodifiche);
		leggiClassificatori(elementoCapitoloCodifiche);
		// Imposto i dati nell'old e cancello i dati dal nuovo
		model.getSpecificaCodifiche().setElementoCapitoloCodificheOld(elementoCapitoloCodifiche);
		model.getSpecificaCodifiche().setElementoCapitoloCodifiche(null);
		return SUCCESS;
	}
	
	/**
	 * Associa un nuovo elemento di variazione di codifiche del capitolo {@link ElementoCapitoloCodifiche} alla variazione.
	 * Chiamato quando l'utente ha modificato le codifiche di un capitolo non ancora associato alla variazione che deve essere pertanto associato.
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String inserisciNellaVariazioneCodifica() {
		final String methodName = "inserisciNellaVariazioneCodifica";
		List<ElementoCapitoloCodifiche> listaElementoCapitoloCodifiche = model.getSpecificaCodifiche().getListaElementoCapitoloCodifiche();
		ElementoCapitoloCodifiche elementoCapitoloCodifiche = model.getSpecificaCodifiche().getElementoCapitoloCodifiche();
		ElementoCapitoloCodifiche elementoCapitoloCodificheOld = model.getSpecificaCodifiche().getElementoCapitoloCodificheOld();
		
		log.debug(methodName, "Ripopolo i dati del nuovo capitolo a partire dal vecchio");
		// Ripopolamento dei campi
		popolaNuovoDaVecchio(elementoCapitoloCodifiche, elementoCapitoloCodificheOld);
		
		StringBuilder sb = new StringBuilder();
		sb.append(obtainUidAndDataFineValidita(elementoCapitoloCodifiche.getElementoPianoDeiConti()))
			.append(obtainUidAndDataFineValidita(elementoCapitoloCodifiche.getStrutturaAmministrativoContabile()))
			.append(obtainUidAndDataFineValidita(elementoCapitoloCodifiche.getTitoloEntrata()))
			.append(obtainUidAndDataFineValidita(elementoCapitoloCodifiche.getTipologiaTitolo()))
			.append(obtainUidAndDataFineValidita(elementoCapitoloCodifiche.getCategoriaTipologiaTitolo()))
			
			.append(obtainUidAndDataFineValidita(elementoCapitoloCodifiche.getTitoloSpesa()))
			.append(obtainUidAndDataFineValidita(elementoCapitoloCodifiche.getProgramma()))
			.append(obtainUidAndDataFineValidita(elementoCapitoloCodifiche.getMacroaggregato()));
		
		log.debug(methodName, sb.toString());
		
		CategoriaCapitolo categoria = ComparatorUtils.searchByUid(model.getSpecificaCodifiche().getListaCategoriaCapitolo(), elementoCapitoloCodifiche.getCategoriaCapitolo());
		elementoCapitoloCodifiche.setCategoriaCapitolo(categoria);
		
		if(!elementoCapitoloCodifiche.validaCodifiche(model.getBilancio())) {			
			log.debug(methodName, "Non tutti i classificatori necessari sono stati inseriti");
			addErrore(ErroreBil.NON_TUTTI_I_CAMPI_DI_UN_CAPITOLO_ASSOCIATO_AD_UNA_VARIAZIONE_SONO_STATI_VALORIZZATI.getErrore());
			return SUCCESS;
		}
		
		log.debug(methodName, "Controllo che l'elemento selezionato non sia già presente nella lista");
		List<ElementoCapitoloCodifiche> listaElementiNellaVariazione = ComparatorUtils.findByNumeroCapitoloNumeroArticoloAndTipoCapitolo(listaElementoCapitoloCodifiche, elementoCapitoloCodifiche);
		if(!listaElementiNellaVariazione.isEmpty()) {
			log.debug(methodName, "Elemento già presente");
			addErrore(ErroreBil.CAPITOLO_GIA_ASSOCIATO_ALLA_VARIAZIONE.getErrore());
			return SUCCESS;
		}
		
		log.debug(methodName, "Imposto i dati nel model");
		// Imposto i dati nel model
		listaElementoCapitoloCodifiche.add(elementoCapitoloCodifiche);
		model.getSpecificaCodifiche().setElementoCapitoloCodifiche(null);
		model.getSpecificaCodifiche().setElementoCapitoloCodificheOld(null);
		
		return SUCCESS;
	}
	
	/**
	 * Popola il nuovo wrapper a partire dal vecchio.
	 * 
	 * @param elementoCapitoloCodifiche    il nuovo wrapper
	 * @param elementoCapitoloCodificheOld il vecchio wrapper
	 */
	private void popolaNuovoDaVecchio(ElementoCapitoloCodifiche elementoCapitoloCodifiche, ElementoCapitoloCodifiche elementoCapitoloCodificheOld) {
		// Sono da controllare tutti i campi
		Map<TipologiaClassificatore, Boolean> mappaClassificatoriModificabili = model.getSpecificaCodifiche().getMappaClassificatoriModificabili();
		
		boolean pdcEditabile = mappaClassificatoriModificabili.get(TipologiaClassificatore.PDC) || mappaClassificatoriModificabili.get(TipologiaClassificatore.PDC_I) ||
				mappaClassificatoriModificabili.get(TipologiaClassificatore.PDC_II) || mappaClassificatoriModificabili.get(TipologiaClassificatore.PDC_III)
				|| mappaClassificatoriModificabili.get(TipologiaClassificatore.PDC_IV) || mappaClassificatoriModificabili.get(TipologiaClassificatore.PDC_V);
		
		boolean siopeSpesaEditabile = mappaClassificatoriModificabili.get(TipologiaClassificatore.SIOPE_SPESA) || mappaClassificatoriModificabili.get(TipologiaClassificatore.SIOPE_SPESA_I) ||
				mappaClassificatoriModificabili.get(TipologiaClassificatore.SIOPE_SPESA_II) || mappaClassificatoriModificabili.get(TipologiaClassificatore.SIOPE_SPESA_III);
		
		boolean siopeEntrataEditabile = mappaClassificatoriModificabili.get(TipologiaClassificatore.SIOPE_ENTRATA) || mappaClassificatoriModificabili.get(TipologiaClassificatore.SIOPE_ENTRATA_I) ||
				mappaClassificatoriModificabili.get(TipologiaClassificatore.SIOPE_ENTRATA_II) || mappaClassificatoriModificabili.get(TipologiaClassificatore.SIOPE_ENTRATA_III);
		
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, Missione.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.MISSIONE));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, Programma.class,  mappaClassificatoriModificabili.get(TipologiaClassificatore.PROGRAMMA));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, ClassificazioneCofog.class,  mappaClassificatoriModificabili.get(TipologiaClassificatore.CLASSIFICAZIONE_COFOG));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, TitoloSpesa.class,  mappaClassificatoriModificabili.get(TipologiaClassificatore.TITOLO_SPESA));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, Macroaggregato.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.MACROAGGREGATO));
		
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, TitoloEntrata.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.TITOLO_ENTRATA));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, TipologiaTitolo.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.TIPOLOGIA));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, CategoriaTipologiaTitolo.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.CATEGORIA));
		
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, ElementoPianoDeiConti.class, pdcEditabile);
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, SiopeSpesa.class, siopeSpesaEditabile);
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, SiopeEntrata.class, siopeEntrataEditabile);
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, StrutturaAmministrativoContabile.class, true);
		
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, TipoFinanziamento.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.TIPO_FINANZIAMENTO));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, TipoFondo.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.TIPO_FONDO));
		
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, RicorrenteSpesa.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.RICORRENTE_SPESA));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, RicorrenteEntrata.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.RICORRENTE_ENTRATA));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, PerimetroSanitarioSpesa.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.PERIMETRO_SANITARIO_SPESA));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, PerimetroSanitarioEntrata.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, TransazioneUnioneEuropeaSpesa.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.TRANSAZIONE_UE_SPESA));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, TransazioneUnioneEuropeaEntrata.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA));
		controllaCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, PoliticheRegionaliUnitarie.class, mappaClassificatoriModificabili.get(TipologiaClassificatore.POLITICHE_REGIONALI_UNITARIE));
		
		controllaClassificatoriGenerici(elementoCapitoloCodifiche, elementoCapitoloCodificheOld);
		
		controllaCategoriaCapitolo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld);
		
		// Imposto la lista dei sotto-elementi
		elementoCapitoloCodifiche.setListaUidCapitolo(elementoCapitoloCodificheOld.getListaUidCapitolo());
		
		// Imposto i campi testuali e di anagrafica
		impostaIlCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, "uid", Integer.class);
		impostaIlCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, "annoCapitolo", Integer.class);
		impostaIlCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, "numeroCapitolo", Integer.class);
		impostaIlCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, "numeroArticolo", Integer.class);
		impostaIlCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, "descrizioneCapitolo", String.class);
		impostaIlCampo(elementoCapitoloCodifiche, elementoCapitoloCodificheOld, "tipoCapitolo", TipoCapitolo.class);
		
		// Imposto la denominazione
		elementoCapitoloCodifiche.impostaDenominazioneCapitolo();
		
		model.getSpecificaCodifiche().setElementoCapitoloCodifiche(elementoCapitoloCodifiche);
		model.getSpecificaCodifiche().popolaCodifiche();
		
		// Imposta la descrizione delle codifiche
		elementoCapitoloCodifiche.impostaDescrizioneCodifiche();
	}
	
	/**
	 * Controlla la categoria del capitolo
	 * 
	 * @param nuovo   il nuovo wrapper
	 * @param vecchio il vecchio wrapper
	 */
	private void controllaCategoriaCapitolo(ElementoCapitoloCodifiche nuovo, ElementoCapitoloCodifiche vecchio) {
		CategoriaCapitolo nuovoCategoria = nuovo.getCategoriaCapitolo();
		CategoriaCapitolo vecchioCategoria = vecchio.getCategoriaCapitolo();
		
		if(nuovoCategoria == null) {
			// Era non editabile. Reimposto il vecchio valore
			nuovo.setCategoriaCapitolo(vecchioCategoria);
		}
	}
	
	/**
	 * Controlla cosa effettuare con un dato campo.
	 * 
	 * @param nuovo        il nuovo wrapper
	 * @param vecchio      il vecchio wrapper
	 * @param clazz        la classe del campo
	 * @param editabile se il campo sia editabile o meno
	 */
	private void controllaCampo(ElementoCapitoloCodifiche nuovo, ElementoCapitoloCodifiche vecchio, Class<?> clazz, boolean editabile) {
		String methodName = "controllaCampo";
		String nomeCampo = clazz.getSimpleName();
		Method metodoGet = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "get" + nomeCampo);
		Object campoNuovo = ReflectionUtils.invokeMethod(metodoGet, nuovo);
		Object campoVecchio = ReflectionUtils.invokeMethod(metodoGet, vecchio);
		
		Method metodoSet = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "set" + nomeCampo, clazz);
		
		boolean campoNuovoNonPresente = !isPresentAsEntita(campoNuovo);
		
		log.debug(methodName, "nomeCampo : " + nomeCampo);
		log.debug(methodName, nomeCampo + " - campoNuovoNonPresente: " + campoNuovoNonPresente);
		log.debug(methodName, nomeCampo + " - campoVecchioPresente: " + isPresentAsEntita(campoVecchio));
		
		if(!editabile) {
			// Era non editabile
			log.debug(methodName, "nell'if");
			ReflectionUtils.invokeMethod(metodoSet, nuovo, campoVecchio);
		} else if(campoNuovoNonPresente && isPresentAsEntita(campoVecchio)) {
			// Era presente ma l'ho cancellato: imposto la data di fine validità
			Entita entita = (Entita)campoVecchio;
			entita.setDataFineValidita(new Date());
			ReflectionUtils.invokeMethod(metodoSet, nuovo, entita);
			log.debug(methodName, "nel primo else if");
		} else if(!campoNuovoNonPresente){
			Entita entita = (Entita)campoNuovo;
			entita.setDataFineValidita(null);
			ReflectionUtils.invokeMethod(metodoSet, nuovo, entita);
			log.debug(methodName, "nel secondo else if");
		}else {
			log.debug(methodName, "nessuna delle precedenti!");
		}
		log.logXmlTypeObject(nuovo, "nuovo dopo controllo per il campo: " + nomeCampo);
	}
	
	/**
	 * Controlla se il campo &eacute; valorizzato come entita.
	 * 
	 * @param campo il campo da controllare
	 * @return <code>true</code> se il campo &eacute; valorizzato; <code>false</code> in caso contrario
	 */
	private boolean isPresentAsEntita(Object campo) {
		return campo instanceof Entita && ((Entita)campo).getUid() != 0 ;
	}
	
//	/**
//	 * Controlla se il campo &eacute; valorizzato come entita.
//	 * 
//	 * @param campo il campo da controllare
//	 * @return <code>true</code> se il campo &eacute; valorizzato; <code>false</code> in caso contrario
//	 */
//	private boolean isPresentAsEntitaValida(Object campo) {
//		return campo != null && campo instanceof Entita && ((Entita)campo).getUid() != 0
//				&& (((Entita)campo).getDataFineValidita() == null || ((Entita)campo).getDataFineValidita().after(new Date()));
//	}
	
	/**
	 * Controlla cosa effettuare con un classificatore generico.
	 * 
	 * @param nuovo   il nuovo wrapper
	 * @param vecchio il vecchio wrapper
	 */
	private void controllaClassificatoriGenerici(ElementoCapitoloCodifiche nuovo, ElementoCapitoloCodifiche vecchio) {
		Date now = new Date();
		for(int i = 1; i <= 15; i++) {
			Method metodoGet = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "getClassificatoreGenerico" + i);
			Object campoNuovo = ReflectionUtils.invokeMethod(metodoGet, nuovo);
			Object campoVecchio = ReflectionUtils.invokeMethod(metodoGet, vecchio);
			if(campoNuovo == null && campoVecchio != null) {
				// Era presente ma l'ho cancellato: imposto la data di fine validità
				Entita entita = (Entita)campoVecchio;
				entita.setDataFineValidita(now);
				Method metodoSet = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "setClassificatoreGenerico" + i, ClassificatoreGenerico.class);
				ReflectionUtils.invokeMethod(metodoSet, nuovo, entita);
			}
		}
	}
	
	/**
	 * Imposta il campo nel nuovo wrapper.
	 * 
	 * @param nuovo     il nuovo wrapper
	 * @param vecchio   il vecchio wrapper
	 * @param nomeCampo il nome del campo
	 * @param clazz     la classe del campo
	 */
	private void impostaIlCampo(ElementoCapitoloCodifiche nuovo, ElementoCapitoloCodifiche vecchio, String nomeCampo, Class<?> clazz) {
		String nomeCampoCapitalizzato = StringUtils.capitalize(nomeCampo);
		Method metodoGet = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "get" + nomeCampoCapitalizzato);
		Object campoNuovo = ReflectionUtils.invokeMethod(metodoGet, nuovo);
		if(campoNuovo == null) {
			Object campoVecchio = ReflectionUtils.invokeMethod(metodoGet, vecchio);
			Method metodoSet = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "set" + nomeCampoCapitalizzato, clazz);
			ReflectionUtils.invokeMethod(metodoSet, nuovo, campoVecchio);
		}
	}
	
	/**
	 * Ottiene uid e data fine validit&agrave; dell'entita fornita.
	 * 
	 * @param entita     l'entita di cui ottenere i dati
	 * 
	 * @return uid e data fine validita dell'entita, se presente; NULL in caso contrario
	 */
	private String obtainUidAndDataFineValidita(Entita entita) {
		return obtainUidAndDataFineValidita(entita, entita != null ? entita.getClass().getSimpleName() : "");
	}
	
	/**
	 * Ottiene uid e data fine validit&agrave; dell'entita fornita.
	 * 
	 * @param entita     l'entita di cui ottenere i dati
	 * @param entitaNome il nome dell'entita
	 * 
	 * @return uid e data fine validita dell'entita, se presente; NULL in caso contrario
	 */
	private String obtainUidAndDataFineValidita(Entita entita, String entitaNome) {
		return obtainUidAndDataFineValidita(entita, entitaNome, "NULL");
	}
	
	/**
	 * Ottiene uid e data fine validit&agrave; dell'entita fornita.
	 * 
	 * @param entita       l'entita di cui ottenere i dati
	 * @param entitaNome   il nome dell'entita
	 * @param defaultValue il valore di default
	 * 
	 * @return uid e data fine validita dell'entita, se presente; defaultValue in caso contrario
	 */
	private String obtainUidAndDataFineValidita(Entita entita, String entitaNome, String defaultValue) {
		return entita != null
				? entitaNome + entita.getUid() + " - " + entita.getDataFineValidita()
				: defaultValue;
	}
	
	/**
	 * Elimina un elemento dalla lista delle variazioni di codifica.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String eliminaElementoVariazioneCodifica() {
		final String methodName = "eliminaElementoVariazioneCodifica";
		
		log.debug(methodName, "Cerco il capitolo nella variazione");
		ElementoCapitoloCodifiche elementoDaEliminare = model.getSpecificaCodifiche().getElementoCapitoloCodifiche();
		List<ElementoCapitoloCodifiche> lista = model.getSpecificaCodifiche().getListaElementoCapitoloCodifiche();
		
		// Controllo l'indice dell'elemento
		int indice = ComparatorUtils.getIndexByUid(lista, elementoDaEliminare);
		if(indice == -1) {
			log.error(methodName, "Il capitolo non è presente");
			// Non ho trovato l'elemento: lancio un errore
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Elemento non trovato"));
			return SUCCESS;
		}
		
		log.debug(methodName, "Elemento trovato all'indice " + indice);
		// L'elemento è stato trovato: lo elimino
		lista.remove(indice);
		// Svuoto l'elemento dal model
		model.getSpecificaCodifiche().setElementoCapitoloCodifiche(null);
		
		return SUCCESS;
	}
	
	/**
	 * Aggiorna un elemento di variazione codifiche {@link ElementoCapitoloCodifiche} gi&agrave; associato alla variazione di codifica e quindi gia' presente nel model. 
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiornaElementoVariazioneCodifica() {
		final String methodName = "aggiornaElementoVariazioneCodifica";
		
		log.debug(methodName, "Cerco il capitolo da aggiornare");
		ElementoCapitoloCodifiche elementoDaAggiornare = model.getSpecificaCodifiche().getElementoCapitoloCodifiche();
		List<ElementoCapitoloCodifiche> lista = model.getSpecificaCodifiche().getListaElementoCapitoloCodifiche();
		
		// Controllo l'indice dell'elemento
		int indice = ComparatorUtils.getIndexByUid(lista, elementoDaAggiornare);
		if(indice == -1) {
			log.error(methodName, "Non ho trovato il capitolo");
			// Non ho trovato l'elemento: lancio un errore
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Elemento non trovato"));
			return SUCCESS;
		}
		
		log.debug(methodName, "Capitolo trovato all'indice " + indice);
		ElementoCapitoloCodifiche elementoInLista = lista.get(indice);
		
		// Imposto il valore trovato nell'old
		model.getSpecificaCodifiche().setElementoCapitoloCodificheOld(elementoInLista);
		log.debug(methodName, "Ripopolo l'elemento da aggiornare a partire dall'elemento presente il lista");
		// Popolo correttamente l'elemento
		try {
			elementoDaAggiornare = (ElementoCapitoloCodifiche) elementoInLista.clone();
		} catch (CloneNotSupportedException e) {
			log.warn(methodName, "Impossibile clonare: " + e.getMessage());
			elementoDaAggiornare = elementoInLista;
		}
		// Svuoto l'elemento dal model
		model.getSpecificaCodifiche().setElementoCapitoloCodifiche(elementoDaAggiornare);
		
		return SUCCESS;
	}
	
	/**
	 * Aggiorna effettivamente un elemento della lista delle variazioni di codifica. (pulsante inserisciModifica)
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiornamentoElementoVariazioneCodifica() {
		final String methodName = "aggiornamentoElementoVariazioneCodifica";
		List<ElementoCapitoloCodifiche> listaElementoCapitoloCodifiche = model.getSpecificaCodifiche().getListaElementoCapitoloCodifiche();
		ElementoCapitoloCodifiche elementoCapitoloCodifiche = model.getSpecificaCodifiche().getElementoCapitoloCodifiche();
		ElementoCapitoloCodifiche elementoCapitoloCodificheOld = model.getSpecificaCodifiche().getElementoCapitoloCodificheOld();
		
		log.debug(methodName, "Ripopolo i dati del nuovo capitolo a partire dal vecchio");
		// Ripopolamento dei campi
		popolaNuovoDaVecchio(elementoCapitoloCodifiche, elementoCapitoloCodificheOld);
		
		CategoriaCapitolo categoria = ComparatorUtils.searchByUid(model.getSpecificaCodifiche().getListaCategoriaCapitolo(), elementoCapitoloCodifiche.getCategoriaCapitolo());
		elementoCapitoloCodifiche.setCategoriaCapitolo(categoria);
		if(!elementoCapitoloCodifiche.validaCodifiche(model.getBilancio())) {			
			log.debug(methodName, "Non tutti i classificatori necessari sono stati inseriti");
			addErrore(ErroreBil.NON_TUTTI_I_CAMPI_DI_UN_CAPITOLO_ASSOCIATO_AD_UNA_VARIAZIONE_SONO_STATI_VALORIZZATI.getErrore());
			return SUCCESS;
		}
		
		log.debug(methodName, "Ottengo l'indice del classificatore dalla lista");
		int indice = ComparatorUtils.getIndexByUid(listaElementoCapitoloCodifiche, elementoCapitoloCodifiche);
		
		if(indice == -1) {
			log.error(methodName, "Non ho trovato il capitolo");
			// Non ho trovato l'elemento: lancio un errore
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Elemento non trovato"));
			return SUCCESS;
		}
		
		log.debug(methodName, "Imposto i dati nel model");
		// Imposto i dati nel model
		listaElementoCapitoloCodifiche.set(indice, elementoCapitoloCodifiche);
		model.getSpecificaCodifiche().setElementoCapitoloCodifiche(null);
		model.getSpecificaCodifiche().setElementoCapitoloCodificheOld(null);
		
		return SUCCESS;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* **** Metodi di utilita' **** */
	
	/**
	 * Effettua una ricerca per trovare l'elenco dei classificatori modificabili.
	 * 
	 * @param elementoCapitoloCodifiche l'elemento rispetto al quale effettuare la ricerca
	 */
	private void ricercaClassificatoriModificabili(ElementoCapitoloCodifiche elementoCapitoloCodifiche) {
		final String methodName = "ricercaClassificatoriModificabili";
		log.debugStart(methodName, "");
		log.debug(methodName, "Controllo quali classificatori siano modificabili");
		ControllaClassificatoriModificabiliCapitolo request = model.creaRequestControllaClassificatoriModificabiliCapitolo(elementoCapitoloCodifiche);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di controllo dei classificatori");
		ControllaClassificatoriModificabiliCapitoloResponse response = capitoloService.controllaClassificatoriModificabiliCapitolo(request);
		logServiceResponse(response);
		
		log.debug(methodName, "Imposto i dati nel model");
		model.getSpecificaCodifiche().impostaClassificatoriModificabili(response);
		log.debugEnd(methodName, "");
	}
	
	/**
	 * Effettua una ricerca per trovare l'elenco degli attributi modificabili.
	 * 
	 * @param elementoCapitoloCodifiche l'elemento rispetto al quale effettuare la ricerca
	 */
	private void ricercaAttributiModificabili(ElementoCapitoloCodifiche elementoCapitoloCodifiche) {
		final String methodName = "ricercaAttributiModificabili";
		log.debugStart(methodName, "");
		log.debug(methodName, "Controllo quali attributi siano modificabili");
		ControllaAttributiModificabiliCapitolo request = model.creaRequestControllaAttributiModificabiliCapitolo(elementoCapitoloCodifiche);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di controllo degli attirbuti");
		ControllaAttributiModificabiliCapitoloResponse response = capitoloService.controllaAttributiModificabiliCapitolo(request);
		logServiceResponse(response);
		
		log.debug(methodName, "Imposto i dati nel model");
		model.getSpecificaCodifiche().impostaAttributiModificabili(response);
		model.getSpecificaCodifiche().impostaDescrizioniModificabili(response);
		log.debugEnd(methodName, "");
	}
	
	/**
	 * Effettua il caricamento dei classificatori.
	 * 
	 * @param elementoCapitoloCodifiche l'elemento rispetto al quale effettuare la ricerca
	 */
	private void leggiClassificatori(ElementoCapitoloCodifiche elementoCapitoloCodifiche) {
		final String methodName = "leggiClassificatori";
		
		log.debugStart(methodName, "");
		log.debug(methodName, "Cerco i classificatori");
		TipoCapitolo tipoCapitolo = elementoCapitoloCodifiche.getTipoCapitolo();
		String tipoElementoBilancioCodice = TipoCapitolo.CAPITOLO_USCITA_PREVISIONE.equals(tipoCapitolo) ? BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE.getConstant() : 
			(TipoCapitolo.CAPITOLO_USCITA_GESTIONE.equals(tipoCapitolo) ? BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant() : 
				(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE.equals(tipoCapitolo) ? BilConstants.CODICE_CAPITOLO_ENTRATA_PREVISIONE.getConstant() : BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant()));
		log.debug(methodName, "Codice del tipo di capitolo: " + tipoElementoBilancioCodice);
		
		clearMappaClassificatori();
		
		LeggiClassificatoriGenericiByTipoElementoBil requestLeggiClassificatoriGenericiByTipoElementoBil = model.creaRequestLeggiClassificatoriGenericiByTipoElementoBil(tipoElementoBilancioCodice);
		LeggiClassificatoriGenericiByTipoElementoBilResponse responseLeggiClassificatoriGenericiByTipoElementoBil = classificatoreBilService.leggiClassificatoriGenericiByTipoElementoBil(requestLeggiClassificatoriGenericiByTipoElementoBil);
		
		addListeClassificatoriGenerici(responseLeggiClassificatoriGenericiByTipoElementoBil);
		
		// Faccio questa successivamente in quanto l'altra impostazione coinvolge tutti i classificatori
		LeggiClassificatoriByTipoElementoBil requestLeggiClassificatoriByTipoElementoBil = model.creaRequestLeggiClassificatoriByTipoElementoBil(tipoElementoBilancioCodice);
		LeggiClassificatoriByTipoElementoBilResponse responseLeggiClassificatoriByTipoElementoBil = classificatoreBilService.leggiClassificatoriByTipoElementoBil(requestLeggiClassificatoriByTipoElementoBil);
		
		addListeClassificatoriGerarchici(responseLeggiClassificatoriByTipoElementoBil);
		
		impostaListaCategoriaCapitolo(tipoCapitolo);
		
		// Caricamento classificatori per id
		ottieniClassificatoreByPadreIfApplicabile(TipologiaClassificatore.PROGRAMMA, elementoCapitoloCodifiche.getMissione(), Programma.class);
		//TODO SIAC-3507 qui non aggiunge nulla alla mappa Classificatori!!!! quel programma non ha figli di tipo COFOG!
		ottieniClassificatoreByRelazioneIfApplicabile(TipologiaClassificatore.CLASSIFICAZIONE_COFOG, elementoCapitoloCodifiche.getProgramma(), ClassificazioneCofog.class);
		ottieniClassificatoreByPadreIfApplicabile(TipologiaClassificatore.MACROAGGREGATO, elementoCapitoloCodifiche.getTitoloSpesa(), Macroaggregato.class);
		ottieniClassificatoreByPadreIfApplicabile(TipologiaClassificatore.TIPOLOGIA, elementoCapitoloCodifiche.getTitoloEntrata(), TipologiaTitolo.class);
		ottieniClassificatoreByPadreIfApplicabile(TipologiaClassificatore.CATEGORIA, elementoCapitoloCodifiche.getTipologiaTitolo(), CategoriaTipologiaTitolo.class);
		
		// Carico la codifica per ottenere l'elemento del piano dei conti
		Codifica codificaPadre = (TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE.equals(tipoCapitolo) || TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE.equals(tipoCapitolo)) ? 
				elementoCapitoloCodifiche.getCategoriaTipologiaTitolo() : elementoCapitoloCodifiche.getMacroaggregato();
		ottieniElementoPianoDeiContiIfApplicabile(codificaPadre);
		
		
		//Il SIOPE è piatto e non dipende più dal Piano dei Conti (vedi CR SIC-2559)
		if(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE.equals(model.getSpecificaCodifiche().getTipologiaCapitolo()) ||
				TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE.equals(model.getSpecificaCodifiche().getTipologiaCapitolo())) {
			addListaClassificatori(TipologiaClassificatore.SIOPE_ENTRATA, Arrays.asList(elementoCapitoloCodifiche.getSiopeEntrata()));
			
		} else {
			addListaClassificatori(TipologiaClassificatore.SIOPE_SPESA, Arrays.asList(elementoCapitoloCodifiche.getSiopeSpesa()));
		}
		
		
		ottieniStrutturaAmministrativoContabile();
		log.debugEnd(methodName, "");
	}

	/**
	 * Pulisce la mappa dei classificatori.
	 */
	private void clearMappaClassificatori() {
		model.getSpecificaCodifiche().getMappaClassificatori().clear();
	}

	/**
	 * Imposta la lista della categoria capitolo nel model.
	 * @param tipoCapitolo il tipo di capitolo
	 */
	private void impostaListaCategoriaCapitolo(TipoCapitolo tipoCapitolo) {
		RicercaCategoriaCapitolo request = model.creaRequestRicercaCategoriaCapitolo(tipoCapitolo);
		RicercaCategoriaCapitoloResponse response = capitoloService.ricercaCategoriaCapitolo(request);
		model.getSpecificaCodifiche().setListaCategoriaCapitolo(response.getElencoCategoriaCapitolo());
	}

	/**
	 * Aggiunge le liste dei classificatori gerarchici.
	 * 
	 * @param response la response tramite cui popolare le liste
	 */
	private void addListeClassificatoriGerarchici(LeggiClassificatoriByTipoElementoBilResponse response) {
		addListaClassificatori(TipologiaClassificatore.MISSIONE, response.getClassificatoriMissione());
		addListaClassificatori(TipologiaClassificatore.TITOLO_SPESA, response.getClassificatoriTitoloSpesa());
		addListaClassificatori(TipologiaClassificatore.TITOLO_ENTRATA, response.getClassificatoriTitoloEntrata());
	}

	/**
	 * Aggiunge le liste dei classificatori.
	 * 
	 * @param response la response del servizio di ricerca classificatori
	 */
	private void addListeClassificatoriGenerici(LeggiClassificatoriGenericiByTipoElementoBilResponse response) {
		final TipologiaClassificatore[] tipologieClassificatori = TipologiaClassificatore.values();
		for(TipologiaClassificatore tc : tipologieClassificatori) {
			addListaClassificatori(tc, response.getClassificatori(tc));
		}
	}

	/**
	 * Imposta la lista dei classificatori nel model.
	 * 
	 * @param tipologiaClassificatore la tipologia del classificatore
	 * @param classificatori          la lista di classificatori
	 */
	private <T extends Codifica> void addListaClassificatori(TipologiaClassificatore tipologiaClassificatore, List<T> classificatori) {
		if(classificatori != null && !classificatori.isEmpty()) {
			model.getSpecificaCodifiche().getMappaClassificatori().put(tipologiaClassificatore, classificatori);
		}
	}
	
	/**
	 * Ottiene il classificatore a partire dall'id del classificatore padre, nel caso in cui tale id sia valorizzato correttamente.
	 * 
	 * @param tipologiaClassificatore la tipologia del classificatore
	 * @param codificaPadre           la codifica padre
	 * @param clazzCodificaFiglio     la classe della codifica figlio
	 */
	@SuppressWarnings("unchecked")
	private <T extends Codifica> void ottieniClassificatoreByPadreIfApplicabile(TipologiaClassificatore tipologiaClassificatore, Codifica codificaPadre, Class<T> clazzCodificaFiglio) {
		if(codificaPadre == null || codificaPadre.getUid() == 0) {
			return;
		}
		final String methodName = "ottieniClassificatoreByPadreIfApplicabile";
		log.debugStart(methodName, "");
		LeggiClassificatoriBilByIdPadre request = model.creaRequestLeggiClassificatoriBilByIdPadre(codificaPadre.getUid());
		LeggiClassificatoriBilByIdPadreResponse response = classificatoreBilService.leggiClassificatoriByIdPadre(request);
		String nomeClassificatore = "getClassificatori" + clazzCodificaFiglio.getSimpleName();
		
		try {
			List<T> listaClassificatori = (List<T>) LeggiClassificatoriBilByIdPadreResponse.class.getMethod(nomeClassificatore).invoke(response);
			addListaClassificatori(tipologiaClassificatore, listaClassificatori);
		} catch (Exception e) {
			log.warn(methodName, "Errore nel caricamento della lista di classificatori tramite la ricerca per id del padre: " + e.getMessage(), e);
		}
		log.debugEnd(methodName, "");
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Codifica> void ottieniClassificatoreByRelazioneIfApplicabile(TipologiaClassificatore tipologiaClassificatore, Codifica codificaPadre, Class<T> clazzCodificaFiglio) {
		if(codificaPadre == null || codificaPadre.getUid() == 0) {
			return;
		}
		final String methodName = "ottieniClassificatoreByPadreIfApplicabile";
		log.debugStart(methodName, "");
		LeggiClassificatoriByRelazione request = model.creaRequestLeggiClassificatoriByRelazione(codificaPadre.getUid());
		LeggiClassificatoriByRelazioneResponse response = classificatoreBilService.leggiClassificatoriByRelazione(request);
		String nomeClassificatore = "getClassificatori" + clazzCodificaFiglio.getSimpleName();
		
		try {
			List<T> listaClassificatori = (List<T>) LeggiClassificatoriByRelazioneResponse.class.getMethod(nomeClassificatore).invoke(response);
			addListaClassificatori(tipologiaClassificatore, listaClassificatori);
		} catch (Exception e) {
			log.warn(methodName, "Errore nel caricamento della lista di classificatori tramite la ricerca per relazione: " + e.getMessage(), e);
		}
		log.debugEnd(methodName, "");
	}
	
	/**
	 * Ottiene l'elemento del piano dei conti a partire dalla codifica padre.
	 * 
	 * @param codificaPadre la codifica padre
	 */
	private void ottieniElementoPianoDeiContiIfApplicabile(Codifica codificaPadre) {
		if(codificaPadre == null || codificaPadre.getUid() == 0) {
			return;
		}
		LeggiTreePianoDeiConti request = model.creaRequestLeggiTreePianoDeiConti(codificaPadre.getUid());
		LeggiTreePianoDeiContiResponse response = classificatoreBilService.leggiTreePianoDeiConti(request);
		addListaClassificatori(TipologiaClassificatore.PDC, response.getTreeElementoPianoDeiConti());
	}
	
//	/**
//	 * Ottiene il SIOPE a partire dalla codifica padre.
//	 * 
//	 * @param elementoPianoDeiConti la codifica padre
//	 */
//	private void ottieniSIOPEIfApplicabile(ClassificatoreGerarchico siope) {
//		
//		
//		
//		
//		(ElementoPianoDeiConti elementoPianoDeiConti)
//		if(elementoPianoDeiConti == null || elementoPianoDeiConti.getUid() == 0) {
//			return;
//		}
//		
//		// Divido i casi di entrata e di spesa
//		LeggiTreeSiope request = model.creaRequestLeggiTreeSiope(elementoPianoDeiConti.getUid());
//		LeggiTreeSiopeResponse response = null;
//		if(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE.equals(model.getSpecificaCodifiche().getTipologiaCapitolo()) ||
//				TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE.equals(model.getSpecificaCodifiche().getTipologiaCapitolo())) {
//			response = classificatoreBilService.leggiTreeSiopeEntrata(request);
//			addListaClassificatori(TipologiaClassificatore.SIOPE_ENTRATA, response.getTreeSiopeEntrata());
//		} else {
//			response = classificatoreBilService.leggiTreeSiopeSpesa(request);
//			addListaClassificatori(TipologiaClassificatore.SIOPE_SPESA, response.getTreeSiopeSpesa());
//		}
//	}
	
	/**
	 * Ottiene la struttura amministrativo contabile.
	 */
	private void ottieniStrutturaAmministrativoContabile() {
		LeggiStrutturaAmminstrativoContabile request = model.creaRequestLeggiStrutturaAmminstrativoContabile();
		LeggiStrutturaAmminstrativoContabileResponse response = classificatoreService.leggiStrutturaAmminstrativoContabile(request);
		addListaClassificatori(TipologiaClassificatore.CDC, response.getListaStrutturaAmmContabile());
	}
	
}

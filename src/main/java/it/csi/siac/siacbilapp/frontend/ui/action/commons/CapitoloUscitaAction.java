/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.commons;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.ComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatori;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatoriResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenerico;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenericoResponse;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.RisorsaAccantonata;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Superclasse per la action del Capitolo di Uscita.
 * <br>
 * Si occupa di definire i metodi comuni per le actions del Capitolo di Uscita, con attenzione a:
 * <ul>
 *   <li>aggiornamento</li>
 *   <li>inserimento</li>
 *   <li>ricerca</li>
 * </ul>
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 20/08/2013
 *
 * @param <M> la parametrizzazione del Model
 */
public abstract class CapitoloUscitaAction<M extends CapitoloUscitaModel> extends CapitoloAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5881206277593915434L;
	
	protected static final String CODICE_MISSIONE_20 = "20";
	
	@Autowired protected ComponenteImportiCapitoloService componenteImportiCapitoloService;
	
	@Override
	protected void caricaListaCodifiche(BilConstants codiceTipoElementoBilancio) {
		final String methodName = "caricaListaCodifiche";
		
		log.debugStart(methodName, "Caricamento delle liste delle codifiche associate");

		caricaListaClassificatoriGerarchici(codiceTipoElementoBilancio);
		
		caricaListaClassificatoriGenerici(codiceTipoElementoBilancio);
		
		super.caricaListaCodifiche(codiceTipoElementoBilancio);
		caricaListaCodificheUscita();
		log.debugEnd(methodName, "Liste delle codifiche caricate");
	}
	
	/**
	 * Carica lista codifiche uscita.
	 */
	protected void caricaListaCodificheUscita() {
		List<RisorsaAccantonata> listaRisorsaAccantonata = sessionHandler.getParametro(BilSessionParameter.LISTA_RISORSA_ACCANTONATA);
		if(listaRisorsaAccantonata == null || listaRisorsaAccantonata.isEmpty()) {
			List<TipologiaClassificatore> listaTipi = new ArrayList<TipologiaClassificatore>();
			listaTipi.add(TipologiaClassificatore.RISORSA_ACCANTONATA);
 			LeggiClassificatoriByTipologieClassificatori request = model.creaRequestLeggiClassificatoriByTipologieClassificatori(listaTipi);
			logServiceRequest(request);
			LeggiClassificatoriByTipologieClassificatoriResponse response = classificatoreBilService.leggiClassificatoriByTipologieClassificatori(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
			}
			
			listaRisorsaAccantonata = response.extractByClass(RisorsaAccantonata.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_RISORSA_ACCANTONATA, listaRisorsaAccantonata);
		}
		model.setListaRisorsaAccantonata(listaRisorsaAccantonata);
	}

	/**
	 * Controlla se almeno una codifica &eacute; gi&agrave; stata caricata in sessione
	 * @return <code>true</code> se vi &eacute; almeno una lista in sessione; <code>false</code> altrimenti
	 */
	protected boolean almenoUnaCodificaGiaCaricata() {
		List<Missione> listaMissione = sessionHandler.getParametro(BilSessionParameter.LISTA_MISSIONE);
		return listaMissione != null && !listaMissione.isEmpty();
	}

	/**
	 * Caricamento della lista dei classificatori gerarchici
	 * @param codiceTipoElementoBilancio il codice del capitolo
	 */
	protected void caricaListaClassificatoriGerarchici(BilConstants codiceTipoElementoBilancio) {
		final String methodName = "caricaListaClassificatoriGerarchici";

		/* Classificatori gerarchici - Primo livello */
		log.debug(methodName, "Caricamento delle liste dei classificatori gerarchici: Missione");

		LeggiClassificatoriByTipoElementoBilResponse response = ottieniResponseLeggiClassificatoriByTipoElementoBil(codiceTipoElementoBilancio.getConstant());
		/* Imposto le liste nel model */
		model.setListaMissione(response.getClassificatoriMissione());
		sessionHandler.setParametro(BilSessionParameter.LISTA_MISSIONE, response.getClassificatoriMissione());
		sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_SPESA_ORIGINALE, response.getClassificatoriTitoloSpesa());
		
		/* Liste derivate */
		/* Programma */
		List<Programma> listaProgramma = sessionHandler.getParametro(BilSessionParameter.LISTA_PROGRAMMA);
		if (listaProgramma != null && !listaProgramma.isEmpty()) {
			model.setListaProgramma(listaProgramma);
		}

		/* Classificazione Cofog */
		List<ClassificazioneCofog> listaClassificazioneCofog = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICAZIONE_COFOG);
		if (listaClassificazioneCofog != null && !listaProgramma.isEmpty()) {
			model.setListaClassificazioneCofog(listaClassificazioneCofog);
		}

		/* Titolo */
		List<TitoloSpesa> listaTitoloSpesa = ottieniListaTitoloSpesaDaSessione();
		if (listaTitoloSpesa != null && !listaTitoloSpesa.isEmpty()) {
			model.setListaTitoloSpesa(listaTitoloSpesa);
		}
			
		/* Macroaggregato */
		List<Macroaggregato> listaMacroaggregato = sessionHandler.getParametro(BilSessionParameter.LISTA_MACROAGGREGATO);
		if (listaMacroaggregato != null && !listaMacroaggregato.isEmpty()) {
			model.setListaMacroaggregato(listaMacroaggregato);
		}
	}

	
	
	protected boolean isMissioneWithCodice(String codice) {
		if(model.getMissione() == null || codice == null) {
			return false;
		}
		Missione missioneConDati = ComparatorUtils.searchByUid(model.getListaMissione(), model.getMissione());
		return missioneConDati != null && codice.equals(missioneConDati.getCodice());
	}
	
	/**
	 * Ottiene la lista del titolo di spesa da sessione
	 * @return la lista del titolo spesa
	 */
	protected List<TitoloSpesa> ottieniListaTitoloSpesaDaSessione() {
		return sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
	}

	/**
	 * Carica le liste dei classificatori generici.
	 * @param codiceTipoElementoBilancio il codice del capitolo
	 */
	protected void caricaListaClassificatoriGenerici(BilConstants codiceTipoElementoBilancio) {
		final String methodName = "caricaListaClassificatoriGenerici";
		/* Classificatori generici */
		log.debug(methodName, "Caricamento delle liste dei classificatori generici: Tipo Finanziamento, Tipo Fondo e Generici con relativi Labels");
		
		/* Tipo Finanziamento */
		List<TipoFinanziamento> listaTipoFinanziamento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		/* Tipo Fondo */
		List<TipoFondo> listaTipoFondo = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_FONDO);
		/* Ricorrente Spesa */
		List<RicorrenteSpesa> listaRicorrenteSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_RICORRENTE_SPESA);
		/* Perimetro Sanitario Spesa */
		List<PerimetroSanitarioSpesa> listaPerimetroSanitarioSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_PERIMETRO_SANITARIO_SPESA);
		/* Transazione Unione Europea Spesa */
		List<TransazioneUnioneEuropeaSpesa> listaTransazioneUnioneEuropeaSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TRANSAZIONE_UNIONE_EUROPEA_SPESA);
		/* Politiche Regionali Unitarie */
		List<PoliticheRegionaliUnitarie> listaPoliticheRegionaliUnitarie = sessionHandler.getParametro(BilSessionParameter.LISTA_POLITICHE_REGIONALI_UNITARIE);
		
		/* Classificatori Generici */
		List<ClassificatoreGenerico> listaClassificatoreGenerico1 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_1);
		List<ClassificatoreGenerico> listaClassificatoreGenerico2 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_2);
		List<ClassificatoreGenerico> listaClassificatoreGenerico3 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_3);
		List<ClassificatoreGenerico> listaClassificatoreGenerico4 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_4);
		List<ClassificatoreGenerico> listaClassificatoreGenerico5 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_5);
		List<ClassificatoreGenerico> listaClassificatoreGenerico6 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_6);
		List<ClassificatoreGenerico> listaClassificatoreGenerico7 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_7);
		List<ClassificatoreGenerico> listaClassificatoreGenerico8 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_8);
		List<ClassificatoreGenerico> listaClassificatoreGenerico9 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_9);
		List<ClassificatoreGenerico> listaClassificatoreGenerico10 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_10);
		List<ClassificatoreGenerico> listaClassificatoreGenerico31 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_31);
		List<ClassificatoreGenerico> listaClassificatoreGenerico32 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_32);
		List<ClassificatoreGenerico> listaClassificatoreGenerico33 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_33);
		List<ClassificatoreGenerico> listaClassificatoreGenerico34 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_34);
		List<ClassificatoreGenerico> listaClassificatoreGenerico35 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_35);
		
		/* Boolean per il controllo della necessita' di caricamento delle liste da servizio */
		boolean almenoUnaListaNulla = listaTipoFinanziamento == null
				|| listaTipoFondo == null
				|| listaRicorrenteSpesa == null
				|| listaPerimetroSanitarioSpesa == null
				|| listaTransazioneUnioneEuropeaSpesa == null
				|| listaPoliticheRegionaliUnitarie == null
				|| listaClassificatoreGenerico1 == null
				|| listaClassificatoreGenerico2 == null
				|| listaClassificatoreGenerico3 == null
				|| listaClassificatoreGenerico4 == null
				|| listaClassificatoreGenerico5 == null
				|| listaClassificatoreGenerico6 == null
				|| listaClassificatoreGenerico7 == null
				|| listaClassificatoreGenerico8 == null
				|| listaClassificatoreGenerico9 == null
				|| listaClassificatoreGenerico10 == null
				|| listaClassificatoreGenerico31 == null
				|| listaClassificatoreGenerico32 == null
				|| listaClassificatoreGenerico33 == null
				|| listaClassificatoreGenerico34 == null
				|| listaClassificatoreGenerico35 == null;
		
		if(almenoUnaListaNulla) {
			log.debug(methodName, "Caricamento delle liste di classificatori generici da servizio");
			/* NOTA: la scelta di utilizzo di CODICE_TIPO_FINANZIAMENTO a' arbitraria */
			LeggiClassificatoriGenericiByTipoElementoBilResponse response =
					ottieniResponseLeggiClassificatoriGenericiByTipoElementoBil(codiceTipoElementoBilancio.getConstant());
			
			/* impostazione in locale delle liste */
			listaTipoFinanziamento = response.getClassificatoriTipoFinanziamento();
			listaTipoFondo = response.getClassificatoriTipoFondo();
			listaRicorrenteSpesa = response.getClassificatoriRicorrenteSpesa();
			listaPerimetroSanitarioSpesa = response.getClassificatoriPerimetroSanitarioSpesa();
			listaTransazioneUnioneEuropeaSpesa = response.getClassificatoriTransazioneUnioneEuropeaSpesa();
			listaPoliticheRegionaliUnitarie = response.getClassificatoriPoliticheRegionaliUnitarie();
			listaClassificatoreGenerico1 = response.getClassificatoriGenerici1();
			listaClassificatoreGenerico2 = response.getClassificatoriGenerici2();
			listaClassificatoreGenerico3 = response.getClassificatoriGenerici3();
			listaClassificatoreGenerico4 = response.getClassificatoriGenerici4();
			listaClassificatoreGenerico5 = response.getClassificatoriGenerici5();
			listaClassificatoreGenerico6 = response.getClassificatoriGenerici6();
			listaClassificatoreGenerico7 = response.getClassificatoriGenerici7();
			listaClassificatoreGenerico8 = response.getClassificatoriGenerici8();
			listaClassificatoreGenerico9 = response.getClassificatoriGenerici9();
			listaClassificatoreGenerico10 = response.getClassificatoriGenerici10();
			listaClassificatoreGenerico31 = response.getClassificatoriGenerici31();
			listaClassificatoreGenerico32 = response.getClassificatoriGenerici32();
			listaClassificatoreGenerico33 = response.getClassificatoriGenerici33();
			listaClassificatoreGenerico34 = response.getClassificatoriGenerici34();
			listaClassificatoreGenerico35 = response.getClassificatoriGenerici35();
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO,listaTipoFinanziamento);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_FONDO, listaTipoFondo);
			sessionHandler.setParametro(BilSessionParameter.LISTA_RICORRENTE_SPESA, listaRicorrenteSpesa);
			sessionHandler.setParametro(BilSessionParameter.LISTA_PERIMETRO_SANITARIO_SPESA, listaPerimetroSanitarioSpesa);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TRANSAZIONE_UNIONE_EUROPEA_SPESA, listaTransazioneUnioneEuropeaSpesa);
			sessionHandler.setParametro(BilSessionParameter.LISTA_POLITICHE_REGIONALI_UNITARIE, listaPoliticheRegionaliUnitarie);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_1, listaClassificatoreGenerico1);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_2, listaClassificatoreGenerico2);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_3, listaClassificatoreGenerico3);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_4, listaClassificatoreGenerico4);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_5, listaClassificatoreGenerico5);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_6, listaClassificatoreGenerico6);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_7, listaClassificatoreGenerico7);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_8, listaClassificatoreGenerico8);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_9, listaClassificatoreGenerico9);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_10, listaClassificatoreGenerico10);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_31, listaClassificatoreGenerico31);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_32, listaClassificatoreGenerico32);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_33, listaClassificatoreGenerico33);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_34, listaClassificatoreGenerico34);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_35, listaClassificatoreGenerico35);
		}
		
		
		/* Impostazione nel model delle liste */
		model.setListaTipoFinanziamento(listaTipoFinanziamento);
		model.setListaTipoFondo(listaTipoFondo);
		model.setListaRicorrenteSpesa(listaRicorrenteSpesa);
		model.setListaPerimetroSanitarioSpesa(listaPerimetroSanitarioSpesa);
		model.setListaTransazioneUnioneEuropeaSpesa(listaTransazioneUnioneEuropeaSpesa);
		model.setListaPoliticheRegionaliUnitarie(listaPoliticheRegionaliUnitarie);
		model.setListaClassificatoreGenerico1(listaClassificatoreGenerico1);
		model.setListaClassificatoreGenerico2(listaClassificatoreGenerico2);
		model.setListaClassificatoreGenerico3(listaClassificatoreGenerico3);
		model.setListaClassificatoreGenerico4(listaClassificatoreGenerico4);
		model.setListaClassificatoreGenerico5(listaClassificatoreGenerico5);
		model.setListaClassificatoreGenerico6(listaClassificatoreGenerico6);
		model.setListaClassificatoreGenerico7(listaClassificatoreGenerico7);
		model.setListaClassificatoreGenerico8(listaClassificatoreGenerico8);
		model.setListaClassificatoreGenerico9(listaClassificatoreGenerico9);
		model.setListaClassificatoreGenerico10(listaClassificatoreGenerico10);
		model.setListaClassificatoreGenerico11(listaClassificatoreGenerico31);
		model.setListaClassificatoreGenerico12(listaClassificatoreGenerico32);
		model.setListaClassificatoreGenerico13(listaClassificatoreGenerico33);
		model.setListaClassificatoreGenerico14(listaClassificatoreGenerico34);
		model.setListaClassificatoreGenerico15(listaClassificatoreGenerico35);
		
		/* Impostazione dei labels */
		model.setLabelClassificatoreGenerico1(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico1, 1));
		model.setLabelClassificatoreGenerico2(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico2, 2));
		model.setLabelClassificatoreGenerico3(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico3, 3));
		model.setLabelClassificatoreGenerico4(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico4, 4));
		model.setLabelClassificatoreGenerico5(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico5, 5));
		model.setLabelClassificatoreGenerico6(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico6, 6));
		model.setLabelClassificatoreGenerico7(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico7, 7));
		model.setLabelClassificatoreGenerico8(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico8, 8));
		model.setLabelClassificatoreGenerico9(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico9, 9));
		model.setLabelClassificatoreGenerico10(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico10, 10));
		model.setLabelClassificatoreGenerico11(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico31, 11));
		model.setLabelClassificatoreGenerico12(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico32, 12));
		model.setLabelClassificatoreGenerico13(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico33, 13));
		model.setLabelClassificatoreGenerico14(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico34, 14));
		model.setLabelClassificatoreGenerico15(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico35, 15));
	}
	
	/**	
	 * Inizializzazione delle liste dovute all'aggiornamento.
	 */
	protected void caricaListaCodificheAggiornamento() {
		final String methodName = "caricaListaCodificheAggiornamento";
		
		/* Programma */
		if(model.getMissione() != null && model.getMissione().getUid() != 0) {
			log.debug(methodName, "Carico la lista dei Programmi da servizio");
			LeggiClassificatoriBilByIdPadreResponse response = ottieniResponseLeggiClassificatoriBilByIdPadre(model.getMissione().getUid());
			sessionHandler.setParametro(BilSessionParameter.LISTA_PROGRAMMA, response.getClassificatoriProgramma());
			model.setListaProgramma(response.getClassificatoriProgramma());
		}
		/* Classificazione Cofog e Titolo */
		if(model.getProgramma() != null && model.getProgramma().getUid() != 0) {
			log.debug(methodName, "Carico la lista dei Cofog e titolo da servizio");
			LeggiClassificatoriByRelazione request = model.creaRequestLeggiClassificatoriByRelazione(model.getProgramma().getUid());
			LeggiClassificatoriByRelazioneResponse response = classificatoreBilService.leggiClassificatoriByRelazione(request);
		
			//COFOG
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICAZIONE_COFOG, response.getClassificatoriClassificazioneCofog());
			model.setListaClassificazioneCofog(response.getClassificatoriClassificazioneCofog());
			
			//TITOLO
		    sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_SPESA, response.getClassificatoriTitoloSpesa());
			model.setListaTitoloSpesa(response.getClassificatoriTitoloSpesa());

		}
		/* Macroaggregato */
		if(model.getTitoloSpesa() != null && model.getTitoloSpesa().getUid() != 0) {
			log.debug(methodName, "Carico la lista dei Macroaggregati spesa da servizio");
			LeggiClassificatoriBilByIdPadreResponse response = ottieniResponseLeggiClassificatoriBilByIdPadre(model.getTitoloSpesa().getUid());
			sessionHandler.setParametro(BilSessionParameter.LISTA_MACROAGGREGATO, response.getClassificatoriMacroaggregato());
			model.setListaMacroaggregato(response.getClassificatoriMacroaggregato());
		}
		/* Elemento del piano dei conti */
		if(model.getMacroaggregato() != null && model.getMacroaggregato().getUid() != 0) {
			log.debug(methodName, "Carico la lista degli ElementiPianoDeiConti da servizio");
			LeggiTreePianoDeiContiResponse response = ottieniResponseLeggiTreePianoDeiConti(model.getMacroaggregato().getUid());
			sessionHandler.setParametro(BilSessionParameter.LISTA_ELEMENTO_PIANO_DEI_CONTI, response.getTreeElementoPianoDeiConti());
			model.setListaElementoPianoDeiConti(response.getTreeElementoPianoDeiConti());
		}
		/* Classificazione SIOPE */
		// CR SIAC-2559
//		if(model.getElementoPianoDeiConti() != null && model.getElementoPianoDeiConti().getUid() != 0) {
//			log.debug(methodName, "Carico la lista dei SIOPE di spesa da servizio");
//			LeggiTreeSiopeResponse response = ottieniResponseLeggiTreeSiopeSpesa(model.getElementoPianoDeiConti().getUid());
//			sessionHandler.setParametro(BilSessionParameter.LISTA_SIOPE_SPESA, response.getTreeSiopeSpesa());
//			model.setListaSiopeSpesa(response.getTreeSiopeSpesa());
//		}
	}

	/**
	 * Caricamento dei label dei classificatori generici
	 * @param tipoCapitolo il tipo di capitolo
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	protected void caricaLabelClassificatoriGenerici(String tipoCapitolo) throws WebServiceInvocationFailureException {
		RicercaTipoClassificatoreGenerico req = model.creaRequestRicercaTipoClassificatoreGenerico(tipoCapitolo);
		RicercaTipoClassificatoreGenericoResponse res = classificatoreBilService.ricercaTipoClassificatoreGenerico(req);
		
		if(res.hasErrori()) {
			// Errori nel caricamento dei label
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaTipoClassificatoreGenerico.class, res));
		}
		// Impostazione dei label
		model.setLabelClassificatoreGenerico1(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_1)));
		model.setLabelClassificatoreGenerico2(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_2)));
		model.setLabelClassificatoreGenerico3(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_3)));
		model.setLabelClassificatoreGenerico4(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_4)));
		model.setLabelClassificatoreGenerico5(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_5)));
		model.setLabelClassificatoreGenerico6(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_6)));
		model.setLabelClassificatoreGenerico7(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_7)));
		model.setLabelClassificatoreGenerico8(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_8)));
		model.setLabelClassificatoreGenerico9(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_9)));
		model.setLabelClassificatoreGenerico10(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_10)));
		model.setLabelClassificatoreGenerico11(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_31)));
		model.setLabelClassificatoreGenerico12(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_32)));
		model.setLabelClassificatoreGenerico13(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_33)));
		model.setLabelClassificatoreGenerico14(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_34)));
		model.setLabelClassificatoreGenerico15(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_35)));
	}
	
	
	
}

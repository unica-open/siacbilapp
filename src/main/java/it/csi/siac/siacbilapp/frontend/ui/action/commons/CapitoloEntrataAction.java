/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.commons;

import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenerico;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenericoResponse;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Superclasse per la action del Capitolo di Entrata.
 * <br>
 * Si occupa di definire i metodi comuni per le actions del Capitolo di Escita, con attenzione a:
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
public abstract class CapitoloEntrataAction<M extends CapitoloEntrataModel> extends CapitoloAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3585979555375855707L;

	@Override
	protected void caricaListaCodifiche(BilConstants codiceTipoElementoBilancio) {
		final String methodName = "caricaListaCodifiche";
		
		log.debugStart(methodName, "Caricamento delle liste delle codifiche associate");
		
		caricaListaClassificatoriGerarchici(codiceTipoElementoBilancio);
		 
		caricaListaClassificatoriGenerici(codiceTipoElementoBilancio);
		
		super.caricaListaCodifiche(codiceTipoElementoBilancio);
		log.debugEnd(methodName, "Liste delle codifiche caricate");
	}
	
	/**
	 * Controlla che almeno una codifica sia gi&agrave; stata caricata in sessione
	 * @return <code>true</code> se almeno una codifica &eacute; gi&agrave; stata caricata; <code>false</code> altrimenti
	 */
	protected boolean almenoUnaCodificaGiaCaricata() {
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		return listaTitoloEntrata!=null && !listaTitoloEntrata.isEmpty();
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
		/* Ricorrente Entrata */
		List<RicorrenteEntrata> listaRicorrenteEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_RICORRENTE_ENTRATA);
		/* Perimetro Sanitario Entrata */
		List<PerimetroSanitarioEntrata> listaPerimetroSanitarioEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_PERIMETRO_SANITARIO_ENTRATA);
		/* Transazione Unione Europea Entrata */
		List<TransazioneUnioneEuropeaEntrata> listaTransazioneUnioneEuropeaEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TRANSAZIONE_UNIONE_EUROPEA_ENTRATA);
		/* Classificatori Generici */
		List<ClassificatoreGenerico> listaClassificatoreGenerico1 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_36);
		List<ClassificatoreGenerico> listaClassificatoreGenerico2 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_37);
		List<ClassificatoreGenerico> listaClassificatoreGenerico3 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_38);
		List<ClassificatoreGenerico> listaClassificatoreGenerico4 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_39);
		List<ClassificatoreGenerico> listaClassificatoreGenerico5 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_40);
		List<ClassificatoreGenerico> listaClassificatoreGenerico6 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_41);
		List<ClassificatoreGenerico> listaClassificatoreGenerico7 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_42);
		List<ClassificatoreGenerico> listaClassificatoreGenerico8 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_43);
		List<ClassificatoreGenerico> listaClassificatoreGenerico9 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_44);
		List<ClassificatoreGenerico> listaClassificatoreGenerico10 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_45);
		List<ClassificatoreGenerico> listaClassificatoreGenerico11 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_46);
		List<ClassificatoreGenerico> listaClassificatoreGenerico12 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_47);
		List<ClassificatoreGenerico> listaClassificatoreGenerico13 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_48);
		List<ClassificatoreGenerico> listaClassificatoreGenerico14 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_49);
		List<ClassificatoreGenerico> listaClassificatoreGenerico15 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_50);
		
		/* Boolean per il controllo della necessita' di caricamento delle liste da servizio */
		boolean almenoUnaListaNulla = listaTipoFinanziamento == null
				|| listaTipoFondo == null
				|| listaRicorrenteEntrata == null
				|| listaPerimetroSanitarioEntrata == null
				|| listaTransazioneUnioneEuropeaEntrata == null
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
				|| listaClassificatoreGenerico11 == null
				|| listaClassificatoreGenerico12 == null
				|| listaClassificatoreGenerico13 == null
				|| listaClassificatoreGenerico14 == null
				|| listaClassificatoreGenerico15 == null;
		
		if(almenoUnaListaNulla) {
			log.debug(methodName, "Caricamento delle liste di classificatori generici da servizio");
			/* NOTA: la scelta di utilizzo di CODICE_TIPO_FINANZIAMENTO a' arbitraria */
			LeggiClassificatoriGenericiByTipoElementoBilResponse response =
					ottieniResponseLeggiClassificatoriGenericiByTipoElementoBil(codiceTipoElementoBilancio.getConstant());
			
			/* Impostazione in sessione delle liste */
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO, response.getClassificatoriTipoFinanziamento());
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_FONDO, response.getClassificatoriTipoFondo());
			sessionHandler.setParametro(BilSessionParameter.LISTA_RICORRENTE_ENTRATA, response.getClassificatoriRicorrenteEntrata());
			sessionHandler.setParametro(BilSessionParameter.LISTA_PERIMETRO_SANITARIO_ENTRATA, response.getClassificatoriPerimetroSanitarioEntrata());
			sessionHandler.setParametro(BilSessionParameter.LISTA_TRANSAZIONE_UNIONE_EUROPEA_ENTRATA, response.getClassificatoriTransazioneUnioneEuropeaEntrata());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_36, response.getClassificatoriGenerici36());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_37, response.getClassificatoriGenerici37());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_38, response.getClassificatoriGenerici38());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_39, response.getClassificatoriGenerici39());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_40, response.getClassificatoriGenerici40());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_41, response.getClassificatoriGenerici41());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_42, response.getClassificatoriGenerici42());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_43, response.getClassificatoriGenerici43());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_44, response.getClassificatoriGenerici44());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_45, response.getClassificatoriGenerici45());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_46, response.getClassificatoriGenerici46());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_47, response.getClassificatoriGenerici47());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_48, response.getClassificatoriGenerici48());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_49, response.getClassificatoriGenerici49());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_50, response.getClassificatoriGenerici50());
			
			/* impostazione in locale delle liste */
			listaTipoFinanziamento = response.getClassificatoriTipoFinanziamento();
			listaTipoFondo = response.getClassificatoriTipoFondo();
			listaRicorrenteEntrata = response.getClassificatoriRicorrenteEntrata();
			listaPerimetroSanitarioEntrata = response.getClassificatoriPerimetroSanitarioEntrata();
			listaTransazioneUnioneEuropeaEntrata = response.getClassificatoriTransazioneUnioneEuropeaEntrata();
			listaClassificatoreGenerico1 = response.getClassificatoriGenerici36();
			listaClassificatoreGenerico2 = response.getClassificatoriGenerici37();
			listaClassificatoreGenerico3 = response.getClassificatoriGenerici38();
			listaClassificatoreGenerico4 = response.getClassificatoriGenerici39();
			listaClassificatoreGenerico5 = response.getClassificatoriGenerici40();
			listaClassificatoreGenerico6 = response.getClassificatoriGenerici41();
			listaClassificatoreGenerico7 = response.getClassificatoriGenerici42();
			listaClassificatoreGenerico8 = response.getClassificatoriGenerici43();
			listaClassificatoreGenerico9 = response.getClassificatoriGenerici44();
			listaClassificatoreGenerico10 = response.getClassificatoriGenerici45();
			listaClassificatoreGenerico11 = response.getClassificatoriGenerici46();
			listaClassificatoreGenerico12 = response.getClassificatoriGenerici47();
			listaClassificatoreGenerico13 = response.getClassificatoriGenerici48();
			listaClassificatoreGenerico14 = response.getClassificatoriGenerici49();
			listaClassificatoreGenerico15 = response.getClassificatoriGenerici50();
		}
		/* Impostazione nel model delle liste */
		model.setListaTipoFinanziamento(listaTipoFinanziamento);
		model.setListaTipoFondo(listaTipoFondo);
		model.setListaRicorrenteEntrata(listaRicorrenteEntrata);
		model.setListaPerimetroSanitarioEntrata(listaPerimetroSanitarioEntrata);
		model.setListaTransazioneUnioneEuropeaEntrata(listaTransazioneUnioneEuropeaEntrata);
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
		model.setListaClassificatoreGenerico11(listaClassificatoreGenerico11);
		model.setListaClassificatoreGenerico12(listaClassificatoreGenerico12);
		model.setListaClassificatoreGenerico13(listaClassificatoreGenerico13);
		model.setListaClassificatoreGenerico14(listaClassificatoreGenerico14);
		model.setListaClassificatoreGenerico15(listaClassificatoreGenerico15);
		
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
		model.setLabelClassificatoreGenerico11(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico11, 11));
		model.setLabelClassificatoreGenerico12(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico12, 12));
		model.setLabelClassificatoreGenerico13(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico13, 13));
		model.setLabelClassificatoreGenerico14(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico14, 14));
		model.setLabelClassificatoreGenerico15(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico15, 15));
	}

	/**
	 * Carica la lista dei classificatori gerarchici.
	 * @param codiceTipoElementoBilancio il tipo di capitolo
	 */
	protected void caricaListaClassificatoriGerarchici(BilConstants codiceTipoElementoBilancio) {
		final String methodName = "caricaListaClassificatoriGerarchici";
		/* Classificatori gerarchici - Primo livello */
		log.debug(methodName, "Caricamento delle liste dei classificatori gerarchici: Titolo Entrata");
		
		/* Titolo Entrata */
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		
		/* Boolean per il controllo della necessita' di caricamento delle liste da servizio */
		boolean almenoUnaListaNulla = listaTitoloEntrata == null;
		
		if(almenoUnaListaNulla) {
			log.debug(methodName, "Caricamento di titolo di spesa da servizio");
			
			LeggiClassificatoriByTipoElementoBilResponse response = ottieniResponseLeggiClassificatoriByTipoElementoBil(codiceTipoElementoBilancio.getConstant());
			
			/* Imposto in sessione le liste */
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA, response.getClassificatoriTitoloEntrata());
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPOLOGIA_TITOLO, null);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CATEGORIA_TIPOLOGIA_TITOLO, null);
			sessionHandler.setParametro(BilSessionParameter.LISTA_ELEMENTO_PIANO_DEI_CONTI, null);
			
			/* Imposto le liste nella variabile locale */
			listaTitoloEntrata = response.getClassificatoriTitoloEntrata();
		}
		
		/* Imposto le liste nel model */
		model.setListaTitoloEntrata(listaTitoloEntrata);
		
		/* Tipologia Titolo */
		List<TipologiaTitolo> listaTipologiaTitolo = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPOLOGIA_TITOLO);
		if (listaTipologiaTitolo != null && !listaTipologiaTitolo.isEmpty()) {
			model.setListaTipologiaTitolo(listaTipologiaTitolo);
		}

		/* CategoriaTipologiaTitolo */
		List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo = sessionHandler.getParametro(BilSessionParameter.LISTA_CATEGORIA_TIPOLOGIA_TITOLO);
		if (listaCategoriaTipologiaTitolo != null && !listaCategoriaTipologiaTitolo.isEmpty()) {
			model.setListaCategoriaTipologiaTitolo(listaCategoriaTipologiaTitolo);
		}
	}
	
	/**	
	 * Inizializzazione delle liste dovute all'aggiornamento.
	 */
	protected void caricaListaCodificheAggiornamento() {
		final String methodName = "caricaListaCodificheAggiornamento";
		
		/* Tipologia Titolo */
		if(model.getTitoloEntrata() != null && model.getTitoloEntrata().getUid() != 0) {
			log.debug(methodName, "Carico la lista dei Tipologie di Titolo da servizio");
			LeggiClassificatoriBilByIdPadreResponse response = ottieniResponseLeggiClassificatoriBilByIdPadre(model.getTitoloEntrata().getUid());
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPOLOGIA_TITOLO, response.getClassificatoriTipologiaTitolo());
			model.setListaTipologiaTitolo(response.getClassificatoriTipologiaTitolo());
		}
		/* Categoria Tipologia Titolo */
		if(model.getTipologiaTitolo() != null && model.getTipologiaTitolo().getUid() != 0) {
			log.debug(methodName, "Carico la lista delle Categorie di Tipologia da servizio");
			LeggiClassificatoriBilByIdPadreResponse response = ottieniResponseLeggiClassificatoriBilByIdPadre(model.getTipologiaTitolo().getUid());
			sessionHandler.setParametro(BilSessionParameter.LISTA_CATEGORIA_TIPOLOGIA_TITOLO, response.getClassificatoriCategoriaTipologiaTitolo());
			model.setListaCategoriaTipologiaTitolo(response.getClassificatoriCategoriaTipologiaTitolo());
		}
		/* Elemento del piano dei conti */
		if(model.getCategoriaTipologiaTitolo() != null && model.getCategoriaTipologiaTitolo().getUid() != 0) {
			log.debug(methodName, "Carico la lista degli ElementiPianoDeiConti da servizio");
			LeggiTreePianoDeiContiResponse response = ottieniResponseLeggiTreePianoDeiConti(model.getCategoriaTipologiaTitolo().getUid());
			sessionHandler.setParametro(BilSessionParameter.LISTA_ELEMENTO_PIANO_DEI_CONTI, response.getTreeElementoPianoDeiConti());
			model.setListaElementoPianoDeiConti(response.getTreeElementoPianoDeiConti());
		}
		/* Classificazione SIOPE */
		// CR SIAC-2559
//		if(model.getElementoPianoDeiConti() != null && model.getElementoPianoDeiConti().getUid() != 0) {
//			log.debug(methodName, "Carico la lista dei SIOPE di spesa da servizio");
//			LeggiTreeSiopeResponse response = ottieniResponseLeggiTreeSiopeEntrata(model.getElementoPianoDeiConti().getUid());
//			sessionHandler.setParametro(BilSessionParameter.LISTA_SIOPE_ENTRATA, response.getTreeSiopeEntrata());
//			model.setListaSiopeEntrata(response.getTreeSiopeEntrata());
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
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			// Errori nel caricamento dei label
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		// Impostazione dei label
		model.setLabelClassificatoreGenerico1(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_36)));
		model.setLabelClassificatoreGenerico2(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_37)));
		model.setLabelClassificatoreGenerico3(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_38)));
		model.setLabelClassificatoreGenerico4(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_39)));
		model.setLabelClassificatoreGenerico5(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_40)));
		model.setLabelClassificatoreGenerico6(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_41)));
		model.setLabelClassificatoreGenerico7(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_42)));
		model.setLabelClassificatoreGenerico8(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_43)));
		model.setLabelClassificatoreGenerico9(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_44)));
		model.setLabelClassificatoreGenerico10(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_45)));
		model.setLabelClassificatoreGenerico11(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_46)));
		model.setLabelClassificatoreGenerico12(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_47)));
		model.setLabelClassificatoreGenerico13(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_48)));
		model.setLabelClassificatoreGenerico14(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_49)));
		model.setLabelClassificatoreGenerico15(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_50)));
	}
}

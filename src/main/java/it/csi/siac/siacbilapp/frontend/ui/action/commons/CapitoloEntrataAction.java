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
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Filter;
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
		List<ClassificatoreGenerico> listaClassificatoreGenerico36 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_36);
		List<ClassificatoreGenerico> listaClassificatoreGenerico37 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_37);
		List<ClassificatoreGenerico> listaClassificatoreGenerico38 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_38);
		List<ClassificatoreGenerico> listaClassificatoreGenerico39 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_39);
		List<ClassificatoreGenerico> listaClassificatoreGenerico40 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_40);
		List<ClassificatoreGenerico> listaClassificatoreGenerico41 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_41);
		List<ClassificatoreGenerico> listaClassificatoreGenerico42 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_42);
		List<ClassificatoreGenerico> listaClassificatoreGenerico43 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_43);
		List<ClassificatoreGenerico> listaClassificatoreGenerico44 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_44);
		List<ClassificatoreGenerico> listaClassificatoreGenerico45 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_45);
		List<ClassificatoreGenerico> listaClassificatoreGenerico46 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_46);
		List<ClassificatoreGenerico> listaClassificatoreGenerico47 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_47);
		List<ClassificatoreGenerico> listaClassificatoreGenerico48 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_48);
		List<ClassificatoreGenerico> listaClassificatoreGenerico49 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_49);
		List<ClassificatoreGenerico> listaClassificatoreGenerico50 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_50);
		
		/* Boolean per il controllo della necessita' di caricamento delle liste da servizio */
		boolean almenoUnaListaNulla = listaTipoFinanziamento == null
				|| listaTipoFondo == null
				|| listaRicorrenteEntrata == null
				|| listaPerimetroSanitarioEntrata == null
				|| listaTransazioneUnioneEuropeaEntrata == null
				|| listaClassificatoreGenerico36 == null
				|| listaClassificatoreGenerico37 == null
				|| listaClassificatoreGenerico38 == null
				|| listaClassificatoreGenerico39 == null
				|| listaClassificatoreGenerico40 == null
				|| listaClassificatoreGenerico41 == null
				|| listaClassificatoreGenerico42 == null
				|| listaClassificatoreGenerico43 == null
				|| listaClassificatoreGenerico44 == null
				|| listaClassificatoreGenerico45 == null
				|| listaClassificatoreGenerico46 == null
				|| listaClassificatoreGenerico47 == null
				|| listaClassificatoreGenerico48 == null
				|| listaClassificatoreGenerico49 == null
				|| listaClassificatoreGenerico50 == null;
		
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
			listaClassificatoreGenerico36 = response.getClassificatoriGenerici36();
			listaClassificatoreGenerico37 = response.getClassificatoriGenerici37();
			listaClassificatoreGenerico38 = response.getClassificatoriGenerici38();
			listaClassificatoreGenerico39 = response.getClassificatoriGenerici39();
			listaClassificatoreGenerico40 = response.getClassificatoriGenerici40();
			listaClassificatoreGenerico41 = response.getClassificatoriGenerici41();
			listaClassificatoreGenerico42 = response.getClassificatoriGenerici42();
			listaClassificatoreGenerico43 = response.getClassificatoriGenerici43();
			listaClassificatoreGenerico44 = response.getClassificatoriGenerici44();
			listaClassificatoreGenerico45 = response.getClassificatoriGenerici45();
			listaClassificatoreGenerico46 = response.getClassificatoriGenerici46();
			listaClassificatoreGenerico47 = response.getClassificatoriGenerici47();
			listaClassificatoreGenerico48 = response.getClassificatoriGenerici48();
			listaClassificatoreGenerico49 = response.getClassificatoriGenerici49();
			listaClassificatoreGenerico50 = response.getClassificatoriGenerici50();
		}
		/* Impostazione nel model delle liste */
		model.setListaTipoFinanziamento(listaTipoFinanziamento);
		model.setListaTipoFondo(listaTipoFondo);
		model.setListaRicorrenteEntrata(listaRicorrenteEntrata);
		model.setListaPerimetroSanitarioEntrata(listaPerimetroSanitarioEntrata);
		model.setListaTransazioneUnioneEuropeaEntrata(listaTransazioneUnioneEuropeaEntrata);
		model.setListaClassificatoreGenerico36(listaClassificatoreGenerico36);
		model.setListaClassificatoreGenerico37(listaClassificatoreGenerico37);
		model.setListaClassificatoreGenerico38(listaClassificatoreGenerico38);
		model.setListaClassificatoreGenerico39(listaClassificatoreGenerico39);
		model.setListaClassificatoreGenerico40(listaClassificatoreGenerico40);
		model.setListaClassificatoreGenerico41(listaClassificatoreGenerico41);
		model.setListaClassificatoreGenerico42(listaClassificatoreGenerico42);
		model.setListaClassificatoreGenerico43(listaClassificatoreGenerico43);
		model.setListaClassificatoreGenerico44(listaClassificatoreGenerico44);
		model.setListaClassificatoreGenerico45(listaClassificatoreGenerico45);
		model.setListaClassificatoreGenerico46(listaClassificatoreGenerico46);
		model.setListaClassificatoreGenerico47(listaClassificatoreGenerico47);
		model.setListaClassificatoreGenerico48(listaClassificatoreGenerico48);
		model.setListaClassificatoreGenerico49(listaClassificatoreGenerico49); 
		model.setListaClassificatoreGenerico50(listaClassificatoreGenerico50);
		
		/* Impostazione dei labels */
		model.setLabelClassificatoreGenerico36(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico36, 1));
		model.setLabelClassificatoreGenerico37(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico37, 2));
		model.setLabelClassificatoreGenerico38(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico38, 3));
		model.setLabelClassificatoreGenerico39(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico39, 4));
		model.setLabelClassificatoreGenerico40(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico40, 5));
		model.setLabelClassificatoreGenerico41(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico41, 6));
		model.setLabelClassificatoreGenerico42(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico42, 7));
		model.setLabelClassificatoreGenerico43(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico43, 8));
		model.setLabelClassificatoreGenerico44(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico44, 9));
		model.setLabelClassificatoreGenerico45(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico45, 10));
		model.setLabelClassificatoreGenerico46(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico46, 11));
		model.setLabelClassificatoreGenerico47(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico47, 12));
		model.setLabelClassificatoreGenerico48(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico48, 13));
		model.setLabelClassificatoreGenerico49(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico49, 14));
		model.setLabelClassificatoreGenerico50(estraiLabelDaListaClassificatoreGenerico(listaClassificatoreGenerico50, 15));
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
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaTipoClassificatoreGenerico.class, res));
		}
		// Impostazione dei label
		model.setLabelClassificatoreGenerico36(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_36)));
		model.setLabelClassificatoreGenerico37(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_37)));
		model.setLabelClassificatoreGenerico38(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_38)));
		model.setLabelClassificatoreGenerico39(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_39)));
		model.setLabelClassificatoreGenerico40(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_40)));
		model.setLabelClassificatoreGenerico41(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_41)));
		model.setLabelClassificatoreGenerico42(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_42)));
		model.setLabelClassificatoreGenerico43(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_43)));
		model.setLabelClassificatoreGenerico44(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_44)));
		model.setLabelClassificatoreGenerico45(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_45)));
		model.setLabelClassificatoreGenerico46(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_46)));
		model.setLabelClassificatoreGenerico47(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_47)));
		model.setLabelClassificatoreGenerico48(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_48)));
		model.setLabelClassificatoreGenerico49(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_49)));
		model.setLabelClassificatoreGenerico50(estraiLabelByTipo(res.getTipoClassificatoreByTipologia(TipologiaClassificatore.CLASSIFICATORE_50)));
	}

	//task-244
	public CategoriaCapitolo caricaCategoriaCapitolo (int uid) {
		return CollectionUtil.findFirst(model.getListaCategoriaCapitolo(), new Filter<CategoriaCapitolo>() {
			@Override
			public boolean isAcceptable(CategoriaCapitolo source) {
				return source.getUid() == uid;
			}});
	}
	
	
}

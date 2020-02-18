/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenerico;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;


/**
 * Superclasse per il model del Capitolo di Entrata.
 * <br>
 * Si occupa di definire i campi e i metodi comuni per le actions del Capitolo di Entrata, con attenzione a:
 * <ul>
 *   <li>aggiornamento</li>
 *   <li>inserimento</li>
 *   <li>ricerca</li>
 * </ul>
 * 
 * @author Alessandro Marchino
 * @version 1.0.1 03/12/2013
 *
 */
public abstract class CapitoloEntrataModel extends CapitoloModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1393414257243302252L;
	
	/** Il numero dei classificatori generici */
	private static final int NUMERO_CLASSIFICATORI_GENERICI = 15;
	
	/* Prima maschera: dati di base */
	private TitoloEntrata            titoloEntrata;
	private TipologiaTitolo          tipologiaTitolo;
	private CategoriaTipologiaTitolo categoriaTipologiaTitolo;
	
	private Integer codiceTitolo;
	
	/* Liste */
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	
	/* Liste derivate */
	private List<TipologiaTitolo>          listaTipologiaTitolo          = new ArrayList<TipologiaTitolo>();
	private List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo = new ArrayList<CategoriaTipologiaTitolo>();
	
	/* Booleani rappresentanti i campi, per la editabilita' */
	private boolean titoloEntrataEditabile;
	private boolean tipologiaTitoloEditabile;
	private boolean categoriaTipologiaTitoloEditabile;
	
	// Classificatori fase 4
	private SiopeEntrata siopeEntrata;
	private RicorrenteEntrata ricorrenteEntrata;
	private PerimetroSanitarioEntrata perimetroSanitarioEntrata;
	private TransazioneUnioneEuropeaEntrata transazioneUnioneEuropeaEntrata;
	
	private String siopeInserito;
	
	private List<SiopeEntrata> listaSiopeEntrata = new ArrayList<SiopeEntrata>();
	private List<RicorrenteEntrata> listaRicorrenteEntrata = new ArrayList<RicorrenteEntrata>();
	private List<PerimetroSanitarioEntrata> listaPerimetroSanitarioEntrata = new ArrayList<PerimetroSanitarioEntrata>();
	private List<TransazioneUnioneEuropeaEntrata> listaTransazioneUnioneEuropeaEntrata = new ArrayList<TransazioneUnioneEuropeaEntrata>();
	
	private boolean siopeEntrataEditabile;
	private boolean ricorrenteEntrataEditabile;
	private boolean perimetroSanitarioEntrataEditabile;
	private boolean transazioneUnioneEuropeaEntrataEditabile;
	
	private boolean flagEntrateRicorrenti;
	
	/**
	 * @return the titoloEntrata
	 */
	public TitoloEntrata getTitoloEntrata() {
		return titoloEntrata;
	}
	/**
	 * @param titoloEntrata the titoloEntrata to set
	 */
	public void setTitoloEntrata(TitoloEntrata titoloEntrata) {
		this.titoloEntrata = titoloEntrata;
	}
	/**
	 * @return the tipologiaTitolo
	 */
	public TipologiaTitolo getTipologiaTitolo() {
		return tipologiaTitolo;
	}
	/**
	 * @param tipologiaTitolo the tipologiaTitolo to set
	 */
	public void setTipologiaTitolo(TipologiaTitolo tipologiaTitolo) {
		this.tipologiaTitolo = tipologiaTitolo;
	}
	/**
	 * @return the categoriaTipologiaTitolo
	 */
	public CategoriaTipologiaTitolo getCategoriaTipologiaTitolo() {
		return categoriaTipologiaTitolo;
	}
	/**
	 * @param categoriaTipologiaTitolo the categoriaTipologiaTitolo to set
	 */
	public void setCategoriaTipologiaTitolo(CategoriaTipologiaTitolo categoriaTipologiaTitolo) {
		this.categoriaTipologiaTitolo = categoriaTipologiaTitolo;
	}
	/**
	 * @return the codiceTitolo
	 */
	public Integer getCodiceTitolo() {
		return codiceTitolo;
	}
	/**
	 * @param codiceTitolo the codiceTitolo to set
	 */
	public void setCodiceTitolo(Integer codiceTitolo) {
		this.codiceTitolo = codiceTitolo;
	}
	/**
	 * @return the listaTitoloEntrata
	 */
	public List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}
	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata;
	}
	/**
	 * @return the listaTipologiaTitolo
	 */
	public List<TipologiaTitolo> getListaTipologiaTitolo() {
		return listaTipologiaTitolo;
	}
	/**
	 * @param listaTipologiaTitolo the listaTipologiaTitolo to set
	 */
	public void setListaTipologiaTitolo(List<TipologiaTitolo> listaTipologiaTitolo) {
		this.listaTipologiaTitolo = listaTipologiaTitolo;
	}
	/**
	 * @return the listaCategoriaTipologiaTitolo
	 */
	public List<CategoriaTipologiaTitolo> getListaCategoriaTipologiaTitolo() {
		return listaCategoriaTipologiaTitolo;
	}
	/**
	 * @param listaCategoriaTipologiaTitolo the listaCategoriaTipologiaTitolo to set
	 */
	public void setListaCategoriaTipologiaTitolo(List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo) {
		this.listaCategoriaTipologiaTitolo = listaCategoriaTipologiaTitolo;
	}
	/**
	 * @return the titoloEntrataEditabile
	 */
	public boolean isTitoloEntrataEditabile() {
		return titoloEntrataEditabile;
	}
	/**
	 * @param titoloEntrataEditabile the titoloEntrataEditabile to set
	 */
	public void setTitoloEntrataEditabile(boolean titoloEntrataEditabile) {
		this.titoloEntrataEditabile = titoloEntrataEditabile;
	}
	/**
	 * @return the tipologiaTitoloEditabile
	 */
	public boolean isTipologiaTitoloEditabile() {
		return tipologiaTitoloEditabile;
	}
	/**
	 * @param tipologiaTitoloEditabile the tipologiaTitoloEditabile to set
	 */
	public void setTipologiaTitoloEditabile(boolean tipologiaTitoloEditabile) {
		this.tipologiaTitoloEditabile = tipologiaTitoloEditabile;
	}
	/**
	 * @return the categoriaTipologiaTitoloEditabile
	 */
	public boolean isCategoriaTipologiaTitoloEditabile() {
		return categoriaTipologiaTitoloEditabile;
	}
	/**
	 * @param categoriaTipologiaTitoloEditabile the categoriaTipologiaTitoloEditabile to set
	 */
	public void setCategoriaTipologiaTitoloEditabile(boolean categoriaTipologiaTitoloEditabile) {
		this.categoriaTipologiaTitoloEditabile = categoriaTipologiaTitoloEditabile;
	}
	
	/**
	 * @return the siopeEntrata
	 */
	public SiopeEntrata getSiopeEntrata() {
		return siopeEntrata;
	}
	/**
	 * @param siopeEntrata the siopeEntrata to set
	 */
	public void setSiopeEntrata(SiopeEntrata siopeEntrata) {
		this.siopeEntrata = siopeEntrata;
	}
	/**
	 * @return the ricorrenteEntrata
	 */
	public RicorrenteEntrata getRicorrenteEntrata() {
		return ricorrenteEntrata;
	}
	/**
	 * @param ricorrenteEntrata the ricorrenteEntrata to set
	 */
	public void setRicorrenteEntrata(RicorrenteEntrata ricorrenteEntrata) {
		this.ricorrenteEntrata = ricorrenteEntrata;
	}
	/**
	 * @return the perimetroSanitarioEntrata
	 */
	public PerimetroSanitarioEntrata getPerimetroSanitarioEntrata() {
		return perimetroSanitarioEntrata;
	}
	/**
	 * @param perimetroSanitarioEntrata the perimetroSanitarioEntrata to set
	 */
	public void setPerimetroSanitarioEntrata(
			PerimetroSanitarioEntrata perimetroSanitarioEntrata) {
		this.perimetroSanitarioEntrata = perimetroSanitarioEntrata;
	}
	/**
	 * @return the transazioneUnioneEuropeaEntrata
	 */
	public TransazioneUnioneEuropeaEntrata getTransazioneUnioneEuropeaEntrata() {
		return transazioneUnioneEuropeaEntrata;
	}
	/**
	 * @param transazioneUnioneEuropeaEntrata the transazioneUnioneEuropeaEntrata to set
	 */
	public void setTransazioneUnioneEuropeaEntrata(
			TransazioneUnioneEuropeaEntrata transazioneUnioneEuropeaEntrata) {
		this.transazioneUnioneEuropeaEntrata = transazioneUnioneEuropeaEntrata;
	}
	/**
	 * @return the siopeInserito
	 */
	public String getSiopeInserito() {
		return siopeInserito;
	}
	/**
	 * @param siopeInserito the siopeInserito to set
	 */
	public void setSiopeInserito(String siopeInserito) {
		this.siopeInserito = siopeInserito;
	}
	/**
	 * @return the listaSiopeEntrata
	 */
	public List<SiopeEntrata> getListaSiopeEntrata() {
		return listaSiopeEntrata;
	}
	/**
	 * @param listaSiopeEntrata the listaSiopeEntrata to set
	 */
	public void setListaSiopeEntrata(List<SiopeEntrata> listaSiopeEntrata) {
		this.listaSiopeEntrata = listaSiopeEntrata;
	}
	/**
	 * @return the listaRicorrenteEntrata
	 */
	public List<RicorrenteEntrata> getListaRicorrenteEntrata() {
		return listaRicorrenteEntrata;
	}
	/**
	 * @param listaRicorrenteEntrata the listaRicorrenteEntrata to set
	 */
	public void setListaRicorrenteEntrata(
			List<RicorrenteEntrata> listaRicorrenteEntrata) {
		this.listaRicorrenteEntrata = listaRicorrenteEntrata;
	}
	/**
	 * @return the listaPerimetroSanitarioEntrata
	 */
	public List<PerimetroSanitarioEntrata> getListaPerimetroSanitarioEntrata() {
		return listaPerimetroSanitarioEntrata;
	}
	/**
	 * @param listaPerimetroSanitarioEntrata the listaPerimetroSanitarioEntrata to set
	 */
	public void setListaPerimetroSanitarioEntrata(List<PerimetroSanitarioEntrata> listaPerimetroSanitarioEntrata) {
		this.listaPerimetroSanitarioEntrata = listaPerimetroSanitarioEntrata;
	}
	/**
	 * @return the listaTransazioneUnioneEuropeaEntrata
	 */
	public List<TransazioneUnioneEuropeaEntrata> getListaTransazioneUnioneEuropeaEntrata() {
		return listaTransazioneUnioneEuropeaEntrata;
	}
	/**
	 * @param listaTransazioneUnioneEuropeaEntrata the listaTransazioneUnioneEuropeaEntrata to set
	 */
	public void setListaTransazioneUnioneEuropeaEntrata(List<TransazioneUnioneEuropeaEntrata> listaTransazioneUnioneEuropeaEntrata) {
		this.listaTransazioneUnioneEuropeaEntrata = listaTransazioneUnioneEuropeaEntrata;
	}
	/**
	 * @return the siopeEntrataEditabile
	 */
	public boolean isSiopeEntrataEditabile() {
		return siopeEntrataEditabile;
	}
	/**
	 * @param siopeEntrataEditabile the siopeEntrataEditabile to set
	 */
	public void setSiopeEntrataEditabile(boolean siopeEntrataEditabile) {
		this.siopeEntrataEditabile = siopeEntrataEditabile;
	}
	/**
	 * @return the ricorrenteEntrataEditabile
	 */
	public boolean isRicorrenteEntrataEditabile() {
		return ricorrenteEntrataEditabile;
	}
	/**
	 * @param ricorrenteEntrataEditabile the ricorrenteEntrataEditabile to set
	 */
	public void setRicorrenteEntrataEditabile(boolean ricorrenteEntrataEditabile) {
		this.ricorrenteEntrataEditabile = ricorrenteEntrataEditabile;
	}
	/**
	 * @return the perimetroSanitarioEntrataEditabile
	 */
	public boolean isPerimetroSanitarioEntrataEditabile() {
		return perimetroSanitarioEntrataEditabile;
	}
	/**
	 * @param perimetroSanitarioEntrataEditabile the perimetroSanitarioEntrataEditabile to set
	 */
	public void setPerimetroSanitarioEntrataEditabile(boolean perimetroSanitarioEntrataEditabile) {
		this.perimetroSanitarioEntrataEditabile = perimetroSanitarioEntrataEditabile;
	}
	/**
	 * @return the transazioneUnioneEuropeaEntrataEditabile
	 */
	public boolean isTransazioneUnioneEuropeaEntrataEditabile() {
		return transazioneUnioneEuropeaEntrataEditabile;
	}
	/**
	 * @param transazioneUnioneEuropeaEntrataEditabile the transazioneUnioneEuropeaEntrataEditabile to set
	 */
	public void setTransazioneUnioneEuropeaEntrataEditabile(boolean transazioneUnioneEuropeaEntrataEditabile) {
		this.transazioneUnioneEuropeaEntrataEditabile = transazioneUnioneEuropeaEntrataEditabile;
	}
	
	/**
	 * @return the flagEntrateRicorrenti
	 */
	public boolean isFlagEntrateRicorrenti() {
		return flagEntrateRicorrenti;
	}
	/**
	 * @param flagEntrateRicorrenti the flagEntrateRicorrenti to set
	 */
	public void setFlagEntrateRicorrenti(boolean flagEntrateRicorrenti) {
		this.flagEntrateRicorrenti = flagEntrateRicorrenti;
	}
	/**
	 * @return the numeroClassificatoriGenerici
	 */
	public int getNumeroClassificatoriGenerici() {
		return NUMERO_CLASSIFICATORI_GENERICI;
	}
	
	/**
	 * @return the flagAccertatoPerCassaVisibile
	 */
	public boolean isFlagAccertatoPerCassaVisibile() {
		String codiceTitoloEntrataOK = "1";
		Collection<String> codiceTipologiaTitoloOK = Arrays.asList("1010100", "1010200", "1010300");
		
		return titoloEntrata != null
				&& tipologiaTitolo != null
				&& codiceTitoloEntrataOK.equals(titoloEntrata.getCodice())
				&& codiceTipologiaTitoloOK.contains(tipologiaTitolo.getCodice());
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Crea una Request per il servizio di {@link RicercaTipoClassificatoreGenerico}.
	 * @param tipoCapitolo il tipo di capitolo
	 * @return la request creata
	 */
	public RicercaTipoClassificatoreGenerico creaRequestRicercaTipoClassificatoreGenerico(String tipoCapitolo) {
		RicercaTipoClassificatoreGenerico req = creaRequest(RicercaTipoClassificatoreGenerico.class);
		
		req.setAnno(getAnnoEsercizioInt());
		req.setTipologieClassificatore(Arrays.asList(
				TipologiaClassificatore.CLASSIFICATORE_36,
				TipologiaClassificatore.CLASSIFICATORE_37,
				TipologiaClassificatore.CLASSIFICATORE_38,
				TipologiaClassificatore.CLASSIFICATORE_39,
				TipologiaClassificatore.CLASSIFICATORE_40,
				TipologiaClassificatore.CLASSIFICATORE_41,
				TipologiaClassificatore.CLASSIFICATORE_42,
				TipologiaClassificatore.CLASSIFICATORE_43,
				TipologiaClassificatore.CLASSIFICATORE_44,
				TipologiaClassificatore.CLASSIFICATORE_45,
				TipologiaClassificatore.CLASSIFICATORE_46,
				TipologiaClassificatore.CLASSIFICATORE_47,
				TipologiaClassificatore.CLASSIFICATORE_48,
				TipologiaClassificatore.CLASSIFICATORE_49,
				TipologiaClassificatore.CLASSIFICATORE_50));
		req.setTipoElementoBilancio(tipoCapitolo);
		
		return req;
	}
	
	@Override
	public void caricaClassificatoriDaSessione(SessionHandler sessionHandler) {
		// Carico i classificatori comuni
		super.caricaClassificatoriDaSessione(sessionHandler);
		
		titoloEntrata = caricaClassificatoriDaSessione(sessionHandler, titoloEntrata, BilSessionParameter.LISTA_TITOLO_ENTRATA);
		tipologiaTitolo = caricaClassificatoriDaSessione(sessionHandler, tipologiaTitolo, BilSessionParameter.LISTA_TIPOLOGIA_TITOLO);
		categoriaTipologiaTitolo = caricaClassificatoriDaSessione(sessionHandler, categoriaTipologiaTitolo, BilSessionParameter.LISTA_CATEGORIA_TIPOLOGIA_TITOLO);
		
		ricorrenteEntrata = caricaClassificatoriDaSessione(sessionHandler, ricorrenteEntrata, BilSessionParameter.LISTA_RICORRENTE_ENTRATA);
		perimetroSanitarioEntrata = caricaClassificatoriDaSessione(sessionHandler, perimetroSanitarioEntrata, BilSessionParameter.LISTA_PERIMETRO_SANITARIO_ENTRATA);
		transazioneUnioneEuropeaEntrata = caricaClassificatoriDaSessione(sessionHandler, transazioneUnioneEuropeaEntrata, BilSessionParameter.LISTA_TRANSAZIONE_UNIONE_EUROPEA_ENTRATA);
		
		siopeEntrata = caricaClassificatoriDaSessione(sessionHandler, siopeEntrata, BilSessionParameter.LISTA_SIOPE_SPESA);
		if(siopeEntrata != null && siopeEntrata.getUid() != 0 && (siopeInserito == null || siopeInserito.isEmpty())) {
			siopeInserito = getCodiceEDescrizione(siopeEntrata);
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle informazioni relative ai classificatori a partire dalle liste in sessione.
	 * 
	 * @param sessionHandler     l'handler per la sessione
	 * @param classificatore     il classificatore da ricercare
	 * @param nomeClassificatore il nome del classificatore in sessione
	 * 
	 * @return il classificatore valorizzato come da lista in sessione
	 */
	protected SiopeEntrata caricaClassificatoriDaSessione(SessionHandler sessionHandler, SiopeEntrata classificatore, BilSessionParameter nomeClassificatore) {
		List<SiopeEntrata> lista = sessionHandler.getParametro(nomeClassificatore);
		return ComparatorUtils.searchByUidWithChildren(lista, classificatore);
	}
	
	@Override
	protected void caricaClassificatoriGenericiDaSessione(SessionHandler sessionHandler) {
		setClassificatoreGenerico1(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico1(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_36));
		setClassificatoreGenerico2(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico2(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_37));
		setClassificatoreGenerico3(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico3(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_38));
		setClassificatoreGenerico4(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico4(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_39));
		setClassificatoreGenerico5(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico5(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_40));
		setClassificatoreGenerico6(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico6(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_41));
		setClassificatoreGenerico7(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico7(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_42));
		setClassificatoreGenerico8(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico8(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_43));
		setClassificatoreGenerico9(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico9(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_44));
		setClassificatoreGenerico10(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico10(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_45));
		setClassificatoreGenerico11(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico11(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_46));
		setClassificatoreGenerico12(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico12(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_47));
		setClassificatoreGenerico13(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico13(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_48));
		setClassificatoreGenerico14(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico14(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_49));
		setClassificatoreGenerico15(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico15(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_50));
	}
	
	@Override
	public void valutaModificabilitaClassificatori(ControllaClassificatoriModificabiliCapitoloResponse response, boolean isMassivo) {
		super.valutaModificabilitaClassificatori(response, isMassivo);
		
		// Controllo se il classificatore è unico: in tal caso, ogni classificatore sarà modificabile
		long stessoNumCapArt = response.getStessoNumCapArt() != null ? response.getStessoNumCapArt().longValue() : 0L;
		long stessoNumCap = response.getStessoNumCap() != null ? response.getStessoNumCap().longValue() : 0L;
		boolean unico =false;
		if (isGestioneUEB()){
		 unico = stessoNumCapArt <= 1L;
		}else{
			unico = stessoNumCap <= 1L;
		}
		
		
		// Controllo i classificatori definiti per il capitolo di entrata
		titoloEntrataEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TITOLO_ENTRATA);
		tipologiaTitoloEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TIPOLOGIA);
		categoriaTipologiaTitoloEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.CATEGORIA);
		
		siopeEntrataEditabile = isEditabile(unico, isMassivo, response,TipologiaClassificatore.SIOPE_ENTRATA, TipologiaClassificatore.SIOPE_ENTRATA_I,TipologiaClassificatore.SIOPE_ENTRATA_II, TipologiaClassificatore.SIOPE_ENTRATA_III);
		ricorrenteEntrataEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.RICORRENTE_ENTRATA);
		perimetroSanitarioEntrataEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA);
		transazioneUnioneEuropeaEntrataEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA);
	}
	
	@Override
	protected void isClassificatoriGenericiEditabile(boolean unico, boolean isMassivo, ControllaClassificatoriModificabiliCapitoloResponse response) {
		setClassificatoreGenerico1Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_36));
		setClassificatoreGenerico2Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_37));
		setClassificatoreGenerico3Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_38));
		setClassificatoreGenerico4Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_39));
		setClassificatoreGenerico5Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_40));
		setClassificatoreGenerico6Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_41));
		setClassificatoreGenerico7Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_42));
		setClassificatoreGenerico8Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_43));
		setClassificatoreGenerico9Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_44));
		setClassificatoreGenerico10Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_45));
		setClassificatoreGenerico11Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_46));
		setClassificatoreGenerico12Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_47));
		setClassificatoreGenerico13Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_48));
		setClassificatoreGenerico14Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_49));
		setClassificatoreGenerico15Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_50));
	}
	
	/**
	 * Reimposta i dati disabilitati causa non editabilit&agrave; nel model.
	 * 
	 * @param classificatoreAggiornamento i classificatori originali in sessione
	 */
	public void setParametriDisabilitati(ClassificatoreAggiornamentoCapitoloEntrata classificatoreAggiornamento) {
		super.setParametriDisabilitati(classificatoreAggiornamento);
		titoloEntrata = impostaIlDato(titoloEntrataEditabile, titoloEntrata, classificatoreAggiornamento.getTitoloEntrata());
		tipologiaTitolo = impostaIlDato(tipologiaTitoloEditabile, tipologiaTitolo, classificatoreAggiornamento.getTipologiaTitolo());
		categoriaTipologiaTitolo = impostaIlDato(categoriaTipologiaTitoloEditabile, categoriaTipologiaTitolo, classificatoreAggiornamento.getCategoriaTipologiaTitolo());
		
		siopeEntrata = impostaIlDato(siopeEntrataEditabile, siopeEntrata, classificatoreAggiornamento.getSiopeEntrata());
		ricorrenteEntrata = impostaIlDato(ricorrenteEntrataEditabile, ricorrenteEntrata, classificatoreAggiornamento.getRicorrenteEntrata());
		perimetroSanitarioEntrata = impostaIlDato(perimetroSanitarioEntrataEditabile, perimetroSanitarioEntrata, classificatoreAggiornamento.getPerimetroSanitarioEntrata());
		transazioneUnioneEuropeaEntrata = impostaIlDato(transazioneUnioneEuropeaEntrataEditabile, transazioneUnioneEuropeaEntrata, classificatoreAggiornamento.getTransazioneUnioneEuropeaEntrata());
	}
	
	@Override
	protected void valorizzaStringheUtilita() {
		super.valorizzaStringheUtilita();
		if(siopeEntrata != null) {
			siopeInserito = getCodiceEDescrizione(siopeEntrata);
		} else {
			// Valore di default
			siopeInserito = "Nessun SIOPE selezionato";
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per impostare i classificatori generici a partire da una lista.
	 * 
	 * @param listaClassificatoriGenerici la lista di classificatori generici da cui popolare il model
	 */
	protected void impostaClassificatoriGenericiDaLista(List<ClassificatoreGenerico> listaClassificatoriGenerici) {
		setClassificatoreGenerico1(null);
		setClassificatoreGenerico2(null);
		setClassificatoreGenerico3(null);
		setClassificatoreGenerico4(null);
		setClassificatoreGenerico5(null);
		setClassificatoreGenerico6(null);
		setClassificatoreGenerico7(null);
		setClassificatoreGenerico8(null);
		setClassificatoreGenerico9(null);
		setClassificatoreGenerico10(null);
		setClassificatoreGenerico11(null);
		setClassificatoreGenerico12(null);
		setClassificatoreGenerico13(null);
		setClassificatoreGenerico14(null);
		setClassificatoreGenerico15(null);
		
		for(ClassificatoreGenerico classificatoreGenerico : listaClassificatoriGenerici) {
			TipoClassificatore tipoClassificatore = classificatoreGenerico.getTipoClassificatore();
			
			if(tipoClassificatore == null || tipoClassificatore.getCodice() == null) {
				// Il tipo di classificatore per il Classificatore Generico non e' valido
				continue;
			}
			
			if(BilConstants.CODICE_CLASSIFICATORE36.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico1(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE37.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico2(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE38.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico3(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE39.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico4(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE40.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico5(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE41.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico6(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE42.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico7(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE43.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico8(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE44.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico9(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE45.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico10(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE46.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico11(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE47.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico12(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE48.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico13(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE49.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico14(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE50.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico15(classificatoreGenerico);
			}
		}
	}
	
	/**
	 * Imposta i labels per i classificatori generici a partire dalle liste caricate in sessione.
	 * 
	 * @param sessionHandler l'handler della sessione
	 */
	@SuppressWarnings("unchecked")
	protected void impostaLabelDaSessione(SessionHandler sessionHandler) {
		setLabelClassificatoreGenerico1(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_36)));
		setLabelClassificatoreGenerico2(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_37)));
		setLabelClassificatoreGenerico3(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_38)));
		setLabelClassificatoreGenerico4(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_39)));
		setLabelClassificatoreGenerico5(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_40)));
		setLabelClassificatoreGenerico6(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_41)));
		setLabelClassificatoreGenerico7(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_42)));
		setLabelClassificatoreGenerico8(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_43)));
		setLabelClassificatoreGenerico9(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_44)));
		setLabelClassificatoreGenerico10(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_45)));
		setLabelClassificatoreGenerico11(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_46)));
		setLabelClassificatoreGenerico12(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_47)));
		setLabelClassificatoreGenerico13(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_48)));
		setLabelClassificatoreGenerico14(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_49)));
		setLabelClassificatoreGenerico15(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_50)));
	}
	
}

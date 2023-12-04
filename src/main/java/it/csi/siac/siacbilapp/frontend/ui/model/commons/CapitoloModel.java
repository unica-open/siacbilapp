/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.commons;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamento;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaClassificatoriERestituisciSeSingolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiPropostaNumeroCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Superclasse per il model del Capitolo.
 * <br>
 * Si occupa di definire i campi e i metodi comuni per le actions del Capitolo, con attenzione a:
 * <ul>
 *   <li>aggiornamento</li>
 *   <li>inserimento</li>
 *   <li>ricerca</li>
 * </ul>
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 20/08/2013
 *
 */
public abstract class CapitoloModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8402159471354010337L;
	
	/** Il numero dei classificatori generici */
	protected static final int NUMERO_CLASSIFICATORI_GENERICI = 15;
	
	/* Dati principali */
	private ElementoPianoDeiConti            elementoPianoDeiConti;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	/* Altri dati */
	private TipoFinanziamento tipoFinanziamento;
	private TipoFondo         tipoFondo;
	private TipoAtto          tipoAtto;
	
	
	/* Liste */
	private List<TipoFinanziamento>      listaTipoFinanziamento        = new ArrayList<TipoFinanziamento>();
	private List<TipoFondo>              listaTipoFondo                = new ArrayList<TipoFondo>();
	private List<TipoAtto>               listaTipoAtto                 = new ArrayList<TipoAtto>();
	
	
	private List<CategoriaCapitolo>      listaCategoriaCapitolo        = new ArrayList<CategoriaCapitolo>();
	
	/* Liste derivate */
	private List<ElementoPianoDeiConti>            listaElementoPianoDeiConti            = new ArrayList<ElementoPianoDeiConti>();
	private List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = new ArrayList<StrutturaAmministrativoContabile>();
	
	/* Stringhe di utilita' per il post-aggiornamento */
	private String pdcFinanziario;
	private String strutturaAmministrativoResponsabile;
	
	/* Booleani rappresentanti i campi, per la editabilita' */
	private boolean elementoPianoDeiContiEditabile;
	private boolean strutturaAmministrativoContabileEditabile;
	private boolean tipoFinanziamentoEditabile;
	private boolean tipoFondoEditabile;
	
	
	private boolean categoriaCapitoloEditabile;
	
	//SIAC-8749, le descrizioni devono essere editabili
	private boolean descrizioneEditabile = true;
	private boolean descrizioneArticoloEditabile = true;
	private boolean flagRilevanteIvaEditabile;
	private boolean noteEditabile;
	private boolean flagImpegnabileEditabile;
	
	/* Per il ritorno all'aggiornamento */
	private boolean daAggiornamento = false;
	private Integer annoImporti;
	
	/* dati aggiuntivi per l'inserimento del capitolo da variazione*/
	private int uidVariazioneCapitolo;
	private Boolean daVariazione;
	/* Getter e setter */
	
	/**
	 * @return the elementoPianoDeiConti
	 */
	public ElementoPianoDeiConti getElementoPianoDeiConti() {
		return elementoPianoDeiConti;
	}

	/**
	 * @param elementoPianoDeiConti the elementoPianoDeiConti to set
	 */
	public void setElementoPianoDeiConti(ElementoPianoDeiConti elementoPianoDeiConti) {
		this.elementoPianoDeiConti = elementoPianoDeiConti;
	}

	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the tipoFinanziamento
	 */
	public TipoFinanziamento getTipoFinanziamento() {
		return tipoFinanziamento;
	}

	/**
	 * @param tipoFinanziamento the tipoFinanziamento to set
	 */
	public void setTipoFinanziamento(TipoFinanziamento tipoFinanziamento) {
		this.tipoFinanziamento = tipoFinanziamento;
	}

	/**
	 * @return the tipoFondo
	 */
	public TipoFondo getTipoFondo() {
		return tipoFondo;
	}

	/**
	 * @param tipoFondo the tipoFondo to set
	 */
	public void setTipoFondo(TipoFondo tipoFondo) {
		this.tipoFondo = tipoFondo;
	}

	/**
	 * @return the tipoAtto
	 */
	public TipoAtto getTipoAtto() {
		return tipoAtto;
	}

	/**
	 * @param tipoAtto the tipoAtto to set
	 */
	public void setTipoAtto(TipoAtto tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	
	/**
	 * @return the listaTipoFinanziamento
	 */
	public List<TipoFinanziamento> getListaTipoFinanziamento() {
		return listaTipoFinanziamento;
	}

	/**
	 * @param listaTipoFinanziamento the listaTipoFinanziamento to set
	 */
	public void setListaTipoFinanziamento(List<TipoFinanziamento> listaTipoFinanziamento) {
		this.listaTipoFinanziamento = listaTipoFinanziamento;
	}

	/**
	 * @return the listaTipoFondo
	 */
	public List<TipoFondo> getListaTipoFondo() {
		return listaTipoFondo;
	}

	/**
	 * @param listaTipoFondo the listaTipoFondo to set
	 */
	public void setListaTipoFondo(List<TipoFondo> listaTipoFondo) {
		this.listaTipoFondo = listaTipoFondo;
	}

	/**
	 * @return the listaTipoAtto
	 */
	public List<TipoAtto> getListaTipoAtto() {
		return listaTipoAtto;
	}

	/**
	 * @param listaTipoAtto the listaTipoAtto to set
	 */
	public void setListaTipoAtto(List<TipoAtto> listaTipoAtto) {
		this.listaTipoAtto = listaTipoAtto;
	}

	/**
	 * @return the listaCategoriaCapitolo
	 */
	public List<CategoriaCapitolo> getListaCategoriaCapitolo() {
		return listaCategoriaCapitolo;
	}

	/**
	 * @param listaCategoriaCapitolo the listaCategoriaCapitolo to set
	 */
	public void setListaCategoriaCapitolo(List<CategoriaCapitolo> listaCategoriaCapitolo) {
		this.listaCategoriaCapitolo = listaCategoriaCapitolo;
	}

	/**
	 * @return the listaElementoPianoDeiConti
	 */
	public List<ElementoPianoDeiConti> getListaElementoPianoDeiConti() {
		return listaElementoPianoDeiConti;
	}

	/**
	 * @param listaElementoPianoDeiConti the listaElementoPianoDeiConti to set
	 */
	public void setListaElementoPianoDeiConti(List<ElementoPianoDeiConti> listaElementoPianoDeiConti) {
		this.listaElementoPianoDeiConti = listaElementoPianoDeiConti;
	}

	/**
	 * @return the listaStrutturaAmministrativoContabile
	 */
	public List<StrutturaAmministrativoContabile> getListaStrutturaAmministrativoContabile() {
		return listaStrutturaAmministrativoContabile;
	}

	/**
	 * @param listaStrutturaAmministrativoContabile the listaStrutturaAmministrativoContabile to set
	 */
	public void setListaStrutturaAmministrativoContabile(List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		this.listaStrutturaAmministrativoContabile = listaStrutturaAmministrativoContabile;
	}

	/**
	 * @return the pdcFinanziario
	 */
	public String getPdcFinanziario() {
		return pdcFinanziario;
	}

	/**
	 * @param pdcFinanziario the pdcFinanziario to set
	 */
	public void setPdcFinanziario(String pdcFinanziario) {
		this.pdcFinanziario = pdcFinanziario;
	}

	/**
	 * @return the strutturaAmministrativoResponsabile
	 */
	public String getStrutturaAmministrativoResponsabile() {
		return strutturaAmministrativoResponsabile;
	}

	/**
	 * @param strutturaAmministrativoResponsabile the strutturaAmministrativoResponsabile to set
	 */
	public void setStrutturaAmministrativoResponsabile(String strutturaAmministrativoResponsabile) {
		this.strutturaAmministrativoResponsabile = strutturaAmministrativoResponsabile;
	}

	/**
	 * @return the elementoPianoDeiContiEditabile
	 */
	public boolean isElementoPianoDeiContiEditabile() {
		return elementoPianoDeiContiEditabile;
	}

	/**
	 * @param elementoPianoDeiContiEditabile the elementoPianoDeiContiEditabile to set
	 */
	public void setElementoPianoDeiContiEditabile(boolean elementoPianoDeiContiEditabile) {
		this.elementoPianoDeiContiEditabile = elementoPianoDeiContiEditabile;
	}

	/**
	 * @return the strutturaAmministrativoContabileEditabile
	 */
	public boolean isStrutturaAmministrativoContabileEditabile() {
		return strutturaAmministrativoContabileEditabile;
	}

	/**
	 * @param strutturaAmministrativoContabileEditabile the strutturaAmministrativoContabileEditabile to set
	 */
	public void setStrutturaAmministrativoContabileEditabile(boolean strutturaAmministrativoContabileEditabile) {
		this.strutturaAmministrativoContabileEditabile = strutturaAmministrativoContabileEditabile;
	}

	/**
	 * @return the tipoFinanziamentoEditabile
	 */
	public boolean isTipoFinanziamentoEditabile() {
		return tipoFinanziamentoEditabile;
	}

	/**
	 * @param tipoFinanziamentoEditabile the tipoFinanziamentoEditabile to set
	 */
	public void setTipoFinanziamentoEditabile(boolean tipoFinanziamentoEditabile) {
		this.tipoFinanziamentoEditabile = tipoFinanziamentoEditabile;
	}

	/**
	 * @return the tipoFondoEditabile
	 */
	public boolean isTipoFondoEditabile() {
		return tipoFondoEditabile;
	}

	/**
	 * @param tipoFondoEditabile the tipoFondoEditabile to set
	 */
	public void setTipoFondoEditabile(boolean tipoFondoEditabile) {
		this.tipoFondoEditabile = tipoFondoEditabile;
	}


	/**
	 * @return the categoriaCapitoloEditabile
	 */
	public boolean isCategoriaCapitoloEditabile() {
		return categoriaCapitoloEditabile;
	}

	/**
	 * @param categoriaCapitoloEditabile the categoriaCapitoloEditabile to set
	 */
	public void setCategoriaCapitoloEditabile(boolean categoriaCapitoloEditabile) {
		this.categoriaCapitoloEditabile = categoriaCapitoloEditabile;
	}

	/**
	 * @return the descrizioneEditabile
	 */
	public boolean isDescrizioneEditabile() {
		return descrizioneEditabile;
	}

	/**
	 * @param descrizioneEditabile the descrizioneEditabile to set
	 */
	public void setDescrizioneEditabile(boolean descrizioneEditabile) {
		this.descrizioneEditabile = descrizioneEditabile;
	}

	/**
	 * @return the descrizioneArticoloEditabile
	 */
	public boolean isDescrizioneArticoloEditabile() {
		return descrizioneArticoloEditabile;
	}

	/**
	 * @param descrizioneArticoloEditabile the descrizioneArticoloEditabile to set
	 */
	public void setDescrizioneArticoloEditabile(boolean descrizioneArticoloEditabile) {
		this.descrizioneArticoloEditabile = descrizioneArticoloEditabile;
	}

	/**
	 * @return the flagRilevanteIvaEditabile
	 */
	public boolean isFlagRilevanteIvaEditabile() {
		return flagRilevanteIvaEditabile;
	}

	/**
	 * @param flagRilevanteIvaEditabile the flagRilevanteIvaEditabile to set
	 */
	public void setFlagRilevanteIvaEditabile(boolean flagRilevanteIvaEditabile) {
		this.flagRilevanteIvaEditabile = flagRilevanteIvaEditabile;
	}

	/**
	 * @return the noteEditabile
	 */
	public boolean isNoteEditabile() {
		return noteEditabile;
	}

	/**
	 * @param noteEditabile the noteEditabile to set
	 */
	public void setNoteEditabile(boolean noteEditabile) {
		this.noteEditabile = noteEditabile;
	}

	/**
	 * @return the flagImpegnabileEditabile
	 */
	public boolean isFlagImpegnabileEditabile() {
		return flagImpegnabileEditabile;
	}

	/**
	 * @param flagImpegnabileEditabile the flagImpegnabileEditabile to set
	 */
	public void setFlagImpegnabileEditabile(boolean flagImpegnabileEditabile) {
		this.flagImpegnabileEditabile = flagImpegnabileEditabile;
	}

	/**
	 * @return the daAggiornamento
	 */
	public boolean isDaAggiornamento() {
		return daAggiornamento;
	}

	/**
	 * @param daAggiornamento the daAggiornamento to set
	 */
	public void setDaAggiornamento(boolean daAggiornamento) {
		this.daAggiornamento = daAggiornamento;
	}

	/**
	 * @return the annoImporti
	 */
	public Integer getAnnoImporti() {
		return annoImporti;
	}

	/**
	 * @param annoImporti the annoImporti to set
	 */
	public void setAnnoImporti(Integer annoImporti) {
		this.annoImporti = annoImporti;
	}
	
	/**
	 * @return the uidVariazioneCapitolo
	 */
	public int getUidVariazioneCapitolo() {
		return uidVariazioneCapitolo;
	}

	/**
	 * @param uidVariazioneCapitolo the uidVariazioneCapitolo to set
	 */
	public void setUidVariazioneCapitolo(int uidVariazioneCapitolo) {
		this.uidVariazioneCapitolo = uidVariazioneCapitolo;
	}
	
	/**
	 * @return the daVariazione
	 */
	public Boolean getDaVariazione() {
		return daVariazione;
	}

	/**
	 * @param daVariazione the daVariazione to set
	 */
	public void setDaVariazione(Boolean daVariazione) {
		this.daVariazione = daVariazione;
	}
	
	/**
	 * @return the placeholderAnnoExCapitolo
	 */
	public String getPlaceholderAnnoExCapitolo() {
		return Integer.toString(getAnnoEsercizioInt().intValue() - 1);
	}
	
	/* **** REQUESTS **** */
	
	/**
	 * Restituisce una Request di tipo {@link ControllaClassificatoriModificabiliCapitolo} a partire dal Model.
	 * 
	 * @param capitolo il capitolo da inserire nella request 
	 * @param tipo     il tipo del capitolo
	 * 
	 * @return la Request creata
	 */
	public ControllaClassificatoriModificabiliCapitolo creaRequestControllaClassificatoriModificabiliCapitolo(Capitolo<?, ?> capitolo, TipoCapitolo tipo) {
		ControllaClassificatoriModificabiliCapitolo request = creaRequest(ControllaClassificatoriModificabiliCapitolo.class);
		
		request.setBilancio(getBilancio());
		
		request.setEnte(getEnte());
		// Il capitolo può essere nullo, nel caso di ingresso nella ricerca
		if(capitolo != null) {
			request.setNumeroCapitolo(capitolo.getNumeroCapitolo());
			request.setNumeroArticolo(capitolo.getNumeroArticolo());
			request.setNumeroUEB(capitolo.getNumeroUEB());
		}
		request.setTipoCapitolo(tipo);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link ControllaAttributiModificabiliCapitolo} a partire dal Model.
	 * 
	 * @param capitolo il capitolo da inserire nella request 
	 * @param tipo     il tipo del capitolo
	 * 
	 * @return la Request creata
	 */
	public ControllaAttributiModificabiliCapitolo creaRequestControllaAttributiModificabiliCapitolo(Capitolo<?, ?> capitolo, TipoCapitolo tipo) {
		ControllaAttributiModificabiliCapitolo request = creaRequest(ControllaAttributiModificabiliCapitolo.class);
		
		request.setBilancio(getBilancio());
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		// Il capitolo può essere nullo, nel caso di ingresso nella ricerca
		if(capitolo != null) {
			request.setNumeroArticolo(capitolo.getNumeroArticolo());
			request.setNumeroCapitolo(capitolo.getNumeroCapitolo());
			request.setNumeroUEB(capitolo.getNumeroUEB());
		}
		request.setTipoCapitolo(tipo);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaVariazioniCapitoloPerAggiornamentoCapitolo} a partire dal Model.
	 * 
	 * @param uid l'uid del capitolo per cui efffettuare la richiesta
	 * @return la Request creata
	 */
	public RicercaVariazioniCapitoloPerAggiornamentoCapitolo creaRequestRicercaVariazioniCapitoloPerAggiornamentoCapitolo(Integer uid) {
		RicercaVariazioniCapitoloPerAggiornamentoCapitolo request = creaRequest(RicercaVariazioniCapitoloPerAggiornamentoCapitolo.class);
		request.setUidCapitolo(uid);
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link ContaClassificatoriERestituisciSeSingolo} a partire dal Model.
	 * 
	 * @param tipologiaClassificatore la tipologia di classificatore
	 * @return la Request creata
	 */
	public ContaClassificatoriERestituisciSeSingolo creaRequestContaClassificatoriERestituisciSeSingolo(TipologiaClassificatore tipologiaClassificatore) {
		ContaClassificatoriERestituisciSeSingolo request = creaRequest(ContaClassificatoriERestituisciSeSingolo.class);
		
		request.setAnno(getAnnoEsercizioInt());
		request.setTipologiaClassificatore(tipologiaClassificatore);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno}.
	 * @param classificatoreGerarchico il classificatore da cercare
	 * @param tipologiaClassificatore la tipologia del classificatore
	 * @return la request creata
	 */
	public LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno creaRequestLeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(ClassificatoreGerarchico classificatoreGerarchico, TipologiaClassificatore tipologiaClassificatore) {
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno req = creaRequest(LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno.class);
		
		req.setAnno(getAnnoEsercizioInt());
		req.setClassificatore(classificatoreGerarchico);
		req.setTipologiaClassificatore(tipologiaClassificatore);
		
		return req;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Ottiene codice e descrizione dalla codifica.
	 * 
	 * @param codifica la codifica
	 * b
	 * @return il codice e la descrizione
	 */
	protected String getCodiceEDescrizione(Codifica codifica) {
		return codifica == null ? "" : (codifica.getCodice() + " - " + codifica.getDescrizione());
	}
	
	
	
	/**
	 * Metodo di utilit&agrave; per l'inserimento dei classificatori generici se e solo se essi sono
	 * <ul>
	 *  <li>non-<code>null</code>;</li>
	 *  <li>con UID valorizzato.</li>
	 * </ul>
	 * 
	 * @param lista						la lista di classificatori cui apporre il classificatore
	 * @param classificatoreGenerico	il classificatore da apporre
	 */
	protected void addClassificatoreGenericoALista(List<ClassificatoreGenerico> lista, ClassificatoreGenerico classificatoreGenerico) {
		if(classificatoreGenerico != null && classificatoreGenerico.getUid() != 0) {
			lista.add(classificatoreGenerico);
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per l'inserimento dei classificatori generici se e solo se essi sono
	 * <ul>
	 *  <li>non-<code>null</code>;</li>
	 *  <li>con UID valorizzato.</li>
	 * </ul>
	 * 
	 * @param lista							   la lista di classificatori cui apporre il classificatore
	 * @param classificatoreGenericoDaFrontEnd il classificatore da apporre
	 * @param classificatoreGenericoInSessione il classificatore gi&agrave; in sessione
	 * @param editabile						   definisce se il classificatore sia editabile
	 */
	protected void addClassificatoreGenericoALista(List<ClassificatoreGenerico> lista, ClassificatoreGenerico classificatoreGenericoDaFrontEnd,
			ClassificatoreGenerico classificatoreGenericoInSessione, boolean editabile) {
		lista.add(valutaInserimento(classificatoreGenericoDaFrontEnd, classificatoreGenericoInSessione, editabile));
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle informazioni relative ai classificatori a partire dalle liste in sessione.
	 * 
	 * @param sessionHandler l'handler per la sessione
	 */
	public void caricaClassificatoriDaSessione(SessionHandler sessionHandler) {
		elementoPianoDeiConti = caricaClassificatoriDaSessione(sessionHandler, elementoPianoDeiConti, BilSessionParameter.LISTA_ELEMENTO_PIANO_DEI_CONTI);
		strutturaAmministrativoContabile = caricaClassificatoriDaSessione(sessionHandler, strutturaAmministrativoContabile,
				BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		
		// Prima di poter compilare le stringhe di utilita', serve che gli elementi da cui le costruisco siano non-null
		if(elementoPianoDeiConti != null && StringUtils.isBlank(pdcFinanziario)) {
			pdcFinanziario = getCodiceEDescrizione(getElementoPianoDeiConti());
		}
		if(strutturaAmministrativoContabile != null && StringUtils.isBlank(strutturaAmministrativoResponsabile)) {
			strutturaAmministrativoResponsabile = getCodiceEDescrizione(getStrutturaAmministrativoContabile());
		}
		
		tipoFinanziamento = caricaClassificatoriDaSessione(sessionHandler, tipoFinanziamento, BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		tipoFondo = caricaClassificatoriDaSessione(sessionHandler, tipoFondo, BilSessionParameter.LISTA_TIPO_FONDO);
		
		// Caricamento dei classificatori generici
		caricaClassificatoriGenericiDaSessione(sessionHandler);
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle informazioni relative ai classificatori a partire dalle liste in sessione.
	 * @param <T> la tipizzazione della codifica
	 * 
	 * @param sessionHandler     l'handler per la sessione
	 * @param classificatore     il classificatore da ricercare
	 * @param nomeClassificatore il nome del classificatore in sessione
	 * 
	 * @return il classificatore valorizzato come da lista in sessione
	 */
	protected <T extends Codifica> T caricaClassificatoriDaSessione(SessionHandler sessionHandler, T classificatore, BilSessionParameter nomeClassificatore) {
		List<T> lista = sessionHandler.getParametro(nomeClassificatore);
		T classif = ComparatorUtils.searchByUid(lista, classificatore);
		if(classif != null) {
			classif.setDataFineValidita(null);
		}
		return classif;
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
	private ElementoPianoDeiConti caricaClassificatoriDaSessione(SessionHandler sessionHandler, ElementoPianoDeiConti classificatore, BilSessionParameter nomeClassificatore) {
		List<ElementoPianoDeiConti> lista = sessionHandler.getParametro(nomeClassificatore);
		return ComparatorUtils.searchByUidWithChildren(lista, classificatore);
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
	private StrutturaAmministrativoContabile caricaClassificatoriDaSessione(SessionHandler sessionHandler, StrutturaAmministrativoContabile classificatore, BilSessionParameter nomeClassificatore) {
		List<StrutturaAmministrativoContabile> lista = sessionHandler.getParametro(nomeClassificatore);
		return ComparatorUtils.searchByUidWithChildren(lista, classificatore);
	}
	
	/**
	 * Carica i classificatori generici a partire dalla sessione
	 * 
	 * @param sessionHandler l'handler per la sessione
	 */
	protected abstract void caricaClassificatoriGenericiDaSessione(SessionHandler sessionHandler);
	
	/**
	 * Metodo di utilit&agrave; per l'inserimento degli importi dei Capitolo di entrata previsione.
	 * <br>
	 * Il presente metodo controlla che sia valorizzato il campo {@code annoCompetenza}. In caso constrario, esso viene
	 * valorizzato tramite l'utilizzo del parametro {@code anno}.
	 * @param <T> la tipizzazione degli importi
	 * 
	 * @param lista			  la lista degli importi
	 * @param importiCapitolo l'importo da apporre alla lista
	 * @param anno			  l'anno cui l'importo si riferisce
	 */
	protected <T extends ImportiCapitolo> void addImportoCapitoloALista(List<T> lista, T importiCapitolo, Integer anno) {
		if(importiCapitolo.getAnnoCompetenza() == null || importiCapitolo.getAnnoCompetenza() == 0) {
			importiCapitolo.setAnnoCompetenza(anno);
		}
		lista.add(importiCapitolo);
	}
	
	/**
	 * Metodo di utilit&agrave; per la valutazione dell'inserimento di una data Entita nella request di aggiornamento.
	 * @param <T> la tipizzazione dell'entita
	 * 
	 * @param daFrontEnd l'entita ottenuta da frontEnd
	 * @param inSessione l'entita gi&agrave; presente in sessione
	 * @param editabile  definisce se il campo sia editabile
	 * 
	 * @return l'oggetto da inserire nella request
	 */
	public <T extends Entita> T valutaInserimento(T daFrontEnd, T inSessione, boolean editabile) {
		T entita = null;
		
		if(!editabile) {
			entita = inSessione;
		} else if(inSessione != null && (daFrontEnd == null || daFrontEnd.getUid() == 0)) {
			entita = inSessione;
			entita.setDataFineValidita(new Date());
			
		} else if(daFrontEnd != null && daFrontEnd.getUid() != 0) {
			entita = daFrontEnd;
			entita.setDataFineValidita(null);
		}
		
		return entita;
	}
	
	/**
	 * Metodo di utilit&agrave; per la valutazione dell'inserimento di una data Entita nella request di aggiornamento massivo.
	 * @param <T> la tipizzazione dell'entita
	 * 
	 * @param daFrontEnd l'entita ottenuta da frontEnd
	 * @param inSessione l'entita gi&agrave; presente in sessione
	 * @param editabile  definisce se il campo sia editabile
	 * 
	 * @return l'oggetto da inserire nella request
	 */
	protected <T extends Entita> T valutaInserimentoMassivo(T daFrontEnd, T inSessione, boolean editabile) {
		T entita = null;
		
		// Se non ho l'editabilità, restitisco il null
		if(!editabile) {
			return entita;
		}
		
		if(inSessione != null && (daFrontEnd == null || daFrontEnd.getUid() == 0)) {
			entita = inSessione;
			entita.setDataFineValidita(new Date());
		} else if(daFrontEnd != null && daFrontEnd.getUid() != 0) {
			entita = daFrontEnd;
			entita.setDataFineValidita(null);
		}
		
		return entita;
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle stringhe di utilit&agrave;.
	 */
	protected void valorizzaStringheUtilita() {
		pdcFinanziario = getElementoPianoDeiConti() != null
				? getCodiceEDescrizione(getElementoPianoDeiConti())
				: "Nessun P.d.C. finanziario selezionato";
		strutturaAmministrativoResponsabile = getStrutturaAmministrativoContabile() != null
				? getCodiceEDescrizione(getStrutturaAmministrativoContabile())
				: "Nessuna Struttura Amministrativa Responsabile selezionata";
	}
	
	/**
	 * Metodo di utilit&agrave; per la valutazione della modificabilit&agrave; dei classificatori per l'aggiornamento.
	 *  
	 * @param response  la response da cui ottenere la modificabilt&agrave; dei campi
	 * @param isMassivo definisce le la valutazione si riferisce al caso massivo
	 */
	public void valutaModificabilitaClassificatori(ControllaClassificatoriModificabiliCapitoloResponse response, boolean isMassivo) {
		// Controllo ciascun classificatore per la modificabilità
		
		// Controllo se il classificatore è unico: in tal caso, ogni classificatore sarà modificabile
		long stessoNumCapArt = response.getStessoNumCapArt() != null ? response.getStessoNumCapArt().longValue() : 0L;
		boolean unico = stessoNumCapArt <= 1L;
		
		// Elemento del piano dei conti
		elementoPianoDeiContiEditabile = isEditabile(unico, isMassivo, response,
				TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III,
				TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V);
		// Struttura amministrativa contabile
		strutturaAmministrativoContabileEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.CDC, TipologiaClassificatore.CDR);
		
		tipoFinanziamentoEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TIPO_FINANZIAMENTO);
		tipoFondoEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TIPO_FONDO);
		
		categoriaCapitoloEditabile = !isMassivo || unico;
		// Classificatori generici
		isClassificatoriGenericiEditabile(unico, isMassivo, response);
	}
	
	/**
	 * Controlla se il classificatore di dato tipo sia editabile.
	 * 
	 * @param unico     se l'elemento di getBilancio() sia unico
	 * @param isMassivo se il caso d'uso sia massivo
	 * @param response  la response del servizio di controllo modificabilit&agrave;
	 * @param tipologie le tipologie di classificatori da controllare
	 * 
	 * @return <code>true</code> se il classificatore &eacute; editabile; <code>false</code> in caso contrario.
	 */
	protected boolean isEditabile(boolean unico, boolean isMassivo, ControllaClassificatoriModificabiliCapitoloResponse response, TipologiaClassificatore... tipologie) {
		// Null-safety
		if(tipologie == null || tipologie.length == 0) {
			return false;
		}
		
		
		boolean modificabileClassificatore = false;
		for(TipologiaClassificatore tipologia : tipologie) {
			modificabileClassificatore = modificabileClassificatore || response.isModificabile(tipologia);
		}
		
		//CR 3591
		boolean modificabileClassificatoreMG = false;
		for(TipologiaClassificatore tipologia : tipologie) {
			modificabileClassificatoreMG = modificabileClassificatoreMG || response.isModificabilePerMovimentoGestione(tipologia);
		}
		
		return unico || (modificabileClassificatore ^ (isMassivo && modificabileClassificatoreMG));
		
		//Tabella casi possibili:
		//             false                 false        false                            -> false (ok)
		//             false                 true         false                            -> false (ok)
		//             false                 true         true                             -> true  (ok)
		                                          
        //             true                  false        true                             -> true  (ok)
		//             true                  true         true                             -> false (ok)

	}
	
	/**
	 * Controlla se i classificatori generici siano editabili.
	 * 
	 * @param unico     se l'elemento di getBilancio() sia unico
	 * @param isMassivo se il caso d'uso sia massivo
	 * @param response  la response del servizio di controllo modificabilit&agrave;
	 */
	protected abstract void isClassificatoriGenericiEditabile(boolean unico, boolean isMassivo, ControllaClassificatoriModificabiliCapitoloResponse response);
	
	/**
	 * Metodo di utilit&agrave; per la valutazione della modificabilit&agrave; degli attributi per l'aggiornamento.
	 *  
	 * @param response  la response da cui ottenere la modificabilt&agrave; dei campi
	 * @param isMassivo definisce le la valutazione si riferisce al caso massivo
	 */
	public void valutaModificabilitaAttributi(ControllaAttributiModificabiliCapitoloResponse response, boolean isMassivo) {
		// Controllo ciascun attributo per la modificabilita'
		
		// Controllo se il classificatore è unico: in tal caso, ogni classificatore sarà modificabile
		long stessoNumCapArt = response.getStessoNumCapArt() != null ? response.getStessoNumCapArt().longValue() : 0L;
		long stessoNumCap = response.getStessoNumCap() != null ? response.getStessoNumCap().longValue() : 0L;
		boolean unicoArticolo = stessoNumCapArt <= 1L;
		boolean unicoCapitolo = stessoNumCap <= 1L;
		
		// Se il numero di elementi con lo stesso capitolo è pari al numero di elementi con lo stesso articolo, allora esiste solo l'articolo in questione
		boolean soloCapitoliDiDatoArticolo = stessoNumCap == stessoNumCapArt;
		
		// Descrizione
		//SIAC-8749
		descrizioneEditabile = true; //unicoCapitolo || (soloCapitoliDiDatoArticolo && isMassivo);
		descrizioneArticoloEditabile = isMassivo || unicoArticolo || soloCapitoliDiDatoArticolo;
		
		// Flag Rilevante IVA
		flagRilevanteIvaEditabile = isEditabile(unicoArticolo, isMassivo, response, TipologiaAttributo.FLAG_RILEVANTE_IVA);
		// Note
		noteEditabile = isEditabile(unicoArticolo, isMassivo, response, TipologiaAttributo.NOTE);
		// Flag Impegnabile
		flagImpegnabileEditabile = unicoArticolo || !isMassivo;
	}
	
	/**
	 * Controlla se l'attributo di dato tipo sia editabile.
	 * 
	 * @param unico     se l'elemento di getBilancio() sia unico
	 * @param isMassivo se il caso d'uso sia massivo
	 * @param response  la response del servizio di controllo modificabilit&agrave;
	 * @param tipologie le tipologie di classificatori da controllare
	 * 
	 * @return <code>true</code> se il classificatore &eacute; editabile; <code>false</code> in caso contrario.
	 */
	protected boolean isEditabile(boolean unico, boolean isMassivo, ControllaAttributiModificabiliCapitoloResponse response, TipologiaAttributo... tipologie) {
		// Null-safety
		if(tipologie == null || tipologie.length == 0) {
			return false;
		}
		boolean modificabileClassificatore = false;
		for(TipologiaAttributo tipologia : tipologie) {
			modificabileClassificatore = modificabileClassificatore || response.isModificabile(tipologia);
		}
		return unico || modificabileClassificatore ^ isMassivo;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'estrazione del label del classificatore generico a partire dalla lista.
	 * 
	 * @param listaClassificatoriGenerici	la lista del Classificatore da cui estrarre il label
	 * 
	 * @return il label estratto
	 */
	protected String ottieniLabelClassificatoreGenerico(List<ClassificatoreGenerico> listaClassificatoriGenerici) {
		if(listaClassificatoriGenerici != null && !listaClassificatoriGenerici.isEmpty()) {
			return listaClassificatoriGenerici.get(0).getTipoClassificatore().getDescrizione();
		}
		return null;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'estrazione del label del classificatore generico a partire dal classificatore.
	 * @param classificatoreGenerici il classificatore
	 * @return il label estratto
	 */
	protected String ottieniLabelClassificatoreGenerico(ClassificatoreGenerico classificatoreGenerici) {
		return classificatoreGenerici == null || classificatoreGenerici.getTipoClassificatore() == null
			? null
			: classificatoreGenerici.getTipoClassificatore().getDescrizione();
	}
	
	
	
	/**
	 * Imposta un dato tra due campi dato il booleano.
	 * @param <T> il tipo
	 * 
	 * @param editabile se il campo sia editabile o meno
	 * @param campo     il campo da popolare
	 * @param vecchio   il campo con il valore da ottenere
	 * 
	 * @return il campo impostato
	 */
	protected <T> T impostaIlDato(boolean editabile, T campo, T vecchio) {
		T result = campo;
		if(!editabile) {
			result = vecchio;
		}
		return result;
	}
	
	/**
	 * Imposta l'entit&agrave; selezionata all'interno del capitolo.
	 * <br>
	 * <strong>ATTENZIONE!</strong> Non funziona con il TipoAtto.
	 * @param <C> la tipizzazione del capitolo
	 * @param <E> la tipizzazione dell'entita
	 * 
	 * @param capitolo  il capitolo in cui injettare il dato
	 * @param entita    l'entit&agrave; da injettare
	 * 
	 * @throws IllegalArgumentException nel caso in cui l'invocazione non vada a buon fine
	 */
	protected <C extends Capitolo<?, ?>, E extends Entita> void impostaIlParametroNelCapitolo(C capitolo, E entita) {
		if(entita == null || entita.getUid() == 0) {
			return;
		}
		// Fallback in caso di entita null
		Class<? extends Entita> clazz = entita.getClass();
		String nomeSetter = "set" + StringUtils.capitalize(clazz.getSimpleName());
		try {
			Method method = ReflectionUtil.silentlyFindMethod(capitolo.getClass(), nomeSetter, clazz);
			ReflectionUtil.silentlyInvokeMethod(method, capitolo, entita);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("IllegalArgumentException nell'impostazione del parametro. Invocazione del metodo " + nomeSetter, e);
		}
	}
	
	/**
	 * Injetta il codice della codifica nella request di ricerca nel caso in cui tale codifica sia valida.
	 * 
	 * @param request   la request in cui injettare il codice
	 * @param codifica  la codifica da cui ottenere il codice
	 * @param nomeCampo il nome del campo in cui injettare la codifica
	 * 
	 * @throws IllegalArgumentException nel caso in cui il nome del campo sia non corretto
	 */
	protected void injettaCodiceCodificaNellaRicercaSeValida(Serializable request, Codifica codifica, String nomeCampo) {
		// Se la codifica non è valorizzata correttamente, esco subito dal metodo
		if(codifica == null || StringUtils.isBlank(codifica.getCodice())) {
			return;
		}
		
		ReflectionUtil.invokeSetterMethod(request, nomeCampo, String.class, codifica.getCodice());
	}
	
	/**
	 * Injetta l'atto di legge nella request di ricerca nel caso in cui tale codifica sia valida.
	 * 
	 * @param request     la request in cui injettare il codice
	 * @param attoDiLegge l'atto di legge da cui ottenere i dati
	 */
	protected void injettaAttoDiLeggeNellaRicercaSeValido(Serializable request, AttoDiLegge attoDiLegge) {
		// Se l'atto di legge è null esco immediatamente
		if(attoDiLegge == null) {
			return;
		}
		
		ReflectionUtil.invokeSetterMethod(request, "setAnnoAttoDilegge", Integer.class, attoDiLegge.getAnno());
		ReflectionUtil.invokeSetterMethod(request, "setArticoloAttoDilegge", String.class, attoDiLegge.getArticolo());
		ReflectionUtil.invokeSetterMethod(request, "setCommaAttoDilegge", String.class, attoDiLegge.getComma());
		ReflectionUtil.invokeSetterMethod(request, "setNumeroAttoDilegge", Integer.class, attoDiLegge.getNumero());
		ReflectionUtil.invokeSetterMethod(request, "setPuntoAttoDilegge", String.class, attoDiLegge.getPunto());
		
		injettaCodiceCodificaNellaRicercaSeValida(request, attoDiLegge.getTipoAtto(), "setTipoAttoDilegge");
	}
	
	/**
	 * Injetta il codice della codifica nella request di ricerca nel caso in cui tale codifica sia valida.
	 * 
	 * @param request   la request in cui injettare il codice
	 * @param stringa   la stringa da injettare
	 * @param nomeCampo il nome del campo in cui injettare la codifica
	 * 
	 * @throws IllegalArgumentException nel caso in cui il nome del campo sia non corretto
	 */
	protected void injettaStringaRicercaSeValida(Serializable request, String stringa, String nomeCampo) {
		// Se la stringa è vuota o null esco immediatamente
		if(StringUtils.isBlank(stringa)) {
			return;
		}
		
		ReflectionUtil.invokeSetterMethod(request, nomeCampo, String.class, StringUtils.trim(stringa));
	}
	
	/**
	 * Injetta il capitolo nella request di ricerca nel caso in cui tale entit&agrave; sia valida.
	 * 
	 * @param request  la request in cui injettare il codice
	 * @param capitolo il capitolo da cui ottenere i dati
	 * 
	 * @throws IllegalArgumentException nel caso in cui il nome del campo sia non corretto
	 */
	protected void injettaCapitoloNellaRicercaSeValido(Serializable request, Capitolo<?, ?> capitolo) {
		// Se il capitolo non è valorizzato esco immediatamente
		if(capitolo == null) {
			return;
		}
		
		ReflectionUtil.invokeSetterMethod(request, "setAnnoCapitolo", Integer.class, capitolo.getAnnoCapitolo());
		ReflectionUtil.invokeSetterMethod(request, "setNumeroCapitolo", Integer.class, capitolo.getNumeroCapitolo());
		ReflectionUtil.invokeSetterMethod(request, "setNumeroArticolo", Integer.class, capitolo.getNumeroArticolo());
		ReflectionUtil.invokeSetterMethod(request, "setNumeroUEB", Integer.class, capitolo.getNumeroUEB());
		
		// Stringhe
		injettaStringaRicercaSeValida(request, capitolo.getDescrizione(), "setDescrizioneCapitolo");
		injettaStringaRicercaSeValida(request, capitolo.getDescrizioneArticolo(), "setDescrizioneArticolo");
		
		// Campi ex
		ReflectionUtil.invokeSetterMethod(request, "setExAnnoCapitolo", Integer.class, capitolo.getExAnnoCapitolo());
		ReflectionUtil.invokeSetterMethod(request, "setExNumeroArticolo", Integer.class, capitolo.getExArticolo());
		ReflectionUtil.invokeSetterMethod(request, "setExNumeroCapitolo", Integer.class, capitolo.getExCapitolo());
		ReflectionUtil.invokeSetterMethod(request, "setExNumeroUEB", Integer.class, capitolo.getExUEB());
	}

	/**
	 * Crea una request per il servizio di {@link InserisciDettaglioVariazioneImportoCapitolo}.
	 * @param uidCapitolo l'uid del capitolo da utilizzare
	 * @return la request creata
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoCapitolo(int uidCapitolo){
		InserisciDettaglioVariazioneImportoCapitolo request = creaRequest(InserisciDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = new DettaglioVariazioneImportoCapitolo();
		
		Capitolo<?,?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		capitolo.setUid(uidCapitolo);
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
		dettaglioVariazioneImportoCapitolo.setFlagNuovoCapitolo(Boolean.TRUE);
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazioneCapitolo);
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		request.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		return request;
	}
	
	/**
	 * Injetta la Struttura Amministrativo Contabile nella request di ricerca nel caso in cui tale entit&agrave; sia valida.
	 * <br>
	 * Il metodo &eacute; a parte in quanto si richiede anche l'injezione del codice del tipo di Struttura.
	 * 
	 * @param request                          la request in cui injettare il codice
	 * @param sac la Struttura da cui ottenere i dati
	 * @param stringaUtilita                   la stringa di utilit&agrave; da cui ottenere il codice del tipo
	 * 
	 * @throws IllegalArgumentException nel caso in cui il nome del campo sia non corretto
	 */
	protected void injettaStrutturaAmministrativoContabileNellaRicercaSeValido(Serializable request, StrutturaAmministrativoContabile sac, 
			String stringaUtilita) {
		// Se la struttura non è valorizzata correttamente esco immediatamente
		if(sac == null || sac.getUid() == 0 || StringUtils.isBlank(stringaUtilita)) {
			return;
		}
		ReflectionUtil.invokeSetterMethod(request, "setCodiceStrutturaAmmCont", String.class, sac.getCodice());
		//task-90
		ReflectionUtil.invokeSetterMethod(request, "setIdStrutturaAmmCont", Integer.class, sac.getUid());
		
		String codiceTipoStrutturaAmmCont = Pattern.compile("\\d{3} -").matcher(strutturaAmministrativoResponsabile).find() ? 
				BilConstants.CODICE_CDC.getConstant() : BilConstants.CODICE_CDR.getConstant();
		ReflectionUtil.invokeSetterMethod(request, "setCodiceTipoStrutturaAmmCont", String.class, codiceTipoStrutturaAmmCont);
	}
	
	/**
	 * Controlla gli importi del capitolo equivalente, e nel caso non siano settati li inizializza.
	 * @param <IEQ> la tipizzaizone degli importi
	 * @param <CAP> la tipizzazione del capitolo
	 * 
	 * @param capitolo     il capitolo da controllare e popolare
	 * @param importiClazz la classe degli importi
	 */
	protected <IEQ extends ImportiCapitolo, CAP extends Capitolo<?, IEQ>> void controllaImportiCapitoloEquivalente(CAP capitolo, Class<IEQ> importiClazz) {
		// Check dei campi per gli importi del capitolo equivalente
		if(capitolo.getImportiCapitoloEquivalente() == null) {
			try {
				capitolo.setImportiCapitoloEquivalente(importiClazz.newInstance());
			} catch (Exception e) {
				// Ignoro l'errore: impossibile inizializzare gli importi
			}
			
		}
	}
	
	/**
	 * Crea un'Utilit&agrave; per i Parametri di Paginazione come dato aggiuntivo.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	protected ParametriPaginazione creaParametriPaginazioneComeDatoAggiuntivo() {
		return new ParametriPaginazione(0, 10000);
	}

	
	/**
	 * Reimposta i dati disabilitati causa non editabilit&agrave; nel model.
	 * 
	 * @param classificatoreAggiornamento i classificatori originali in sessione
	 */
	protected void setParametriDisabilitati(ClassificatoreAggiornamento classificatoreAggiornamento) {
		elementoPianoDeiConti = impostaIlDato(elementoPianoDeiContiEditabile, elementoPianoDeiConti, classificatoreAggiornamento.getElementoPianoDeiConti());
		strutturaAmministrativoContabile = impostaIlDato(strutturaAmministrativoContabileEditabile, strutturaAmministrativoContabile, classificatoreAggiornamento.getStrutturaAmministrativoContabile());
		tipoFinanziamento = impostaIlDato(tipoFinanziamentoEditabile, tipoFinanziamento, classificatoreAggiornamento.getTipoFinanziamento());
		tipoFondo = impostaIlDato(tipoFondoEditabile, tipoFondo, classificatoreAggiornamento.getTipoFondo());
		
	}
	//task-86
	public LeggiPropostaNumeroCapitolo leggiPropostaNumeroCapitolo() {
		LeggiPropostaNumeroCapitolo request = creaRequest(LeggiPropostaNumeroCapitolo.class);
		request.setEnte(getEnte());
		return request;
	}
}

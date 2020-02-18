/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodifiche;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodificheFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoStatoOperativoVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoStatoOperativoVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodificheResponse;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per la consultazione delle variazioni Codifiche. 
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 05/11/2013
 *
 */

public class ConsultaVariazioneCodificheModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4256856520682050552L;
	
	private Integer numeroVariazione;
	private Integer uidVariazione;
	private TipoVariazione tipoVariazione;
	private String applicazioneVariazione;
	private String descrizioneVariazione;
	private StatoOperativoVariazioneDiBilancio statoVariazione;
	private String noteVariazione;
	
	private Integer uidProvvedimento;
	private String tipoProvvedimento;
	private Integer annoProvvedimento;
	private Integer numeroProvvedimento;
	private String strutturaProvvedimento;
	private String oggettoProvvedimento;

	private List<ElementoCapitoloCodifiche> listaCapitoli = new ArrayList<ElementoCapitoloCodifiche>();
	//SIAC-6177
		private ElementoStatoOperativoVariazione elementoStatoOperativoVariazione;

	/** Costruttore vuoto di default */
	public ConsultaVariazioneCodificheModel() {
		super();
		setTitolo(" Consultazione Variazioni");
	}

	/**
	 * @return the numeroVariazione
	 */
	public Integer getNumeroVariazione() {
		return numeroVariazione;
	}

	/**
	 * @param numeroVariazione the numeroVariazione to set
	 */
	public void setNumeroVariazione(Integer numeroVariazione) {
		this.numeroVariazione = numeroVariazione;
	}

	/**
	 * @return the uidVariazione
	 */
	public Integer getUidVariazione() {
		return uidVariazione;
	}

	/**
	 * @param uidVariazione the uidVariazione to set
	 */
	public void setUidVariazione(Integer uidVariazione) {
		this.uidVariazione = uidVariazione;
	}

	/**
	 * @return the tipoVariazione
	 */
	public TipoVariazione getTipoVariazione() {
		return tipoVariazione;
	}

	/**
	 * @param tipoVariazione the tipoVariazione to set
	 */
	public void setTipoVariazione(TipoVariazione tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}

	/**
	 * @return the applicazioneVariazione
	 */
	public String getApplicazioneVariazione() {
		return applicazioneVariazione;
	}

	/**
	 * @param applicazioneVariazione the applicazioneVariazione to set
	 */
	public void setApplicazioneVariazione(String applicazioneVariazione) {
		this.applicazioneVariazione = applicazioneVariazione;
	}

	/**
	 * @return the descrizioneVariazione
	 */
	public String getDescrizioneVariazione() {
		return descrizioneVariazione;
	}

	/**
	 * @param descrizioneVariazione the descrizioneVariazione to set
	 */
	public void setDescrizioneVariazione(String descrizioneVariazione) {
		this.descrizioneVariazione = descrizioneVariazione;
	}

	/**
	 * @return the statoVariazione
	 */
	public StatoOperativoVariazioneDiBilancio getStatoVariazione() {
		return statoVariazione;
	}

	/**
	 * @param statoVariazione the statoVariazione to set
	 */
	public void setStatoVariazione(
			StatoOperativoVariazioneDiBilancio statoVariazione) {
		this.statoVariazione = statoVariazione;
	}

	/**
	 * @return the noteVariazione
	 */
	public String getNoteVariazione() {
		return noteVariazione;
	}

	/**
	 * @param noteVariazione the noteVariazione to set
	 */
	public void setNoteVariazione(String noteVariazione) {
		this.noteVariazione = noteVariazione;
	}

	/**
	 * @return the uidProvvedimento
	 */
	public Integer getUidProvvedimento() {
		return uidProvvedimento;
	}

	/**
	 * @param uidProvvedimento the uidProvvedimento to set
	 */
	public void setUidProvvedimento(Integer uidProvvedimento) {
		this.uidProvvedimento = uidProvvedimento;
	}

	/**
	 * @return the tipoProvvedimento
	 */
	public String getTipoProvvedimento() {
		return tipoProvvedimento;
	}

	/**
	 * @param tipoProvvedimento the tipoProvvedimento to set
	 */
	public void setTipoProvvedimento(String tipoProvvedimento) {
		this.tipoProvvedimento = tipoProvvedimento;
	}

	/**
	 * @return the annoProvvedimento
	 */
	public Integer getAnnoProvvedimento() {
		return annoProvvedimento;
	}
	
	/**
	 * @param annoProvvedimento the annoProvvedimento to set
	 */
	public void setAnnoProvvedimento(Integer annoProvvedimento) {
		this.annoProvvedimento = annoProvvedimento;
	}

	/**
	 * @return the numeroProvvedimento
	 */
	public Integer getNumeroProvvedimento() {
		return numeroProvvedimento;
	}

	/**
	 * @param numeroProvvedimento the numeroProvvedimento to set
	 */
	public void setNumeroProvvedimento(Integer numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}

	/**
	 * @return the strutturaProvvedimento
	 */
	public String getStrutturaProvvedimento() {
		return strutturaProvvedimento;
	}

	/**
	 * @param strutturaProvvedimento the strutturaProvvedimento to set
	 */
	public void setStrutturaProvvedimento(String strutturaProvvedimento) {
		this.strutturaProvvedimento = strutturaProvvedimento;
	}

	/**
	 * @return the oggettoProvvedimento
	 */
	public String getOggettoProvvedimento() {
		return oggettoProvvedimento;
	}

	/**
	 * @param oggettoProvvedimento the oggettoProvvedimento to set
	 */
	public void setOggettoProvvedimento(String oggettoProvvedimento) {
		this.oggettoProvvedimento = oggettoProvvedimento;
	}


	/**
	 * @return the listaCapitoli
	 */
	public List<ElementoCapitoloCodifiche> getListaCapitoli() {
		return listaCapitoli;
	}

	/**
	 * @param listaCapitoli the listaCapitoli to set
	 */
	public void setListaCapitoli(List<ElementoCapitoloCodifiche> listaCapitoli) {
		this.listaCapitoli = listaCapitoli;
	}
	
	/**
	 * @return the elementoStatoOperativoVariazione
	 */
	public ElementoStatoOperativoVariazione getElementoStatoOperativoVariazione() {
		return elementoStatoOperativoVariazione;
	}

	/**
	 * @param elementoStatoOperativoVariazione the elementoStatoOperativoVariazione to set
	 */
	public void setElementoStatoOperativoVariazione(ElementoStatoOperativoVariazione elementoStatoOperativoVariazione) {
		this.elementoStatoOperativoVariazione = elementoStatoOperativoVariazione;
	}

	/**
	 * Crea una Request per il servizio di {@link RicercaDettaglioVariazioneCodifiche}.
	 *
	 * @return la Request creata
	 */
	public RicercaDettaglioVariazioneCodifiche creaRequestRicercaVariazioneCodifiche() {
		RicercaDettaglioVariazioneCodifiche request = new RicercaDettaglioVariazioneCodifiche();
		request.setDataOra(new Date());
		request.setUidVariazione(uidVariazione);
		request.setRichiedente(getRichiedente());
		return request;
	}
	
	/**
	 * Crea una Request per il servizio di {@link RicercaProvvedimento}.
	 * 
	 * @param response la response da cui ottenere l'atto amministrativo
	 *
	 * @return la Request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento(RicercaDettaglioVariazioneCodificheResponse response) {
		RicercaProvvedimento request = new RicercaProvvedimento();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());

		AttoAmministrativo a = response.getVariazioneCodificaCapitolo().getAttoAmministrativo();
		if(a != null){
			RicercaAtti utility = new RicercaAtti();
			utility.setUid(a.getUid());
			utility.setAnnoAtto(a.getAnno());
			utility.setNumeroAtto(a.getNumero());
			request.setRicercaAtti(utility);
		}
		return request;
	}

	/**
	 * Imposta i dati dalla response e dalla sessione.
	 * 
	 * @param response              la response di ricerca dettaglio
	 * @param responseProvvedimento la response di ricerca del provvedimento
	 * @param sessionHandler        l'handler della sessione
	 */
	public void impostaDatiDaResponseESessione(RicercaDettaglioVariazioneCodificheResponse response, RicercaProvvedimentoResponse responseProvvedimento, SessionHandler sessionHandler) {
		VariazioneCodificaCapitolo variazione = response.getVariazioneCodificaCapitolo();
		listaCapitoli = ElementoCapitoloCodificheFactory.getInstancesFromDettaglio(variazione.getListaDettaglioVariazioneCodifica());
		
		numeroVariazione = variazione.getNumero();
		descrizioneVariazione = variazione.getDescrizione();
		tipoVariazione = variazione.getTipoVariazione();
		
			
		TipoCapitolo tipoCapitolo = listaCapitoli.get(0).getTipoCapitolo();
		String applicazione = (TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE.equals(tipoCapitolo) || TipoCapitolo.CAPITOLO_USCITA_PREVISIONE.equals(tipoCapitolo)) ? "Previsione" : "Gestione";
		
		// Impostazione dei dati nel model
		applicazioneVariazione = applicazione;
		statoVariazione = variazione.getStatoOperativoVariazioneDiBilancio();
		elementoStatoOperativoVariazione = ElementoStatoOperativoVariazioneFactory.getInstance(getEnte().getGestioneLivelli(), statoVariazione);
		noteVariazione = variazione.getNote();
		
		AttoAmministrativo atto = variazione.getAttoAmministrativo();
		if(atto!=null){
			annoProvvedimento = atto.getAnno();
			numeroProvvedimento = atto.getNumero();
			oggettoProvvedimento = atto.getOggetto();
		}
		
		if(!responseProvvedimento.getListaAttiAmministrativi().isEmpty()){
			AttoAmministrativo provvedimentoDaConsultare = responseProvvedimento.getListaAttiAmministrativi().get(0);
			tipoProvvedimento = provvedimentoDaConsultare.getTipoAtto().getCodice()+" - "+provvedimentoDaConsultare.getTipoAtto().getDescrizione();
			StrutturaAmministrativoContabile strutturaAmministrativoContabile = provvedimentoDaConsultare.getStrutturaAmmContabile();
			if(strutturaAmministrativoContabile != null) {
				strutturaProvvedimento = strutturaAmministrativoContabile.getCodice() + " - " + strutturaAmministrativoContabile.getDescrizione();
			}
		}
	}
	
}

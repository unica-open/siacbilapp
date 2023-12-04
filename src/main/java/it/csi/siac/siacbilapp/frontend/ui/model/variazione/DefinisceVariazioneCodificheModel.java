/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodifiche;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodificheFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.VariabileProcesso;

/**
 * Classe di model per la consultazione delle variazioni di codifiche. 
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 05/11/2013
 *
 */

public class DefinisceVariazioneCodificheModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4256856520682050552L;
	
	private VariazioneCodificaCapitolo variazioneCodificaCapitolo;
	
	private Integer numeroVariazione;
	private Boolean definizioneEseguita = Boolean.FALSE;
	private String idAttivita;
	
	private Integer uidVariazione;
	private TipoVariazione tipoVariazione;
	private String applicazioneVariazione;
	private String descrizioneVariazione;
	private StatoOperativoVariazioneBilancio statoVariazione;
	private String noteVariazione;
	
	private Integer uidProvvedimento;
	private String tipoProvvedimento;
	private Integer annoProvvedimento;
	private Integer numeroProvvedimento;
	private String strutturaProvvedimento;
	private String oggettoProvvedimento;
	
	private Boolean daInviareInGiunta;
	private Boolean daInviareInConsiglio;

	private List<ElementoCapitoloCodifiche> listaCapitoli = new ArrayList<ElementoCapitoloCodifiche>();

	/** Costruttore vuoto di default */
	public DefinisceVariazioneCodificheModel() {
		super();
		setTitolo(" Definisci Variazione Codifiche");
	}
	
	



	/**
	 * @return the variazioneCodificaCapitolo
	 */
	public VariazioneCodificaCapitolo getVariazioneCodificaCapitolo() {
		return variazioneCodificaCapitolo;
	}





	/**
	 * @param variazioneCodificaCapitolo the variazioneCodificaCapitolo to set
	 */
	public void setVariazioneCodificaCapitolo(
			VariazioneCodificaCapitolo variazioneCodificaCapitolo) {
		this.variazioneCodificaCapitolo = variazioneCodificaCapitolo;
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
	 * @return the definizioneEseguita
	 */
	public Boolean getDefinizioneEseguita() {
		return definizioneEseguita;
	}





	/**
	 * @param definizioneEseguita the definizioneEseguita to set
	 */
	public void setDefinizioneEseguita(Boolean definizioneEseguita) {
		this.definizioneEseguita = definizioneEseguita;
	}





	/**
	 * @return the idAttivita
	 */
	public String getIdAttivita() {
		return idAttivita;
	}





	/**
	 * @param idAttivita the idAttivita to set
	 */
	public void setIdAttivita(String idAttivita) {
		this.idAttivita = idAttivita;
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
	public StatoOperativoVariazioneBilancio getStatoVariazione() {
		return statoVariazione;
	}





	/**
	 * @param statoVariazione the statoVariazione to set
	 */
	public void setStatoVariazione(
			StatoOperativoVariazioneBilancio statoVariazione) {
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
	 * @return the daInviareInGiunta
	 */
	public Boolean getDaInviareInGiunta() {
		return daInviareInGiunta;
	}





	/**
	 * @param daInviareInGiunta the daInviareInGiunta to set
	 */
	public void setDaInviareInGiunta(Boolean daInviareInGiunta) {
		this.daInviareInGiunta = daInviareInGiunta;
	}





	/**
	 * @return the daInviareInConsiglio
	 */
	public Boolean getDaInviareInConsiglio() {
		return daInviareInConsiglio;
	}





	/**
	 * @param daInviareInConsiglio the daInviareInConsiglio to set
	 */
	public void setDaInviareInConsiglio(Boolean daInviareInConsiglio) {
		this.daInviareInConsiglio = daInviareInConsiglio;
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
	 * Crea una Request per il servizio di {@link DefinisceVariazioneCodifiche}.
	 *
	 * @return la Request creata
	 */
	public DefinisceVariazioneCodifiche creaRequestDefinisceVariazioneCodifiche() {
		DefinisceVariazioneCodifiche request = new DefinisceVariazioneCodifiche();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setIdAttivita(idAttivita);
		
		request.setVariazioneCodificaCapitolo(variazioneCodificaCapitolo);
		
		return request;
	}

	//SIAC-8332
	public void impostaDatiNelModel(AzioneRichiesta azioneRichiesta) {
		if(azioneRichiesta.getIdAttivita()!= null){
			String[] splitted = StringUtils.split(azioneRichiesta.getIdAttivita(), "%&");
			if(splitted.length > 0) {
				uidVariazione = Integer.valueOf(splitted[0]);
			}
		}
	}
	/**
	 * Injetta le variabili del processo.
	 * 
	 * @param azioneRichiesta l'azione richiesta da cui ottenere le variabili di processo
	 */
	public void injettaVariabiliProcesso(AzioneRichiesta azioneRichiesta) {
		VariabileProcesso numeroVariazioneVP = getVariabileProcesso(azioneRichiesta, BilConstants.VARIABILE_PROCESSO_NUMERO_VARIAZIONE);
		String uidVariazioneStringa = ((String)numeroVariazioneVP.getValore()).split("\\|")[0];
		uidVariazione = Integer.valueOf(uidVariazioneStringa);
		idAttivita = azioneRichiesta.getIdAttivita();
		
		VariabileProcesso variabileProcessoDaInviareInGiunta = getVariabileProcesso(azioneRichiesta, BilConstants.VARIABILE_PROCESSO_INVIO_GIUNTA);
		VariabileProcesso variabileProcessoDaInviareInConsiglio = getVariabileProcesso(azioneRichiesta, BilConstants.VARIABILE_PROCESSO_INVIO_CONSIGLIO);
		this.daInviareInGiunta = (Boolean)variabileProcessoDaInviareInGiunta.getValore();
		this.daInviareInGiunta = (Boolean)variabileProcessoDaInviareInConsiglio.getValore();
	}
	
	/**
	 * Injetta i dati della variazione a termine della definizione.
	 * 
	 * @param response la response da cui ottenere i dati per il popolamento
	 */
	public void injettaDatiPostDefinizione(DefinisceVariazioneCodificheResponse response) {
		this.variazioneCodificaCapitolo = response.getVariazioneCodificaCapitolo();
	}
	
	/**
	 * Imposta i dati nel model a partire dalla Response del servizio di {@link RicercaVariazioneBilancioResponse} e dalla sessione.
	 * 
	 * @param response              la Response del servizio
	 * @param responseProvvedimento la Response di ricerca del provvedimento
	 * @param sessionHandler        l'handler per la sessione
	 */
	public void impostaDatiDaResponseESessione(RicercaDettaglioVariazioneCodificheResponse response, RicercaProvvedimentoResponse responseProvvedimento, SessionHandler sessionHandler) {
		VariazioneCodificaCapitolo variazione = response.getVariazioneCodificaCapitolo();
		
		/* Uso per definisci variazione */
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_VARIAZIONI, variazione);
		
		numeroVariazione = variazione.getNumero();
		descrizioneVariazione = variazione.getDescrizione();
		tipoVariazione = variazione.getTipoVariazione();
		
		listaCapitoli = ElementoCapitoloCodificheFactory.getInstancesFromDettaglio(variazione.getListaDettaglioVariazioneCodifica());
			
		TipoCapitolo tipoCapitolo = listaCapitoli.get(0).getTipoCapitolo();
		String applicazione = (TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE.equals(tipoCapitolo) || TipoCapitolo.CAPITOLO_USCITA_PREVISIONE.equals(tipoCapitolo)) ? "Previsione" : "Gestione";
		
		// Impostazione dei dati nel model
		applicazioneVariazione = applicazione;
		statoVariazione = variazione.getStatoOperativoVariazioneDiBilancio();
		noteVariazione = variazione.getNote();
		
		AttoAmministrativo atto = variazione.getAttoAmministrativo();
		if(atto!=null){
			annoProvvedimento = atto.getAnno();
			numeroProvvedimento = atto.getNumero();
			oggettoProvvedimento = atto.getOggetto();
		}
		
		if(!responseProvvedimento.getListaAttiAmministrativi().isEmpty()){
			AttoAmministrativo provvedimentoDaConsultare = responseProvvedimento.getListaAttiAmministrativi().get(0);
			tipoProvvedimento = provvedimentoDaConsultare.getTipoAtto().getCodice();
			StrutturaAmministrativoContabile strutturaAmministrativoContabile = provvedimentoDaConsultare.getStrutturaAmmContabile();
			if(strutturaAmministrativoContabile != null) {
				strutturaProvvedimento = strutturaAmministrativoContabile.getCodice() + " - " + strutturaAmministrativoContabile.getDescrizione();
			}
		}
	}
	
}

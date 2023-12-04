/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.progetto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorDettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorDettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoVersioneCronoprogramma;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoVersioneCronoprogrammaFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.BaseCalcoloProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaAggiorna;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaConsulta;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.ModalitaAffidamentoProgetto;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccommonapp.action.GenericAction;;

/**
 * Classe astratta di model per il Progetto.
 * 
 * @author Osorio Alessandra,Nazha Ahmad 
 * @version 1.0.0 - 04/02/2014
 * @version 1.0.1 - 25/03/2015
 */
public abstract class GenericProgettoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4948730122493670234L;
	
	private Progetto progetto;
	
	
	// Dati provvedimento per il collapse
	private AttoAmministrativo attoAmministrativo;
//	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private Integer uidProvvedimento;
//	private String statoProvvedimento;
//	private TipoAtto tipoAtto;
//	private Integer annoProvvedimento;
//	private Integer numeroProvvedimento;
	
	
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
		
	//Ambito
	private List<TipoAmbito> listaTipiAmbito = new ArrayList<TipoAmbito>();
	
	//SIAC-8900 TipoProgetto
	private List<TipoProgetto> listaTipoProgetto = new ArrayList<TipoProgetto>();

	private List<ElementoVersioneCronoprogramma> listaElementiVersioneCronoprogramma = new ArrayList<ElementoVersioneCronoprogramma>();
	
	//aggiunti ahmad 23/03/2015
	//questa lista e' stata creata in modo // a  listaElementiVersioneCronprogramma in modo da avere la lista delle entita di tipo cronoprogramma(lista filtrata)
	private List<Cronoprogramma> listaCronoprogrammiCollegatiAlProgetto;
	private Cronoprogramma cronoprogrammaDaAggiornare;
	private Cronoprogramma cronoprogrammaDiGestione;
	
	//SIAC-6255
	private List<ModalitaAffidamentoProgetto> listaModalitaAffidamento = new ArrayList<ModalitaAffidamentoProgetto>();
	/**
	 * @return the progetto
	 */
	public Progetto getProgetto() {
		return progetto;
	}

	/**
	 * @param progetto the progetto to set
	 */
	public void setProgetto(Progetto progetto) {
		this.progetto = progetto;
	}

	/**
	 * @return the attoAmministrativo
	 */
	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}

	/**
	 * @param attoAmministrativo the attoAmministrativo to set
	 */
	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
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
	
//	/**
//	 * @return the statoProvvedimento
//	 */
//	public String getStatoProvvedimento() {
//		return statoProvvedimento;
//	}
//
//	/**
//	 * @param statoProvvedimento the statoProvvedimento to set
//	 */
//	public void setStatoProvvedimento(String statoProvvedimento) {
//		this.statoProvvedimento = statoProvvedimento;
//	}

	/**
	 * @return the tipoAtto
	 */
//	public TipoAtto getTipoAtto() {
//		return tipoAtto;
//	}
//
//	/**
//	 * @param tipoAtto the tipoAtto to set
//	 */
//	public void setTipoAtto(TipoAtto tipoAtto) {
//		this.tipoAtto = tipoAtto;
//	}
//	/**
//	 * @return the annoProvvedimento
//	 */
//	public Integer getAnnoProvvedimento() {
//		return annoProvvedimento;
//	}
//
//	/**
//	 * @param annoProvvedimento the annoProvvedimento to set
//	 */
//	public void setAnnoProvvedimento(Integer annoProvvedimento) {
//		this.annoProvvedimento = annoProvvedimento;
//	}

	/**
	 * @return the numeroProvvedimento
	 */
//	public Integer getNumeroProvvedimento() {
//		return numeroProvvedimento;
//	}
//
//	/**
//	 * @param numeroProvvedimento the numeroProvvedimento to set
//	 */
//	public void setNumeroProvvedimento(Integer numeroProvvedimento) {
//		this.numeroProvvedimento = numeroProvvedimento;
//	}

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
	 * @return the listaTipiAmbito
	 */
	public List<TipoAmbito> getListaTipiAmbito() {
		return listaTipiAmbito;
	}


	/**
	 * @param listaTipiAmbito the listaTipiAmbito to set
	 */
	public void setListaTipiAmbito(List<TipoAmbito> listaTipiAmbito) {
		this.listaTipiAmbito = listaTipiAmbito;
	}
	
		
	/**
	 * @return the listaElementiVersioneCronoprogramma
	 */
	public List<ElementoVersioneCronoprogramma> getListaElementiVersioneCronoprogramma() {
		return listaElementiVersioneCronoprogramma;
	}

	/**
	 * @param listaElementiVersioneCronoprogramma the listaElementiVersioneCronoprogramma to set
	 */
	public void setListaElementiVersioneCronoprogramma(List<ElementoVersioneCronoprogramma> listaElementiVersioneCronoprogramma) {
		this.listaElementiVersioneCronoprogramma = listaElementiVersioneCronoprogramma;
	}

	/**
	 * @return the cronoprogrammaDiGestione
	 */
	public Cronoprogramma getCronoprogrammaDiGestione() {
		return cronoprogrammaDiGestione;
	}

	/**
	 * @param cronoprogrammaDiGestione the cronoprogrammaDiGestione to set
	 */
	public void setCronoprogrammaDiGestione(Cronoprogramma cronoprogrammaDiGestione) {
		this.cronoprogrammaDiGestione = cronoprogrammaDiGestione;
	}

	/**
	 * @return the listaCronoprogrammiCollegatiAlProgetto
	 */
	public List<Cronoprogramma> getListaCronoprogrammiCollegatiAlProgetto() {
		return listaCronoprogrammiCollegatiAlProgetto;
	}

	/**
	 * @param listaCronoprogrammiCollegatiAlProgetto the listaCronoprogrammiCollegatiAlProgetto to set
	 */
	public void setListaCronoprogrammiCollegatiAlProgetto(List<Cronoprogramma> listaCronoprogrammiCollegatiAlProgetto) {
		this.listaCronoprogrammiCollegatiAlProgetto = listaCronoprogrammiCollegatiAlProgetto;
	}

	/**
	 * @return the cronoprogrammaDaAggiornare
	 */
	public Cronoprogramma getCronoprogrammaDaAggiornare() {
		return cronoprogrammaDaAggiornare;
	}

	/**
	 * @param cronoprogrammaDaAggiornare the cronoprogrammaDaAggiornare to set
	 */
	public void setCronoprogrammaDaAggiornare(Cronoprogramma cronoprogrammaDaAggiornare) {
		this.cronoprogrammaDaAggiornare = cronoprogrammaDaAggiornare;
	}

	/**
	 * @return the listaModalitaAffidamento
	 */
	public List<ModalitaAffidamentoProgetto> getListaModalitaAffidamento() {
		return listaModalitaAffidamento;
	}

	/**
	 * @param listaModalitaAffidamento the listaModalitaAffidamento to set
	 */
	public void setListaModalitaAffidamento(List<ModalitaAffidamentoProgetto> listaModalitaAffidamento) {
		this.listaModalitaAffidamento = listaModalitaAffidamento;
	}
	

	public List<TipoProgetto> getListaTipoProgetto() {
		return listaTipoProgetto;
	}

	public void setListaTipoProgetto(List<TipoProgetto> listaTipoProgetto) {
		this.listaTipoProgetto = listaTipoProgetto;
	}

	/**
	 * @return the codiceProgettoAutomatico
	 */
	public boolean isCodiceProgettoAutomatico() {
		return isGestioneLivello(TipologiaGestioneLivelli.CODICE_PROGETTO_AUTOMATICO, BilConstants.CODICE_PROGETTO_AUTOMATICO);
	}
	
	
		
	/**
	 * Gets the stringa provvedimento.
	 *
	 * @return the stringa provvedimento
	 */
	public String getStringaProvvedimento(){
		StringBuilder sb = new StringBuilder();
		sb.append("provvedimento");
		
		AttoAmministrativo aa = getAttoAmministrativo();

		if(!idEntitaPresente(aa)){
			return sb.toString();
		}
		
		//i campi dell'atto amministrativo getattoamministrativo().getAnno() e getAttoAmministrativo.getNumero() essendo quelli utilizzati come criteri di ricerca, non sono affidabili 
		//(potrebbero essere stati cancellati dall'utente o dal javascript per pulire). uso i campi impostati inssieme all'uid
		sb.append(": ")
		.append(aa.getAnno()!= 0?  Integer.valueOf(aa.getAnno()).toString() : "")
		.append(" / ")
		.append(aa.getNumero()!= 0? Integer.valueOf(aa.getNumero()).toString() : "");
		
		TipoAtto ta = aa.getTipoAtto();
		
		if(ta == null) {
			return sb.toString();
		}
		sb.append(" / ");
		if(StringUtils.isNotBlank(ta.getCodice()) && StringUtils.isNotBlank(ta.getDescrizione())) {
			//sia codice che descrizione sono popolati: li metto entrambi
			return sb.append(FormatUtils.formatCodificaCodiceDescrizione(ta)).toString();
		}
		//sicuramente uno tra codice e descrizione e' null: popolo quello che non lo e'
		return sb.append(StringUtils.defaultString(ta.getCodice())).append(StringUtils.defaultString(ta.getDescrizione())).toString();
	}
	
	/* Requests */

	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di get Lista Tipi di Ambito
	 * 
	 * @return la request creata
	 */
	public RicercaTipiAmbito creaRequestRicercaTipiAmbito() {
		RicercaTipiAmbito request = creaRequest(RicercaTipiAmbito.class);
		request.setEnte(getEnte());
		request.setAnno(getAnnoEsercizioInt());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDeiCronoprogrammiCollegatiAlProgetto}.
	 * 
	 * @param idProgetto l'id del progetto i cui cronoprogrammi devono essere reperiti
	 * 
	 * @return la request creata
	 */
	public RicercaDeiCronoprogrammiCollegatiAlProgetto creaRequestRicercaDeiCronoprogrammiCollegatiAlProgetto(Integer idProgetto) {
		RicercaDeiCronoprogrammiCollegatiAlProgetto request = creaRequest(RicercaDeiCronoprogrammiCollegatiAlProgetto.class);
		Progetto p = new Progetto();
		p.setUid(idProgetto);
		request.setProgetto(p);
		
		return request;
		
	}
	
	
	/**
	 * Imposta i dati per l'aggiornamento del progetto al model.
	 * 
	 * @param response     la response da cui popolare il model
	 * @param listaCronoprogramma la lista dei cronoprogrammi 
	 * 							 (da cui setto la lista listaElementiVersioneCronoprogramma necessaria
	 * 							  per la pagina di aggiornamento)
	 * 
	 */
	public void impostaDati(RicercaDettaglioProgettoResponse response, List<Cronoprogramma> listaCronoprogramma) {
		progetto = response.getProgetto();
		
		// la lista che utilizzo per popolare i cronoprogrammi nella pagina di aggiornamento
		List<ElementoVersioneCronoprogramma> lista = ElementoVersioneCronoprogrammaFactory.getInstances(listaCronoprogramma, getAnnoEsercizioInt(), progetto.getTipoProgetto());
		setListaElementiVersioneCronoprogramma(lista);

		//l'atto per la pagina di aggiornamento
		setAttoAmministrativo(progetto.getAttoAmministrativo());
	}
	
	/**
	 * Capitalizza la Stringa fornita in input.
	 * 
	 * @param string la stringa da capitalizzare
	 * 
	 * @return la stringa capitalizzata
	 */
	public static String capitaliseString(String string) {
		return StringUtils.capitalize(string.toLowerCase(Locale.ITALIAN));
	}
	
	/**
	 * cronoprogramma di gestione fake cambiare quando ci sara il servizio
	 * 
	 * @return il cronoprogramma di gestione aggiunto
	 */
	public Cronoprogramma aggiungiCronoprogrammaDaGestione() {
		//FIXME QUANDO CI SARA' IL SERVIZIO opportuno questo cronoprogramma finto deve essere modificato
		Cronoprogramma cronoprogrammaGestione= new Cronoprogramma();
		
		cronoprogrammaGestione.setUid(-1);
		cronoprogrammaGestione.setDescrizione("Cronoprogramma ottenuto dagli accertamenti ed impegni afferenti il progetto");
		cronoprogrammaGestione.setCodice("Da Gestione");
		cronoprogrammaGestione.setBilancio(getBilancio());
		cronoprogrammaGestione.setEnte(getEnte());
		cronoprogrammaGestione.setStatoOperativoCronoprogramma(StatoOperativoCronoprogramma.VALIDO);
		return cronoprogrammaGestione;
	}
	/**
	 * Ordina la lista in base all'anno di competenza
	 * 
	 * @param listaDaOrdinare la lista da ordinare
	 */
	public void sortListaDettaglioEntrataByAnnoDiCompetenza(List<DettaglioEntrataCronoprogramma> listaDaOrdinare) {
		if (listaDaOrdinare != null) {
			Collections.sort(listaDaOrdinare, ComparatorDettaglioEntrataCronoprogramma.INSTANCE_ASC);
		}
	}
	
	/**
	 * Ordina la lista in base all'anno di Entrata/spesa
	 * 
	 * @param listaDaOrdinare la lista da ordinare
	 */
	public void sortListaDettaglioUscitaByAnnoEntrataSpesa(List<DettaglioUscitaCronoprogramma> listaDaOrdinare) {
		if (listaDaOrdinare != null) {
			Collections.sort(listaDaOrdinare, ComparatorDettaglioUscitaCronoprogramma.INSTANCE_ASC);
		}
	}
	/**
	 * Ordina le liste di entrata in base all'anno di competenza; Ordina le liste di uscite in base all'anno di entrata/spesa.
	 * 
	 * @param cronoprogrammaDaOrdinare il cronoprogramma da ordinare
	 * 
	 * @return il cronoprogramma ordinato
	 */
	public Cronoprogramma ordinaListeEntrataUscitaCronoprogramma(Cronoprogramma cronoprogrammaDaOrdinare){
		List<DettaglioEntrataCronoprogramma> listaDettaglioEntrata = cronoprogrammaDaOrdinare.getCapitoliEntrata();
		sortListaDettaglioEntrataByAnnoDiCompetenza(listaDettaglioEntrata);
		cronoprogrammaDaOrdinare.setCapitoliEntrata(listaDettaglioEntrata);
		
		List<DettaglioUscitaCronoprogramma> listaDettaglioUscita = cronoprogrammaDaOrdinare.getCapitoliUscita();
		sortListaDettaglioUscitaByAnnoEntrataSpesa(listaDettaglioUscita);
		cronoprogrammaDaOrdinare.setCapitoliUscita(listaDettaglioUscita);
		
		return cronoprogrammaDaOrdinare;
	}
	
	/**
	 * Controlla se il provvedimento sia valorizzato.
	 * 
	 * @return <code>true</code> se il provvedimento &eacute; presente conu uid non zero; <code>false</code> altrimenti
	 */
	public boolean isProvvedimentoValorizzato (){
//		return uidProvvedimento !=null && uidProvvedimento.intValue() > 0;
		return idEntitaPresente(getAttoAmministrativo());
	}
	
	/**
	 * Crea una request per il servizio di Calcolo Prospetto Riassuntivo cronoprogramma di Gestione.
	 * @param uid l'uid per cui creare la richiesta
	 * 
	 * @return la request creata
	 */

	public CalcoloProspettoRiassuntivoCronoprogrammaConsulta creaRequestCalcoloProspettoRiassuntivoCronoprogrammaConsulta(Integer uid) {
		return creaRequestCalcoloProspettoRiassuntivoCronoprogramma(uid, new Progetto(), CalcoloProspettoRiassuntivoCronoprogrammaConsulta.class);
	}	
	
	public CalcoloProspettoRiassuntivoCronoprogrammaAggiorna creaRequestCalcoloProspettoRiassuntivoCronoprogrammaAggiorna(Integer uid) {
		return creaRequestCalcoloProspettoRiassuntivoCronoprogramma(uid, new Progetto(), CalcoloProspettoRiassuntivoCronoprogrammaAggiorna.class);
	}	
	
	protected <T extends BaseCalcoloProspettoRiassuntivoCronoprogramma> T creaRequestCalcoloProspettoRiassuntivoCronoprogramma(Integer uid, 
			Progetto p, Class<T> cls) {
		T request = creaRequest(cls);
	
		
		if (uid != null) {
			p.setUid(uid);
		}
		request.setProgetto(p);
		
		//SIAC-5859: anno del getBilancio()
		request.setAnno(getBilancio().getAnno());
		
		return request;
	}
	
	/**
	 * Crea la request per il servizio {@link RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio}
	 * @param progetto il progetto
	 * @return la request creata
	 */
	public RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio creaRequestRicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio(Progetto progetto) {
		RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio req = creaRequest(RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio.class);
		req.setBilancio(getBilancio());
		
		Progetto p = new Progetto();
		p.setUid(progetto.getUid());
		req.setProgetto(p);
		
		return req;
	}
	
	
}

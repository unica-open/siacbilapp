/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di model per la ricerca della causale EP.
 * 
 * @author Marchino Alessandro
 * @author Simona Paggio
 * @version 1.0.0 - 31/03/2015
 * @version 1.1.0 - 06/10/2015 - adattato per GEN/GSA
 *
 */
public abstract class RicercaCausaleEPBaseModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 702242402937343407L;
	
	private CausaleEP causaleEP;
	private TipoEvento tipoEvento;
	private Evento evento;
	private Conto conto;
	private ElementoPianoDeiConti elementoPianoDeiConti;
	private Soggetto soggetto;
	
	private List<TipoCausale> listaTipoCausale = new ArrayList<TipoCausale>();
	private List<StatoOperativoCausaleEP> listaStatoOperativoCausaleEP = new ArrayList<StatoOperativoCausaleEP>();
	private List<TipoEvento> listaTipoEvento = new ArrayList<TipoEvento>();
	private List<Evento> listaEvento = new ArrayList<Evento>();
	
	/* modale compilazione guidata conto*/
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	
	/** Costruttore vuoto di default */
	public RicercaCausaleEPBaseModel() {
		setTitolo("Ricerca Causale");
	}
	
	/**
	 * Ottiene l'ambito corrispondente: pu&oacute; essere AMBITO_FIN o AMBITO_GSA.
	 * 
	 * @return l'ambito
	 */
	public abstract Ambito getAmbito();

	/**
	 * @return the ambitoFIN
	 */
	public Ambito getAmbitoFIN() {
		return Ambito.AMBITO_FIN;
	}
	
	/**
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		return causaleEP;
	}

	/**
	 * @param causaleEP the causaleEP to set
	 */
	public void setCausaleEP(CausaleEP causaleEP) {
		this.causaleEP = causaleEP;
	}

	/**
	 * @return the tipoEvento
	 */
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	/**
	 * @return the evento
	 */
	public Evento getEvento() {
		return evento;
	}

	/**
	 * @param evento the evento to set
	 */
	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	/**
	 * @return the conto
	 */
	public Conto getConto() {
		return conto;
	}

	/**
	 * @param conto the conto to set
	 */
	public void setConto(Conto conto) {
		this.conto = conto;
	}

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
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the listaTipoCausale
	 */
	public List<TipoCausale> getListaTipoCausale() {
		return listaTipoCausale;
	}

	/**
	 * @param listaTipoCausale the listaTipoCausale to set
	 */
	public void setListaTipoCausale(List<TipoCausale> listaTipoCausale) {
		this.listaTipoCausale = listaTipoCausale != null ? listaTipoCausale : new ArrayList<TipoCausale>();
	}

	/**
	 * @return the listaStatoOperativoCausaleEP
	 */
	public List<StatoOperativoCausaleEP> getListaStatoOperativoCausaleEP() {
		return listaStatoOperativoCausaleEP;
	}

	/**
	 * @param listaStatoOperativoCausaleEP the listaStatoOperativoCausaleEP to set
	 */
	public void setListaStatoOperativoCausaleEP(List<StatoOperativoCausaleEP> listaStatoOperativoCausaleEP) {
		this.listaStatoOperativoCausaleEP = listaStatoOperativoCausaleEP != null ? listaStatoOperativoCausaleEP : new ArrayList<StatoOperativoCausaleEP>();
	}

	/**
	 * @return the listaTipoEvento
	 */
	public List<TipoEvento> getListaTipoEvento() {
		return listaTipoEvento;
	}

	/**
	 * @param listaTipoEvento the listaTipoEvento to set
	 */
	public void setListaTipoEvento(List<TipoEvento> listaTipoEvento) {
		this.listaTipoEvento = listaTipoEvento != null ? listaTipoEvento : new ArrayList<TipoEvento>();
	}

	/**
	 * @return the listaEvento
	 */
	public List<Evento> getListaEvento() {
		return listaEvento;
	}

	/**
	 * @param listaEvento the listaEvento to set
	 */
	public void setListaEvento(List<Evento> listaEvento) {
		this.listaEvento = listaEvento != null ? listaEvento : new ArrayList<Evento>();
	}
	
	/**
	 * @return the listaClassi
	 */
	public final List<ClassePiano> getListaClassi() {
		return listaClassi;
	}

	/**
	 * @param listaClassi the listaClassi to set
	 */
	public final void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi;
	}

	/**
	 * @return the listaTitoloEntrata
	 */
	public final List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public final void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata;
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public final List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public final void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa;
	}

	/**
	 * @return the listaClasseSoggetto
	 */
	public final List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}

	/**
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public final void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto;
	}

	/**
	 * @return the denominazioneSoggetto
	 */
	public String getDenominazioneSoggetto() {
		StringBuilder sb = new StringBuilder();
		if(getSoggetto() != null && StringUtils.isNotBlank(getSoggetto().getCodiceSoggetto())) {
			sb.append(": ")
				.append(getSoggetto().getCognome())
				.append(" ")
				.append(getSoggetto().getNome());
		}
		return sb.toString();
	}

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaConto}.
	 * @param conto il conto per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto(Conto conto) {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		conto.setAmbito(getAmbito());
		request.setBilancio(getBilancio());
		request.setConto(conto);
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(getEnte());
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(getSoggetto().getCodiceSoggetto());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaEventiPerTipo}.
	 * 
	 * @return la request creata
	 */
	public RicercaEventiPerTipo creaRequestRicercaEventiPerTipo() {
		RicercaEventiPerTipo request = creaRequest(RicercaEventiPerTipo.class);
		
		request.setTipoEvento(getTipoEvento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link LeggiElementoPianoDeiContiByCodiceAndAnno}.
	 *
	 * @param elementoPianoDeiConti l'elemento per cui effettuare la ricerca
	 * @return la request creata
	 */
	public LeggiElementoPianoDeiContiByCodiceAndAnno creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno(ElementoPianoDeiConti elementoPianoDeiConti) {
		LeggiElementoPianoDeiContiByCodiceAndAnno request = creaRequest(LeggiElementoPianoDeiContiByCodiceAndAnno.class);
		
		request.setAnno(getAnnoEsercizioInt());
		request.setElementoPianoDeiConti(elementoPianoDeiConti);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaCausale creaRequestRicercaSinteticaCausale() {
		RicercaSinteticaCausale request = creaRequest(RicercaSinteticaCausale.class);
		request.setBilancio(getBilancio());
		
		request.setTipoEvento(impostaEntitaFacoltativa(getTipoEvento()));
		
		getCausaleEP().setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		getCausaleEP().setElementoPianoDeiConti(impostaEntitaFacoltativa(getElementoPianoDeiConti()));
		// Impostazione evento
		if(getEvento() != null && getEvento().getUid() != 0) {
			getCausaleEP().addEvento(getEvento());
		}
		impostaContoTipoOperazione(request);
		getCausaleEP().setAmbito(getAmbito());
		request.setCausaleEP(getCausaleEP());
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}

	/**
	 * Imposta il conto tipo operazione
	 * @param request la request
	 */
	protected void impostaContoTipoOperazione(RicercaSinteticaCausale request) {
		// Impostazione conto
		if(getConto() != null && getConto().getUid() != 0) {
			ContoTipoOperazione contoTipoOperazione = new ContoTipoOperazione();
			contoTipoOperazione.setConto(getConto());
			getCausaleEP().addContoTipoOperazione(contoTipoOperazione);
			
			// Cancello il tipo evento, per velocizzare un po' il servizio
			request.setTipoEvento(null);
		}
	}
	
	/**
	 * Crea la request per il servizio ricerca codifiche per tipoCodifica uguale a ClassePiano
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaClassi(){
		return creaRequestRicercaCodifiche("ClassePiano" + "_" + getAmbito().getSuffix());
	}
	
}

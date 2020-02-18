/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancio;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassiPianoAmmortamento;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.CodiceBilancio;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.TipoConto;
import it.csi.siac.siacgenser.model.TipoLegame;

/**
 * Classe di model di base per l'inserimento e l'aggiornamento del piano dei conti.
 * 
 * @author Valentina Triolo
 * @version 1.0.0
 *
 */
public class BaseInserisciAggiornaPianoDeiContiModel extends GenericBilancioModel{

	private static final long serialVersionUID = -3520073213837887184L;
	
	private Ambito ambito;
	
	private Conto conto;
	private Integer uidTipoCespiti;
	private Integer uidTipoGenerico;
	private Integer uidConto;
	private String codiceContoEditato;
	private Boolean contiFiglioSenzaFigli;
	private CodiceBilancio codiceBilancio;
	private boolean figlioNonDiLegge = false;
	
	private List<TipoConto> listaTipoConto= new ArrayList<TipoConto>();
	private List<CategoriaCespiti> listaCategoriaCespiti= new ArrayList<CategoriaCespiti>();
	private List<TipoLegame> listaTipoLegame= new ArrayList<TipoLegame>();
	private List<CodiceBilancio> listaCodiceBilancio= new ArrayList<CodiceBilancio>();
	private boolean contoFinGuidataDisabled = Boolean.FALSE;
	
	/* modale*/
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	private List<ClassePiano> listaClassiAmmortamento = new ArrayList<ClassePiano>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	/** Costruttore vuoto di default */
	public BaseInserisciAggiornaPianoDeiContiModel(){
		setTitolo("Base Inserisci Aggiorna Piano Dei Conti");
	}

	/**
	 * @return the ambito
	 */
	public Ambito getAmbito() {
		return ambito;
	}

	/**
	 * @param ambito the ambito to set
	 */
	public void setAmbito(Ambito ambito) {
		this.ambito = ambito;
	}
	
	/**
	 * @return the ambitoFIN
	 */
	public Ambito getAmbitoFIN() {
		return Ambito.AMBITO_FIN;
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
	 * @return the uidTipoCespiti
	 */
	public Integer getUidTipoCespiti() {
		return uidTipoCespiti;
	}

	/**
	 * @param uidTipoCespiti the uidTipoCespiti to set
	 */
	public void setUidTipoCespiti(Integer uidTipoCespiti) {
		this.uidTipoCespiti = uidTipoCespiti;
	}
	
	/**
	 * @return the uidTipoGenerico
	 */
	public Integer getUidTipoGenerico() {
		return uidTipoGenerico;
	}

	/**
	 * @param uidTipoGenerico the uidTipoGenerico to set
	 */
	public void setUidTipoGenerico(Integer uidTipoGenerico) {
		this.uidTipoGenerico = uidTipoGenerico;
	}

	/**
	 * @return the uidConto
	 */
	public Integer getUidConto() {
		return uidConto;
	}

	/**
	 * @param uidConto the uidConto to set
	 */
	public void setUidConto(Integer uidConto) {
		this.uidConto = uidConto;
	}
	
	/**
	 * @return the codiceContoEditato
	 */
	public String getCodiceContoEditato() {
		return codiceContoEditato;
	}

	/**
	 * @param codiceContoEditato the codiceContoEditato to set
	 */
	public void setCodiceContoEditato(String codiceContoEditato) {
		this.codiceContoEditato = codiceContoEditato;
	}

	/**
	 * @return the contiFiglioSenzaFigli
	 */
	public Boolean getContiFiglioSenzaFigli() {
		return contiFiglioSenzaFigli;
	}

	/**
	 * @param contiFiglioSenzaFigli the contiFiglioSenzaFigli to set
	 */
	public void setContiFiglioSenzaFigli(Boolean contiFiglioSenzaFigli) {
		this.contiFiglioSenzaFigli = contiFiglioSenzaFigli;
	}
	
	/**
	 * @return the codiceBilancio
	 */
	public CodiceBilancio getCodiceBilancio() {
		return codiceBilancio;
	}

	/**
	 * @param codiceBilancio the codiceBilancio to set
	 */
	public void setCodiceBilancio(CodiceBilancio codiceBilancio) {
		this.codiceBilancio = codiceBilancio;
	}
	/**
	 * @return the figlioNonDiLegge
	 */
	public boolean isFiglioNonDiLegge() {
		return figlioNonDiLegge;
	}
	/**
	 * @param figlioNonDiLegge the figlioNonDiLegge to set
	 */
	public void setFiglioNonDiLegge(boolean figlioNonDiLegge) {
		this.figlioNonDiLegge = figlioNonDiLegge;
	}
	/**
	 * @return the listaTipoConto
	 */
	public List<TipoConto> getListaTipoConto() {
		return listaTipoConto;
	}

	/**
	 * @param listaTipoConto the listaTipoConto to set
	 */
	public void setListaTipoConto(List<TipoConto> listaTipoConto) {
		this.listaTipoConto = listaTipoConto;
	}

	/**
	 * @return the listaCategoriaCespiti
	 */
	public List<CategoriaCespiti> getListaCategoriaCespiti() {
		return listaCategoriaCespiti;
	}

	/**
	 * @param listaCategoriaCespiti the listaCategoriaCespiti to set
	 */
	public void setListaCategoriaCespiti(
			List<CategoriaCespiti> listaCategoriaCespiti) {
		this.listaCategoriaCespiti = listaCategoriaCespiti;
	}

	/**
	 * @return the listaTipoLegame
	 */
	public List<TipoLegame> getListaTipoLegame() {
		return listaTipoLegame;
	}

	/**
	 * @param listaTipoLegame the listaTipoLegame to set
	 */
	public void setListaTipoLegame(List<TipoLegame> listaTipoLegame) {
		this.listaTipoLegame = listaTipoLegame;
	}
	
	
	/**
	 * @return the listaCodiceBilancio
	 */
	public List<CodiceBilancio> getListaCodiceBilancio() {
		return listaCodiceBilancio;
	}

	/**
	 * @param listaCodiceBilancio the listaCodiceBilancio to set
	 */
	public void setListaCodiceBilancio(List<CodiceBilancio> listaCodiceBilancio) {
		this.listaCodiceBilancio = listaCodiceBilancio;
	}

	/**
	 * @return the contoFinGuidataDisabled
	 */
	public final boolean isContoFinGuidataDisabled() {
		return contoFinGuidataDisabled;
	}

	/**
	 * @param contoFinGuidataDisabled the contoFinGuidataDisabled to set
	 */
	public final void setContoFinGuidataDisabled(boolean contoFinGuidataDisabled) {
		this.contoFinGuidataDisabled = contoFinGuidataDisabled;
	}

	/**
	 * @return the listaClassi
	 */
	public List<ClassePiano> getListaClassi() {
		return listaClassi;
	}

	/**
	 * @param listaClassi the listaClassi to set
	 */
	public void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi;
	}
	
	/**
	 * @return the listaClassiAmmortamento
	 */
	public List<ClassePiano> getListaClassiAmmortamento() {
		return listaClassiAmmortamento;
	}

	/**
	 * @param listaClassiAmmortamento the listaClassiAmmortamento to set
	 */
	public void setListaClassiAmmortamento(List<ClassePiano> listaClassiAmmortamento) {
		this.listaClassiAmmortamento = listaClassiAmmortamento;
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
	public List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
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

	/**Crea una request per il servizio di RicercaSoggettoPerChiave
	 * 
	 * @return la request
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave(){
		RicercaSoggettoPerChiave reqSoggetto = new RicercaSoggettoPerChiave();
		reqSoggetto.setEnte(getEnte());
		reqSoggetto.setRichiedente(getRichiedente());
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(getConto().getSoggetto().getCodiceSoggetto());
		reqSoggetto.setParametroSoggettoK(parametroSoggettoK);
		return reqSoggetto;
	}
	
	/**
	 * Imposta a null le coodifiche con uid=0
	 */
	protected void impostaElementoDellaLista(){
		if(conto.getTipoConto() == null || conto.getTipoConto().getUid() == 0){
			conto.setTipoConto(null);
		}else{
			conto.setTipoConto(ComparatorUtils.searchByUid(listaTipoConto, conto.getTipoConto()));
		}
		
		if(conto.getTipoLegame() == null || conto.getTipoLegame().getUid() == 0){
			conto.setTipoLegame(null);
		}else{
			conto.setTipoLegame(ComparatorUtils.searchByUid(listaTipoLegame, conto.getTipoLegame()));
		}
		
		if(conto.getCategoriaCespiti() == null || conto.getCategoriaCespiti().getUid() == 0){
			conto.setCategoriaCespiti(null);
		}else{
			conto.setCategoriaCespiti(ComparatorUtils.searchByUid(listaCategoriaCespiti, conto.getCategoriaCespiti()));
		}
	}
	
	/**
	 * ripulisce per sicurezza i campi che non devon essere salvati nel caso di un conto foglia
	 */
	protected void ripulisciCampiPerContoFoglia(){
		if(!Boolean.TRUE.equals(conto.getContoFoglia())
				&& conto.getLivello() != null
				&& conto.getPianoDeiConti().getClassePiano().getLivelloDiLegge() != null
				&& !conto.getLivello().equals(conto.getPianoDeiConti().getClassePiano().getLivelloDiLegge())){
			conto.setContoAPartite(Boolean.FALSE);
			conto.setCodiceBilancio(null);
			conto.setTipoLegame(null);
			conto.setContoCollegato(null);
			conto.setSoggetto(null);
			conto.setElementoPianoDeiConti(null);
			conto.setCategoriaCespiti(null);
		}
	}
	
	/**Crea una request per il servizio di RicercaCodifiche per TipoConto, TipoLegame e Categoria Cespiti
	 * 
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaCodifiche(){
		return creaRequestRicercaCodifiche(TipoConto.class, CategoriaCespiti.class, TipoLegame.class);
	}
	
	/**Crea una request per il servizio di RicercaConto per il conto collegato
	 * 
	 * @return la request
	 */
	public RicercaSinteticaConto creaRequestRicercaContoCollegato(){
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		request.setParametriPaginazione(creaParametriPaginazione());
		Conto contoCollegato = new Conto();
		contoCollegato.setCodice(conto.getContoCollegato().getCodice());
		contoCollegato.setAmbito(getAmbito());
		request.setConto(contoCollegato);
		request.setBilancio(getBilancio());
		return request;
	}

	/**Crea una request per il servizio di RicercaDettaglioConto
	 * 
	 * @return la request
	 */
	public RicercaDettaglioConto creaRequestRicercaDettaglioConto(){
		RicercaDettaglioConto reqRDC = creaRequest(RicercaDettaglioConto.class);
		Conto c = new Conto();
		c.setUid(uidConto);
		reqRDC.setConto(c);
		reqRDC.setBilancio(getBilancio());
		return reqRDC;
	}
	
	/**Crea una request per il servizio di RicercaDettaglioConto
	 * @param uidClasse l'uid classe
	 * 
	 * @return la request
	 */
	public LeggiTreeCodiceBilancio creaRequestLeggiTreeCodiceBilancio(Integer uidClasse){
		LeggiTreeCodiceBilancio reqLTCB = creaRequest(LeggiTreeCodiceBilancio.class);
		reqLTCB.setAnno(getBilancio().getAnno());
		ClassePiano classePiano = new ClassePiano();
		classePiano.setUid(uidClasse);
		reqLTCB.setClassePiano(classePiano);
		return reqLTCB;
	}
	
	
	/**Crea una request per il servizio di LeggiElementoPianoDeiContiByCodiceAndAnno
	 * 
	 * @return la request
	 */
	public LeggiElementoPianoDeiContiByCodiceAndAnno creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno(){
		LeggiElementoPianoDeiContiByCodiceAndAnno request = creaRequest(LeggiElementoPianoDeiContiByCodiceAndAnno.class);
		request.setAnno(getAnnoEsercizioInt());
		ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		pdc.setCodice(getConto().getElementoPianoDeiConti().getCodice());
		request.setElementoPianoDeiConti(pdc );
		return request;
	}
	
	/**
	 * Crea la request per il servizio ricerca codifiche per tipoCodifica uguale a ClassePiano
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaClassi(){
		String ambitoSuffix = getAmbito().name().split("_")[1];
		return creaRequestRicercaCodifiche("ClassePiano" + ambitoSuffix);
	}
	
	
	/**
	 * Crea la request per il servizio RicercaClassiPianoAmmortamento
	 * @return la request
	 */
	public RicercaClassiPianoAmmortamento creaRequestRicercaClassiPianoAmmortamento(){
		return creaRequest(RicercaClassiPianoAmmortamento.class);
	}
}

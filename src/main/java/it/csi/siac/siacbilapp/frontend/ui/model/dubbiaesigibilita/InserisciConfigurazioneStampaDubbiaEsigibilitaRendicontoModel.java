/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.AggiornaAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.EliminaAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.RipristinaAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportRendiconto;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoImportazione;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.GruppoAzioni;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

/**
 * Classe base di modello per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione, rendiconto
 * 
 * @author Marchino Alessandro
 */
public class InserisciConfigurazioneStampaDubbiaEsigibilitaRendicontoModel extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6054489897303243021L;
	
	private List<CapitoloEntrataGestione> listaCapitoloEntrataGestione = new ArrayList<CapitoloEntrataGestione>();
	private List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
	private List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
	private List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendiconto = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();

	private AccantonamentoFondiDubbiaEsigibilitaRendiconto accantonamento = new AccantonamentoFondiDubbiaEsigibilitaRendiconto();
	
	// SIAC-5304
	private Azione azioneReportistica;
	private GruppoAzioni gruppoAzioniReportistica;
	
	/** Costruttore vuoto di default */
	public InserisciConfigurazioneStampaDubbiaEsigibilitaRendicontoModel(){
		super();
		setTitolo("Gestione fondo crediti dubbia esigibilità - rendiconto");
	}
	

	@Override
	public Collection<TitoloEntrata> getElencoTitoloEntrataAccantonamenti() {
		if(listaAccantonamentoFondiDubbiaEsigibilitaRendiconto == null) {
			return new HashSet<TitoloEntrata>();
		}
		Map<String, TitoloEntrata> res = new HashMap<String, TitoloEntrata>();
		for(AccantonamentoFondiDubbiaEsigibilitaRendiconto acc : listaAccantonamentoFondiDubbiaEsigibilitaRendiconto) {
			CapitoloEntrataGestione capitolo = acc.getCapitolo();
			if(capitolo != null && capitolo.getTitoloEntrata() != null && capitolo.getTitoloEntrata().getCodice() != null) {
				res.put(capitolo.getTitoloEntrata().getCodice(), capitolo.getTitoloEntrata());
			}
		}
		return res.values();
	}
	
	@Override
	public Collection<TipologiaTitolo> getElencoTipologiaTitoloAccantonamenti() {
		if(listaAccantonamentoFondiDubbiaEsigibilitaRendiconto == null) {
			return new HashSet<TipologiaTitolo>();
		}
		Map<String, TipologiaTitolo> res = new HashMap<String, TipologiaTitolo>();
		for(AccantonamentoFondiDubbiaEsigibilitaRendiconto acc : listaAccantonamentoFondiDubbiaEsigibilitaRendiconto) {
			CapitoloEntrataGestione capitolo = acc.getCapitolo();
			if(capitolo != null && capitolo.getTipologiaTitolo() != null && capitolo.getTipologiaTitolo().getCodice() != null) {
				res.put(capitolo.getTipologiaTitolo().getCodice(), capitolo.getTipologiaTitolo());
			}
		}
		return res.values();
	}


	// REQUESTS
	
	/**
	 * Crea una request per il servizio di {@link InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto}.
	 * @return la request creata
	 */
	public InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto creaRequestInserisceFondiDubbiaEsigibilitaRendiconto() {
		InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto request = creaRequest(InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto.class);
		request.setListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati());
		return request;
	}

	// REQUESTS
	
	/**
	 * Crea una request per il servizio di {@link InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto}.
	 * @return la request creata
	 */
	public InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto creaRequestInserisceFondiDubbiaEsigibilita() {
		InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto request = creaRequest(InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto.class);
		request.setListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto}.
	 * @return la request creata
	 * 
	 * SIAC-7858 14/06/2021 CM 
	 */
	public InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto creaRequestCopiaCapitoliModaleAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() {
		InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto request = creaRequest(InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto.class);
		
		request.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		request.setListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp());
	
		return request;
	}
	


	public RicercaAccantonamentoFondiDubbiaEsigibilita creaRequestRicercaAccantonamentoFondiDubbiaEsigibilita() {
		RicercaAccantonamentoFondiDubbiaEsigibilita req = creaRequest(RicercaAccantonamentoFondiDubbiaEsigibilita.class);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		req.setModelDetails(
				AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.CapitoloEntrataGestione,
				AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.TipoMedia,
				AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.StanziamentiCapitolo,
				CapitoloEntrataGestioneModelDetail.TitoloTipologiaCategoriaSAC);
		return req;
	}

	public EliminaAccantonamentoFondiDubbiaEsigibilitaRendiconto creaRequestEliminaAccantonamentoFondiDubbiaEsigibilita() {
		EliminaAccantonamentoFondiDubbiaEsigibilitaRendiconto req = creaRequest(EliminaAccantonamentoFondiDubbiaEsigibilitaRendiconto.class);
		req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto().add(getAccantonamentoByIndice(listaAccantonamentoFondiDubbiaEsigibilitaRendiconto));
		return req;
	}

	public RipristinaAccantonamentoFondiDubbiaEsigibilitaRendiconto creaRequestRipristinaAccantonamentoFondiDubbiaEsigibilita() {
		RipristinaAccantonamentoFondiDubbiaEsigibilitaRendiconto req = creaRequest(RipristinaAccantonamentoFondiDubbiaEsigibilitaRendiconto.class);
		req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto().add(getAccantonamentoByIndice(listaAccantonamentoFondiDubbiaEsigibilitaRendiconto));
		return req;
	}

	public AggiornaAccantonamentoFondiDubbiaEsigibilitaRendiconto creaRequestAggiornaAccantonamentoFondiDubbiaEsigibilita() {
		AggiornaAccantonamentoFondiDubbiaEsigibilitaRendiconto req = creaRequest(AggiornaAccantonamentoFondiDubbiaEsigibilitaRendiconto.class);
		popolaListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(req);
		return req;
	}

	public SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportRendiconto creaRequestSimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport(TipoImportazione tipoImportazione) {
		SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportRendiconto req = creaRequest(SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportRendiconto.class);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioOld(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa());
		req.setTipoImportazione(tipoImportazione);
		return req;
	}
	
	@Override
	public ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestInizializzaioneAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() {
		ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = creaRequest(ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);
		req.setBilancio(getBilancio());
		req.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO);
		req.setStatoAccantonamentoFondiDubbiaEsigibilita(StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails(
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.values()
			);
		return req;
	}

	/**
	 * Rimozione dei capitoli dalla lista dei remporanei
	 */
	public void rimuoviCapitoliDaTemp() {
		rimuoviCapitoliDaTemp(getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp(), getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati());
	}
	
	@Override
	protected void setDefaultValuesAttributiBilancio() {
		super.setDefaultValuesAttributiBilancio();
		// CAmpi aggiuntivi non presenti su interfaccia
		if(getAttributiBilancio().getPercentualeAccantonamentoAnno() == null) {
			getAttributiBilancio().setPercentualeAccantonamentoAnno(BigDecimal.ZERO);
		}
		if(getAttributiBilancio().getPercentualeAccantonamentoAnno1() == null) {
			getAttributiBilancio().setPercentualeAccantonamentoAnno1(BigDecimal.ZERO);
		}
		if(getAttributiBilancio().getPercentualeAccantonamentoAnno2() == null) {
			getAttributiBilancio().setPercentualeAccantonamentoAnno2(BigDecimal.ZERO);
		}
	}

	@Override
	public RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestRicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() {
		RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio request = creaRequest(RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);

		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio afdeab = new AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		afdeab.setBilancio(getBilancio());
		afdeab.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO);
		
		// se ho una versione sono nel caso del reset
		if(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() != null 
				&& getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getVersione() != null) {
			afdeab.setVersione(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getVersione());
		}

		request.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(afdeab);
		request.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails(
			AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato
		);
		
		return request;
	}

	@Override
	public RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestRicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() {
		RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = creaRequest(RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);
		
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio afdeab = new AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		afdeab.setBilancio(getBilancio());
		afdeab.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO);
		
		if(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() != null 
				&& getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getVersione() != null) {
			afdeab.setVersione(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getVersione());
		}

		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(afdeab);

		req.setExcludeCurrent(Boolean.TRUE);
		req.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato);
		return req;
	}
	
	@Override
	protected void impostaAttributiBilancio(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributi) {
		attributi.setBilancio(getBilancio());
		attributi.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO);
		attributi.setStatoAccantonamentoFondiDubbiaEsigibilita(StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
		attributi.setAccantonamentoGraduale(new BigDecimal(100));
		attributi.setQuinquennioRiferimento(getBilancio().getAnno());
		attributi.setRiscossioneVirtuosa(Boolean.FALSE);
		attributi.setCreditiStralciati(null);
		attributi.setCreditiStralciatiFcde(null);
		attributi.setAccertamentiAnniSuccessivi(null);
		attributi.setAccertamentiAnniSuccessiviFcde(null);
		attributi.setVersione(null);
	}
	
	
	private void popolaListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(AggiornaAccantonamentoFondiDubbiaEsigibilitaRendiconto req) {
		req.setListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(popolaListaDatiSalvataggio(new AccantonamentoFondiDubbiaEsigibilitaRendiconto(), new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>()));
	}
	
	// ============================================================================ GETTER & SETTER =================================================================================================
	
	/**
	 * @return the listaCapitoloEntrataGestione
	 */
	public List<CapitoloEntrataGestione> getListaCapitoloEntrataGestione() {
		return listaCapitoloEntrataGestione;
	}

	/**
	 * @param listaCapitoloEntrataGestione the listaCapitoloEntrataGestione to set
	 */
	public void setListaCapitoloEntrataGestione(List<CapitoloEntrataGestione> listaCapitoloEntrataGestione) {
		this.listaCapitoloEntrataGestione = listaCapitoloEntrataGestione != null ? listaCapitoloEntrataGestione : new ArrayList<CapitoloEntrataGestione>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp
	 */
	public List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp() {
		return listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp the listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp(List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp = listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp != null ? listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp : new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati
	 */
	public List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati() {
		return listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati the listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati(List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati = listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati != null ? listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati : new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaRendiconto
	 */
	public List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto() {
		return listaAccantonamentoFondiDubbiaEsigibilitaRendiconto;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaRendiconto the listaAccantonamentoFondiDubbiaEsigibilitaRendiconto to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendiconto) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaRendiconto = listaAccantonamentoFondiDubbiaEsigibilitaRendiconto != null ? listaAccantonamentoFondiDubbiaEsigibilitaRendiconto : new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
	}

	/**
	 * @return the accantonamento
	 */
	public AccantonamentoFondiDubbiaEsigibilitaRendiconto getAccantonamento() {
		return accantonamento;
	}

	/**
	 * @param accantonamento the accantonamento to set
	 */
	public void setAccantonamento(AccantonamentoFondiDubbiaEsigibilitaRendiconto accantonamento) {
		this.accantonamento = accantonamento;
	}

	/**
	 * @return the azioneReportistica
	 */
	public Azione getAzioneReportistica() {
		return azioneReportistica;
	}

	/**
	 * @param azioneReportistica the azioneReportistica to set
	 */
	public void setAzioneReportistica(Azione azioneReportistica) {
		this.azioneReportistica = azioneReportistica;
	}

	/**
	 * @return the gruppoAzioniReportistica
	 */
	public GruppoAzioni getGruppoAzioniReportistica() {
		return gruppoAzioniReportistica;
	}

	/**
	 * @param gruppoAzioniReportistica the gruppoAzioniReportistica to set
	 */
	public void setGruppoAzioniReportistica(GruppoAzioni gruppoAzioniReportistica) {
		this.gruppoAzioniReportistica = gruppoAzioniReportistica;
	}
	
	
}

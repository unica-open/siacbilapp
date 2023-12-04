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
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.AggiornaAccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.EliminaAccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.InserisceAccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.RipristinaAccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoImportazione;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.GruppoAzioni;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

/**
 * Classe base di modello per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione, Gestione
 * 
 * @author Marchino Alessandro
 */
public class InserisciConfigurazioneStampaDubbiaEsigibilitaGestioneModel extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6054489897303243021L;
	
	private List<CapitoloEntrataGestione> listaCapitoloEntrataGestione = new ArrayList<CapitoloEntrataGestione>();
	private List<AccantonamentoFondiDubbiaEsigibilitaGestione> listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaGestione>();
	private List<AccantonamentoFondiDubbiaEsigibilitaGestione> listaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaGestione>();
	private List<AccantonamentoFondiDubbiaEsigibilitaGestione> listaAccantonamentoFondiDubbiaEsigibilitaGestione = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaGestione>();

	private AccantonamentoFondiDubbiaEsigibilitaGestione accantonamento = new AccantonamentoFondiDubbiaEsigibilitaGestione();
	
	// SIAC-5304
	private Azione azioneReportistica;
	private GruppoAzioni gruppoAzioniReportistica;
	
	/** Costruttore vuoto di default */
	public InserisciConfigurazioneStampaDubbiaEsigibilitaGestioneModel(){
		super();
		setTitolo("Gestione fondo crediti dubbia esigibilità - Gestione");
	}
	

	@Override
	public Collection<TitoloEntrata> getElencoTitoloEntrataAccantonamenti() {
		if(listaAccantonamentoFondiDubbiaEsigibilitaGestione == null) {
			return new HashSet<TitoloEntrata>();
		}
		Map<String, TitoloEntrata> res = new HashMap<String, TitoloEntrata>();
		for(AccantonamentoFondiDubbiaEsigibilitaGestione acc : listaAccantonamentoFondiDubbiaEsigibilitaGestione) {
			CapitoloEntrataGestione capitolo = acc.getCapitolo();
			if(capitolo != null && capitolo.getTitoloEntrata() != null && capitolo.getTitoloEntrata().getCodice() != null) {
				res.put(capitolo.getTitoloEntrata().getCodice(), capitolo.getTitoloEntrata());
			}
		}
		return res.values();
	}
	
	@Override
	public Collection<TipologiaTitolo> getElencoTipologiaTitoloAccantonamenti() {
		if(listaAccantonamentoFondiDubbiaEsigibilitaGestione == null) {
			return new HashSet<TipologiaTitolo>();
		}
		Map<String, TipologiaTitolo> res = new HashMap<String, TipologiaTitolo>();
		for(AccantonamentoFondiDubbiaEsigibilitaGestione acc : listaAccantonamentoFondiDubbiaEsigibilitaGestione) {
			CapitoloEntrataGestione capitolo = acc.getCapitolo();
			if(capitolo != null && capitolo.getTipologiaTitolo() != null && capitolo.getTipologiaTitolo().getCodice() != null) {
				res.put(capitolo.getTipologiaTitolo().getCodice(), capitolo.getTipologiaTitolo());
			}
		}
		return res.values();
	}


	// REQUESTS
	
	/**
	 * Crea una request per il servizio di {@link InserisceAccantonamentoFondiDubbiaEsigibilitaGestione}.
	 * @return la request creata
	 */
	public InserisceAccantonamentoFondiDubbiaEsigibilitaGestione creaRequestInserisceFondiDubbiaEsigibilitaGestione() {
		InserisceAccantonamentoFondiDubbiaEsigibilitaGestione request = creaRequest(InserisceAccantonamentoFondiDubbiaEsigibilitaGestione.class);
		request.setListaAccantonamentoFondiDubbiaEsigibilitaGestione(getListaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati());
		return request;
	}

	// REQUESTS
	
	/**
	 * Crea una request per il servizio di {@link InserisceAccantonamentoFondiDubbiaEsigibilitaGestione}.
	 * @return la request creata
	 */
	public InserisceAccantonamentoFondiDubbiaEsigibilitaGestione creaRequestInserisceFondiDubbiaEsigibilita() {
		InserisceAccantonamentoFondiDubbiaEsigibilitaGestione request = creaRequest(InserisceAccantonamentoFondiDubbiaEsigibilitaGestione.class);
		request.setListaAccantonamentoFondiDubbiaEsigibilitaGestione(getListaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceAccantonamentoFondiDubbiaEsigibilitaGestione}.
	 * @return la request creata
	 * 
	 * SIAC-7858 14/06/2021 CM 
	 */
	public InserisceAccantonamentoFondiDubbiaEsigibilitaGestione creaRequestCopiaCapitoliModaleAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() {
		InserisceAccantonamentoFondiDubbiaEsigibilitaGestione request = creaRequest(InserisceAccantonamentoFondiDubbiaEsigibilitaGestione.class);
		
		request.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		request.setAnnoBilancio(getAnnoEsercizioInt());
		request.setListaAccantonamentoFondiDubbiaEsigibilitaGestione(getListaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp());
	
		return request;
	}
	


	public RicercaAccantonamentoFondiDubbiaEsigibilita creaRequestRicercaAccantonamentoFondiDubbiaEsigibilita() {
		RicercaAccantonamentoFondiDubbiaEsigibilita req = creaRequest(RicercaAccantonamentoFondiDubbiaEsigibilita.class);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		req.setModelDetails(
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.CapitoloEntrataGestione,
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.TipoMedia,
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.StanziamentiCapitolo,
				AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.TipoMediaConfronto,
				CapitoloEntrataGestioneModelDetail.TitoloTipologiaCategoriaSAC);
		return req;
	}

	public EliminaAccantonamentoFondiDubbiaEsigibilitaGestione creaRequestEliminaAccantonamentoFondiDubbiaEsigibilita() {
		EliminaAccantonamentoFondiDubbiaEsigibilitaGestione req = creaRequest(EliminaAccantonamentoFondiDubbiaEsigibilitaGestione.class);
		req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione().add(getAccantonamentoByIndice(listaAccantonamentoFondiDubbiaEsigibilitaGestione));
		return req;
	}

	public RipristinaAccantonamentoFondiDubbiaEsigibilitaGestione creaRequestRipristinaAccantonamentoFondiDubbiaEsigibilita() {
		RipristinaAccantonamentoFondiDubbiaEsigibilitaGestione req = creaRequest(RipristinaAccantonamentoFondiDubbiaEsigibilitaGestione.class);
		req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione().add(getAccantonamentoByIndice(listaAccantonamentoFondiDubbiaEsigibilitaGestione));
		return req;
	}

	public AggiornaAccantonamentoFondiDubbiaEsigibilitaGestione creaRequestAggiornaAccantonamentoFondiDubbiaEsigibilita() {
		AggiornaAccantonamentoFondiDubbiaEsigibilitaGestione req = creaRequest(AggiornaAccantonamentoFondiDubbiaEsigibilitaGestione.class);
		popolaListaAccantonamentoFondiDubbiaEsigibilitaGestione(req);
		return req;
	}

	public SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione creaRequestSimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport(TipoImportazione tipoImportazione) {
		SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione req = creaRequest(SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione.class);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioOld(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa());
		req.setTipoImportazione(tipoImportazione);
		return req;
	}
	
	@Override
	public ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestInizializzaioneAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() {
		ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = creaRequest(ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);
		req.setBilancio(getBilancio());
		req.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE);
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
		rimuoviCapitoliDaTemp(getListaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp(), getListaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati());
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
		afdeab.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE);

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
		afdeab.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE);

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
		attributi.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE);
		attributi.setStatoAccantonamentoFondiDubbiaEsigibilita(StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
		attributi.setAccantonamentoGraduale(new BigDecimal(100));
		//SIAC-8754
		attributi.setQuinquennioRiferimento(getBilancio().getAnno());
		attributi.setRiscossioneVirtuosa(Boolean.FALSE);
		attributi.setCreditiStralciati(null);
		attributi.setCreditiStralciatiFcde(null);
		attributi.setAccertamentiAnniSuccessivi(null);
		attributi.setAccertamentiAnniSuccessiviFcde(null);
		attributi.setVersione(null);
	}
	
	
	private void popolaListaAccantonamentoFondiDubbiaEsigibilitaGestione(AggiornaAccantonamentoFondiDubbiaEsigibilitaGestione req) {
		req.setListaAccantonamentoFondiDubbiaEsigibilitaGestione(popolaListaDatiSalvataggio(new AccantonamentoFondiDubbiaEsigibilitaGestione(), new ArrayList<AccantonamentoFondiDubbiaEsigibilitaGestione>()));
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
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp
	 */
	public List<AccantonamentoFondiDubbiaEsigibilitaGestione> getListaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp() {
		return listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp the listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp(List<AccantonamentoFondiDubbiaEsigibilitaGestione> listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp = listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp != null ? listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp : new ArrayList<AccantonamentoFondiDubbiaEsigibilitaGestione>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati
	 */
	public List<AccantonamentoFondiDubbiaEsigibilitaGestione> getListaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati() {
		return listaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati the listaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati(List<AccantonamentoFondiDubbiaEsigibilitaGestione> listaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati = listaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati != null ? listaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati : new ArrayList<AccantonamentoFondiDubbiaEsigibilitaGestione>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaGestione
	 */
	public List<AccantonamentoFondiDubbiaEsigibilitaGestione> getListaAccantonamentoFondiDubbiaEsigibilitaGestione() {
		return listaAccantonamentoFondiDubbiaEsigibilitaGestione;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaGestione the listaAccantonamentoFondiDubbiaEsigibilitaGestione to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaGestione(List<AccantonamentoFondiDubbiaEsigibilitaGestione> listaAccantonamentoFondiDubbiaEsigibilitaGestione) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaGestione = listaAccantonamentoFondiDubbiaEsigibilitaGestione != null ? listaAccantonamentoFondiDubbiaEsigibilitaGestione : new ArrayList<AccantonamentoFondiDubbiaEsigibilitaGestione>();
	}

	/**
	 * @return the accantonamento
	 */
	public AccantonamentoFondiDubbiaEsigibilitaGestione getAccantonamento() {
		return accantonamento;
	}

	/**
	 * @param accantonamento the accantonamento to set
	 */
	public void setAccantonamento(AccantonamentoFondiDubbiaEsigibilitaGestione accantonamento) {
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

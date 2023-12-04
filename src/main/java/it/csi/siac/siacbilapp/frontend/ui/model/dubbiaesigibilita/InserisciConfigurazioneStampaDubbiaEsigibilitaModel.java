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
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.InserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.AggiornaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.EliminaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.InserisceAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.RipristinaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoImportazione;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataPrevisioneModelDetail;

/**
 * Classe di modello per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione
 * 
 * @author Alessio Romanato
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/10/2016
 */
public class InserisciConfigurazioneStampaDubbiaEsigibilitaModel extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6728629980362725537L;
	
	private List<CapitoloEntrataPrevisione> listaCapitoloEntrataPrevisione = new ArrayList<CapitoloEntrataPrevisione>();
	private List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilitaTemp = new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	private List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilitaSelezionati = new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	private List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilita = new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	
	private Integer numeroAccantonamentiDefinitivi;

	/** Costruttore vuoto di default */
	public InserisciConfigurazioneStampaDubbiaEsigibilitaModel(){
		super();
		setTitolo("Gestione fondo crediti dubbia esigibilità - previsione");
	}
	
	/**
	 * @return the listaCapitoloEntrataPrevisione
	 */
	public List<CapitoloEntrataPrevisione> getListaCapitoloEntrataPrevisione() {
		return listaCapitoloEntrataPrevisione;
	}

	/**
	 * @param listaCapitoloEntrataPrevisione the listaCapitoloEntrataPrevisione to set
	 */
	public void setListaCapitoloEntrataPrevisione(List<CapitoloEntrataPrevisione> listaCapitoloEntrataPrevisione) {
		this.listaCapitoloEntrataPrevisione = listaCapitoloEntrataPrevisione != null ? listaCapitoloEntrataPrevisione : new ArrayList<CapitoloEntrataPrevisione>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaTemp
	 */
	public List<AccantonamentoFondiDubbiaEsigibilita> getListaAccantonamentoFondiDubbiaEsigibilitaTemp() {		
		return listaAccantonamentoFondiDubbiaEsigibilitaTemp;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaTemp the listaAccantonamentoFondiDubbiaEsigibilitaTemp to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaTemp(List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilitaTemp) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaTemp = listaAccantonamentoFondiDubbiaEsigibilitaTemp != null ? listaAccantonamentoFondiDubbiaEsigibilitaTemp : new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaSelezionati
	 */
	public List<AccantonamentoFondiDubbiaEsigibilita> getListaAccantonamentoFondiDubbiaEsigibilitaSelezionati() {
		return listaAccantonamentoFondiDubbiaEsigibilitaSelezionati;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaSelezionati the listaAccantonamentoFondiDubbiaEsigibilitaSelezionati to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaSelezionati(List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilitaSelezionati) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaSelezionati = listaAccantonamentoFondiDubbiaEsigibilitaSelezionati != null ? listaAccantonamentoFondiDubbiaEsigibilitaSelezionati : new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilita
	 */
	public List<AccantonamentoFondiDubbiaEsigibilita> getListaAccantonamentoFondiDubbiaEsigibilita() {
		return listaAccantonamentoFondiDubbiaEsigibilita;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilita the listaAccantonamentoFondiDubbiaEsigibilita to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilita(List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilita) {
		this.listaAccantonamentoFondiDubbiaEsigibilita = listaAccantonamentoFondiDubbiaEsigibilita != null ? listaAccantonamentoFondiDubbiaEsigibilita : new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	}

	/**
	 * @return the numeroAccantonamentiDefinitivi
	 */
	public Integer getNumeroAccantonamentiDefinitivi() {
		return this.numeroAccantonamentiDefinitivi;
	}

	/**
	 * @param numeroAccantonamentiDefinitivi the numeroAccantonamentiDefinitivi to set
	 */
	public void setNumeroAccantonamentiDefinitivi(Integer numeroAccantonamentiDefinitivi) {
		this.numeroAccantonamentiDefinitivi = numeroAccantonamentiDefinitivi;
	}

	@Override
	public Collection<TitoloEntrata> getElencoTitoloEntrataAccantonamenti() {
		if(listaAccantonamentoFondiDubbiaEsigibilita == null) {
			return new HashSet<TitoloEntrata>();
		}
		Map<String, TitoloEntrata> res = new HashMap<String, TitoloEntrata>();
		for(AccantonamentoFondiDubbiaEsigibilita acc : listaAccantonamentoFondiDubbiaEsigibilita) {
			CapitoloEntrataPrevisione capitolo = acc.getCapitolo();
			if(capitolo != null && capitolo.getTitoloEntrata() != null && capitolo.getTitoloEntrata().getCodice() != null) {
				res.put(capitolo.getTitoloEntrata().getCodice(), capitolo.getTitoloEntrata());
			}
		}
		return res.values();
	}
	
	@Override
	public Collection<TipologiaTitolo> getElencoTipologiaTitoloAccantonamenti() {
		if(listaAccantonamentoFondiDubbiaEsigibilita == null) {
			return new HashSet<TipologiaTitolo>();
		}
		Map<String, TipologiaTitolo> res = new HashMap<String, TipologiaTitolo>();
		for(AccantonamentoFondiDubbiaEsigibilita acc : listaAccantonamentoFondiDubbiaEsigibilita) {
			CapitoloEntrataPrevisione capitolo = acc.getCapitolo();
			if(capitolo != null && capitolo.getTipologiaTitolo() != null && capitolo.getTipologiaTitolo().getCodice() != null) {
				res.put(capitolo.getTipologiaTitolo().getCodice(), capitolo.getTipologiaTitolo());
			}
		}
		return res.values();
	}

	// REQUESTS
	
	/**
	 * Crea una request per il servizio di {@link InserisceAccantonamentoFondiDubbiaEsigibilita}.
	 * @return la request creata
	 */
	public InserisceAccantonamentoFondiDubbiaEsigibilita creaRequestInserisceFondiDubbiaEsigibilita() {
		InserisceAccantonamentoFondiDubbiaEsigibilita request = creaRequest(InserisceAccantonamentoFondiDubbiaEsigibilita.class);
		request.setListaAccantonamentoFondiDubbiaEsigibilita(getListaAccantonamentoFondiDubbiaEsigibilitaSelezionati());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio}.
	 * @param stato lo statoda applicare
	 * @return la request creata
	 * 
	 * SIAC-7858 04/06/2021 CM 
	 */
	@Override
	public ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(StatoAccantonamentoFondiDubbiaEsigibilita stato) {
		ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio request = creaRequest(ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);
		
		request.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(new AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		request.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setUid(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid());
		request.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setStatoAccantonamentoFondiDubbiaEsigibilita(stato);

		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione}.
	 * @return la request creata
	 * 
	 * SIAC-7858 14/06/2021 CM 
	 */
	public InserisceAccantonamentoFondiDubbiaEsigibilita creaRequestCopiaCapitoliModaleAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() {
		InserisceAccantonamentoFondiDubbiaEsigibilita request = creaRequest(InserisceAccantonamentoFondiDubbiaEsigibilita.class);
		
		request.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		request.setListaAccantonamentoFondiDubbiaEsigibilita(getListaAccantonamentoFondiDubbiaEsigibilitaTemp());
	
		return request;
	}
	
	public RicercaAccantonamentoFondiDubbiaEsigibilita creaRequestRicercaAccantonamentoFondiDubbiaEsigibilita() {
		RicercaAccantonamentoFondiDubbiaEsigibilita req = creaRequest(RicercaAccantonamentoFondiDubbiaEsigibilita.class);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		req.setModelDetails(
				AccantonamentoFondiDubbiaEsigibilitaModelDetail.CapitoloEntrataPrevisione,
				AccantonamentoFondiDubbiaEsigibilitaModelDetail.TipoMedia,
				AccantonamentoFondiDubbiaEsigibilitaModelDetail.StanziamentiCapitolo,
				CapitoloEntrataPrevisioneModelDetail.TitoloTipologiaCategoriaSAC);
		return req;
	}

	public EliminaAccantonamentoFondiDubbiaEsigibilita creaRequestEliminaAccantonamentoFondiDubbiaEsigibilita() {
		EliminaAccantonamentoFondiDubbiaEsigibilita req = creaRequest(EliminaAccantonamentoFondiDubbiaEsigibilita.class);
		req.getListaAccantonamentoFondiDubbiaEsigibilita().add(getAccantonamentoByIndice(listaAccantonamentoFondiDubbiaEsigibilita));
		return req;
	}

	public RipristinaAccantonamentoFondiDubbiaEsigibilita creaRequestRipristinaAccantonamentoFondiDubbiaEsigibilita() {
		RipristinaAccantonamentoFondiDubbiaEsigibilita req = creaRequest(RipristinaAccantonamentoFondiDubbiaEsigibilita.class);
		req.getListaAccantonamentoFondiDubbiaEsigibilita().add(getAccantonamentoByIndice(listaAccantonamentoFondiDubbiaEsigibilita));
		return req;
	}

	public AggiornaAccantonamentoFondiDubbiaEsigibilita creaRequestAggiornaAccantonamentoFondiDubbiaEsigibilita() {
		AggiornaAccantonamentoFondiDubbiaEsigibilita req = creaRequest(AggiornaAccantonamentoFondiDubbiaEsigibilita.class);
		popolaListaAccantonamentoFondiDubbiaEsigibilita(req);
		return req;
	}

	public SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport creaRequestSimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport(TipoImportazione tipoImportazione) {
		SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport req = creaRequest(SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport.class);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioOld(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa());
		req.setTipoImportazione(tipoImportazione);
		return req;
	}
	
	@Override
	public ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestInizializzaioneAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() {
		ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = creaRequest(ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);
		req.setBilancio(getBilancio());
		req.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE);
		req.setStatoAccantonamentoFondiDubbiaEsigibilita(StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails(
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.values()
			);
		return req;
	}

	@Override
	public RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestRicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() {
		RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio request = creaRequest(RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);

		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio afdeab = new AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		afdeab.setBilancio(getBilancio());
		afdeab.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE);

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
		afdeab.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE);
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(afdeab);

		if(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() != null 
				&& getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getVersione() != null) {
			afdeab.setVersione(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getVersione());
		}
		
		req.setExcludeCurrent(Boolean.TRUE);
		req.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato);
		return req;
	}
	
	/**
	 * Ricerca sintatica dell'accantonamento definitivo
	 * @return la request creata
	 */
	public RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio creaRequestRicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDefinitivi() {
		RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = creaRequest(RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class);
		
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio afdeab = new AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		afdeab.setBilancio(getBilancio());
		afdeab.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE);
		afdeab.setVersione(getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getVersione());
		afdeab.setStatoAccantonamentoFondiDubbiaEsigibilita(StatoAccantonamentoFondiDubbiaEsigibilita.DEFINITIVA);
		
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(afdeab);
		req.setExcludeCurrent(Boolean.TRUE);
		req.setParametriPaginazione(creaParametriPaginazione(1));
		// Request no model details
		req.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails();

		return req;
	}
	
	@Override
	protected void impostaAttributiBilancio(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributi) {
		attributi.setBilancio(getBilancio());
		attributi.setTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE);
		attributi.setStatoAccantonamentoFondiDubbiaEsigibilita(StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
		attributi.setAccantonamentoGraduale(new BigDecimal(100));
		attributi.setQuinquennioRiferimento(getBilancio().getAnno() - 1);
		attributi.setRiscossioneVirtuosa(Boolean.FALSE);
		attributi.setCreditiStralciati(null);
		attributi.setCreditiStralciatiFcde(null);
		attributi.setAccertamentiAnniSuccessivi(null);
		attributi.setAccertamentiAnniSuccessiviFcde(null);
		attributi.setVersione(null);
	}
	
	private void popolaListaAccantonamentoFondiDubbiaEsigibilita(AggiornaAccantonamentoFondiDubbiaEsigibilita req) {
		req.setListaAccantonamentoFondiDubbiaEsigibilita(popolaListaDatiSalvataggio(new AccantonamentoFondiDubbiaEsigibilita(), new ArrayList<AccantonamentoFondiDubbiaEsigibilita>()));
	}
}
